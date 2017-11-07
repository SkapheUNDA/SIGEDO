package la.netco.core.spring.security;

import java.util.Collection;
import java.util.List;

import la.netco.core.businesslogic.services.impl.SpringApplicationContext;
import la.netco.core.persistencia.dao.UsuariosDao;
import la.netco.core.persistencia.vo.Usuario;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;


public class UserDetailsUtils {

	public static String usernameLogged() {
		String username = null;
		if(SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null) {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			if (principal instanceof UserDetails) {
				username = ((UserDetails) principal).getUsername();
			} else {
				username = principal.toString();
			}		
		}	
		return username;
	}
	
	public static Usuario usuarioLogged() {
		
		Usuario user = null;
		if(SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null) {
			String username = null;
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			if (principal instanceof UserDetails) {
				username = ((UserDetails) principal).getUsername();
			} else {
				username = principal.toString();
			}
		
			UsuariosDao usuariosDao = (UsuariosDao)SpringApplicationContext.getBean("usuariosDao");
			user = usuariosDao.findUsuarioByUsername(username);
		}
		return user;
	}
	
	
	public static Collection<GrantedAuthority> userLoggedAuthorities() {
		
		Collection<GrantedAuthority> authorities= null;
		if(SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null) {
			
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			if (principal instanceof UserDetails) {
				authorities = ((UserDetails) principal).getAuthorities();
			} 
		
		}
		return authorities;
	}
	
	public static List<Object> allUsersLogged(){
		 SessionRegistry sr =  (SessionRegistry)SpringApplicationContext.getBean("sessionRegistry");
		 return sr.getAllPrincipals();
	}
	
}
