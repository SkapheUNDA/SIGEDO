package la.netco.registro.uilayer.beans;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
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

import la.netco.commons.services.FileSystemRepositoryService;
import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.persistencia.vo.Usuario;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.spring.security.UserDetailsUtils;
import la.netco.core.uilayer.beans.Constants;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.persistencia.dto.commons.ArchivoRegistro;
import la.netco.persistencia.dto.commons.ConstantsKeysFire;
import la.netco.persistencia.dto.commons.Documentos;
import la.netco.persistencia.dto.commons.Expediente;
import la.netco.persistencia.dto.commons.Registro;
import la.netco.persistencia.dto.commons.Transicion;

import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import b.b.b.c.d;

@ManagedBean
@ViewScoped
public class AdjuntaDocRegBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer idRegSeleccionado;
	private Registro registroSeleccionado;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_FILE_SYSTEM_REPO_SERVICE)
	transient private FileSystemRepositoryService repoService;

	private List<ArchivoRegistro> archivosregistro = null;
	private List<SelectItem> itemsDocumentos;

	private Integer idTipoDocumentoSeleccionado;

	private String OPERACION_EXITOSA_GUARDAR_ARCHIVO = "corresArchivoGuardadoCorrectamente";
	private String ERROR_ARCHIVO_REPETIDO = "corresErrorArchivoRepetido";
	private static final Logger log = Logger.getLogger("AdjuntaDocRegBean");


	@SuppressWarnings("unchecked")
	public void uploadFile(FileUploadEvent event) {
		try {

			UploadedFile file = event.getFile();
			System.out.println(" guarda archivo");
			if (archivosregistro != null && !archivosregistro.isEmpty()) {
				for (ArchivoRegistro archivo : archivosregistro) {
					if (archivo.getNombre()
							.equalsIgnoreCase(file.getFileName())) {
						FacesUtils.addFacesMessageFromBundle(
								ERROR_ARCHIVO_REPETIDO,
								FacesMessage.SEVERITY_ERROR);
						return;
					}
				}
			}

			ArchivoRegistro archivoRegistro = new ArchivoRegistro();
			archivoRegistro.setRegistro(registroSeleccionado);
			archivoRegistro.setNombre(file.getFileName());
			archivoRegistro.setContentType(file.getContentType());
			archivoRegistro.setSize(file.getSize());
			archivoRegistro.setFechaRegistro(new Date(System
					.currentTimeMillis()));
			archivoRegistro.setDocumentos((Documentos) serviceDao
					.getGenericCommonDao().read(Documentos.class,
							idTipoDocumentoSeleccionado));
			repoService.guardarArchivo(file.getInputstream(), archivoRegistro);

			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, registroSeleccionado.getRegId());

			archivosregistro = (List<ArchivoRegistro>) serviceDao
					.getGenericCommonDao()
					.executeFind(ArchivoRegistro.class, params,
							ArchivoRegistro.NAMED_QUERY_GET_BY_REGISTRO);
			System.out.println("///////////////////////////////////////////////////////////////////////////////////////////");
			Expediente objExpediente = registroSeleccionado.getExpediente();
			
			serviceDao.getSeguimientosDao().addSeguimientoExp(objExpediente,"Se agrego archivo "+archivoRegistro.getNombre(),"");
			System.out.println("agrego seguimiento");

			FacesUtils.addFacesMessageFromBundle(
					OPERACION_EXITOSA_GUARDAR_ARCHIVO,
					FacesMessage.SEVERITY_INFO);

		} catch (Exception e) {
			System.out.println("///"+e);
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
		}
	}

	public StreamedContent descargar(Integer idArchivo) {
		try {
			ArchivoRegistro archivo = (ArchivoRegistro) serviceDao
					.getGenericCommonDao().read(ArchivoRegistro.class,
							idArchivo);
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

	@SuppressWarnings("unchecked")
	public void cargarRegistroDetalleResumen() {
		try {
			if (idRegSeleccionado == null) {
				FacesContext facesContext = FacesContext.getCurrentInstance();
				String idRegSeleccionado = facesContext.getExternalContext()
						.getRequestParameterMap().get("idRegSeleccionado");
				if (idRegSeleccionado != null)
					this.idRegSeleccionado = Integer
							.parseInt(idRegSeleccionado);
			}

			if ((registroSeleccionado == null || registroSeleccionado
					.getRegId() == null) && (idRegSeleccionado != null)) {
				registroSeleccionado = (Registro) serviceDao
						.getGenericCommonDao().read(Registro.class,
								idRegSeleccionado);

				if (archivosregistro == null) {
					Map<Integer, Object> params = new HashMap<Integer, Object>();
					params.put(0, registroSeleccionado.getRegId());

					archivosregistro = (List<ArchivoRegistro>) serviceDao
							.getGenericCommonDao()
							.executeFind(ArchivoRegistro.class, params,
									ArchivoRegistro.NAMED_QUERY_GET_BY_REGISTRO_SIN_PDF);
					
					cargaItemsDocumentos();
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
				return name.matches(pattern);
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

	@SuppressWarnings("unchecked")
	public void cargaItemsDocumentos() {
		if (itemsDocumentos == null) {
			List<Documentos> listaAuxiDocumentos = (List<Documentos>) serviceDao
					.getGenericCommonDao().loadAll(Documentos.class);

			itemsDocumentos = new ArrayList<SelectItem>();

			for (Documentos documentos : listaAuxiDocumentos) {
				itemsDocumentos.add(new SelectItem(documentos.getDocId(),
						documentos.getDocNombre()));
			
			}
		}
	}

	public Registro getRegistroSeleccionado() {
		return registroSeleccionado;
	}

	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public void setRegistroSeleccionado(Registro registroSeleccionado) {
		this.registroSeleccionado = registroSeleccionado;
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

	public List<ArchivoRegistro> getArchivosregistro() {
		return archivosregistro;
	}

	public void setArchivosregistro(List<ArchivoRegistro> archivosregistro) {
		this.archivosregistro = archivosregistro;
	}

	public String getSizeLimit() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		String maxRequestSize = ctx.getExternalContext().getInitParameter(
				"maxRequestSize");
		return maxRequestSize;
	}

	/**
	 * @return the idTipoDocumentoSeleccionado
	 */
	public Integer getIdTipoDocumentoSeleccionado() {
		return idTipoDocumentoSeleccionado;
	}

	/**
	 * @param idTipoDocumentoSeleccionado
	 *            the idTipoDocumentoSeleccionado to set
	 */
	public void setIdTipoDocumentoSeleccionado(
			Integer idTipoDocumentoSeleccionado) {
		this.idTipoDocumentoSeleccionado = idTipoDocumentoSeleccionado;
	}

	/**
	 * @return the itemsDocumentos
	 */
	public List<SelectItem> getItemsDocumentos() {
		return itemsDocumentos;
	}

	/**
	 * @param itemsDocumentos
	 *            the itemsDocumentos to set
	 */
	public void setItemsDocumentos(List<SelectItem> itemsDocumentos) {
		this.itemsDocumentos = itemsDocumentos;
	}
}
