package la.netco.core.uilayer.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.persistencia.vo.FirmaUsuario;
import la.netco.core.persistencia.vo.Grupo;
import la.netco.core.persistencia.vo.GrupoUsuario;
import la.netco.core.persistencia.vo.Perfil;
import la.netco.core.persistencia.vo.PerfilUsuario;
import la.netco.core.persistencia.vo.Usuario;
import la.netco.core.persistencia.vo.UsuarioEntidad;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.spring.security.CommonsActionsKey;
import la.netco.core.spring.security.CommonsModKeys;
import la.netco.core.spring.security.SecuredAction;
import la.netco.core.spring.security.UserDetailsUtils;
import la.netco.core.uilayer.beans.utils.ExportUtils;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.core.uilayer.commons.GenericDataModel;
import la.netco.sgc.persistence.dto.Entidades;

import org.apache.commons.io.IOUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jasypt.encryption.pbe.StandardPBEByteEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.primefaces.model.UploadedFile;
//import org.richfaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@ManagedBean(name = "usuariosBean")
@ViewScoped
public class UsuariosBean implements Serializable {

	private static final long serialVersionUID = 7671081880606782759L;

	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	private transient ServiceDao serviceDao;
	
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_PBE_STRING_ENCRYPTOR)
	private transient StandardPBEStringEncryptor jasyptPBEStringEncryptor;
	
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_PBE_BYTE_ENCRYPTOR)
	private transient StandardPBEByteEncryptor jasyptPBEByteEncryptor;
	
	private UsuariosDataModel usuariosDataModel;
	private String nombres;
	private String primerApellido;
	private String segundoApellido;
	private String nombreUsuario;
	
	private Map<Integer, Boolean> idsUsuariosSeleccionados = new HashMap<Integer, Boolean>();
	
	private Usuario nuevoUsuario;

	private Usuario usuarioSeleccionado;
	private PerfilUsuario perfilUsuarioSeleccionado;
	private Short idUsuarioSeleccionado;
	
	private String nombre_usuario;
	private String clave;
	private static Logger log = LoggerFactory.getLogger(UsuariosBean.class);
	
	
	private List<SelectItem> perfilesItems ;
	private List<SelectItem> gruposItems;
	private List<SelectItem> entidadesItems;
	
	private List<Integer> perfilesUsuario;
	private List<Integer> gruposUsuario;
	private List<Integer> entidadesUsuario;
	
	private UploadedFile firmaArchivo;
	

	private Integer idRegSeleccionado;
	
	private String claveactual;
	private String clavenueva;
	private String claveverificar;
	
	private Boolean estadoUsuario;
	private boolean habilitaAlertaCheck;
	
	public UsuariosBean(){
		usuariosDataModel = new UsuariosDataModel();
		nuevoUsuario= new Usuario();
		usuarioSeleccionado = new Usuario();
		perfilUsuarioSeleccionado =  new PerfilUsuario();
	}
	
	private static final String ACTION_RELACIONAR_GRUPO = "UPDATE_G";
	private static final String ACTION_RELACIONAR_PERFIL = "UPDATE_P";

	

	private Integer valorPrueba;
	
	
	/*
	 * Metodos que retonar la regla de navegacion. Tinene la anotacion @SecuredAction que valida si el usuario
	 * registrado tiene permisos sobre la accion
	 *
	 * */
	
	@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod = CommonsModKeys.SE_USU_USU)
	public String actualizarNavigation() {		
		return "actualizar";
	}
	
	@SecuredAction(keyAction = CommonsActionsKey.DELETE, keyMod = CommonsModKeys.SE_USU_USU)
	public String eliminarNavigation() {
		return "eliminar";
	}
	
	@SecuredAction(keyAction = CommonsActionsKey.ADD, keyMod = CommonsModKeys.SE_USU_USU)
	public String agregarNavigation() {
		return "agregar";
	}
	
	@SecuredAction(keyAction = CommonsActionsKey.VIEW, keyMod = CommonsModKeys.SE_USU_USU)
	public String detalleNavigation() {
		return "detalle";
	}
	
	@SecuredAction(keyAction = ACTION_RELACIONAR_PERFIL, keyMod = CommonsModKeys.SE_USU_USU)
	public String relacionarperfilesNavigation() {
		return "relacionarperfiles";
	}
	
	@SecuredAction(keyAction = ACTION_RELACIONAR_GRUPO, keyMod = CommonsModKeys.SE_USU_USU)
	public String relacionargruposNavigation() {
		return "relacionargrupos";
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
	
	@SecuredAction(keyAction = CommonsActionsKey.ADD, keyMod = CommonsModKeys.SE_USU_USU)
	public String  crearUsuario() {
		String page =null;
		try {

			nuevoUsuario.setUsrFreg(new Date(System.currentTimeMillis()));

			nuevoUsuario.setUsrLact(estadoUsuario);	
			Usuario userAux = serviceDao.getUsuariosDao().findUsuarioByUsername((nuevoUsuario.getUsrLog()));
				
			if (userAux != null) {
					FacesUtils.addFacesMessageFromBundle(Constants.ERROR_NOMBRE_USUARIO_EXITE,FacesMessage.SEVERITY_ERROR);
					return page;
			}

			nuevoUsuario.setUsrCon(jasyptPBEStringEncryptor.encrypt(nuevoUsuario.getUsrCon()));
			Short idUsuario  = (Short) serviceDao.getUsuariosDao().create(Usuario.class,nuevoUsuario);

			
			if (firmaArchivo != null) {
				FirmaUsuario firma = new FirmaUsuario();
				firma.setUsuario(nuevoUsuario);
				byte [] input = IOUtils.toByteArray(firmaArchivo.getInputstream());
				firma.setFirmaStream(jasyptPBEByteEncryptor.encrypt(input));
				serviceDao.getGenericCommonDao().create(FirmaUsuario.class, firma);
			}
			
			if(idUsuario != null && !idUsuario.equals(0)){
				for (Integer perfil : perfilesUsuario) {
					PerfilUsuario perfilPorUsuario = new PerfilUsuario(perfil, idUsuario.shortValue());
					serviceDao.getGenericCommonDao().create(PerfilUsuario.class, perfilPorUsuario);
				}
				
				for (Integer grupo : gruposUsuario) {
					GrupoUsuario grupoPorUsuario = new GrupoUsuario(grupo, idUsuario.shortValue());
					serviceDao.getGenericCommonDao().create(GrupoUsuario.class, grupoPorUsuario);
				}
				
				for (Integer entidad : entidadesUsuario) {
					UsuarioEntidad entidadPorUsuario = new UsuarioEntidad(idUsuario ,entidad);
					serviceDao.getGenericCommonDao().create(UsuarioEntidad.class, entidadPorUsuario);
				}
			}			
			
			FacesUtils.addFacesMessageFromBundle(Constants.OPERACION_EXITOSA_CREAR_USUARIO,	FacesMessage.SEVERITY_INFO);

			page = "transaccionExitosa";
			nuevoUsuario = new Usuario();
			
			limpiar();
		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
		}
		
		return page;
	}
	
	
	@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod = CommonsModKeys.SE_USU_USU)
	public String actualizarUsuario() {
		String page = null;
		try {

			usuarioSeleccionado.setUsrFhact(new Date(System.currentTimeMillis()));
				
			Usuario userAux = null;
	
			if(!nombre_usuario.equals(usuarioSeleccionado.getUsrLog())){
					userAux = serviceDao.getUsuariosDao().findUsuarioByUsername(nombre_usuario);
			}
				
			if (userAux != null) {
					FacesUtils.addFacesMessageFromBundle(Constants.ERROR_NOMBRE_USUARIO_EXITE,FacesMessage.SEVERITY_ERROR);
					return page;
			}

			usuarioSeleccionado.setUsrCon(jasyptPBEStringEncryptor.encrypt(clave));//TODO verificar
			usuarioSeleccionado.setUsrLog(nombre_usuario);
			
			serviceDao.getUsuariosDao().update(Usuario.class,usuarioSeleccionado);
			
			System.out.println("Procesar firma!");
			if (firmaArchivo != null) {
				System.out.println("No es vacio");
				Map<Integer, Short> params = new HashMap<Integer, Short>();
				params.put(0, usuarioSeleccionado.getUsrId());
				byte[] input = null;
				input = IOUtils.toByteArray(firmaArchivo.getInputstream());
				FirmaUsuario firma = (FirmaUsuario) serviceDao.getGenericCommonDao().executeUniqueResult(FirmaUsuario.NAMED_QUERY_FIND_BY_USUARIO, params);
				if(firma != null){
					System.out.println("ya tenia firma; se actualiza");
					firma.setFirmaStream(jasyptPBEByteEncryptor.encrypt(input));
					firma.setMimeType(firmaArchivo.getContentType());
					serviceDao.getGenericCommonDao().update(FirmaUsuario.class,  firma);
				} else {
					System.out.println("no tenia firma; se crea");
					firma = new FirmaUsuario();
					firma.setUsuario(usuarioSeleccionado);
					firma.setFirmaStream(jasyptPBEByteEncryptor.encrypt(input));
					firma.setMimeType(firmaArchivo.getContentType());
					serviceDao.getGenericCommonDao().create(FirmaUsuario.class, firma);
				}
			}
			
			
			if(usuarioSeleccionado.getUsrId() != 0 ){
				
				//ELIMINA PERFILES
				for (Perfil perfil : usuarioSeleccionado.getPerfiles()) {
					PerfilUsuario perfilPorUsuario = new PerfilUsuario(perfil.getId(), usuarioSeleccionado.getUsrId());
					serviceDao.getGenericCommonDao().delete(PerfilUsuario.class, perfilPorUsuario);
				}
				
				//ELIMINA GRUPOS RELACIONADOS  ACTUALES
				for (Grupo grupo : usuarioSeleccionado.getGrupos()) {
					GrupoUsuario perfilPorUsuario = new GrupoUsuario(grupo.getId(), usuarioSeleccionado.getUsrId());
					serviceDao.getGenericCommonDao().delete(GrupoUsuario.class, perfilPorUsuario);
				}	
				
				//ELIMINA ENTIDADES RELACIONADOS  ACTUALES
				for (Entidades entidad  : usuarioSeleccionado.getEntidades()) {
					UsuarioEntidad entidadPorUsuario = new UsuarioEntidad(usuarioSeleccionado.getUsrId(),entidad.getEntCodigo());
					serviceDao.getGenericCommonDao().delete(UsuarioEntidad.class, entidadPorUsuario);
				}	
				//AGREGA PERFILES
				for (Integer perfil : perfilesUsuario) {
					PerfilUsuario perfilPorUsuario = new PerfilUsuario(perfil, usuarioSeleccionado.getUsrId() );
					serviceDao.getGenericCommonDao().create(PerfilUsuario.class, perfilPorUsuario);
				}
				
				//RELACIONA LA LISTA DE GRUPOS
				for (Integer perfil : gruposUsuario) {
					GrupoUsuario perfilPorUsuario = new GrupoUsuario(perfil, usuarioSeleccionado.getUsrId());
					serviceDao.getGenericCommonDao().create(GrupoUsuario.class, perfilPorUsuario);
				}
				
				//RELACIONA LA LISTA DE ENTIDADES
				for (Integer entidad : entidadesUsuario) {
					UsuarioEntidad entidadPorUsuario = new UsuarioEntidad(usuarioSeleccionado.getUsrId() ,entidad);
					serviceDao.getGenericCommonDao().create(UsuarioEntidad.class, entidadPorUsuario);
				}
			}			

			FacesUtils.addFacesMessageFromBundle(
					Constants.OPERACION_EXITOSA_ACTUALIZAR_USUARIO,
					FacesMessage.SEVERITY_INFO);

			page = "transaccionExitosa";


			usuarioSeleccionado = new Usuario();
			limpiar();
		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,	FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
		}

		return page;
	}
	
	
	public void cargarRegistro(){	
		
		try {						
			if(idRegSeleccionado == null){
				 FacesContext facesContext = FacesContext.getCurrentInstance();
			     String idRegSeleccionado = facesContext.getExternalContext().getRequestParameterMap().get("idRegSeleccionado");			   
			     if(idRegSeleccionado != null) this.idRegSeleccionado = Integer.parseInt(idRegSeleccionado);		
			}
				
			if((usuarioSeleccionado == null || usuarioSeleccionado.getUsrId() == null || usuarioSeleccionado.getUsrId().equals(0))&&  (idRegSeleccionado != null && !idRegSeleccionado.equals(""))){
				usuarioSeleccionado =(Usuario) serviceDao.getGenericCommonDao().read(Usuario.class, idRegSeleccionado.shortValue());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	
	@SecuredAction(keyAction = CommonsActionsKey.DELETE, keyMod = CommonsModKeys.SE_USU_USU)
	public String eliminarUsuario() {
		String page = null;
		try {

			Usuario 	userAux = (Usuario) serviceDao.getUsuariosDao().read(Usuario.class,usuarioSeleccionado.getUsrId());
			boolean error = false;
			if (userAux != null && userAux.getPerfiles() != null  && !userAux.getPerfiles().isEmpty()) {				
				FacesUtils.addFacesMessageFromBundle(Constants.ERROR_ELIMINAR_USUARIO_PERFILES,FacesMessage.SEVERITY_ERROR);	
				 error = true;
			} 

			
	
			List<Criterion> filtros = new ArrayList<Criterion>();	
			Criterion asignador = Restrictions.eq("usuario_asignador.idUsuario",  userAux.getUsrId());
			Criterion asignado= Restrictions.eq("usuario_asignado.idUsuario", userAux.getUsrId());
			LogicalExpression orExp = Restrictions.or(asignado,asignador);
			filtros.add(orExp);
	
			if(!error){
			serviceDao.getUsuariosDao().delete(Usuario.class,userAux);
			FacesUtils.addFacesMessageFromBundle(
					Constants.OPERACION_EXITOSA_ELIMINAR_USUARIO,
					FacesMessage.SEVERITY_INFO);

			page = "transaccionExitosa";
			}

			usuarioSeleccionado = new Usuario();
		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
		}

		return page;
	}
	
	

	
	@SecuredAction(keyAction = ACTION_RELACIONAR_PERFIL, keyMod = CommonsModKeys.SE_USU_USU)
	public String  relacionarUsuario() {
		String page =null;
		try {
			
			//ELIMINA PERFILES RELACIONADOS  ACTUALES
			for (Perfil perfil : usuarioSeleccionado.getPerfiles()) {
				PerfilUsuario perfilPorUsuario = new PerfilUsuario(perfil.getId(), usuarioSeleccionado.getUsrId());
				serviceDao.getGenericCommonDao().delete(PerfilUsuario.class, perfilPorUsuario);
			}
			
			//RELACIONA LA LISTA DE PERFILES
			for (Integer perfil : perfilesUsuario) {
				PerfilUsuario perfilPorUsuario = new PerfilUsuario(perfil, usuarioSeleccionado.getUsrId());
				serviceDao.getGenericCommonDao().create(PerfilUsuario.class, perfilPorUsuario);
			}
			
			//ACTUALIZACION DE USUARIO
			serviceDao.getGenericCommonDao().update(Usuario.class, usuarioSeleccionado);
			
			FacesUtils.addFacesMessageFromBundle(Constants.OPERACION_EXITOSA_ACTUALIZAR_REGISTRO,	FacesMessage.SEVERITY_INFO);

			page = "transaccionExitosa";
			usuarioSeleccionado = new Usuario();
		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
		}
		
		return page;
	}
	
	
	@SecuredAction(keyAction = ACTION_RELACIONAR_GRUPO, keyMod = CommonsModKeys.SE_USU_USU)
	public String  relacionarGrupos() {
		String page =null;
				try {
					
					//ELIMINA PERFILES RELACIONADOS  ACTUALES
					for (Grupo grupo : usuarioSeleccionado.getGrupos()) {
						GrupoUsuario perfilPorUsuario = new GrupoUsuario(grupo.getId(), usuarioSeleccionado.getUsrId());
						serviceDao.getGenericCommonDao().delete(GrupoUsuario.class, perfilPorUsuario);
					}
					
					for (Object perfil : gruposUsuario) {
						System.out.println(" perfil " + perfil);
						System.out.println("  usuarioSeleccionado.getUsrId()  " +  usuarioSeleccionado.getUsrId());
					}
					
					//RELACIONA LA LISTA DE PERFILES
					for (Integer perfil : gruposUsuario) {
						GrupoUsuario perfilPorUsuario = new GrupoUsuario(perfil, usuarioSeleccionado.getUsrId());
						serviceDao.getGenericCommonDao().create(GrupoUsuario.class, perfilPorUsuario);
					}
					
					FacesUtils.addFacesMessageFromBundle(Constants.OPERACION_EXITOSA_ACTUALIZAR_REGISTRO,	FacesMessage.SEVERITY_INFO);

					page = "transaccionExitosa";
					usuarioSeleccionado = new Usuario();
				} catch (Exception e) {
					FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
							FacesMessage.SEVERITY_FATAL);
					e.printStackTrace();
				}
				
				return page;
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
    	
    	Map<String, String> alias = new HashMap<String, String>();
    	usuariosDataModel = new UsuariosDataModel();    	
    	usuariosDataModel.setFiltros(filtros);
    	usuariosDataModel.setAlias(alias);
    }
    
	

	
	public void cambiarClave(){
		try {
		Usuario usuario = UserDetailsUtils.usuarioLogged();
		String claveUsuario = usuario.getUsrCon();
		claveUsuario =  jasyptPBEStringEncryptor.decrypt(claveUsuario);
			if(claveactual!= null && !claveactual.equals(claveUsuario)){
				FacesUtils.addFacesMessageFromBundle(Constants.ERROR_CLAVE_ACTUAL,FacesMessage.SEVERITY_ERROR);
				return;
			}
			
			if(!clavenueva.equals(claveverificar)){
				FacesUtils.addFacesMessageFromBundle(Constants.ERROR_VERIFICAR_CLAVE,FacesMessage.SEVERITY_ERROR);
				return;
			}
			usuario.setUsrCon(jasyptPBEStringEncryptor.encrypt(clavenueva));//TODO verificar
			serviceDao.getUsuariosDao().update(Usuario.class, usuario);
			
			
			FacesUtils.addFacesMessageFromBundle(Constants.OPERACION_EXITOSA_CLAVE,	FacesMessage.SEVERITY_INFO);

			
			
		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
		}	
	}
	
	public void limpiar(){
		nombres = null;
		primerApellido = null;
		segundoApellido = null;
		nombreUsuario = null;
		nuevoUsuario = new Usuario();
		usuarioSeleccionado = new Usuario();
	}
	
	public List<Object> getAllUsersLogged(){
		return UserDetailsUtils.allUsersLogged();
	}

	
	public void exportarXLS(){
		try {
			cargaFiltrosDataModel();	
		UsuariosDataModel data = this.usuariosDataModel;
		List<Usuario>  datos =data.getAllRows();
		ExportUtils.exportarXls(Constants.TEMPLATE_XLS_USUARIOS_PERFILES,datos,"usuario");
		
		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_EXPORTAR_REPORTE, FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
		}
	}
	
	public boolean isTieneUsuarioFirma(){
		if (usuarioSeleccionado != null
				&& usuarioSeleccionado.getUsrId() != 0) {
			Map<Integer, Short> params = new HashMap<Integer, Short>();
			params.put(0, usuarioSeleccionado.getUsrId());
			
			FirmaUsuario firma = (FirmaUsuario) serviceDao.getGenericCommonDao().executeUniqueResult(FirmaUsuario.NAMED_QUERY_FIND_BY_USUARIO, params);
			if (firma != null)
				return true;
		}
		return false;
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

	public void setUsuariosDataModel(UsuariosDataModel usuariosDataModel) {
		this.usuariosDataModel = usuariosDataModel;
	}

	public UsuariosDataModel getUsuariosDataModel() {
		cargaFiltrosDataModel();
		return usuariosDataModel;
	}

	public void setIdsUsuariosSeleccionados(Map<Integer, Boolean> idsUsuariosSeleccionados) {
		this.idsUsuariosSeleccionados = idsUsuariosSeleccionados;
	}

	public Map<Integer, Boolean> getIdsUsuariosSeleccionados() {
		return idsUsuariosSeleccionados;
	}




	public void setNuevoUsuario(Usuario nuevoUsuario) {
		this.nuevoUsuario = nuevoUsuario;
	}




	public Usuario getNuevoUsuario() {
		return nuevoUsuario;
	}
	
	public void setJasyptPBEStringEncryptor(StandardPBEStringEncryptor jasyptPBEStringEncryptor) {
		this.jasyptPBEStringEncryptor = jasyptPBEStringEncryptor;
	}



	public StandardPBEStringEncryptor getJasyptPBEStringEncryptor() {
		return jasyptPBEStringEncryptor;
	}

	public Usuario getUsuarioSeleccionado() {
		return usuarioSeleccionado;
	}

	public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
		this.usuarioSeleccionado = usuarioSeleccionado;
	}


	public void setNombre_usuario(String nombre_usuario) {
		this.nombre_usuario = nombre_usuario;
	}

	public String getNombre_usuario() {
		if(usuarioSeleccionado.getUsrId() != 0 && nombre_usuario == null ){
			nombre_usuario = usuarioSeleccionado.getUsrLog();
		}
		
		return nombre_usuario;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}



	public String getClave() {
		if(usuarioSeleccionado.getUsrId() != 0 && clave == null ){
			try {
				String claveUsuario = usuarioSeleccionado.getUsrCon();
				clave =  jasyptPBEStringEncryptor.decrypt(claveUsuario);
			} catch (EncryptionOperationNotPossibleException e) {
				log.info("NO ES POSIBLE DESENCRIPTAR LA CADENA : " + usuarioSeleccionado.getUsrCon());
			}
		
		}
		return clave;
	}



	public void setPerfilesItems(List<SelectItem> perfilesItems) {
		this.perfilesItems = perfilesItems;
	}


	@SuppressWarnings("unchecked")
	public List<SelectItem> getPerfilesItems() {
		if (perfilesItems == null) {


			List<Perfil> allPerfiles = (List<Perfil>) serviceDao.getGenericCommonDao().loadAll(Perfil.class);
			perfilesItems = new ArrayList<SelectItem>();
			for (Perfil perfil : allPerfiles) {
				perfilesItems.add(new SelectItem(perfil.getId(), perfil
						.getNombre()));
			}

		}

		return perfilesItems;
	}



	public void setPerfilesUsuario(List<Integer> perfilesUsuario) {
		this.perfilesUsuario = perfilesUsuario;
	}


	public List<Integer> getPerfilesUsuario() {
		if (perfilesUsuario == null && usuarioSeleccionado != null	&& usuarioSeleccionado.getUsrId() != null && usuarioSeleccionado.getUsrId() != 0) {
			Set<Perfil> allPerfiles = usuarioSeleccionado.getPerfiles();
			perfilesUsuario = new ArrayList<Integer>();
			for (Perfil perfil : allPerfiles) {
				perfilesUsuario.add(perfil.getId());
			}
		}

		return perfilesUsuario;
	}
	
	public void setJasyptPBEByteEncryptor(StandardPBEByteEncryptor jasyptPBEByteEncryptor) {
		this.jasyptPBEByteEncryptor = jasyptPBEByteEncryptor;
	}
	public StandardPBEByteEncryptor getJasyptPBEByteEncryptor() {
		return jasyptPBEByteEncryptor;
	}
	public void setGruposUsuario(List<Integer> gruposUsuario) {
		this.gruposUsuario = gruposUsuario;
	}
	public List<Integer> getGruposUsuario() {
		if (gruposUsuario == null && usuarioSeleccionado != null && usuarioSeleccionado.getUsrId() != null
				&& usuarioSeleccionado.getUsrId() != 0) {
			Set<Grupo> allPerfiles = usuarioSeleccionado.getGrupos();
			gruposUsuario = new ArrayList<Integer>();
			for (Grupo perfil : allPerfiles) {
				gruposUsuario.add(perfil.getId());
			}
		}
		return gruposUsuario;
	}
	public void setGruposItems(List<SelectItem> gruposItems) {
		this.gruposItems = gruposItems;
	}
	@SuppressWarnings("unchecked")
	public List<SelectItem> getGruposItems() {
		if (gruposItems == null) {
			List<Grupo> allPerfiles = (List<Grupo>) serviceDao.getGenericCommonDao().loadAll(Grupo.class);
			gruposItems = new ArrayList<SelectItem>();
			for (Grupo perfil : allPerfiles) {
				gruposItems.add(new SelectItem(perfil.getId(), perfil.getNombre()));
			}

		}

		return gruposItems;
	}

	public Short getIdUsuarioSeleccionado() {
		return idUsuarioSeleccionado;
	}

	public void setIdUsuarioSeleccionado(Short idUsuarioSeleccionado) {
		this.idUsuarioSeleccionado = idUsuarioSeleccionado;
	}

	public Integer getValorPrueba() {
		return valorPrueba;
	}

	public void setValorPrueba(Integer valorPrueba) {
		this.valorPrueba = valorPrueba;
	}

	public Integer getIdRegSeleccionado() {
		return idRegSeleccionado;
	}

	public void setIdRegSeleccionado(Integer idRegSeleccionado) {
		this.idRegSeleccionado = idRegSeleccionado;
	}

	public String getClaveactual() {
		return claveactual;
	}

	public String getClavenueva() {
		return clavenueva;
	}

	public String getClaveverificar() {
		return claveverificar;
	}

	public void setClaveactual(String claveactual) {
		this.claveactual = claveactual;
	}

	public void setClavenueva(String clavenueva) {
		this.clavenueva = clavenueva;
	}

	public void setClaveverificar(String claveverificar) {
		this.claveverificar = claveverificar;
	}

	public List<Integer> getEntidadesUsuario() {
		if (entidadesUsuario == null && usuarioSeleccionado != null && usuarioSeleccionado.getUsrId() != null && usuarioSeleccionado.getUsrId() != 0) {
			Set<Entidades> allData = usuarioSeleccionado.getEntidades();
			entidadesUsuario = new ArrayList<Integer>();
			for (Entidades data : allData) {
				entidadesUsuario.add(data.getEntCodigo());
			}
		}
		return entidadesUsuario;
	}

	public void setEntidadesUsuario(List<Integer> entidadesUsuario) {
		this.entidadesUsuario = entidadesUsuario;
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getEntidadesItems() {
		if (entidadesItems == null) {
			List<Entidades> allData = (List<Entidades>) serviceDao.getGenericCommonDao().loadAll(Entidades.class);
			entidadesItems = new ArrayList<SelectItem>();
			for (Entidades data : allData) {
				entidadesItems.add(new SelectItem(data.getEntCodigo(), data.getEntPjuridica()));
			}

		}
		return entidadesItems;
	}

	public void setEntidadesItems(List<SelectItem> entidadesItems) {
		this.entidadesItems = entidadesItems;
	}

	public Boolean getEstadoUsuario() {
		return estadoUsuario;
	}

	public void setEstadoUsuario(Boolean estadoUsuario) {
		this.estadoUsuario = estadoUsuario;
	}

	/**
	 * @return the habilitaAlertaCheck
	 */
	public boolean isHabilitaAlertaCheck() {
		return habilitaAlertaCheck;
	}

	/**
	 * @param habilitaAlertaCheck the habilitaAlertaCheck to set
	 */
	public void setHabilitaAlertaCheck(boolean habilitaAlertaCheck) {
		this.habilitaAlertaCheck = habilitaAlertaCheck;
	}

	/**
	 * @return the perfilUsuarioSeleccionado
	 */
	public PerfilUsuario getPerfilUsuarioSeleccionado() {
		return perfilUsuarioSeleccionado;
	}

	/**
	 * @param perfilUsuarioSeleccionado the perfilUsuarioSeleccionado to set
	 */
	public void setPerfilUsuarioSeleccionado(PerfilUsuario perfilUsuarioSeleccionado) {
		this.perfilUsuarioSeleccionado = perfilUsuarioSeleccionado;
	}

	public UploadedFile getFirmaArchivo() {
		return firmaArchivo;
	}

	public void setFirmaArchivo(UploadedFile firmaArchivo) {
		this.firmaArchivo = firmaArchivo;
	}
	
	
}
