/**
 * 
 */
package la.netco.sgc.persistence.dao.impl;

import java.sql.SQLException;
import java.util.List;

import la.netco.core.persitencia.dao.commos.GenericCommonDaoHibernateImpl;
import la.netco.sgc.persistence.dao.RegistroCargueFormatoDao;
import la.netco.sgc.persistence.dto.RegistroCargueFormato;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Service;

/**
 * @author cguzman
 *
 */
@Service("registroCargueFormatoDao")
public class RegistroCargueFormatoDaoImpl extends GenericCommonDaoHibernateImpl implements RegistroCargueFormatoDao {


	/* (non-Javadoc)
	 * @see la.netco.sgc.persistence.dao.RegistroCargueFormatoDao#tomarRegistroCargueFormatoPorCargue(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	public RegistroCargueFormato tomarRegistroCargueFormatoPorCargue(Integer codFormato, Integer codEntidad, Integer codCorte) {
		final Integer codigoFormato = codFormato;
		final Integer codigoEntidad = codEntidad;
		final Integer codigoCorteFormato = codCorte;
	
		HibernateCallback<RegistroCargueFormato> callback = new HibernateCallback<RegistroCargueFormato>() {
			
			public RegistroCargueFormato doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query query = session.getNamedQuery(RegistroCargueFormato.NAMED_QUERY_REGISTRO_CARGUE_FORMATO_CARGUE);
				query.setParameter("codFormato", codigoFormato);
				query.setParameter("codEntidad", codigoEntidad);
				query.setParameter("codCorte", codigoCorteFormato);
				
				try{
					RegistroCargueFormato temp= (RegistroCargueFormato)query.uniqueResult();
					return temp;
				}catch(Exception e){
					e.printStackTrace();
					return null;
				}
			}
		};
		
		
		return getHibernateTemplate().execute(callback);
	}

	public List<RegistroCargueFormato> tomarRegistroCargueFormatoSinCorte(
			Integer codFormato, Integer codEntidad) {
		final Integer codigoFormato = codFormato;
		final Integer codigoEntidad = codEntidad;
		return getHibernateTemplate().executeFind(
				new HibernateCallback<Object>() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
				Query query = session.getNamedQuery(RegistroCargueFormato.NAMED_QUERY_REGISTRO_CARGUE_FORMATO_SIN_CORTE);
				query.setParameter("codFormato", codigoFormato);
				query.setParameter("codEntidad", codigoEntidad);				
								
				return (List<RegistroCargueFormato>)query.list();									
					}
				});
	}

}
