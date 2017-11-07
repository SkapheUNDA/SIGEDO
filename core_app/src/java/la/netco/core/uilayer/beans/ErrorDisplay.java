package la.netco.core.uilayer.beans;



import java.io.PrintWriter;
import java.io.StringWriter;

import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;


import org.apache.myfaces.shared_tomahawk.util.ExceptionUtils;

@ManagedBean(name = "errorDisplay")
@RequestScoped
public class ErrorDisplay {
	

    public String getInfoMessage() {
        return "An unexpected processing error has occurred.";
    }

    public String getStackTrace() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<?, ?> requestMap = context.getExternalContext().getSessionMap();
        Throwable ex = (Throwable) requestMap.remove("GLOBAL_ERROR");

        StringWriter writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
 
        fillStackTrace(ex, pw);

        return writer.toString();
    }

    private void fillStackTrace(Throwable ex, PrintWriter pw) {
    	if (null == ex) {
            return;
        }

        List<?> exceptions = ExceptionUtils.getExceptions(ex);
        Throwable throwable = (Throwable) exceptions.get(exceptions.size()-1);

        for (int i = 0; i<exceptions.size(); i++)
        {
            if (i > 0)
            {
                pw.println("Cause:");
            }
            throwable.printStackTrace(pw);
        }

    }


}