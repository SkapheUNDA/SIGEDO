package la.netco.core.spring.security;


import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;


public class LoginFilter extends OncePerRequestFilter {

	private final Log log = LogFactory.getLog(LoginFilter.class);
	private String defaultTargetUrl;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request,	HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
System.out.println(" LoginFilter >>> " + authentication);
	        if(authentication != null){
		    	Object principal = authentication.getPrincipal();
				if (principal instanceof UserDetails) {
					log.warn("Filtro login.jsf - Existe una sesión activa en el navegador, se redirecciona a : " + defaultTargetUrl);
					response.sendRedirect(defaultTargetUrl);
				}
	        }
	        
	        
	        doFilter(request, response, filterChain);
		
	}
	public String getDefaultTargetUrl() {
		return defaultTargetUrl;
	}
	public void setDefaultTargetUrl(String defaultTargetUrl) {
		this.defaultTargetUrl = defaultTargetUrl;
	}

}
