package la.netco.sgc.uilayer.beans;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;

import la.netco.commons.services.FileSystemRepositoryService;
import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.spring.security.UserDetailsUtils;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.persistencia.dto.commons.Calidadrepresentante;
import la.netco.persistencia.dto.commons.Clasificacion;
import la.netco.persistencia.dto.commons.Depend;
import la.netco.persistencia.dto.commons.Entrada;
import la.netco.persistencia.dto.commons.Lugar;
import la.netco.persistencia.dto.commons.Medioscorrespondencia;
import la.netco.persistencia.dto.commons.Paises;
import la.netco.persistencia.dto.commons.Tiposdocumento;
import la.netco.persistencia.dto.commons.Tipospersona;
import la.netco.sgc.persistence.dto.ArchivoSGC;
import la.netco.sgc.persistence.dto.CortesFormato;
import la.netco.sgc.persistence.dto.DetallesFormato;
import la.netco.sgc.persistence.dto.Entidades;
import la.netco.sgc.persistence.dto.Formatos;
import la.netco.sgc.persistence.dto.FormatosTiposDato;
import la.netco.sgc.persistence.dto.InformeEntidad;
import la.netco.sgc.persistence.dto.RegistroCargue;
import la.netco.sgc.persistence.dto.RegistroCargueFormato;
import la.netco.sgc.persistence.dto.RegistroRadicado;
import la.netco.sgc.persistence.dto.TiposDato;
import la.netco.sgc.uilayer.beans.util.Archivo;
import la.netco.sgc.uilayer.beans.util.CargueArchivoExcel;
import la.netco.sgc.uilayer.beans.util.Columna;
import la.netco.sgc.uilayer.beans.util.ErrorGenerico;
import la.netco.sgc.uilayer.beans.util.Fila;

import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author cpulido
 * 
 */
/**
 * @author cpineros
 * 
 */
@ManagedBean(name = "cargarFormatoBean")
@ViewScoped
public class CargarFormatoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{serviceDao}")
	transient private ServiceDao serviceDao;

	@ManagedProperty(value = BeansSpringConst.BEAN_ID_FILE_SYSTEM_REPO_SERVICE)
	transient private FileSystemRepositoryService repoServicio;

	// Listas
	private List<SelectItem> listaFormatos;
	private List<SelectItem> listaEntidades;
	private List<SelectItem> listaFechasCorte;
	private String periodoHabilitado;
	private List<String> errorLog;
	private Boolean faltaAlgunObligatorio;

	// campos
	private Integer formatoSeleccionado;
	private Integer entidadSeleccionada;
	private Integer idFechaCorteSeleccionada;
	private Date fechaVencimientoCargue;
	private String tmpFile;
	private String fechaCorte;

	// parametros internos
	private List<Formatos> arrFormatos;
	private List<InformeEntidad> informesPorEntidad;
	private List<Entidades> arrEntidades;
	private List<RegistroCargueFormato> listaregistroCargueFormato;
	@SuppressWarnings("unused")
	@Deprecated
	private List<String> arrQuerys;
	private Formatos formato;
	private Entidades entidad;
	private Integer formatoDetalladoId;
	private Integer formatoComparativoId;
	private Integer formatoInformeGestionId;
	private String formatoGeneralId;
	private Integer formatoFlujoEfectivoId;
	
	public static final String IMG = "/resources/images/indice.jpg";

	// parametros de validacion
	private List<Map<String, Object>> validador;

	private List<Archivo> archivosCargados;
	private List<ErrorGenerico> erroresCargue;

	public CargarFormatoBean() {
		this.listaEntidades = new ArrayList<SelectItem>();
		this.listaFormatos = new ArrayList<SelectItem>();
		this.listaFechasCorte = new ArrayList<SelectItem>();
		this.errorLog = new ArrayList<String>();
		this.arrQuerys = new ArrayList<String>();
		this.arrEntidades = new ArrayList<Entidades>();
		this.arrFormatos = new ArrayList<Formatos>();
		this.archivosCargados = new ArrayList<Archivo>();
	}

	@SuppressWarnings("unused")
	@PostConstruct
	private void initCargarFormatoBean() {		
		System.out.println("Init");
		cargarPeriodoHabilitado();
		cargarEntidades();
		cargarFormatos();		
		formatoComparativoId = Integer.parseInt(FacesUtils.getPropsSGC().getString("idFormatoComparativo"));
		formatoDetalladoId = Integer.parseInt(FacesUtils.getPropsSGC().getString("idFormatoDetallado"));
		formatoInformeGestionId = Integer.parseInt(FacesUtils.getPropsSGC().getString("idFormatoInformeGestion"));
		formatoGeneralId = FacesUtils.getPropsSGC().getString("idFormatoGeneral");
		formatoFlujoEfectivoId = Integer.parseInt(FacesUtils.getPropsSGC().getString("idFormatoFlujoEfectivo"));
		System.out.println("Valor de Formato Detallado" + formatoDetalladoId);
		System.out.println("Valor de Formato Comparativo " + formatoComparativoId);
		System.out.println("Valor de Formato Informe Gestion Colectiva " + formatoInformeGestionId);
		System.out.println("Valor de Formato General " + formatoGeneralId);
		System.out.println("Valor de Formato Flujo Efectivo" + formatoFlujoEfectivoId);
	}
	
	
	private void cargarPeriodoHabilitado(){
		CortesFormato corteFormatoConsulta = serviceDao.getCortesFormatoDao().obtenerCortesFormatoActivoPorFormato();
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");		
		try {
			fechaVencimientoCargue = formato.parse(corteFormatoConsulta.getFcrCorte());
		} catch (ParseException e) {
			FacesUtils.addFacesMessage("Se ha presentado un fallo inesperado durante el cargue de archivos. Detalles: " + e.getMessage(),
					FacesMessage.SEVERITY_ERROR);
			e.printStackTrace();
		}		
		if (corteFormatoConsulta!=null){
			periodoHabilitado=corteFormatoConsulta.getFcrPeriodo()+" "+corteFormatoConsulta.getAno();
			idFechaCorteSeleccionada=corteFormatoConsulta.getFcrCodigo();
		}
	}
	
	String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }
	
	
	/**
	 * Convierte un String a un Calendar usando el formato de fecha indicado.
	 * <b>Caso de Uso: </b> Utilidades de Calendar
	 * 
	 * @param date
	 *            String contenedor de una fecha.
	 * @param format
	 *            formato de fecha en el que se encuentra date.
	 * @return Un Calendar correspondiente a date.
	 */
	private Calendar parseDate(String date, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		try {
			Date returnDate = dateFormat.parse(date);
			Calendar returnCalendar = Calendar.getInstance();
			returnCalendar.setTime(returnDate);
			return returnCalendar;
		} catch (Exception e) {
			return null;
		}
	}

	private void cargarEntidades() {

		Set<Entidades> entidadesUsuario = UserDetailsUtils.usuarioLogged().getEntidades();
		arrEntidades = new ArrayList<Entidades>(entidadesUsuario);
		entidadSeleccionada=arrEntidades.get(0).getEntCodigo();
		System.out.println("Sale Entidades");
	}

	@SuppressWarnings("unchecked")
	public void cargaListaRegistroCargue(Integer idEntidadSeleccionada) {
		HashMap<Integer, Integer> paramsBusq = new HashMap<Integer, Integer>();
		listaregistroCargueFormato = new ArrayList<RegistroCargueFormato>();
		paramsBusq.put(0, idEntidadSeleccionada);
	}

	@SuppressWarnings("unchecked")
	private void cargarFormatos() {		
		faltaAlgunObligatorio= Boolean.FALSE;
		arrFormatos = (List<Formatos>) serviceDao.getGenericCommonDao().loadAll(Formatos.class);				
		informesPorEntidad = serviceDao.getInformeEntidadDao().tomarInformesEntidad(entidadSeleccionada);
		for (Formatos f : arrFormatos) {
			RegistroCargueFormato registroCargueFormato = serviceDao.getRegistroCargueFormatoDao().tomarRegistroCargueFormatoPorCargue(f.getForCodigo(), entidadSeleccionada, idFechaCorteSeleccionada);
			if (registroCargueFormato==null){	
				if(formatosPorEntidad(informesPorEntidad, f.getForCodigo()))
				{
					listaFormatos.add(new SelectItem(f.getForCodigo(), f.getForNombre()));
				}
			}			
		}
		System.out.println("Sale formatos " + arrFormatos.size());
	}
	
	private Boolean formatosPorEntidad(List<InformeEntidad> informes, int codigo){
		for (InformeEntidad informeEntidad : informes) {
			if (codigo==informeEntidad.getFormato().getForCodigo()){
				if (informeEntidad.getObligatorio()){
					faltaAlgunObligatorio=Boolean.TRUE;
				}
				return true;
			}
		}
		return false;
	}
		

	public void cambiarFormato() {
		String sql = "SELECT FCR_Corte FROM sgc.Cortes_Formato ";
		sql += "WHERE FCR_Activo = 'true' ";
		sql += "AND FOR_Codigo = " + formatoSeleccionado + " ";
		sql += "AND FCR_Vencido = 'false' ";
		sql += "ORDER BY FCR_Corte desc ";

		fechaCorte = (String) serviceDao.getSpringGenericDao().executeQuery(sql).get(0).get("FCR_Corte");

		System.out.println(fechaCorte);
	}

	public void cargarValidaciones() {
		String sql = "SELECT F.FOR_Nombre AS tabla, F.FOR_TotalCampos AS cantColumnas, "
				+ "F.FOR_lineaInicial as LineaInicial, F.FOR_NombreHoja as NombreHoja, "
				+ "FD.DET_Descripcion AS columna, TD.TPD_Descripcion AS tipoDato, "
				+ "FD.DET_Requerido as requerido, FTD.FTD_Formato as formatoDato,  " + "F.FOR_Codigo, FD.DET_Codigo, FCR.FCR_Codigo "
				+ "FROM SGC.FORMATOS F " + "INNER JOIN SGC.Detalles_Formato FD ON FD.FOR_CODIGO = F.FOR_CODIGO "
				+ "INNER JOIN SGC.Tipos_Dato TD ON TD.TPD_Codigo = FD.TPD_Codigo "
				+ "LEFT JOIN SGC.Formatos_TiposDato FTD ON FTD.TPD_Codigo = TD.TPD_Codigo "
				+ "INNER JOIN SGC.Cortes_Formato FCR ON FCR.FOR_Codigo = F.FOR_Codigo " + "WHERE F.FOR_Codigo = " + formatoSeleccionado;

		validador = serviceDao.getSpringGenericDao().executeQuery(sql);

		for (Formatos f : arrFormatos) {
			if (f.getForCodigo() == formatoSeleccionado) {
				formato = f;
				break;
			}
		}

		for (Entidades e : arrEntidades) {
			if (e.getEntCodigo() == entidadSeleccionada) {
				entidad = e;
				break;
			}
		}

		System.out.println("Sale validaciones " + this.validador.size());
	}
	
	private Boolean validarFecha(Calendar dateStart) {
		Calendar dateEnd = Calendar.getInstance();

		int dateDifference = 0;

		dateStart.set(Calendar.HOUR_OF_DAY, 0);
		dateStart.set(Calendar.MINUTE, 0);
		dateStart.set(Calendar.SECOND, 0);
		dateStart.set(Calendar.MILLISECOND, 0);

		dateEnd.set(Calendar.HOUR_OF_DAY, 0);
		dateEnd.set(Calendar.MINUTE, 0);
		dateEnd.set(Calendar.SECOND, 0);
		dateEnd.set(Calendar.MILLISECOND, 0);

		dateDifference = dateStart.compareTo(dateEnd);
		if (dateDifference == -1) {			
			return Boolean.TRUE;
		}

		return Boolean.FALSE;
	}

	/**
	 * Metodo utilizado para inicar el proceso de cargue de un archivo.
	 * 
	 * @param event
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void uploadFile(FileUploadEvent event) throws Exception {
		Calendar fechaV= Calendar.getInstance();
		fechaV.setTime(fechaVencimientoCargue);
			try {			
				if (!validarFecha(fechaV)){
				String nombreFormato="";
				for (Formatos f : arrFormatos) {
					if (f.getForCodigo()==formatoSeleccionado.intValue()){
						nombreFormato=f.getForNombre();
					}				
				}
				archivosCargados = null;
				System.out.println("Entra en evento file upload");
				System.out.println("Entidad Seleccionada " + entidadSeleccionada);
				System.out.println("Formato Seleccionado " + formatoSeleccionado);
				UploadedFile archivo = event.getFile();
				String nombreReal = archivo.getFileName();
				String[] composicion = nombreReal.split("\\.");
				String extension = composicion[composicion.length - 1];
				RegistroCargueFormato registroCargueFormato = new RegistroCargueFormato();
				CortesFormato objCortesFormato = new CortesFormato();
				Date fechaRegistroCargue = new Date(System.currentTimeMillis());
	
				String nombreArchivo = FilenameUtils.removeExtension(archivo.getFileName()) + "-" + System.currentTimeMillis() + "." + extension;
	
				Archivo archivoCargado = new Archivo();
				archivoCargado.setNombre(nombreArchivo);
				archivoCargado.setExtension(extension);
	
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(System.currentTimeMillis());
				archivoCargado.setFechaHoraCargue(calendar.getTime());
	
				archivosCargados = new ArrayList<Archivo>();
				archivosCargados.add(archivoCargado);
	
				InputStream is = archivo.getInputstream();
				setTmpFile(nombreArchivo);
				FileOutputStream fos = new FileOutputStream(new File(getTmpFile()));
	
				int read = 0;
				byte[] bytes = new byte[1024];
	
				while ((read = is.read(bytes)) != -1) {
					fos.write(bytes, 0, read);
				}
	
				is.close();
				fos.flush();
				fos.close();
					
				formato = (Formatos) serviceDao.getGenericCommonDao().read(Formatos.class, formatoSeleccionado);			
				entidad = (Entidades) serviceDao.getGenericCommonDao().read(
						Entidades.class, entidadSeleccionada);
				if (null != entidad) {
	
					if (null != formato) {					
	
							List<DetallesFormato> detallesFormatos = serviceDao.getDetallesFormatoDao().obtenerDetallesFormatoPorFormato(
									formatoSeleccionado);
	
							List<TiposDato> tiposDatos = (List<TiposDato>) serviceDao.getGenericCommonDao().loadAll(TiposDato.class);
							List<FormatosTiposDato> formatosTiposDatos = (List<FormatosTiposDato>) serviceDao.getGenericCommonDao().loadAll(
									FormatosTiposDato.class);
	
							this.erroresCargue = null;
							CargueArchivoExcel cargue=null;
							formato.setCorteAsignado(idFechaCorteSeleccionada);
							if ("xlsx".equalsIgnoreCase(archivosCargados.get(0).getExtension()) || "xls".equalsIgnoreCase(archivosCargados.get(0).getExtension())) {
								cargue = new CargueArchivoExcel(serviceDao, formato, entidad, detallesFormatos, tiposDatos, formatosTiposDatos);	
							}						
							
							cargue.cargar(archivosCargados, nombreFormato);
							
	
							int registros=registrarDatos();
	
							if (this.erroresCargue == null || this.erroresCargue.size() == 0) {
	
								objCortesFormato = (CortesFormato) serviceDao.getGenericCommonDao().read(CortesFormato.class, idFechaCorteSeleccionada);
								registroCargueFormato.setEntidades(entidad);
								registroCargueFormato.setFormatos(formato);
								registroCargueFormato.setCortesFormato(objCortesFormato);
								registroCargueFormato.setRcfFecha(fechaRegistroCargue);
								registroCargueFormato.setUsuario(UserDetailsUtils.usuarioLogged());
								serviceDao.getGenericCommonDao().create(RegistroCargueFormato.class, registroCargueFormato);
	
								ArchivoSGC archivoSGC = new ArchivoSGC();
								archivoSGC.setEntidades((Entidades) serviceDao.getGenericCommonDao().read(Entidades.class, entidadSeleccionada));
								archivoSGC.setFormatos((Formatos) serviceDao.getGenericCommonDao().read(Formatos.class, formatoSeleccionado));
								archivoSGC.setNombre(nombreArchivo);
								archivoSGC.setContentType(archivo.getContentType());
								archivoSGC.setTamano(archivo.getSize());
								archivoSGC.setFechaRegistro(new Date(System.currentTimeMillis()));
								archivoSGC.setPeriodo(objCortesFormato.getFcrPeriodo());
								archivoSGC.setAno(objCortesFormato.getAno());
								repoServicio.guardarArchivo(archivo.getInputstream(), archivoSGC);
	
								FacesUtils.addFacesMessage("Archivo cargado con éxito.  Total registros: "+registros, FacesMessage.SEVERITY_INFO);
								listaFormatos = new ArrayList<SelectItem>();
								cargarFormatos();							
								entidadSeleccionada = null;
								cargarEntidades();
								formatoSeleccionado = null;
								entidad = null;
								formato = null;
								fechaCorte = null;
								registroCargueFormato = new RegistroCargueFormato();
								objCortesFormato = new CortesFormato();
	
							} else {
	
								FacesUtils.addFacesMessage("Se presentaron errores durante el cargue del archivo", FacesMessage.SEVERITY_ERROR);
							}
						
					} else {
	
						FacesUtils.addFacesMessage("El campo Formato es obligatorio", FacesMessage.SEVERITY_ERROR);
					}
				} else {
	
					FacesUtils.addFacesMessage("El campo Entidad es obligatorio", FacesMessage.SEVERITY_ERROR);
				}
			}else{
				SimpleDateFormat fechaFormat = new SimpleDateFormat("dd/MM/yyyy");
				FacesUtils.addFacesMessage("No es posible realizar el cargue.  La fecha actual es mayor a la fecha de vencimiento: "+fechaFormat.format(fechaVencimientoCargue), FacesMessage.SEVERITY_ERROR);
			}
		} catch (Exception e) {

			FacesUtils.addFacesMessage("Se ha presentado un fallo inesperado durante el cargue de archivos. Detalles: " + e.getMessage(),
					FacesMessage.SEVERITY_ERROR);
			e.printStackTrace();
		}
	}

	/**
	 * Metodo utilizado para consultar la fecha de corte activa dependiendo del
	 * formato seleccionado
	 * 
	 * @param e
	 */
	public void hallarFechaCorteFormato(ValueChangeEvent e) {

		if (null != e.getNewValue()) {
			formatoSeleccionado = (Integer) e.getNewValue();
			cargaListaFechasCorte(formatoSeleccionado);
		}
	}

	/**
	 * evento para cargar la lista de registro cargue formato en la tabla de
	 * datos.
	 * 
	 * @param e
	 */
	public void hallarCargueEntidades(ValueChangeEvent e) {

		if (null != e.getNewValue()) {
			entidadSeleccionada = (Integer) e.getNewValue();
			cargaListaRegistroCargue(entidadSeleccionada);
		}
	}

	/**
	 * 
	 * Metodo para cargar las lista de fechas corte para un formato
	 * seleccionado.
	 * 
	 * @param idFormato
	 */
	@SuppressWarnings("unchecked")
	public void cargaListaFechasCorte(Integer idFormato) {
		System.out.println("***Cargando fechas de corte para formato : " + idFormato);
		Calendar fechaActual = Calendar.getInstance();
		HashMap<Integer, Object> paramBusq = new HashMap<Integer, Object>();
		Integer anoAnterior = (fechaActual.get(Calendar.YEAR) - 1);
		Integer anoActual = fechaActual.get(Calendar.YEAR);
		Integer anoFuturo = (fechaActual.get(Calendar.YEAR) + 1);
		paramBusq.put(0, idFormato);
		paramBusq.put(1, "%" + anoAnterior + "%");
		paramBusq.put(2, "%" + anoActual + "%");
		paramBusq.put(3, "%" + anoFuturo + "%");

		if (listaFechasCorte.isEmpty()) {
			System.out.println("Fechas Nulas");
			List<CortesFormato> listaCortesFormatosAuxi = (List<CortesFormato>) serviceDao.getGenericCommonDao().executeFind(CortesFormato.class,
					paramBusq, CortesFormato.NAMED_QUERY_GET_BY_FORMATOF);

			for (CortesFormato cortesFormato : listaCortesFormatosAuxi) {
				listaFechasCorte.add(new SelectItem(cortesFormato.getFcrCodigo(), cortesFormato.getFcrCorte()));
			}

		} else if (!listaFechasCorte.isEmpty()) {
			System.out.println("Fechas Llenas , Limpiando.");
			listaFechasCorte.clear();
			List<CortesFormato> listaCortesFormatosAuxi = (List<CortesFormato>) serviceDao.getGenericCommonDao().executeFind(CortesFormato.class,
					paramBusq, CortesFormato.NAMED_QUERY_GET_BY_FORMATOF);

			for (CortesFormato cortesFormato : listaCortesFormatosAuxi) {
				listaFechasCorte.add(new SelectItem(cortesFormato.getFcrCodigo(), cortesFormato.getFcrCorte()));
			}
		}
	}

	/**
	 * Metodo utilizado para registrar en la base de datos los datos tomados del
	 * archivo cargado
	 * 
	 * @throws Exception
	 * 
	 * 
	 */
	public int registrarDatos() throws Exception {

		validarDatosRegistro();
		int i=0;
		if (this.erroresCargue == null || this.erroresCargue.size() == 0) {			

			for (Archivo archivo : archivosCargados) {
				for (Fila fila : archivo.getFilas()) {
					for (Columna col : fila.getColumnas()) {
						if (null != col.getValor() && !"".equals(col.getValor())) {

							RegistroCargue cargue = new RegistroCargue();
							cargue.setCrgNoRegistro(fila.getId());
							cargue.setCrgValor(col.getValor());
							cargue.setDetallesFormato(col.getDetalle());
							cargue.setCortesFormato(archivo.getCorte());
							cargue.setEntCodigo(archivo.getEntidad().getEntCodigo());
							cargue.setFormatos(archivo.getFormato());
							i=fila.getId();
							serviceDao.getGenericCommonDao().create(RegistroCargue.class, cargue);							
						}
					}
					
				}
			}			
		}
		return i;
	}

	/**
	 * Metodo utilizado para registrar en la base de datos los datos tomados del
	 * archivo cargado
	 * 
	 * 
	 */
	public void validarDatosRegistro() {

		try {
			this.erroresCargue = new ArrayList<ErrorGenerico>();

			BucleArchivo: for (Archivo archivo : archivosCargados) {

				if (archivo.getErrores() != null || archivo.getErrores().size() > 0) {
					
					for (ErrorGenerico e : archivo.getErrores()) {
						this.erroresCargue.add(e);
					}
				}
					if (archivo.getFilas() != null) {

						for (Fila fila : archivo.getFilas()) {

							if (!fila.isEsEncabezado() && fila.getErrores() == null) {

								for (Columna col : fila.getColumnas()) {

									if (col.getError() != null) {

										this.erroresCargue.add(col.getError());
										continue BucleArchivo;
									}
								}
							} else {

								if (fila.getErrores() != null && fila.getErrores().size()>0) {
									this.erroresCargue.addAll(fila.getErrores());
									//continue BucleArchivo;
								}
							}
						}
					}				
			}
			
			
		} catch (Exception e) {
			FacesUtils.addFacesMessage("Se ha presentado un error inesperado durante el proceso de registro de la información, \n\n Detalles: \n "
					+ e.getMessage(), FacesMessage.SEVERITY_INFO);
		}
	}
	
	public void download(){
		try{						
			byte[] contentInBytes=null;
			String linea="";
			String nuevalinea = System.getProperty("line.separator");
			String columna="";
			String valor="";
													            		           
	            for (ErrorGenerico error : erroresCargue) {
	            	columna=error.getColumna()!=null?error.getColumna():"NA";
	            	valor=error.getValor()!=null?error.getValor():"NA";
	            	linea=linea+"L�nea: "+error.getIdFila()+ " Columna: "+columna+" Valor: "+valor+ " Descripci�n: "+error.getMensaje()+nuevalinea;	            		            	
				}		            	                       	            
	            contentInBytes = linea.getBytes();
	            FacesContext fc = FacesContext.getCurrentInstance();
	            HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();
	            response.reset();
	            response.setContentType("text/plain");
	            response.setContentLength(contentInBytes.length);
	            String attachmentName = "attachment; filename=\"export.txt\"";
	            response.setHeader("Content-Disposition", attachmentName);
	            try {
	                OutputStream output = response.getOutputStream();
	                output.write(contentInBytes);		                
	            } catch (IOException ex) {
	            	FacesUtils.addFacesMessage(
	    					"Se ha presentado un fallo inesperado exportando el registro a PDF: "
	    							+ ex.getMessage(), FacesMessage.SEVERITY_ERROR);
	    			ex.printStackTrace();
	            }

	            fc.responseComplete();
	            
	        
		} catch (Exception e) {
		   e.printStackTrace();
		}
	}
	
	public void descargarCertificado() {
		RegistroRadicado registro = serviceDao.getRegistroRadicadoDao()
				.tomarRegistroRadicado(entidadSeleccionada,
						idFechaCorteSeleccionada);
		if (registro != null) {
			exportarPDF(registro);
		} else {
			entidad = (Entidades) serviceDao.getGenericCommonDao().read(
					Entidades.class, entidadSeleccionada);
			Integer idEntrada=generarRadicado();
			registro = new RegistroRadicado();
			registro.setEntidad(entidad);
			CortesFormato objCortesFormato = (CortesFormato) serviceDao
					.getGenericCommonDao().read(CortesFormato.class,
							idFechaCorteSeleccionada);
			registro.setCortesFormato(objCortesFormato);
			Entrada entrada = (Entrada)serviceDao.getGenericCommonDao().read(Entrada.class, idEntrada);
			registro.setEntrada(entrada);
			registro.setFecha(new Date());
			serviceDao.getGenericCommonDao().create(RegistroRadicado.class,
					registro);
			exportarPDF(registro);
		}

	}
	
	
	private Integer generarRadicado(){
		try{
		Entrada nuevoRegistro = new Entrada();
		nuevoRegistro.setEntAsu("INFORME TRIMESTRAL PERIODO "+periodoHabilitado);		
		nuevoRegistro.setEntFre(new Date(System.currentTimeMillis()));
				
		
		Tipospersona tipoPersona= (Tipospersona) serviceDao.getGenericCommonDao().read(Tipospersona.class, Tipospersona.TIPO_PERSONA_JURIDICA);
		nuevoRegistro.setTipoPersona(tipoPersona);
		
			Tiposdocumento tipoDocumento = (Tiposdocumento) serviceDao.getGenericCommonDao().read(Tiposdocumento.class, 7);
			nuevoRegistro.setTiposdocumento(tipoDocumento);
		
		
		
			Paises pais = (Paises) serviceDao.getGenericCommonDao().read(Paises.class, new Integer(1).shortValue());
			nuevoRegistro.setPais(pais);
		
		nuevoRegistro.setEntDir(entidad.getEntDireccion());
		
		
			Lugar lugar = (Lugar) serviceDao.getGenericCommonDao().read(Lugar.class, new Integer(1).shortValue());
			nuevoRegistro.setLugar(lugar);
		
						
		nuevoRegistro.setEntCano(Calendar.getInstance().get(Calendar.YEAR));
		nuevoRegistro.setEntNdo("");
		nuevoRegistro.setEntCar("");
		nuevoRegistro.setEntNom(entidad.getEntNombreContacto());
		nuevoRegistro.setEntLnot(false);
		nuevoRegistro.setUsuario(UserDetailsUtils.usuarioLogged());		
		Date fechaCorrespondencia = serviceDao.getEntradaDao().obtenerFechaRadicacion();
		nuevoRegistro.setEntFen(fechaCorrespondencia);
		Calidadrepresentante data = (Calidadrepresentante) serviceDao
				.getGenericCommonDao().read(
						Calidadrepresentante.class, new Integer(10).shortValue());
		
		nuevoRegistro.setCalidadRepresentante(data);
		Clasificacion clasificaciones = (Clasificacion) serviceDao
				.getGenericCommonDao().read(
						Clasificacion.class, new Integer(155).shortValue());

		nuevoRegistro.setClasificacion(clasificaciones);
		
		

		Depend dependencias = (Depend) serviceDao
				.getGenericCommonDao()
				.read(Depend.class, new Integer(20).shortValue());
		nuevoRegistro.setEntCel(entidad.getEntEmailcontacto());
		nuevoRegistro.setDependencia(dependencias);
		nuevoRegistro.setEntIde(entidad.getEntNit().toString());
		nuevoRegistro.setEntEnt(entidad.getEntObjetoSocial());
		Medioscorrespondencia medio=(Medioscorrespondencia) serviceDao.getGenericCommonDao().read(Medioscorrespondencia.class, new Integer(26).shortValue());
		nuevoRegistro.setMedio(medio);
		
		return (Integer) serviceDao.getEntradaDao().create(Entrada.class, nuevoRegistro);
		}catch(Exception e){
			FacesUtils.addFacesMessage(
					"Se ha presentado un fallo inesperado exportando el registro a PDF: "
							+ e.getMessage(), FacesMessage.SEVERITY_ERROR);
			e.printStackTrace();
		}
		return null;
	}
	
	

	
	public void displayCurrencyInfoForLocale(Locale locale) {
		System.out.println("Locale: " + locale.getDisplayName());
		Currency currency = Currency.getInstance(locale);
		System.out.println("Currency Code: " + currency.getCurrencyCode());
		System.out.println("Symbol: " + currency.getSymbol());
		System.out.println("Default Fraction Digits: " + currency.getDefaultFractionDigits());
		System.out.println();
	}
	
	
	
	private void exportarPDF(RegistroRadicado registro) {
		Document document = new Document();
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			HttpServletResponse response = (HttpServletResponse) fc
					.getExternalContext().getResponse();
			String contextpath=fc.getExternalContext().getRealPath("/");
			response.reset();
			response.setHeader("Content-disposition", "attachment;filename=Radicado.pdf");
			response.setContentType("application/pdf");
			PdfWriter.getInstance(document, response.getOutputStream());
			document.open();
			Image image = Image.getInstance(contextpath+IMG);
			Font font = new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD);
			Phrase phrase = new Phrase();
			phrase.add(new com.itextpdf.text.Chunk("Se han subido correctamente todos los formatos obligatorios de la entidad: "));
			phrase.add(
				    new com.itextpdf.text.Chunk(registro.getEntidad()
							.getEntObjetoSocial(), font));				
			phrase.add(new com.itextpdf.text.Chunk(" para el per�odo: "));
			phrase.add(
				    new com.itextpdf.text.Chunk(registro.getCortesFormato()
							.getFcrPeriodo() + " "+registro.getCortesFormato().getAno(), font));			
			
			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");			
			
			Phrase phrase1 = new Phrase();
			phrase1.add(new com.itextpdf.text.Chunk("El numero radicado es "));
			phrase1.add(
				    new com.itextpdf.text.Chunk(registro.getEntrada().getEntId().toString(), font));
			phrase1.add(new com.itextpdf.text.Chunk(". Fecha del radicado: "));
			phrase1.add(new com.itextpdf.text.Chunk(formato.format(registro.getFecha())));
								
			PdfPTable table = new PdfPTable(1);			
			
			image.setWidthPercentage(80);
			image.setAlignment(Image.MIDDLE);
			PdfPCell cellOne = new PdfPCell();	
			PdfPCell cellImagen = new PdfPCell();
			cellImagen.addElement(image);			
			cellOne.addElement(phrase);
			PdfPCell cellTwo = new PdfPCell();			
			cellTwo.addElement(phrase1);
			cellImagen.setBorder(Rectangle.NO_BORDER);
			cellOne.setBorder(Rectangle.NO_BORDER);
			cellTwo.setBorder(Rectangle.NO_BORDER);
			
			table.addCell(cellImagen);
			table.addCell(cellOne);
			table.addCell(cellTwo);			
			document.add(table);
		} catch (Exception e) {
			FacesUtils.addFacesMessage(
					"Se ha presentado un fallo inesperado exportando el registro a PDF: "
							+ e.getMessage(), FacesMessage.SEVERITY_ERROR);
			e.printStackTrace();
		}
		document.close();
	}

	/**
	 * Metodo generico utilizado para tomar el objeto relacionado al datos
	 * seleccionado en una combo
	 * 
	 * @param lista
	 * @param seleccionado
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@Deprecated
	private <T> T tomarObjetoListaSeleccionada(List<T> lista, Integer seleccionado) {

		for (Object obj : lista) {

			if (obj instanceof Formatos) {

				Formatos formatoSeleccionado = (Formatos) obj;

				if (seleccionado.compareTo(formatoSeleccionado.getForCodigo()) == 0) {

					return (T) formatoSeleccionado;
				}
			} else if (obj instanceof Entidades) {

				Entidades entSeleccionado = (Entidades) obj;

				if (seleccionado.compareTo(entSeleccionado.getEntCodigo()) == 0) {

					return (T) entSeleccionado;
				}
			}

		}

		return null;
	}

	public List<SelectItem> getListaFormatos() {
		return listaFormatos;
	}

	public void setListaFormatos(List<SelectItem> listaFormatos) {
		this.listaFormatos = listaFormatos;
	}

	public Integer getFormatoSeleccionado() {
		return formatoSeleccionado;
	}

	public void setFormatoSeleccionado(Integer formatoSeleccionado) {
		this.formatoSeleccionado = formatoSeleccionado;
	}

	/**
	 * @return the serviceDao
	 */
	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	/**
	 * @param serviceDao
	 *            the serviceDao to set
	 */
	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	/**
	 * @return the listaEntidades
	 */
	public List<SelectItem> getListaEntidades() {
		return listaEntidades;
	}

	/**
	 * @param listaEntidades
	 *            the listaEntidades to set
	 */
	public void setListaEntidades(List<SelectItem> listaEntidades) {
		this.listaEntidades = listaEntidades;
	}

	/**
	 * @return the entidadSeleccionada
	 */
	public Integer getEntidadSeleccionada() {
		return entidadSeleccionada;
	}

	/**
	 * @param entidadSeleccionada
	 *            the entidadSeleccionada to set
	 */
	public void setEntidadSeleccionada(Integer entidadSeleccionada) {
		this.entidadSeleccionada = entidadSeleccionada;
	}

	/**
	 * @return the errorLog
	 */
	public List<String> getErrorLog() {
		return errorLog;
	}

	/**
	 * @param errorLog
	 *            the errorLog to set
	 */
	public void setErrorLog(List<String> errorLog) {
		this.errorLog = errorLog;
	}

	/**
	 * @return the validador
	 */
	public List<Map<String, Object>> getValidador() {
		return validador;
	}

	/**
	 * @param validador
	 *            the validador to set
	 */
	public void setValidador(List<Map<String, Object>> validador) {
		this.validador = validador;
	}

	/**
	 * @return the tmpFile
	 */
	public String getTmpFile() {
		return tmpFile;
	}

	/**
	 * @param tmpFile
	 *            the tmpFile to set
	 */
	public void setTmpFile(String tmpFile) {
		this.tmpFile = tmpFile;
	}

	/**
	 * @return the formato
	 */
	public Formatos getFormato() {
		return formato;
	}

	/**
	 * @param formato
	 *            the formato to set
	 */
	public void setFormato(Formatos formato) {
		this.formato = formato;
	}

	/**
	 * @return the entidad
	 */
	public Entidades getEntidad() {
		return entidad;
	}

	/**
	 * @param entidad
	 *            the entidad to set
	 */
	public void setEntidad(Entidades entidad) {
		this.entidad = entidad;
	}

	/**
	 * @return the fechaCorte
	 */
	public String getFechaCorte() {
		return fechaCorte;
	}

	/**
	 * @param fechaCorte
	 *            the fechaCorte to set
	 */
	public void setFechaCorte(String fechaCorte) {
		this.fechaCorte = fechaCorte;
	}

	/**
	 * @return the archivosCargados
	 */
	public List<Archivo> getArchivosCargados() {
		return archivosCargados;
	}

	/**
	 * @param archivosCargados
	 *            the archivosCargados to set
	 */
	public void setArchivosCargados(List<Archivo> archivosCargados) {
		this.archivosCargados = archivosCargados;
	}

	/**
	 * @return the erroresCargue
	 */
	public List<ErrorGenerico> getErroresCargue() {
		return erroresCargue;
	}

	/**
	 * @param erroresCargue
	 *            the erroresCargue to set
	 */
	public void setErroresCargue(List<ErrorGenerico> erroresCargue) {
		this.erroresCargue = erroresCargue;
	}

	/**
	 * @return the repoServicio
	 */
	public FileSystemRepositoryService getRepoServicio() {
		return repoServicio;
	}

	/**
	 * @param repoServicio
	 *            the repoServicio to set
	 */
	public void setRepoServicio(FileSystemRepositoryService repoServicio) {
		this.repoServicio = repoServicio;
	}

	/**
	 * @return the listaFechasCorte
	 */
	public List<SelectItem> getListaFechasCorte() {
		return listaFechasCorte;
	}

	/**
	 * @param listaFechasCorte
	 *            the listaFechasCorte to set
	 */
	public void setListaFechasCorte(List<SelectItem> listaFechasCorte) {
		this.listaFechasCorte = listaFechasCorte;
	}

	/**
	 * @return the idFechaCorteSeleccionada
	 */
	public Integer getIdFechaCorteSeleccionada() {
		return idFechaCorteSeleccionada;
	}

	/**
	 * @param idFechaCorteSeleccionada
	 *            the idFechaCorteSeleccionada to set
	 */
	public void setIdFechaCorteSeleccionada(Integer idFechaCorteSeleccionada) {
		this.idFechaCorteSeleccionada = idFechaCorteSeleccionada;
	}

	/**
	 * @return the listaregistroCargueFormato
	 */
	public List<RegistroCargueFormato> getListaregistroCargueFormato() {
		return listaregistroCargueFormato;
	}

	/**
	 * @param listaregistroCargueFormato
	 *            the listaregistroCargueFormato to set
	 */
	public void setListaregistroCargueFormato(List<RegistroCargueFormato> listaregistroCargueFormato) {
		this.listaregistroCargueFormato = listaregistroCargueFormato;
	}

	public String getPeriodoHabilitado() {
		return periodoHabilitado;
	}

	public void setPeriodoHabilitado(String periodoHabilitado) {
		this.periodoHabilitado = periodoHabilitado;
	}

	public Date getFechaVencimientoCargue() {
		return fechaVencimientoCargue;
	}

	public void setFechaVencimientoCargue(Date fechaVencimientoCargue) {
		this.fechaVencimientoCargue = fechaVencimientoCargue;
	}

	public Boolean getFaltaAlgunObligatorio() {
		return faltaAlgunObligatorio;
	}

	public void setFaltaAlgunObligatorio(Boolean faltaAlgunObligatorio) {
		this.faltaAlgunObligatorio = faltaAlgunObligatorio;
	}
	
	

}
