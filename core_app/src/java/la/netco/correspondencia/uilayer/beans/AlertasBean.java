package la.netco.correspondencia.uilayer.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.persistencia.vo.Usuario;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.spring.security.UserDetailsUtils;
import la.netco.core.uilayer.commons.GenericDataModel;
import la.netco.persistencia.dto.commons.ConstantsKeysFire;
import la.netco.persistencia.dto.commons.Entrada;
import la.netco.persistencia.dto.commons.UsuarioCtr;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

@ManagedBean
@ViewScoped
public class AlertasBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer idRegSeleccionado;
	private Entrada entradaSeleccionada;
	private String justificacion;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;

	private EntradasDataModel principalDataModel;
	private Usuario usuarioLogeado;
	private Boolean usuarioDirectivo;
	public AlertasBean(){
		if(usuarioLogeado == null)
			usuarioLogeado = UserDetailsUtils.usuarioLogged();
	}
	
	@PostConstruct
	public void init(){
		if(usuarioDirectivo == null && usuarioLogeado != null){			
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, usuarioLogeado.getUsrLog());
			params.put(1, ConstantsKeysFire.GRUPO_DIRECTIVO_CORRESPONDENCIA);
			Long adminGroup = (Long) serviceDao.getEntradaDao().executeUniqueResult(Usuario.NAMED_QUERY_FIND_BY_USUARIO_GRUPO, params);
			if(adminGroup != null && !adminGroup.equals(new Integer(0).longValue())){
				usuarioDirectivo = true;
			}else{
				usuarioDirectivo = false;
			}		
		}
	}
	
	public String cargaFiltrosDataModel(){
    	List<Criterion> filtros = new ArrayList<Criterion>();
    	
    	if(entradaSeleccionada != null && entradaSeleccionada.getEntId() != null) {
    		filtros.add(Restrictions.eq("entId", entradaSeleccionada.getEntId()));  
    	}
    	
    	if(!usuarioDirectivo)
    		filtros.add( Restrictions.eq("usuario.usrId", usuarioLogeado.getUsrId()));  
    	else{
    		
    		Criterion usuarioEntrada = Restrictions.eq("usuario.usrId", usuarioLogeado.getUsrId());
    		
    		DetachedCriteria criteriSub = DetachedCriteria.forClass(UsuarioCtr.class);
    		criteriSub.add(Restrictions.eq("compositePK.uscJEF",  usuarioLogeado.getUsrId()));
    		
    		Criterion usuarioSubs = Subqueries.exists(criteriSub);
    				
			LogicalExpression orExp = Restrictions.or(usuarioEntrada,usuarioSubs);
			
			filtros.add(orExp);  
    	}
    	
    	filtros.add(Restrictions.eq("entLcon", false));
    	filtros.add(Restrictions.eq("entLrta", true));
    	filtros.add(Restrictions.gt("entTrt", new Integer(0).shortValue()));
    	
    	Map<String, String> alias = new HashMap<String, String>();
    	alias.put("usuario", "usuario");
    	principalDataModel.setAlias(alias);
    	principalDataModel.setFiltros(filtros);
    	return "";
	}
	
	public Long getTiempoRestante(Date fechaFen){
		Long tiempo = null; 
		if(fechaFen != null){
			tiempo = serviceDao.getEntradaDao().obtenerDiffDiasHabiles(fechaFen, new Date(System.currentTimeMillis()));	
		}
		return tiempo;	
	}
	
	public void cargarRegistroDetalleResumen(){		
		try {						
			if(idRegSeleccionado == null){
				 FacesContext facesContext = FacesContext.getCurrentInstance();
			     String idRegSeleccionado = facesContext.getExternalContext().getRequestParameterMap().get("idRegSeleccionado");			   
			     if(idRegSeleccionado != null) this.idRegSeleccionado = Integer.parseInt(idRegSeleccionado);		
			}
			
			if((entradaSeleccionada == null || entradaSeleccionada.getEntId() == null) 	&&  (idRegSeleccionado != null)){
				entradaSeleccionada =(Entrada) serviceDao.getEntradaDao().read(Entrada.class, idRegSeleccionado);			
			}			
			
			if(principalDataModel ==null){
				principalDataModel 	= new EntradasDataModel();
				cargaFiltrosDataModel();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	public Integer getIdRegSeleccionado() {
		return idRegSeleccionado;
	}

	public Entrada getEntradaSeleccionada() {
		return entradaSeleccionada;
	}

	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public void setIdRegSeleccionado(Integer idRegSeleccionado) {
		this.idRegSeleccionado = idRegSeleccionado;
	}

	public void setEntradaSeleccionada(Entrada entradaSeleccionada) {
		this.entradaSeleccionada = entradaSeleccionada;
	}
	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}


	public String getJustificacion() {
		return justificacion;
	}


	public void setJustificacion(String justificacion) {
		this.justificacion = justificacion;
	}	
	
	
	private static final class EntradasDataModel extends
			GenericDataModel<Entrada, Integer> {
		private static final long serialVersionUID = 1L;

		private EntradasDataModel() {
			super(Entrada.class);
			setOrderBy(Order.desc("entFen"));
		}

		@Override
		protected Object getId(Entrada t) {
			return t.getEntId();
		}
}


	public EntradasDataModel getPrincipalDataModel() {
		return principalDataModel;
	}

	public void setPrincipalDataModel(EntradasDataModel principalDataModel) {
		this.principalDataModel = principalDataModel;
	}

}
