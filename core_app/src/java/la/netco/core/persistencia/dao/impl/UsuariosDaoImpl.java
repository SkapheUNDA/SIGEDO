package la.netco.core.persistencia.dao.impl;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import la.netco.core.persistencia.dao.UsuariosDao;
import la.netco.core.persistencia.vo.Usuario;
import la.netco.core.persitencia.dao.commos.GenericCommonDaoHibernateImpl;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Service;


@Service("usuariosDao")
public class UsuariosDaoImpl extends  GenericCommonDaoHibernateImpl implements UsuariosDao {

	
	public UsuariosDaoImpl() {
		//super(Usuario.class);
	}	
	
	public Usuario findUsuarioByUsername(String username) {
		final String nombreusuario = username;
	
		return (Usuario) getHibernateTemplate().execute(new HibernateCallback<Object>(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.getNamedQuery(Usuario.NAMED_QUERY_FIND_BY_USERNAME);
				query.setParameter(0, nombreusuario);
				return query.uniqueResult();
			}
		});
	}
	
	public Object findDatosByUsername(String username) {
		final String nombreusuario = username;
	
		return (Object) getHibernateTemplate().execute(new HibernateCallback<Object>(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.getNamedQuery(Usuario.NAMED_QUERY_DATOS_BY_USERNAME);
				query.setParameter(0, nombreusuario);
				List<?> allpatients;
				allpatients=  query.list();
				Object[] myResult = null;
		        for (Iterator<?> it = allpatients.iterator(); it.hasNext(); ) {
		        	myResult = (Object[]) it.next();
		        }
				return myResult;
			}
		});
	}

	
	public String findUsuarioByActionRule(final String username,final  String actionKey, final String actionMod) {
		return (String) getHibernateTemplate().execute(new HibernateCallback<Object>(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer queryString = new StringBuffer();
				queryString.append("select DISTINCT usuario.usrLog FROM Usuario usuario");
				queryString.append(" INNER JOIN usuario.perfiles perfil  ");	
				queryString.append(" INNER JOIN perfil.recursos recurso  ");	
				queryString.append(" WHERE ");
				queryString.append(" recurso.keyRecurso = :actionKey ");
				queryString.append(" AND recurso.modulo.keyMod = :actionMod ");	
				queryString.append(" AND  usuario.usrLog =  :username ");	
				
				Query query = session.createQuery(queryString.toString());
				query.setString("actionKey", actionKey);
				query.setString("actionMod", actionMod);
				query.setString("username", username);
				
				return query.uniqueResult();
			}
		});
	}


}
