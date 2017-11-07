package la.netco.core.persistencia.dao;

import la.netco.core.persistencia.vo.Usuario;
import la.netco.core.persitencia.dao.commos.GenericCommonDao;

public interface  UsuariosDao  extends GenericCommonDao {
	
	public Usuario findUsuarioByUsername(String username);
	public Object findDatosByUsername(String username);
	public String findUsuarioByActionRule(final String username,final  String actionKey, final String actionMod);
}
