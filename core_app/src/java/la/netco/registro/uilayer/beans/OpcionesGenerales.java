package la.netco.registro.uilayer.beans;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;


//Clase en uso
// ediaz

@Entity
@Table(name = "OPCIONES_GENERALES", schema = Schemas.RLINEA_SCHEMA)
public class OpcionesGenerales implements Serializable {

	
	private static final long serialVersionUID = 1L;
	private Integer opcgenId;
	private String opcgenRutaArc;
	private String idmedioentrega;
	private String idclasificacion;
	private String iddependencia;
	private String idusuario;
	private String idtramite;
	private String idtipexpediente;
	private String pathRepositorio;
	private Boolean activoInedita;
	private Boolean activoEditada;
	private Boolean activoArtistica;
	private Boolean activoMusical;
	private Boolean activoAudiovisual;
	private Boolean activoSoftware;
	private Boolean activoActos;
	private Boolean activoFonogramas;
	private Boolean activoFirmasd;
	private String sizeAyuda;
	private Integer numsolicitudes;
	private Long filesizeUploads;
	private Short annoConsultas;
	private Boolean busquedaInedita;
	private Boolean busquedaEditada;
	private Boolean busquedaArtistica;
	private Boolean busquedaMusical;
	private Boolean busquedaAudiovisual;
	private Boolean busquedaSoftware;
	private Boolean busquedaActos;
	private Boolean busquedaFonogramas;
	private String smtp;
	private String mailremite;
	private String automatizacion;
	private String jefeOficinaRegistro;
	private String rutaImagenReporte;
	//ediaz
	private String rutaImagenFirma;

	public OpcionesGenerales() {
	}

	public OpcionesGenerales(Integer opcgenId) {
		this.opcgenId = opcgenId;
	}

	public OpcionesGenerales(Integer opcgenId, String opcgenRutaArc, String idmedioentrega, String idclasificacion, String iddependencia,
			String idusuario, String idtramite, String idtipexpediente, String pathRepositorio, Boolean activoInedita, Boolean activoEditada,
			Boolean activoArtistica, Boolean activoMusical, Boolean activoAudiovisual, Boolean activoSoftware, Boolean activoActos,
			Boolean activoFonogramas, Boolean activoFirmasd, String sizeAyuda, Integer numsolicitudes, Long filesizeUploads, Short annoConsultas,
			Boolean busquedaInedita, Boolean busquedaEditada, Boolean busquedaArtistica, Boolean busquedaMusical, Boolean busquedaAudiovisual,
			Boolean busquedaSoftware, Boolean busquedaActos, Boolean busquedaFonogramas, String smtp, String mailremite, String automatizacion,String jefeOficinaRegistro,String rutaImagenReporte, String rutaImagenFirma) {
		this.opcgenId = opcgenId;
		this.opcgenRutaArc = opcgenRutaArc;
		this.idmedioentrega = idmedioentrega;
		this.idclasificacion = idclasificacion;
		this.iddependencia = iddependencia;
		this.idusuario = idusuario;
		this.idtramite = idtramite;
		this.idtipexpediente = idtipexpediente;
		this.pathRepositorio = pathRepositorio;
		this.activoInedita = activoInedita;
		this.activoEditada = activoEditada;
		this.activoArtistica = activoArtistica;
		this.activoMusical = activoMusical;
		this.activoAudiovisual = activoAudiovisual;
		this.activoSoftware = activoSoftware;
		this.activoActos = activoActos;
		this.activoFonogramas = activoFonogramas;
		this.activoFirmasd = activoFirmasd;
		this.sizeAyuda = sizeAyuda;
		this.numsolicitudes = numsolicitudes;
		this.filesizeUploads = filesizeUploads;
		this.annoConsultas = annoConsultas;
		this.busquedaInedita = busquedaInedita;
		this.busquedaEditada = busquedaEditada;
		this.busquedaArtistica = busquedaArtistica;
		this.busquedaMusical = busquedaMusical;
		this.busquedaAudiovisual = busquedaAudiovisual;
		this.busquedaSoftware = busquedaSoftware;
		this.busquedaActos = busquedaActos;
		this.busquedaFonogramas = busquedaFonogramas;
		this.smtp = smtp;
		this.mailremite = mailremite;
		this.automatizacion = automatizacion;
		this.jefeOficinaRegistro =  jefeOficinaRegistro;
		this.rutaImagenReporte =  rutaImagenReporte;
		this.rutaImagenFirma = rutaImagenFirma;
	}

	@Id
	@Column(name = "OPCGEN_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Integer getOpcgenId() {
		return this.opcgenId;
	}

	public void setOpcgenId(Integer opcgenId) {
		this.opcgenId = opcgenId;
	}

	@Column(name = "OPCGEN_RUTA_ARC", length = 250)
	public String getOpcgenRutaArc() {
		return this.opcgenRutaArc;
	}

	public void setOpcgenRutaArc(String opcgenRutaArc) {
		this.opcgenRutaArc = opcgenRutaArc;
	}

	@Column(name = "IDMEDIOENTREGA", length = 5)
	public String getIdmedioentrega() {
		return this.idmedioentrega;
	}

	public void setIdmedioentrega(String idmedioentrega) {
		this.idmedioentrega = idmedioentrega;
	}

	@Column(name = "IDCLASIFICACION", length = 5)
	public String getIdclasificacion() {
		return this.idclasificacion;
	}

	public void setIdclasificacion(String idclasificacion) {
		this.idclasificacion = idclasificacion;
	}

	@Column(name = "IDDEPENDENCIA", length = 5)
	public String getIddependencia() {
		return this.iddependencia;
	}

	public void setIddependencia(String iddependencia) {
		this.iddependencia = iddependencia;
	}

	@Column(name = "IDUSUARIO", length = 5)
	public String getIdusuario() {
		return this.idusuario;
	}

	public void setIdusuario(String idusuario) {
		this.idusuario = idusuario;
	}

	@Column(name = "IDTRAMITE", length = 5)
	public String getIdtramite() {
		return this.idtramite;
	}

	public void setIdtramite(String idtramite) {
		this.idtramite = idtramite;
	}

	@Column(name = "IDTIPEXPEDIENTE", length = 5)
	public String getIdtipexpediente() {
		return this.idtipexpediente;
	}

	public void setIdtipexpediente(String idtipexpediente) {
		this.idtipexpediente = idtipexpediente;
	}

	@Column(name = "PATH_REPOSITORIO")
	public String getPathRepositorio() {
		return this.pathRepositorio;
	}

	public void setPathRepositorio(String pathRepositorio) {
		this.pathRepositorio = pathRepositorio;
	}

	@Column(name = "ACTIVO_INEDITA")
	public Boolean getActivoInedita() {
		return this.activoInedita;
	}

	public void setActivoInedita(Boolean activoInedita) {
		this.activoInedita = activoInedita;
	}

	@Column(name = "ACTIVO_EDITADA")
	public Boolean getActivoEditada() {
		return this.activoEditada;
	}

	public void setActivoEditada(Boolean activoEditada) {
		this.activoEditada = activoEditada;
	}

	@Column(name = "ACTIVO_ARTISTICA")
	public Boolean getActivoArtistica() {
		return this.activoArtistica;
	}

	public void setActivoArtistica(Boolean activoArtistica) {
		this.activoArtistica = activoArtistica;
	}

	@Column(name = "ACTIVO_MUSICAL")
	public Boolean getActivoMusical() {
		return this.activoMusical;
	}

	public void setActivoMusical(Boolean activoMusical) {
		this.activoMusical = activoMusical;
	}

	@Column(name = "ACTIVO_AUDIOVISUAL")
	public Boolean getActivoAudiovisual() {
		return this.activoAudiovisual;
	}

	public void setActivoAudiovisual(Boolean activoAudiovisual) {
		this.activoAudiovisual = activoAudiovisual;
	}

	@Column(name = "ACTIVO_SOFTWARE")
	public Boolean getActivoSoftware() {
		return this.activoSoftware;
	}

	public void setActivoSoftware(Boolean activoSoftware) {
		this.activoSoftware = activoSoftware;
	}

	@Column(name = "ACTIVO_ACTOS")
	public Boolean getActivoActos() {
		return this.activoActos;
	}

	public void setActivoActos(Boolean activoActos) {
		this.activoActos = activoActos;
	}

	@Column(name = "ACTIVO_FONOGRAMAS")
	public Boolean getActivoFonogramas() {
		return this.activoFonogramas;
	}

	public void setActivoFonogramas(Boolean activoFonogramas) {
		this.activoFonogramas = activoFonogramas;
	}

	@Column(name = "ACTIVO_FIRMASD")
	public Boolean getActivoFirmasd() {
		return this.activoFirmasd;
	}

	public void setActivoFirmasd(Boolean activoFirmasd) {
		this.activoFirmasd = activoFirmasd;
	}

	@Column(name = "SIZE_AYUDA", length = 30)
	public String getSizeAyuda() {
		return this.sizeAyuda;
	}

	public void setSizeAyuda(String sizeAyuda) {
		this.sizeAyuda = sizeAyuda;
	}

	@Column(name = "NUMSOLICITUDES")
	public Integer getNumsolicitudes() {
		return this.numsolicitudes;
	}

	public void setNumsolicitudes(Integer numsolicitudes) {
		this.numsolicitudes = numsolicitudes;
	}

	@Column(name = "FILESIZE_UPLOADS", precision = 18, scale = 0)
	public Long getFilesizeUploads() {
		return this.filesizeUploads;
	}

	public void setFilesizeUploads(Long filesizeUploads) {
		this.filesizeUploads = filesizeUploads;
	}

	@Column(name = "ANNO_CONSULTAS", precision = 4, scale = 0)
	public Short getAnnoConsultas() {
		return this.annoConsultas;
	}

	public void setAnnoConsultas(Short annoConsultas) {
		this.annoConsultas = annoConsultas;
	}

	@Column(name = "BUSQUEDA_INEDITA")
	public Boolean getBusquedaInedita() {
		return this.busquedaInedita;
	}

	public void setBusquedaInedita(Boolean busquedaInedita) {
		this.busquedaInedita = busquedaInedita;
	}

	@Column(name = "BUSQUEDA_EDITADA")
	public Boolean getBusquedaEditada() {
		return this.busquedaEditada;
	}

	public void setBusquedaEditada(Boolean busquedaEditada) {
		this.busquedaEditada = busquedaEditada;
	}

	@Column(name = "BUSQUEDA_ARTISTICA")
	public Boolean getBusquedaArtistica() {
		return this.busquedaArtistica;
	}

	public void setBusquedaArtistica(Boolean busquedaArtistica) {
		this.busquedaArtistica = busquedaArtistica;
	}

	@Column(name = "BUSQUEDA_MUSICAL")
	public Boolean getBusquedaMusical() {
		return this.busquedaMusical;
	}

	public void setBusquedaMusical(Boolean busquedaMusical) {
		this.busquedaMusical = busquedaMusical;
	}

	@Column(name = "BUSQUEDA_AUDIOVISUAL")
	public Boolean getBusquedaAudiovisual() {
		return this.busquedaAudiovisual;
	}

	public void setBusquedaAudiovisual(Boolean busquedaAudiovisual) {
		this.busquedaAudiovisual = busquedaAudiovisual;
	}

	@Column(name = "BUSQUEDA_SOFTWARE")
	public Boolean getBusquedaSoftware() {
		return this.busquedaSoftware;
	}

	public void setBusquedaSoftware(Boolean busquedaSoftware) {
		this.busquedaSoftware = busquedaSoftware;
	}

	@Column(name = "BUSQUEDA_ACTOS")
	public Boolean getBusquedaActos() {
		return this.busquedaActos;
	}

	public void setBusquedaActos(Boolean busquedaActos) {
		this.busquedaActos = busquedaActos;
	}

	@Column(name = "BUSQUEDA_FONOGRAMAS")
	public Boolean getBusquedaFonogramas() {
		return this.busquedaFonogramas;
	}

	public void setBusquedaFonogramas(Boolean busquedaFonogramas) {
		this.busquedaFonogramas = busquedaFonogramas;
	}

	@Column(name = "SMTP", length = 50)
	public String getSmtp() {
		return this.smtp;
	}

	public void setSmtp(String smtp) {
		this.smtp = smtp;
	}

	@Column(name = "MAILREMITE", length = 100)
	public String getMailremite() {
		return this.mailremite;
	}

	public void setMailremite(String mailremite) {
		this.mailremite = mailremite;
	}

	@Column(name = "AUTOMATIZACION", length = 40)
	public String getAutomatizacion() {
		return this.automatizacion;
	}

	public void setAutomatizacion(String automatizacion) {
		this.automatizacion = automatizacion;
	}

	@Column(name = "JEFEOFICINAREGISTRO", length = 250)
	public String getJefeOficinaRegistro() {
		return jefeOficinaRegistro;
	}

	public void setJefeOficinaRegistro(String jefeOficinaRegistro) {
		this.jefeOficinaRegistro = jefeOficinaRegistro;
	}

	@Column(name = "RUTAIMAGENREPORTE", length = 250)
	public String getRutaImagenReporte() {
		return rutaImagenReporte;
	}

	public void setRutaImagenReporte(String rutaImagenReporte) {
		this.rutaImagenReporte = rutaImagenReporte;
	}
	
	@Column(name="RUTAIMAGENFIRMA", length = 250)
	public String getRutaImagenFirma(){
		return rutaImagenFirma;
	}
	
	public void setRutaImagenFirma(String rutaImagenFirma){
		this.rutaImagenFirma = rutaImagenFirma;
	}
	
	

}
