package la.netco.uilayer.beans;

import java.io.Serializable;
import java.net.IDN;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;










import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.persistencia.vo.Usuario;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.spring.security.CommonsActionsKey;
import la.netco.core.spring.security.CommonsModKeys;
import la.netco.core.spring.security.SecuredAction;
import la.netco.core.uilayer.beans.Constants;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.persistencia.dto.commons.Lugar;
import la.netco.persistencia.dto.commons.Paises;
import la.netco.persistencia.dto.commons.Persona;




import la.netco.persistencia.dto.commons.Tiposdocumento;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @author ediaz
 *
 */
@ManagedBean(name = "personaNaturalBean")
@ViewScoped
public class PersonaNaturalBean implements Serializable {
	/*Log*/
	private static Logger log = LoggerFactory.getLogger(PersonaNaturalBean.class);
	 
	
	/*Atributos de nivel Visual Filtros*/
	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;
	private PersonaDataModel listDatamodel = null;
	private String numIdentificacion;
	private String nombres;
	private String primerApellido;
	private String segundoApellido;
	private String seudonimo;
	private Integer usuarioBorrar;
	
	
	/*Atributos de Nivel Visual Modal Personas Creacion*/
	private List<SelectItem> tiposDocumentosItems;
	private Integer idTipoDocumentoSol;
	private boolean muestrese = true;
	private List<SelectItem> paisesItems;
	private Short idPaisSeleccionado;
	private List<SelectItem> lugaresItems;
	private Short idLugarSeleccionado;
	private Persona persona;
	private boolean mostrarModal = false;
	private String mensaje;
	
	/*Control de edicin*/
	private Integer idPerSeleccionada;
	private Persona personaSeleccionada;
	
	/*Valores predeterminados creacin*/
	private static final Integer TAG_TIPO_DOCUMENTO = 7;
	private static final Short TAG_PAIS_PREDETERMINADO = 1;
	private static final Short TAG_LUGAR_PREDETERMINADO = 1;
	
	
	/*Constructor*/
	
	public PersonaNaturalBean() {
		persona = new Persona();
		personaSeleccionada = new Persona();
	}

	public Integer getUsuarioBorrar() {
		return usuarioBorrar;
	}

	public void setUsuarioBorrar(Integer usuarioBorrar) {
		this.usuarioBorrar = usuarioBorrar;
	}
	/**
	 * Busqueda de personas, segn los atributos dados en pantall
	 */
	public void buscar(){
		log.info("Ingreso a buscar Personas");
		List<Criterion> filtros = new ArrayList<Criterion>();
		if(numIdentificacion != null  && !numIdentificacion.trim().equals("") ){
    		filtros.add(Restrictions.eq("perDoc",  numIdentificacion));  
    	}
		
		if(nombres != null  && !nombres.trim().equals("") ){
    		filtros.add(Restrictions.ilike("perNom", "%" + nombres + "%"));  
    	}
		
		if(primerApellido != null  && !primerApellido.trim().equals("") ){
    		filtros.add(Restrictions.ilike("perPap",  "%" + primerApellido + "%"));  
    	}
		
		if(segundoApellido != null  && !segundoApellido.trim().equals("") ){
    		filtros.add(Restrictions.ilike("perSap",  "%" + segundoApellido + "%"));  
    	}
		
		if(seudonimo != null  && !seudonimo.trim().equals("") ){
    		filtros.add(Restrictions.ilike("perSeunom",  "%" + seudonimo + "%"));  
    	}
		
		filtros.add(Restrictions.isNotNull("perDoc"));
		filtros.add(Restrictions.ne("perDoc",  ""));  
		
		filtros.add(Restrictions.isNotNull("perNom"));
		filtros.add(Restrictions.ne("perNom",  "")); 
		
		filtros.add(Restrictions.isNotNull("perPap"));
		filtros.add(Restrictions.ne("perPap",  "")); 
		
		
		Map<String, String> alias = new HashMap<String, String>();
		listDatamodel.setAlias(alias);
		listDatamodel.setFiltros(filtros);
	}

	/**	
	 * Carga listado inicial 
	 */
	public void cargaListaDataModel() {
		if (listDatamodel == null) {
			listDatamodel = new PersonaDataModel();
			List<Criterion> filtros = new ArrayList<Criterion>();
			Map<String, String> alias = new HashMap<String, String>();
			
			
			filtros.add(Restrictions.isNotNull("perDoc"));
			filtros.add(Restrictions.ne("perDoc",  ""));  
			
			filtros.add(Restrictions.isNotNull("perNom"));
			filtros.add(Restrictions.ne("perNom",  "")); 
			
			filtros.add(Restrictions.isNotNull("perPap"));
			filtros.add(Restrictions.ne("perPap",  "")); 
			
			listDatamodel.setAlias(alias);
			listDatamodel.setFiltros(filtros);
		}
	}

	public void nada() {
		//Mtodo usado para hacer doble invocacin
		//para confirmar borrar un usuario antes de 
		//hacerlo realmente.
	}
	
	public void eliminarUsuario() {
		System.out.println("eliminarUsuario... "+usuarioBorrar);
		if (usuarioBorrar == null) return;
		
		Connection con = serviceDao.getGenericCommonDao().connectionFromHibernate();
		try {
			CallableStatement stm = con.prepareCall("{call dbo.PaEliPersona(?, ?, ?)}");
			int i=1;
			stm.setInt(i++, usuarioBorrar);
			stm.registerOutParameter(i++, Types.INTEGER);
			stm.registerOutParameter(i++, Types.VARCHAR);
			stm.setQueryTimeout(60);
			stm.execute();
			
			int error = stm.getInt(2);
			String mensaje = stm.getString(3);
			mensaje = mensaje.replace("'", "\"");
			System.out.println("error="+error);
			RequestContext.getCurrentInstance().execute("alert('"+mensaje+"')");
		} catch (Exception e) {
			RequestContext.getCurrentInstance().execute("alert('Error: "+e.getMessage()+"')");
			e.printStackTrace();
		}
	}
	
	public void inicializar (){
		if(persona != null){
		persona = new Persona();
		}
		limpiar();
		
		
	}
	/**
	 * Crear o Actualizar una Persona
	 */
	public String createOrReplace(){
		String page = null;
		try {
			log.info("Ingreso a crear Persona" + persona.getPerDoc());
			if (idTipoDocumentoSol != null) {
				Tiposdocumento tipoDocumento = (Tiposdocumento) serviceDao
						.getGenericCommonDao().read(Tiposdocumento.class,
								idTipoDocumentoSol);
				persona.setTiposdocumento(tipoDocumento);
			}
			if (idLugarSeleccionado != null){
				Lugar lugar = (Lugar) serviceDao.getGenericCommonDao().read(
						Lugar.class, idLugarSeleccionado);
				persona.setLugarByPerLug(lugar);
			}
			if(idPaisSeleccionado != null){
				Paises pais = (Paises) serviceDao.getGenericCommonDao().read(
						Paises.class, idPaisSeleccionado);
				persona.setPaises(pais);
			}							
			//serviceDao.getGenericCommonDao().saveOrUpdate(Persona.class, persona);
			Integer idPersona = (Integer) serviceDao.getGenericCommonDao().create(Persona.class, persona);
			log.info("ID PERSONA" + idPersona);
			FacesUtils.addFacesMessageFromBundle(Constants.OPERACION_EXITOSA_CREAR_USUARIO,	FacesMessage.SEVERITY_INFO);
			page = "transaccionExitosa";
			persona = new Persona();
			/*mostrarModal = true;
			mensaje = "Registro Creado";*/
			limpiar();
			
			
			
		} catch(Exception exception){
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,	FacesMessage.SEVERITY_FATAL);
			log.error(exception.getMessage());
			
		}
		return page;
	
	}
	/**
	 * Actualiza Persona Natural
	 */
	public String actualizarPersona(){
		String page = null;
		try {
			log.info("Ingreso a actualizar Persona" + personaSeleccionada.getPerId());
			if(personaSeleccionada != null && personaSeleccionada.getPerId()>0){					
			serviceDao.getGenericCommonDao().saveOrUpdate(Persona.class, personaSeleccionada);
			FacesUtils.addFacesMessageFromBundle(
					Constants.OPERACION_EXITOSA_ACTUALIZAR_USUARIO,
					FacesMessage.SEVERITY_INFO);
			personaSeleccionada = new Persona();
			limpiar();
			}else{
				FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,	FacesMessage.SEVERITY_FATAL);
				
			}
			
			
			page = "transaccionExitosa";
		} catch(Exception exception){
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,	FacesMessage.SEVERITY_FATAL);
			log.error(exception.getMessage());
			
		}
		return page;
	
	}
	
	/**
	 * Eliminar Persona Natural
	 */
	public String eliminarPersona(){
		String page = null;
		try {
			log.info("Ingreso a eliminar Persona" + personaSeleccionada.getPerId());
			if(personaSeleccionada != null){					
			serviceDao.getGenericCommonDao().delete(Persona.class, personaSeleccionada);
			}
			FacesUtils.addFacesMessageFromBundle(
					Constants.OPERACION_EXITOSA_ELIMINAR_USUARIO,
					FacesMessage.SEVERITY_INFO);
			page = "transaccionExitosa";
			personaSeleccionada = new Persona();
			
		} catch(Exception exception){
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,	FacesMessage.SEVERITY_FATAL);
			log.error(exception.getMessage());
			
		}
		return page;
	
	}
	public void cargarPrederteminado(){
		if (!FacesContext.getCurrentInstance().isPostback()) {
			/*Setear valores por Default*/
			log.info("Set Datos Default");
			idTipoDocumentoSol = TAG_TIPO_DOCUMENTO;
			idPaisSeleccionado = TAG_PAIS_PREDETERMINADO;
			idLugarSeleccionado = TAG_LUGAR_PREDETERMINADO;
	    }
		
	
	}
	
	public void buttonAction(ActionEvent actionEvent) {
		log.info("HOLA HOLA");
    }
	/*
	 * Inicializa Objetos utilizados
	 * */
	public void limpiar(){
		persona = new Persona();
		personaSeleccionada = new Persona();
	}
	
	/**
	 * Cargar Tipos de documentos
	 */
	@SuppressWarnings("unchecked")
	public void cargarTiposDocItems() {

		if (tiposDocumentosItems == null) {

			List<Tiposdocumento> allData = (List<Tiposdocumento>) serviceDao
					.getGenericCommonDao().executeFind(
							Tiposdocumento.NAMED_QUERY_ALL);

			tiposDocumentosItems = new ArrayList<SelectItem>();

			for (Tiposdocumento dato : allData) {
				tiposDocumentosItems.add(new SelectItem(dato.getTdoId(), dato
						.getTdoNom()));
			}

		} else if (tiposDocumentosItems == null) {
			tiposDocumentosItems = new ArrayList<SelectItem>();
		}
	}
	/**
	 * Crear persona No documentadas
	 */
	public void valueChangedTipoDoc(){
		log.info("change tipo documento " + idTipoDocumentoSol);
		if(idTipoDocumentoSol == 15){
			muestrese = false;
		} else{
			muestrese = true;
		}
		log.info("bandera " + muestrese);
	}
	
	/**
	 * Cambio tipo de nacionalidad
	 */
	public void valueChangedNacionalidad(){
		log.info("change Nacionalidad " + idPaisSeleccionado);
		
	}
	/**
	 * Cambio tipo de nacionalidad
	 */
	public void valueChangedLugar(){
		log.info("change Lugar " + idLugarSeleccionado);
		
	}
	/**
	 * Cargar lista de paises
	 */
	@SuppressWarnings("unchecked")
	public void cargaPaisesItems() {
		if (paisesItems == null) {
			List<Paises> allData = (List<Paises>) serviceDao
					.getGenericCommonDao().executeFind(Paises.NAMED_QUERY_ALL);

			paisesItems = new ArrayList<SelectItem>();
			for (Paises dato : allData) {
				paisesItems.add(new SelectItem(dato.getPaiId(), dato
						.getPaiNom()));
			}
		}
	}
	
	/**
	 * Cargar lista de lugares
	 * */
	@SuppressWarnings("unchecked")
	public void cargaLugaresItems() {
		if (lugaresItems == null) {
			List<Lugar> allData = (List<Lugar>) serviceDao
					.getGenericCommonDao().executeFind(Lugar.NAMED_QUERY_ALL);
			lugaresItems = new ArrayList<SelectItem>();
			for (Lugar dato : allData) {
				lugaresItems.add(new SelectItem(dato.getLugId(), dato
						.getLugCiu()));
			}
		} else if (lugaresItems == null) {
			lugaresItems = new ArrayList<SelectItem>();
		}
	}

	
	public static final class PersonaDataModel extends PrimeDataModel<Persona, Integer> {
		private static final long serialVersionUID = 1L;

		private PersonaDataModel() {
			super(Persona.class);
			setOrderBy(Order.asc("perNom"));
		}

		@Override
		protected Object getId(Persona t) {
			return t.getClass();
		}
	}


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
	
	
	/**
	 * 
	 * Metodo para cargar el la informacin de una persona seleccionada 
	 * */
	public void cargarRegistro(){	
		
		try {
			log.info("Cargar datos de persona");
			if(idPerSeleccionada == null){
				 FacesContext facesContext = FacesContext.getCurrentInstance();
			     String idRegSeleccionado = facesContext.getExternalContext().getRequestParameterMap().get("idPerSeleccionada");			   
			     if(idRegSeleccionado != null) this.idPerSeleccionada = Integer.parseInt(idRegSeleccionado);		
			}
				
			if(idPerSeleccionada != null ){
				personaSeleccionada =(Persona) serviceDao.getGenericCommonDao().read(Persona.class, idPerSeleccionada);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	public PersonaDataModel getListDatamodel() {
		cargaListaDataModel();
		return listDatamodel;
	}

	public void setListDatamodel(PersonaDataModel listDatamodel) {
		this.listDatamodel = listDatamodel;
	}

	public String getNumIdentificacion() {
		return numIdentificacion;
	}

	public void setNumIdentificacion(String numIdentificacion) {
		this.numIdentificacion = numIdentificacion;
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

	public String getSeudonimo() {
		return seudonimo;
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

	public void setSeudonimo(String seudonimo) {
		this.seudonimo = seudonimo;
	}
	/*Gets  And Sets Creacin Persona*/
	public List<SelectItem> getTiposDocumentosItems() {
		cargarTiposDocItems();
		return tiposDocumentosItems;
	}

	public void setTiposDocumentosItems(List<SelectItem> tiposDocumentosItems) {
		this.tiposDocumentosItems = tiposDocumentosItems;
	}

	public Integer getIdTipoDocumentoSol() {
		return idTipoDocumentoSol;
	}


	public void setIdTipoDocumentoSol(Integer idTipoDocumentoSol) {
		this.idTipoDocumentoSol = idTipoDocumentoSol;
	}

	public boolean isMuestrese() {
		return muestrese;
	}

	public void setMuestrese(boolean muestrese) {
		this.muestrese = muestrese;
	}

	public List<SelectItem> getPaisesItems() {
		cargaPaisesItems();
		return paisesItems;
	}

	public void setPaisesItems(List<SelectItem> paisesItems) {
		this.paisesItems = paisesItems;
	}

	public Short getIdPaisSeleccionado() {
		return idPaisSeleccionado;
	}

	public void setIdPaisSeleccionado(Short idPaisSeleccionado) {
		this.idPaisSeleccionado = idPaisSeleccionado;
	}

	public List<SelectItem> getLugaresItems() {
		cargaLugaresItems();
		return lugaresItems;
	}

	public void setLugaresItems(List<SelectItem> lugaresItems) {
		this.lugaresItems = lugaresItems;
	}

	public Short getIdLugarSeleccionado() {
		return idLugarSeleccionado;
	}

	public void setIdLugarSeleccionado(Short idLugarSeleccionado) {
		this.idLugarSeleccionado = idLugarSeleccionado;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public boolean isMostrarModal() {
		return mostrarModal;
	}

	public void setMostrarModal(boolean mostrarModal) {
		this.mostrarModal = mostrarModal;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public Integer getIdPerSeleccionada() {
		return idPerSeleccionada;
	}

	public void setIdPerSeleccionada(Integer idPerSeleccionada) {
		this.idPerSeleccionada = idPerSeleccionada;
	}

	public Persona getPersonaSeleccionada() {
		return personaSeleccionada;
	}

	public void setPersonaSeleccionada(Persona personaSeleccionada) {
		this.personaSeleccionada = personaSeleccionada;
	}
	
	
	

}
