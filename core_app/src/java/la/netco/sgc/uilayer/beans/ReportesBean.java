/**
 * 
 */
package la.netco.sgc.uilayer.beans;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.sgc.persistence.dto.Entidades;
import la.netco.sgc.persistence.dto.Formatos;
import la.netco.sgc.uilayer.beans.util.FormatosEnum;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.DefaultFontMapper;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author cguzman
 *
 */
/**
 * @author cpineros
 * 
 */
@ManagedBean(name = "reportesBean")
@RequestScoped
public class ReportesBean implements Serializable {

	private static final long serialVersionUID = 6757074746821543344L;

	private String ano;

	private String trimestre;

	private List<SelectItem> listaEntidades;
	
	private Integer idEntidadSeleccionada;

	@ManagedProperty(value = "#{serviceDao}")
	transient private ServiceDao serviceDao;
	
	private int idBienestar;
	
	private int idRecaudo;
	
	private int idGastos;
	
	private int idDistribuciones;

	public ReportesBean() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings({ "unused", "rawtypes" })
	@PostConstruct
	private void _init() {
		try {
			List<Entidades> entidades = (List<Entidades>) serviceDao
					.getGenericCommonDao().loadAll(Entidades.class);
			List<Formatos> formatos = (List<Formatos>) serviceDao
					.getGenericCommonDao().loadAll(Formatos.class);
			for (Formatos formatos2 : formatos) {
				if (formatos2.getForNombre().equals(FormatosEnum.DETALLADO_DE_BIENESTAR_SOCIAL.getId())){
					idBienestar=formatos2.getForCodigo();
				}else{
					if (formatos2.getForNombre().equals(FormatosEnum.INFORME_TRIMESTRAL_DE_RECAUDOS.getId())){
						idRecaudo=formatos2.getForCodigo();
					}else{
						if (formatos2.getForNombre().equals(FormatosEnum.EJECUCION_PRESUPUESTAL.getId())){
							idGastos=formatos2.getForCodigo();
						}else{
							if (formatos2.getForNombre().equals(FormatosEnum.DETALLADO_DE_DISTRIBUCION.getId())){
								idDistribuciones=formatos2.getForCodigo();
							}
						}
					}					
				}
			}
			this.listaEntidades = new ArrayList<SelectItem>();

			for (Entidades e : entidades) {
				SelectItem item = new SelectItem(e.getEntCodigo(),
						e.getEntObjetoSocial());
				this.listaEntidades.add(item);
			}
			
			Calendar now = Calendar.getInstance();   
			ano = String.valueOf(now.get(Calendar.YEAR)); 

		} catch (NumberFormatException e) {
			FacesUtils.addFacesMessage(
					"Se ha presentado un fallo inesperado: "
							+ e.getMessage(), FacesMessage.SEVERITY_ERROR);
			e.printStackTrace();
		}
	}

	public String ejecutarReporte() {
		Entidades entidad = (Entidades) serviceDao.getGenericCommonDao().read(
				Entidades.class, idEntidadSeleccionada);
		try {
			if (idEntidadSeleccionada != null && idEntidadSeleccionada>0 && trimestre != null
					&& !trimestre.equals("")) {
				BigDecimal recaudo = ejecutarConsultaFB(idRecaudo, "Valor",
						trimestre, ano);
				BigDecimal distribucion = ejecutarConsultaFB(idDistribuciones,
						"Valor_Neto", trimestre, ano);
				BigDecimal bienestar = ejecutarConsultaFB(idBienestar,
						"Saldo_Final", trimestre, ano);
				BigDecimal gastos = ejecutarConsultaFB(idGastos,
						"Valor_Ejecutado", trimestre, ano);	
				if (recaudo==null && distribucion==null && bienestar==null && gastos==null){
					throw new Exception("error");
				}
				exportarExcel(recaudo, distribucion, bienestar, gastos, entidad.getEntObjetoSocial()+" "+trimestre+" "+ano);			
			}else
				if ((idEntidadSeleccionada==null || idEntidadSeleccionada==0) && trimestre != null
						&& !trimestre.equals("")){
					List<Map<String, Object>> recaudo = ejecutarConsultaFA1(idRecaudo, "Valor", trimestre, ano);
					List<Map<String, Object>> distribucion = ejecutarConsultaFA1(idDistribuciones,"Valor_Neto", trimestre, ano);
					List<Map<String, Object>> bienestar = ejecutarConsultaFA1(idBienestar,"Saldo_Final", trimestre, ano);
					List<Map<String, Object>> gastos = ejecutarConsultaFA1(idGastos,"Valor_Ejecutado", trimestre, ano);
					if (recaudo.size()==0 && distribucion.size()==0 && bienestar.size()==0 && gastos.size()==0){
						throw new Exception("error");
					}
					exportarExcel(recaudo, distribucion, bienestar, gastos, "Consolidado "+trimestre+" "+ano,null);
				}else if ((idEntidadSeleccionada==null || idEntidadSeleccionada==0) && (trimestre == null
						|| trimestre.equals(""))){
					List<Map<String, Object>> recaudo = ejecutarConsultaFA2(idRecaudo, "Valor", ano);
					List<Map<String, Object>> distribucion = ejecutarConsultaFA2(idDistribuciones,"Valor_Neto", ano);
					List<Map<String, Object>> bienestar = ejecutarConsultaFA2(idBienestar,"Saldo_Final", ano);
					List<Map<String, Object>> gastos = ejecutarConsultaFA2(idGastos,"Valor_Ejecutado", ano);
					if (recaudo.size()==0 && distribucion.size()==0 && bienestar.size()==0 && gastos.size()==0){
						throw new Exception("error");
					}
					exportarExcel(recaudo, distribucion, bienestar, gastos, "Consolidado por sociedad año "+ano,null);				
				}else if ((idEntidadSeleccionada!=null && idEntidadSeleccionada>0) && (trimestre == null
						|| trimestre.equals(""))){
					List<Map<String, Object>> recaudo = ejecutarConsultaFA3(idRecaudo, "Valor", ano);
					List<Map<String, Object>> distribucion = ejecutarConsultaFA3(idDistribuciones,"Valor_Neto", ano);
					List<Map<String, Object>> bienestar = ejecutarConsultaFA3(idBienestar,"Saldo_Final", ano);
					List<Map<String, Object>> gastos = ejecutarConsultaFA3(idGastos,"Valor_Ejecutado", ano);
					if (recaudo.size()==0 && distribucion.size()==0 && bienestar.size()==0 && gastos.size()==0){
						throw new Exception("error");
					}
					Map<String, BigDecimal> mapValores = new HashMap<String, BigDecimal>();
					mapValores.put("recaudo", ejecutarConsultaTotalTrimestres(idRecaudo, "Valor", ano)
							);
					mapValores.put("distribucion", ejecutarConsultaTotalTrimestres(idDistribuciones,"Valor_Neto", ano));
					mapValores.put("bienestar", ejecutarConsultaTotalTrimestres(idBienestar,"Saldo_Final", ano));
					mapValores.put("gastos", ejecutarConsultaTotalTrimestres(idGastos,"Valor_Ejecutado", ano));
					exportarExcel(recaudo, distribucion, bienestar, gastos, entidad.getEntObjetoSocial()+" "+"Informe por trimestre - "+ano, mapValores);
				}
			
		} catch (Exception e) {
			FacesUtils.addFacesMessage(
					"No existen datos para los filtros de búsqueda ingresados", FacesMessage.SEVERITY_ERROR);
			e.printStackTrace();
		}
		return null;
	}
	
	
	private BigDecimal ejecutarConsultaFB(int idFormato, String columna, String trimestre, String ano){
		String sql = "SELECT sum(CAST(REPLACE(CRG_Valor, '.',',') AS MONEY) ) as valor "
				+ "FROM SGC.Registro_Cargue "
				+ "where FOR_Codigo="+idFormato
				+ " and DET_Codigo=(select DET_Codigo "
				+ "from SGC.Detalles_Formato where FOR_Codigo="+idFormato+" and DET_Nombre='"+columna+"') "
				+ "and ENT_Codigo="+idEntidadSeleccionada.intValue()+" and FCR_Codigo=(select FCR_Codigo from SGC.Cortes_Formato "
				+ "where FCR_Periodo='"+trimestre+"' and FCR_Ano='"+ano+"') ";
		List<Map<String, Object>> lectura = null;
		lectura = serviceDao.getSpringGenericDao().executeQuery(sql);
		return ((BigDecimal) lectura.get(0).get("valor"));		
	}
	
	private List<Map<String, Object>> ejecutarConsultaFA1(int idFormato, String columna, String trimestre, String ano){
		String sql="SELECT sum(CAST(REPLACE(CRG_Valor, '.',',') AS MONEY) ) as valor, ENT_ObjetoSocial as sociedad "
				+ "FROM SGC.Registro_Cargue rc "
				+ " inner join SGC.Entidades ent"
				+ " on rc.ENT_Codigo=ent.ENT_Codigo"
				+ " where FOR_Codigo="+idFormato
				+ " and DET_Codigo=(select DET_Codigo" 
				+ " from SGC.Detalles_Formato"
				+ " where FOR_Codigo="+idFormato+" and DET_Nombre='"+columna+"')"
				+ " and FCR_Codigo=(select FCR_Codigo from SGC.Cortes_Formato "
				+ " where FCR_Periodo='"+trimestre+"'"
				+ " and FCR_Ano='"+ano+"'"
				+ " ) group by ENT_ObjetoSocial";		
		return serviceDao.getSpringGenericDao().executeQuery(sql);		
	}
	
	private List<Map<String, Object>> ejecutarConsultaFA2(int idFormato, String columna, String ano){
		String sql="SELECT sum(CAST(REPLACE(CRG_Valor, '.',',') AS MONEY) ) as valor, ENT_ObjetoSocial as sociedad "
				+ "FROM SGC.Registro_Cargue rc "
				+ " inner join SGC.Entidades ent"
				+ " on rc.ENT_Codigo=ent.ENT_Codigo"
				+ " where FOR_Codigo="+idFormato
				+ " and DET_Codigo=(select DET_Codigo" 
				+ " from SGC.Detalles_Formato"
				+ " where FOR_Codigo="+idFormato+" and DET_Nombre='"+columna+"')"
				+ " and FCR_Codigo in (select FCR_Codigo from SGC.Cortes_Formato "
				+ " where FCR_Ano='"+ano+"'"
				+ " ) group by ENT_ObjetoSocial";		
		return serviceDao.getSpringGenericDao().executeQuery(sql);		
	}
	
	private List<Map<String, Object>> ejecutarConsultaFA3(int idFormato, String columna, String ano){
		String sql="SELECT sum(CAST(REPLACE(CRG_Valor, '.',',') AS MONEY) ) as valor, FCR_Periodo as trimestre "
				+ "FROM SGC.Registro_Cargue rc "
				+ " inner join SGC.Cortes_Formato cor"
				+ " on rc.FCR_Codigo=cor.FCR_Codigo"
				+ " where rc.FOR_Codigo="+idFormato
				+ " and DET_Codigo=(select DET_Codigo" 
				+ " from SGC.Detalles_Formato"
				+ " where FOR_Codigo="+idFormato+" and DET_Nombre='"+columna+"')"
				+ " and ENT_Codigo="+idEntidadSeleccionada.intValue()+" and rc.FCR_Codigo in (select FCR_Codigo from SGC.Cortes_Formato "
				+ " where FCR_Ano='"+ano+"'"
				+ " ) group by FCR_Periodo";		
		return serviceDao.getSpringGenericDao().executeQuery(sql);		
	}
	
	private BigDecimal ejecutarConsultaTotalTrimestres(int idFormato, String columna, String ano){
		String sql="SELECT sum(CAST(REPLACE(CRG_Valor, '.',',') AS MONEY) ) as valor "
				+ " FROM SGC.Registro_Cargue rc "
				+ " inner join SGC.Cortes_Formato cor"
				+ " on rc.FCR_Codigo=cor.FCR_Codigo"
				+ " where rc.FOR_Codigo="+idFormato
				+ " and DET_Codigo=(select DET_Codigo" 
				+ " from SGC.Detalles_Formato"
				+ " where FOR_Codigo="+idFormato+" and DET_Nombre='"+columna+"')"
				+ " and ENT_Codigo="+idEntidadSeleccionada.intValue()+" and rc.FCR_Codigo in (select FCR_Codigo from SGC.Cortes_Formato "
				+ " where FCR_Ano='"+ano+"'"
				+ " )";		
		List<Map<String, Object>> lectura = null;
		lectura = serviceDao.getSpringGenericDao().executeQuery(sql);
		return ((BigDecimal) lectura.get(0).get("valor"));			
	}
	
	private Map<String,BigDecimal> obtenerValores(List<Map<String, Object>> valoresBD, Map<String,BigDecimal> mapValores){
		mapValores = new HashMap<String, BigDecimal>();
		for (Map<String, Object> map : valoresBD) {
			String temp=(String)map.get("sociedad");
			if (temp!=null){
				mapValores.put((String)map.get("sociedad"), (BigDecimal)map.get("valor"));
			}else{
				mapValores.put((String)map.get("trimestre"), (BigDecimal)map.get("valor"));
			}
		}
		return mapValores;
	}
	
	
	public void exportarExcel(List<Map<String, Object>> recaudo, List<Map<String, Object>> distribucion, List<Map<String, Object>> bienestar, List<Map<String, Object>> gastos, String titulo, Map<String, BigDecimal> mapValores){		
		try {
			
		DecimalFormat myFormatter = new DecimalFormat("###,###.###");
		BigDecimal temp=null;
		Map<String,BigDecimal> valoresRecaudo = new HashMap<String, BigDecimal>();
		valoresRecaudo = obtenerValores(recaudo, valoresRecaudo);
		Map<String,BigDecimal> valoresGastos = new HashMap<String, BigDecimal>();
		valoresGastos = obtenerValores(gastos, valoresGastos);
		Map<String,BigDecimal> valoresDistribucion = new HashMap<String, BigDecimal>();
		valoresDistribucion = obtenerValores(distribucion, valoresDistribucion);
		Map<String,BigDecimal> valoresBienestar = new HashMap<String, BigDecimal>();
		valoresBienestar = obtenerValores(bienestar, valoresBienestar);
		List<String> entidadesAgregadas = new ArrayList<String>();
			
		int numeroEntidades=recaudo.size();
		if (distribucion.size()>numeroEntidades){
			numeroEntidades=distribucion.size();
		}
		if (bienestar.size()>numeroEntidades){
			numeroEntidades=bienestar.size();
		}
		if (gastos.size()>numeroEntidades){
			numeroEntidades=gastos.size();
		}
		
		if (mapValores!=null){
			numeroEntidades=numeroEntidades+2;
		}else{
			numeroEntidades=numeroEntidades+1;
		}
		String[][] valores = new String[5][numeroEntidades];
		
		valores[0][0]="Concepto";
		valores[1][0]="Recaudo";
		valores[2][0]="Gastos";
		valores[3][0]="Distribucion";
		valores[4][0]="Bienestar";
		int i=1;
		
				for (Map.Entry<String, BigDecimal> entry : valoresRecaudo.entrySet())
				{				
					
					entidadesAgregadas.add(entry.getKey());
					valores[0][i]=entry.getKey();					
					valores[1][i]=myFormatter.format(entry.getValue());
					temp=valoresGastos.get(entry.getKey());
					if (temp!=null){
						valores[2][i]=myFormatter.format(temp);
					}
					temp=valoresDistribucion.get(entry.getKey());
					if (temp!=null){
						valores[3][i]=myFormatter.format(temp);
					}
					temp=valoresBienestar.get(entry.getKey());
					if (temp!=null){
						valores[4][i]=myFormatter.format(temp);
					}
				    
				    i++;
				    
				}
				
				
				
				for (Map.Entry<String, BigDecimal> entry : valoresGastos.entrySet())
				{				
					if (!entidadesAgregadas.contains(entry.getKey())){
						entidadesAgregadas.add(entry.getKey());
						valores[0][i]=entry.getKey();					
						valores[2][i]=myFormatter.format(entry.getValue());
						
						temp=valoresDistribucion.get(entry.getKey());
						if (temp!=null){
							valores[3][i]=myFormatter.format(temp);
						}
						temp=valoresBienestar.get(entry.getKey());
						if (temp!=null){
							valores[4][i]=myFormatter.format(temp);
						}
						i++;
					}
				    
				    
				}
				
				
				
				for (Map.Entry<String, BigDecimal> entry : valoresDistribucion.entrySet())
				{				
					if (!entidadesAgregadas.contains(entry.getKey())){
						entidadesAgregadas.add(entry.getKey());
						valores[0][i]=entry.getKey();					
						valores[3][i]=myFormatter.format(entry.getValue());
												
						temp=valoresBienestar.get(entry.getKey());
						if (temp!=null){
							valores[4][i]=myFormatter.format(temp);
						}
						i++;
					}
				    
				    
				}
				
				
				
				for (Map.Entry<String, BigDecimal> entry : valoresBienestar.entrySet())
				{				
					if (!entidadesAgregadas.contains(entry.getKey())){
						entidadesAgregadas.add(entry.getKey());
						valores[0][i]=entry.getKey();					
						valores[3][i]=myFormatter.format(entry.getValue());
						i++;
					}
				    				    
				}
				
				if (mapValores!=null){
					valores[0][i]="TOTAL";
					valores[1][i]=myFormatter.format(mapValores.get("recaudo"));
					valores[2][i]=myFormatter.format(mapValores.get("gastos"));
					valores[3][i]=myFormatter.format(mapValores.get("distribucion"));
					valores[4][i]=myFormatter.format(mapValores.get("bienestar"));
				}
				
		        	
					
					HSSFWorkbook workbook = new HSSFWorkbook();
			        HSSFSheet sheet = workbook.createSheet();
			        workbook.setSheetName(0, "Reporte");

			        CellStyle headerStyle = workbook.createCellStyle();	 
			        HSSFFont hSSFFont = workbook.createFont();
			        hSSFFont.setFontName(HSSFFont.FONT_ARIAL);
			        hSSFFont.setFontHeightInPoints((short) 16);
			        hSSFFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);	        
			        headerStyle.setFont(hSSFFont);

			        CellStyle style = workbook.createCellStyle();
			        style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
			        style.setFillPattern(CellStyle.SOLID_FOREGROUND);

			        HSSFRow headerRow = sheet.createRow(0);
			        for (i = 0; i < numeroEntidades; i++) {
			            String header = valores[0][i];
			            HSSFCell cell = headerRow.createCell(i);
			            cell.setCellStyle(headerStyle);
			            cell.setCellValue(header);
			            sheet.autoSizeColumn((short)i);
			        }			        
			        
			        DefaultCategoryDataset my_bar_chart_dataset = new DefaultCategoryDataset();			        			        
			   
					
			        String series=null; 	
			        Double valorDecimal=null;
			       for (i = 1; i < 5; i++) {
			    	   HSSFRow dataRow = sheet.createRow(i);
			    	   int z=1;
			    	   for (int j = 0; j < numeroEntidades; j++) {				
			            String valor = valores[i][j];
			            boolean isNumero=false;
			            try{			            	
			            	valorDecimal=Double.parseDouble(valor.replaceAll("\\.", ""));
			            	isNumero=true;
			            }catch(Exception e){
			            	series=valor;
			            }			            
			            style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
			            style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
			            style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
			            style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
			            HSSFCell cell=dataRow.createCell(j);			            
			            cell.setCellValue(valor);	            			            
			            cell.setCellStyle(style);
			            sheet.autoSizeColumn((short)i);
			            
			            	if(isNumero){			            	
			            		my_bar_chart_dataset.addValue(valorDecimal.doubleValue(),series,valores[0][z]);
			            		z++;
			            	}		            
			    	   }			    	   
			       }   
			       			       
			       JFreeChart BarChartObject=ChartFactory.createBarChart(titulo,"","Valor",my_bar_chart_dataset,PlotOrientation.VERTICAL,true,true,false);
			       

					final CategoryPlot plot = BarChartObject.getCategoryPlot();
					

					final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
					rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
					rangeAxis.setNumberFormatOverride(myFormatter); 
					

			       
			       int width=640; /* Width of the chart */
		           int height=480; /* Height of the chart */ 
			       ByteArrayOutputStream chart_out = new ByteArrayOutputStream();          
		           ChartUtilities.writeChartAsPNG(chart_out,BarChartObject,width,height);
		           int my_picture_id = workbook.addPicture(chart_out.toByteArray(), Workbook.PICTURE_TYPE_PNG);
		           /* we close the output stream as we don't need this anymore */
		           chart_out.close();
		           /* Create the drawing container */
		           HSSFPatriarch drawing = sheet.createDrawingPatriarch();
		           /* Create an anchor point */
		           ClientAnchor my_anchor = new HSSFClientAnchor();
		           /* Define top left corner, and we can resize picture suitable from there */
		           my_anchor.setCol1(4);
		           my_anchor.setRow1(5);
		           /* Invoke createPicture and pass the anchor point and ID */
		           HSSFPicture  my_picture = drawing.createPicture(my_anchor, my_picture_id);
		           /* Call resize method, which resizes the image */
		           my_picture.resize();
			        
			        try {
			        	ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			        	workbook.write(outByteStream);
			        	byte[] outArray = outByteStream.toByteArray();
			        	FacesContext fc = FacesContext.getCurrentInstance();
			    		HttpServletResponse response = (HttpServletResponse) fc
			    				.getExternalContext().getResponse();					
			    		response.setHeader("Content-disposition", "attachment;filename=Reporte.xls");
			    		 response.setContentType("application/vnd.ms-excel");
			    		 response.setContentLength(outArray.length);
			        	OutputStream outStream = response.getOutputStream();
			        	outStream.write(outArray);
			        	outStream.flush();	                
		            } catch (IOException ex) {
		            	FacesUtils.addFacesMessage(
		    					"Se ha presentado un fallo inesperado construyendo el reporte: "
		    							+ ex.getMessage(), FacesMessage.SEVERITY_ERROR);
		                ex.printStackTrace();
		            }
		

		} catch (Exception e) {			
			FacesUtils.addFacesMessage(
					"No existen datos para los filtros de búsqueda ingresados", FacesMessage.SEVERITY_ERROR);
			e.printStackTrace();
		} 
	}
	
	
	public void exportarExcel(BigDecimal recaudo, BigDecimal distribucion, BigDecimal bienestar, BigDecimal gastos, String titulo){		
		try {
		
		BigDecimal[] v= {recaudo, gastos, distribucion, bienestar };	
		String[] s = {"Recaudo", "Gastos", "Distribucion", "Bienestar"};
			
		 HSSFWorkbook workbook = new HSSFWorkbook();
	        HSSFSheet sheet = workbook.createSheet();
	        workbook.setSheetName(0, "Reporte");

	        String[] headers = new String[]{
	            "Concepto",
	            "Valor"	            
	        };
	        	        

	        CellStyle headerStyle = workbook.createCellStyle();	 
	        HSSFFont hSSFFont = workbook.createFont();
	        hSSFFont.setFontName(HSSFFont.FONT_ARIAL);
	        hSSFFont.setFontHeightInPoints((short) 16);
	        hSSFFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);	        
	        headerStyle.setFont(hSSFFont);

	        CellStyle style = workbook.createCellStyle();
	        style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
	        style.setFillPattern(CellStyle.SOLID_FOREGROUND);

	        HSSFRow headerRow = sheet.createRow(0);
	        for (int i = 0; i < headers.length; ++i) {
	            String header = headers[i];
	            HSSFCell cell = headerRow.createCell(i);
	            cell.setCellStyle(headerStyle);
	            cell.setCellValue(header);
	        }
	        DefaultCategoryDataset my_bar_chart_dataset = new DefaultCategoryDataset();
	        

	        DecimalFormat myFormatter = new DecimalFormat("###,###.###");
	   
			
	        
	       for (int i = 0; i < s.length; i++) {
			
	    	   String valor="0";
	            HSSFRow dataRow = sheet.createRow(i + 1);	            
	            String concepto = s[i];	            
	            if (v[i]!=null){
	            	valor = myFormatter.format(v[i]);	
	            }
	            
	            style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
	            style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
	            style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
	            style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);

	            HSSFCell cell=dataRow.createCell(0);
	            HSSFCell cell1=dataRow.createCell(1);
	            cell.setCellValue(concepto);	            
	            cell1.setCellValue(valor);
	            cell.setCellStyle(style);
	            cell1.setCellStyle(style);
	            sheet.autoSizeColumn((short)0);
	            sheet.autoSizeColumn((short)1);
	            my_bar_chart_dataset.addValue(v[i]!=null?v[i].doubleValue():new BigDecimal("0").doubleValue(),"Valor",s[i]);
	       }   	       
	       JFreeChart BarChartObject=ChartFactory.createBarChart(titulo,"Concepto","Valor",my_bar_chart_dataset,PlotOrientation.VERTICAL,true,true,false);
	       

			final CategoryPlot plot = BarChartObject.getCategoryPlot();
			

			final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
			rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
			rangeAxis.setNumberFormatOverride(myFormatter); 
			
	       
	       
	       
	       int width=640; /* Width of the chart */
           int height=480; /* Height of the chart */ 
	       ByteArrayOutputStream chart_out = new ByteArrayOutputStream();          
           ChartUtilities.writeChartAsPNG(chart_out,BarChartObject,width,height);
           int my_picture_id = workbook.addPicture(chart_out.toByteArray(), Workbook.PICTURE_TYPE_PNG);
           /* we close the output stream as we don't need this anymore */
           chart_out.close();
           /* Create the drawing container */
           HSSFPatriarch drawing = sheet.createDrawingPatriarch();
           /* Create an anchor point */
           ClientAnchor my_anchor = new HSSFClientAnchor();
           /* Define top left corner, and we can resize picture suitable from there */
           my_anchor.setCol1(4);
           my_anchor.setRow1(5);
           /* Invoke createPicture and pass the anchor point and ID */
           HSSFPicture  my_picture = drawing.createPicture(my_anchor, my_picture_id);
           /* Call resize method, which resizes the image */
           my_picture.resize();
	        
	        try {
	        	ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
	        	workbook.write(outByteStream);
	        	byte[] outArray = outByteStream.toByteArray();
	        	FacesContext fc = FacesContext.getCurrentInstance();
	    		HttpServletResponse response = (HttpServletResponse) fc
	    				.getExternalContext().getResponse();					
	    		response.setHeader("Content-disposition", "attachment;filename=Reporte.xls");
	    		 response.setContentType("application/vnd.ms-excel");
	    		 response.setContentLength(outArray.length);
	        	OutputStream outStream = response.getOutputStream();
	        	outStream.write(outArray);
	        	outStream.flush();	                
            } catch (IOException ex) {
            	FacesUtils.addFacesMessage(
    					"Se ha presentado un fallo inesperado construyendo el reporte: "
    							+ ex.getMessage(), FacesMessage.SEVERITY_ERROR);
                ex.printStackTrace();
            }

		} catch (Exception e) {
			FacesUtils.addFacesMessage(
					"No existen datos para los filtros de búsqueda ingresados", FacesMessage.SEVERITY_ERROR);
			e.printStackTrace();
		} 
	}
	
	
	public void exportarPdf(BigDecimal recaudo, BigDecimal distribucion, BigDecimal bienestar, BigDecimal gastos, String titulo){		
		try {
		
			BigDecimal[] v= {recaudo, gastos, distribucion, bienestar };	
			String[] s = {"Recaudo", "Gastos", "Distribucion", "Bienestar"};					 

	        String[] headers = new String[]{
	            "Concepto",
	            "Valor"	            
	        };
	        	        

	        PdfPTable table = new PdfPTable(headers.length);
	        // the cell object
	        PdfPCell cell;
	        Font font = new Font();		        
	        font.setStyle(Font.BOLD);
	       	        
	        for (int i = 0; i < headers.length; ++i) {
	            String header = headers[i];
	            cell = new PdfPCell(new Phrase(header,font));
	            table.addCell(cell);
	        }
	        DefaultCategoryDataset my_bar_chart_dataset = new DefaultCategoryDataset();
	        

	        DecimalFormat myFormatter = new DecimalFormat("###,###.###");
	   
			
	        
	       for (int i = 0; i < s.length; i++) {
			
	    	   String valor="0";	            
	            String concepto = s[i];	            
	            if (v[i]!=null){
	            	valor = myFormatter.format(v[i]);	
	            }
	            cell = new PdfPCell(new Phrase(concepto));	            
	            table.addCell(cell);
	            cell = new PdfPCell(new Phrase(valor));
	            table.addCell(cell);

	            my_bar_chart_dataset.addValue(v[i]!=null?v[i].doubleValue():new BigDecimal("0").doubleValue(),"Valor",s[i]);
	       }   	       
	       JFreeChart BarChartObject=ChartFactory.createBarChart(titulo,"Concepto","Valor",my_bar_chart_dataset,PlotOrientation.VERTICAL,true,true,false);
	       

			final CategoryPlot plot = BarChartObject.getCategoryPlot();
			

			final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
			rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
			rangeAxis.setNumberFormatOverride(myFormatter); 
			
	       
	       
	       
	       int width=400; /* Width of the chart */
           int height=300; /* Height of the chart */ 
	        
	        try {
	        	Document document = new Document();	        	
	        	FacesContext fc = FacesContext.getCurrentInstance();
				HttpServletResponse response = (HttpServletResponse) fc
						.getExternalContext().getResponse();
				response.setHeader("Content-disposition", "attachment;filename=Reporte.pdf");
				response.setContentType("application/pdf");
				PdfWriter writer=PdfWriter.getInstance(document, response.getOutputStream());
				document.open();
				table.setSpacingAfter(25);
				document.add(table);
				PdfContentByte contentByte = writer.getDirectContent();
				PdfTemplate template = contentByte.createTemplate(width, height);
				Graphics2D graphics2d = template.createGraphics(width, height,
						new DefaultFontMapper());
				Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, width,
						height);

				BarChartObject.draw(graphics2d, rectangle2d);
				
				graphics2d.dispose();
				Image img = Image.getInstance(template);
				//img.scaleToFit(575, 822);
				img.setWidthPercentage(80);
				img.setAlignment(Image.MIDDLE);
				document.add(img);								
				document.close();
            } catch (IOException ex) {
            	FacesUtils.addFacesMessage(
    					"Se ha presentado un fallo inesperado construyendo el reporte: "
    							+ ex.getMessage(), FacesMessage.SEVERITY_ERROR);
                ex.printStackTrace();                
            }

		} catch (Exception e) {		
			FacesUtils.addFacesMessage(
					"No existen datos para los filtros de búsqueda ingresados", FacesMessage.SEVERITY_ERROR);
			e.printStackTrace();
		} 
	}
	
	public void exportarPdf(List<Map<String, Object>> recaudo, List<Map<String, Object>> distribucion, List<Map<String, Object>> bienestar, List<Map<String, Object>> gastos, String titulo,Map<String, BigDecimal> mapValores){		
		try {
			
		DecimalFormat myFormatter = new DecimalFormat("###,###.###");
		BigDecimal temp=null;
		Map<String,BigDecimal> valoresRecaudo = new HashMap<String, BigDecimal>();
		valoresRecaudo = obtenerValores(recaudo, valoresRecaudo);
		Map<String,BigDecimal> valoresGastos = new HashMap<String, BigDecimal>();
		valoresGastos = obtenerValores(gastos, valoresGastos);
		Map<String,BigDecimal> valoresDistribucion = new HashMap<String, BigDecimal>();
		valoresDistribucion = obtenerValores(distribucion, valoresDistribucion);
		Map<String,BigDecimal> valoresBienestar = new HashMap<String, BigDecimal>();
		valoresBienestar = obtenerValores(bienestar, valoresBienestar);
		List<String> entidadesAgregadas = new ArrayList<String>();
			
		int numeroEntidades=recaudo.size();
		if (distribucion.size()>numeroEntidades){
			numeroEntidades=distribucion.size();
		}
		if (bienestar.size()>numeroEntidades){
			numeroEntidades=bienestar.size();
		}
		if (gastos.size()>numeroEntidades){
			numeroEntidades=gastos.size();
		}
		
		if (mapValores!=null){
			numeroEntidades=numeroEntidades+2;
		}else{
			numeroEntidades=numeroEntidades+1;
		}
		String[][] valores = new String[5][numeroEntidades];
				
		valores[0][0]="Concepto";
		valores[1][0]="Recaudo";
		valores[2][0]="Gastos";
		valores[3][0]="Distribucion";
		valores[4][0]="Bienestar";
		int i=1;
		
				for (Map.Entry<String, BigDecimal> entry : valoresRecaudo.entrySet())
				{				
					
					entidadesAgregadas.add(entry.getKey());
					valores[0][i]=entry.getKey();					
					valores[1][i]=myFormatter.format(entry.getValue());
					temp=valoresGastos.get(entry.getKey());
					if (temp!=null){
						valores[2][i]=myFormatter.format(temp);
					}
					temp=valoresDistribucion.get(entry.getKey());
					if (temp!=null){
						valores[3][i]=myFormatter.format(temp);
					}
					temp=valoresBienestar.get(entry.getKey());
					if (temp!=null){
						valores[4][i]=myFormatter.format(temp);
					}
				    
				    i++;
				    
				}
				
				
				
				for (Map.Entry<String, BigDecimal> entry : valoresGastos.entrySet())
				{				
					if (!entidadesAgregadas.contains(entry.getKey())){
						entidadesAgregadas.add(entry.getKey());
						valores[0][i]=entry.getKey();					
						valores[2][i]=myFormatter.format(entry.getValue());
						
						temp=valoresDistribucion.get(entry.getKey());
						if (temp!=null){
							valores[3][i]=myFormatter.format(temp);
						}
						temp=valoresBienestar.get(entry.getKey());
						if (temp!=null){
							valores[4][i]=myFormatter.format(temp);
						}
						i++;
					}
				    
				    
				}
				
				
				
				for (Map.Entry<String, BigDecimal> entry : valoresDistribucion.entrySet())
				{				
					if (!entidadesAgregadas.contains(entry.getKey())){
						entidadesAgregadas.add(entry.getKey());
						valores[0][i]=entry.getKey();					
						valores[3][i]=myFormatter.format(entry.getValue());
												
						temp=valoresBienestar.get(entry.getKey());
						if (temp!=null){
							valores[4][i]=myFormatter.format(temp);
						}
						i++;
					}
				    
				    
				}
				
				
				
				for (Map.Entry<String, BigDecimal> entry : valoresBienestar.entrySet())
				{				
					if (!entidadesAgregadas.contains(entry.getKey())){
						entidadesAgregadas.add(entry.getKey());
						valores[0][i]=entry.getKey();					
						valores[3][i]=myFormatter.format(entry.getValue());
						i++;
					}
				    				    
				}
				
				if (mapValores!=null){
					valores[0][i]="TOTAL";
					valores[1][i]=myFormatter.format(mapValores.get("recaudo"));
					valores[2][i]=myFormatter.format(mapValores.get("gastos"));
					valores[3][i]=myFormatter.format(mapValores.get("distribucion"));
					valores[4][i]=myFormatter.format(mapValores.get("bienestar"));
				}
				
				PdfPTable table = new PdfPTable(numeroEntidades);
		        // the cell object
		        PdfPCell cell;
		        Font font = new Font();		        
		        font.setStyle(Font.BOLD);
			        for (i = 0; i < numeroEntidades; i++) {
			            String header = valores[0][i];
			            cell = new PdfPCell(new Phrase(header,font));
			            table.addCell(cell);
			        }
			        DefaultCategoryDataset my_bar_chart_dataset = new DefaultCategoryDataset();			        			        
			   
					
			        String series=null; 	
			        Double valorDecimal=null;
			       for (i = 1; i < 5; i++) {			    	   
			    	   int z=1;
			    	   for (int j = 0; j < numeroEntidades; j++) {				
			            String valor = valores[i][j];
			            boolean isNumero=false;
			            try{			            	
			            	valorDecimal=Double.parseDouble(valor.replaceAll("\\.", ""));
			            	isNumero=true;
			            }catch(Exception e){
			            	series=valor;
			            }			            			            			           
			            cell = new PdfPCell(new Phrase(valor));
			            table.addCell(cell);
			            
			            	if(isNumero){			            	
			            		my_bar_chart_dataset.addValue(valorDecimal.doubleValue(),series,valores[0][z]);
			            		z++;
			            	}		            
			    	   }			    	   
			       }   
			       			       
			       JFreeChart BarChartObject=ChartFactory.createBarChart(titulo,"","Valor",my_bar_chart_dataset,PlotOrientation.VERTICAL,true,true,false);
			       

					final CategoryPlot plot = BarChartObject.getCategoryPlot();
					

					final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
					rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
					rangeAxis.setNumberFormatOverride(myFormatter); 
					

			       
			       int width=350; /* Width of the chart */
		           int height=250; /* Height of the chart */ 
			       
			        
			        try {
			        	Document document = new Document();	        	
			        	FacesContext fc = FacesContext.getCurrentInstance();
						HttpServletResponse response = (HttpServletResponse) fc
								.getExternalContext().getResponse();
						response.setHeader("Content-disposition", "attachment;filename=Reporte.pdf");
						response.setContentType("application/pdf");
						PdfWriter writer=PdfWriter.getInstance(document, response.getOutputStream());
						document.open();
						table.setSpacingAfter(25);
						document.add(table);
						PdfContentByte contentByte = writer.getDirectContent();
						PdfTemplate template = contentByte.createTemplate(width, height);
						Graphics2D graphics2d = template.createGraphics(width, height,
								new DefaultFontMapper());
						Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, width,
								height);

						BarChartObject.draw(graphics2d, rectangle2d);
						
						graphics2d.dispose();
						Image img = Image.getInstance(template);
						//img.scaleToFit(575, 822);
						img.setWidthPercentage(80);
						img.setAlignment(Image.MIDDLE);
						document.add(img);								
						document.close();                
		            } catch (IOException ex) {
		            	FacesUtils.addFacesMessage(
		    					"Se ha presentado un fallo inesperado construyendo el reporte: "
		    							+ ex.getMessage(), FacesMessage.SEVERITY_ERROR);
		                ex.printStackTrace();
		            }
		

		} catch (Exception e) {			
			FacesUtils.addFacesMessage(
					"No existen datos para los filtros de búsqueda ingresados", FacesMessage.SEVERITY_ERROR);
			e.printStackTrace();
		} 
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getTrimestre() {
		return trimestre;
	}

	public void setTrimestre(String trimestre) {
		this.trimestre = trimestre;
	}

	public List<SelectItem> getListaEntidades() {
		return listaEntidades;
	}

	public void setListaEntidades(List<SelectItem> listaEntidades) {
		this.listaEntidades = listaEntidades;
	}

	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	public Integer getIdEntidadSeleccionada() {
		return idEntidadSeleccionada;
	}

	public void setIdEntidadSeleccionada(Integer idEntidadSeleccionada) {
		this.idEntidadSeleccionada = idEntidadSeleccionada;
	}
	
}
