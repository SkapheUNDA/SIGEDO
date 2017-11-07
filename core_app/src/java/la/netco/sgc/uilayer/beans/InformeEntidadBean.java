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
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.spring.security.CommonsActionsKey;
import la.netco.core.spring.security.CommonsModKeys;
import la.netco.core.spring.security.SecuredAction;
import la.netco.core.uilayer.beans.Constants;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.sgc.persistence.dto.Entidades;
import la.netco.sgc.persistence.dto.Formatos;
import la.netco.sgc.persistence.dto.InformeEntidad;
import la.netco.uilayer.beans.PrimeDataModel;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author ediaz
 *
 */
@ManagedBean(name = "informeEntidadBean")
@ViewScoped
public class InformeEntidadBean implements Serializable {

	/* Log */
	private static Logger log = LoggerFactory
			.getLogger(InformeEntidadBean.class);

	/* Atributos de nivel Visual Filtros */
	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;
	private InformeEntidaDataModel listDatamodel = null;
	InformeEntidad informeEntidad;
	InformeEntidad informeEntidadAux;
	
	private Integer idInformeSeleccionado;

	/* Combos en pantalla */
	private List<SelectItem> tiposSociedadesItems;
	private Integer idSociedad;
	private List<SelectItem> tiposInformes;
	private Integer idInforme;

	public InformeEntidadBean() {

		informeEntidad = new InformeEntidad();
		informeEntidadAux = new InformeEntidad();
	}

	/**
	 * Busqueda Informe Sociedad
	 */
	public void buscar() {
		log.info("Ingreso a buscar Informe Sociedades");
		List<Criterion> filtros = new ArrayList<Criterion>();

		if (idSociedad != null && idSociedad != 0) {
			filtros.add(Restrictions.eq("entidad.entCodigo", idSociedad));

		}
		if (idInforme != null && idSociedad != 0) {
			filtros.add(Restrictions.eq("formato.forCodigo", idInforme));
		}

		Map<String, String> alias = new HashMap<String, String>();
		listDatamodel.setAlias(alias);
		listDatamodel.setFiltros(filtros);
	}
	
	public void validarObligatoriedad(ValueChangeEvent event){
			if (event.getNewValue().equals(Boolean.TRUE)){
				informeEntidad.setOpcional(Boolean.FALSE);
				informeEntidad.setObligatorio(Boolean.TRUE);
			}else{
				informeEntidad.setOpcional(Boolean.TRUE);
				informeEntidad.setObligatorio(Boolean.FALSE);
			}
		
	}
	
	public void validarOpcional(ValueChangeEvent event){
		if (event.getNewValue().equals(Boolean.TRUE)){
				informeEntidad.setObligatorio(Boolean.FALSE);
				informeEntidad.setOpcional(Boolean.TRUE);
			}else{				
				informeEntidad.setObligatorio(Boolean.TRUE);
				informeEntidad.setOpcional(Boolean.FALSE);
			}		
	}

	/**
	 * Cargar Sociedades
	 */
	@SuppressWarnings("unchecked")
	public void cargarSociedadesItems() {

		if (tiposSociedadesItems == null) {
			/* Obtener List de sociedades */
			List<Entidades> listSociedades = (List<Entidades>) serviceDao
					.getGenericCommonDao().executeFind(
							Entidades.NAMED_QUERY_ALL);

			tiposSociedadesItems = new ArrayList<SelectItem>();

			for (Entidades sociedades : listSociedades) {
				tiposSociedadesItems.add(new SelectItem(sociedades
						.getEntCodigo(), sociedades.getEntObjetoSocial()));
			}

		} else if (tiposSociedadesItems == null) {
			tiposSociedadesItems = new ArrayList<SelectItem>();
		}
	}

	/**
	 * Cargar Informes
	 */
	@SuppressWarnings("unchecked")
	public void cargarInformestems() {

		if (tiposInformes == null) {
			/* Obtener List de Formatos */
			List<Formatos> listFormatos = (List<Formatos>) serviceDao
					.getGenericCommonDao()
					.executeFind(Formatos.NAMED_QUERY_ALL);

			tiposInformes = new ArrayList<SelectItem>();

			for (Formatos formatos : listFormatos) {
				tiposInformes.add(new SelectItem(formatos.getForCodigo(),
						formatos.getForDescripcion()));
			}

		} else if (tiposInformes == null) {
			tiposInformes = new ArrayList<SelectItem>();
		}
	}

	/**
	 * Carga listado inicial
	 */
	public void cargaListaDataModel() {
		if (listDatamodel == null) {
			listDatamodel = new InformeEntidaDataModel();
			List<Criterion> filtros = new ArrayList<Criterion>();
			Map<String, String> alias = new HashMap<String, String>();

			if (idSociedad != null && idSociedad != 0) {
				filtros.add(Restrictions.eq("entidad.entCodigo", idSociedad));

			}
			if (idInforme != null && idSociedad != 0) {
				filtros.add(Restrictions.eq("formato.forCodigo", idInforme));
			}

			listDatamodel.setAlias(alias);
			listDatamodel.setFiltros(filtros);
		}
	}

	/**
	 * Crear Informe Entidad
	 */
	public String createOrReplace() {
		String page = null;
		try {
			log.info("Ingreso a crear informeEntidad"
					+ informeEntidad.getObligatorio());

			if (idInforme != null) {
				Formatos formatos = (Formatos) serviceDao.getGenericCommonDao()
						.read(Formatos.class, idInforme);
				informeEntidad.setFormato(formatos);
			}
			if (idSociedad != null) {
				Entidades entidades = (Entidades) serviceDao
						.getGenericCommonDao()
						.read(Entidades.class, idSociedad);
				informeEntidad.setEntidad(entidades);
			}

			// Valida no duplicado
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, informeEntidad.getEntidad().getEntCodigo());
			params.put(1, informeEntidad.getFormato().getForCodigo());
			informeEntidadAux = new InformeEntidad();
			informeEntidadAux = (InformeEntidad) serviceDao
					.getGenericCommonDao()
					.executeUniqueResult(
							InformeEntidad.NAMED_QUERY_INFORME_ENTIDAD_BY_ENTIDAD_AND_FORMATO,
							params);

			if (informeEntidadAux != null) {
				FacesUtils.addFacesMessageFromBundle(
						Constants.INFORME_ENTIDAD_EXISTE,
						FacesMessage.SEVERITY_FATAL);

				return page;

			} else {
				serviceDao.getGenericCommonDao().create(InformeEntidad.class,
						informeEntidad);
				FacesUtils.addFacesMessageFromBundle(
						Constants.OPERACION_EXITOSA_CREAR_REGISTRO,
						FacesMessage.SEVERITY_INFO);
				page = "transaccionExitosaSociedades";
				informeEntidad = new InformeEntidad();
			}

		} catch (Exception exception) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_FATAL);
			log.error(exception.getMessage());

		}
		return page;

	}

	/**
	 * Metodo utilizado para cargar los datos del registro a modificar
	 * 
	 */
	public void cargarDatosModificar() {
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		try {
			if(idInformeSeleccionado == null){
			     String idInforme = ctx.getExternalContext().getRequestParameterMap().get("idInforme");			   
			     if(idInforme != null) this.idInformeSeleccionado = Integer.parseInt(idInforme);
			}
				
			if (informeEntidad==null || informeEntidad.getId()==null){
				//Informe Entidad
				Map<Integer, Object> params1 = new HashMap<Integer, Object>();
				params1.put(0,idInformeSeleccionado );
				informeEntidad = (InformeEntidad) serviceDao
						.getGenericCommonDao()
						.executeUniqueResult(
								InformeEntidad.NAMED_QUERY_INFORME_ENTIDAD_BY_ID,
								params1);
				//informeEntidad = (InformeEntidad) serviceDao.getGenericCommonDao().read(InformeEntidad.class, idInformeSeleccionado);
				if(informeEntidad != null){
					idSociedad = informeEntidad.getEntidad().getEntCodigo();
					idInforme = informeEntidad.getFormato().getForCodigo();
				}
				
			}			
		
		} catch (Exception e) {
		
			ctx.addMessage(null, new FacesMessage("Se ha presentado un error. Detalles: " + e.getMessage()));
		}
		
	}
	/**
	 * Actualizar entidad Informe
	 * @return
	 */
	
	public String actualizarInformeEntidad() {
		String page = null;
		try {
			log.info("Ingreso a actualizar informeEntidad"
					+ informeEntidad.getObligatorio());

			if (idInforme != null) {
				Formatos formatos = (Formatos) serviceDao.getGenericCommonDao()
						.read(Formatos.class, idInforme);
				informeEntidad.setFormato(formatos);
			}
			if (idSociedad != null) {
				Entidades entidades = (Entidades) serviceDao
						.getGenericCommonDao()
						.read(Entidades.class, idSociedad);
				informeEntidad.setEntidad(entidades);
			}

			// Valida no duplicado
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, informeEntidad.getEntidad().getEntCodigo());
			params.put(1, informeEntidad.getFormato().getForCodigo());
			informeEntidadAux = new InformeEntidad();
			informeEntidadAux = (InformeEntidad) serviceDao
					.getGenericCommonDao()
					.executeUniqueResult(
							InformeEntidad.NAMED_QUERY_INFORME_ENTIDAD_BY_ENTIDAD_AND_FORMATO,
							params);

			if (informeEntidadAux != null) {
				FacesUtils.addFacesMessageFromBundle(
						Constants.INFORME_ENTIDAD_EXISTE,
						FacesMessage.SEVERITY_FATAL);

				return page;

			} else {
				serviceDao.getGenericCommonDao().update(InformeEntidad.class,
						informeEntidad);
				FacesUtils.addFacesMessageFromBundle(
						Constants.OPERACION_EXITOSA_ACTUALIZAR_REGISTRO,
						FacesMessage.SEVERITY_INFO);
				page = "transaccionExitosaSociedades";
				informeEntidad = new InformeEntidad();
			}

		} catch (Exception exception) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_FATAL);
			log.error(exception.getMessage());

		}
		return page;

	}
	
	/*
	 * Metodos que retonar la regla de navegacion. Tinene la anotacion
	 * @SecuredAction que valida si el usuario registrado tiene permisos sobre
	 * la accion
	 */

	@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod = CommonsModKeys.SE_USU_USU)
	public String agregarNavigation() {
		return "agregarInformeSociedad";
	}

	@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod = CommonsModKeys.SE_USU_USU)
	public String editarNavigation() {
		return "actualizarInformeSociedad";
	}

	public String navlistado() {
		return "listadoInformesSociedades";
	}

	public static final class InformeEntidaDataModel extends
			PrimeDataModel<InformeEntidad, Integer> {
		private static final long serialVersionUID = 1L;

		private InformeEntidaDataModel() {
			super(InformeEntidad.class);
			setOrderBy(Order.asc("id"));
		}

		@Override
		protected Object getId(InformeEntidad t) {
			return t.getClass();
		}
	}

	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	public List<SelectItem> getTiposSociedadesItems() {
		cargarSociedadesItems();
		return tiposSociedadesItems;
	}

	public void setTiposSociedadesItems(List<SelectItem> tiposSociedadesItems) {
		this.tiposSociedadesItems = tiposSociedadesItems;
	}

	public Integer getIdSociedad() {
		return idSociedad;
	}

	public void setIdSociedad(Integer idSociedad) {
		this.idSociedad = idSociedad;
	}

	public List<SelectItem> getTiposInformes() {
		cargarInformestems();
		return tiposInformes;
	}

	public void setTiposInformes(List<SelectItem> tiposInformes) {
		this.tiposInformes = tiposInformes;
	}

	public Integer getIdInforme() {
		return idInforme;
	}

	public void setIdInforme(Integer idInforme) {
		this.idInforme = idInforme;
	}

	public InformeEntidaDataModel getListDatamodel() {
		cargaListaDataModel();
		return listDatamodel;
	}

	public void setListDatamodel(InformeEntidaDataModel listDatamodel) {
		this.listDatamodel = listDatamodel;
	}

	public InformeEntidad getInformeEntidad() {
		return informeEntidad;
	}

	public void setInformeEntidad(InformeEntidad informeEntidad) {
		this.informeEntidad = informeEntidad;
	}

	public Integer getIdInformeSeleccionado() {
		return idInformeSeleccionado;
	}

	public void setIdInformeSeleccionado(Integer idInformeSeleccionado) {
		this.idInformeSeleccionado = idInformeSeleccionado;
	}
 
}
