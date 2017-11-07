package la.netco.registro.uilayer.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import la.netco.commons.utils.CriteriaAlias;
import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.persistencia.vo.Usuario;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.spring.security.CommonsModKeys;
import la.netco.core.spring.security.SecuredAction;
import la.netco.core.spring.security.UserDetailsUtils;
import la.netco.core.uilayer.beans.Constants;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.expedientes.utils.ExpedientesConstants;
import la.netco.persistencia.dto.commons.ArchivoRegistro;
import la.netco.persistencia.dto.commons.Calidadrepresentante;
import la.netco.persistencia.dto.commons.Claseregistro;
import la.netco.persistencia.dto.commons.ConstantsKeysFire;
import la.netco.persistencia.dto.commons.Expediente;
import la.netco.persistencia.dto.commons.ExpedienteEstado;
import la.netco.persistencia.dto.commons.Medioscorrespondencia;
import la.netco.persistencia.dto.commons.Registro;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.data.PageEvent;
import org.primefaces.model.StreamedContent;
/**
 * 
 * @author ediaz
 *
 */
@ManagedBean
@ViewScoped
public class BuscadorRegistroBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;
	// private RegistroDataModel listDatamodel;

	private String noRadicacionEntrada;
	private String noRadicacionSalida;
	private String noRegistro;

	private String noExpediente;
	private String nombreExpediente;
	private String solicitante;

	private List<SelectItem> claseRegistroItems;
	private Integer idClaseRegistroSeleccionado;

	private List<SelectItem> mediosItems;
	private Short idMedioSeleccionada;

	private Date fechaSolicitudIni;
	private Date fechaSolicitudFin;

	private List<SelectItem> usuariosItems;
	private Short idUsuarioSeleccionado;

	private Boolean adminCorrespondencia;
	private String usuarioLogeado;

	private Expediente expedienteDetalle;

	private Boolean grupoAbogado;
	private Boolean grupoTecnico;

	private List<RegistroResumen> resultadoRegistros;

	private List<SelectItem> estadoGeneralItems;
	private Integer idEstadoGeneral;
	private Map<Integer, String> idsRegistrosSeleccionadas = new HashMap<Integer, String>();

	private boolean filtroimpresion;
	private boolean selectAll;
	private int first=0;

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public boolean isSelectAll() {
		
		return selectAll;
	}

	public void setSelectAll(boolean selectAll) {
		this.selectAll = selectAll;
	}
	
	public void onPageChange(PageEvent event) {  
		selectAll=false;
		System.out.println("onpagechange"+  event.getPage());
		
		for(RegistroResumen res: resultadoRegistros){
			idsRegistrosSeleccionadas.put(res.getRegId(),"false");
			
		}
		
        this.setFirst(event.getPage());  
   }
	
	public void seleccionar(){
		if(selectAll){
			for(int i=first*15;i<(first*15)+15;i++){
				RegistroResumen res=resultadoRegistros.get(i);
				idsRegistrosSeleccionadas.put(res.getRegId(),"true");
				
			}
		}else{
			for(RegistroResumen res: resultadoRegistros){
				idsRegistrosSeleccionadas.put(res.getRegId(),"false");
				
			}
		}
	}

	public BuscadorRegistroBean() {
		if (usuarioLogeado == null)
			usuarioLogeado = UserDetailsUtils.usernameLogged();

	}

	@PostConstruct
	public void init() {

		if (adminCorrespondencia == null && usuarioLogeado != null) {
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, usuarioLogeado);
			params.put(1, ConstantsKeysFire.GRUPO_ADMIN_CORRESPONDENCIA);

			Long adminGroup = (Long) serviceDao.getEntradaDao().executeUniqueResult(Usuario.NAMED_QUERY_FIND_BY_USUARIO_GRUPO, params);
			if (adminGroup != null && !adminGroup.equals(new Integer(0).longValue())) {
				adminCorrespondencia = true;
			} else {
				adminCorrespondencia = false;
				params = new HashMap<Integer, Object>();
				params.put(0, usuarioLogeado);
				params.put(1, ConstantsKeysFire.GRUPO_REGISTRO_ABOGADOS);

				Long abogadoGroup = (Long) serviceDao.getEntradaDao().executeUniqueResult(Usuario.NAMED_QUERY_FIND_BY_USUARIO_GRUPO, params);
				if (abogadoGroup != null && !abogadoGroup.equals(new Integer(0).longValue())) {
					grupoAbogado = true;
				} else {
					grupoAbogado = false;
					params = new HashMap<Integer, Object>();
					params.put(0, usuarioLogeado);
					params.put(1, ConstantsKeysFire.GRUPO_REGISTRO_TECNICO);

					Long tecnicoGroup = (Long) serviceDao.getEntradaDao().executeUniqueResult(Usuario.NAMED_QUERY_FIND_BY_USUARIO_GRUPO, params);
					if (tecnicoGroup != null && !tecnicoGroup.equals(new Integer(0).longValue())) {
						grupoTecnico = true;
					} else {
						grupoTecnico = false;
					}
				}
			}

		}

	}

	// @SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod
	// =CommonsModKeys.MNUE)
	public String cambiaEtapaNavigation() {
		return "cambiaEtapaReg";
	}

	// @SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod
	// =CommonsModKeys.MNUE)
	public String editarRegistroNavigation() {
		return "editarRegistro";
	}

	// @SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod
	// =CommonsModKeys.MNUE)
	public String detalleRegistroNavigation() {
		return "detalleRegistro";
	}

	// @SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod
	// =CommonsModKeys.MNUE)
	public String devolverRegistroNavigation() {
		return "devolucionRechazo";
	}

	// @SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod
	// =CommonsModKeys.MNUE)
	public String adjArchivosRegistroNavigation() {
		return "archivosAdjuntos";
	}

	@SecuredAction(keyAction = ExpedientesConstants.INFO_EXP, keyMod = CommonsModKeys.MNUEPAE)
	public String infoExpedienteNavegacion() {
		return "infoexpediente";
	}

	// @SecuredAction(keyAction = ExpedientesConstants.INFO_EXP, keyMod =
	// CommonsModKeys.MNUEPAE)
	public String generarConsecutivoManualNavegacion() {
		return "generarConsecutivoManual";
	}

	// @SecuredAction(keyAction = ExpedientesConstants.DEV_EXP, keyMod =
	// CommonsModKeys.MNUEPAE)
	public String guardaFlashRegistroSeleccionadosDevolucion() {
		FacesUtils.flashScope(FacesContext.getCurrentInstance()).put("idsRegistrosSeleccionadas", idsRegistrosSeleccionadas);
		return "devolucionRechazo";
	}

	// @SecuredAction(keyAction = ExpedientesConstants.DEV_EXP, keyMod =
	// CommonsModKeys.MNUEPAE)
	public String guardaFlashRegistroSeleccionadosCodigo() {
		FacesUtils.flashScope(FacesContext.getCurrentInstance()).put("idsRegistrosSeleccionadas", idsRegistrosSeleccionadas);
		return "generarCodigoMasivo";
	}
	
	public String guardaFlashCambiarEtapa() {
		FacesUtils.flashScope(FacesContext.getCurrentInstance()).put("idsRegistrosSeleccionadas", idsRegistrosSeleccionadas);
		return "cambiaEtapaReg";
	}
	
	public String guardaFlashRegistroSeleccionadosSalida() {
		FacesUtils.flashScope(FacesContext.getCurrentInstance()).put("certificado", 3);
		FacesUtils.flashScope(FacesContext.getCurrentInstance()).put("idsRegistrosSeleccionadas", idsRegistrosSeleccionadas);
		return "imprimirRegistro";
	}

	// @SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod
	// =CommonsModKeys.MNUE)
	public String imprimirRegistroNavigation() {
		FacesUtils.flashScope(FacesContext.getCurrentInstance()).put("certificado", 1);
		return "imprimirRegistro";
	}
	
	public String salidaRegistroNavigation() {
		FacesUtils.flashScope(FacesContext.getCurrentInstance()).put("certificado", 3);
		return "imprimirRegistro";
	}

	// @SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod
	// =CommonsModKeys.MNUE)
	public String imprimirCertificadoNavigation() {
		FacesUtils.flashScope(FacesContext.getCurrentInstance()).put("certificado", 2);
		return "imprimirRegistro";
	}

	// @SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod
	// =CommonsModKeys.MNUE)
	public String registroSalidasNavigation() {
		return "registroSalidas";
	}

	// @SecuredAction(keyAction = ExpedientesConstants.DEV_EXP, keyMod =
	// CommonsModKeys.MNUEPAE)
	public String guardaFlashRegistroSeleccionadosImprimir() {
		FacesUtils.flashScope(FacesContext.getCurrentInstance()).put("certificado", 1);
		FacesUtils.flashScope(FacesContext.getCurrentInstance()).put("idsRegistrosSeleccionadas", idsRegistrosSeleccionadas);
		return "imprimirRegistro";
	}

	@SuppressWarnings("unchecked")
	public void cargaListaDataModel() {

		try {
			System.out.println("----------------IMPRESION-----");
			List<Criterion> filtros = new ArrayList<Criterion>();
			List<CriteriaAlias> alias = new ArrayList<CriteriaAlias>();
			boolean filtro = false;
			
			System.out.println("filtro "+noRadicacionEntrada);
			if (noRadicacionEntrada != null && !noRadicacionEntrada.trim().equals("")) {
				filtros.add(Restrictions.eq("entrada.entNen", noRadicacionEntrada));
				filtro = true;
			}

			if (noRadicacionSalida != null && !noRadicacionSalida.trim().equals("")) {
				filtros.add(Restrictions.eq("salida.salNsa", noRadicacionSalida));
				filtro = true;
			}

			if (noRegistro != null && !noRegistro.trim().equals("")) {
				filtros.add(Restrictions.eq("regCod", noRegistro));
				filtro = true;
			}

			if (noExpediente != null && !noExpediente.trim().equals("")) {
				filtros.add(Restrictions.eq("expediente.expCod", noExpediente));
				filtro = true;
			}

			if (nombreExpediente != null && !nombreExpediente.trim().equals("")) {
				filtros.add(Restrictions.ilike("expediente.expNom", "%" + nombreExpediente + "%"));
				filtro = true;
			}

			if (solicitante != null && !solicitante.trim().equals("")) {
				filtros.add(Restrictions.ilike("persona.nombreCompleto", "%" + solicitante + "%"));
			}

			if (fechaSolicitudIni != null || fechaSolicitudIni != null) {

				if (fechaSolicitudIni != null && fechaSolicitudFin == null) {

					filtros.add(Restrictions.ge("expediente.expFso", fechaSolicitudIni));

				} else if (fechaSolicitudIni == null && fechaSolicitudFin != null) {

					Calendar fecha = Calendar.getInstance();
					fecha.setTime(fechaSolicitudFin);
					fecha.set(Calendar.HOUR_OF_DAY, 24);
					filtros.add(Restrictions.le("expediente.expFso", fecha.getTime()));

				} else {

					Calendar fecha = Calendar.getInstance();
					fecha.setTime(fechaSolicitudFin);
					fecha.set(Calendar.HOUR_OF_DAY, 24);
					filtros.add(Restrictions.between("expediente.expFso", fechaSolicitudIni, fecha.getTime()));

				}

			}
			

			if (idMedioSeleccionada != null && idMedioSeleccionada != 0) {
				if(idMedioSeleccionada==999){
					filtros.add(Restrictions.ne("medio.medNom", "REGISTRO EN LINEA"));
				}else{
					filtros.add(Restrictions.eq("medio.medId", idMedioSeleccionada));
				}
				filtro = true;
			}
			if (idClaseRegistroSeleccionado != null && idClaseRegistroSeleccionado != 0) {
				filtros.add(Restrictions.eq("claseregistro.creId", idClaseRegistroSeleccionado));
				filtro = true;
			}

			if (!filtroimpresion) {
				System.out.println("filtro no es impresion y usuario "+usuarioLogeado);

				if (adminCorrespondencia == true) {
					if (idUsuarioSeleccionado != null && idUsuarioSeleccionado != 0) {
						filtros.add(Restrictions.eq("usuario.usrId", idUsuarioSeleccionado));
					} else if (!filtro) {
						filtros.add(Restrictions.eq("usuario.usrLog", usuarioLogeado));
					}
				} else {
					System.out.println("no es admincorrespondencia");
					if (grupoAbogado) {
						System.out.println("grupo abogado");
						Criterion condicionAuxi = null;
						Short[] calidad = new Short[] { Calidadrepresentante.R_PERSONA_NATURAL, Calidadrepresentante.R_PERSONA_JURIDICA, Calidadrepresentante.R_MENOR_DE_EDAD };
						Criterion calidadRepresentante = Restrictions.in("expediente.calidadrepresentante.calId", calidad);
						Criterion registroPersona = Restrictions.eq("personareg.perLviv", true);

						String elementosAbogado = FacesUtils.getValorPropiedad("elementos.filtroAbogado");

						List<Integer> elementos = new ArrayList<Integer>();

						for (String ele : elementosAbogado.split(",")) {
							elementos.add(Integer.parseInt(ele));
						}

						Criterion elementosregistros = Restrictions.in("elementoregistros.elemento.eleId", elementos.toArray());

						Criterion condicion1 = Restrictions.or(calidadRepresentante, registroPersona);

						Criterion condicion2 = Restrictions.or(elementosregistros, condicion1);

						Criterion condicion3 = Restrictions.or(condicion2, Restrictions.eq("usuario.usrLog", usuarioLogeado));

						alias.add(new CriteriaAlias("registropersonas", "registropersonas", Criteria.INNER_JOIN));
						alias.add(new CriteriaAlias("registropersonas.persona", "personareg", Criteria.INNER_JOIN));
						alias.add(new CriteriaAlias("elementoregistros", "elementoregistros", Criteria.INNER_JOIN));

						if ((idEstadoGeneral == null || idEstadoGeneral.equals(-1)) && !filtro) {
							filtros.add(Restrictions.eq("expediente.estadoGeneral.id", ExpedienteEstado.TRAMITE));
						} else if (idEstadoGeneral != null && !idEstadoGeneral.equals(-1)) {
							filtros.add(Restrictions.eq("expediente.estadoGeneral.id", idEstadoGeneral));
						}

						if ((idClaseRegistroSeleccionado == null || idClaseRegistroSeleccionado.equals(-1) && !filtro)) {
							condicionAuxi = Restrictions.eq("claseregistro.creId", ConstantsKeysFire.CLASECONTRATOS);
						} else if (idClaseRegistroSeleccionado != null && !idClaseRegistroSeleccionado.equals(-1)) {
							condicionAuxi = Restrictions.eq("claseregistro.creId", idClaseRegistroSeleccionado);
						}

						Criterion condicion4 = Restrictions.or(condicionAuxi, condicion3);

						filtros.add(condicion4);

					} else if (grupoTecnico) {
						System.out.println("grupotecnico");

						Criterion condicionAuxi = null;
						Criterion condicionAuxi2 = null;
						Short[] calidad = new Short[] { Calidadrepresentante.R_PERSONA_NATURAL, Calidadrepresentante.R_PERSONA_JURIDICA, Calidadrepresentante.R_MENOR_DE_EDAD };

						Criterion calidadRepresentante = Restrictions.in("expediente.calidadrepresentante.calId", calidad);

						Criterion registroPersona = Restrictions.eq("personareg.perLviv", true);

						String elementosAbogado = FacesUtils.getValorPropiedad("elementos.filtroAbogado");

						List<Integer> elementos = new ArrayList<Integer>();

						for (String ele : elementosAbogado.split(",")) {
							elementos.add(Integer.parseInt(ele));
						}

						Criterion elementosregistros = Restrictions.in("elementoregistros.elemento.eleId", elementos.toArray());
						
						if ((idEstadoGeneral == null || idEstadoGeneral.equals(-1)) && !filtro) {
							System.out.println("*********entro");
							condicionAuxi = Restrictions.eq("expediente.estadoGeneral.id", ExpedienteEstado.TRAMITE);
						} else if (idEstadoGeneral != null && !idEstadoGeneral.equals(-1)) {
							condicionAuxi = Restrictions.eq("expediente.estadoGeneral.id", idEstadoGeneral);
						}

						if ((idClaseRegistroSeleccionado == null || idClaseRegistroSeleccionado.equals(-1) && !filtro)) {
							condicionAuxi2 = Restrictions.eq("claseregistro.creId", ConstantsKeysFire.CLASECONTRATOS);
						} else if (idClaseRegistroSeleccionado != null && !idClaseRegistroSeleccionado.equals(-1)) {
							condicionAuxi2 = Restrictions.eq("claseregistro.creId", idClaseRegistroSeleccionado);
						}

						Criterion condicionUno = Restrictions.and(calidadRepresentante, registroPersona);
						Criterion condicionDos = Restrictions.and(elementosregistros, condicionUno);

						Criterion condicionTres = Restrictions.and(condicionAuxi, Restrictions.not(condicionAuxi2));

						Criterion condicionCuatro = Restrictions.and(condicionTres, Restrictions.not(condicionDos));

						alias.add(new CriteriaAlias("registropersonas", "registropersonas", Criteria.INNER_JOIN));
						alias.add(new CriteriaAlias("registropersonas.persona", "personareg", Criteria.INNER_JOIN));
						alias.add(new CriteriaAlias("elementoregistros", "elementoregistros", Criteria.INNER_JOIN));

						Criterion condicionCinco = Restrictions.or(condicionCuatro, Restrictions.eq("usuario.usrLog", usuarioLogeado));

						filtros.add(condicionCinco);
//
					} else {

						filtros.add(Restrictions.eq("usuario.usrLog", usuarioLogeado));

						if ((idEstadoGeneral == null || idEstadoGeneral.equals(-1)) && !filtro) {
							filtros.add(Restrictions.eq("expediente.estadoGeneral.id", ExpedienteEstado.TRAMITE));
						} else if (idEstadoGeneral != null && !idEstadoGeneral.equals(-1)) {
							filtros.add(Restrictions.eq("expediente.estadoGeneral.id", idEstadoGeneral));
						}

					}

				}

			} else {
				System.out.println("Es para impresion");
				if (idEstadoGeneral != null && !idEstadoGeneral.equals(-1)) {
					filtros.add(Restrictions.eq("expediente.estadoGeneral.id", idEstadoGeneral));
				}

			}

			alias.add(new CriteriaAlias("expediente", "expediente", Criteria.INNER_JOIN));
			alias.add(new CriteriaAlias("expediente.estado", "estado", Criteria.INNER_JOIN));
			alias.add(new CriteriaAlias("expediente.estadoGeneral", "estadoGeneral", Criteria.INNER_JOIN));
			alias.add(new CriteriaAlias("expediente.usuarioTemp", "usuario", Criteria.INNER_JOIN));
			alias.add(new CriteriaAlias("expediente.expedientecorresponds", "expedientecorresponds", Criteria.LEFT_JOIN));
			alias.add(new CriteriaAlias("expedientecorresponds.entrada", "entrada", Criteria.INNER_JOIN));
			alias.add(new CriteriaAlias("entrada.medio", "medio", Criteria.INNER_JOIN));
			alias.add(new CriteriaAlias("expediente.persona", "persona", Criteria.INNER_JOIN));
			alias.add(new CriteriaAlias("claseregistro", "claseregistro", Criteria.INNER_JOIN));

			if (noRadicacionSalida != null && !noRadicacionSalida.trim().equals("")) {
				alias.add(new CriteriaAlias("expedientecorresponds.salida", "salida", Criteria.INNER_JOIN));
			}

			// listDatamodel.setAliasCriteria(alias);
			// listDatamodel.setFiltros(filtros);

			Session session = serviceDao.getGenericCommonDao().getSessionFromHibernate();

			Criteria criteria = session.createCriteria(Registro.class);
			ProjectionList projectionList = Projections.projectionList();

			projectionList.add(Projections.alias(Projections.property("regId"), "regId"));

			projectionList.add(Projections.alias(Projections.property("entrada.entNen"), "entNen"));

			projectionList.add(Projections.alias(Projections.property("claseregistro.creNom"), "creNom"));

			projectionList.add(Projections.alias(Projections.property("regTor"), "regTor"));

			projectionList.add(Projections.alias(Projections.property("persona.perDoc"), "perDoc"));

			projectionList.add(Projections.alias(Projections.property("persona.nombreCompleto"), "soliciante"));

			projectionList.add(Projections.alias(Projections.property("expediente.expFso"), "expFso"));
			
			projectionList.add(Projections.alias(Projections.property("regFec"), "regFec"));

			projectionList.add(Projections.alias(Projections.property("medio.medNom"), "medNom"));

			projectionList.add(Projections.alias(Projections.property("regCod"), "regCod"));

			projectionList.add(Projections.alias(Projections.property("estadoGeneral.nombre"), "estadoGeneral"));

			projectionList.add(Projections.alias(Projections.property("estado.estNom"), "etapa"));

			projectionList.add(Projections.alias(Projections.property("regLblo"), "regLblo"));
			projectionList.add(Projections.alias(Projections.property("regLgen"), "regLgen"));
			projectionList.add(Projections.alias(Projections.property("expediente.expId"), "expId"));
			projectionList.add(Projections.alias(Projections.property("usuario.usrLog"), "usuario"));

			if (alias != null) {
				for (CriteriaAlias alia : alias) {
					criteria.createAlias(alia.getColumna(), alia.getAlias(), alia.getTipo());
				}
			}

			if (filtros != null) {
				for (Criterion iterable_element : filtros) {
					criteria.add(iterable_element);
				}
			}
			criteria.setProjection(Projections.distinct(projectionList));
			criteria.setResultTransformer(Transformers.aliasToBean(RegistroResumen.class));
			// criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			criteria.addOrder(Order.desc("regId"));
			criteria.setMaxResults(100);
			resultadoRegistros = criteria.list();
			/*Incluye Documentos en listado principal*/
			for (RegistroResumen registroResumen : resultadoRegistros){
				int regId = registroResumen.getRegId();
				List<ArchivoRegistro> archivosregistroTemp = null;
				Map<Integer, Object> params = new HashMap<Integer, Object>();
				params.put(0,regId);
				archivosregistroTemp = (List<ArchivoRegistro>) serviceDao
						.getGenericCommonDao()
						.executeFind(ArchivoRegistro.class, params,
								ArchivoRegistro.NAMED_QUERY_GET_BY_REGISTRO_SIN_PDF);
				if(archivosregistroTemp != null && archivosregistroTemp.size()>0){
					registroResumen.setArchivosregistro(archivosregistroTemp);
				}
				
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void obtenerExpediente(Integer regId) {
		try {

			Registro registro = (Registro) serviceDao.getGenericCommonDao().read(Registro.class, regId);
			expedienteDetalle = (Expediente) serviceDao.getGenericCommonDao().read(Expediente.class, registro.getExpediente().getExpId());

		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION, FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
		}

	}

	public void obtenerEntrada(Registro registro) {
		try {
			expedienteDetalle = (Expediente) serviceDao.getGenericCommonDao().read(Expediente.class, registro.getExpediente().getExpId());

		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION, FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public void cargarMediosItems() {

		if (mediosItems == null) {

//			List<Medioscorrespondencia> allData = (List<Medioscorrespondencia>) serviceDao.getGenericCommonDao().executeFind(Medioscorrespondencia.NAMED_QUERY_ALL);

			mediosItems = new ArrayList<SelectItem>();

//			for (Medioscorrespondencia dato : allData) {
				mediosItems.add(new SelectItem(17, "REGISTRO EN LINEA"));
				mediosItems.add(new SelectItem(999, "OTROS"));
//			}

		} else if (mediosItems == null) {
			mediosItems = new ArrayList<SelectItem>();
		}
	}

	/*
	 * private static final class RegistroDataModel extends
	 * PrimeDataModel<Registro, Integer> {
	 * 
	 * private static final long serialVersionUID = 1L;
	 * 
	 * private RegistroDataModel () { super(Registro.class);
	 * setRowCounter(Projections
	 * .sqlProjection("count(DISTINCT this_.REG_ID) as rowcounter", new String[]
	 * {"rowcounter"}, new Type[] {Hibernate.LONG}));
	 * setOrderBy(Order.desc("regId")); }
	 * 
	 * @Override protected Object getId(Registro t) { return t.getRegId(); } }
	 */

	@SuppressWarnings("unchecked")
	public void cargaClaseRegistrosItems() {
		if (claseRegistroItems == null) {
			List<Claseregistro> registros = (List<Claseregistro>) serviceDao.getGenericCommonDao().executeFind(Claseregistro.NAMED_QUERY_GET_ALL_ACTIVOS);

			claseRegistroItems = new ArrayList<SelectItem>();
			for (Claseregistro registro : registros) {
				claseRegistroItems.add(new SelectItem(registro.getCreId(), registro.getCreNom()));
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void cargarUsuariosItems() {
		if (usuariosItems == null) {
			List<Usuario> usuarios = (List<Usuario>) serviceDao.getGenericCommonDao().executeFind(Usuario.NAMED_QUERY_ALL_USUARIOS_ACTIVOS);
			usuariosItems = new ArrayList<SelectItem>();
			for (Usuario usu : usuarios) {
				usuariosItems.add(new SelectItem(usu.getUsrId(), usu.getUsrNom() + " " + usu.getUsrPap() + " " + usu.getUsrSap()));
			}
		}
	}

	public void cargarEstadoImpresion() {
		System.out.println(" cargarEstadoImpresion init param ");
		idEstadoGeneral = ExpedienteEstado.REGISTRADO_AUTO;
		filtroimpresion = true;
	}

	public void cargarEstadoSalida() {
		System.out.println(" cargarEstadoSalida init param ");
		idEstadoGeneral = ExpedienteEstado.IMPRESO;
		filtroimpresion = true;
	}
	public void cargarEstadoRegistro() {
		System.out.println(" cargarEstadoRegistro init param ");
		idEstadoGeneral = ExpedienteEstado.TRAMITE;
		filtroimpresion = true;
	}
	
	public StreamedContent descargar(Integer idArchivo) {
		
		System.out.println(" Hola");
		return null;
		
		
	}

	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	/*
	 * public RegistroDataModel getListDatamodel() { return listDatamodel; }
	 */
	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	/*
	 * public void setListDatamodel(RegistroDataModel listDatamodel) {
	 * this.listDatamodel = listDatamodel; }
	 */

	public List<SelectItem> getUsuariosItems() {
		cargarUsuariosItems();
		return usuariosItems;
	}

	public Short getIdUsuarioSeleccionado() {
		return idUsuarioSeleccionado;
	}

	public void setUsuariosItems(List<SelectItem> usuariosItems) {
		this.usuariosItems = usuariosItems;
	}

	public void setIdUsuarioSeleccionado(Short idUsuarioSeleccionado) {
		this.idUsuarioSeleccionado = idUsuarioSeleccionado;
	}

	public String getNoRegistro() {
		return noRegistro;
	}

	public String getNoExpediente() {
		return noExpediente;
	}

	public String getNombreExpediente() {
		return nombreExpediente;
	}

	public void setNoRegistro(String noRegistro) {
		this.noRegistro = noRegistro;
	}

	public void setNoExpediente(String noExpediente) {
		this.noExpediente = noExpediente;
	}

	public void setNombreExpediente(String nombreExpediente) {
		this.nombreExpediente = nombreExpediente;
	}

	public String getNoRadicacionEntrada() {
		return noRadicacionEntrada;
	}

	public String getNoRadicacionSalida() {
		return noRadicacionSalida;
	}

	public void setNoRadicacionEntrada(String noRadicacionEntrada) {
		this.noRadicacionEntrada = noRadicacionEntrada;
	}

	public void setNoRadicacionSalida(String noRadicacionSalida) {
		this.noRadicacionSalida = noRadicacionSalida;
	}

	public String getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(String solicitante) {
		this.solicitante = solicitante;
	}

	public Boolean getAdminCorrespondencia() {
		return adminCorrespondencia;
	}

	public void setAdminCorrespondencia(Boolean adminCorrespondencia) {
		this.adminCorrespondencia = adminCorrespondencia;
	}

	public Expediente getExpedienteDetalle() {
		return expedienteDetalle;
	}

	public void setExpedienteDetalle(Expediente expedienteDetalle) {
		this.expedienteDetalle = expedienteDetalle;
	}

	public Boolean getGrupoAbogado() {
		return grupoAbogado;
	}

	public void setGrupoAbogado(Boolean grupoAbogado) {
		this.grupoAbogado = grupoAbogado;
	}

	public List<RegistroResumen> getResultadoRegistros() {
		if (resultadoRegistros == null) {
			cargaListaDataModel();
		}
		return resultadoRegistros;
	}

	public void setResultadoRegistros(List<RegistroResumen> resultadoRegistros) {
		this.resultadoRegistros = resultadoRegistros;
	}

	public List<SelectItem> getClaseRegistroItems() {
		cargaClaseRegistrosItems();
		return claseRegistroItems;
	}

	public Integer getIdClaseRegistroSeleccionado() {
		return idClaseRegistroSeleccionado;
	}

	public void setClaseRegistroItems(List<SelectItem> claseRegistroItems) {
		this.claseRegistroItems = claseRegistroItems;
	}

	public void setIdClaseRegistroSeleccionado(Integer idClaseRegistroSeleccionado) {
		this.idClaseRegistroSeleccionado = idClaseRegistroSeleccionado;
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

	public Boolean getGrupoTecnico() {
		return grupoTecnico;
	}

	public void setGrupoTecnico(Boolean grupoTecnico) {
		this.grupoTecnico = grupoTecnico;
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getEstadoGeneralItems() {
		if (estadoGeneralItems == null) {
			List<ExpedienteEstado> registros = (List<ExpedienteEstado>) serviceDao.getGenericCommonDao().executeFind(ExpedienteEstado.NAMED_QUERY_GET_ALL);

			estadoGeneralItems = new ArrayList<SelectItem>();
			for (ExpedienteEstado registro : registros) {
				estadoGeneralItems.add(new SelectItem(registro.getId(), registro.getNombre()));
			}
		}
		return estadoGeneralItems;
	}

	public Integer getIdEstadoGeneral() {
		return idEstadoGeneral;
	}

	public void setEstadoGeneralItems(List<SelectItem> estadoGeneralItems) {
		this.estadoGeneralItems = estadoGeneralItems;
	}

	public void setIdEstadoGeneral(Integer idEstadoGeneral) {
		this.idEstadoGeneral = idEstadoGeneral;
	}

	public Date getFechaSolicitudIni() {
		return fechaSolicitudIni;
	}

	public Date getFechaSolicitudFin() {
		return fechaSolicitudFin;
	}

	public void setFechaSolicitudIni(Date fechaSolicitudIni) {
		this.fechaSolicitudIni = fechaSolicitudIni;
	}

	public void setFechaSolicitudFin(Date fechaSolicitudFin) {
		this.fechaSolicitudFin = fechaSolicitudFin;
	}

	public Map<Integer, String> getIdsRegistrosSeleccionadas() {
		return idsRegistrosSeleccionadas;
	}

	public void setIdsRegistrosSeleccionadas(Map<Integer, String> idsRegistrosSeleccionadas) {
		this.idsRegistrosSeleccionadas = idsRegistrosSeleccionadas;
	}

	public boolean isFiltroimpresion() {
		return filtroimpresion;
	}

	public void setFiltroimpresion(boolean filtroimpresion) {
		this.filtroimpresion = filtroimpresion;
	}

}
