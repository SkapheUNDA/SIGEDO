package la.netco.core.uilayer.phaseListeners;

import java.util.Iterator;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public class SetFocusListener implements PhaseListener {


	private static final long serialVersionUID = 1035567818666643236L;

	public PhaseId getPhaseId() {
        // Listen on render response phase.
        return PhaseId.RENDER_RESPONSE;
    }

    public void beforePhase(PhaseEvent event) {
        FacesContext facesContext = event.getFacesContext();
        String focus = null;
     
        Iterator<String> clientIdsWithMessages = facesContext.getClientIdsWithMessages();
        while (clientIdsWithMessages.hasNext()) {
            String clientIdWithMessages = clientIdsWithMessages.next();
            if (focus == null) {
                focus = clientIdWithMessages;
            }
           
        }
         facesContext.getExternalContext().getRequestMap().put("focus", focus);
    }

    public void afterPhase(PhaseEvent event) {
        // Do nothing.
    }



}
