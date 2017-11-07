package la.netco.correspondencia.uilayer.beans.salidas;

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
import javax.faces.model.SelectItem;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.persistencia.vo.Usuario;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.spring.security.CommonsActionsKey;
import la.netco.core.spring.security.CommonsModKeys;
import la.netco.core.spring.security.SecuredAction;
import la.netco.core.spring.security.UserDetailsUtils;
import la.netco.persistencia.dto.commons.Clasificacion;
import la.netco.persistencia.dto.commons.ConstantsKeysFire;
import la.netco.persistencia.dto.commons.Depend;
import la.netco.persistencia.dto.commons.Salida;
import la.netco.uilayer.beans.PrimeDataModel;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;


@ManagedBean
@ViewScoped
public class BuscadorSalidasBean implements Serializable {


	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;
	private SalidaDataModel listDatamodel;

	private List<Salida> salidas;
	
	private String noRadicacion;
	private Date	fechaRadicacion;
	private String 	asunto;
	private String 	nomPersonaNatural;
	private String 	nomPersonaJuridica;
	
	private List<SelectItem> clasificacionItems;
	private Short 	idClasificacionSeleccionada;
	
	private List<SelectItem> usuariosItems;	
	private Short 	idUsuarioSeleccionado;
	
	private List<SelectItem> dependenciasItems;
	private Short idDependenciaSeleccionada;

	private Boolean adminCorrespondencia;
	private String usuarioLogeado;
	private Boolean filtroEntregada;
	
	public BuscadorSalidasBean(){
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
	
	@SecuredAction(keyAction = CommonsActionsKey.VIEW, keyMod =CommonsModKeys.MNUS)
	public String detalleNavigation() {
		return "detalle";
	}

	@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod =CommonsModKeys.MNUS)
	public String actualizarNavigation() {
		return "actualizar";
	}
	
	@SecuredAction(keyAction = CommonsActionsKey.ADD, keyMod = CommonsModKeys.MNUS)
	public String agregarNavigation() {
		return "agregar?faces-redirect=true";
	}
	
	//@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod =CommonsModKeys.MNUE)
	public String duplicarNavigation() {
		return "duplicar";
	}
	
	//@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod =CommonsModKeys.MNUE)
	public String historialNavigation() {
			return "historial";
	}

	//@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod =CommonsModKeys.MNUE)
	public String archivosAdjuntosNavigation() {
				return "archivosAdjuntos";
	}
	
	//@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod =CommonsModKeys.MNUE)
	public String cambioEtapaNavigation() {
				return "cambiarEtapa";
	}
	
	//@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod =CommonsModKeys.MNUE)
	public String cambiarEtapasMultiplesNavigation() {
			return "cambiarEtapaMultiple?faces-redirect=true";
	}
	
	//@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod =CommonsModKeys.MNUE)
	public String cambiarFechaNavigation() {
		return "cambiarFecha";
	}
	
	//@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod =CommonsModKeys.MNUE)
	public String entregaCorrespondenciaNavigation() {
		return "entregaCorrespondencia";
	}
	
	//@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod =CommonsModKeys.MNUE)
	public String entregaMultipleNavigation() {
		return "entregaMultiple?faces-redirect=true";
	}
	
	public void cargaListaDataModel(){
		List<Criterion> filtros = new ArrayList<Criterion>();
		boolean filtro = false;
		if(noRadicacion != null  && !noRadicacion.trim().equals("") ){
    		filtros.add(Restrictions.eq("salNsa",  noRadicacion));  
    		filtro = true;
    	}
		
		if(asunto != null  && !asunto.trim().equals("") ){
    		filtros.add(Restrictions.ilike("salAsu",  asunto));  
    		filtro = true;
    	}
    	
    	if(fechaRadicacion != null  && !fechaRadicacion.equals("") ){
    		filtros.add(Restrictions.eq("salFsa", fechaRadicacion));  
    		filtro = true;
    	}
    	
    	if(nomPersonaNatural != null  && !nomPersonaNatural.equals("") ){
    		filtros.add(Restrictions.ilike("nombreCompletoDest", "%" + nomPersonaNatural + "%"));
    		filtro = true;
    	}
    	
    	if(nomPersonaJuridica != null  && !nomPersonaJuridica.equals("") ){
    		filtros.add(Restrictions.ilike("salEnt", "%" + nomPersonaJuridica + "%"));
    		filtro = true;
    	}
    	
    	if(idDependenciaSeleccionada != null && idDependenciaSeleccionada != 0){
    		filtros.add(Restrictions.eq("depend.depId", idDependenciaSeleccionada));
    		filtro = true;
    	}
    	
    	if(idClasificacionSeleccionada != null &&  idClasificacionSeleccionada != 0){
    		filtros.add(Restrictions.eq("clasificacion.claId", idClasificacionSeleccionada));   
    		filtro = true;
    	}
    	
    	if(filtroEntregada != null){
    		if(filtroEntregada.equals(Boolean.TRUE)){
    			filtros.add(Restrictions.eq("salLent", filtroEntregada));
    		}else{
    			filtros.add(Restrictions.or(Restrictions.eq("salLent", filtroEntregada), Restrictions.isNull("salLent")));
    		}
    		
    		filtro = true;
    	}
    	
    	
    	if(adminCorrespondencia == true){
    		if(idUsuarioSeleccionado != null &&   idUsuarioSeleccionado != 0){
    			System.out.println(" filtra por usuaio " + idUsuarioSeleccionado);
        		filtros.add(Restrictions.eq("usuarioBySalUsr.usrId", idUsuarioSeleccionado));    	
        	}else if(!filtro){
        		filtros.add(Restrictions.eq("usuarioBySalUsr.usrLog", usuarioLogeado));
        	}
    	}else{
    		filtros.add(Restrictions.eq("usuarioBySalUsr.usrLog", usuarioLogeado));    	
    	}

		Map<String, String> alias = new HashMap<String, String>();

		alias.put("usuarioBySalUsr", "usuarioBySalUsr");
		listDatamodel.setAlias(alias);
		listDatamodel.setFiltros(filtros);
	}
	
	private static final class SalidaDataModel extends
			PrimeDataModel<Salida, Integer> {
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


	
	@SuppressWarnings("unchecked")	
	public void cargarClasificacionItems(){

		if(idDependenciaSeleccionada != null){
		
			
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, Clasificacion.TIPO_SALIDA);
			params.put(1, idDependenciaSeleccionada);

			List<Clasificacion> clasificaciones = (List<Clasificacion>) serviceDao.getGenericCommonDao().executeFind(Depend.class, params, Clasificacion.NAMED_QUERY_ALL_BY_TIPO_DEPENDENCIA);
			
		
			clasificacionItems = new ArrayList<SelectItem>();
			 
			for (Clasificacion clasificacion : clasificaciones) {
				clasificacionItems.add(new SelectItem(clasificacion.getClaId(), clasificacion.getClaNom()));
			}
		}else if(clasificacionItems == null){
			clasificacionItems = new ArrayList<SelectItem>();
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
	public void cargarDependenciasItems(){

		if(dependenciasItems == null){
			
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, Clasificacion.TIPO_SALIDA);

			List<Depend> dependencias = (List<Depend>) serviceDao.getGenericCommonDao().executeFind(Depend.class, params, Depend.NAMED_QUERY_ALL_DEPEND_BY_TIPO_CLASIFICACION);
			 
			dependenciasItems = new ArrayList<SelectItem>();
			 
			for (Depend dependencia : dependencias) {
				dependenciasItems.add(new SelectItem(dependencia.getDepId(), dependencia.getDepNom()));
			}
		}else if(dependenciasItems == null){
			dependenciasItems = new ArrayList<SelectItem>();
		}
	}

	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	public SalidaDataModel getListDatamodel() {
		return listDatamodel;
	}

	public void setListDatamodel(SalidaDataModel listDatamodel) {
		this.listDatamodel = listDatamodel;
	}

	public List<Salida> getSalidas() {
		return salidas;
	}

	public void setSalidas(List<Salida> salidas) {
		this.salidas = salidas;
	}

	public String getNoRadicacion() {
		return noRadicacion;
	}

	public Date getFechaRadicacion() {
		return fechaRadicacion;
	}

	public String getAsunto() {
		return asunto;
	}

	public String getNomPersonaNatural() {
		return nomPersonaNatural;
	}

	public String getNomPersonaJuridica() {
		return nomPersonaJuridica;
	}

	public List<SelectItem> getClasificacionItems() {
		cargarClasificacionItems();
		return clasificacionItems;
	}

	public Short getIdClasificacionSeleccionada() {
		return idClasificacionSeleccionada;
	}

	public List<SelectItem> getUsuariosItems() {
		cargarUsuariosItems();
		return usuariosItems;
	}

	public Short getIdUsuarioSeleccionado() {
		return idUsuarioSeleccionado;
	}

	public List<SelectItem> getDependenciasItems() {
		cargarDependenciasItems();
		return dependenciasItems;
	}

	public Short getIdDependenciaSeleccionada() {
		return idDependenciaSeleccionada;
	}

	public void setNoRadicacion(String noRadicacion) {
		this.noRadicacion = noRadicacion;
	}

	public void setFechaRadicacion(Date fechaRadicacion) {
		this.fechaRadicacion = fechaRadicacion;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public void setNomPersonaNatural(String nomPersonaNatural) {
		this.nomPersonaNatural = nomPersonaNatural;
	}

	public void setNomPersonaJuridica(String nomPersonaJuridica) {
		this.nomPersonaJuridica = nomPersonaJuridica;
	}

	public void setClasificacionItems(List<SelectItem> clasificacionItems) {
		this.clasificacionItems = clasificacionItems;
	}

	public void setIdClasificacionSeleccionada(Short idClasificacionSeleccionada) {
		this.idClasificacionSeleccionada = idClasificacionSeleccionada;
	}

	public void setUsuariosItems(List<SelectItem> usuariosItems) {
		this.usuariosItems = usuariosItems;
	}

	public void setIdUsuarioSeleccionado(Short idUsuarioSeleccionado) {
		this.idUsuarioSeleccionado = idUsuarioSeleccionado;
	}

	public void setDependenciasItems(List<SelectItem> dependenciasItems) {
		this.dependenciasItems = dependenciasItems;
	}

	public void setIdDependenciaSeleccionada(Short idDependenciaSeleccionada) {
		this.idDependenciaSeleccionada = idDependenciaSeleccionada;
	}
	public Boolean getAdminCorrespondencia() {
		return adminCorrespondencia;
	}
	public void setAdminCorrespondencia(Boolean adminCorrespondencia) {
		this.adminCorrespondencia = adminCorrespondencia;
	}
	public Boolean getFiltroEntregada() {
		return filtroEntregada;
	}
	public void setFiltroEntregada(Boolean filtroEntregada) {
		this.filtroEntregada = filtroEntregada;
	}


		
}
