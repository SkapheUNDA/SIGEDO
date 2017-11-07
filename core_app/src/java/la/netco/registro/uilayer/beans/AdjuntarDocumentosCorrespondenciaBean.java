package la.netco.registro.uilayer.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import la.netco.persistencia.dto.commons.ArchivoRegistro;

import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;


@ManagedBean (name="AdjuntarDocumentosCorrespondenciaBean")
@ViewScoped
public class AdjuntarDocumentosCorrespondenciaBean implements Serializable {
	
	private static final long serialVersionUID = 2545659061107489091L;
	private HashMap<String, Object> files =   new HashMap<String, Object>();
	private List<String> dataFiles;
//	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
//	transient private ServiceDao serviceDao;
//	@ManagedProperty(value = BeansSpringConst.BEAN_ID_FILE_SYSTEM_REPO_SERVICE)
//	transient private FileSystemRepositoryService repoService;
//	private String OPERACION_EXITOSA_GUARDAR_ARCHIVO = "corresArchivoGuardadoCorrectamente";
//	private String ERROR_ARCHIVO_REPETIDO = "corresErrorArchivoRepetido";
	private static final Logger log = Logger.getLogger("AdjuntarCorrespondenciaBean");

	public void uploadFile(FileUploadEvent event) throws Exception { 
		try {
			FacesMessage msg = new FacesMessage("Archivo ", event.getFile().getFileName() + " agregado correctamente.");  
			FacesContext.getCurrentInstance().addMessage(null, msg);
			System.out.println("***Entro uploadFile");
			log.info("Entro uploadFile");
			UploadedFile file = event.getFile();
			System.out.println("---Nombre" + file.getFileName());
			System.out.println("---Size Data " + file.getSize());
			
			files.put(file.getFileName() + "ARCHIVO", file);
			listarArchivos();
			
			
		} catch (Exception e){
			log.error("Error en uploadFile: ", e);;
		}
			
	}
	
	public String getSizeLimit() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		String maxRequestSize = ctx.getExternalContext().getInitParameter(
				"maxRequestSize");
		return maxRequestSize;
	}
	
	
	public void listarArchivos(){
		try {
			
			dataFiles = new ArrayList<String>();
			System.out.println("Map size " + files.size());
			for (Map.Entry<String, Object> file : files.entrySet()) {
				if (file.getValue() instanceof UploadedFile) {
					UploadedFile temp = (UploadedFile) file.getValue();
					dataFiles.add(file.getKey().toString() + ": " + temp.getFileName());
					System.out.println("--> Valor Archivos " + file.getKey().toString() + "  " + temp.getFileName());
				}

			}
		}catch (Exception e) {
			
		}
		
	}
	
	

}
