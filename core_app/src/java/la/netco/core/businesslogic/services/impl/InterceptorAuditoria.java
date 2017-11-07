package la.netco.core.businesslogic.services.impl;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.persistencia.dao.impl.Auditable;
import la.netco.core.persistencia.vo.Auditoria;
import la.netco.core.persistencia.vo.EntidadAuditada;
import la.netco.core.spring.security.UserDetailsUtils;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service("interceptorAuditoria")
public class InterceptorAuditoria implements AfterReturningAdvice {

	
	private ServiceDao serviceDao;
	private boolean auditoriaActivada;
	private static Logger log = LoggerFactory.getLogger(InterceptorAuditoria.class);

	public void afterReturning(Object  returnValue, Method metodo, Object[] args, Object target) throws Throwable {
	
		try {
		if(auditoriaActivada){
			if(args.length < 2)
				return;
			
				Object object = args[1];
				if(object instanceof Serializable &&  !(object instanceof Auditoria)){
					Map<Integer, String> params = new HashMap<Integer, String>();
					params.put(0, object.getClass().getName());
					EntidadAuditada entidadAuditada = (EntidadAuditada) serviceDao.getGenericCommonDao().executeUniqueResult(EntidadAuditada.NAMED_QUERY_GET_ENTIDAD_BY_ENTITY_CLASS, params);
				   
					
					if(entidadAuditada != null && entidadAuditada.getAuditoriaActiva().equals(EntidadAuditada.ESTADO_ACTIVA)){
				    	
				    	Auditoria auditoria = new Auditoria();
				    	
				    	auditoria.setEntidadAuditada(entidadAuditada);
				    	auditoria.setFechaRegistro(new Date(System.currentTimeMillis()));
				    	auditoria.setUsername(UserDetailsUtils.usernameLogged());
				    	Map<String, String> requestHeaders = FacesContext.getCurrentInstance().getExternalContext().getRequestHeaderMap();
				    	HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
				    	auditoria.setIdioma(requestHeaders.get("accept-language"));
				    	auditoria.setIpHost(httpServletRequest.getRemoteAddr());
				    	auditoria.setNombreHost(httpServletRequest.getRemoteHost());
				    	auditoria.setOperacion(metodo.getAnnotation(Auditable.class).tipoOperacion());
				    	auditoria.setUserAgent(requestHeaders.get("user-agent"));
				    	auditoria.setVia(requestHeaders.get("via"));

				    	List<String>  propiedadesObjeto = new ArrayList<String>();
				    	List<String>  valoresObjeto = new ArrayList<String>();				    	
				    
						PropertyDescriptor[] descriptors =  BeanUtilsBean.getInstance().getPropertyUtils().getPropertyDescriptors(object);
						
				    	for (PropertyDescriptor propertyDescriptor : descriptors) {				    		
				    		String propiedad = propertyDescriptor.getName();				    		
				    		if(!propiedad.equalsIgnoreCase("class")){				    		
				    			if (propertyDescriptor.getReadMethod() != null){
				    				propiedadesObjeto.add(propiedad);
				    				Object value = null; 
				    				
				    				try{
				    					BeanUtilsBean beanUtils = BeanUtilsBean.getInstance();
				    					value = beanUtils.getProperty(object, propiedad);
				    				}catch (Exception e) {
										log.error("ERROR AL RECUPERAR PROPIEDAD DE OBJETO "+object.getClass().getName()+" EN REGISTRAR AUDITORIA " +e.getMessage());
									}
				    				String stringValue = null;
				    				
				    				if(value !=null)
				    					stringValue = value.toString();
									else
										stringValue = ""+value;
										
									valoresObjeto.add(stringValue);
				    				
				    				if(propertyDescriptor.getReadMethod().isAnnotationPresent(Id.class)){
				    					auditoria.setValueID(stringValue);
				    					auditoria.setPropiedadID(propiedad);
				    				}
				    			}				    		
				    		}				    		
						}
					   
						 auditoria.setPropiedadesObjeto(propiedadesObjeto.toString()) ; 
						 auditoria.setValoresObjeto(valoresObjeto.toString());
						 if(returnValue != null ){
							 auditoria.setReturnClass(returnValue.getClass().getName());
							 auditoria.setReturnValue(returnValue.toString());
						 }
						 serviceDao.getGenericCommonDao().create(Auditoria.class, auditoria);
				    }
			}
		
		}
		
		} catch (Exception e) {		
			log.error("ERROR AL REGISTRAR LA AUDITORIA " + e.getMessage() + "  - " + e.getCause());
		}
		
		
	}

	public ServiceDao getServiceDao() {
		return serviceDao;
	}
	@Autowired
	@Resource(name="serviceDao")
	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	public boolean isAuditoriaActivada() {
		return auditoriaActivada;
	}
	@Value("${auditoria.service.active}")
	public void setAuditoriaActivada(boolean auditoriaActivada) {
		this.auditoriaActivada = auditoriaActivada;
	}

	
	
	
	
}
