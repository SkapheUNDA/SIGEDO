package la.netco.sgc.uilayer.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.spring.security.CommonsActionsKey;
import la.netco.core.spring.security.CommonsModKeys;
import la.netco.core.spring.security.SecuredAction;
import la.netco.core.uilayer.beans.Constants;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.persistencia.dto.commons.Entidad;
import la.netco.sgc.persistence.dto.Area;
import la.netco.sgc.persistence.dto.TipoAuditoria;
import la.netco.uilayer.beans.PrimeDataModel;

import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;

import la.netco.core.spring.BeansSpringConst;

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

@ManagedBean(name = "tipoAuditoriaBean")
@ViewScoped
public class TipoAuditoriaBean implements Serializable {

	/* Log */
	private static Logger log = LoggerFactory
			.getLogger(TipoAuditoriaBean.class);

	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;
	private TipoAuditoriaDataModel listDatamodel = null;

	/* Atributos de nivel Visual Filtros */
	private String nombreTipoAuditoria;

	/* Control de edicin */
	private Integer idTipoAuditoriaSeleccionada;
	private TipoAuditoria tipoauditoria;
	private TipoAuditoria tipoAuditoriaSeleccionada;
	private TipoAuditoria tipoAuditoriaAux;

	public TipoAuditoriaBean() {
		tipoauditoria = new TipoAuditoria();
		tipoAuditoriaSeleccionada = new TipoAuditoria();
		tipoAuditoriaAux = new TipoAuditoria();
	}

	/**
	 * Busqueda de areas, segn los atributos dados en pantalla
	 */
	public void buscar() {
		log.info("Ingreso a buscar TipoAuditoria");
		List<Criterion> filtros = new ArrayList<Criterion>();
		if (nombreTipoAuditoria != null
				&& nombreTipoAuditoria.trim().length() > 0) {
			filtros.add(Restrictions.ilike("tauNombre", "%"
					+ nombreTipoAuditoria + "%"));
		}

		filtros.add(Restrictions.isNotNull("tauNombre"));
		filtros.add(Restrictions.ne("tauNombre", ""));

		Map<String, String> alias = new HashMap<String, String>();
		listDatamodel.setAlias(alias);
		listDatamodel.setFiltros(filtros);
	}

	/**
	 * Carga listado inicial
	 */
	public void cargaListaDataModel() {
		if (listDatamodel == null) {
			listDatamodel = new TipoAuditoriaDataModel();
			List<Criterion> filtros = new ArrayList<Criterion>();
			Map<String, String> alias = new HashMap<String, String>();

			filtros.add(Restrictions.isNotNull("tauNombre"));
			filtros.add(Restrictions.ne("tauNombre", ""));

			listDatamodel.setAlias(alias);
			listDatamodel.setFiltros(filtros);
		}
	}

	public void inicializar() {
		if (tipoauditoria != null) {
			tipoauditoria = new TipoAuditoria();
		}
		limpiar();
	}

	/**
	 * Crear o Actualizar auditoria
	 */
	public String createOrReplace() {
		String page = null;
		try {
			log.info("Ingreso a crear Tipo de Auditoria"
					+ tipoauditoria.getTauCodigo());
			tipoAuditoriaAux = (TipoAuditoria)serviceDao.getGenericCommonDao().read(TipoAuditoria.class, tipoauditoria.getTauCodigo());
			if(tipoAuditoriaAux != null){
				if(tipoAuditoriaAux.getTauCodigo() == tipoauditoria.getTauCodigo()){
					FacesUtils.addFacesMessageFromBundle(
							Constants.TIPO_AUDITORIA_YA_EXISTE,
							FacesMessage.SEVERITY_FATAL);
					return page;
					
				}
			} else {
				// serviceDao.getGenericCommonDao().saveOrUpdate(Persona.class,
				// persona);
				Integer idTipoAuditoria = (Integer) serviceDao
						.getGenericCommonDao().create(TipoAuditoria.class,
								tipoauditoria);
				log.info("TIPO AUDITORIA" + idTipoAuditoria);
				FacesUtils.addFacesMessageFromBundle(
						Constants.OPERACION_EXITOSA_CREAR_REGISTRO,
						FacesMessage.SEVERITY_INFO);
				page = "listadoTipoAuditoria";
				tipoauditoria = new TipoAuditoria();
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
	 * Actualiza Tipo Auditoria
	 */
	public String actualizarTipoAuditoria() {
		String page = null;
		try {
			log.info("Ingreso a actualizar Tipo Auditoria"
					+ tipoAuditoriaSeleccionada.getTauCodigo());
			if (tipoAuditoriaSeleccionada != null) {
				serviceDao.getGenericCommonDao().saveOrUpdate(
						TipoAuditoria.class, tipoAuditoriaSeleccionada);
			}
			FacesUtils.addFacesMessageFromBundle(
					Constants.OPERACION_EXITOSA_ACTUALIZAR_REGISTRO,
					FacesMessage.SEVERITY_INFO);
			page = "listadoTipoAuditoria";
			tipoAuditoriaSeleccionada = new TipoAuditoria();
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
		tipoauditoria = new TipoAuditoria();
		tipoAuditoriaSeleccionada = new TipoAuditoria();
	}

	private static final class TipoAuditoriaDataModel extends
			PrimeDataModel<TipoAuditoria, Integer> {
		private static final long serialVersionUID = 1L;

		private TipoAuditoriaDataModel() {
			super(TipoAuditoria.class);
			setOrderBy(Order.asc("tauCodigo"));
		}

		@Override
		protected Object getId(TipoAuditoria t) {
			return t.getClass();
		}
	}

	/**
	 * 
	 * Metodo para cargar el la informacin de un tipo de auditoria
	 * */
	public void cargarRegistro() {

		try {
			log.info("Cargar datos tipo de auditoria");
			if (idTipoAuditoriaSeleccionada == null) {
				FacesContext facesContext = FacesContext.getCurrentInstance();
				String idRegSeleccionado = facesContext.getExternalContext()
						.getRequestParameterMap()
						.get("idTipoAuditoriaSeleccionada");
				if (idRegSeleccionado != null)
					this.idTipoAuditoriaSeleccionada = Integer
							.parseInt(idRegSeleccionado);
			}
		
			if (idTipoAuditoriaSeleccionada != null) {
				tipoAuditoriaSeleccionada = (TipoAuditoria) serviceDao
						.getGenericCommonDao().read(TipoAuditoria.class,
								idTipoAuditoriaSeleccionada);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Elimina tipo de auditoria
	 */
	public String eliminarTipoAuditoria() {
		String page = null;
		try {
			log.info("Ingreso a eliminar Persona"
					+ tipoAuditoriaSeleccionada.getTauCodigo());
			if (tipoAuditoriaSeleccionada != null) {
				serviceDao.getGenericCommonDao().delete(TipoAuditoria.class,
						tipoAuditoriaSeleccionada);
			}
			FacesUtils.addFacesMessageFromBundle(
					Constants.OPERACION_EXITOSA_ELIMINAR_REGISTRO,
					FacesMessage.SEVERITY_INFO);
			page = "listadoTipoAuditoria";
			tipoAuditoriaSeleccionada = new TipoAuditoria();

		} catch (Exception exception) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_FATAL);
			log.error(exception.getMessage());

		}
		return page;

	}

	public String actualizarNavigation() {
		return "actualizarTipoAuditoria";
	}

	public String eliminarNavigation() {
		return "eliminarTipoAuditoria";
	}
	
	public String crearNavigation() {
		return "crearTipoAuditoria";
	}
	
	public String navListado() {
		return "listadoTipoAuditoria";
	}


	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	public TipoAuditoriaDataModel getListDatamodel() {
		cargaListaDataModel();
		return listDatamodel;
	}

	public void setListDatamodel(TipoAuditoriaDataModel listDatamodel) {
		this.listDatamodel = listDatamodel;
	}

	public TipoAuditoria getTipoauditoria() {
		return tipoauditoria;
	}

	public void setTipoauditoria(TipoAuditoria tipoauditoria) {
		this.tipoauditoria = tipoauditoria;
	}

	public String getNombreTipoAuditoria() {
		return nombreTipoAuditoria;
	}

	public void setNombreTipoAuditoria(String nombreTipoAuditoria) {
		this.nombreTipoAuditoria = nombreTipoAuditoria;
	}

	public Integer getIdTipoAuditoriaSeleccionada() {
		return idTipoAuditoriaSeleccionada;
	}

	public void setIdTipoAuditoriaSeleccionada(
			Integer idTipoAuditoriaSeleccionada) {
		this.idTipoAuditoriaSeleccionada = idTipoAuditoriaSeleccionada;
	}

	public TipoAuditoria getTipoAuditoriaSeleccionada() {
		return tipoAuditoriaSeleccionada;
	}

	public void setTipoAuditoriaSeleccionada(
			TipoAuditoria tipoAuditoriaSeleccionada) {
		this.tipoAuditoriaSeleccionada = tipoAuditoriaSeleccionada;
	}

}
