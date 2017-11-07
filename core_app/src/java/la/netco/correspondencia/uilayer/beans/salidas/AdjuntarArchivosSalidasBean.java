package la.netco.correspondencia.uilayer.beans.salidas;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import la.netco.commons.services.FileSystemRepositoryService;
import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.uilayer.beans.Constants;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.persistencia.dto.commons.ArchivoSalida;
import la.netco.persistencia.dto.commons.Salida;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

@ManagedBean
@ViewScoped
public class AdjuntarArchivosSalidasBean implements Serializable{
	

	private static final long serialVersionUID = 1L;
	private Integer idRegSeleccionado;

	private Salida salidaSeleccionada;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_FILE_SYSTEM_REPO_SERVICE)
	transient private FileSystemRepositoryService repoService;
	
	private List<ArchivoSalida> archivossalida = null;
	
	private String OPERACION_EXITOSA_GUARDAR_ARCHIVO = "corresArchivoGuardadoCorrectamente";
	private String ERROR_ARCHIVO_REPETIDO = "corresErrorArchivoRepetido";
	
	@SuppressWarnings("unchecked")
	public void uploadFile(FileUploadEvent event)  {
		try {

			UploadedFile file = event.getFile();
			System.out.println(" guarda archivo");
			
			if(archivossalida != null && !archivossalida.isEmpty()){
				for (ArchivoSalida archivo : archivossalida) {
					if(archivo.getNombre().equalsIgnoreCase(file.getFileName())){
						FacesUtils.addFacesMessageFromBundle(ERROR_ARCHIVO_REPETIDO,FacesMessage.SEVERITY_ERROR);
						return;
					}
				}
			}
			
			ArchivoSalida archivoSalida = new ArchivoSalida();
			archivoSalida.setSalida(salidaSeleccionada);
			archivoSalida.setNombre(file.getFileName());
			archivoSalida.setContentType(file.getContentType());
			archivoSalida.setSize(file.getSize());
			archivoSalida.setFechaRegistro(new Date(System.currentTimeMillis()));
			repoService.guardarArchivo(file.getInputstream(), archivoSalida);			
			
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, salidaSeleccionada.getSalId());
			
			archivossalida = (List<ArchivoSalida>) serviceDao.getGenericCommonDao().executeFind(ArchivoSalida.class , params, ArchivoSalida.NAMED_QUERY_GET_BY_SALIDA);
			
			FacesUtils.addFacesMessageFromBundle(OPERACION_EXITOSA_GUARDAR_ARCHIVO,	FacesMessage.SEVERITY_INFO);			
			
		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,	FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
		}
	}
	
	public StreamedContent descargar(Integer idArchivo) {
		try {
			ArchivoSalida archivo = (ArchivoSalida) serviceDao.getGenericCommonDao().read(ArchivoSalida.class, idArchivo);
			InputStream stream = repoService.obtenerInputStream(archivo);

			StreamedContent file = new DefaultStreamedContent(stream,archivo.getContentType(), archivo.getNombre());
			
			return file;
		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public void cargarRegistroDetalleResumen(){		
		try {						
			if(idRegSeleccionado == null){
				 FacesContext facesContext = FacesContext.getCurrentInstance();
			     String idRegSeleccionado = facesContext.getExternalContext().getRequestParameterMap().get("idRegSeleccionado");			   
			     if(idRegSeleccionado != null) this.idRegSeleccionado = Integer.parseInt(idRegSeleccionado);		
			}

			if((salidaSeleccionada == null || salidaSeleccionada.getSalId() == null) 	&&  (idRegSeleccionado != null)){
				salidaSeleccionada =(Salida) serviceDao.getGenericCommonDao().read(Salida.class, idRegSeleccionado);
				
				if(archivossalida == null){					
					Map<Integer, Object> params = new HashMap<Integer, Object>();
					params.put(0, salidaSeleccionada.getSalId());
					
					archivossalida = (List<ArchivoSalida>) serviceDao.getGenericCommonDao().executeFind(ArchivoSalida.class , params, ArchivoSalida.NAMED_QUERY_GET_BY_SALIDA);
				}
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	
	public ServiceDao getServiceDao() {
		return serviceDao;
	}


	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	public Integer getIdRegSeleccionado() {
		return idRegSeleccionado;
	}

	public void setIdRegSeleccionado(Integer idRegSeleccionado) {
		this.idRegSeleccionado = idRegSeleccionado;
	}

	public FileSystemRepositoryService getRepoService() {
		return repoService;
	}

	public void setRepoService(FileSystemRepositoryService repoService) {
		this.repoService = repoService;
	}

	
	public String getSizeLimit(){
		FacesContext ctx = FacesContext.getCurrentInstance();
		String maxRequestSize = ctx.getExternalContext().getInitParameter("maxRequestSize");
		return maxRequestSize;
	}

	public Salida getSalidaSeleccionada() {
		return salidaSeleccionada;
	}

	public void setSalidaSeleccionada(Salida salidaSeleccionada) {
		this.salidaSeleccionada = salidaSeleccionada;
	}

	public List<ArchivoSalida> getArchivossalida() {
		return archivossalida;
	}

	public void setArchivossalida(List<ArchivoSalida> archivossalida) {
		this.archivossalida = archivossalida;
	}
}
