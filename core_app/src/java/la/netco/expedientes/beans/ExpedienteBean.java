package la.netco.expedientes.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
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
import la.netco.core.uilayer.commons.GenericDataModel;
import la.netco.correspondencia.dto.custom.Consecutivo;
import la.netco.expedientes.utils.ExpedientesConstants;
import la.netco.persistencia.dto.commons.Anexo;
import la.netco.persistencia.dto.commons.Anotacionesexpedientes;
import la.netco.persistencia.dto.commons.Calidadrepresentante;
import la.netco.persistencia.dto.commons.ConstantsKeysFire;
import la.netco.persistencia.dto.commons.Depend;
import la.netco.persistencia.dto.commons.Devoluciones;
import la.netco.persistencia.dto.commons.Entidad;
import la.netco.persistencia.dto.commons.Entrada;
import la.netco.persistencia.dto.commons.Estado;
import la.netco.persistencia.dto.commons.Evento;
import la.netco.persistencia.dto.commons.Expediente;
import la.netco.persistencia.dto.commons.ExpedienteEstado;
import la.netco.persistencia.dto.commons.Expedienteanexo;
import la.netco.persistencia.dto.commons.ExpedienteanexoId;
import la.netco.persistencia.dto.commons.Expedientepersona;
import la.netco.persistencia.dto.commons.Persona;
import la.netco.persistencia.dto.commons.Seguimientoexpediente;
import la.netco.persistencia.dto.commons.Tiposexpediente;
import la.netco.persistencia.dto.commons.Tipospersona;
import la.netco.persistencia.dto.commons.Tramite;
import la.netco.persistencia.dto.commons.Transicion;
import la.netco.persistencia.dto.commons.Ubicacion;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

@ManagedBean(name = "expedienteBean")
@RequestScoped
public class ExpedienteBean implements Serializable {

	private static final long serialVersionUID = 3883455270583942942L;

	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	private transient ServiceDao daoServicio;

	private String noRadicacion;
	private String noExpediente;
	private String nomExpediente;
	private String nomSolicitante;
	private Date fechaSolicitud;
	private String tipodeRadicacion;
	private Persona personaSeleccionada;
	private Entidad entidadSeleccionada;
	private String observacionGeneric;
	private String nombreGeneric;
	private String textoGeneric;
	private Date fechaUnoGeneric;
	private Date fechaDosGeneric;

	private Boolean boleanoUno;
	private Boolean boleanoDos;
	private Boolean boleanoTres;
	private Boolean boleanoCuatro;
	private Boolean boleanoCinco;
	private Boolean boleanoSeis;

	private Integer idEntradaSeleccionada;
	private Entrada entradaSeleccionada;

	private List<SelectItem> eventoItems;
	private Short idEventoSeleccionado;

	private Expediente expedienteSeleccionado;
	private Integer idExpedienteSeleccionado;

	private List<SelectItem> dependItems;
	private Short idDependSeleccionada;

	private List<SelectItem> tramiteItems;
	private Short idTramiteSeleccionado;

	private List<SelectItem> tiposPersonaItems;
	private Short idTiposPersonaSeleccionado;

	private List<SelectItem> calidadRepresentateItems;
	private Short idCRepresentanteSeleccionado;

	private List<SelectItem> usuariosItems;
	private Short idUsuarioSeleccionado;

	private List<SelectItem> anexosItems;
	private Short idAnexoSeleccionado;

	private List<SelectItem> devolucionesItems;
	private Integer idDevolucionSeleccionada;

	private List<Seguimientoexpediente> listaDummyExpediente;
	private ExpedientesDataModel expedientesDataModel;

	private List<Anotacionesexpedientes> listaAnotaciones;
	private Integer idAnotacionSeleccionada;
	private Anotacionesexpedientes anotacionSeleccionada;

	private List<Expedienteanexo> listaExpedienteAnexo;
	private ExpedienteanexoId idExpedienteanexoId;
	private Expedienteanexo expedienteAnexoSeleccionado;

	private List<Expedientepersona> listaExpedientepersonas;
	private Integer idExpedientePersona;
	private Expedientepersona expedientepersonaSeleccionada;

	private Expediente expedienteNuevo;

	private String origen;

	public ExpedienteBean() {
		expedienteNuevo = new Expediente();
		expedientesDataModel = new ExpedientesDataModel();
		expedienteSeleccionado = new Expediente();
		personaSeleccionada = new Persona();
		entradaSeleccionada = new Entrada();
		entidadSeleccionada = new Entidad();
		anotacionSeleccionada = new Anotacionesexpedientes();
		expedienteAnexoSeleccionado = new Expedienteanexo();
		expedientepersonaSeleccionada = new Expedientepersona();
		System.out.println("Construyendose Expediente");
	}

	public Boolean getBoleanoUno() {
		return boleanoUno;
	}

	public void setBoleanoUno(Boolean boleanoUno) {
		this.boleanoUno = boleanoUno;
	}

	public Boolean getBoleanoDos() {
		return boleanoDos;
	}

	public void setBoleanoDos(Boolean boleanoDos) {
		this.boleanoDos = boleanoDos;
	}

	public Boolean getBoleanoTres() {
		return boleanoTres;
	}

	public void setBoleanoTres(Boolean boleanoTres) {
		this.boleanoTres = boleanoTres;
	}

	public Boolean getBoleanoCuatro() {
		return boleanoCuatro;
	}

	public void setBoleanoCuatro(Boolean boleanoCuatro) {
		this.boleanoCuatro = boleanoCuatro;
	}

	public Boolean getBoleanoCinco() {
		return boleanoCinco;
	}

	public void setBoleanoCinco(Boolean boleanoCinco) {
		this.boleanoCinco = boleanoCinco;
	}

	public Boolean getBoleanoSeis() {
		return boleanoSeis;
	}

	public void setBoleanoSeis(Boolean boleanoSeis) {
		this.boleanoSeis = boleanoSeis;
	}

	public Expedientepersona getExpedientepersonaSeleccionada() {
		return expedientepersonaSeleccionada;
	}

	public void setExpedientepersonaSeleccionada(
			Expedientepersona expedientepersonaSeleccionada) {
		this.expedientepersonaSeleccionada = expedientepersonaSeleccionada;
	}

	public Integer getIdExpedientePersona() {
		return idExpedientePersona;
	}

	public void setIdExpedientePersona(Integer idExpedientePersona) {
		this.idExpedientePersona = idExpedientePersona;
	}

	public List<Expedientepersona> getListaExpedientepersonas() {
		return listaExpedientepersonas;
	}

	public void setListaExpedientepersonas(
			List<Expedientepersona> listaExpedientepersonas) {
		this.listaExpedientepersonas = listaExpedientepersonas;
	}

	public ExpedienteanexoId getIdExpedienteanexoId() {
		return idExpedienteanexoId;
	}

	public void setIdExpedienteanexoId(ExpedienteanexoId idExpedienteanexoId) {
		this.idExpedienteanexoId = idExpedienteanexoId;
	}

	public Expedienteanexo getExpedienteAnexoSeleccionado() {
		return expedienteAnexoSeleccionado;
	}

	public void setExpedienteAnexoSeleccionado(
			Expedienteanexo expedienteAnexoSeleccionado) {
		this.expedienteAnexoSeleccionado = expedienteAnexoSeleccionado;
	}

	public List<Expedienteanexo> getListaExpedienteAnexo() {
		return listaExpedienteAnexo;
	}

	public void setListaExpedienteAnexo(
			List<Expedienteanexo> listaExpedienteAnexo) {
		this.listaExpedienteAnexo = listaExpedienteAnexo;
	}

	public String getTextoGeneric() {
		return textoGeneric;
	}

	public void setTextoGeneric(String textoGeneric) {
		this.textoGeneric = textoGeneric;
	}

	public List<SelectItem> getAnexosItems() {
		cargaAnexosItems();
		return anexosItems;
	}

	public void setAnexosItems(List<SelectItem> anexosItems) {
		this.anexosItems = anexosItems;
	}

	public Short getIdAnexoSeleccionado() {
		return idAnexoSeleccionado;
	}

	public void setIdAnexoSeleccionado(Short idAnexoSeleccionado) {
		this.idAnexoSeleccionado = idAnexoSeleccionado;
	}

	public Anotacionesexpedientes getAnotacionSeleccionada() {
		return anotacionSeleccionada;
	}

	public void setAnotacionSeleccionada(
			Anotacionesexpedientes anotacionSeleccionada) {
		this.anotacionSeleccionada = anotacionSeleccionada;
	}

	public Date getFechaUnoGeneric() {
		return fechaUnoGeneric;
	}

	public void setFechaUnoGeneric(Date fechaUnoGeneric) {
		this.fechaUnoGeneric = fechaUnoGeneric;
	}

	public Date getFechaDosGeneric() {
		return fechaDosGeneric;
	}

	public void setFechaDosGeneric(Date fechaDosGeneric) {
		this.fechaDosGeneric = fechaDosGeneric;
	}

	public List<Anotacionesexpedientes> getListaAnotaciones() {
		return listaAnotaciones;
	}

	public void setListaAnotaciones(
			List<Anotacionesexpedientes> listaAnotaciones) {
		this.listaAnotaciones = listaAnotaciones;
	}

	public Integer getIdAnotacionSeleccionada() {
		return idAnotacionSeleccionada;
	}

	public void setIdAnotacionSeleccionada(Integer idAnotacionSeleccionada) {
		this.idAnotacionSeleccionada = idAnotacionSeleccionada;
	}

	public String getNombreGeneric() {
		return nombreGeneric;
	}

	public void setNombreGeneric(String nombreGeneric) {
		this.nombreGeneric = nombreGeneric;
	}

	public Integer getIdEntradaSeleccionada() {
		return idEntradaSeleccionada;
	}

	public void setIdEntradaSeleccionada(Integer idEntradaSeleccionada) {
		this.idEntradaSeleccionada = idEntradaSeleccionada;
	}

	public Entrada getEntradaSeleccionada() {
		return entradaSeleccionada;
	}

	public void setEntradaSeleccionada(Entrada entradaSeleccionada) {
		this.entradaSeleccionada = entradaSeleccionada;
	}

	public String getObservacionGeneric() {
		return observacionGeneric;
	}

	public void setObservacionGeneric(String observacionGeneric) {
		this.observacionGeneric = observacionGeneric;
	}

	public List<SelectItem> getDevolucionesItems() {
		cargaDevolucionesItems();
		return devolucionesItems;
	}

	public void setDevolucionesItems(List<SelectItem> devolucionesItems) {
		this.devolucionesItems = devolucionesItems;
	}

	public Integer getIdDevolucionSeleccionada() {
		return idDevolucionSeleccionada;
	}

	public void setIdDevolucionSeleccionada(Integer idDevolucionSeleccionada) {
		this.idDevolucionSeleccionada = idDevolucionSeleccionada;
	}

	public List<SelectItem> getEventoItems() {
		cargaEventoItems();
		return eventoItems;
	}

	public void setEventoItems(List<SelectItem> eventoItems) {
		this.eventoItems = eventoItems;
	}

	public Short getIdEventoSeleccionado() {
		return idEventoSeleccionado;
	}

	public void setIdEventoSeleccionado(Short idEventoSeleccionado) {
		this.idEventoSeleccionado = idEventoSeleccionado;
	}

	public Entidad getEntidadSeleccionada() {
		return entidadSeleccionada;
	}

	public void setEntidadSeleccionada(Entidad entidadSeleccionada) {
		this.entidadSeleccionada = entidadSeleccionada;
	}

	public Persona getPersonaSeleccionada() {
		return personaSeleccionada;
	}

	public void setPersonaSeleccionada(Persona personaSeleccionada) {
		this.personaSeleccionada = personaSeleccionada;
	}

	public String getTipodeRadicacion() {
		return tipodeRadicacion;
	}

	public void setTipodeRadicacion(String tipodeRadicacion) {
		this.tipodeRadicacion = tipodeRadicacion;
	}

	public Expediente getExpedienteSeleccionado() {
		return expedienteSeleccionado;
	}

	public void setExpedienteSeleccionado(Expediente expedienteSeleccionado) {
		this.expedienteSeleccionado = expedienteSeleccionado;
	}

	public Integer getIdExpedienteSeleccionado() {
		return idExpedienteSeleccionado;
	}

	public void setIdExpedienteSeleccionado(Integer idExpedienteSeleccionado) {
		this.idExpedienteSeleccionado = idExpedienteSeleccionado;
	}

	public Date getFechaSolicitud() {
		return fechaSolicitud;
	}

	public void setFechaSolicitud(Date fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}

	public List<SelectItem> getCalidadRepresentateItems() {
		cargaCalidadRepresentanteItems();
		return calidadRepresentateItems;
	}

	public void setCalidadRepresentateItems(
			List<SelectItem> calidadRepresentateItems) {
		this.calidadRepresentateItems = calidadRepresentateItems;
	}

	public Short getIdCRepresentanteSeleccionado() {
		return idCRepresentanteSeleccionado;
	}

	public void setIdCRepresentanteSeleccionado(
			Short idCRepresentanteSeleccionado) {
		this.idCRepresentanteSeleccionado = idCRepresentanteSeleccionado;
	}

	public List<SelectItem> getUsuariosItems() {
		cargaUsuariosItems();
		return usuariosItems;
	}

	public void setUsuariosItems(List<SelectItem> usuariosItems) {
		this.usuariosItems = usuariosItems;
	}

	public Short getIdUsuarioSeleccionado() {
		return idUsuarioSeleccionado;
	}

	public void setIdUsuarioSeleccionado(Short idUsuarioSeleccionado) {
		this.idUsuarioSeleccionado = idUsuarioSeleccionado;
	}

	public String getNomExpediente() {
		return nomExpediente;
	}

	public void setNomExpediente(String nomExpediente) {
		this.nomExpediente = nomExpediente;
	}

	public String getNoExpediente() {
		return noExpediente;
	}

	public void setNoExpediente(String noExpediente) {
		this.noExpediente = noExpediente;
	}

	public String getNomSolicitante() {
		return nomSolicitante;
	}

	public void setNomSolicitante(String nomSolicitante) {
		this.nomSolicitante = nomSolicitante;
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

	public List<Seguimientoexpediente> getListaDummyExpediente() {
		return listaDummyExpediente;
	}

	public void setListaDummyExpediente(
			List<Seguimientoexpediente> listaDummyExpediente) {
		this.listaDummyExpediente = listaDummyExpediente;
	}

	public ExpedientesDataModel getExpedientesDataModel() {
		return expedientesDataModel;
	}

	public void setExpedientesDataModel(
			ExpedientesDataModel expedientesDataModel) {
		this.expedientesDataModel = expedientesDataModel;
	}

	public List<SelectItem> getTramiteItems() {
		cargaTramiteItems();
		return tramiteItems;
	}

	public void setTramiteItems(List<SelectItem> tramiteItems) {
		this.tramiteItems = tramiteItems;
	}

	public Short getIdTramiteSeleccionado() {
		return idTramiteSeleccionado;
	}

	public void setIdTramiteSeleccionado(Short idTramiteSeleccionado) {
		this.idTramiteSeleccionado = idTramiteSeleccionado;
	}

	public Short getIdDependSeleccionada() {
		return idDependSeleccionada;
	}

	public void setIdDependSeleccionada(Short idDependSeleccionada) {
		this.idDependSeleccionada = idDependSeleccionada;
	}

	public List<SelectItem> getDependItems() {
		cargaDependItems();
		return dependItems;
	}

	public void setDependItems(List<SelectItem> dependItems) {
		this.dependItems = dependItems;
	}

	public String getNoRadicacion() {
		return noRadicacion;
	}

	public void setNoRadicacion(String noRadicacion) {
		this.noRadicacion = noRadicacion;
	}

	public ServiceDao getDaoServicio() {
		return daoServicio;
	}

	public void setDaoServicio(ServiceDao daoServicio) {
		this.daoServicio = daoServicio;
	}

	// Navegaciones principales de expedientes ****

	@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod = CommonsModKeys.MNUEPAE)
	public String actualizarNavegacion() {
		return "actualizar";
	}

	@SecuredAction(keyAction = CommonsActionsKey.VIEW, keyMod = CommonsModKeys.MNUEPAE)
	public String listadoNavegacion() {
		return "listado";
	}

	// @SecuredAction(keyAction = CommonsActionsKey.VIEW, keyMod =
	// CommonsModKeys.MNUEPAE)
	public String listadoRegistrosNavegacion() {
		return "listadoRegistros";
	}

	@SecuredAction(keyAction = CommonsActionsKey.DELETE, keyMod = CommonsModKeys.MNUEPAE)
	public String eliminarNavegacion() {
		return "eliminar";
	}

	@SecuredAction(keyAction = CommonsActionsKey.ADD, keyMod = CommonsModKeys.MNUEPAE)
	public String agregarNavegacion() {
		return "agregar";
	}

	@SecuredAction(keyAction = CommonsActionsKey.VIEW, keyMod = CommonsModKeys.MNUEPAE)
	public String verExpedienteNavegacion() {
		return "detalle";
	}

	@SecuredAction(keyAction = ExpedientesConstants.CAMBIA_E, keyMod = CommonsModKeys.MNUEPAE)
	public String cambiarEtapaNavegacion() {
		return "cambiarEtapaExp";
	}

	@SecuredAction(keyAction = ExpedientesConstants.INFO_EXP, keyMod = CommonsModKeys.MNUEPAE)
	public String infoExpedienteNavegacion() {
		return "infoExpedienteExp";
	}

	@SecuredAction(keyAction = ExpedientesConstants.DUPLEX_E, keyMod = CommonsModKeys.MNUEPAE)
	public String duplicarExpedienteNavegacion() {
		return "duplicarExp";
	}

	@SecuredAction(keyAction = ExpedientesConstants.DEV_EXP, keyMod = CommonsModKeys.MNUEPAE)
	public String devolverExpedienteNavegacion() {
		return "devolucionExp";
	}

	@SecuredAction(keyAction = ExpedientesConstants.ADJ_EXP, keyMod = CommonsModKeys.MNUEPAE)
	public String adjArchivosExpedienteNavegacion() {
		return "adjArchivosExp";
	}

	@SecuredAction(keyAction = ExpedientesConstants.DOC_EXP, keyMod = CommonsModKeys.MNUEPAE)
	public String genDocumentoExpedienteNavegacion() {
		return "genDocumentosExp";
	}

	// Seguridad anotaciones de expediente

	@SecuredAction(keyAction = ExpedientesConstants.ADD_ANO, keyMod = CommonsModKeys.MNUEPAE)
	public String agregarAnotacionNavegacion() {
		return "agregarAnotacionExp";
	}

	@SecuredAction(keyAction = ExpedientesConstants.UPD_ANO, keyMod = CommonsModKeys.MNUEPAE)
	public String actualizarAnotacionNavegacion() {
		return "actualizarAnotacionExp";
	}

	@SecuredAction(keyAction = ExpedientesConstants.DEL_ANO, keyMod = CommonsModKeys.MNUEPAE)
	public String eliminarAnotacionNavegacion() {
		return "eliminarAnotacionExp";
	}

	// Seguridad anexos de expediente

	@SecuredAction(keyAction = ExpedientesConstants.ADD_ANX, keyMod = CommonsModKeys.MNUEPAE)
	public String agregarAnexoNavegacion() {
		return "agregarAnexoExp";
	}

	@SecuredAction(keyAction = ExpedientesConstants.UPD_ANX, keyMod = CommonsModKeys.MNUEPAE)
	public String actualizarAnexoNavegacion() {
		return "actualizarAnexoExp";
	}

	@SecuredAction(keyAction = ExpedientesConstants.DEL_ANX, keyMod = CommonsModKeys.MNUEPAE)
	public String eliminarAnexoNavegacion() {
		return "eliminarAnexoExp";
	}

	// Seguridad otros de expediente

	@SecuredAction(keyAction = ExpedientesConstants.ADD_OTR, keyMod = CommonsModKeys.MNUEPAE)
	public String agregarOtroNavegacion() {
		return "agregarOtroExp";
	}

	@SecuredAction(keyAction = ExpedientesConstants.UPD_OTR, keyMod = CommonsModKeys.MNUEPAE)
	public String actualizarOtroNavegacion() {
		return "actualizarOtroExp";
	}

	@SecuredAction(keyAction = ExpedientesConstants.DEL_OTR, keyMod = CommonsModKeys.MNUEPAE)
	public String eliminarOtroNavegacion() {
		return "eliminarOtroExp";
	}

	// Acciones Propias de Expediente

	@SuppressWarnings("unchecked")
	@SecuredAction(keyAction = CommonsActionsKey.ADD, keyMod = CommonsModKeys.MNUEPAE)
	public String agregarExpediente() {
		String page = null;
		try {

			HashMap<Integer, Object> paramsEve = new HashMap<Integer, Object>();
			HashMap<Integer, Object> paramsTrs = new HashMap<Integer, Object>();
			Short TrsFinal = null, medioAuxi = null;
			Integer variUbicacion = null;

			if (idDependSeleccionada == null || idDependSeleccionada.equals(0)) {
				FacesUtils.addFacesMessageFromBundle(
						Constants.ERROR_SELECCIONE_DEPENDENCIA,
						FacesMessage.SEVERITY_ERROR);
				return page;
			}

			if (idTramiteSeleccionado == null
					|| idTramiteSeleccionado.equals(0)) {
				FacesUtils.addFacesMessageFromBundle(
						Constants.ERROR_SELECCIONE_TRAMITE,
						FacesMessage.SEVERITY_ERROR);
				return page;
			}

			if (idTiposPersonaSeleccionado == null
					|| idTiposPersonaSeleccionado.equals(0)) {
				FacesUtils.addFacesMessageFromBundle(
						Constants.ERROR_SELECCIONE_TIPOPERSONA,
						FacesMessage.SEVERITY_ERROR);
				return page;
			}

			System.out.println("***Agregando un expediente***");

			Calendar forYear = Calendar.getInstance();
			int anoActual;
			anoActual = forYear.get(Calendar.YEAR);

			// Obteniendo Id de Evento por tramite de expediente
			paramsEve.put(0, idTramiteSeleccionado);
			paramsEve.put(1,
					Byte.parseByte(ExpedientesConstants.TIPOVENTO_INICIAL));

			Short eventoID = (Short) daoServicio.getGenericCommonDao()
					.executeUniqueResult(Evento.NAMED_QUERY_GET_EVEID,
							paramsEve);

			// Obteniendo Transiciones de Estado inicial y Final
			paramsTrs.put(0, eventoID);

			List<Transicion> transicionLista = (List<Transicion>) daoServicio
					.getGenericCommonDao().executeFind(Transicion.class,
							paramsTrs, Transicion.NAMED_QUERY_GET_ALL_BY_EVEID);

			for (Transicion transicion : transicionLista) {
				TrsFinal = transicion.getEstadoByTrsEstfin().getEstId();
			}

			if (TrsFinal == null || TrsFinal.equals(0)) {
				TrsFinal = Short.parseShort(ExpedientesConstants.VAL_EST_INI);
			}

			Depend dependenciaAuxi = (Depend) daoServicio.getGenericCommonDao()
					.read(Depend.class, idDependSeleccionada);

			Consecutivo consecutivoCapt = daoServicio.getExpedienteDao()
					.obtenerConsecutivoExp(dependenciaAuxi.getDepCod().trim());

			System.out.println("La dependencia es : " + idDependSeleccionada);
			expedienteNuevo.setDepend((Depend) daoServicio
					.getGenericCommonDao().read(Depend.class,
							idDependSeleccionada));

			System.out.println("El tramite es:" + idTramiteSeleccionado);
			expedienteNuevo.setTramite((Tramite) daoServicio
					.getGenericCommonDao().read(Tramite.class,
							idTramiteSeleccionado));

			System.out.println("El solicitante es:"
					+ personaSeleccionada.getPerId());
			expedienteNuevo.setPersona((Persona) daoServicio
					.getGenericCommonDao().read(Persona.class,
							personaSeleccionada.getPerId()));

			System.out.println("La calidad del representante es: "
					+ idCRepresentanteSeleccionado);
			expedienteNuevo
					.setCalidadrepresentante((Calidadrepresentante) daoServicio
							.getGenericCommonDao().read(
									Calidadrepresentante.class,
									idCRepresentanteSeleccionado));

			System.out.println("El nombre del expediente es: "
					+ this.nomExpediente);
			expedienteNuevo.setExpNom(this.nomExpediente);

			System.out.println("El codigo del expediente es : "
					+ consecutivoCapt.getConsecutivoCompleto());
			expedienteNuevo.setExpCod(consecutivoCapt.getConsecutivoCompleto());

			System.out.println("El tipo del expediente es: "
					+ ExpedientesConstants.TIPOEXP_OE);
			expedienteNuevo.setTiposexpediente((Tiposexpediente) daoServicio
					.getGenericCommonDao().read(Tiposexpediente.class,
							Byte.parseByte(ExpedientesConstants.TIPOEXP_OE)));

			System.out.println("La fecha actual es: "
					+ System.currentTimeMillis());
			expedienteNuevo.setExpFso((new Date(System.currentTimeMillis())));

			System.out.println("El usuario actual es: "
					+ UserDetailsUtils.usuarioLogged().getUsrId());
			expedienteNuevo.setUsuarioTemp(UserDetailsUtils.usuarioLogged());

			System.out.println("El estado del expediente es: " + TrsFinal);
			expedienteNuevo.setEstado((Estado) daoServicio
					.getGenericCommonDao().read(Estado.class, TrsFinal));

			System.out.println("El codigo de la dependencia: "
					+ dependenciaAuxi.getDepCod());
			expedienteNuevo.setExpCdep(dependenciaAuxi.getDepCod());

			System.out.println("El año actual es : "
					+ forYear.get(Calendar.YEAR));
			expedienteNuevo.setExpCano(anoActual);

			System.out.println("El consecutivo es: "
					+ consecutivoCapt.getConsecutivo());
			expedienteNuevo.setExpCnro(consecutivoCapt.getConsecutivo());

			System.out.println("El tipo de la persona es: "
					+ idTiposPersonaSeleccionado);
			expedienteNuevo.setTipospersona((Tipospersona) daoServicio
					.getGenericCommonDao().read(Tipospersona.class,
							idTiposPersonaSeleccionado));

			if (entidadSeleccionada != null) {
				System.out.println("La Entidad es: "
						+ entidadSeleccionada.getEtdId());
				expedienteNuevo.setEntidad((Entidad) daoServicio
						.getGenericCommonDao().read(Entidad.class,
								entidadSeleccionada.getEtdId()));
			}

			System.out.println("El estatus es :"
					+ ExpedientesConstants.VAL_EXP_STA);

			expedienteNuevo.setEstadoGeneral((ExpedienteEstado) daoServicio
					.getGenericCommonDao().read(ExpedienteEstado.class,
							ExpedientesConstants.VAL_EXP_STA));

			if (entradaSeleccionada == null) {
				System.out.println("La entrada seleccionada es : "
						+ entradaSeleccionada.getEntId());
				Entrada entradaAuxi = (Entrada) daoServicio
						.getGenericCommonDao().read(Entrada.class,
								entradaSeleccionada.getEntId());
				medioAuxi = entradaAuxi.getMedio().getMedId();
			} else {
				medioAuxi = 0;
			}

			if (medioAuxi == 17) {
				variUbicacion = Integer
						.parseInt(ExpedientesConstants.VAL_EXP_UBIUNO);
			} else if (medioAuxi != 17) {
				variUbicacion = Integer
						.parseInt(ExpedientesConstants.VAL_EXP_UBIDOS);
			}

			System.out.println("La ubicacion es:  " + variUbicacion);
			expedienteNuevo
					.setUbicacion((Ubicacion) daoServicio.getGenericCommonDao()
							.read(Ubicacion.class, variUbicacion));

			System.out.println("El tipo de evento es : "
					+ ExpedientesConstants.TIPOVENTO_INICIAL);

			System.out.println("El VBO es : "
					+ ExpedientesConstants.VAL_EXP_VBO);
			expedienteNuevo.setExpVbo(Boolean
					.parseBoolean(ExpedientesConstants.VAL_EXP_VBO));

			Integer idExp = (Integer) daoServicio.getGenericCommonDao().create(
					Expediente.class, expedienteNuevo);
			System.out.println("Expediente Creado: EXP No:" + idExp);

			daoServicio.getSeguimientosDao().addSeguimientoExp(expedienteNuevo,
					ConstantsKeysFire.CREACIONEXP, null);

			return "transaccionExitosa";

		} catch (Exception ex) {
			ex.printStackTrace();
			return page;
		}

	}

	@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod = CommonsModKeys.MNUEPAE)
	public String actualizarExpediente() {
		try {

			daoServicio.getGenericCommonDao().update(Expediente.class,
					expedienteSeleccionado);
			return "transaccionExitosa";
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "transaccionExitosa";
	}

	public String eliminarExpediente() {
		try {

			return "transaccionExitosa";
		} catch (Exception ex) {
			ex.printStackTrace();
			return "transaccionExitosa";
		}

	}

	@SecuredAction(keyAction = ExpedientesConstants.DUPLEX_E, keyMod = CommonsModKeys.MNUEPAE)
	public String duplicarExpediente() {
		try {

			Calendar forYear = Calendar.getInstance();
			int anoActual;

			anoActual = forYear.get(Calendar.YEAR);
			System.out.println("Año actual:" + forYear.get(Calendar.YEAR));
			System.out.println("Dependencia seleccionada : "
					+ idDependSeleccionada);
			System.out.println("Id de la Persona :  "
					+ expedienteSeleccionado.getPersona().getPerId());

			Depend dependenciaAuxi = (Depend) daoServicio.getGenericCommonDao()
					.read(Depend.class, idDependSeleccionada);

			System.out.println("Codigo de Dependencia: "
					+ dependenciaAuxi.getDepCod());

			Consecutivo consecutivoCapt = daoServicio.getExpedienteDao()
					.obtenerConsecutivoExp(dependenciaAuxi.getDepCod());

			System.out.println("Consecutivo Completo :"
					+ consecutivoCapt.getConsecutivoCompleto());
			System.out.println("Consecutivo: "
					+ consecutivoCapt.getConsecutivo());

			System.out.println("Nombre del Expediente: " + this.nomExpediente);
			System.out.println("Estado del Exp:  "
					+ ExpedientesConstants.VAL_EST_INI);
			System.out.println("STA del Exp:  "
					+ ExpedientesConstants.VAL_EXP_STA);
			System.out.println("Ubicacion: Nulo ");

			expedienteSeleccionado.setExpId((Integer) null);

			expedienteSeleccionado
					.setTiposexpediente((Tiposexpediente) daoServicio
							.getGenericCommonDao()
							.read(Tiposexpediente.class,
									Byte.parseByte(ExpedientesConstants.TIPOVENTO_INICIAL)));

			expedienteSeleccionado.setExpCod(consecutivoCapt
					.getConsecutivoCompleto());

			expedienteSeleccionado.setExpFso((new Date(System
					.currentTimeMillis())));
			expedienteSeleccionado
					.setEstado((Estado) daoServicio.getGenericCommonDao().read(
							Estado.class,
							Short.parseShort(ExpedientesConstants.VAL_EST_INI)));

			expedienteSeleccionado.setExpVbo(Boolean
					.parseBoolean(ExpedientesConstants.VAL_EXP_VBO));

			expedienteSeleccionado.setExpCano(anoActual);

			expedienteSeleccionado.setExpCnro(consecutivoCapt.getConsecutivo());

			expedienteSeleccionado
					.setEstadoGeneral((ExpedienteEstado) daoServicio
							.getGenericCommonDao().read(ExpedienteEstado.class,
									ExpedientesConstants.VAL_EXP_STA));
			Integer idExp = (Integer) daoServicio.getGenericCommonDao().create(
					Expediente.class, expedienteSeleccionado);
			System.out
					.println("Expediente Duplicado Creado : EXP No: " + idExp);

			daoServicio.getSeguimientosDao().addSeguimientoExp(expedienteNuevo,
					ConstantsKeysFire.CREACIONEXP, null);

			return "transaccionExitosa";

		} catch (Exception ex) {
			ex.printStackTrace();
			return "transaccionExitosa";
		}

	}

	@SecuredAction(keyAction = ExpedientesConstants.CAMBIA_E, keyMod = CommonsModKeys.MNUEPAE)
	public String cambioEtapaExpediente() {

		Expediente expedienteEnviar = cargaExpActual();

		expedienteEnviar.setUsuarioTemp((Usuario) daoServicio
				.getGenericCommonDao().read(Usuario.class,
						idUsuarioSeleccionado));

		daoServicio.getSeguimientosDao().addSeguimientoExpCambioEtapa(
				expedienteEnviar, ConstantsKeysFire.CAMETAPA,
				this.observacionGeneric, idEventoSeleccionado);

		return "infoExpedienteExp";
	}

	@SecuredAction(keyAction = ExpedientesConstants.DEV_EXP, keyMod = CommonsModKeys.MNUEPAE)
	public String devolverRechazoExpediente() {
		try {

			String justifiFinal;

			Expediente expedienteDevol = cargaExpActual();
			Devoluciones devolucionConsul = (Devoluciones) daoServicio
					.getGenericCommonDao().read(Devoluciones.class,
							idDevolucionSeleccionada);

			expedienteDevol.setExpDev(idDevolucionSeleccionada);
			expedienteDevol.setEstadoGeneral((ExpedienteEstado) daoServicio
					.getGenericCommonDao().read(ExpedienteEstado.class,
							ConstantsKeysFire.STADEVEXP));
			justifiFinal = devolucionConsul.getDevNom() + " || "
					+ observacionGeneric;

			daoServicio.getGenericCommonDao().update(Expediente.class,
					expedienteDevol);

			if (devolucionConsul.getDevLdev() == false) {
				daoServicio.getSeguimientosDao().addSeguimientoExp(
						expedienteDevol, ConstantsKeysFire.RECHAZO,
						justifiFinal);
			} else {
				daoServicio.getSeguimientosDao().addSeguimientoExp(
						expedienteDevol, ConstantsKeysFire.DEVOLUCION,
						justifiFinal);
			}
			return "infoExpedienteExp";
		} catch (Exception e) {
			e.printStackTrace();
			return "transaccionExitosa";
		}

	}

	@SecuredAction(keyAction = ExpedientesConstants.DOC_EXP, keyMod = CommonsModKeys.MNUEPAE)
	public void generaDocumento() {
	}

	// Acciones de Anotaciones

	@SecuredAction(keyAction = ExpedientesConstants.ADD_ANO, keyMod = CommonsModKeys.MNUEPAE)
	public String anotacionesExpAgregar() {

		Anotacionesexpedientes nuevaAnotacion = new Anotacionesexpedientes();
		try {

			System.out.println("****Creando anotacion para expediente****");
			expedienteSeleccionado = cargaExpActual();

			nuevaAnotacion.setExpediente(expedienteSeleccionado);
			nuevaAnotacion.setAnoDes(this.observacionGeneric);
			nuevaAnotacion.setAnoFec((new Date(System.currentTimeMillis())));
			nuevaAnotacion.setEstado(expedienteSeleccionado.getEstado());
			nuevaAnotacion.setAnoNom(this.nombreGeneric);
			nuevaAnotacion.setAnoFcr(this.fechaUnoGeneric);
			nuevaAnotacion.setAnoFve(this.fechaDosGeneric);

			Integer idAnoC = (Integer) daoServicio.getGenericCommonDao()
					.create(Anotacionesexpedientes.class, nuevaAnotacion);

			limpiaGeneric();
			System.out
					.println("Anotacion creada Correctamente**** Anotacion No. : "
							+ idAnoC + " ***");

			return "infoExpedienteExp";
		} catch (Exception e) {
			e.printStackTrace();
			return "infoExpedienteExp";
		}
	}

	@SecuredAction(keyAction = ExpedientesConstants.UPD_ANO, keyMod = CommonsModKeys.MNUEPAE)
	public String anotacionesExpActualizar() {
		try {

			System.out.println("***Actualizando anotacion ***");
			daoServicio.getGenericCommonDao().update(
					Anotacionesexpedientes.class, anotacionSeleccionada);
			System.out.println("***Anotacion actualizada***");
			return "infoExpedienteExp";
		} catch (Exception e) {
			e.printStackTrace();
			return "infoExpedienteExp";
		}
	}

	@SecuredAction(keyAction = ExpedientesConstants.DEL_ANO, keyMod = CommonsModKeys.MNUEPAE)
	public String anotacionesExpEliminar() {
		try {

			System.out.println("***Eliminando  anotacion ***");
			daoServicio.getGenericCommonDao().delete(
					Anotacionesexpedientes.class, anotacionSeleccionada);
			System.out.println("***Anotacion eliminada***");
			return "infoExpedienteExp";
		} catch (Exception e) {
			e.printStackTrace();
			return "infoExpedienteExp";
		}
	}

	// Acciones de Anexos

	@SecuredAction(keyAction = ExpedientesConstants.ADD_ANX, keyMod = CommonsModKeys.MNUEPAE)
	public String anexosExpAgregar() {
		try {
			Expedienteanexo nuevoExpedienteanexo = new Expedienteanexo();
			ExpedienteanexoId nuevoExpedienteanexoId = new ExpedienteanexoId();

			System.out.println("***Creando anexo para expediente***");

			nuevoExpedienteanexoId.setExaAnx(idAnexoSeleccionado);
			nuevoExpedienteanexoId.setExaExp(idExpedienteSeleccionado);

			nuevoExpedienteanexo.setId(nuevoExpedienteanexoId);
			nuevoExpedienteanexo.setExpediente((Expediente) daoServicio
					.getGenericCommonDao().read(Expediente.class,
							idExpedienteSeleccionado));
			nuevoExpedienteanexo.setAnexo((Anexo) daoServicio
					.getGenericCommonDao().read(Anexo.class,
							idAnexoSeleccionado));
			nuevoExpedienteanexo.setExaCan(Integer.parseInt(this.textoGeneric));

			Integer idAnxC = (Integer) daoServicio.getGenericCommonDao()
					.create(Expedienteanexo.class, nuevoExpedienteanexo);

			System.out.println("***Anexo creado correctamente*** Anexo No. : "
					+ idAnxC + " ****");

			return "infoExpedienteExp";
		} catch (Exception e) {
			e.printStackTrace();
			return "infoExpedienteExp";
		}
	}

	@SecuredAction(keyAction = ExpedientesConstants.UPD_ANX, keyMod = CommonsModKeys.MNUEPAE)
	public String anexosExpActualizar() {
		try {

			System.out.println("***Actualizando ExpedienteAnexo ****");

			idExpedienteanexoId.setExaAnx(expedienteAnexoSeleccionado
					.getAnexo().getAnxId());
			idExpedienteanexoId.setExaExp(expedienteAnexoSeleccionado
					.getExpediente().getExpId());

			expedienteAnexoSeleccionado.setId(idExpedienteanexoId);

			daoServicio.getGenericCommonDao().update(Expedienteanexo.class,
					expedienteAnexoSeleccionado);
			System.out.println("***ExpedienteAnexo Actualizado ****");
			return "infoExpedienteExp";
		} catch (Exception e) {
			e.printStackTrace();
			return "infoExpedienteExp";
		}

	}

	@SecuredAction(keyAction = ExpedientesConstants.DEL_ANX, keyMod = CommonsModKeys.MNUEPAE)
	public String anexosExpEliminar() {
		try {

			System.out.println("***Eliminando ExpedienteAnexo ****");

			idExpedienteanexoId.setExaAnx(expedienteAnexoSeleccionado
					.getAnexo().getAnxId());
			idExpedienteanexoId.setExaExp(expedienteAnexoSeleccionado
					.getExpediente().getExpId());

			expedienteAnexoSeleccionado.setId(idExpedienteanexoId);

			daoServicio.getGenericCommonDao().delete(Expedienteanexo.class,
					expedienteAnexoSeleccionado);
			System.out.println("***ExpedienteAnexo eliminado ****");
			return "infoExpedienteExp";
		} catch (Exception e) {
			e.printStackTrace();
			return "infoExpedienteExp";
		}

	}

	// Acciones de Otros

	@SecuredAction(keyAction = ExpedientesConstants.ADD_OTR, keyMod = CommonsModKeys.MNUEPAE)
	public String otrosExpAgregar() {
		try {

			System.out
					.println("***Agregando otra persona o Entidad  al expediente***");

			Expedientepersona nuevoExpedientepersona = new Expedientepersona();

			nuevoExpedientepersona.setEpeExp(idExpedienteSeleccionado);
			nuevoExpedientepersona.setTipospersona((Tipospersona) daoServicio
					.getGenericCommonDao().read(Tipospersona.class,
							idTiposPersonaSeleccionado));
			if (idTiposPersonaSeleccionado == 1) {
				nuevoExpedientepersona.setPersona((Persona) daoServicio
						.getGenericCommonDao().read(Persona.class,
								personaSeleccionada.getPerId()));
			} else {
				nuevoExpedientepersona.setEntidad((Entidad) daoServicio
						.getGenericCommonDao().read(Entidad.class,
								entidadSeleccionada.getEtdId()));
			}
			nuevoExpedientepersona.setEpeDes(this.observacionGeneric);

			Integer idOtroC = (Integer) daoServicio.getGenericCommonDao()
					.create(Expedientepersona.class, nuevoExpedientepersona);

			System.out
					.println("***Otra persona o Entidad agregada*** Otro No. : "
							+ idOtroC + " ****");

			return "infoExpedienteExp";
		} catch (Exception e) {
			e.printStackTrace();
			return "infoExpedienteExp";
		}
	}

	@SecuredAction(keyAction = ExpedientesConstants.UPD_OTR, keyMod = CommonsModKeys.MNUEPAE)
	public String otrosExpActualizar() {
		try {
			System.out
					.println("***Actualizando ExpedientePersona de expediente***");

			daoServicio.getGenericCommonDao().update(Expedientepersona.class,
					expedientepersonaSeleccionada);

			System.out.println("***ExpedientePersona actualizado***");

			return "infoExpedienteExp";
		} catch (Exception e) {
			e.printStackTrace();
			return "infoExpedienteExp";
		}
	}

	/**
	 * Cargar el expediente Actual.
	 * 
	 * @return Expediente
	 */
	public Expediente cargaExpActual() {
		Expediente expActual;
		if (idExpedienteSeleccionado != null) {
			expActual = (Expediente) daoServicio.getGenericCommonDao().read(
					Expediente.class, idExpedienteSeleccionado);
			return expActual;
		} else {
			expActual = new Expediente();
		}
		return expActual;
	}

	/**
	 * Cargar lista de items de dependencias
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void cargaDependItems() {

		if (dependItems == null) {
			List<Depend> dependAuxi = (List<Depend>) daoServicio
					.getGenericCommonDao().executeFind(
							Depend.NAMED_QUERY_ALL_DEPEND);

			dependItems = new ArrayList<SelectItem>();

			for (Depend depend : dependAuxi) {
				dependItems.add(new SelectItem(depend.getDepId(), depend
						.getDepNom()));
			}
		}

	}

	/**
	 * carga Lista de tramites.
	 */
	@SuppressWarnings("unchecked")
	public void cargaTramiteItems() {
		if (tramiteItems == null) {
			List<Tramite> tramiteAuxi = (List<Tramite>) daoServicio
					.getGenericCommonDao().executeFind(
							Tramite.NAMED_QUERY_ALL_TRM);

			tramiteItems = new ArrayList<SelectItem>();
			for (Tramite tramite : tramiteAuxi) {
				tramiteItems.add(new SelectItem(tramite.getTrmId(), tramite
						.getTrmNom()));
			}
		}
	}

	/**
	 * Carga items de devoluciones
	 */
	@SuppressWarnings("unchecked")
	public void cargaDevolucionesItems() {
		if (devolucionesItems == null) {
			List<Devoluciones> devolucionesAuxi = (List<Devoluciones>) daoServicio
					.getGenericCommonDao().executeFind(
							Devoluciones.NAMED_QUERY_ALL_DEVOLUCION);

			devolucionesItems = new ArrayList<SelectItem>();
			for (Devoluciones devoluciones : devolucionesAuxi) {
				devolucionesItems.add(new SelectItem(devoluciones.getDevId(),
						devoluciones.getDevNom()));
			}

		}
	}

	/**
	 * Carga lista tipo de peronas tipo de personas.
	 */
	@SuppressWarnings("unchecked")
	public void cargaTipoPersonaItems() {
		if (tiposPersonaItems == null) {
			List<Tipospersona> tipospersonaAuxi = (List<Tipospersona>) daoServicio
					.getGenericCommonDao().executeFind(
							Tipospersona.NAMED_QUERY_ALL_TP);

			tiposPersonaItems = new ArrayList<SelectItem>();
			for (Tipospersona tipospersona : tipospersonaAuxi) {
				tiposPersonaItems.add(new SelectItem(tipospersona.getTpeId(),
						tipospersona.getTpeNom()));
			}
		}
	}

	/**
	 * 
	 * carga lista de usuarios
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void cargaUsuariosItems() {
		if (usuariosItems == null) {
			List<Usuario> usuariosAuxi = (List<Usuario>) daoServicio
					.getGenericCommonDao().executeFind(
							Usuario.NAMED_QUERY_ALL_USUARIOS);

			usuariosItems = new ArrayList<SelectItem>();

			for (Usuario usuarioTemp : usuariosAuxi) {
				usuariosItems.add(new SelectItem(usuarioTemp.getUsrId(),
						usuarioTemp.getUsrNom() + usuarioTemp.getUsrPap()
								+ usuarioTemp.getUsrSap()));
			}
		}
	}

	/**
	 * Carga Lista de calidad de representante , se obtiene nombre y id.
	 */
	@SuppressWarnings("unchecked")
	public void cargaCalidadRepresentanteItems() {
		if (calidadRepresentateItems == null) {
			List<Calidadrepresentante> calidadRepresentanteAuxi = (List<Calidadrepresentante>) daoServicio
					.getGenericCommonDao().executeFind(
							Calidadrepresentante.NAMED_QUERY_ALL_CR);

			calidadRepresentateItems = new ArrayList<SelectItem>();
			for (Calidadrepresentante calidadrepresentante : calidadRepresentanteAuxi) {
				calidadRepresentateItems.add(new SelectItem(
						calidadrepresentante.getCalId(), calidadrepresentante
								.getCalNom()));
			}
		}
	}

	/**
	 * Carga lista de anexos para cumplir con funcion en informacion del
	 * expediente.
	 */
	@SuppressWarnings("unchecked")
	public void cargaAnexosItems() {
		if (anexosItems == null) {
			List<Anexo> anexoAuxi = (List<Anexo>) daoServicio
					.getGenericCommonDao().loadAll(Anexo.class);

			anexosItems = new ArrayList<SelectItem>();
			for (Anexo anexo : anexoAuxi) {
				anexosItems.add(new SelectItem(anexo.getAnxId(), anexo
						.getAnxNom()));
			}

		}
	}

	public void cargaSeguimientosExp() {

		System.out.println(" Cargando Historial ****  : "
				+ expedienteSeleccionado.getSeguimientoexpedientes().size());

		Set<Seguimientoexpediente> objSegui = expedienteSeleccionado
				.getSeguimientoexpedientes();

		listaDummyExpediente = new ArrayList<Seguimientoexpediente>();

		for (Seguimientoexpediente seguimientoexpediente : objSegui) {
			System.out.println("Cargando movimiento N°: "
					+ seguimientoexpediente.getSgeId());

			listaDummyExpediente
					.add(new Seguimientoexpediente(seguimientoexpediente
							.getSgeId(), seguimientoexpediente.getEvento(),
							seguimientoexpediente.getEstadoBySgeEstini(),
							seguimientoexpediente.getEstadoBySgeEst(),
							seguimientoexpediente.getExpediente(),
							seguimientoexpediente.getUsuario(),
							seguimientoexpediente.getSgeDep(),
							seguimientoexpediente.getUsuario(),
							seguimientoexpediente.getSgeFec(),
							seguimientoexpediente.getSgeDes(),
							seguimientoexpediente.getSgeObs(),
							seguimientoexpediente.getSgeFecorg(),
							seguimientoexpediente.getSgeDev()));
		}

	}

	/**
	 * Carga la anotaciones por el expediente seleccionado , uso para
	 * informacion de expediente
	 */
	@SuppressWarnings({ "unchecked" })
	public void cargaAnotaciones() {
		System.out.println("***Cargando Anotaciones***");
		HashMap<Integer, Integer> parametrosBusq = new HashMap<Integer, Integer>();

		parametrosBusq.put(0, idExpedienteSeleccionado);
		listaAnotaciones = new ArrayList<Anotacionesexpedientes>();
		listaAnotaciones = (List<Anotacionesexpedientes>) daoServicio
				.getGenericCommonDao().executeFind(
						Anotacionesexpedientes.class, parametrosBusq,
						Anotacionesexpedientes.NAMED_QUERY_GET_ALL_BYEXP);

	}

	/**
	 * Carga lista de eventos para el expediente seleccionado
	 */
	@SuppressWarnings({ "unchecked" })
	public void cargaEventoItems() {

		if (eventoItems == null && expedienteSeleccionado.getExpId() != 0) {
			HashMap<Integer, Object> parametrosBusqueda = new HashMap<Integer, Object>();

			System.out.println("Eventos****");
			System.out.println("Id Tramite de expediente: "
					+ expedienteSeleccionado.getTramite().getTrmId());
			System.out.println("Id Estado de expediente: "
					+ expedienteSeleccionado.getEstado().getEstId());
			System.out
					.println("Tipo de evento: "
							+ Byte.parseByte(ExpedientesConstants.TIPOEVENTO_SECUNDARIO));

			parametrosBusqueda.put(0, expedienteSeleccionado.getTramite()
					.getTrmId());
			parametrosBusqueda.put(1, expedienteSeleccionado.getEstado()
					.getEstId());
			parametrosBusqueda.put(2, Byte.parseByte(
					ExpedientesConstants.TIPOEVENTO_SECUNDARIO, 3));

			List<Evento> eventoAuxi = (List<Evento>) daoServicio
					.getGenericCommonDao()
					.executeFind(Evento.class, parametrosBusqueda,
							Evento.NAMED_QUERY_GET_EVEBYEXP);

			eventoItems = new ArrayList<SelectItem>();

			for (Evento evento : eventoAuxi) {
				System.out.println("Evento Id :" + evento.getEveId()
						+ " Evento Nombre : " + evento.getEveNom());
				eventoItems.add(new SelectItem(evento.getEveId(), evento
						.getEveNom()));
			}
		}

	}

	/**
	 * Carga los anexos por el expediente seleccionado , uso para informacion de
	 * expediente
	 */
	@SuppressWarnings({ "unchecked" })
	public void cargaAnexos() {
		System.out.println("***Cargando Anexos***");
		HashMap<Integer, Integer> parametrosBusq = new HashMap<Integer, Integer>();

		parametrosBusq.put(0, idExpedienteSeleccionado);
		listaExpedienteAnexo = new ArrayList<Expedienteanexo>();
		listaExpedienteAnexo = (List<Expedienteanexo>) daoServicio
				.getGenericCommonDao().executeFind(Expedienteanexo.class,
						parametrosBusq,
						Expedienteanexo.NAMED_QUERY_GET_ALL_BYEXPANX);
	}

	/**
	 * Carga las otras persona por el expediente seleccionado , uso para
	 * informacion de expediente
	 */
	@SuppressWarnings({ "unchecked" })
	public void cargaOtros() {
		System.out.println("***Cargando Otros***");
		HashMap<Integer, Integer> parametrosBusq = new HashMap<Integer, Integer>();

		parametrosBusq.put(0, idExpedienteSeleccionado);
		listaExpedientepersonas = new ArrayList<Expedientepersona>();
		listaExpedientepersonas = (List<Expedientepersona>) daoServicio
				.getGenericCommonDao().executeFind(Expedientepersona.class,
						parametrosBusq,
						Expedientepersona.NAMED_QUERY_GET_ALL_BYEXPPER);
	}

	public void LimpiaObjPersonaNatural() {
		personaSeleccionada = null;
	}

	/**
	 * Carga anotacion seleccionada.
	 */
	public void cargaAnotacion() {
		try {
			System.out.println("anotacionSeleccionada" + anotacionSeleccionada);
			System.out.println("idAnotacionSeleccionada : "
					+ idAnotacionSeleccionada);

			if ((anotacionSeleccionada == null || anotacionSeleccionada
					.getAnoId() == 0)
					&& (idAnotacionSeleccionada != null && !idAnotacionSeleccionada
							.equals(0))) {
				anotacionSeleccionada = (Anotacionesexpedientes) daoServicio
						.getGenericCommonDao().read(
								Anotacionesexpedientes.class,
								idAnotacionSeleccionada);

			} else {
				anotacionSeleccionada = new Anotacionesexpedientes();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Carga anexo seleccionado
	 */
	public void cargaAnexo() {
		try {
			System.out.println("expedienteAnexoSeleccionado "
					+ expedienteAnexoSeleccionado);
			System.out.println("idExpedienteAnexo : " + idExpedienteanexoId);

			if ((expedienteAnexoSeleccionado == null || expedienteAnexoSeleccionado
					.getId() == null) && (idExpedienteanexoId != null)) {
				expedienteAnexoSeleccionado = (Expedienteanexo) daoServicio
						.getGenericCommonDao().read(Expedienteanexo.class,
								idExpedienteanexoId);

			} else {
				expedienteAnexoSeleccionado = new Expedienteanexo();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Carga la persona o entidad seleccionada
	 */
	public void cargaOtro() {
		try {
			System.out.println("expedientePersonaSeleccionada "
					+ expedientepersonaSeleccionada);
			System.out.println("idExpedientePersona : " + idExpedientePersona);

			if ((expedientepersonaSeleccionada == null || expedientepersonaSeleccionada
					.getEpeId() == 0) && (idExpedientePersona != null)) {
				expedientepersonaSeleccionada = (Expedientepersona) daoServicio
						.getGenericCommonDao().read(Expedientepersona.class,
								idExpedientePersona);

			} else {
				expedientepersonaSeleccionada = new Expedientepersona();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void cargaParametros() {

		System.out
				.println(" expedienteSeleccionado  " + expedienteSeleccionado);
		System.out.println(" idExpedienteSeleccionado  "
				+ idExpedienteSeleccionado);

		if ((expedienteSeleccionado == null || expedienteSeleccionado
				.getExpId() == 0)
				&& (idExpedienteSeleccionado != null && !idExpedienteSeleccionado
						.equals(0))) {

			expedienteSeleccionado = (Expediente) daoServicio
					.getGenericCommonDao().read(Expediente.class,
							idExpedienteSeleccionado);

			cargaSeguimientosExp();
			cargaAnotaciones();
			cargaAnexos();
			cargaOtros();

		} else if (expedienteSeleccionado != null) {
			expedienteSeleccionado = (Expediente) daoServicio
					.getGenericCommonDao().read(Expediente.class,
							idExpedienteSeleccionado);

			cargaSeguimientosExp();
			cargaAnotaciones();
			cargaAnexos();
			cargaOtros();
		} else {
			expedienteSeleccionado = new Expediente();
		}
	}

	public void cargaValoresEntrada() {
		if ((entradaSeleccionada == null || entradaSeleccionada.getEntId() != 0)
				&& (idEntradaSeleccionada != null && !idEntradaSeleccionada
						.equals(0))) {
			entradaSeleccionada = new Entrada();
		} else {
			this.fechaSolicitud = entradaSeleccionada.getEntFen();
			this.nomExpediente = entradaSeleccionada.getEntAsu();
			this.idDependSeleccionada = entradaSeleccionada.getDependencia()
					.getDepId();
			this.idTiposPersonaSeleccionado = entradaSeleccionada
					.getTipoPersona().getTpeId();
			Persona personaAuxi = daoServicio.getExpedienteDao().validaPersona(
					entradaSeleccionada);
			this.personaSeleccionada = personaAuxi;
			this.idCRepresentanteSeleccionado = entradaSeleccionada
					.getCalidadRepresentante().getCalId();
		}
	}

	public void limpiaGeneric() {
		this.observacionGeneric = null;
		this.nombreGeneric = null;
		this.fechaUnoGeneric = null;
		this.fechaDosGeneric = null;
	}

	public void cargaFiltrosDataModel() {
		List<Criterion> filtros = new ArrayList<Criterion>();
		filtros.add(Restrictions.eq("tiposexpediente.texId",
				ConstantsKeysFire.TIPOEXPOTROS));
		filtros.add(Restrictions.eq("usuarioTemp.usrId", UserDetailsUtils
				.usuarioLogged().getUsrId()));

		if (idTramiteSeleccionado != null && idTramiteSeleccionado != 0) {
			filtros.add(Restrictions.eq("tramite.trmId", idTramiteSeleccionado));
		}

		if (noExpediente != null && !noExpediente.trim().equals("")) {
			filtros.add(Restrictions.eq("expCod", noExpediente));
		}

		if (nomExpediente != null && !nomExpediente.trim().equals("")) {
			filtros.add(Restrictions.ilike("expNom", "%" + nomExpediente + "%"));
		}

		if (nomSolicitante != null && !nomSolicitante.trim().equals("")) {
			filtros.add(Restrictions.ilike("persona.perNom", "%"
					+ nomSolicitante + "%"));
		}

		if (idUsuarioSeleccionado != null && idUsuarioSeleccionado != 0) {
			filtros.add(Restrictions.eq("usuarioTemp.usrId",
					idUsuarioSeleccionado));
		}

		if (idDependSeleccionada != null && idDependSeleccionada != 0) {
			filtros.add(Restrictions.eq("depend.depId", idDependSeleccionada));
		}

		if (fechaSolicitud != null) {
			System.out.println("" + fechaSolicitud.toString());
			// filtros.add(Restrictions.ilike("expFso", "%" +
			// fechaSolicitud.toString() + "%"));
		}

		if (tipodeRadicacion == ConstantsKeysFire.ENTRADA) {
			filtros.add(Restrictions.eq("entrada.entNen", noRadicacion));
		} else if (tipodeRadicacion == ConstantsKeysFire.SALIDA) {
			filtros.add(Restrictions.eq("entrada.entNen", noRadicacion));
		}

		Map<String, String> alias = new HashMap<String, String>();
		expedientesDataModel.setAlias(alias);
		expedientesDataModel.setFiltros(filtros);

	}

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	private static final class ExpedientesDataModel extends
			GenericDataModel<Expediente, Integer> {
		private static final long serialVersionUID = 1L;

		private ExpedientesDataModel() {
			super(Expediente.class);
			List<Criterion> filtros = new ArrayList<Criterion>();
			filtros.add(Restrictions.eq("tiposexpediente.texId",
					ConstantsKeysFire.TIPOEXPOTROS));
			filtros.add(Restrictions.eq("usuarioTemp.usrId", UserDetailsUtils
					.usuarioLogged().getUsrId()));
			setFiltros(filtros);
			setOrderBy(Order.asc("expNom"));
		}

		@Override
		protected Object getId(Expediente t) {
			return t.getExpId();
		}
	}

}
