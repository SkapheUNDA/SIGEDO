package la.netco.core.spring.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlStrategy;

public class ConcurrentSessionControl extends ConcurrentSessionControlStrategy {
    private int maxSystemSessions; 
    SessionRegistry sr;


	public ConcurrentSessionControl(SessionRegistry sr) {
        super(sr);
        this.sr = sr;
    }

    @Override
    public void onAuthentication(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
    	
    	System.out.println( " >>> ConcurrentSessionControl " +sr.getAllPrincipals().size() ) ;
    	if (sr.getAllPrincipals().size() >= maxSystemSessions) {    		
            throw new MaxUsersSessionsException("Maximum number of users exceeded");
        }
    	
    	    	
        super.onAuthentication(authentication, request, response);
    }

	public void setMaxSystemSessions(int maxSystemSessions) {
		this.maxSystemSessions = maxSystemSessions;
	}

	public int getMaxSystemSessions() {
		return maxSystemSessions;
	}
}
