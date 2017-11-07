package la.netco.commons.utils;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;

public class JasperReports {

	/*Incluir nuevo generador de pdfs*/
	
	//
	//Rellenar el informe con datos
	public static void generarReporteDatosPDF(InputStream inputJasper, File archivoDestino,   Map<String, Object> parametros ){
		Logger logg = Logger.getLogger("JasperReports");
		
		 try {			 
			
			 List<String> lista = new ArrayList<String>();
				lista.add("valor");
				
				JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(lista);
				JasperPrint jasperPrint = JasperFillManager.fillReport(inputJasper, parametros,ds);
				
				JRPdfExporter exporter = new JRPdfExporter();
				exporter.setParameter(JRExporterParameter.JASPER_PRINT,jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE, archivoDestino);
	            exporter.exportReport();

		} catch (JRException e) {
			logg.info("EXCEPTION  generarReporteDataPDF" );
			logg.info("EXCEPTION  generarReporteDataPDF Detalle:", e);
			e.printStackTrace();
		}
	}
	
	
	
	
	public static void generarReportePDF(String pathArchivoJasper, File archivoDestino,   Map<String, Object> parametros ){
		 try {
			 
			List<String> lista = new ArrayList<String>();
			lista.add("valor");
			
			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(lista);
			JasperPrint jasperPrint = JasperFillManager.fillReport(pathArchivoJasper, parametros,ds);
			
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT,jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE, archivoDestino);
            exporter.exportReport();

		} catch (JRException e) {
			e.printStackTrace();
		}
		 catch (Exception e){
			 e.printStackTrace();
		 }
		
		
	}
	
	public static void generarReporteDataPDF(String pathArchivoJasper, File archivoDestino,   Map<String, String> parametros ,  List<Object> data){
		 try {			 
			
			JRBeanCollectionDataSource ds3 = new JRBeanCollectionDataSource(data);			
			JasperPrint jasperPrint = JasperFillManager.fillReport(pathArchivoJasper, parametros,ds3);
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT,jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE, archivoDestino);
			exporter.exportReport();

		} catch (JRException e) {
			e.printStackTrace();
		}
	}
	
//	public static void generarReportePDFConnection(String pathArchivoJasper, File archivoDestino,   Map<String, String> parametros ){
//		 try {
//			
//			ServiceDao serviceDao = (ServiceDao) SpringApplicationContext.getBean("serviceDao");
//			Connection conn = serviceDao.getGenericCommonDao().connectionFromHibernate();
//		
//			JasperPrint jasperPrint = JasperFillManager.fillReport(pathArchivoJasper, parametros,conn);
//			
//			JRPdfExporter exporter = new JRPdfExporter();
//			exporter.setParameter(JRExporterParameter.JASPER_PRINT,jasperPrint);
//			exporter.setParameter(JRExporterParameter.OUTPUT_FILE, archivoDestino);
//			exporter.exportReport();
//
//		} catch (JRException e) {
//			e.printStackTrace();
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

}
