package la.netco.correspondencia.uilayer.beans.salidas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.persistencia.vo.Usuario;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.spring.security.CommonsActionsKey;
import la.netco.core.spring.security.CommonsModKeys;
import la.netco.core.spring.security.SecuredAction;
import la.netco.core.spring.security.UserDetailsUtils;
import la.netco.core.uilayer.beans.Constants;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.correspondencia.dto.custom.Consecutivo;
import la.netco.correspondencia.uilayer.beans.ConfigForms;
import la.netco.correspondencia.uilayer.beans.Propiedad;
import la.netco.persistencia.dto.commons.Anexo;
import la.netco.persistencia.dto.commons.Anexosalida;
import la.netco.persistencia.dto.commons.AnexosalidaId;
import la.netco.persistencia.dto.commons.Clasificacion;
import la.netco.persistencia.dto.commons.Depend;
import la.netco.persistencia.dto.commons.Enlace;
import la.netco.persistencia.dto.commons.EnlaceId;
import la.netco.persistencia.dto.commons.Entidad;
import la.netco.persistencia.dto.commons.Entrada;
import la.netco.persistencia.dto.commons.Estado;
import la.netco.persistencia.dto.commons.Lugar;
import la.netco.persistencia.dto.commons.Medioscorrespondencia;
import la.netco.persistencia.dto.commons.Paises;
import la.netco.persistencia.dto.commons.Persona;
import la.netco.persistencia.dto.commons.Salida;
import la.netco.persistencia.dto.commons.Seguimientoentrada;
import la.netco.persistencia.dto.commons.Seguimientosalida;
import la.netco.persistencia.dto.commons.Tiposdocumento;
import la.netco.persistencia.dto.commons.Tiposevento;
import la.netco.persistencia.dto.commons.Tipospersona;
import la.netco.persistencia.dto.commons.Transicion;

@ManagedBean
@ViewScoped
public class SalidasBean implements Serializable {

	private static final long serialVersionUID = 1L;
	//private static Logger log = LoggerFactory.getLogger(SalidasBean.class);

	
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;
	
	private List<SelectItem> clasificacionItems;
	private Short 	idClasificacionSeleccionada;
	
	
	private List<SelectItem> dependenciasItems;
	private Short idDependenciaSeleccionada;
	
	private List<SelectItem> mediosItems;
	private Short idMedioSeleccionada;
	
	private List<SelectItem> tiposPersonaItems;
	private Short idTiposPersonaSeleccionado;
	
	private List<SelectItem> tiposDocumentosItems;
	private Integer idTipoDocumentoSol;
	
	
	private List<SelectItem> paisesItems;
	private Short idPaisSeleccionado;
	
	private List<SelectItem> lugaresItems;
	private Short idLugarSeleccionado;
	
	
	private Persona personaNaturalSeleccionada;
	
	private Entidad personaJuridicaSeleccionada;
	
	private Salida nuevoRegistro;
	
	private Map<String, Propiedad> configuracionFormulario;
	
	private List<Anexo> anexosClasificacion  = new ArrayList<Anexo>();
	
	private List<Entrada> entradasSeleccionadas = new ArrayList<Entrada>();
	
	private boolean respuestaEntrada;
	private Salida salidaSeleccionada;
	private Entrada entradaSeleccionada;	
	private Integer idRegSeleccionado;
	
	private List<Seguimientosalida> seguimientoSalida = new ArrayList<Seguimientosalida>();
	private boolean generarEtiqueta;
	public SalidasBean(){
		nuevoRegistro		= new Salida();
		
		if(personaNaturalSeleccionada ==null)
			personaNaturalSeleccionada = new Persona();
		
		if(personaJuridicaSeleccionada ==null)
			personaJuridicaSeleccionada = new Entidad();
		
	}

	@SecuredAction(keyAction = CommonsActionsKey.ADD, keyMod =CommonsModKeys.MNUS)
	@SuppressWarnings("unchecked")
	public String agregar(){
		String page =	null;
		try {
				Integer totalFolios = 0;
				if(anexosClasificacion!= null && !anexosClasificacion.isEmpty()){
					for (Anexo anexo : anexosClasificacion) {
						totalFolios = totalFolios.intValue() + anexo.getNumFolios().intValue();
					}
				}		
				
				Depend dependencia = (Depend) serviceDao.getGenericCommonDao().read(Depend.class, idDependenciaSeleccionada);
				Clasificacion clasificacion= (Clasificacion) serviceDao.getGenericCommonDao().read(Clasificacion.class, idClasificacionSeleccionada);
				Medioscorrespondencia medio = (Medioscorrespondencia) serviceDao.getGenericCommonDao().read(Medioscorrespondencia.class, idMedioSeleccionada);
				Tipospersona tipoPersona= (Tipospersona) serviceDao.getGenericCommonDao().read(Tipospersona.class, idTiposPersonaSeleccionado);
				Usuario usuario = UserDetailsUtils.usuarioLogged();
				
				Map<Integer, Object> params = new HashMap<Integer, Object>();
				params.put(0, idClasificacionSeleccionada);
				params.put(1, Tiposevento.ETAPA_INICIAL);
				Short estadoId = null;
				List<Transicion> transicions = (List<Transicion>) serviceDao.getEntradaDao().executeFind(Transicion.class, params, Transicion.NAMED_QUERY_BY_CLASIFICACION_TIPO_EVENTO);
				Transicion transicion = null;
				if(transicions != null && !transicions.isEmpty()){			
					transicion = transicions.get(0);					
					estadoId = transicion.getId().getTrsEstfin();				
				}
				Estado estadoFinal = (Estado) serviceDao.getGenericCommonDao().read(Estado.class, estadoId);
				
				nuevoRegistro.setSalFrs(new Date(System.currentTimeMillis()));		
				
				nuevoRegistro.setEstado(estadoFinal);
				nuevoRegistro.setSalLren(respuestaEntrada);
				nuevoRegistro.setMedio(medio);
				nuevoRegistro.setTipospersona(tipoPersona);
				
				nuevoRegistro.setDepend(dependencia);
				nuevoRegistro.setClasificacion(clasificacion);
				
				if(idTipoDocumentoSol != null){
					Tiposdocumento tipoDocumento = (Tiposdocumento) serviceDao.getGenericCommonDao().read(Tiposdocumento.class, idTipoDocumentoSol);
					nuevoRegistro.setTiposdocumento(tipoDocumento);
				}
				if(idLugarSeleccionado != null){
					Lugar lugar = (Lugar) serviceDao.getGenericCommonDao().read(Lugar.class, idLugarSeleccionado);
					nuevoRegistro.setLugar(lugar);
				}
				if(idPaisSeleccionado != null){
					Paises pais = (Paises) serviceDao.getGenericCommonDao().read(Paises.class, idPaisSeleccionado);
					nuevoRegistro.setPaises(pais);
				}
				
				Date fechaCorrespondencia = serviceDao.getEntradaDao().obtenerFechaRadicacion();
				nuevoRegistro.setSalFsa(fechaCorrespondencia);		
				nuevoRegistro.setSalFol(totalFolios.shortValue());
				
				nuevoRegistro.setUsuarioBySalUsr(usuario);

				nuevoRegistro.setSalCdep(dependencia.getDepCod());
				nuevoRegistro.setSalCano(Calendar.getInstance().get(Calendar.YEAR));
				
				Consecutivo numCorrespondencia = serviceDao.getEntradaDao().obtenerConsecutivo(dependencia.getDepCod(), Consecutivo.TIPO_SALIDA);
				nuevoRegistro.setSalCnro(numCorrespondencia.getConsecutivo());
				nuevoRegistro.setSalNsa(numCorrespondencia.getConsecutivoCompleto());		
				Integer  idSalida = (Integer) serviceDao.getGenericCommonDao().create(Salida.class, nuevoRegistro);
				
				if(idSalida != null){
					if(anexosClasificacion!= null && !anexosClasificacion.isEmpty()){
						for (Anexo anexo : anexosClasificacion) {							
							if(anexo.getNumFolios() != null && !anexo.getNumFolios().equals(new Integer(0).shortValue())){
								Anexosalida anexoSalida = new Anexosalida(new AnexosalidaId(idSalida, anexo.getAnxId()),anexo.getNumFolios());
								serviceDao.getGenericCommonDao().create(Anexosalida.class, anexoSalida);
							}
						}
					}
					
					if(entradasSeleccionadas != null && !entradasSeleccionadas.isEmpty()){
						for (Entrada entrada: entradasSeleccionadas) {							
							Enlace enlaceEntrada = new Enlace(new EnlaceId(Enlace.ORIGEN_SALIDA, entrada.getEntId(),idSalida));
							serviceDao.getGenericCommonDao().create(Enlace.class, enlaceEntrada);	
							
							entrada.setEntLnot(true);
							entrada.setEntLcon(true);
							
							serviceDao.getEntradaDao().update(Entrada.class, entradaSeleccionada);
						}
					}
					
				}
				
				Salida salidaReg = (Salida)serviceDao.getGenericCommonDao().read(Salida.class, idSalida);
				serviceDao.getSeguimientosDao().addSeguimientoSalida(salidaReg, transicion, Seguimientoentrada.SEGUIMIENTO_CREACION, "", usuario, usuario);
				
				FacesUtils.addFacesMessageFromBundle(Constants.OPERACION_EXITOSA_CREAR_REGISTRO,	FacesMessage.SEVERITY_INFO);
				page = "transaccionExitosa";
				

				FacesUtils.flashScope(FacesContext.getCurrentInstance()).put("idSalidaGenerado", idSalida);
				FacesUtils.flashScope(FacesContext.getCurrentInstance()).put("generarEtiqueta", generarEtiqueta);
				
				nuevoRegistro = new Salida();
				
		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
		}
		
		return page;
	}
	
	@SuppressWarnings("unchecked")
	@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod =CommonsModKeys.MNUS)
	public String actualizar(){
		String page =	null;
		try {
				Integer totalFolios = 0;
				if(anexosClasificacion!= null && !anexosClasificacion.isEmpty()){
					for (Anexo anexo : anexosClasificacion) {
						totalFolios = totalFolios.intValue() + anexo.getNumFolios().intValue();
					}
				}		
				
				Depend dependencia = (Depend) serviceDao.getGenericCommonDao().read(Depend.class, idDependenciaSeleccionada);
				Clasificacion clasificacion= (Clasificacion) serviceDao.getGenericCommonDao().read(Clasificacion.class, idClasificacionSeleccionada);
				Medioscorrespondencia medio = (Medioscorrespondencia) serviceDao.getGenericCommonDao().read(Medioscorrespondencia.class, idMedioSeleccionada);
				Tipospersona tipoPersona= (Tipospersona) serviceDao.getGenericCommonDao().read(Tipospersona.class, idTiposPersonaSeleccionado);
				
				salidaSeleccionada.setSalLren(respuestaEntrada);
				salidaSeleccionada.setMedio(medio);
				salidaSeleccionada.setTipospersona(tipoPersona);
				
				salidaSeleccionada.setDepend(dependencia);
				salidaSeleccionada.setClasificacion(clasificacion);
				
				if(idTipoDocumentoSol != null){
					Tiposdocumento tipoDocumento = (Tiposdocumento) serviceDao.getGenericCommonDao().read(Tiposdocumento.class, idTipoDocumentoSol);
					salidaSeleccionada.setTiposdocumento(tipoDocumento);
				}
				if(idLugarSeleccionado != null){
					Lugar lugar = (Lugar) serviceDao.getGenericCommonDao().read(Lugar.class, idLugarSeleccionado);
					salidaSeleccionada.setLugar(lugar);
				}
				if(idPaisSeleccionado != null){
					Paises pais = (Paises) serviceDao.getGenericCommonDao().read(Paises.class, idPaisSeleccionado);
					salidaSeleccionada.setPaises(pais);
				}
				
				salidaSeleccionada.setSalFol(totalFolios.shortValue());
	
				serviceDao.getGenericCommonDao().update(Salida.class, salidaSeleccionada);
				Integer  idSalida =   salidaSeleccionada.getSalId();
				if(idSalida != null){
					
					//ELIMINA LA RELACION DE ANEXOS
					for (Anexosalida anexoSalida : this.salidaSeleccionada.getAnexosalidas()) {
						serviceDao.getGenericCommonDao().delete(Anexosalida.class, anexoSalida);
					}
					
					Map<Integer, Object> params = new HashMap<Integer, Object>();
					params.put(0, salidaSeleccionada.getSalId());
					
					//ELIMINA ENLACES
					List<Enlace> enlaces = (List<Enlace>) serviceDao.getGenericCommonDao().executeFind(Enlace.class, params, Enlace.NAMED_QUERY_GET_BY_SALIDA_ORIGEN_SALIDA);
					for (Enlace enlace : enlaces) {
						serviceDao.getEntradaDao().delete(Enlace.class, enlace);
					}
					
					
					if(anexosClasificacion!= null && !anexosClasificacion.isEmpty()){
						for (Anexo anexo : anexosClasificacion) {							
							if(anexo.getNumFolios() != null && !anexo.getNumFolios().equals(new Integer(0).shortValue())){
								Anexosalida anexoSalida = new Anexosalida(new AnexosalidaId(idSalida, anexo.getAnxId()),anexo.getNumFolios());
								serviceDao.getGenericCommonDao().create(Anexosalida.class, anexoSalida);
							}
						}
					}
					
					if(entradasSeleccionadas != null && !entradasSeleccionadas.isEmpty()){
						for (Entrada entrada: entradasSeleccionadas) {							
							Enlace enlaceEntrada = new Enlace(new EnlaceId(Enlace.ORIGEN_SALIDA, entrada.getEntId(),idSalida));
							serviceDao.getGenericCommonDao().create(Enlace.class, enlaceEntrada);	
							
							entrada.setEntLnot(true);
							entrada.setEntLcon(true);
							
							serviceDao.getEntradaDao().update(Entrada.class, entrada);
						}
					}
					
				}
				
				
				FacesUtils.addFacesMessageFromBundle(Constants.OPERACION_EXITOSA_ACTUALIZAR_REGISTRO,	FacesMessage.SEVERITY_INFO);
				page = "transaccionExitosa";
				salidaSeleccionada = new Salida();
				
		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
		}
		
		return page;
	}
	


	@SuppressWarnings("unchecked")
	public void cargarRegistroDetalle(){	
		
		try {						
			if(idRegSeleccionado == null){
				 FacesContext facesContext = FacesContext.getCurrentInstance();
			     String idRegSeleccionado = facesContext.getExternalContext().getRequestParameterMap().get("idRegSeleccionado");			   
			     if(idRegSeleccionado != null) this.idRegSeleccionado = Integer.parseInt(idRegSeleccionado);		
			}
			
			if((salidaSeleccionada == null || salidaSeleccionada.getSalId() == null) 	&&  (idRegSeleccionado != null)){
				salidaSeleccionada =(Salida) serviceDao.getGenericCommonDao().read(Salida.class, idRegSeleccionado);
				
				Map<Integer, Object> params = new HashMap<Integer, Object>();
				params.put(0, salidaSeleccionada.getSalId());
				seguimientoSalida = (List<Seguimientosalida>)serviceDao.getEntradaDao().executeFind(Seguimientosalida.class, params, Seguimientosalida.NAMED_QUERY_GET_BY_SALIDA);
			
				
				for (Anexosalida anexoSalida: this.salidaSeleccionada.getAnexosalidas()) {
					Anexo anexo = anexoSalida.getAnexo();
					anexo.setNumFolios(anexoSalida.getDasCan());
					anexosClasificacion.add(anexo);
				}
				
				List<Enlace> enlaces = (List<Enlace>) serviceDao.getGenericCommonDao().executeFind(Enlace.class, params, Enlace.NAMED_QUERY_GET_BY_SALIDA_ORIGEN_SALIDA);
				for (Enlace enlace : enlaces) {
					entradasSeleccionadas.add(enlace.getEntrada());
				}
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	

	
	@SuppressWarnings("unchecked")
	public void cargarRegistroActualizar(){	
		
		try {						
			
			if(idRegSeleccionado == null){
				 FacesContext facesContext = FacesContext.getCurrentInstance();
			     String idRegSeleccionado = facesContext.getExternalContext().getRequestParameterMap().get("idRegSeleccionado");			   
			     if(idRegSeleccionado != null) this.idRegSeleccionado = Integer.parseInt(idRegSeleccionado);		
			}
			
			if((salidaSeleccionada == null || salidaSeleccionada.getSalId() == null) 	&&  (idRegSeleccionado != null)){
				salidaSeleccionada =(Salida) serviceDao.getGenericCommonDao().read(Salida.class, idRegSeleccionado);
				Map<Integer, Object> params = new HashMap<Integer, Object>();
				params.put(0, salidaSeleccionada.getSalId());
				
				for (Anexosalida anexoSalida: this.salidaSeleccionada.getAnexosalidas()) {
					Anexo anexo = anexoSalida.getAnexo();
					anexo.setNumFolios(anexoSalida.getDasCan());
					anexosClasificacion.add(anexo);
				}
				
				List<Enlace> enlaces = (List<Enlace>) serviceDao.getGenericCommonDao().executeFind(Enlace.class, params, Enlace.NAMED_QUERY_GET_BY_SALIDA_ORIGEN_SALIDA);
				for (Enlace enlace : enlaces) {
					entradasSeleccionadas.add(enlace.getEntrada());
				}
				
				respuestaEntrada = salidaSeleccionada.isSalLren();
				
				
				idDependenciaSeleccionada = salidaSeleccionada.getDepend().getDepId();
				idClasificacionSeleccionada = salidaSeleccionada.getClasificacion().getClaId();
				idMedioSeleccionada = salidaSeleccionada.getMedio().getMedId();
				idTiposPersonaSeleccionado= salidaSeleccionada.getTipospersona().getTpeId();
				
				if(salidaSeleccionada.getTiposdocumento() != null){
					idTipoDocumentoSol = salidaSeleccionada.getTiposdocumento().getTdoId();
				}
				if(salidaSeleccionada.getPaises() != null){
					idPaisSeleccionado = salidaSeleccionada.getPaises().getPaiId();
				}
				if(salidaSeleccionada.getLugar() != null){
					idLugarSeleccionado = salidaSeleccionada.getLugar().getLugId();
				}
				
				cargarConfiguracionCampos();
				
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	
	public void cargarRegistroDuplicar(){	
		
		try {						
			
			if(idRegSeleccionado == null){
				 FacesContext facesContext = FacesContext.getCurrentInstance();
			     String idRegSeleccionado = facesContext.getExternalContext().getRequestParameterMap().get("idRegSeleccionado");			   
			     if(idRegSeleccionado != null) this.idRegSeleccionado = Integer.parseInt(idRegSeleccionado);		
			}
			
			if(idRegSeleccionado != null){
				nuevoRegistro =(Salida) serviceDao.getGenericCommonDao().read(Salida.class, idRegSeleccionado);
				Map<Integer, Object> params = new HashMap<Integer, Object>();
				params.put(0, nuevoRegistro.getSalId());
				
				nuevoRegistro.setSalId(0);
				nuevoRegistro.setSalCano(null);
				nuevoRegistro.setSalFrs(null);
				nuevoRegistro.setSalFsa(null);
				nuevoRegistro.setSalCdep(null);
				nuevoRegistro.setSalCano(null);
				nuevoRegistro.setUsuarioBySalUen(null);
				nuevoRegistro.setUsuarioBySalUsr(null);
				nuevoRegistro.setSalFen(null);
				nuevoRegistro.setSalCnro(null);
				nuevoRegistro.setSalNsa(null);
				nuevoRegistro.setSalLren(false);
				nuevoRegistro.setSalLent(false);
				nuevoRegistro.setEstado(null);
				respuestaEntrada = nuevoRegistro.isSalLren();
				idDependenciaSeleccionada = nuevoRegistro.getDepend().getDepId();
				idClasificacionSeleccionada = nuevoRegistro.getClasificacion().getClaId();
				idMedioSeleccionada = nuevoRegistro.getMedio().getMedId();
				idTiposPersonaSeleccionado= nuevoRegistro.getTipospersona().getTpeId();
				
				if(nuevoRegistro.getTiposdocumento() != null){
					idTipoDocumentoSol = nuevoRegistro.getTiposdocumento().getTdoId();
				}
				if(nuevoRegistro.getPaises() != null){
					idPaisSeleccionado = nuevoRegistro.getPaises().getPaiId();
				}
				if(nuevoRegistro.getLugar() != null){
					idLugarSeleccionado = nuevoRegistro.getLugar().getLugId();
				}
				
				cargarConfiguracionCampos();
				
				idRegSeleccionado = null;
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
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
	
	
	@SuppressWarnings("unchecked")
	public void cargarConfiguracionCampos(){
		
		if(idClasificacionSeleccionada != null && idDependenciaSeleccionada != null){		
			configuracionFormulario =  new ConfigForms(idDependenciaSeleccionada, idClasificacionSeleccionada);	

			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, Clasificacion.TIPO_SALIDA);
			params.put(1, idClasificacionSeleccionada);
			
			anexosClasificacion = (List<Anexo>) serviceDao.getGenericCommonDao().executeFind(Anexo.class, params, Anexo.NAMED_QUERY_ALL_BY_TIPO_CLASIFICACION);
			
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
	public void cargarMediosItems(){

		if(mediosItems == null){			

			List<Medioscorrespondencia> allData = (List<Medioscorrespondencia>) serviceDao.getGenericCommonDao().executeFind(Medioscorrespondencia.NAMED_QUERY_ALL);
			 
			mediosItems = new ArrayList<SelectItem>();
			 
			for (Medioscorrespondencia dato : allData) {
				mediosItems.add(new SelectItem(dato.getMedId(), dato.getMedNom()));
			}
			
		}else if(mediosItems == null){
			mediosItems = new ArrayList<SelectItem>();
		}
	}

	@SuppressWarnings("unchecked")	
	public void cargarTiposDocItems(){

		if(tiposDocumentosItems == null){			

			List<Tiposdocumento> allData = (List<Tiposdocumento>) serviceDao.getGenericCommonDao().executeFind(Tiposdocumento.NAMED_QUERY_ALL);
			 
			tiposDocumentosItems = new ArrayList<SelectItem>();
			 
			for (Tiposdocumento dato : allData) {
				tiposDocumentosItems.add(new SelectItem(dato.getTdoId(), dato.getTdoNom()));
			}
			
		}else if(tiposDocumentosItems == null){
			tiposDocumentosItems = new ArrayList<SelectItem>();
		}
	}
	
	/**
	 * Carga lista tipo de peronas tipo de personas.
	 */
	@SuppressWarnings("unchecked")
	public void cargaTipoPersonaItems() {
		if (tiposPersonaItems == null) {
			List<Tipospersona> tipospersonaAuxi = (List<Tipospersona>) serviceDao.getGenericCommonDao().executeFind(Tipospersona.NAMED_QUERY_ALL_TP);

			tiposPersonaItems = new ArrayList<SelectItem>();
			for (Tipospersona tipospersona : tipospersonaAuxi) {
				tiposPersonaItems.add(new SelectItem(tipospersona.getTpeId(),
						tipospersona.getTpeNom()));
			}
		}else if(tiposPersonaItems == null){
			tiposPersonaItems = new ArrayList<SelectItem>();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void cargaPaisesItems() {
		if (paisesItems == null) {
			List<Paises> allData = (List<Paises>) serviceDao.getGenericCommonDao().executeFind(Paises.NAMED_QUERY_ALL);

			paisesItems = new ArrayList<SelectItem>();
			for (Paises dato : allData) {
				paisesItems.add(new SelectItem(dato.getPaiId(),	dato.getPaiNom()));
			}
		}else if(paisesItems == null){
			paisesItems = new ArrayList<SelectItem>();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void cargaLugaresItems() {
		if (lugaresItems == null) {
			List<Lugar> allData = (List<Lugar>) serviceDao.getGenericCommonDao().executeFind(Lugar.NAMED_QUERY_ALL);
			lugaresItems = new ArrayList<SelectItem>();
			for (Lugar dato : allData) {
				lugaresItems.add(new SelectItem(dato.getLugId(),dato.getLugCiu()));
			}
		}else if(lugaresItems == null){
			lugaresItems = new ArrayList<SelectItem>();
		}
	}

	
	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	public Short getIdDependenciaSeleccionada() {
		return idDependenciaSeleccionada;
	}


	public Short getIdClasificacionSeleccionada() {
		return idClasificacionSeleccionada;
	}

	public void setIdDependenciaSeleccionada(Short idDependenciaSeleccionada) {
		this.idDependenciaSeleccionada = idDependenciaSeleccionada;
	}


	public void setIdClasificacionSeleccionada(Short idClasificacionSeleccionada) {
		this.idClasificacionSeleccionada = idClasificacionSeleccionada;
	}

	public List<SelectItem> getDependenciasItems() {
		cargarDependenciasItems();
		return dependenciasItems;
	}

	public void setDependenciasItems(List<SelectItem> dependenciasItems) {
		this.dependenciasItems = dependenciasItems;
	}

	public List<SelectItem> getClasificacionItems() {
		cargarClasificacionItems();
		return clasificacionItems;
	}

	public void setClasificacionItems(List<SelectItem> clasificacionItems) {
		this.clasificacionItems = clasificacionItems;
	}

	public Persona getPersonaNaturalSeleccionada() {
		return personaNaturalSeleccionada;
	}

	public void setPersonaNaturalSeleccionada(Persona personaNaturalSeleccionada) {
		this.personaNaturalSeleccionada = personaNaturalSeleccionada;
		
		if(salidaSeleccionada != null && salidaSeleccionada.getSalId() != null){
			salidaSeleccionada.setSalPap(personaNaturalSeleccionada.getPerPap());
			salidaSeleccionada.setSalSap(personaNaturalSeleccionada.getPerSap());
			salidaSeleccionada.setSalNom(personaNaturalSeleccionada.getPerNom());
			salidaSeleccionada.setSalNdo(personaNaturalSeleccionada.getPerDoc());
			if(personaNaturalSeleccionada.getTiposdocumento() != null)
				setIdTipoDocumentoSol(personaNaturalSeleccionada.getTiposdocumento().getTdoId());
				
			if(personaNaturalSeleccionada.getPaises() != null)
				setIdPaisSeleccionado(personaNaturalSeleccionada.getPaises().getPaiId());
				
			salidaSeleccionada.setSalDir(personaNaturalSeleccionada.getPerDir());
			salidaSeleccionada.setSalTel(personaNaturalSeleccionada.getPerTl1());
			salidaSeleccionada.setSalFax(personaNaturalSeleccionada.getPerFax());
			salidaSeleccionada.setSalCel(personaNaturalSeleccionada.getPerCe1());
			if(personaNaturalSeleccionada.getLugarByPerLug() != null)
				setIdLugarSeleccionado(personaNaturalSeleccionada.getLugarByPerLug().getLugId());
			
		}else if(personaNaturalSeleccionada != null && nuevoRegistro != null){						
			nuevoRegistro.setSalPap(personaNaturalSeleccionada.getPerPap());
			nuevoRegistro.setSalSap(personaNaturalSeleccionada.getPerSap());
			nuevoRegistro.setSalNom(personaNaturalSeleccionada.getPerNom());
			nuevoRegistro.setSalNdo(personaNaturalSeleccionada.getPerDoc());
			if(personaNaturalSeleccionada.getTiposdocumento() != null)
			setIdTipoDocumentoSol(personaNaturalSeleccionada.getTiposdocumento().getTdoId());
			
			if(personaNaturalSeleccionada.getPaises() != null)
			setIdPaisSeleccionado(personaNaturalSeleccionada.getPaises().getPaiId());
			
			nuevoRegistro.setSalDir(personaNaturalSeleccionada.getPerDir());
			nuevoRegistro.setSalTel(personaNaturalSeleccionada.getPerTl1());
			nuevoRegistro.setSalFax(personaNaturalSeleccionada.getPerFax());
			nuevoRegistro.setSalCel(personaNaturalSeleccionada.getPerCe1());
			if(personaNaturalSeleccionada.getLugarByPerLug() != null)
				setIdLugarSeleccionado(personaNaturalSeleccionada.getLugarByPerLug().getLugId());
		}	
	}

	public Entidad getPersonaJuridicaSeleccionada() {
		return personaJuridicaSeleccionada;
	}

	public void setPersonaJuridicaSeleccionada(Entidad personaJuridicaSeleccionada) {
		this.personaJuridicaSeleccionada = personaJuridicaSeleccionada;
		
		if(salidaSeleccionada != null && salidaSeleccionada.getSalId() != null){
			salidaSeleccionada.setSalIde(personaJuridicaSeleccionada.getEtdIde());
			salidaSeleccionada.setSalEnt(personaJuridicaSeleccionada.getEtdNom());		
		}else if(personaJuridicaSeleccionada != null && nuevoRegistro != null){						
			nuevoRegistro.setSalIde(personaJuridicaSeleccionada.getEtdIde());
			nuevoRegistro.setSalEnt(personaJuridicaSeleccionada.getEtdNom());		
		}	
	}

	public List<SelectItem> getMediosItems() {
		cargarMediosItems();
		return mediosItems;
	}

	public void setMediosItems(List<SelectItem> mediosItems) {
		this.mediosItems = mediosItems;
	}

	public Short getIdMedioSeleccionada() {
		return idMedioSeleccionada;
	}

	public void setIdMedioSeleccionada(Short idMedioSeleccionada) {
		this.idMedioSeleccionada = idMedioSeleccionada;
	}

	public Salida getNuevoRegistro() {
		return nuevoRegistro;
	}

	public void setNuevoRegistro(Salida nuevoRegistro) {
		this.nuevoRegistro = nuevoRegistro;
	}
	
	public List<SelectItem> getTiposPersonaItems() {
		cargaTipoPersonaItems();
		return tiposPersonaItems;
	}

	public void setTiposPersonaItems(List<SelectItem> tiposPersonaItems) {
		this.tiposPersonaItems = tiposPersonaItems;
	}

	public Short getIdTiposPersonaSeleccionado() {
		return idTiposPersonaSeleccionado;
	}

	public void setIdTiposPersonaSeleccionado(Short idTiposPersonaSeleccionado) {
		this.idTiposPersonaSeleccionado = idTiposPersonaSeleccionado;
	}


	public void pruebaPersona(){
		System.out.println( " pruebaPersona ");
		setPersonaNaturalSeleccionada((Persona) serviceDao.getGenericCommonDao().read(Persona.class, new Integer(102838)));
	}
	
	
	
	public void quitarEntrada(Entrada entrada){
		if(entradasSeleccionadas.contains(entrada)){
			entradasSeleccionadas.remove(entrada);
		}
	}

	public List<SelectItem> getTiposDocumentosItems() {
		cargarTiposDocItems();
		return tiposDocumentosItems;
	}

	public Integer getIdTipoDocumentoSol() {
		return idTipoDocumentoSol;
	}

	public void setTiposDocumentosItems(List<SelectItem> tiposDocumentosItems) {
		this.tiposDocumentosItems = tiposDocumentosItems;
	}

	public void setIdTipoDocumentoSol(Integer idTipoDocumentoSol) {
		this.idTipoDocumentoSol = idTipoDocumentoSol;
	}

	public List<SelectItem> getPaisesItems() {
		cargaPaisesItems();
		return paisesItems;
	}

	public Short getIdPaisSeleccionado() {
		return idPaisSeleccionado;
	}

	public List<SelectItem> getLugaresItems() {
		cargaLugaresItems();
		return lugaresItems;
	}

	public Short getIdLugarSeleccionado() {
		return idLugarSeleccionado;
	}

	public void setPaisesItems(List<SelectItem> paisesItems) {
		this.paisesItems = paisesItems;
	}

	public void setIdPaisSeleccionado(Short idPaisSeleccionado) {
		this.idPaisSeleccionado = idPaisSeleccionado;
	}

	public void setLugaresItems(List<SelectItem> lugaresItems) {
		this.lugaresItems = lugaresItems;
	}

	public void setIdLugarSeleccionado(Short idLugarSeleccionado) {
		this.idLugarSeleccionado = idLugarSeleccionado;
	}

	public Map<String, Propiedad> getConfiguracionFormulario() {
		return configuracionFormulario;
	}

	public void setConfiguracionFormulario(
			Map<String, Propiedad> configuracionFormulario) {
		this.configuracionFormulario = configuracionFormulario;
	}


	public List<Anexo> getAnexosClasificacion() {
		return anexosClasificacion;
	}

	public void setAnexosClasificacion(List<Anexo> anexosClasificacion) {
		this.anexosClasificacion = anexosClasificacion;
	}

	
	public Salida getSalidaSeleccionada() {
		return salidaSeleccionada;
	}

	public void setEntradaSeleccionada(Entrada entradaSeleccionada) {
		this.entradaSeleccionada = entradaSeleccionada;
		
		if(entradaSeleccionada != null && entradasSeleccionadas != null){	
			if(!entradasSeleccionadas.contains(entradaSeleccionada)){
				entradasSeleccionadas.add(entradaSeleccionada);	
				
				if((salidaSeleccionada == null || salidaSeleccionada.getSalId() == null) && nuevoRegistro != null){						
					nuevoRegistro.setSalPap(entradaSeleccionada.getEntPap());
					nuevoRegistro.setSalSap(entradaSeleccionada.getEntSap());
					nuevoRegistro.setSalNom(entradaSeleccionada.getEntNom());
					nuevoRegistro.setSalNdo(entradaSeleccionada.getEntNdo());
					if(entradaSeleccionada.getTiposdocumento() != null)
					setIdTipoDocumentoSol(entradaSeleccionada.getTiposdocumento().getTdoId());
					
					if(entradaSeleccionada.getPais() != null)
					setIdPaisSeleccionado(entradaSeleccionada.getPais().getPaiId());
					
					nuevoRegistro.setSalDir(entradaSeleccionada.getEntDir());
					nuevoRegistro.setSalTel(entradaSeleccionada.getEntTel());
					nuevoRegistro.setSalFax(entradaSeleccionada.getEntFax());
					nuevoRegistro.setSalCel(entradaSeleccionada.getEntCel());
					if(entradaSeleccionada.getLugar() != null)
						setIdLugarSeleccionado(entradaSeleccionada.getLugar().getLugId());
					
					if(entradaSeleccionada.getTipoPersona() != null && entradaSeleccionada.getTipoPersona().getTpeId() == Tipospersona.TIPO_PERSONA_JURIDICA){
						setIdTiposPersonaSeleccionado(Tipospersona.TIPO_PERSONA_JURIDICA);
						nuevoRegistro.setSalIde(entradaSeleccionada.getEntIde());
						nuevoRegistro.setSalEnt(entradaSeleccionada.getEntEnt());		
					}
					
				}	
				
			}
						
		}
		
	}

	public Entrada getEntradaSeleccionada() {
		return entradaSeleccionada;
	}


	public Integer getIdRegSeleccionado() {
		return idRegSeleccionado;
	}

	public void setIdRegSeleccionado(Integer idRegSeleccionado) {
		this.idRegSeleccionado = idRegSeleccionado;
	}

	public List<Entrada> getEntradasSeleccionadas() {
		return entradasSeleccionadas;
	}

	public boolean isRespuestaEntrada() {
		return respuestaEntrada;
	}

	public void setEntradasSeleccionadas(List<Entrada> entradasSeleccionadas) {
		this.entradasSeleccionadas = entradasSeleccionadas;
	}

	public void setRespuestaEntrada(boolean respuestaEntrada) {
		this.respuestaEntrada = respuestaEntrada;
	}

	public void setSalidaSeleccionada(Salida salidaSeleccionada) {
		this.salidaSeleccionada = salidaSeleccionada;
	}

	public List<Seguimientosalida> getSeguimientoSalida() {
		return seguimientoSalida;
	}

	public void setSeguimientoSalida(List<Seguimientosalida> seguimientoSalida) {
		this.seguimientoSalida = seguimientoSalida;
	}

	public boolean isGenerarEtiqueta() {
		return generarEtiqueta;
	}

	public void setGenerarEtiqueta(boolean generarEtiqueta) {
		this.generarEtiqueta = generarEtiqueta;
	}

}
