package la.netco.registro.uilayer.beans;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import la.netco.commons.utils.JasperReports;
import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.persistencia.vo.Usuario;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.spring.security.UserDetailsUtils;
import la.netco.core.uilayer.beans.Constants;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.correspondencia.dto.custom.Consecutivo;
import la.netco.persistencia.dto.commons.Clasificacion;
import la.netco.persistencia.dto.commons.ConstantsKeysFire;
import la.netco.persistencia.dto.commons.Elementoregistro;
import la.netco.persistencia.dto.commons.Enlace;
import la.netco.persistencia.dto.commons.EnlaceId;
import la.netco.persistencia.dto.commons.Entrada;
import la.netco.persistencia.dto.commons.Estado;
import la.netco.persistencia.dto.commons.Expediente;
import la.netco.persistencia.dto.commons.ExpedienteEstado;
import la.netco.persistencia.dto.commons.Medioscorrespondencia;
import la.netco.persistencia.dto.commons.Persona;
import la.netco.persistencia.dto.commons.Registro;
import la.netco.persistencia.dto.commons.Registropersona;
import la.netco.persistencia.dto.commons.Salida;
import la.netco.persistencia.dto.commons.Seguimientoentrada;
import la.netco.persistencia.dto.commons.Seguimientoexpediente;
import la.netco.persistencia.dto.commons.Tiposevento;
import la.netco.persistencia.dto.commons.Transicion;

@ManagedBean
@ViewScoped
public class GenerarDocRegistroBean implements Serializable {


	private static final long serialVersionUID = 1L;
	private Registro registroSeleccionado;
	
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;

	private Integer idRegSeleccionado;
	
	private String observacion;

	private Map<Integer, String> idsRegistrosSeleccionadas = new HashMap<Integer, String>();
	
	private List<Registro> registrosSeleccionados = null;
	
	private String enlaceArchivo;
	
	private String certificado;
	
	private static String CREACION_SALIDA ="CREACION DE SALIDA";
	/**
	 * Respuesta verdadera de una entrada a una salida que se creara para un registro 
	 * en el listado de impresion
	 */
	private static boolean RESPUESTA_ENT = true;

	public String getCertificado() {
		return certificado;
	}
	public void setCertificado(String certificado) {
		this.certificado = certificado;
	}


	private static String IMPRESION_REGISTRO_LINEA ="IMPRESION REGISTRO EN LÃ�NEA";

	
	@SuppressWarnings("unchecked")
	public void cargarRegistro() {
		try {
			
			setCertificado(FacesUtils.flashScope(FacesContext.getCurrentInstance()).get("certificado").toString());
			FacesContext facesContext = FacesContext.getCurrentInstance();
			if (idRegSeleccionado == null) {
				facesContext = FacesContext.getCurrentInstance();
				String idRegSeleccionado = facesContext.getExternalContext().getRequestParameterMap().get("idRegSeleccionado");
				if (idRegSeleccionado != null)
					this.idRegSeleccionado = Integer.parseInt(idRegSeleccionado);
			}

			if ((registroSeleccionado == null || registroSeleccionado.getRegId() == null) && (idRegSeleccionado != null)) {
				registroSeleccionado = (Registro) serviceDao.getGenericCommonDao().read(Registro.class,	idRegSeleccionado);
			}
			
			
			if(idRegSeleccionado == null && registrosSeleccionados ==null){
				idsRegistrosSeleccionadas = (Map<Integer, String>) FacesUtils.flashScope(FacesContext.getCurrentInstance()).get("idsRegistrosSeleccionadas");
				if(idsRegistrosSeleccionadas != null && !idsRegistrosSeleccionadas.isEmpty()){
					registrosSeleccionados = new ArrayList<Registro>();
					
					for (Entry<Integer, String> registrosId : idsRegistrosSeleccionadas.entrySet()) {
						if(registrosId.getValue().equals("true")){
							registrosSeleccionados.add( (Registro) serviceDao.getGenericCommonDao().read(Registro.class,registrosId.getKey()));
						}
					}
					
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	//@SecuredAction(keyAction = ExpedientesConstants.DEV_EXP, keyMod = CommonsModKeys.MNUEPAE)
		public String imprimirCertificado() {
			try {
				System.out.println("imprimir certificado");
				List<Object> dataReporte = new ArrayList<Object>();		
				
				if(registroSeleccionado != null){		
					
					dataReporte.add( procesoGenerarDocumento(registroSeleccionado));	
					System.out.println("paso proceso generar documento");
				}else if(registrosSeleccionados != null && !registrosSeleccionados.isEmpty()){	
					for (Registro registro : registrosSeleccionados) {
						dataReporte.add( procesoGenerarDocumento(registro));
					}
				}
				
				Map<String, String> parametros = new HashMap<String, String>();
				
				
				String imagenReporte= FacesUtils.getValorPropiedad("ruta.imagenReporte");
				
				parametros.put("IMAGEN_REPORTE", imagenReporte);
				

		    	File archivoPDF = new File(ConstantsKeysFire.RUTA_ABSOLUTA_TEMP_DOWNLOAD_FILE+"/"+ System.currentTimeMillis()+"Reporte.pdf");
		    	
		    	
		    	
		    	JasperReports.generarReporteDataPDF(ConstantsKeysFire.RUTA_ARCHIVOS_REPORTES_JASPER+ConstantsKeysFire.REPORTE_REGISTRO,archivoPDF , parametros,dataReporte);
		    	
		    	enlaceArchivo = ConstantsKeysFire.RUTA_PUBLICA_TEMP_DOWNLOAD_FILE +"/"+ archivoPDF.getName();
		    	
				
		    	FacesUtils.flashScope(FacesContext.getCurrentInstance()).put("enlaceArchivo", enlaceArchivo);
		    	FacesUtils.flashScope(FacesContext.getCurrentInstance()).put("modalDocumentoVisible", true);
				FacesUtils.addFacesMessageFromBundle(Constants.OPERACION_EXITOSA_ACTUALIZAR_REGISTRO,	FacesMessage.SEVERITY_INFO);
				return "transaccionExitosaImpresion";
			}catch (Exception e) {
				System.out.println("ERROR juanky "+e);
				FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,	FacesMessage.SEVERITY_FATAL);
				
				e.printStackTrace();
				return "";
			}

		}
		
		public String salidaRegistro() {
			try {
				System.out.println("salida registro");	
				if(registroSeleccionado != null){		
					System.out.println("entro en =!null ");
					generaSalidaRegistro(registroSeleccionado.getRegId());
				}else if(registrosSeleccionados != null && !registrosSeleccionados.isEmpty()){	
					for (Registro registro : registrosSeleccionados) {
						generaSalidaRegistro(registro.getRegId());
					}
				}
				
				
				FacesUtils.addFacesMessageFromBundle(Constants.OPERACION_EXITOSA_ACTUALIZAR_REGISTRO,	FacesMessage.SEVERITY_INFO);
				return "transaccionExitosaSalida";
			}catch (Exception e) {
				System.out.println("ERROR juanky "+e);
				FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,	FacesMessage.SEVERITY_FATAL);
				
				e.printStackTrace();
				return "";
			}

		}
		@SuppressWarnings("unchecked")
		public void generaSalidaRegistro(Integer regId) {
			try {
				
				Salida nuevaSalida =  new Salida();
				Registro registro = (Registro) serviceDao.getGenericCommonDao().read(Registro.class, regId);
				Expediente expediente = registro.getExpediente();
				Entrada entrada =expediente.getExpedientecorresponds().iterator().next().getEntrada();
				Usuario usuario = UserDetailsUtils.usuarioLogged();
				Usuario usuarioREL = (Usuario) serviceDao.getGenericCommonDao().read(Usuario.class, Usuario.SOLICITUD_REL);
				
				Map<Integer, Object> params = new HashMap<Integer, Object>();
				params.put(0, expediente.getExpId());
				List<Seguimientoexpediente> seguimientos = (List<Seguimientoexpediente>)serviceDao.getEntradaDao().executeFindPaged(0,1, params, Seguimientoexpediente.NAMED_QUERY_GET_BY_EXPEDIENTE);
				Seguimientoexpediente seguimiento = seguimientos.get(0);
				Transicion transicion = new Transicion();
				transicion.setEstadoByTrsEstfin(seguimiento.getEstadoBySgeEst());
				
				
				//**Actualizando a Impreso , El expediente y su seguimiento del Registro seleccionado
				
				registro.setUsuario(usuario);
				serviceDao.getGenericCommonDao().update(Registro.class, registro);
				
				
				ExpedienteEstado estadoGeneral = (ExpedienteEstado)serviceDao.getGenericCommonDao().read(ExpedienteEstado.class, ExpedienteEstado.REGISTRADO);
				expediente.setEstadoGeneral(estadoGeneral);
				expediente.setUsuarioTemp(usuario);
				serviceDao.getGenericCommonDao().update(Expediente.class, expediente);
				serviceDao.getSeguimientosDao().addSeguimientoExp(expediente, CREACION_SALIDA, "", usuario,usuario, transicion);
				
				//**Actualizando Entrada y su seguimiento del registro seleccionado
				entrada.setUsuario(usuario);
				serviceDao.getGenericCommonDao().update(Entrada.class, entrada);
				serviceDao.getSeguimientosDao().addSeguimientoEntrada(entrada, transicion, CREACION_SALIDA, "", usuario, usuario);
						
				registroSeleccionado =  registro;
				
				
				//**Creacion de Salida
				
				Map<Integer, Object> paramsBusq = new HashMap<Integer, Object>();
				paramsBusq.put(0,  Clasificacion.EXP_CER_REGISTRO);
				paramsBusq.put(1, Tiposevento.ETAPA_INICIAL);
				Short estadoId = null;
				List<Transicion> transicions = (List<Transicion>) serviceDao.getEntradaDao().executeFind(Transicion.class, paramsBusq, Transicion.NAMED_QUERY_BY_CLASIFICACION_TIPO_EVENTO);
				Transicion transicionSal = null;
				if(transicions != null && !transicions.isEmpty()){			
					transicionSal = transicions.get(0);					
					estadoId = transicionSal.getId().getTrsEstfin();				
				}
				Estado estadoFinal = (Estado) serviceDao.getGenericCommonDao().read(Estado.class, estadoId);
				
				nuevaSalida.setSalFsa(new Date(System.currentTimeMillis()));
				nuevaSalida.setSalAsu(entrada.getEntAsu());
				nuevaSalida.setSalLren(RESPUESTA_ENT);
				nuevaSalida.setMedio((Medioscorrespondencia) serviceDao.getGenericCommonDao().read(Medioscorrespondencia.class, Medioscorrespondencia.MEDIO_SERVIENTREGA));
				nuevaSalida.setTipospersona(entrada.getTipoPersona());
				nuevaSalida.setTiposdocumento(entrada.getTiposdocumento());
				nuevaSalida.setSalNdo(entrada.getEntNdo());
				nuevaSalida.setSalNom(entrada.getEntNom());
				nuevaSalida.setSalPap(entrada.getEntPap());
				nuevaSalida.setSalSap(entrada.getEntSap());
				nuevaSalida.setSalCar(entrada.getEntCar());
				nuevaSalida.setSalDir(entrada.getEntDir());
				nuevaSalida.setLugar(entrada.getLugar());
				nuevaSalida.setClasificacion((Clasificacion) serviceDao.getGenericCommonDao().read(Clasificacion.class, Clasificacion.EXP_CER_REGISTRO));
				nuevaSalida.setDepend(entrada.getDependencia());
				nuevaSalida.setSalTel(entrada.getEntTel());
				nuevaSalida.setSalFax(entrada.getEntFax());
				nuevaSalida.setSalCel(entrada.getEntCel());
				nuevaSalida.setUsuarioBySalUsr(usuario);
				nuevaSalida.setSalFol(ConstantsKeysFire.FOLIOS_SALIDA_DEF);
				nuevaSalida.setSalCano(Calendar.getInstance().get(Calendar.YEAR));
				nuevaSalida.setEstado(estadoFinal);
				if (entrada.getMedio().getMedId() == Short.parseShort(Medioscorrespondencia.MEDIO_REGISTRO_LINEA)) {
					nuevaSalida.setSalLent(ConstantsKeysFire.ENTREGADA);
					nuevaSalida.setUsuarioBySalUen(usuario);
					nuevaSalida.setSalPen(ConstantsKeysFire.USUARIO_PEN);
					nuevaSalida.setSalFen(new Date(System.currentTimeMillis()));
					nuevaSalida.setSalHen(new Date(System.currentTimeMillis()));
					nuevaSalida.setSalEnm(Integer.parseInt(Medioscorrespondencia.MEDIO_REGISTRO_LINEA));
				}
			
				Consecutivo numCorrespondencia = serviceDao.getEntradaDao().obtenerConsecutivo(entrada.getDependencia().getDepCod(), Consecutivo.TIPO_SALIDA);
				nuevaSalida.setSalCnro(numCorrespondencia.getConsecutivo());
				nuevaSalida.setSalNsa(numCorrespondencia.getConsecutivoCompleto());		
				Integer  idSalida = (Integer) serviceDao.getGenericCommonDao().create(Salida.class, nuevaSalida);

				Salida salidaReg = (Salida)serviceDao.getGenericCommonDao().read(Salida.class, idSalida);
				serviceDao.getSeguimientosDao().addSeguimientoSalida(salidaReg, transicionSal, Seguimientoentrada.SEGUIMIENTO_CREACION, "",usuarioREL ,usuario);
				
				if (entrada.getMedio().getMedId() == Short.parseShort(Medioscorrespondencia.MEDIO_REGISTRO_LINEA))
				{
					serviceDao.getSeguimientosDao().addSeguimientoSalida(salidaReg, transicionSal,ConstantsKeysFire.ENTREGADO_OBS,salidaReg.getSalPen(),usuarioREL , usuario);
				}
				
				System.out.println("***Exito-Salida No: " + idSalida);
				
				//**Creando Enlace
				
				Enlace nuevoEnlace =  new Enlace();
				EnlaceId nuevoEnlaceId =  new EnlaceId();
				nuevoEnlaceId.setEnlEnt(entrada.getEntId());
				nuevoEnlaceId.setEnlSal(salidaReg.getSalId());
				nuevoEnlaceId.setEnlOrg(Enlace.ORIGEN_SALIDA);
				nuevoEnlace.setId(nuevoEnlaceId);
				nuevoEnlace.setEntrada(entrada);
				nuevoEnlace.setSalida(salidaReg);
				serviceDao.getGenericCommonDao().create(Enlace.class, nuevoEnlace);
				// incluir mensaje
				
				
				
				
		

			} catch (Exception e) {
				System.out.println("Error: " + e.toString());
				System.out.println("Linea:" +  e.getLocalizedMessage());
				System.out.println("Mensaje: " +  e.getMessage());
				e.printStackTrace();
				
			}
		}

	
	
	//@SecuredAction(keyAction = ExpedientesConstants.DEV_EXP, keyMod = CommonsModKeys.MNUEPAE)
	public String imprimirRegistro() {
		try {
			System.out.println("imprimir registro");
			List<Object> dataReporte = new ArrayList<Object>();		
			
			if(registroSeleccionado != null){		
				System.out.println("entro en =!null ");
				dataReporte.add( procesoGenerarDocumento(registroSeleccionado));	
				System.out.println("paso proceso generar documento");
			}else if(registrosSeleccionados != null && !registrosSeleccionados.isEmpty()){	
				for (Registro registro : registrosSeleccionados) {
					dataReporte.add( procesoGenerarDocumento(registro));
				}
			}
			
			Map<String, String> parametros = new HashMap<String, String>();
			
			
			String imagenReporte= FacesUtils.getValorPropiedad("ruta.imagenReporte");
			
			parametros.put("IMAGEN_REPORTE", imagenReporte);
			

	    	File archivoPDF = new File(ConstantsKeysFire.RUTA_ABSOLUTA_TEMP_DOWNLOAD_FILE+"/"+ System.currentTimeMillis()+"Reporte.pdf");
	    	
	    	
	    	
	    	JasperReports.generarReporteDataPDF(ConstantsKeysFire.RUTA_ARCHIVOS_REPORTES_JASPER+ConstantsKeysFire.REPORTE_REGISTRO,archivoPDF , parametros,dataReporte);
	    	
	    	enlaceArchivo = ConstantsKeysFire.RUTA_PUBLICA_TEMP_DOWNLOAD_FILE +"/"+ archivoPDF.getName();
	    	
			
	    	FacesUtils.flashScope(FacesContext.getCurrentInstance()).put("enlaceArchivo", enlaceArchivo);
	    	FacesUtils.flashScope(FacesContext.getCurrentInstance()).put("modalDocumentoVisible", true);
			FacesUtils.addFacesMessageFromBundle(Constants.OPERACION_EXITOSA_ACTUALIZAR_REGISTRO,	FacesMessage.SEVERITY_INFO);
			return "transaccionExitosaImpresion";
		}catch (Exception e) {
			System.out.println("ERROR juanky "+e);
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,	FacesMessage.SEVERITY_FATAL);
			
			e.printStackTrace();
			return "";
		}

	}
	
	

	@SuppressWarnings("unchecked")
	private RegistroResumen procesoGenerarDocumento(Registro registroSel){
		
		registroSel = (Registro) serviceDao.getGenericCommonDao().read(Registro.class, registroSel.getRegId());
		RegistroResumen registro = new RegistroResumen();

		Expediente expediente = registroSel.getExpediente();
		Persona solicitante = expediente.getPersona();
		Entrada entrada =expediente.getExpedientecorresponds().iterator().next().getEntrada();
		
		registro.setRegCod(registroSel.getRegCod());
		registro.setRegFec(registroSel.getRegFec());
		registro.setCreNom(registroSel.getClaseregistro().getCreNom());
		registro.setRegTor(registroSel.getRegTor());
		registro.setRegFcr(registroSel.getRegFcr());
		if(registroSel.getRegDes()==null){
			registro.setRegDes("");
		}else{
			registro.setRegDes(registroSel.getRegDes());
		}
		registro.setRegObs(registroSel.getRegObs());
		registro.setNombreCompleto(solicitante.getNombreCompleto());
		registro.setPerDoc(solicitante.getPerDoc());
		registro.setNacionalidad(solicitante.getPaises().getPaiNom());
		registro.setPerDir(solicitante.getPerDir());
		registro.setPerTl1(solicitante.getPerTl1());
		registro.setCiudad(solicitante.getLugarByPerLug().getLugCiu());
		registro.setPerCe1(solicitante.getPerCe1());
		
		
		registro.setMedNom(entrada.getMedio().getMedNom());

		registro.setEntNen(entrada.getEntNen());
		registro.setCalNom(expediente.getCalidadrepresentante().getCalNom());
			
		Set<Elementoregistro> elementosRegistro = registroSel.getElementoregistros();
		List<ElementoReg> elementos = new ArrayList<ElementoReg>();
		
		for (Elementoregistro elementoregistro : elementosRegistro) {					
			ElementoReg elemento = new ElementoReg();
			elemento.setEleNom(elementoregistro.getElemento().getEleNom());
			elemento.setProNom(elementoregistro.getElemento().getPropiedad().getProNom());
			elementos.add(elemento);
		}
		
		
		registro.setElementos(elementos);
		Set<Registropersona> registroPersonas = registroSel.getRegistropersonas();
		List<PersonaRegistro> personas = new ArrayList<PersonaRegistro>();
		try{
			for (Registropersona registropersona : registroPersonas) {
				Persona persona = registropersona.getPersona();
				PersonaRegistro personaReg = new PersonaRegistro();
				personaReg.setNombreCompleto(persona.getNombreCompleto());
				personaReg.setNoDocumento(persona.getPerDoc());
				
				if(persona.getTiposdocumento() != null)
					personaReg.setTipoDocumento(persona.getTiposdocumento().getTdoNom());
				
				if(persona.getPaises() != null)						
					personaReg.setNacionalidad(persona.getPaises().getPaiNom());
				
				if(persona.getLugarByPerLug() != null)
					personaReg.setCiudad(persona.getLugarByPerLug().getLugCiu());
				
				personaReg.setDireccion(persona.getPerDir());
				
				if(registropersona.getTipospersona() != null)
					personaReg.setTipoPersona(registropersona.getTipospersona().getTpeId());
				
				if(registropersona.getTiporegistropersona() != null)
					personaReg.setTipoPersonaRegistro(registropersona.getTiporegistropersona().getTrpNom());
				
				personas.add(personaReg);
			}
		}catch(Exception e){
			System.out.println("error proceso "+ e);
		}
		

		registro.setPersonas(personas);
		
		
		//Expediente expediente = registro.getExpediente();

		ExpedienteEstado estadoGeneral = (ExpedienteEstado)serviceDao.getGenericCommonDao().read(ExpedienteEstado.class, ExpedienteEstado.IMPRESO);

		expediente.setEstadoGeneral(estadoGeneral);
		serviceDao.getGenericCommonDao().update(Expediente.class, expediente);
		
		
		Map<Integer, Object> params = new HashMap<Integer, Object>();
		params.put(0, expediente.getExpId());
		List<Seguimientoexpediente> seguimientos = (List<Seguimientoexpediente>)serviceDao.getEntradaDao().executeFindPaged(0,1, params, Seguimientoexpediente.NAMED_QUERY_GET_BY_EXPEDIENTE);
		Seguimientoexpediente seguimiento = seguimientos.get(0);

		
		Transicion transicion = new Transicion();
		transicion.setEstadoByTrsEstfin(seguimiento.getEstadoBySgeEst());
		transicion.setEstadoByTrsEstini(seguimiento.getEstadoBySgeEstini());
		transicion.setEvento(seguimiento.getEvento());
		

		Usuario usuario = UserDetailsUtils.usuarioLogged();
		serviceDao.getSeguimientosDao().addSeguimientoExp(expediente, IMPRESION_REGISTRO_LINEA ,this.observacion, usuario, seguimiento.getSgeUsr(), transicion);	
		serviceDao.getSeguimientosDao().addSeguimientoEntrada(entrada, transicion, IMPRESION_REGISTRO_LINEA, this.observacion, usuario, seguimiento.getSgeUsr());
		
		
		return registro;
	
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

	public String getObservacion() {
		return observacion;
	}


	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Map<Integer, String> getIdsRegistrosSeleccionadas() {
		return idsRegistrosSeleccionadas;
	}

	public void setIdsRegistrosSeleccionadas(
			Map<Integer, String> idsRegistrosSeleccionadas) {
		this.idsRegistrosSeleccionadas = idsRegistrosSeleccionadas;
	}

	public List<Registro> getRegistrosSeleccionados() {
		return registrosSeleccionados;
	}

	public void setRegistrosSeleccionados(List<Registro> registrosSeleccionados) {
		this.registrosSeleccionados = registrosSeleccionados;
	}


	public String getEnlaceArchivo() {
		return enlaceArchivo;
	}


	public void setEnlaceArchivo(String enlaceArchivo) {
		this.enlaceArchivo = enlaceArchivo;
	}
	
}
