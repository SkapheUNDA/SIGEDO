package la.netco.core.spring.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.persistencia.vo.Perfil;
import la.netco.core.persistencia.vo.Recurso;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.AntUrlPathMatcher;
import org.springframework.security.web.util.UrlMatcher;

public class CustomFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	private ServiceDao serviceDao;
	private static final Log logger = LogFactory.getLog(CustomFilterInvocationSecurityMetadataSource.class);
	private UrlMatcher urlMatcher = new AntUrlPathMatcher(); 
	private static Map<String, Collection<ConfigAttribute>> resourceMap = null;

	public Collection<ConfigAttribute> getAttributes(Object object)
			throws IllegalArgumentException {
		 FilterInvocation fi = (FilterInvocation) object;
	     String url = fi.getRequestUrl();
	     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	     if(authentication != null){
		    	Object principal = authentication.getPrincipal();
		    	if (principal instanceof UserDetails) {
		    		
		    		Iterator<String> ite = resourceMap.keySet().iterator();  
		 	        while (ite.hasNext()) {  
		 	            String resURL = ite.next();  
		 	            
		 	           
		 	            if (urlMatcher.pathMatchesUrl(url, resURL)) {  
		 	                Collection<ConfigAttribute> returnCollection = resourceMap.get(resURL);  
		 	                if (logger.isDebugEnabled()) {  
		 	                    logger.debug("getAttributes(Object) - end"); //$NON-NLS-1$  
		 	                } 
		 	                
		 	                return returnCollection;  
		 	            }  
		 	        }
		 	        
		 	       return SecurityConfig.createList("ROLE_DENYALL");
		 	        
		    	}else if(url.equals("/login.jsf") ||  url.equals("/accessDenied.jsf") || url.equals("/errorDisplay.jsf"))  {
		    			   String[] roles = new String[] { "ROLE_ANONYMOUS"};
		    			    return SecurityConfig.createList(roles);
		    			    
		    	}else{		
		    		SecurityContextHolder.getContext().setAuthentication(null);
		    		return SecurityConfig.createList("ROLE_DENYALL");
		    	}
	     
	     }
	     
	     return null;
	  
	}

	@SuppressWarnings("unchecked")
	public  void loadResourceDefine()throws Exception  {  
        resourceMap = new HashMap<String, Collection<ConfigAttribute>>();  
       
		List<Recurso> recursos = (List<Recurso>) serviceDao.getGenericCommonDao().loadAll(Recurso.class);
        for (Recurso action: recursos ){  
            Collection<ConfigAttribute> atts = new ArrayList<ConfigAttribute>(); 
            
            Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, action.getId());					
			List<Perfil> perfiles = (List<Perfil>)serviceDao.getGenericCommonDao().executeFind(Perfil.class, params, Perfil.NAMED_QUERY_FIND_BY_RECURSO);
	
            for(Perfil perfil:perfiles){  
            	ConfigAttribute ca = new SecurityConfig("ROLE_"+ perfil.getNombre());  
                atts.add(ca); 
            }  
            
            resourceMap.put(action.getInterceptUrl(), atts);  
        }
        
        List<Perfil> perfiles = (List<Perfil>) serviceDao.getGenericCommonDao().loadAll(Perfil.class);
    	Collection<ConfigAttribute> atts = new ArrayList<ConfigAttribute>();  
        for(Perfil perfil:perfiles){          
        	ConfigAttribute ca = new SecurityConfig("ROLE_"+ perfil.getNombre());  
            atts.add(ca);             
        } 
        resourceMap.put("/kernel/index.jsf", atts);  
        resourceMap.put("/login.jsf", atts);  
        resourceMap.put("/errorDisplay.jsf", atts);
        resourceMap.put("/index.jsp", atts); 
        
        logger.info("SE REALIZA LA CARGA DE resourceMap ");
	}
	
	//@SuppressWarnings("unchecked")
	/*public  void loadResourceDefine()throws Exception  {  
        resourceMap = new HashMap<String, Collection<ConfigAttribute>>();  
       
		List<Perfil> perfiles = (List<Perfil>) serviceDao.getGenericCommonDao().loadAll(Perfil.class);
        for (Perfil item: perfiles ){  
            Collection<ConfigAttribute> atts = new ArrayList<ConfigAttribute>();  
            ConfigAttribute ca = new SecurityConfig("ROLE_"+ item.getNombre());  
            atts.add(ca);  
            
            Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, item.getId());					
			 List<Recurso> tActionList =   (List<Recurso>) serviceDao.getGenericCommonDao().executeFind(Recurso.class, params, Recurso.NAMED_QUERY_FIND_BY_PERFIL);
	
            for(Recurso tAction:tActionList){           
                resourceMap.put(tAction.getInterceptUrl(), atts);  
            }  
            
            resourceMap.put("/kernel/index.jsf", atts);  
            resourceMap.put("/login.jsf", atts);  
            resourceMap.put("/errorDisplay.jsf", atts);
            resourceMap.put("/index.jsp", atts); 
        } 
        logger.info("SE REALIZA LA CARGA DE resourceMap ");
	}*/
	
   public Collection<ConfigAttribute> getAllConfigAttributes() {		
    		return null;
   }
    	
	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}


	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	@Autowired
	@Resource(name="serviceDao")
	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

}
