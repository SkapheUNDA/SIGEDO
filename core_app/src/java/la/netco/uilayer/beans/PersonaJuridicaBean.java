package la.netco.uilayer.beans;

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
import la.netco.core.spring.security.CommonsActionsKey;
import la.netco.core.spring.security.CommonsModKeys;
import la.netco.core.spring.security.SecuredAction;
import la.netco.core.uilayer.beans.Constants;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.persistencia.dto.commons.Entidad;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean(name = "personaJuridicaBean")
@ViewScoped
public class PersonaJuridicaBean implements Serializable {
	/* Log */
	private static Logger log = LoggerFactory
			.getLogger(PersonaJuridicaBean.class);

	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;

	private EntidadDataModel listDatamodel = null;
	/* Atributos de nivel Visual Filtros */
	private String numIdentificacion = "";
	private String nombres = "";

	/* Control de edicin */
	private Integer idJurSeleccionada;
	private Entidad juridicaSeleccionada;
	private String juridicaBorrar;
	private boolean muestrese = true;
	private Entidad juridica;
	private boolean mostrarModal = false;


	private static final class EntidadDataModel extends
			PrimeDataModel<Entidad, Integer> {
		private static final long serialVersionUID = 1L;

		private EntidadDataModel() {
			super(Entidad.class);
			setOrderBy(Order.asc("etdNom"));
		}

		@Override
		protected Object getId(Entidad t) {
			return t.getClass();
		}
	}

	/**
	 * Busca personas juridicas segun filtros de pantalla
	 */
	public void buscar() {
		List<Criterion> filtros = new ArrayList<Criterion>();
		if (numIdentificacion != null && numIdentificacion.trim().length() > 0) {
			filtros.add(Restrictions.eq("etdIde", numIdentificacion));
		}

		if (nombres != null && nombres.trim().length() > 0) {
			filtros.add(Restrictions.ilike("etdNom", "%" + nombres + "%"));
		}

		filtros.add(Restrictions.isNotNull("etdNom"));
		filtros.add(Restrictions.ne("etdNom", ""));

		Map<String, String> alias = new HashMap<String, String>();
		listDatamodel.setAlias(alias);
		listDatamodel.setFiltros(filtros);
	}

	/**
	 * Carga lista de personas juridicas
	 */
	public void cargaListaDataModel() {
		if (listDatamodel == null) {
			listDatamodel = new EntidadDataModel();
			List<Criterion> filtros = new ArrayList<Criterion>();
			Map<String, String> alias = new HashMap<String, String>();

			filtros.add(Restrictions.isNotNull("etdNom"));
			filtros.add(Restrictions.ne("etdNom", ""));

			listDatamodel.setAlias(alias);
			listDatamodel.setFiltros(filtros);
		}
	}

	/**
	 * Elimina persona juridica
	 */
	public String eliminarJuridica() {
		String page = null;
		try {
			log.info("Ingreso a eliminar Persona"
					+ juridicaSeleccionada.getEtdId());
			if (juridicaSeleccionada != null) {
				serviceDao.getGenericCommonDao().delete(Entidad.class,
						juridicaSeleccionada);
			}
			FacesUtils.addFacesMessageFromBundle(
					Constants.OPERACION_EXITOSA_ELIMINAR_USUARIO,
					FacesMessage.SEVERITY_INFO);
			page = "transaccionExitosa";
			juridicaSeleccionada = new Entidad();

		} catch (Exception exception) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_FATAL);
			log.error(exception.getMessage());

		}
		return page;

	}

	/**
	 * Actualiza Persona Juridica
	 */
	public String actualizarJuridica() {
		String page = null;
		try {
			if (juridicaSeleccionada != null
					&& juridicaSeleccionada.getEtdId() > 0) {
				log.info("Ingreso a actualizar Persona jurídica"
						+ juridicaSeleccionada.getEtdId());
				serviceDao.getGenericCommonDao().saveOrUpdate(Entidad.class,
						juridicaSeleccionada);
				FacesUtils.addFacesMessageFromBundle(
						Constants.OPERACION_EXITOSA_ACTUALIZAR_USUARIO,
						FacesMessage.SEVERITY_INFO);
				juridicaSeleccionada = new Entidad();
				limpiar();
			} else {
				FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
						FacesMessage.SEVERITY_FATAL);
			}
			page = "transaccionExitosa";
		} catch (Exception exception) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_FATAL);
			log.error(exception.getMessage());
		}
		return page;
	}

	/**
	 * Metodo para cargar el la informacin de una persona juridica seleccionada
	 * */
	public void cargarRegistro() {
		try {
			log.info("Cargar datos de persona juridica");
			if (idJurSeleccionada == null) {
				FacesContext facesContext = FacesContext.getCurrentInstance();
				String idRegSeleccionado = facesContext.getExternalContext()
						.getRequestParameterMap().get("idJurSeleccionada");
				if (idRegSeleccionado != null)
					this.idJurSeleccionada = Integer
							.parseInt(idRegSeleccionado);
			}
			if (idJurSeleccionada != null) {
				juridicaSeleccionada = (Entidad) serviceDao
						.getGenericCommonDao().read(Entidad.class,
								idJurSeleccionada);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * inicializa persona juridica a crear
	 */
	public void inicializar() {
		if (juridica != null) {
			juridica = new Entidad();
		}
		limpiar();
	}

	/**
	 * Crear o Actualizar una Persona juridica
	 */
	public String createOrReplace() {
		String page = null;
		try {
			log.info("Ingreso a crear Persona" + juridica.getEtdIde());
			Integer idJuridica = (Integer) serviceDao.getGenericCommonDao()
					.create(Entidad.class, juridica);
			log.info("ID PERSONA" + idJuridica);
			FacesUtils.addFacesMessageFromBundle(
					Constants.OPERACION_EXITOSA_CREAR_USUARIO,
					FacesMessage.SEVERITY_INFO);
			page = "transaccionExitosa";
			juridica = new Entidad();
			/*
			 * mostrarModal = true; mensaje = "Registro Creado";
			 */
			limpiar();

		} catch (Exception exception) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_FATAL);
			log.error(exception.getMessage());

		}
		return page;

	}

	/*
	 * Metodos que retonar la regla de navegacion. Tinene la anotacion
	 * 
	 * @SecuredAction que valida si el usuario registrado tiene permisos sobre
	 * la accion
	 */

	@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod = CommonsModKeys.SE_USU_USU)
	public String actualizarNavigation() {
		log.info("action actualizar juridica");
		return "actualizar";
	}

	@SecuredAction(keyAction = CommonsActionsKey.DELETE, keyMod = CommonsModKeys.SE_USU_USU)
	public String eliminarNavigation() {
		return "eliminar";
	}

	/*
	 * Inicializa Objetos utilizados
	 */
	public void limpiar() {
		juridica = new Entidad();
		juridicaSeleccionada = new Entidad();
	}

	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	public EntidadDataModel getListDatamodel() {
		cargaListaDataModel();
		return listDatamodel;
	}

	public void setListDatamodel(EntidadDataModel listDatamodel) {
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

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getPersonaBorrar() {
		return juridicaBorrar;
	}

	public void setPersonaBorrar(String personaBorrar) {
		this.juridicaBorrar = personaBorrar;
	}

	public Integer getIdJurSeleccionada() {
		return idJurSeleccionada;
	}

	public void setIdJurSeleccionada(Integer idJurSeleccionada) {
		this.idJurSeleccionada = idJurSeleccionada;
	}

	public Entidad getJuridicaSeleccionada() {
		return juridicaSeleccionada;
	}

	public void setjuridicaSeleccionada(Entidad juridicaSeleccionada) {
		this.juridicaSeleccionada = juridicaSeleccionada;
	}

	public boolean isMuestrese() {
		return muestrese;
	}

	public void setMuestrese(boolean muestrese) {
		this.muestrese = muestrese;
	}
	
	public Entidad getJuridica() {
		return juridica;
	}

	public void setJuridica(Entidad juridica) {
		this.juridica = juridica;
	}

	public boolean isMostrarModal() {
		return mostrarModal;
	}

	public void setMostrarModal(boolean mostrarModal) {
		this.mostrarModal = mostrarModal;
	}


	public void nada() {
		// Mtodo usado para hacer doble invocacin
		// para confirmar borrar una entidad antes de
		// hacerlo realmente.
	}

}
