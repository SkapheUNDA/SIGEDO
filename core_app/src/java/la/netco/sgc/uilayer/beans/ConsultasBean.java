package la.netco.sgc.uilayer.beans;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.sgc.persistence.dto.CortesFormato;
import la.netco.sgc.persistence.dto.InformeEntidad;
import la.netco.sgc.uilayer.beans.util.InformesFaltantesDTO;

/**
 * @author cpulido
 * 
 */
/**
 * @author cpineros
 * 
 */
@ManagedBean(name = "consultasBean")
@ViewScoped
public class ConsultasBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{serviceDao}")
	transient private ServiceDao serviceDao;
	
	private Date fechaVencimientoCargue;
	private Integer idFechaCorteSeleccionada;
	private String periodoHabilitado;
	private List<InformeEntidad> informesPorEntidad;
	private List<InformesFaltantesDTO> informesFaltantes;
	

	public ConsultasBean() {		
	}

	@SuppressWarnings("unused")
	@PostConstruct
	private void initCargarFormatoBean() {
		cargarPeriodoHabilitado();
		informesFaltantes = new ArrayList<InformesFaltantesDTO>();
		InformesFaltantesDTO infFalt;
		informesPorEntidad = serviceDao.getInformeEntidadDao().obtenerInformesFaltantes(idFechaCorteSeleccionada);	
		for (InformeEntidad informeEntidad : informesPorEntidad) {
			infFalt = new InformesFaltantesDTO();
			infFalt.setSociedad(informeEntidad.getEntidad().getEntObjetoSocial());
			infFalt.setFormato(informeEntidad.getFormato().getForNombre());
			informesFaltantes.add(infFalt);
		}
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
	
	public void exportarExcel(){
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) fc
				.getExternalContext().getResponse();					
		response.setHeader("Content-disposition", "attachment;filename=InfFaltantes.xls");
		 response.setContentType("application/vnd.ms-excel");		 
		try {
		
		 HSSFWorkbook workbook = new HSSFWorkbook();
	        HSSFSheet sheet = workbook.createSheet();
	        workbook.setSheetName(0, periodoHabilitado);

	        String[] headers = new String[]{
	            "SOCIEDAD",
	            "FORMATO"	            
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

	        int i=0;
	        for (InformeEntidad informe : informesPorEntidad) {
	            HSSFRow dataRow = sheet.createRow(i + 1);	            
	            String sociedad = informe.getEntidad().getEntObjetoSocial();
	            String formato = informe.getFormato().getForNombre();
	            style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
	            style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
	            style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
	            style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);

	            HSSFCell cell=dataRow.createCell(0);
	            HSSFCell cell1=dataRow.createCell(1);
	            cell.setCellValue(sociedad);	            
	            cell1.setCellValue(formato);
	            cell.setCellStyle(style);
	            cell1.setCellStyle(style);
	            sheet.autoSizeColumn((short)0);
	            sheet.autoSizeColumn((short)1);
	            i++;
	        }	        	       
	        
	        try {
	        	ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
	        	workbook.write(outByteStream);
	        	byte[] outArray = outByteStream.toByteArray();
	        	OutputStream outStream = response.getOutputStream();
	        	response.setContentLength(outArray.length);	        	
	        	outStream.write(outArray);
	        	outStream.flush();	                
            } catch (IOException ex) {
                ex.printStackTrace();
            }

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	public List<InformeEntidad> getInformesPorEntidad() {
		return informesPorEntidad;
	}

	public void setInformesPorEntidad(List<InformeEntidad> informesPorEntidad) {
		this.informesPorEntidad = informesPorEntidad;
	}

	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	public Date getFechaVencimientoCargue() {
		return fechaVencimientoCargue;
	}

	public void setFechaVencimientoCargue(Date fechaVencimientoCargue) {
		this.fechaVencimientoCargue = fechaVencimientoCargue;
	}

	public Integer getIdFechaCorteSeleccionada() {
		return idFechaCorteSeleccionada;
	}

	public void setIdFechaCorteSeleccionada(Integer idFechaCorteSeleccionada) {
		this.idFechaCorteSeleccionada = idFechaCorteSeleccionada;
	}

	public String getPeriodoHabilitado() {
		return periodoHabilitado;
	}

	public void setPeriodoHabilitado(String periodoHabilitado) {
		this.periodoHabilitado = periodoHabilitado;
	}

	public List<InformesFaltantesDTO> getInformesFaltantes() {
		return informesFaltantes;
	}

	public void setInformesFaltantes(List<InformesFaltantesDTO> informesFaltantes) {
		this.informesFaltantes = informesFaltantes;
	}
	
	
	

}
