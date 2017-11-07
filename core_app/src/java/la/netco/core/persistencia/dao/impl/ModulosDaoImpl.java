package la.netco.core.persistencia.dao.impl;

import java.sql.SQLException;
import java.util.List;

import la.netco.core.persistencia.dao.ModulosDao;
import la.netco.core.persitencia.dao.commos.GenericCommonDaoHibernateImpl;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Service;



@Service("modulosDao")
public class ModulosDaoImpl  extends  GenericCommonDaoHibernateImpl  implements ModulosDao {

	
	@SuppressWarnings("unchecked")
	public List<Integer> buscaModuloPorPerfilesPadre(final List<String> perfiles, final Integer idParent) {
		
		return getHibernateTemplate().executeFind(new HibernateCallback<Object>(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				StringBuffer queryString = new StringBuffer();
				
				queryString.append("WITH  modulospermiso  AS  ");
				queryString.append("( ");        
				queryString.append("	SELECT  mod_padre.* ");
				queryString.append("	FROM    CORE_APP.seg_modulo mod_padre ");   
				queryString.append("	inner join CORE_APP.seg_recurso recursos on mod_padre.MNU_Id=recursos.MNU_Id "); 
				queryString.append("	inner join CORE_APP.seg_recursos_perfiles perfiles on recursos.REC_Id=perfiles.REC_Id "); 
				queryString.append("	inner join CORE_APP.seg_perfiles perfil on perfiles.id_perfil=perfil.id_perfil "); 
				queryString.append("	where perfil.nombre in (:perfiles) ");   
				queryString.append("");		         
				queryString.append("	UNION ALL ");
				queryString.append("");
				queryString.append("	SELECT  modulo.* ");
				queryString.append("	FROM     CORE_APP.seg_modulo modulo "); 
				queryString.append("	inner join    modulospermiso  ON      modulo.MNU_Id = modulospermiso.MNU_IdPadre ");
				queryString.append("");
				queryString.append("),  ");
				queryString.append("modulos_seleccionado  AS "); 
				queryString.append("	( ");
				queryString.append("	SELECT  modulo2.* ");
				queryString.append("	FROM     CORE_APP.seg_modulo modulo2 "); 		       
				queryString.append("	WHERE  modulo2. MNU_Id = :idParent ");	         
				queryString.append("	UNION ALL ");
				queryString.append("	SELECT  mod_padre2.* ");
				queryString.append("	FROM    CORE_APP.seg_modulo mod_padre2 ");   
				queryString.append("	inner join    modulos_seleccionado  ON      mod_padre2.MNU_IdPadre = modulos_seleccionado.MNU_Id ");
				queryString.append(") ");
				queryString.append("SELECT   distinct modulospermiso.MNU_Id ");
				queryString.append("FROM    modulospermiso where modulospermiso.MNU_Id in ( ");        
				queryString.append("SELECT   distinct modulos_seleccionado.MNU_Id ");
				queryString.append("FROM    modulos_seleccionado) ");
				Query query = session.createSQLQuery(queryString.toString());
				
				query.setInteger("idParent", idParent);
				query.setParameterList("perfiles", perfiles);
				
				return query.list();
			}
		});
	
	}

	
	@SuppressWarnings("unchecked")
	public List<Integer> buscaModulosPadre(final List<String> perfiles) {
		
		return getHibernateTemplate().executeFind(new HibernateCallback<Object>(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				StringBuffer queryString = new StringBuffer();
				
				queryString.append("WITH  modulospermiso  AS  ");
				queryString.append("( ");        
				queryString.append("	SELECT  mod_padre.* ");
				queryString.append("	FROM    CORE_APP.seg_modulo mod_padre ");   
				queryString.append("	inner join CORE_APP.seg_recurso recursos on mod_padre.MNU_Id=recursos.MNU_Id "); 
				queryString.append("	inner join CORE_APP.seg_recursos_perfiles perfiles on recursos.REC_Id=perfiles.REC_Id "); 
				queryString.append("	inner join CORE_APP.seg_perfiles perfil on perfiles.id_perfil=perfil.id_perfil "); 
				queryString.append("	where perfil.nombre in (:perfiles) ");   
				queryString.append("	UNION ALL ");
				queryString.append("	SELECT  modulo.* ");
				queryString.append("	FROM     CORE_APP.seg_modulo modulo "); 
				queryString.append("	inner join    modulospermiso  ON      modulo.MNU_Id = modulospermiso.MNU_IdPadre ");

				queryString.append(") ");
				queryString.append("SELECT   distinct modulospermiso.MNU_Id ");
				queryString.append("FROM    modulospermiso WHERE modulospermiso.MNU_IdPadre IS NULL  ");
				
				Query query = session.createSQLQuery(queryString.toString());
				
				query.setParameterList("perfiles", perfiles);
				
				return query.list();
			}
		});
	
	}

	
public boolean eliminarRecursosPerfil(final Integer idPerfil) {
		
		return (Boolean) getHibernateTemplate().execute(new HibernateCallback<Object>(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				try {
					StringBuffer queryString = new StringBuffer();
					queryString.append("DELETE FROM CORE_APP.seg_recursos_perfiles WHERE id_perfil = :idPerfil");
					Query query = session.createSQLQuery(queryString.toString());
					query.setInteger("idPerfil", idPerfil);
					query.executeUpdate();
					

					return true;
				} catch (Exception e) {
					e.printStackTrace();

					return false;
				}
				
				
			}
		});
	
	}

}
