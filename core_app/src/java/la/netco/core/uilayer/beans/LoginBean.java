package la.netco.core.uilayer.beans;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;

import la.netco.core.spring.BeansSpringConst;
import la.netco.core.spring.security.MaxUsersSessionsException;
import la.netco.core.spring.security.UserDetailsUtils;
import la.netco.core.uilayer.beans.utils.FacesUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;


//import la.netco.core.uilayer.beans.utils.FacesUtils;

@ManagedBean(name = "loginBean")
@RequestScoped
public class LoginBean {

	private String username = "";

	private String password = "";

	private boolean rememberMe = false;

	private boolean loggedIn = false;

	private final Log log = LogFactory.getLog(LoginBean.class);

	@ManagedProperty(value = BeansSpringConst.BEAN_AUTENTICATION_MANAGER)
	private AuthenticationManager am;


	public String doLogin() throws IOException, ServletException {
		String page = "home";

		try {
			
			

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			boolean doLogin = true;
			
			System.out.println("doLogin >>  " + doLogin + " authentication " + authentication);
			if (authentication != null) {
				Object principal = authentication.getPrincipal();
				if (principal instanceof UserDetails) {
					log.warn("LoginBean login.jsf - Existe una sesión activa en el navegador, se redirecciona a home");
					doLogin = false;
				} else {
					doLogin = true;
				}
			}

			if (doLogin) {
				Authentication request = new UsernamePasswordAuthenticationToken(
						getUsername(), getPassword());
				Authentication result = am.authenticate(request);
				
				
				System.out.println("doLogin >>  result " + result);
				
				
				SecurityContextHolder.getContext().setAuthentication(result);
			}
		} catch (AuthenticationException exception) {
			if ((exception != null) && ((exception instanceof BadCredentialsException))) {
				FacesUtils.addFacesMessageFromBundle(Constants.ERROR_BAD_CREDENTIALS,FacesMessage.SEVERITY_ERROR);
			}

			if ((exception != null) && ((exception instanceof SessionAuthenticationException)) ) {
				FacesUtils.addFacesMessageFromBundle(Constants.ERROR_MAX_SESSIONS_BY_USERS,	FacesMessage.SEVERITY_ERROR);
			}
			
			if ((exception != null) && ((exception instanceof MaxUsersSessionsException)) ) {
				FacesUtils.addFacesMessageFromBundle(Constants.ERROR_MAX_SYSTEM_SESSIONS,FacesMessage.SEVERITY_ERROR);
			}
			return null;
		}
		return page;

	}

	/*
	public static void main(String[] test) throws IOException{
		
		Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("socks.mydom.com", 1080));
		Socket socket = new Socket(proxy); 
		
		
		socket.close();
		
	}
	*/
	public String doLogout() throws IOException, ServletException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext ec = facesContext.getExternalContext();
		ec.dispatch("/j_spring_security_logout");

		return "logout";
	}

	public String getUsernameLogged() {

		return UserDetailsUtils.usernameLogged();
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public boolean isRememberMe() {
		return this.rememberMe;
	}

	public void setRememberMe(final boolean rememberMe) {
		this.rememberMe = rememberMe;
	}

	public boolean isLoggedIn() {
		return this.loggedIn;
	}

	public void setLoggedIn(final boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public AuthenticationManager getAm() {
		return am;
	}

	public void setAm(AuthenticationManager am) {
		this.am = am;
	}

}