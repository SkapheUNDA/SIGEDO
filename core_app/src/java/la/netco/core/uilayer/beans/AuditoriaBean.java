package la.netco.core.uilayer.beans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.persistencia.vo.Auditoria;
import la.netco.core.persistencia.vo.EntidadAuditada;
import la.netco.core.persistencia.vo.custom.ObjetoAuditado;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.uilayer.commons.GenericDataModel;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;


@ManagedBean(name = "auditoriaBean")
@RequestScoped
public class AuditoriaBean {
	
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	private ServiceDao serviceDao;
	
	private AuditoriaDataModel auditoriaDataModel;
	private Auditoria  auditoriaSeleccionada;
	private Date fechaInicial;
	private Date fechaFinal;
	private String nombreUsuario;
	
	private List<SelectItem> entidadesItems;
	
	private Integer idEntidadSeleccionada;
	public void cargaFiltrosDataModel(){   
	  	
		List<Criterion> filtros = new ArrayList<Criterion>();
		
		if(nombreUsuario != null  && !nombreUsuario.trim().equals("") ){
    		filtros.add(Restrictions.ilike("username",  "%" +nombreUsuario + "%"  ));
    	}
		
		if(fechaInicial != null || fechaFinal!=null){
    		
    		if(fechaInicial != null && fechaFinal == null ){  
    			filtros.add(Restrictions.ge("fechaRegistro",  fechaInicial));    			
    		}else if(fechaInicial == null && fechaFinal != null ){
    			Calendar fecha = Calendar.getInstance();
				fecha.setTime(fechaFinal);
				fecha.set(Calendar.HOUR_OF_DAY, 24);
    			filtros.add(Restrictions.le("fechaRegistro", fecha.getTime()));    		
    		}else{
    			Calendar fecha = Calendar.getInstance();
				fecha.setTime(fechaFinal);
				fecha.set(Calendar.HOUR_OF_DAY, 24);
    			filtros.add(Restrictions.between("fechaRegistro", fechaInicial, fecha.getTime()));    	
    		}    		
    		
    	}
		if(idEntidadSeleccionada != null && idEntidadSeleccionada != 0  ){
    		filtros.add(Restrictions.eq("entidadAuditada.id", idEntidadSeleccionada ));
    	}

		auditoriaDataModel = new AuditoriaDataModel();    	
		auditoriaDataModel.setFiltros(filtros);
    }

	public AuditoriaDataModel getAuditoriaDataModel() {
		cargaFiltrosDataModel();
		return auditoriaDataModel;
	}
	
	public List<ObjetoAuditado> getPropiedadesObjetoAuditado(){
		List<ObjetoAuditado>  propiedadesValores = new ArrayList<ObjetoAuditado>();
		if(auditoriaSeleccionada != null){
			String[] propiedadesObjeto  = auditoriaSeleccionada.getPropiedadesObjeto().split(",");
			String[] valoresObjeto  	= auditoriaSeleccionada.getValoresObjeto().split(",");
			
			
			for (int i = 0; i < propiedadesObjeto.length; i++) {
				String propiedad = propiedadesObjeto[i];
				String valor = valoresObjeto[i];
				ObjetoAuditado objetoAuditado = new ObjetoAuditado();
				objetoAuditado.setPropiedad(propiedad);
				objetoAuditado.setValor(valor);
				propiedadesValores.add(objetoAuditado);
			}
		}
		return propiedadesValores;
	}

	@SuppressWarnings("unchecked")
	public List<Auditoria> getAuditoriasRelacionadas(){
		List<Auditoria> auditorias = null;
		if(auditoriaSeleccionada != null){
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, auditoriaSeleccionada.getPropiedadID());
			params.put(1, auditoriaSeleccionada.getValueID());
			params.put(2, auditoriaSeleccionada.getEntidadAuditada().getId());
			auditorias =  (List<Auditoria>) serviceDao.getGenericCommonDao().executeFind(Auditoria.class,params, Auditoria.NAMED_QUERY_GET_BY_VALOR_PROPIEDAD);
		}
		return  auditorias;
	}
	
	public void setAuditoriaDataModel(AuditoriaDataModel auditoriaDataModel) {
		this.auditoriaDataModel = auditoriaDataModel;
	}

	public void setAuditoriaSeleccionada(Auditoria auditoriaSeleccionada) {
		this.auditoriaSeleccionada = auditoriaSeleccionada;
	}

	public Auditoria getAuditoriaSeleccionada() {
		return auditoriaSeleccionada;
	}

	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	public Date getFechaInicial() {
		return fechaInicial;
	}

	public Date getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}
	
	@SuppressWarnings("unchecked")
	public List<SelectItem> getEntidadesItems() {
		if(entidadesItems == null){
			
			List<EntidadAuditada>  allEntidades =  (List<EntidadAuditada>) serviceDao.getGenericCommonDao().loadAll(EntidadAuditada.class);
			entidadesItems = new ArrayList<SelectItem>();
			for (EntidadAuditada entidad : allEntidades) {
				entidadesItems.add(new SelectItem(entidad.getId(), entidad.getNombreModulo()));
			}
		}
		return entidadesItems;
	}
	
	private static final class AuditoriaDataModel extends
			GenericDataModel<Auditoria, Integer> {
		private static final long serialVersionUID = 1L;

		private AuditoriaDataModel() {
			super(Auditoria.class);
			setOrderBy(Order.asc("id"));
		}

		@Override
		protected Object getId(Auditoria t) {
			return t.getId();
		}
	}
	
	public void setEntidadesItems(List<SelectItem> entidadesItems) {
		this.entidadesItems = entidadesItems;
	}
	
	public void setIdEntidadSeleccionada(Integer idEntidadSeleccionada) {
		this.idEntidadSeleccionada = idEntidadSeleccionada;
	}
	
	public Integer getIdEntidadSeleccionada() {
		return idEntidadSeleccionada;
	}	
	
}
