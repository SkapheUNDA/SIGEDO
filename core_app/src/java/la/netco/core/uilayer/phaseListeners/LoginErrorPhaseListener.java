package la.netco.core.uilayer.phaseListeners;


import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseListener;
import javax.faces.application.FacesMessage;
import javax.faces.event.PhaseId;

import la.netco.core.spring.security.MaxUsersSessionsException;
import la.netco.core.uilayer.beans.Constants;
import la.netco.core.uilayer.beans.utils.FacesUtils;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;



public class LoginErrorPhaseListener implements PhaseListener {
	private static final long serialVersionUID = -1216620620302322995L;

	public void beforePhase(PhaseEvent arg0) {
				
		Exception exception = (Exception) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("SPRING_SECURITY_LAST_EXCEPTION");
		
				
		if ((exception != null) && ((exception instanceof BadCredentialsException))) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_BAD_CREDENTIALS,FacesMessage.SEVERITY_ERROR);
		}

		if ((exception != null) && ((exception instanceof SessionAuthenticationException)) ) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_MAX_SESSIONS_BY_USERS,	FacesMessage.SEVERITY_ERROR);
		}
		
		if ((exception != null) && ((exception instanceof MaxUsersSessionsException)) ) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_MAX_SYSTEM_SESSIONS,FacesMessage.SEVERITY_ERROR);
		}
	}

	public void afterPhase(PhaseEvent arg0) {
	}

	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}
}
