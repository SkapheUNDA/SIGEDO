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
import la.netco.core.businesslogic.services.impl.SpringApplicationContext;
import la.netco.core.persistencia.vo.Modulo;
import la.netco.core.persistencia.vo.Perfil;
import la.netco.core.persistencia.vo.PerfilRecurso;
import la.netco.core.persistencia.vo.PerfilUsuario;
import la.netco.core.persistencia.vo.Recurso;
import la.netco.core.persistencia.vo.Usuario;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.spring.security.CommonsActionsKey;
import la.netco.core.spring.security.CommonsModKeys;
import la.netco.core.spring.security.ResourceDefine;
import la.netco.core.spring.security.SecuredAction;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.core.uilayer.commons.GenericDataModel;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.richfaces.component.UIDataTable;
import org.richfaces.component.UITree;
import org.richfaces.event.TreeSelectionChangeEvent;

@ManagedBean(name = "perfilesBean")
@ViewScoped
public class PerfilesBean implements Serializable {

	private static final long serialVersionUID = 799883398809849635L;

	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;

	private PerfilesDataModel dataModel;
	private UsuariosDataModel usuariosDataModel;
	private UsuariosDataModel usuariosRelacionadosDataModel;
	private String nombres;
	private String primerApellido;
	private String segundoApellido;
	private String nombreUsuario;
	
	
	private Perfil perfilSeleccionado;
	private Perfil nuevoRegistro;
	private Map<Short, Boolean> idsUsuariosSeleccionados = new HashMap<Short, Boolean>();
	transient private UIDataTable usuariosDataTable;

	private Integer idRegSeleccionado;
	private List<Modulo> sourceModulesRoots = new ArrayList<Modulo>();
	private List<Recurso> recursosModulo = new ArrayList<Recurso>();
	private List<Recurso> recursosSeleccionados = new ArrayList<Recurso>();


	public PerfilesBean() {
		System.out.println("CONSTRUCTOR");
		setDataModel(new PerfilesDataModel());
		usuariosDataModel = new UsuariosDataModel();
		usuariosRelacionadosDataModel = new UsuariosDataModel();
		perfilSeleccionado = new Perfil();
		nuevoRegistro = new Perfil();
	}

	private static final String ACTION_RELACIONAR_RECURSOS = "UPDATE_R";
	private static final String ACTION_RELACIONAR_USUARIOS = "UPDATE_U";
	
	@SecuredAction(keyAction = ACTION_RELACIONAR_RECURSOS, keyMod = CommonsModKeys.SE_USU_ROL)
	public String relacionarrecursosNavigation() {
		return "relacionarrecursos";
	}
	
	@SecuredAction(keyAction = ACTION_RELACIONAR_USUARIOS, keyMod = CommonsModKeys.SE_USU_ROL)
	public String relacionarUsuariosNavigation() {
		return "relacionarUsuarios";
	}
	
	@SecuredAction(keyAction = CommonsActionsKey.VIEW, keyMod = CommonsModKeys.SE_USU_ROL)
	public String verUsuariosRelacionadosNavigation() {
		return "verUsuariosRelacionados";
	}
	
	@SecuredAction(keyAction = CommonsActionsKey.ADD, keyMod = CommonsModKeys.SE_USU_ROL)
	public String agregarNavigation() {
		return "agregar";
	}
	
	@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod = CommonsModKeys.SE_USU_ROL)
	public String actualizarNavigation() {
		return "actualizar";
	}
	
	@SecuredAction(keyAction = CommonsActionsKey.DELETE, keyMod = CommonsModKeys.SE_USU_ROL)
	public String eliminarNavigation() {
		return "eliminar";
	}
	
	
	public String relacionarUsuarios() {

		String page = null;

		try {

			boolean relaciono = false;

			for (Entry<Short, Boolean> entry : idsUsuariosSeleccionados.entrySet()) {
				if (entry.getValue()) {

					PerfilUsuario perfilPorUsuario = new PerfilUsuario(
							perfilSeleccionado.getId(), entry.getKey());

					if (serviceDao.getGenericCommonDao().read(PerfilUsuario.class, perfilPorUsuario.getCompositePK()) == null) {
						serviceDao.getGenericCommonDao().create(PerfilUsuario.class, perfilPorUsuario);
					}

					relaciono = true;
				}

			}

			if (relaciono) {
				page = "gestionRoles";
				FacesUtils
						.addFacesMessageFromBundle(
								Constants.OPERACION_EXITOSA_RELACIONAR_USUARIOS_PERFILES,
								FacesMessage.SEVERITY_INFO);
			} else {
				FacesUtils.addFacesMessageFromBundle(
						Constants.ERROR_SELECCIONE_USUARIOS_RELACIONAR,
						FacesMessage.SEVERITY_ERROR);

			}

		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
			page = "";
		}
		return page;

	}

	public void cargaFiltrosDataModel() {
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
		usuariosDataModel = new UsuariosDataModel();
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
		
		if (perfilSeleccionado != null && perfilSeleccionado.getId() != null) {

			Map<String, String> alias = new HashMap<String, String>();
			alias.put("perfiles", "perfiles");
			filtros.add(Restrictions.eq("perfiles.id",	perfilSeleccionado.getId()));
			usuariosRelacionadosDataModel = new UsuariosDataModel();
			usuariosRelacionadosDataModel.setFiltros(filtros);
			usuariosRelacionadosDataModel.setAlias(alias);
		}
	}
	
	public void cargaFiltrosDataModelUsuariosRelacionados() {
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
    	
		if (perfilSeleccionado != null && perfilSeleccionado.getId() != null) {

			Map<String, String> alias = new HashMap<String, String>();
			alias.put("perfiles", "perfiles");
			filtros.add(Restrictions.eq("perfiles.id",	perfilSeleccionado.getId()));
			usuariosRelacionadosDataModel = new UsuariosDataModel();
			usuariosRelacionadosDataModel.setFiltros(filtros);
			usuariosRelacionadosDataModel.setAlias(alias);
		}

	}

	public boolean tienePerfil(Usuario usuario) {
		for (Perfil perfil : usuario.getPerfiles()) {
			if (perfil.getId().equals(perfilSeleccionado.getId())) {
				return true;
			}
		}
		return false;
	}

	
	
	@SuppressWarnings("unchecked")
	public void cargarObjetoRelacionarRecursos(){	
		
			try {
		
				System.out.println("en carga objectos");
				
			if(idRegSeleccionado == null){
				 FacesContext facesContext = FacesContext.getCurrentInstance();
			     String idRegSeleccionado = facesContext.getExternalContext().getRequestParameterMap().get("idRegSeleccionado");
			   
			     if(idRegSeleccionado != null) this.idRegSeleccionado = Integer.parseInt(idRegSeleccionado);
			     
			}
				
			if((perfilSeleccionado == null || perfilSeleccionado.getId() == null)&&  (idRegSeleccionado != null && !idRegSeleccionado.equals(""))){
				perfilSeleccionado =(Perfil) serviceDao.getGenericCommonDao().read(Perfil.class, idRegSeleccionado);			
				
				if(sourceModulesRoots.isEmpty()){					
					sourceModulesRoots = (List<Modulo>) serviceDao.getModulosDao().executeFind(Modulo.NAMED_QUERY_FIND_ROOTS);
				}
				
				if(recursosSeleccionados.isEmpty()){
					Map<Integer, Object> params = new HashMap<Integer, Object>();
					params.put(0, idRegSeleccionado);					
					recursosSeleccionados = (List<Recurso>) serviceDao.getGenericCommonDao().executeFind(Recurso.class, params, Recurso.NAMED_QUERY_FIND_BY_PERFIL);
				}

				
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
	}
	
	public void cargarObjetoSelecionado(){	
		
		try {
	
			
		if(idRegSeleccionado == null){
			 FacesContext facesContext = FacesContext.getCurrentInstance();
		     String idRegSeleccionado = facesContext.getExternalContext().getRequestParameterMap().get("idRegSeleccionado");
		   
		     if(idRegSeleccionado != null) this.idRegSeleccionado = Integer.parseInt(idRegSeleccionado);
		     
		}
			
		if((perfilSeleccionado == null || perfilSeleccionado.getId() == null)&&  (idRegSeleccionado != null && !idRegSeleccionado.equals(""))){
			perfilSeleccionado =(Perfil) serviceDao.getGenericCommonDao().read(Perfil.class, idRegSeleccionado);			
					
		}
	
	} catch (Exception e) {
		e.printStackTrace();
	}	
	
	}
	
	public void selectionChanged(TreeSelectionChangeEvent event) {
		
		
		List<Object> selection = new ArrayList<Object>(	event.getNewSelection());
		Object currentSelectionKey = selection.get(0);
		UITree tree = (UITree) event.getSource();
        
		Object storedKey = tree.getRowKey();
		tree.setRowKey(currentSelectionKey);
		
		 
		Modulo moduloSeleccionado = (Modulo) tree.getRowData();
	    tree.setRowKey(storedKey);
        
        
        if(moduloSeleccionado != null){
        	
        	moduloSeleccionado = (Modulo) serviceDao.getGenericCommonDao().read(Modulo.class, moduloSeleccionado.getId());
        	recursosModulo = moduloSeleccionado.getRecursos();
        }
        
	}
	
	public String  crearRegistro() {
		String page =null;
		try {

			
			serviceDao.getGenericCommonDao().create(Perfil.class, nuevoRegistro);
			
			FacesUtils.addFacesMessageFromBundle(Constants.OPERACION_EXITOSA_CREAR_REGISTRO, FacesMessage.SEVERITY_INFO);
			
			page = "transaccionExitosa";
			nuevoRegistro = new Perfil();
		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION, FacesMessage.SEVERITY_ERROR);
			e.printStackTrace();
		}
		
		return page;
	}
	
	@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod = CommonsModKeys.SE_USU_ROL)
	public String  actualizarRegistro() {
		String page =null;
		try {

			
			serviceDao.getGenericCommonDao().update(Perfil.class, perfilSeleccionado);
			
			FacesUtils.addFacesMessageFromBundle(Constants.OPERACION_EXITOSA_ACTUALIZAR_REGISTRO, FacesMessage.SEVERITY_INFO);
			
			page = "transaccionExitosa";
			perfilSeleccionado = new Perfil();
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
			params.put(0, perfilSeleccionado.getId());
		
			List<PerfilUsuario> perfilUsuario = (List<PerfilUsuario>) serviceDao.getGenericCommonDao().executeFind(PerfilUsuario.class, params, PerfilUsuario.NAMED_QUERY_FIND_BY_PERFIL);
			boolean error = false;
			if (perfilUsuario != null && !perfilUsuario.isEmpty()) {				
				FacesUtils.addFacesMessageFromBundle(Constants.ERROR_ELIMINAR_PERFIL_USUARIO,FacesMessage.SEVERITY_ERROR);	
				error = true;
			} 
			
			List<PerfilRecurso> perfilRecurso = (List<PerfilRecurso>) serviceDao.getGenericCommonDao().executeFind(PerfilRecurso.class, params, PerfilRecurso.NAMED_QUERY_FIND_BY_PERFIL);
			
			if (perfilRecurso != null && !perfilRecurso.isEmpty()) {				
				FacesUtils.addFacesMessageFromBundle(Constants.ERROR_ELIMINAR_PERFIL_RECURSOS,FacesMessage.SEVERITY_ERROR);	
				error = true;
			}
			
			if(error){
				return "";
			}else{
				serviceDao.getGenericCommonDao().delete(Perfil.class,perfilSeleccionado);
				FacesUtils.addFacesMessageFromBundle(Constants.OPERACION_EXITOSA_ELIMINAR_REGISTRO,	FacesMessage.SEVERITY_INFO);
		
				page = "transaccionExitosa";			
	
				perfilSeleccionado = new Perfil();
			}
				
		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
		}

		return page;
	}
	
	


	
	public String guardarPerfilRecursos() {
		String page = "";
		try {
			
			System.out.println(" idRegSeleccionado "  + idRegSeleccionado);
			serviceDao.getModulosDao().eliminarRecursosPerfil(idRegSeleccionado);
			for (Recurso recurso : recursosSeleccionados) {
				serviceDao.getGenericCommonDao().create(PerfilRecurso.class, new PerfilRecurso(idRegSeleccionado, recurso.getId()));
			}
			
			
			ResourceDefine resourceDefine = (ResourceDefine) SpringApplicationContext.getBean("resourceDefine");
			
			if(resourceDefine != null){
				resourceDefine.updateResourceDefine();
			}
			
			page = "gestionRoles";
			FacesUtils
					.addFacesMessageFromBundle(
							Constants.OPERACION_EXITOSA_ACTUALIZAR_REGISTRO,
							FacesMessage.SEVERITY_INFO);
		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
			page = "";
		}
		return page;
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
	
	private static final class PerfilesDataModel extends
			GenericDataModel<Perfil, Integer> {
		private static final long serialVersionUID = 1L;

		private PerfilesDataModel() {
			super(Perfil.class);
			setOrderBy(Order.asc("id"));
		}

		@Override
		protected Object getId(Perfil t) {
			return t.getId();
		}
	}
	public String cargaFiltrosPefilesDataModel(){
    	List<Criterion> filtros = new ArrayList<Criterion>();
    
    	Map<String, String> alias = new HashMap<String, String>();
    	dataModel.setAlias(alias);
    	dataModel.setFiltros(filtros);
    	return "";
	}
		
	public void seleccionarRecurso(Recurso recurso){
		System.out.println("  contains recurso " + recurso.getNombre());
		if(!recursosSeleccionados.contains(recurso)){

			System.out.println("  agrega recurso " + recurso.getNombre());
			recursosSeleccionados.add(recurso);
		}
	}
	
	
	public void quitarRecurso(Recurso recurso){
		if(recursosSeleccionados.contains(recurso)){
			recursosSeleccionados.remove(recurso);
		}
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
	public void setPerfilSeleccionado(Perfil perfilSeleccionado) {
		this.perfilSeleccionado = perfilSeleccionado;
	}

	public Perfil getPerfilSeleccionado() {
		return perfilSeleccionado;
	}

	public void setUsuariosDataModel(
			UsuariosDataModel usuariosDataModel) {
		this.usuariosDataModel = usuariosDataModel;
	}

	public UsuariosDataModel getUsuariosDataModel() {
		cargaFiltrosDataModel();
		return usuariosDataModel;
	}

	public void setIdsUsuariosSeleccionados(
			Map<Short, Boolean> idsUsuariosSeleccionados) {
		this.idsUsuariosSeleccionados = idsUsuariosSeleccionados;
	}

	public Map<Short, Boolean> getIdsUsuariosSeleccionados() {
		return idsUsuariosSeleccionados;
	}

	public void setUsuariosRelacionadosDataModel(
			UsuariosDataModel usuariosRelacionadosDataModel) {
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
	
	public Integer getIdRegSeleccionado() {
		return idRegSeleccionado;
	}

	public void setIdRegSeleccionado(Integer idRegSeleccionado) {
		this.idRegSeleccionado = idRegSeleccionado;
	}

	public List<Modulo> getSourceModulesRoots() {
		return sourceModulesRoots;
	}

	public void setSourceModulesRoots(List<Modulo> sourceModulesRoots) {
		this.sourceModulesRoots = sourceModulesRoots;
	}

	public List<Recurso> getRecursosModulo() {
		return recursosModulo;
	}

	public void setRecursosModulo(List<Recurso> recursosModulo) {
		this.recursosModulo = recursosModulo;
	}

	public List<Recurso> getRecursosSeleccionados() {
		return recursosSeleccionados;
	}

	public PerfilesDataModel getDataModel() {
		cargaFiltrosPefilesDataModel();
		return dataModel;
	}

	public void setDataModel(PerfilesDataModel dataModel) {
		
		this.dataModel = dataModel;
	}

	public void setRecursosSeleccionados(List<Recurso> recursosSeleccionados) {
		
		this.recursosSeleccionados = recursosSeleccionados;
	}

	public Perfil getNuevoRegistro() {
		return nuevoRegistro;
	}

	public void setNuevoRegistro(Perfil nuevoRegistro) {
		this.nuevoRegistro = nuevoRegistro;
	}

	
}
