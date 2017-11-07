package la.netco.core.uilayer.beans.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import la.netco.core.uilayer.beans.Constants;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;


public class ExportUtils {

	@SuppressWarnings("unchecked")
	public static void exportarXls(String nameTemplate, List<?>  datos, String listName) throws ParsePropertyException, IOException{
		
		String templateFileName = Constants.RUTA_ARCHIVOS_TEMPLATE_EXPORT_EXCELL+ "" +nameTemplate;
		
		File fsPath = new File(Constants.RUTA_ABSOLUTA_TEMP_DOWNLOAD_FILE +"/"+datos.hashCode());
		
		if(!fsPath.exists())
			fsPath.mkdirs();		
		
		String destFileName = fsPath.getPath() + File.separator	+ "reporte" +  ".xls";
		Map<String, List<Object>> beans = new HashMap<String, List<Object>>();        

        beans.put(listName, (List<Object>)datos);

        XLSTransformer transformer = new XLSTransformer( );	    
	    transformer.transformXLS(templateFileName, beans, destFileName);
	    
	    HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        response.sendRedirect(Constants.RUTA_PUBLICA_TEMP_DOWNLOAD_FILE +"/"+ datos.hashCode()+ "/"+"reporte" + ".xls");
        FacesContext.getCurrentInstance().responseComplete();
	}


	public static void exportarXls(String nameTemplate, Map<String, List<Object>> beans ) throws ParsePropertyException, IOException{
		
		String templateFileName = Constants.RUTA_ARCHIVOS_TEMPLATE_EXPORT_EXCELL+ "" +nameTemplate;
		String hashTmp=""+beans.hashCode(); 
		File fsPath = new File(Constants.RUTA_ABSOLUTA_TEMP_DOWNLOAD_FILE +"/"+hashTmp);
		
		if(!fsPath.exists())
			fsPath.mkdirs();		
		
		String destFileName = fsPath.getPath() + File.separator	+ "reporte" +  ".xls";		

        XLSTransformer transformer = new XLSTransformer( );	    
	    transformer.transformXLS(templateFileName, beans, destFileName);
	    
	    HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        response.sendRedirect(Constants.RUTA_PUBLICA_TEMP_DOWNLOAD_FILE +"/"+ hashTmp+ "/"+"reporte" + ".xls");
        FacesContext.getCurrentInstance().responseComplete();
	}
	
}
