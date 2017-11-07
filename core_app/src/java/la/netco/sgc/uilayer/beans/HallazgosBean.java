package la.netco.sgc.uilayer.beans;

import java.io.Serializable;
import java.util.ArrayList;
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
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.uilayer.beans.Constants;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.sgc.persistence.dto.Area;
import la.netco.sgc.persistence.dto.Auditorias;
import la.netco.sgc.persistence.dto.EstadoAuditoria;
import la.netco.sgc.persistence.dto.Hallazgos;
import la.netco.uilayer.beans.PrimeDataModel;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author jhmoreno
 *
 */

@ManagedBean(name = "hallazgosBean")
@ViewScoped
public class HallazgosBean implements Serializable {

	/* Log */
	private static Logger log = LoggerFactory.getLogger(HallazgosBean.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;

	private HallazgosDataModel listDatamodel = null;
	private Hallazgos hallazgos;
	private Auditorias auditoriaSeleccionada;
	private Integer idAuditoriaSeleccionada;
	private Hallazgos hallazgoSeleccionado;
	private Integer idHallazgoSeleccionado;

	/* Select menu en pantalla */
	private List<SelectItem> areasItems;
	private Integer idArea;
	private List<SelectItem> estadoAuditoriaItems;
	private Integer idEstadoAuditoria;
	private String idTipo;
	private Date fechaVencimiento;
	private String condicion;
	private String norma;
	private String causa;
	private String recomendacion;
	private String observacion;

	public HallazgosBean() {
		this.hallazgos = new Hallazgos();
	}

	/**
	 * Auditorias data model
	 * 
	 * @author Alejo Medell�n
	 *
	 */
	private static final class HallazgosDataModel extends
			PrimeDataModel<Hallazgos, Integer> {
		private static final long serialVersionUID = 1L;

		private HallazgosDataModel() {
			super(Hallazgos.class);
			setOrderBy(Order.asc("halId"));
		}

		@Override
		protected Object getId(Hallazgos t) {
			return t.getClass();
		}
	}

	/**
	 * Carga listado inicial
	 */
	public void cargaListaDataModel() {
		if (listDatamodel == null) {
			listDatamodel = new HallazgosDataModel();
			List<Criterion> filtros = new ArrayList<Criterion>();

			filtros.add(Restrictions.isNotNull("halId"));
			filtros.add(Restrictions.eq("halAuditoria", auditoriaSeleccionada));

			Map<String, String> alias = new HashMap<String, String>();
			listDatamodel.setAlias(alias);
			listDatamodel.setFiltros(filtros);
		}
	}

	/**
	 * Cargar areas
	 */
	@SuppressWarnings("unchecked")
	public void cargarAreasItems() {

		if (areasItems == null) {
			/* Obtener List de areas */
			List<Area> listArea = (List<Area>) serviceDao.getGenericCommonDao()
					.executeFind(Area.NAMED_QUERY_ALL);

			areasItems = new ArrayList<SelectItem>();

			for (Area area : listArea) {
				areasItems.add(new SelectItem(area.getCodigoArea(), area
						.getDescripcionArea()));
			}

		} else if (areasItems == null) {
			areasItems = new ArrayList<SelectItem>();
		}
	}

	/**
	 * Cargar estados de auditoria
	 */
	@SuppressWarnings("unchecked")
	public void cargarEstadosAuditoria() {

		if (estadoAuditoriaItems == null) {
			/* Obtener List de estados */
			List<EstadoAuditoria> listEstados = (List<EstadoAuditoria>) serviceDao
					.getGenericCommonDao().executeFind(
							EstadoAuditoria.NAMED_QUERY_ALL);

			estadoAuditoriaItems = new ArrayList<SelectItem>();

			for (EstadoAuditoria estado : listEstados) {
				estadoAuditoriaItems.add(new SelectItem(estado
						.getCodigoEstadoAuditoria(), estado
						.getDescripcionEstadoAuditoria()));
			}

		} else if (estadoAuditoriaItems == null) {
			estadoAuditoriaItems = new ArrayList<SelectItem>();
		}
	}

	/**
	 * Crear Auditoria
	 */
	public String createOrReplace() {
		String page = null;
		try {
			log.info("Ingreso a crear auditorias");

			// Setea tipo
			if (idTipo != null) {
				hallazgos.setHalTipo(idTipo);
			}

			// Setea Area
			if (idArea != null) {
				Area area = (Area) serviceDao.getGenericCommonDao().read(
						Area.class, idArea);
				hallazgos.setHalArea(area);
			}

			// Setea estado
			if (idEstadoAuditoria != null) {
				EstadoAuditoria estadoAuditoria = (EstadoAuditoria) serviceDao
						.getGenericCommonDao().read(EstadoAuditoria.class,
								idEstadoAuditoria);
				hallazgos.setHalEstado(estadoAuditoria);
			}

			// Setea condicion
			if (condicion != null) {
				hallazgos.setHalCondicion(condicion);
			}

			// Setea norma
			if (norma != null) {
				hallazgos.setHalNorma(norma);
			}

			// Setea causa
			if (causa != null) {
				hallazgos.setHalCausa(causa);
			}

			// Setea recomendacion
			if (recomendacion != null) {
				hallazgos.setHalRecomendacion(recomendacion);
			}

			hallazgos.setHalAuditoria(auditoriaSeleccionada);

			// Setea fecha vencimiento
			if (fechaVencimiento != null) {
				hallazgos.setHalFechaVencimiento(fechaVencimiento);
			}

			hallazgos.setHalObservacion("");

			serviceDao.getGenericCommonDao().create(Hallazgos.class, hallazgos);
			FacesUtils.addFacesMessageFromBundle(
					Constants.OPERACION_EXITOSA_CREAR_REGISTRO,
					FacesMessage.SEVERITY_INFO);
			page = "listadoHallazgo";
			hallazgos = new Hallazgos();

		} catch (Exception exception) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_FATAL);
			log.error(exception.getMessage());
		}
		return page;
	}

	/**
	 * Metodo para cargar el la informaci�n de una auditoria
	 * */
	@SuppressWarnings("unchecked")
	public void cargarRegistro() {
		try {
			log.info("Cargar datos Auditoria");
			if (idAuditoriaSeleccionada == null) {
				FacesContext facesContext = FacesContext.getCurrentInstance();
				String idRegSeleccionado = facesContext.getExternalContext()
						.getRequestParameterMap()
						.get("idAuditoriaSeleccionada");
				if (idRegSeleccionado != null)
					this.idAuditoriaSeleccionada = Integer
							.parseInt(idRegSeleccionado);
			}
			if (idAuditoriaSeleccionada != null) {
				auditoriaSeleccionada = (Auditorias) serviceDao
						.getGenericCommonDao().read(Auditorias.class,
								idAuditoriaSeleccionada);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo para cargar el la informaci�n de una auditoria
	 * */
	@SuppressWarnings("unchecked")
	public void cargarRegistroHallazgo() {
		try {
			log.info("Cargar datos Hallazgo");
			if (idHallazgoSeleccionado == null) {
				FacesContext facesContext = FacesContext.getCurrentInstance();
				String idRegSeleccionado = facesContext.getExternalContext()
						.getRequestParameterMap().get("idHallazgoSel");
				if (idRegSeleccionado != null)
					this.idHallazgoSeleccionado = Integer
							.parseInt(idRegSeleccionado);
			}
			if (idHallazgoSeleccionado != null) {
				hallazgoSeleccionado = (Hallazgos) serviceDao
						.getGenericCommonDao().read(Hallazgos.class,
								idHallazgoSeleccionado);
			}
			if (hallazgoSeleccionado != null) {
				idTipo = hallazgoSeleccionado.getHalTipo();
				fechaVencimiento = hallazgoSeleccionado
						.getHalFechaVencimiento();
				idArea = hallazgoSeleccionado.getHalArea().getCodigoArea();
				idEstadoAuditoria = hallazgoSeleccionado.getHalEstado()
						.getCodigoEstadoAuditoria();
				condicion = hallazgoSeleccionado.getHalCondicion();
				norma = hallazgoSeleccionado.getHalNorma();
				causa = hallazgoSeleccionado.getHalCausa();
				recomendacion = hallazgoSeleccionado.getHalRecomendacion();
				observacion = hallazgoSeleccionado.getHalObservacion();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Actualiza estado hallazgo
	 */
	public String actualizarHallazgo() {
		String page = null;
		try {
			log.info("Ingreso a actualizar hallazgo"
					+ hallazgoSeleccionado.getHalId());

			if (hallazgoSeleccionado != null) {
				// Setea estado
				if (idEstadoAuditoria != null) {
					EstadoAuditoria ea = (EstadoAuditoria) serviceDao
							.getGenericCommonDao().read(EstadoAuditoria.class,
									idEstadoAuditoria);
					hallazgoSeleccionado.setHalEstado(ea);
				}

				// Setea observacion
				if (observacion != null) {
					hallazgoSeleccionado.setHalObservacion(observacion);
				} else {
					hallazgoSeleccionado.setHalObservacion("");
				}

				serviceDao.getGenericCommonDao().saveOrUpdate(Hallazgos.class,
						hallazgoSeleccionado);
			}
			FacesUtils.addFacesMessageFromBundle(
					Constants.OPERACION_EXITOSA_ACTUALIZAR_REGISTRO,
					FacesMessage.SEVERITY_INFO);
			page = "listadoHallazgo";
			hallazgoSeleccionado = new Hallazgos();
			limpiar();

		} catch (Exception exception) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_FATAL);
			log.error(exception.getMessage());

		}
		return page;
	}

	public void limpiar() {
		hallazgos = new Hallazgos();
		hallazgoSeleccionado = new Hallazgos();
	}

	public String navRegistrarHallazgo() {
		return "registrarHallazgo";
	}

	public String navListadoHallazgo() {
		return "listadoHallazgo";
	}

	public String cambiarEstadoNavigation() {
		return "cambiarEstadoHallazgo";
	}

	public String navCancelarP() {
		return "programar";
	}

	public static Logger getLog() {
		return log;
	}

	public static void setLog(Logger log) {
		HallazgosBean.log = log;
	}

	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	public HallazgosDataModel getListDatamodel() {
		cargaListaDataModel();
		return listDatamodel;
	}

	public void setListDatamodel(HallazgosDataModel listDatamodel) {
		this.listDatamodel = listDatamodel;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<SelectItem> getAreasItems() {
		cargarAreasItems();
		return areasItems;
	}

	public void setAreasItems(List<SelectItem> areasItems) {
		this.areasItems = areasItems;
	}

	public List<SelectItem> getEstadoAuditoriaItems() {
		cargarEstadosAuditoria();
		return estadoAuditoriaItems;
	}

	public void setEstadoAuditoriaItems(List<SelectItem> estadoAuditoriaItems) {
		this.estadoAuditoriaItems = estadoAuditoriaItems;
	}

	public Integer getIdEstadoAuditoria() {
		return idEstadoAuditoria;
	}

	public void setIdEstadoAuditoria(Integer idEstadoAuditoria) {
		this.idEstadoAuditoria = idEstadoAuditoria;
	}

	public String getIdTipo() {
		return idTipo;
	}

	public void setIdPeriodo(String idTipo) {
		this.idTipo = idTipo;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public Auditorias getAuditoriaSeleccionada() {
		return auditoriaSeleccionada;
	}

	public void setAuditoriaSeleccionada(Auditorias auditoriaSeleccionada) {
		this.auditoriaSeleccionada = auditoriaSeleccionada;
	}

	public Integer getIdAuditoriaSeleccionada() {
		return idAuditoriaSeleccionada;
	}

	public void setIdAuditoriaSeleccionada(Integer idAuditoriaSeleccionada) {
		this.idAuditoriaSeleccionada = idAuditoriaSeleccionada;
	}

	public Hallazgos getHallazgos() {
		return hallazgos;
	}

	public void setHallazgos(Hallazgos hallazgos) {
		this.hallazgos = hallazgos;
	}

	public void setIdTipo(String idTipo) {
		this.idTipo = idTipo;
	}

	public String getCondicion() {
		return condicion;
	}

	public void setCondicion(String condicion) {
		this.condicion = condicion;
	}

	public String getNorma() {
		return norma;
	}

	public void setNorma(String norma) {
		this.norma = norma;
	}

	public String getCausa() {
		return causa;
	}

	public void setCausa(String causa) {
		this.causa = causa;
	}

	public String getRecomendacion() {
		return recomendacion;
	}

	public void setRecomendacion(String recomendacion) {
		this.recomendacion = recomendacion;
	}

	public Integer getIdArea() {
		return idArea;
	}

	public void setIdArea(Integer idArea) {
		this.idArea = idArea;
	}

	public Hallazgos getHallazgoSeleccionado() {
		return hallazgoSeleccionado;
	}

	public void setHallazgoSeleccionado(Hallazgos hallazgoSeleccionado) {
		this.hallazgoSeleccionado = hallazgoSeleccionado;
	}

	public Integer getIdHallazgoSeleccionado() {
		return idHallazgoSeleccionado;
	}

	public void setIdHallazgoSeleccionado(Integer idHallazgoSeleccionado) {
		this.idHallazgoSeleccionado = idHallazgoSeleccionado;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

}
