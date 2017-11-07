package la.netco.core.uilayer.phaseListeners;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

public class LabelProvider implements SystemEventListener {

    public boolean isListenerForSource(Object source) {
        return true;
    }

    public void processEvent(SystemEvent event){
    	try {
	        HtmlOutputLabel outputLabel = (HtmlOutputLabel) event.getSource();	      
	        UIComponent target = outputLabel.findComponent(outputLabel.getFor());	
	        if(target != null) {
	            target.getAttributes().put("label", outputLabel.getValue());
	        }
	        
    	} catch (Exception e) {
			e.printStackTrace();
		}
    }
}