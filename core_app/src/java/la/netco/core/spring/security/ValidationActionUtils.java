package la.netco.core.spring.security;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.businesslogic.services.impl.SpringApplicationContext;
import la.netco.core.persistencia.dao.UsuariosDao;

public class ValidationActionUtils {

	public static boolean validUserAction(String actionKey, String actionKeyMod){
		String username = UserDetailsUtils.usernameLogged();
		username =	lookupDataProvider().findUsuarioByActionRule(username, actionKey, actionKeyMod);
		if(username  != null){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean validUserAction(String username, String actionKey, String actionKeyMod){
		username =	lookupDataProvider().findUsuarioByActionRule(username, actionKey, actionKeyMod);
		if(username  != null){
			return true;
		}else{
			return false;
		}
		
	}
	
	
	private static UsuariosDao lookupDataProvider() {
    	ServiceDao dataProvider = (ServiceDao) SpringApplicationContext.getBean("serviceDao");
    	return dataProvider.getUsuariosDao();
    }
	
}
