package la.netco.correspondencia.uilayer.beans;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import la.netco.commons.utils.JasperReports;
import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.persistencia.dto.commons.Configuracion;
import la.netco.persistencia.dto.commons.ConstantsKeysFire;
import la.netco.persistencia.dto.commons.Entrada;
import la.netco.persistencia.dto.commons.Salida;

@ManagedBean
@ViewScoped
public class GenerarEtiquetaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;
	private String enlaceEtiqueta;
	private boolean modalVisible;
	
	public void generarEtiquetaEntrada(Entrada entrada){
		Configuracion conf = (Configuracion) serviceDao.getGenericCommonDao().executeFind(Configuracion.NAMED_QUERY_GET_ALL_CONF).get(0);

		String radicado = entrada.getEntNen();
		String fecha = new SimpleDateFormat("dd-MMM-yyyy hh:mm a").format(entrada.getEntFen());
		String dependencia = entrada.getDependencia().getDepNom();
		String telefono	= conf.getPbx();
		String folios = "0";
		if(entrada.getEntFol() != null)
			folios = entrada.getEntFol().toString();
		
    	Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("radicado", radicado);
		parametros.put("fecha", fecha);
		parametros.put("dependencia", dependencia);
		parametros.put("telefono", telefono);
		parametros.put("folios",folios);
		
    	File archivoPDF = new File(ConstantsKeysFire.RUTA_ABSOLUTA_TEMP_DOWNLOAD_FILE+"/"+ System.currentTimeMillis()+"Reporte.pdf");
    	
    	JasperReports.generarReportePDF(ConstantsKeysFire.RUTA_ARCHIVOS_REPORTES_JASPER+ConstantsKeysFire.REPORTE_ETIQUETA_ENTRADA,archivoPDF , parametros);
    	
    	enlaceEtiqueta = ConstantsKeysFire.RUTA_PUBLICA_TEMP_DOWNLOAD_FILE +"/"+ archivoPDF.getName();
	
	}

	
	public void cargarEtiquetaEntrada() {
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			Boolean generarEtiqueta = (Boolean) FacesUtils.flashScope(facesContext).get("generarEtiqueta");
			System.out.println(" flash generarEtiqueta" + generarEtiqueta );
			if (generarEtiqueta != null && generarEtiqueta.equals(Boolean.TRUE)) {
				Integer idRegGenerado =  (Integer) FacesUtils.flashScope(facesContext).get("idEntradaGenerado");
				if (idRegGenerado != null) {
					System.out.println("idRegGenerado "  + idRegGenerado);
					Entrada entradaGenerada = (Entrada) serviceDao.getEntradaDao().read(Entrada.class, idRegGenerado);
					generarEtiquetaEntrada(entradaGenerada);
					modalVisible= true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void generarEtiquetaSalida(Salida salida){
		Configuracion conf = (Configuracion) serviceDao.getGenericCommonDao().executeFind(Configuracion.NAMED_QUERY_GET_ALL_CONF).get(0);

		String radicado = salida.getSalNsa();
		String fecha = new SimpleDateFormat("dd-MMM-yyyy hh:mm a").format(salida.getSalFsa());
		String dependencia = salida.getDepend().getDepNom();
		String telefono	= conf.getPbx();
		String folios = "0";
		if(salida.getSalFol() != null)
			folios = salida.getSalFol().toString();
		
    	Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("radicado", radicado);
		parametros.put("fecha", fecha);
		parametros.put("dependencia", dependencia);
		parametros.put("telefono", telefono);
		parametros.put("folios",folios);
		
    	File archivoPDF = new File(ConstantsKeysFire.RUTA_ABSOLUTA_TEMP_DOWNLOAD_FILE+"/"+ System.currentTimeMillis()+"Reporte.pdf");
    	
    	JasperReports.generarReportePDF(ConstantsKeysFire.RUTA_ARCHIVOS_REPORTES_JASPER+ConstantsKeysFire.REPORTE_ETIQUETA_SALIDA,archivoPDF , parametros);
    	
    	enlaceEtiqueta = ConstantsKeysFire.RUTA_PUBLICA_TEMP_DOWNLOAD_FILE +"/"+ archivoPDF.getName();
	
	}

	
	public void cargarEtiquetaSalida() {
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			Boolean generarEtiqueta = (Boolean) FacesUtils.flashScope(facesContext).get("generarEtiqueta");
			if (generarEtiqueta != null && generarEtiqueta.equals(Boolean.TRUE)) {
				Integer idSalidaGenerado =  (Integer) FacesUtils.flashScope(facesContext).get("idSalidaGenerado");
				if (idSalidaGenerado != null) {
					Entrada entradaGenerada = (Entrada) serviceDao.getEntradaDao().read(Entrada.class, idSalidaGenerado);
					generarEtiquetaEntrada(entradaGenerada);
					modalVisible= true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void generarEtiquetaSalidaDestinatario(Salida salida){
	
		String radicado = salida.getSalNsa();
		String destinatario = salida.getSalNom()+ salida.getSalPap() + salida.getSalSap();
		String direccion = salida.getSalDir();
		String telefono	= salida.getSalTel();
		String ciudad = null;
		if(salida.getLugar() != null)
			ciudad = salida.getLugar().getLugCiu();
		
    	Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("radicado", radicado);
		parametros.put("destinatario", destinatario);
		parametros.put("direccion", direccion);
		parametros.put("telefono", telefono);
		parametros.put("ciudad",ciudad);
		
    	File archivoPDF = new File(ConstantsKeysFire.RUTA_ABSOLUTA_TEMP_DOWNLOAD_FILE+"/"+ System.currentTimeMillis()+"Reporte.pdf");
    	
    	JasperReports.generarReportePDF(ConstantsKeysFire.RUTA_ARCHIVOS_REPORTES_JASPER+ConstantsKeysFire.REPORTE_ETIQUETA_SALIDA_DESTINATARIO,archivoPDF , parametros);
    	
    	enlaceEtiqueta = ConstantsKeysFire.RUTA_PUBLICA_TEMP_DOWNLOAD_FILE +"/"+ archivoPDF.getName();
	
	}
	
	public ServiceDao getServiceDao() {
		return serviceDao;
	}
	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	public boolean isModalVisible() {
		return modalVisible;
	}


	public void setModalVisible(boolean modalVisible) {
		this.modalVisible = modalVisible;
	}


	public String getEnlaceEtiqueta() {
		return enlaceEtiqueta;
	}


	public void setEnlaceEtiqueta(String enlaceEtiqueta) {
		this.enlaceEtiqueta = enlaceEtiqueta;
	}

}
