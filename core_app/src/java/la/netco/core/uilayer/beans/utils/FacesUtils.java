package la.netco.core.uilayer.beans.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;


public class FacesUtils {

	private static ResourceBundle bundle;
	private static ResourceBundle propsSGC;
	
	protected static ClassLoader getCurrentClassLoader(Object defaultObject) {

		ClassLoader loader = Thread.currentThread().getContextClassLoader();

		if (loader == null) {
			loader = defaultObject.getClass().getClassLoader();
		}

		return loader;
	}

	public static ResourceBundle getBundle( FacesContext context) {
		if (bundle == null) {
			bundle = context.getApplication().getResourceBundle(context, "msg");
		}
		return bundle;
	}

	public static String getValue(String key, FacesContext context) {

		String result = null;
		try {
			result = getBundle(context).getString(key);
		} catch (MissingResourceException e) {
			result = "???" + key + "??? not found";
		}
		return result;
	}
	
	public static String getMessageResourceString(String bundleName,
			String key, Object params[], Locale locale) {

		String text = null;

		ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale,	getCurrentClassLoader(params));

		try {
			text = bundle.getString(key);
		} catch (MissingResourceException e) {
			text = "?? key " + key + " not found ??";
		}

		if (params != null) {
			MessageFormat mf = new MessageFormat(text, locale);
			text = mf.format(params, new StringBuffer(), null).toString();
		}

		return text;
	}
	
	public static String getMessage(String bundleName, FacesContext context) {

		String text = FacesUtils.getValue(bundleName, context);
		return text;
	}
	
	public static void addFacesMessageFromBundle(String msgName, FacesMessage.Severity severity) {

		String msg = FacesUtils.getMessage(msgName, FacesContext.getCurrentInstance());

		FacesMessage fm = new FacesMessage(msg);
		fm.setSeverity(severity);
		
		FacesContext.getCurrentInstance().addMessage(msg, fm);

	}
	
	public static void addFacesMessageFromBundle(String msgName, FacesMessage.Severity severity, String... params) {

		String msg = FacesUtils.getMessage(msgName, FacesContext.getCurrentInstance());
		
		String detail = msg;
		
		if (params != null && params.length > 0) {
			MessageFormat messageFormat = new MessageFormat(detail);
			detail = messageFormat.format(params);
		}

		FacesMessage fm = new FacesMessage(msg);
		fm.setSeverity(severity);
		
		FacesContext.getCurrentInstance().addMessage(msg, fm);

	}
	
	
	public static void addFacesMessage(String msg, FacesMessage.Severity severity) {
		FacesMessage fm = new FacesMessage(msg);
		fm.setSeverity(severity);		
		FacesContext.getCurrentInstance().addMessage(msg, fm);
	}
	
	public static String getValorPropiedad(String name){
		
		Properties properties = new Properties();
		String value = null;
		 try {
			 
			 //Context ctx = new InitialContext();
			// Context envCtx = (Context) ctx.lookup("java:comp/env");
             String ruta= (String) FacesContext.getCurrentInstance().getExternalContext().getInitParameter("fileProperties");
             properties.load(new FileInputStream(ruta));
             value = properties.getProperty(name);
            
             
        } catch (IOException e) {
			throw new InternalError("Unable to load properties : "+ e.toString());
		}
		return value;		
	}
	
	public static Flash flashScope (FacesContext context){
		return (context.getExternalContext().getFlash());
	}

	/**
	 * @return the propsSGC
	 */
	public static ResourceBundle getPropsSGC() {
		propsSGC = ResourceBundle.getBundle("la.netco.sgc.reportes.sources.propiedades");
		return propsSGC;
	}

}
