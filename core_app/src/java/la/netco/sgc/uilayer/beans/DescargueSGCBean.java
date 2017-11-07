package la.netco.sgc.uilayer.beans;

import java.io.InputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import la.netco.commons.services.FileSystemRepositoryService;
import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.uilayer.beans.Constants;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.core.uilayer.commons.GenericDataModel;
import la.netco.sgc.persistence.dto.ArchivoSGC;
import la.netco.sgc.persistence.dto.Entidades;
import la.netco.sgc.persistence.dto.Formatos;
import la.netco.sgc.persistence.dto.PUC;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 * @author cpineros
 *
 */
/**
 * @author egiraldo
 *
 */
@ManagedBean(name = "descargueSGCBean")
@ViewScoped
public class DescargueSGCBean implements Serializable {

	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao daoServicio;

	@ManagedProperty(value = BeansSpringConst.BEAN_ID_FILE_SYSTEM_REPO_SERVICE)
	transient private FileSystemRepositoryService repoService;

	private Integer idFormatoSeleccionado;
	private Integer idEntidadSeleccionada;
	private String anoSeleccionado;
	private String periodo;

	private List<SelectItem> entidadesItems;
	private List<SelectItem> formatosItems;
	private List<SelectItem> anosItems;

	private ArchivoSGCDataModel archivoSGCDataModel;

	private List<ArchivoSGC> listaArchivosSGC;

	private static final long serialVersionUID = 1L;
	
	/**
     * Obtener Archivos por codigo de formato 
     */
    
    public static final String QUERY_BASE = "FROM ArchivoSGC archivoSGC left join fetch archivoSGC.entidades entidad left join fetch archivoSGC.formatos formato ";
    
    
    public static final String QUERY_WHERE = "WHERE ";
    
    
    public static final String QUERY_AND = "AND ";
  	
    /**
     * Obtener archivos por codigo de entidad
     */
    
    public static final String QUERY_GET_BY_ENTIDAD = "entidad.entCodigo =:codigoEntidad ";
    
    
    public static final String QUERY_GET_BY_FORMATO = "formato.forCodigo =:codigoFormato ";
    
         
    /**
     * Obtener fechas  que se encuentren en archivoSGC por orden ascendente
     */
    
    public static final String QUERY_GET_DISTINCT_ANOS = "archivoSGC.ano=:ano ";
    
    
    public static final String QUERY_GET_PERIODO = "archivoSGC.periodo=:periodo ";
	
	@ManagedProperty(value = "#{serviceDao}")
	transient private ServiceDao serviceDao;

	public DescargueSGCBean() {
		System.out.println("Construccion DescargueSGC");
		archivoSGCDataModel = new ArchivoSGCDataModel();
	}

	@PostConstruct
	public void initDescargueSGCBean() {
		System.out.println("Post-Construccion DescargueSGC");
		cargaAnosItems();
		cargaEntidadesItems();
		cargaFormatosItems();
		cargaListaArchivosSGC();
	}

	public void cargaFiltrosDM() {
		try {			
			int i=0;
			StringBuilder query = new StringBuilder();			
			query.append(QUERY_BASE);
			
			if (idEntidadSeleccionada != null && idEntidadSeleccionada != 0) {
				query.append(QUERY_WHERE);
				query.append(QUERY_GET_BY_ENTIDAD);
				i++;
			}
			if (idFormatoSeleccionado != null && idFormatoSeleccionado != 0) {
				if (i==0){
					query.append(QUERY_WHERE);	
				}else{
					query.append(QUERY_AND);	
				}
				query.append(QUERY_GET_BY_FORMATO);					
				i++;
			}
			if (anoSeleccionado != null && !anoSeleccionado.equals("")) {
				if (i==0){
					query.append(QUERY_WHERE);	
				}else{
					query.append(QUERY_AND);	
				}
				query.append(QUERY_GET_DISTINCT_ANOS);				
				i++;
			}
			if (periodo != null && !periodo.equals("")) {
				if (i==0){
					query.append(QUERY_WHERE);	
				}else{
					query.append(QUERY_AND);	
				}
				query.append(QUERY_GET_PERIODO);						
			}
			
			listaArchivosSGC = serviceDao
					.getArchivosSGCDao().obtenerArchivos(idEntidadSeleccionada, idFormatoSeleccionado, periodo, anoSeleccionado, query.toString());						
						
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Cargar lista de entidades
	 */
	@SuppressWarnings("unchecked")
	public void cargaEntidadesItems() {

		if (entidadesItems == null) {
			List<Entidades> entidadesAuxi = (List<Entidades>) daoServicio
					.getGenericCommonDao().loadAll(Entidades.class);

			entidadesItems = new ArrayList<SelectItem>();

			for (Entidades entidades : entidadesAuxi) {
				entidadesItems.add(new SelectItem(entidades.getEntCodigo(),
						entidades.getEntObjetoSocial()));
			}
		}

	}

	/**
	 * Cargar lista de todos los archivos cargados
	 */
	@SuppressWarnings("unchecked")
	public void cargaListaArchivosSGC() {

		if (listaArchivosSGC == null) {
			listaArchivosSGC = new ArrayList<ArchivoSGC>();
			listaArchivosSGC = (List<ArchivoSGC>) daoServicio
					.getGenericCommonDao().loadAll(ArchivoSGC.class);
		}

	}

	/**
	 * Cargar lista de formatos
	 */
	@SuppressWarnings("unchecked")
	public void cargaFormatosItems() {

		if (formatosItems == null) {
			List<Formatos> formatosAuxi = (List<Formatos>) daoServicio
					.getGenericCommonDao().loadAll(Formatos.class);

			formatosItems = new ArrayList<SelectItem>();

			for (Formatos formatos : formatosAuxi) {
				formatosItems.add(new SelectItem(formatos.getForCodigo(),
						formatos.getForNombre()));
			}
		}

	}

	/**
	 * Cargar lista de Años
	 */
	public void cargaAnosItems() {

		if (anosItems == null) {
			List<?> archivosgcAuxi = daoServicio
					.getGenericCommonDao().executeFind(
							ArchivoSGC.NAMED_QUERY_GET_DISTINCT_ANOS);

			anosItems = new ArrayList<SelectItem>();

			for (Object object : archivosgcAuxi) {
				anosItems.add(new SelectItem(object.toString(), object
						.toString()));
			}
		}

	}

	public StreamedContent descargar(Integer idArchivo) {
		try {
			ArchivoSGC archivo = (ArchivoSGC) daoServicio.getGenericCommonDao()
					.read(ArchivoSGC.class, idArchivo);
			InputStream stream = repoService.obtenerInputStream(archivo);

			StreamedContent file = new DefaultStreamedContent(stream,
					archivo.getContentType(), archivo.getNombre());

			return file;
		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @return the repoService
	 */
	public FileSystemRepositoryService getRepoService() {
		return repoService;
	}

	/**
	 * @param repoService
	 *            the repoService to set
	 */
	public void setRepoService(FileSystemRepositoryService repoService) {
		this.repoService = repoService;
	}

	/**
	 * @return the idFormatoSeleccionado
	 */
	public Integer getIdFormatoSeleccionado() {
		return idFormatoSeleccionado;
	}

	/**
	 * @param idFormatoSeleccionado
	 *            the idFormatoSeleccionado to set
	 */
	public void setIdFormatoSeleccionado(Integer idFormatoSeleccionado) {
		this.idFormatoSeleccionado = idFormatoSeleccionado;
	}

	/**
	 * @return the idEntidadSeleccionada
	 */
	public Integer getIdEntidadSeleccionada() {
		return idEntidadSeleccionada;
	}

	/**
	 * @param idEntidadSeleccionada
	 *            the idEntidadSeleccionada to set
	 */
	public void setIdEntidadSeleccionada(Integer idEntidadSeleccionada) {
		this.idEntidadSeleccionada = idEntidadSeleccionada;
	}

	/**
	 * @return the daoServicio
	 */
	public ServiceDao getDaoServicio() {
		return daoServicio;
	}

	/**
	 * @param daoServicio
	 *            the daoServicio to set
	 */
	public void setDaoServicio(ServiceDao daoServicio) {
		this.daoServicio = daoServicio;
	}

	/**
	 * @return the entidadesItems
	 */
	public List<SelectItem> getEntidadesItems() {
		return entidadesItems;
	}

	/**
	 * @param entidadesItems
	 *            the entidadesItems to set
	 */
	public void setEntidadesItems(List<SelectItem> entidadesItems) {
		this.entidadesItems = entidadesItems;
	}

	/**
	 * @return the formatosItems
	 */
	public List<SelectItem> getFormatosItems() {
		return formatosItems;
	}

	/**
	 * @param formatosItems
	 *            the formatosItems to set
	 */
	public void setFormatosItems(List<SelectItem> formatosItems) {
		this.formatosItems = formatosItems;
	}

	/**
	 * @return the anosItems
	 */
	public List<SelectItem> getAnosItems() {
		return anosItems;
	}

	/**
	 * @param anosItems
	 *            the anosItems to set
	 */
	public void setAnosItems(List<SelectItem> anosItems) {
		this.anosItems = anosItems;
	}
	
	

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	/**
	 * @return the anoSeleccionado
	 */
	public String getAnoSeleccionado() {
		return anoSeleccionado;
	}

	/**
	 * @param anoSeleccionado
	 *            the anoSeleccionado to set
	 */
	public void setAnoSeleccionado(String anoSeleccionado) {
		this.anoSeleccionado = anoSeleccionado;
	}

	/**
	 * @return the listaArchivosSGC
	 */
	public List<ArchivoSGC> getListaArchivosSGC() {
		return listaArchivosSGC;
	}
	
	

	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	/**
	 * @param listaArchivosSGC
	 *            the listaArchivosSGC to set
	 */
	public void setListaArchivosSGC(List<ArchivoSGC> listaArchivosSGC) {
		this.listaArchivosSGC = listaArchivosSGC;
	}

	/**
	 * @return the archivoSGCDataModel
	 */
	public ArchivoSGCDataModel getArchivoSGCDataModel() {
		return archivoSGCDataModel;
	}

	/**
	 * @param archivoSGCDataModel
	 *            the archivoSGCDataModel to set
	 */
	public void setArchivoSGCDataModel(ArchivoSGCDataModel archivoSGCDataModel) {
		this.archivoSGCDataModel = archivoSGCDataModel;
	}

	private static final class ArchivoSGCDataModel extends
			GenericDataModel<ArchivoSGC, Integer> {
		private static final long serialVersionUID = 1L;

		private ArchivoSGCDataModel() {
			super(ArchivoSGC.class);
			setOrderBy(Order.asc("nombre"));
		}

		@Override
		protected Object getId(ArchivoSGC t) {
			return t.getId();
		}
	}

}
