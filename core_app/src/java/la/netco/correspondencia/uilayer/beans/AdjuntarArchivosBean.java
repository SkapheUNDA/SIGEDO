package la.netco.correspondencia.uilayer.beans;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
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
import la.netco.persistencia.dto.commons.ArchivoEntrada;
import la.netco.persistencia.dto.commons.Entrada;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

@ManagedBean
@ViewScoped
public class AdjuntarArchivosBean implements Serializable{
	

	private static final long serialVersionUID = 1L;
	private Integer idRegSeleccionado;
	private Entrada entradaSeleccionada;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_FILE_SYSTEM_REPO_SERVICE)
	transient private FileSystemRepositoryService repoService;
	
	private List<ArchivoEntrada> archivosentrada = null;
	
	private String OPERACION_EXITOSA_GUARDAR_ARCHIVO = "corresArchivoGuardadoCorrectamente";
	private String ERROR_ARCHIVO_REPETIDO = "corresErrorArchivoRepetido";
	
	@SuppressWarnings("unchecked")
	public void uploadFile(FileUploadEvent event)  {
		try {

			UploadedFile file = event.getFile();
			System.out.println(" guarda archivo");
			
			if(archivosentrada != null && !archivosentrada.isEmpty()){
				for (ArchivoEntrada archivo : archivosentrada) {
					if(archivo.getNombre().equalsIgnoreCase(file.getFileName())){
						FacesUtils.addFacesMessageFromBundle(ERROR_ARCHIVO_REPETIDO,FacesMessage.SEVERITY_ERROR);
						return;
					}
				}
			}
			
			ArchivoEntrada archivoEntrada = new ArchivoEntrada();
			archivoEntrada.setEntrada(entradaSeleccionada);
			archivoEntrada.setNombre(file.getFileName());
			archivoEntrada.setContentType(file.getContentType());
			archivoEntrada.setSize(file.getSize());
			archivoEntrada.setFechaRegistro(new Date(System.currentTimeMillis()));
			repoService.guardarArchivo(file.getInputstream(), archivoEntrada);			
			
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, entradaSeleccionada.getEntId());
			
			archivosentrada = (List<ArchivoEntrada>) serviceDao.getGenericCommonDao().executeFind(ArchivoEntrada.class , params, ArchivoEntrada.NAMED_QUERY_GET_BY_ENTRADA);
			
			FacesUtils.addFacesMessageFromBundle(OPERACION_EXITOSA_GUARDAR_ARCHIVO,	FacesMessage.SEVERITY_INFO);			
			
		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,	FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
		}
	}
	
	public StreamedContent descargar(Integer idArchivo) {
		try {
			ArchivoEntrada archivo = (ArchivoEntrada) serviceDao.getGenericCommonDao().read(ArchivoEntrada.class, idArchivo);
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
			
			if((entradaSeleccionada == null || entradaSeleccionada.getEntId() == null) 	&&  (idRegSeleccionado != null)){
				entradaSeleccionada =(Entrada) serviceDao.getEntradaDao().read(Entrada.class, idRegSeleccionado);
				
				if(archivosentrada == null){					
					Map<Integer, Object> params = new HashMap<Integer, Object>();
					params.put(0, entradaSeleccionada.getEntId());
					
					archivosentrada = (List<ArchivoEntrada>) serviceDao.getGenericCommonDao().executeFind(ArchivoEntrada.class , params, ArchivoEntrada.NAMED_QUERY_GET_BY_ENTRADA);
				}
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public static void main(String args[]) throws IOException {

			File f = new File("C:/"); // current directory

			final String pattern = ".*.jpg";
			
			FilenameFilter textFilter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					 return name.matches( pattern );
				}
			};

			File[] files = f.listFiles(textFilter);
			for (File file : files) {
				if (file.isDirectory()) {
					System.out.print("directory:");
				} else {
					System.out.print("     file:");
				}
				System.out.println(file.getCanonicalPath());
			}

		
		
	} 

	
	public Entrada getEntradaSeleccionada() {
		return entradaSeleccionada;
	}

	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public void setEntradaSeleccionada(Entrada entradaSeleccionada) {
		this.entradaSeleccionada = entradaSeleccionada;
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

	public List<ArchivoEntrada> getArchivosentrada() {
		return archivosentrada;
	}

	public void setArchivosentrada(List<ArchivoEntrada> archivosentrada) {
		this.archivosentrada = archivosentrada;
	}
	
	public String getSizeLimit(){
		FacesContext ctx = FacesContext.getCurrentInstance();
		String maxRequestSize = ctx.getExternalContext().getInitParameter("maxRequestSize");
		return maxRequestSize;
	}
}
