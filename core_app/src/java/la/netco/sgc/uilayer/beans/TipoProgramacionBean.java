package la.netco.sgc.uilayer.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.uilayer.beans.Constants;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.persistencia.dto.commons.Tipospersona;
import la.netco.sgc.persistence.dto.TipoAuditoria;
import la.netco.sgc.persistence.dto.TipoProgramacion;
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

@ManagedBean(name = "tipoProgramacionBean")
@ViewScoped
public class TipoProgramacionBean implements Serializable {

	/* Log */
	private static Logger log = LoggerFactory
			.getLogger(TipoProgramacionBean.class);

	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;
	private TipoProgramacionDataModel listDatamodel = null;

	private TipoProgramacion tipoprogramacion;
	private List<TipoProgramacion> listaTipoProgramacion;

	@ManagedProperty(value = "#{serviceDao}")
	private String codigoTipoProgramacion;
	private String nombreTipoProgramacion;

	/* Control de edicin */
	private Integer idTipoProgramacionSeleccionada;
	private TipoProgramacion tipoProgramacionSeleccionada;
	private TipoProgramacion tipoProgramacionAux;

	public TipoProgramacionBean() {
		tipoprogramacion = new TipoProgramacion();
		tipoProgramacionSeleccionada = new TipoProgramacion();
		tipoProgramacionAux = new TipoProgramacion();
	}

	/**
	 * Busqueda de areas, segn los atributos dados en pantalla
	 */
	public void buscar() {
		log.info("Ingreso a buscar TipoAuditoria");
		List<Criterion> filtros = new ArrayList<Criterion>();
		if (nombreTipoProgramacion != null
				&& nombreTipoProgramacion.trim().length() > 0) {
			filtros.add(Restrictions.ilike("tprNombre", "%"
					+ nombreTipoProgramacion + "%"));
		}

		filtros.add(Restrictions.isNotNull("tprNombre"));
		filtros.add(Restrictions.ne("tprNombre", ""));

		Map<String, String> alias = new HashMap<String, String>();
		listDatamodel.setAlias(alias);
		listDatamodel.setFiltros(filtros);
	}

	/**
	 * Carga listado inicial
	 */
	public void cargaListaDataModel() {
		if (listDatamodel == null) {
			listDatamodel = new TipoProgramacionDataModel();
			List<Criterion> filtros = new ArrayList<Criterion>();
			Map<String, String> alias = new HashMap<String, String>();

			filtros.add(Restrictions.isNotNull("tprNombre"));
			filtros.add(Restrictions.ne("tprNombre", ""));

			listDatamodel.setAlias(alias);
			listDatamodel.setFiltros(filtros);
		}
	}

	public void inicializar() {
		if (tipoprogramacion != null) {
			tipoprogramacion = new TipoProgramacion();
		}
		limpiar();
	}

	/**
	 * Crear o Actualizar programacion
	 */
	public String createOrReplace() {
		String page = null;
		try {
			log.info("Ingreso a crear Tipo de Programacion"
					+ tipoprogramacion.getTprCodigo());
			tipoProgramacionAux =(TipoProgramacion) serviceDao.getGenericCommonDao().read(TipoProgramacion.class, tipoprogramacion.getTprCodigo());
			if(tipoProgramacionAux != null){
				if(tipoProgramacionAux.getTprCodigo() == tipoprogramacion.getTprCodigo()){
					FacesUtils.addFacesMessageFromBundle(
							Constants.TIPO_PROGRAMACION_YA_EXISTE,
							FacesMessage.SEVERITY_FATAL);
					return page;
				}
				
			} else {

			Integer idTipoProgramacion = (Integer) serviceDao
					.getGenericCommonDao().create(TipoProgramacion.class,
							tipoprogramacion);
			log.info("TIPO PROGRAMACION" + idTipoProgramacion);
			FacesUtils.addFacesMessageFromBundle(
					Constants.OPERACION_EXITOSA_CREAR_REGISTRO,
					FacesMessage.SEVERITY_INFO);
			page = "listadoTipoProgramacion";
			tipoprogramacion = new TipoProgramacion();
			/*
			 * mostrarModal = true; mensaje = "Registro Creado";
			 */
			limpiar();
			}

		} catch (Exception exception) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_FATAL);
			log.error(exception.getMessage());

		}
		return page;

	}

	/**
	 * Actualiza Tipo Programacion
	 */
	public String actualizarTipoProgramacion() {
		String page = null;
		try {
			log.info("Ingreso a actualizar Tipo Programacion"
					+ tipoProgramacionSeleccionada.getTprCodigo());
			if (tipoProgramacionSeleccionada != null) {
				serviceDao.getGenericCommonDao().saveOrUpdate(
						TipoProgramacion.class, tipoProgramacionSeleccionada);
			}
			FacesUtils.addFacesMessageFromBundle(
					Constants.OPERACION_EXITOSA_ACTUALIZAR_REGISTRO,
					FacesMessage.SEVERITY_INFO);
			page = "listadoTipoProgramacion";
			tipoProgramacionSeleccionada = new TipoProgramacion();
			limpiar();

		} catch (Exception exception) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_FATAL);
			log.error(exception.getMessage());

		}
		return page;

	}

	/*
	 * Inicializa Objetos utilizados
	 */
	public void limpiar() {
		tipoprogramacion = new TipoProgramacion();
		tipoProgramacionSeleccionada = new TipoProgramacion();
	}

	private static final class TipoProgramacionDataModel extends
			PrimeDataModel<TipoProgramacion, Integer> {
		private static final long serialVersionUID = 1L;

		private TipoProgramacionDataModel() {
			super(TipoProgramacion.class);
			setOrderBy(Order.asc("id"));
		}

		@Override
		protected Object getId(TipoProgramacion t) {
			return t.getClass();
		}
	}

	/**
	 * 
	 * Metodo para cargar el la informaci�n de una persona seleccionada
	 * */
	public void cargarRegistro() {

		try {
			log.info("Cargar datos de persona");
			if (idTipoProgramacionSeleccionada == null) {
				FacesContext facesContext = FacesContext.getCurrentInstance();
				String idRegSeleccionado = facesContext.getExternalContext()
						.getRequestParameterMap()
						.get("idTipoProgramacionSeleccionada");
				if (idRegSeleccionado != null)
					this.idTipoProgramacionSeleccionada = Integer
							.parseInt(idRegSeleccionado);
			}

			if (idTipoProgramacionSeleccionada != null) {
				tipoProgramacionSeleccionada = (TipoProgramacion) serviceDao
						.getGenericCommonDao().read(TipoProgramacion.class,
								idTipoProgramacionSeleccionada);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Elimina tipo de programacion
	 */
	public String eliminarTipoProgramacion() {
		String page = null;
		try {
			log.info("Ingreso a eliminar tipo programacion"
					+ tipoProgramacionSeleccionada.getTprCodigo());
			if (tipoProgramacionSeleccionada != null) {
				serviceDao.getGenericCommonDao().delete(TipoProgramacion.class,
						tipoProgramacionSeleccionada);
			}
			FacesUtils.addFacesMessageFromBundle(
					Constants.OPERACION_EXITOSA_ELIMINAR_REGISTRO,
					FacesMessage.SEVERITY_INFO);
			page = "listadoTipoProgramacion";
			tipoProgramacionSeleccionada = new TipoProgramacion();

		} catch (Exception exception) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_FATAL);
			log.error(exception.getMessage());

		}
		return page;

	}

	public String actualizarNavigation() {
		return "actualizarTipoProgramacion";
	}

	public String eliminarNavigation() {
		return "eliminarTipoProgramacion";
	}

	public String crearNavigation() {
		return "crearTipoProgramacion";
	}

	public String navListado() {
		return "listadoTipoProgramacion";
	}

	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	public TipoProgramacionDataModel getListDatamodel() {
		cargaListaDataModel();
		return listDatamodel;
	}

	public void setListDatamodel(TipoProgramacionDataModel listDatamodel) {
		this.listDatamodel = listDatamodel;
	}

	public TipoProgramacion getTipoprogramacion() {
		return tipoprogramacion;
	}

	public void setTipoprogramacion(TipoProgramacion tipoprogramacion) {
		this.tipoprogramacion = tipoprogramacion;
	}

	public List<TipoProgramacion> getListaTipoProgramacion() {
		return listaTipoProgramacion;
	}

	public void setListaTipoProgramacion(
			List<TipoProgramacion> listaTipoProgramacion) {
		this.listaTipoProgramacion = listaTipoProgramacion;
	}

	public String getCodigoTipoProgramacion() {
		return codigoTipoProgramacion;
	}

	public void setCodigoTipoProgramacion(String codigoTipoProgramacion) {
		this.codigoTipoProgramacion = codigoTipoProgramacion;
	}

	public String getNombreTipoProgramacion() {
		return nombreTipoProgramacion;
	}

	public void setNombreTipoProgramacion(String nombreTipoProgramacion) {
		this.nombreTipoProgramacion = nombreTipoProgramacion;
	}

	public Integer getIdTipoProgramacionSeleccionada() {
		return idTipoProgramacionSeleccionada;
	}

	public void setIdTipoProgramacionSeleccionada(
			Integer idTipoProgramacionSeleccionada) {
		this.idTipoProgramacionSeleccionada = idTipoProgramacionSeleccionada;
	}

	public TipoProgramacion getTipoProgramacionSeleccionada() {
		return tipoProgramacionSeleccionada;
	}

	public void setTipoProgramacionSeleccionada(
			TipoProgramacion tipoProgramacionSeleccionada) {
		this.tipoProgramacionSeleccionada = tipoProgramacionSeleccionada;
	}

}
