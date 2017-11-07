package la.netco.correspondencia.uilayer.beans.salidas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.persistencia.vo.Usuario;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.spring.security.UserDetailsUtils;
import la.netco.core.uilayer.beans.Constants;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.core.uilayer.commons.GenericDataModel;
import la.netco.persistencia.dto.commons.Clasificacion;
import la.netco.persistencia.dto.commons.ConstantsKeysFire;
import la.netco.persistencia.dto.commons.Depend;
import la.netco.persistencia.dto.commons.Medioscorrespondencia;
import la.netco.persistencia.dto.commons.Salida;
import la.netco.persistencia.dto.commons.Seguimientosalida;
import la.netco.persistencia.dto.commons.Transicion;

@ManagedBean
@ViewScoped
public class EntregaSalidasMultipleBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;
	private SalidaDataModel listDatamodel;

	private List<SelectItem> dependenciasItems;
	private Short idDependenciaSeleccionada;
	private boolean filtroEntregada;
	
	private Boolean adminCorrespondencia;
	private String usuarioLogeado;
	
	
	private List<SelectItem> usuariosItems;	
	
	private List<SelectItem> mediosItems;
	
	
	private Map<Integer, Boolean> idsSalidasSeleccionadas = new HashMap<Integer, Boolean>();
	

	private static String ERROR_SELECCIONE_USUARIO = "corresEntregaSeleccionarUsario";
	private static String ERROR_SELECCIONE_MEDIO = "corresEntregaSeleccionarMedio";
	private static String ERROR_SELECCIONE_PERSONA= "corresEntregaSeleccionarPersona";
	private static String ERROR_SELECCIONE_FECHA_HORA= "corresEntregaSeleccionarFechaHora";
	
	public EntregaSalidasMultipleBean(){
		if(usuarioLogeado == null)
			usuarioLogeado = UserDetailsUtils.usernameLogged();
	}
	@PostConstruct
	public void init(){
		if(adminCorrespondencia == null && usuarioLogeado != null){			
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, usuarioLogeado);
			params.put(1, ConstantsKeysFire.GRUPO_ADMIN_CORRESPONDENCIA);
			Long adminGroup = (Long) serviceDao.getEntradaDao().executeUniqueResult(Usuario.NAMED_QUERY_FIND_BY_USUARIO_GRUPO, params);
			if(adminGroup != null && !adminGroup.equals(new Integer(0).longValue())){
				adminCorrespondencia = true;
			}else{
				adminCorrespondencia = false;
			}		
		}
		
		if(listDatamodel ==null){
			listDatamodel 	= new SalidaDataModel();
			cargaListaDataModel();
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public String actualizar(){
		String page =	null;
		try {
			
			Map<Object,Salida> wrappedData = (Map<Object, Salida>) listDatamodel.getWrappedData();
			boolean error= false;
			boolean errorUsuario= false;
			boolean errorPersona= false;
			boolean errorMedio= false;
			boolean errorFecha= false;
			
			for (Entry<Integer, Boolean> salidasId : idsSalidasSeleccionadas.entrySet()) {
				if( salidasId.getValue()){
					Salida salidaSeleccionada = wrappedData.get(salidasId.getKey());
					Short idUsuario = salidaSeleccionada.getUsrEntrega();
					if(idUsuario == null || idUsuario.equals(new Integer(0).shortValue()))
						errorUsuario =true;
					
					Short idMedio = salidaSeleccionada.getIdMedioEntrega();
					if(idMedio == null || idMedio.equals(new Integer(0).shortValue()))
						errorMedio =true;
					
					if(salidaSeleccionada.getSalPen() == null || salidaSeleccionada.getSalPen().equals(""))	
						errorPersona =true;
					
					if(salidaSeleccionada.getSalFen() == null) 
						errorFecha =true;
					
				}
			}
			
			if(errorUsuario){
				FacesUtils.addFacesMessageFromBundle(ERROR_SELECCIONE_USUARIO,FacesMessage.SEVERITY_ERROR);
				error =true;
			}
			
			if(errorMedio){
				FacesUtils.addFacesMessageFromBundle(ERROR_SELECCIONE_MEDIO,FacesMessage.SEVERITY_ERROR);
				error =true;
			}
			
			if(errorPersona){
				FacesUtils.addFacesMessageFromBundle(ERROR_SELECCIONE_PERSONA,FacesMessage.SEVERITY_ERROR);
				error =true;
			}
			
			if(errorFecha){
				FacesUtils.addFacesMessageFromBundle(ERROR_SELECCIONE_FECHA_HORA,FacesMessage.SEVERITY_ERROR);
				error =true;
			}
			
			if(error){
				return page;
			}
			
			
			for (Entry<Integer, Boolean> salidasId : idsSalidasSeleccionadas.entrySet()) {
				if( salidasId.getValue()){
					Salida salidaSeleccionada = wrappedData.get(salidasId.getKey());
					
					
			Usuario usuario = (Usuario)serviceDao.getGenericCommonDao().read(Usuario.class, salidaSeleccionada.getUsrEntrega());
			Medioscorrespondencia medio = (Medioscorrespondencia)serviceDao.getGenericCommonDao().read(Medioscorrespondencia.class, salidaSeleccionada.getIdMedioEntrega());
			
			salidaSeleccionada.setSalLent(true);
			salidaSeleccionada.setUsuarioBySalUen(usuario);
			salidaSeleccionada.setSalHen(salidaSeleccionada.getSalHen());
			salidaSeleccionada.setMedio(medio);
			serviceDao.getGenericCommonDao().update(Salida.class, salidaSeleccionada);
			

			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, salidaSeleccionada.getSalId());
			List<Seguimientosalida> seguimientos = (List<Seguimientosalida>)serviceDao.getEntradaDao().executeFindPaged(0,1, params, Seguimientosalida.NAMED_QUERY_GET_BY_SALIDA);
			Seguimientosalida seguimiento = seguimientos.get(0);
			
			Transicion transicion = new Transicion();
			transicion.setEstadoByTrsEstfin(seguimiento.getEstadoBySsaEst());
			transicion.setEstadoByTrsEstini(seguimiento.getEstadoBySsaEstini());
			transicion.setEvento(seguimiento.getEvento());
			
			serviceDao.getSeguimientosDao().addSeguimientoSalida(salidaSeleccionada, transicion, Seguimientosalida.SEGUIMIENTO_ENTREGADO, salidaSeleccionada.getSalOde(), UserDetailsUtils.usuarioLogged(), usuario);
				}
			}
			FacesUtils.addFacesMessageFromBundle(Constants.OPERACION_EXITOSA_ACTUALIZAR_REGISTRO,	FacesMessage.SEVERITY_INFO);
			
			page = "transaccionExitosa";
			//salidaSeleccionada = new Salida();
				
		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,	FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
		}
		
		return page;
	}
	
	public void cargaListaDataModel(){
		List<Criterion> filtros = new ArrayList<Criterion>();
		
    	if(idDependenciaSeleccionada != null && idDependenciaSeleccionada != 0){
    		filtros.add(Restrictions.eq("depend.depId", idDependenciaSeleccionada));
    	}
    	
    	if(!adminCorrespondencia){
    		filtros.add(Restrictions.eq("usuarioBySalUsr.usrLog", usuarioLogeado));    	
    	}
    	
    	if(filtroEntregada){
			filtros.add(Restrictions.eq("salLent", filtroEntregada));
		}else{
			filtros.add(Restrictions.or(Restrictions.eq("salLent", filtroEntregada), Restrictions.isNull("salLent")));
		}
		Map<String, String> alias = new HashMap<String, String>();

		alias.put("usuarioBySalUsr", "usuarioBySalUsr");
		listDatamodel.setAlias(alias);
		listDatamodel.setFiltros(filtros);
	}
	
	
	@SuppressWarnings("unchecked")	
	public void cargarDependenciasItems(){

		if(dependenciasItems == null){
			
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, Clasificacion.TIPO_SALIDA);

			List<Depend> dependencias = (List<Depend>) serviceDao.getGenericCommonDao().executeFind(Depend.class, params, Depend.NAMED_QUERY_ALL_DEPEND_BY_TIPO_CLASIFICACION);
			 
			dependenciasItems = new ArrayList<SelectItem>();
			 
			for (Depend dependencia : dependencias) {
				dependenciasItems.add(new SelectItem(dependencia.getDepId(), dependencia.getDepNom()));
			}
		}
	}
	
	@SuppressWarnings("unchecked")	
	public void cargarUsuariosItems(){
		if(usuariosItems == null){	
			List<Usuario> usuarios = (List<Usuario>) serviceDao.getGenericCommonDao().executeFind(Usuario.NAMED_QUERY_ALL_USUARIOS_ACTIVOS);
			usuariosItems = new ArrayList<SelectItem>();			 
			for (Usuario usu : usuarios) {
				usuariosItems.add(new SelectItem(usu.getUsrId(), usu.getUsrNom() + " " + usu.getUsrPap() + " " + usu.getUsrSap()));
			}			
		}
	}
	
	@SuppressWarnings("unchecked")	
	public void cargarMediosItems(){

		if(mediosItems == null){			

			List<Medioscorrespondencia> allData = (List<Medioscorrespondencia>) serviceDao.getGenericCommonDao().executeFind(Medioscorrespondencia.NAMED_QUERY_ALL);
			 
			mediosItems = new ArrayList<SelectItem>();
			 
			for (Medioscorrespondencia dato : allData) {
				mediosItems.add(new SelectItem(dato.getMedId(), dato.getMedNom()));
			}
			
		}
	}
	
	private static final class SalidaDataModel extends
			GenericDataModel<Salida, Integer> {
		private static final long serialVersionUID = 1L;

		private SalidaDataModel() {
			super(Salida.class);
			setOrderBy(Order.desc("salFsa"));
		}

		@Override
		protected Object getId(Salida t) {
			return t.getSalId();
		}
	}

	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public SalidaDataModel getListDatamodel() {
		return listDatamodel;
	}

	public List<SelectItem> getDependenciasItems() {
		cargarDependenciasItems();
		return dependenciasItems;
	}

	public Short getIdDependenciaSeleccionada() {
		return idDependenciaSeleccionada;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	public void setListDatamodel(SalidaDataModel listDatamodel) {
		this.listDatamodel = listDatamodel;
	}

	public void setDependenciasItems(List<SelectItem> dependenciasItems) {
		this.dependenciasItems = dependenciasItems;
	}

	public void setIdDependenciaSeleccionada(Short idDependenciaSeleccionada) {
		this.idDependenciaSeleccionada = idDependenciaSeleccionada;
	}
	public boolean isFiltroEntregada() {
		return filtroEntregada;
	}
	public void setFiltroEntregada(boolean filtroEntregada) {
		this.filtroEntregada = filtroEntregada;
	}
	public Map<Integer, Boolean> getIdsSalidasSeleccionadas() {
		return idsSalidasSeleccionadas;
	}
	public void setIdsSalidasSeleccionadas(Map<Integer, Boolean> idsSalidasSeleccionadas) {
		this.idsSalidasSeleccionadas = idsSalidasSeleccionadas;
	}
	public List<SelectItem> getUsuariosItems() {
		cargarUsuariosItems();
		return usuariosItems;
	}
	public List<SelectItem> getMediosItems() {
		cargarMediosItems();
		return mediosItems;
	}
	public void setUsuariosItems(List<SelectItem> usuariosItems) {
		this.usuariosItems = usuariosItems;
	}
	public void setMediosItems(List<SelectItem> mediosItems) {
		this.mediosItems = mediosItems;
	}

}
