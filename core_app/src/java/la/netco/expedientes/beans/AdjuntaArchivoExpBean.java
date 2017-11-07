package la.netco.expedientes.beans;

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
import la.netco.persistencia.dto.commons.ArchivoExpediente;
import la.netco.persistencia.dto.commons.Expediente;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

@ManagedBean
@ViewScoped
public class AdjuntaArchivoExpBean implements Serializable {

	private List<ArchivoExpediente> listaArchivosExpediente = null;
	private Expediente expedienteSeleccionado;
	private Integer idExpSeleccionado;

	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao daoServicio;

	@ManagedProperty(value = BeansSpringConst.BEAN_ID_FILE_SYSTEM_REPO_SERVICE)
	transient private FileSystemRepositoryService repoServicio;

	private String OPERACION_EXITOSA_GUARDAR_ARCHIVO = "corresArchivoGuardadoCorrectamente";
	private String ERROR_ARCHIVO_REPETIDO = "corresErrorArchivoRepetido";

	@SuppressWarnings("unchecked")
	public void uploadFile(FileUploadEvent event) {
		try {

			UploadedFile file = event.getFile();
			System.out.println("***Guardando archivo para expediente***");

			if (listaArchivosExpediente != null
					&& !listaArchivosExpediente.isEmpty()) {
				for (ArchivoExpediente archivo : listaArchivosExpediente) {
					if (archivo.getNombre()
							.equalsIgnoreCase(file.getFileName())) {
						FacesUtils.addFacesMessageFromBundle(
								ERROR_ARCHIVO_REPETIDO,
								FacesMessage.SEVERITY_ERROR);
						return;
					}
				}
			}

			ArchivoExpediente archivoExpediente = new ArchivoExpediente();
			archivoExpediente.setExpediente(expedienteSeleccionado);
			archivoExpediente.setNombre(file.getFileName());
			archivoExpediente.setContentType(file.getContentType());
			archivoExpediente.setSize(file.getSize());
			archivoExpediente.setFechaRegistro(new Date(System
					.currentTimeMillis()));
			repoServicio.guardarArchivo(file.getInputstream(),
					archivoExpediente);

			Map<Integer, Object> paramsBusq = new HashMap<Integer, Object>();
			paramsBusq.put(0, expedienteSeleccionado.getExpId());

			listaArchivosExpediente = (List<ArchivoExpediente>) daoServicio
					.getGenericCommonDao().executeFind(ArchivoExpediente.class,
							paramsBusq,
							ArchivoExpediente.NAMED_QUERY_GET_BY_EXPEDIENTE);

			FacesUtils.addFacesMessageFromBundle(
					OPERACION_EXITOSA_GUARDAR_ARCHIVO,
					FacesMessage.SEVERITY_INFO);

		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
		}
	}

	public StreamedContent descargar(Integer idArchivo) {
		try {
			ArchivoExpediente archivo = (ArchivoExpediente) daoServicio
					.getGenericCommonDao().read(ArchivoExpediente.class,
							idArchivo);
			InputStream stream = repoServicio.obtenerInputStream(archivo);

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

	@SuppressWarnings("unchecked")
	public void cargarExpedienteSel() {
		try {
			if (idExpSeleccionado == null) {
				FacesContext contextoFaces = FacesContext.getCurrentInstance();
				String idExpSeleccionado = contextoFaces.getExternalContext()
						.getRequestParameterMap().get("idExpSeleccionado");
				if (idExpSeleccionado != null)
					this.idExpSeleccionado = Integer
							.parseInt(idExpSeleccionado);
			}

			if ((expedienteSeleccionado == null || expedienteSeleccionado
					.getExpId() != 0) && (idExpSeleccionado != null)) {
				expedienteSeleccionado = (Expediente) daoServicio
						.getGenericCommonDao().read(Expediente.class,
								idExpSeleccionado);

				if (listaArchivosExpediente == null) {
					Map<Integer, Object> params = new HashMap<Integer, Object>();
					params.put(0, expedienteSeleccionado.getExpId());

					listaArchivosExpediente = (List<ArchivoExpediente>) daoServicio
							.getGenericCommonDao()
							.executeFind(
									ArchivoExpediente.class,
									params,
									ArchivoExpediente.NAMED_QUERY_GET_BY_EXPEDIENTE);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getSizeLimit() {
		FacesContext contexto = FacesContext.getCurrentInstance();
		String maxRequestSize = contexto.getExternalContext().getInitParameter(
				"maxRequestSize");
		return maxRequestSize;
	}

	public ServiceDao getDaoServicio() {
		return daoServicio;
	}

	public void setDaoServicio(ServiceDao daoServicio) {
		this.daoServicio = daoServicio;
	}

	public FileSystemRepositoryService getRepoServicio() {
		return repoServicio;
	}

	public void setRepoServicio(FileSystemRepositoryService repoServicio) {
		this.repoServicio = repoServicio;
	}

	public Expediente getExpedienteSeleccionado() {
		return expedienteSeleccionado;
	}

	public void setExpedienteSeleccionado(Expediente expedienteSeleccionado) {
		this.expedienteSeleccionado = expedienteSeleccionado;
	}

	public Integer getIdExpSeleccionado() {
		return idExpSeleccionado;
	}
	

	public void setIdExpSeleccionado(Integer idExpSeleccionado) {
		this.idExpSeleccionado = idExpSeleccionado;
	}

	public List<ArchivoExpediente> getListaArchivosExpediente() {
		return listaArchivosExpediente;
	}

	public void setListaArchivosExpediente(
			List<ArchivoExpediente> listaArchivosExpediente) {
		this.listaArchivosExpediente = listaArchivosExpediente;
	}

}
