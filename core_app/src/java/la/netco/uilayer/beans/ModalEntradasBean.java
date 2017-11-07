package la.netco.uilayer.beans;

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

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.persistencia.vo.Usuario;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.spring.security.UserDetailsUtils;
import la.netco.persistencia.dto.commons.Anexo;
import la.netco.persistencia.dto.commons.Anexoentrada;
import la.netco.persistencia.dto.commons.Clasificacion;
import la.netco.persistencia.dto.commons.ConstantsKeysFire;
import la.netco.persistencia.dto.commons.Depend;
import la.netco.persistencia.dto.commons.Enlace;
import la.netco.persistencia.dto.commons.Entrada;
import la.netco.persistencia.dto.commons.Salida;


@ManagedBean
@ViewScoped
public class ModalEntradasBean implements Serializable {


	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;
	private EntradaDataModel listDatamodel = null;

	private List<Entrada> entradas;
	
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
	
	private Entrada entradaSeleccionada;

	private List<Salida> salidasEntradas = new ArrayList<Salida>();
	private List<Anexo> anexosEntrada = new ArrayList<Anexo>();
	private Boolean adminCorrespondencia;
	private String usuarioLogeado;
	
	public ModalEntradasBean(){
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
			listDatamodel 	= new EntradaDataModel();
			cargaListaDataModel();
		}
	}
	
	public void buscar(){
		List<Criterion> filtros = new ArrayList<Criterion>();
		boolean filtro = false;
		if(noRadicacion != null  && !noRadicacion.trim().equals("") ){
    		filtros.add(Restrictions.eq("entNen",  noRadicacion)); 
    		filtro = true;
    	}
		
		if(asunto != null  && !asunto.trim().equals("") ){
    		filtros.add(Restrictions.ilike("entAsu",   "%" +  asunto + "%"));
    		filtro = true;
    	}
    	
    	if(fechaRadicacion != null  && !fechaRadicacion.equals("") ){
    		filtros.add(Restrictions.eq("entFen", fechaRadicacion));
    		filtro = true;
    	}
    	
    	if(nomPersonaNatural != null  && !nomPersonaNatural.equals("") ){
    		filtros.add(Restrictions.ilike("nombreCompletoSol", "%" + nomPersonaNatural + "%"));
    		filtro = true;
    	}
    	
    	if(nomPersonaJuridica != null  && !nomPersonaJuridica.equals("") ){
    		filtros.add(Restrictions.ilike("entEnt", "%" + nomPersonaJuridica + "%"));
    		filtro = true;
    	}
    	
    	if(idDependenciaSeleccionada != null && idDependenciaSeleccionada != 0){
    		filtros.add(Restrictions.eq("dependencia.depId", idDependenciaSeleccionada));
    		filtro = true;
    	}
    	
    	if(idClasificacionSeleccionada != null &&  idClasificacionSeleccionada != 0){
    		filtros.add(Restrictions.eq("clasificacion.claId", idClasificacionSeleccionada));
    		filtro = true;
    	}
    	
    	if(adminCorrespondencia == true){
    		if(idUsuarioSeleccionado != null &&   idUsuarioSeleccionado != 0){
        		filtros.add(Restrictions.eq("usuario.usrId", idUsuarioSeleccionado));    	
        	}else if(!filtro){
        		filtros.add(Restrictions.eq("usuario.usrLog", usuarioLogeado));
        	}
    	}else{
    		filtros.add(Restrictions.eq("usuario.usrLog", usuarioLogeado));    	
    	}
    	

		Map<String, String> alias = new HashMap<String, String>();
		alias.put("usuario", "usuario");
		listDatamodel.setAlias(alias);
		listDatamodel.setFiltros(filtros);
	}

	public void cargaListaDataModel() {
			listDatamodel = new EntradaDataModel();
			List<Criterion> filtros = new ArrayList<Criterion>();
			Map<String, String> alias = new HashMap<String, String>();			
			filtros.add(Restrictions.eq("usuario.usrLog", usuarioLogeado));    	
			alias.put("usuario", "usuario");
			listDatamodel.setAlias(alias);
			listDatamodel.setFiltros(filtros);
	}

	
	private static final class EntradaDataModel extends PrimeDataModel<Entrada, Integer> {
		private static final long serialVersionUID = 1L;

		private EntradaDataModel() {
			super(Entrada.class);
			setOrderBy(Order.desc("entFen"));
		}

		@Override
		protected Object getId(Entrada t) {
			return t.getClass();
		}
	}

	
	@SuppressWarnings("unchecked")	
	public void cargarClasificacionItems(){

		if(idDependenciaSeleccionada != null){
		
			
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, Clasificacion.TIPO_ENTRADA);
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

			List<Usuario> usuarios = (List<Usuario>) serviceDao.getGenericCommonDao().executeFind(Usuario.NAMED_QUERY_ALL_USUARIOS);
			 
			usuariosItems = new ArrayList<SelectItem>();
			 
			for (Usuario usu : usuarios) {
				usuariosItems.add(new SelectItem(usu.getUsrId(), usu.getUsrNom() + " " + usu.getUsrPap() + " " + usu.getUsrSap()));
			}
			
		}else if(usuariosItems == null){
			usuariosItems = new ArrayList<SelectItem>();
		}
	}
	
	@SuppressWarnings("unchecked")	
	public void cargarDependenciasItems(){

		if(dependenciasItems == null){
			
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, Clasificacion.TIPO_ENTRADA);

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

	public EntradaDataModel getListDatamodel() {
		return listDatamodel;
	}

	public void setListDatamodel(EntradaDataModel listDatamodel) {
		this.listDatamodel = listDatamodel;
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

	public List<Entrada> getEntradas() {
		return entradas;
	}

	public void setEntradas(List<Entrada> entradas) {
		this.entradas = entradas;
	}

	public Entrada getEntradaSeleccionada() {
		return entradaSeleccionada;
	}

	@SuppressWarnings("unchecked")
	public void setEntradaSeleccionada(Entrada entradaSeleccionada) {
		if(entradaSeleccionada != null){
			this.entradaSeleccionada  = (Entrada)serviceDao.getEntradaDao().read(Entrada.class, entradaSeleccionada.getEntId());
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, this.entradaSeleccionada.getEntId());
			List<Enlace> enlaces = (List<Enlace>) serviceDao.getGenericCommonDao().executeFind(Enlace.class, params, Enlace.NAMED_QUERY_GET_BY_ENTRADA_ORIGEN_ENTRADA);
			for (Enlace enlace : enlaces) {
				salidasEntradas.add(enlace.getSalida());
			}
			
			for (Anexoentrada anexoEntrada : this.entradaSeleccionada.getAnexos()) {
				Anexo anexo = anexoEntrada.getAnexo();
				anexo.setNumFolios(anexoEntrada.getDaeCan());
				anexosEntrada.add(anexo);
			}
			
		}
		this.entradaSeleccionada = entradaSeleccionada;
	}

	public List<Salida> getSalidasEntradas() {
		
		return salidasEntradas;
	}

	public void setSalidasEntradas(List<Salida> salidasEntradas) {
		this.salidasEntradas = salidasEntradas;
	}

	public List<Anexo> getAnexosEntrada() {
		return anexosEntrada;
	}

	public void setAnexosEntrada(List<Anexo> anexosEntrada) {
		this.anexosEntrada = anexosEntrada;
	}

	public Boolean getAdminCorrespondencia() {
		return adminCorrespondencia;
	}

	public void setAdminCorrespondencia(Boolean adminCorrespondencia) {
		this.adminCorrespondencia = adminCorrespondencia;
	}
		
}
