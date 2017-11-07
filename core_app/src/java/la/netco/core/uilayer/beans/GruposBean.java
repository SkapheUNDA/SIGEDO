package la.netco.core.uilayer.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.persistencia.vo.Grupo;
import la.netco.core.persistencia.vo.GrupoUsuario;
import la.netco.core.persistencia.vo.Usuario;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.spring.security.CommonsActionsKey;
import la.netco.core.spring.security.CommonsModKeys;
import la.netco.core.spring.security.SecuredAction;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.core.uilayer.commons.GenericDataModel;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.richfaces.component.UIDataTable;


@ManagedBean(name = "gruposBean")
@ViewScoped
public class GruposBean implements Serializable {


	private static final long serialVersionUID = 8533180352102904339L;

	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;
	
	private GruposDataModel dataModel;
	private UsuariosDataModel usuariosDataModel;
	private UsuariosDataModel usuariosRelacionadosDataModel;
	private String nombres;
	private String primerApellido;
	private String segundoApellido;
	private String nombreUsuario;

	
	private Grupo grupoSeleccionado;

	private Integer idRegSeleccionado;
	private Map<Short, Boolean> idsUsuariosSeleccionados = new HashMap<Short, Boolean>();
	transient private UIDataTable usuariosDataTable;

	private Grupo nuevoRegistro;
	
	public GruposBean(){
		dataModel = new GruposDataModel();
		usuariosDataModel = new UsuariosDataModel ();
		
		usuariosRelacionadosDataModel = new UsuariosDataModel ();
		
		grupoSeleccionado = new Grupo();
		
		nuevoRegistro = new Grupo();
	}
	

	private static final String ACTION_RELACIONAR_USUARIOS = "UPDATE_U";
	
	@SecuredAction(keyAction = ACTION_RELACIONAR_USUARIOS, keyMod = CommonsModKeys.SE_USU_GRP)
	public String relacionarUsuariosNavigation() {
		return "relacionarUsuarios";
	}
	
	@SecuredAction(keyAction = CommonsActionsKey.VIEW, keyMod = CommonsModKeys.SE_USU_GRP)
	public String verUsuariosRelacionadosNavigation() {
		return "verUsuariosRelacionados";
	}
	
	@SecuredAction(keyAction = CommonsActionsKey.ADD, keyMod = CommonsModKeys.SE_USU_GRP)
	public String agregarNavigation() {
		return "agregar";
	}
	
	@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod = CommonsModKeys.SE_USU_GRP)
	public String actualizarNavigation() {
		return "actualizar";
	}
	
	@SecuredAction(keyAction = CommonsActionsKey.DELETE, keyMod = CommonsModKeys.SE_USU_GRP)
	public String eliminarNavigation() {
		return "eliminar";
	}
	
	
	public String relacionarUsuarios(){
		
		String page = null;
		
		
		try{

			boolean relaciono = false;
			
			for (Entry<Short, Boolean> entry :idsUsuariosSeleccionados.entrySet()) {
				if(entry.getValue()){
					
					GrupoUsuario perfilPorUsuario = new GrupoUsuario(grupoSeleccionado.getId(), entry.getKey().shortValue());
					
						if(serviceDao.getGenericCommonDao().read(GrupoUsuario.class, perfilPorUsuario.getCompositePK()) == null){
							serviceDao.getGenericCommonDao().create(GrupoUsuario.class,perfilPorUsuario);
						}
					
					relaciono = true;
				}
				
				
			}
			
		 if(relaciono)
		 {
			 page = "transaccionExitosa";
			 FacesUtils.addFacesMessageFromBundle(Constants.OPERACION_EXITOSA_RELACIONAR_USUARIOS_PERFILES, FacesMessage.SEVERITY_INFO);
		 }else{
			 FacesUtils.addFacesMessageFromBundle(Constants.ERROR_SELECCIONE_USUARIOS_RELACIONAR, FacesMessage.SEVERITY_ERROR); 
			 
		 }
		
		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION, FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
			page = "";
		}
		return page ;
		
	}
	
	public void cargaFiltrosDataModel(){
    	List<Criterion> filtros = new ArrayList<Criterion>();
    	
    	
    	if(nombres != null  && !nombres.trim().equals("") ){    		
    		filtros.add(Restrictions.ilike("usrNom", "%" + nombres + "%"));    	
    	}
    	
    	if(primerApellido != null  && !primerApellido.trim().equals("") ){
    		filtros.add(Restrictions.ilike("usrPap", "%" + primerApellido + "%"));
    	}
    	
    	if(segundoApellido != null  && !segundoApellido.trim().equals("") ){
    		filtros.add(Restrictions.ilike("usrSap", "%" +  segundoApellido + "%"));
    	}
    	
    	if(nombreUsuario != null  && !nombreUsuario.trim().equals("") ){
    		filtros.add(Restrictions.ilike("usrLog",  "%" +nombreUsuario + "%"  ));    		
    	}   
    	
    	filtros.add(Restrictions.eq("usrLact", true));
			Map<String, String> alias = new HashMap<String, String>();
	    	usuariosDataModel = new UsuariosDataModel ();  
	    	usuariosDataModel.setFiltros(filtros);
	    	usuariosDataModel.setAlias(alias);
		

    }
    
	
	public void limpiarForm(){
		nombres = null;
		primerApellido = null;
		segundoApellido = null;
		nombreUsuario = null;
		List<Criterion> filtros = new ArrayList<Criterion>();

    	filtros.add(Restrictions.eq("usrLact", true));
		Map<String, String> alias = new HashMap<String, String>();
		usuariosDataModel = new UsuariosDataModel();
		usuariosDataModel.setFiltros(filtros);

		usuariosDataModel.setAlias(alias);
	}
	
	
	public void limpiarFormDetalle(){
		nombres = null;
		primerApellido = null;
		segundoApellido = null;
		nombreUsuario = null;
		List<Criterion> filtros = new ArrayList<Criterion>();
		
		if(grupoSeleccionado != null && grupoSeleccionado.getId() !=null){	
			
			Map<String, String> alias = new HashMap<String, String>();
			alias.put("grupos", "grupos");
			filtros.add(Restrictions.eq("grupos.id", grupoSeleccionado.getId()));
			usuariosRelacionadosDataModel = new UsuariosDataModel ();    	
			usuariosRelacionadosDataModel.setFiltros(filtros);
			usuariosRelacionadosDataModel.setAlias(alias);
		}

	}
	
	
	public void cargaFiltrosDataModelUsuariosRelacionados(){
    	List<Criterion> filtros = new ArrayList<Criterion>();
    	
    	if(nombres != null  && !nombres.trim().equals("") ){    		
    		filtros.add(Restrictions.ilike("usrNom", "%" + nombres + "%"));
    	}
    	
    	if(primerApellido != null  && !primerApellido.trim().equals("") ){
    		filtros.add(Restrictions.ilike("usrPap", "%" + primerApellido + "%"));  
    	}
    	
    	if(segundoApellido != null  && !segundoApellido.trim().equals("") ){
    		filtros.add(Restrictions.ilike("usrSap", "%" +  segundoApellido + "%"));
    	}
    	
    	if(nombreUsuario != null  && !nombreUsuario.trim().equals("") ){
    		filtros.add(Restrictions.ilike("usrLog",  "%" +nombreUsuario + "%"  ));
    	}   
    	
    	
		if(grupoSeleccionado != null && grupoSeleccionado.getId() !=null){	
			
			Map<String, String> alias = new HashMap<String, String>();
			alias.put("grupos", "grupos");
			filtros.add(Restrictions.eq("grupos.id", grupoSeleccionado.getId()));
			usuariosRelacionadosDataModel = new UsuariosDataModel ();    	
			usuariosRelacionadosDataModel.setFiltros(filtros);
			usuariosRelacionadosDataModel.setAlias(alias);
		}

    }

	@SecuredAction(keyAction = CommonsActionsKey.ADD, keyMod = CommonsModKeys.SE_USU_GRP)
	public String  crearRegistro() {
		String page =null;
		try {			
			serviceDao.getGenericCommonDao().create(Grupo.class, nuevoRegistro);
			
			FacesUtils.addFacesMessageFromBundle(Constants.OPERACION_EXITOSA_CREAR_REGISTRO, FacesMessage.SEVERITY_INFO);
			
			page = "transaccionExitosa";
			nuevoRegistro = new Grupo();
		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION, FacesMessage.SEVERITY_ERROR);
			e.printStackTrace();
		}
		
		return page;
	}
	
	@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod = CommonsModKeys.SE_USU_GRP)
	public String  actualizarRegistro() {
		String page =null;
		try {

			
			serviceDao.getGenericCommonDao().update(Grupo.class, grupoSeleccionado);
			
			FacesUtils.addFacesMessageFromBundle(Constants.OPERACION_EXITOSA_ACTUALIZAR_REGISTRO, FacesMessage.SEVERITY_INFO);
			
			page = "transaccionExitosa";
			grupoSeleccionado = new Grupo();
		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION, FacesMessage.SEVERITY_ERROR);
			e.printStackTrace();
		}
		
		return page;
	}
	
	@SuppressWarnings("unchecked")
	@SecuredAction(keyAction = CommonsActionsKey.DELETE, keyMod = CommonsModKeys.SE_USU_GRP)
	public String eliminarRegistro() {
		String page = null;
		try {


			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, grupoSeleccionado.getId());
		
			List<GrupoUsuario> gruposUsuarios = (List<GrupoUsuario>) serviceDao.getGenericCommonDao().executeFind(GrupoUsuario.class, params, GrupoUsuario.NAMED_QUERY_FIND_BY_USUARIO);
			
			if (gruposUsuarios != null && !gruposUsuarios.isEmpty()) {				
				FacesUtils.addFacesMessageFromBundle(Constants.ERROR_ELIMINAR_GRUPO_USUARIO,FacesMessage.SEVERITY_ERROR);	
				 return "";
			} 
		
			serviceDao.getGenericCommonDao().delete(Grupo.class,grupoSeleccionado);
			FacesUtils.addFacesMessageFromBundle(Constants.OPERACION_EXITOSA_ELIMINAR_REGISTRO,	FacesMessage.SEVERITY_INFO);
	
			page = "transaccionExitosa";			

			grupoSeleccionado = new Grupo();
		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
		}

		return page;
	}
	
	public void cargarObjeto(){	
		
		try {
							
			if(idRegSeleccionado == null){
				 FacesContext facesContext = FacesContext.getCurrentInstance();
			     String idRegSeleccionado = facesContext.getExternalContext().getRequestParameterMap().get("idRegSeleccionado");			   
			     if(idRegSeleccionado != null) this.idRegSeleccionado = Integer.parseInt(idRegSeleccionado);
			}
				
			if((grupoSeleccionado == null || grupoSeleccionado.getId() == null)&&  (idRegSeleccionado != null && !idRegSeleccionado.equals(""))){
				grupoSeleccionado =(Grupo) serviceDao.getGenericCommonDao().read(Grupo.class, idRegSeleccionado);			
			
			}
		
		} catch (Exception e) {
		System.err.println(e.getMessage());
		}	
	}
	
	
	public String cargaFiltrosGruposDataModel(){
    	List<Criterion> filtros = new ArrayList<Criterion>();
    
    	Map<String, String> alias = new HashMap<String, String>();
    	dataModel.setAlias(alias);
    	dataModel.setFiltros(filtros);
    	return "";
	}
	
	public boolean isTieneGrupo(Usuario usuario) {
		for (Grupo perfil : usuario.getGrupos()) {
			if(perfil.getId().equals(grupoSeleccionado.getId())){
				return true;
			}
		}
		return false;
	}
	
	private static final class UsuariosDataModel extends
			GenericDataModel<Usuario, Integer> {
		private static final long serialVersionUID = 1L;

		private UsuariosDataModel() {
			super(Usuario.class);
			setOrderBy(Order.asc("usrLog"));
		}

		@Override
		protected Object getId(Usuario t) {
			return t.getUsrId();
		}
	}
	
	private static final class GruposDataModel extends
			GenericDataModel<Grupo, Integer> {
		private static final long serialVersionUID = 1L;

		private GruposDataModel() {
			super(Grupo.class);
			setOrderBy(Order.asc("id"));
		}

		@Override
		protected Object getId(Grupo t) {
			return t.getId();
		}
	}

	
	public void setDataModel(GruposDataModel  dataModel) {
		this.dataModel = dataModel;
	}

	public GruposDataModel getDataModel() {
		cargaFiltrosGruposDataModel();
		return dataModel;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public String getNombres() {
		return nombres;
	}

	public String getPrimerApellido() {
		return primerApellido;
	}

	public String getSegundoApellido() {
		return segundoApellido;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}

	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}
	
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setGrupoSeleccionado(Grupo grupoSeleccionado) {
		this.grupoSeleccionado = grupoSeleccionado;
	}

	public Grupo getGrupoSeleccionado() {
		return grupoSeleccionado;
	}

	public void setUsuariosDataModel(UsuariosDataModel usuariosDataModel) {
		this.usuariosDataModel = usuariosDataModel;
	}

	public UsuariosDataModel getUsuariosDataModel() {
		cargaFiltrosDataModel();
		return usuariosDataModel;
	}

	public void setIdsUsuariosSeleccionados(Map<Short, Boolean> idsUsuariosSeleccionados) {
		this.idsUsuariosSeleccionados = idsUsuariosSeleccionados;
	}

	public Map<Short, Boolean> getIdsUsuariosSeleccionados() {
		return idsUsuariosSeleccionados;
	}

	public void setUsuariosRelacionadosDataModel(UsuariosDataModel usuariosRelacionadosDataModel) {
		this.usuariosRelacionadosDataModel = usuariosRelacionadosDataModel;
	}

	public UsuariosDataModel getUsuariosRelacionadosDataModel() {
		cargaFiltrosDataModelUsuariosRelacionados();
		return usuariosRelacionadosDataModel;
	}

	public UIDataTable getUsuariosDataTable() {
		return usuariosDataTable;
	}


	public void setUsuariosDataTable(UIDataTable usuariosDataTable) {
		this.usuariosDataTable = usuariosDataTable;
	}


	public Grupo getNuevoRegistro() {
		return nuevoRegistro;
	}

	public void setNuevoRegistro(Grupo nuevoRegistro) {
		this.nuevoRegistro = nuevoRegistro;
	}

	public Integer getIdRegSeleccionado() {
		return idRegSeleccionado;
	}

	public void setIdRegSeleccionado(Integer idRegSeleccionado) {
		this.idRegSeleccionado = idRegSeleccionado;
	}


}
