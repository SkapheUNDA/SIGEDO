package la.netco.sgc.persistence.dao.impl;


import java.sql.SQLException;
import java.util.List;



import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Service;

import la.netco.sgc.persistence.dao.CortesFormatoDao;
import la.netco.core.persitencia.dao.commos.GenericCommonDaoHibernateImpl;
import la.netco.sgc.persistence.dto.CortesFormato;

@Service("cortesFormatoDao")
public class CortesFormatoDaoImpl extends GenericCommonDaoHibernateImpl implements CortesFormatoDao {

	/**
	 * 
	 * @param codigoFormato
	 * 			{@link Integer}
	 * 
	 * @return {@link List}
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<CortesFormato> obtenerCortesFormatoPorFormato(
			Integer codigoFormato) {
		// TODO Auto-generated method stub
		final Integer codFormato = codigoFormato;
		return getHibernateTemplate().executeFind(
				new HibernateCallback<Object>() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session
								.getNamedQuery(CortesFormato.NAMED_QUERY_CORTES_FORMATO_POR_FORMATO);
						query.setParameter("codigoFormato", codFormato);
						return query.list();
					}
				});
	}

	/* (non-Javadoc)
	 * @see la.netco.core.persistencia.dao.CortesFormatoDao#obtenerCortesFormatoActivoPorFormato(java.lang.Integer)
	 */
	public CortesFormato obtenerCortesFormatoActivoPorFormato(
			) {
		
		
		HibernateCallback<CortesFormato> callback = new HibernateCallback<CortesFormato>() {
			
			public CortesFormato doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query query = session.getNamedQuery(CortesFormato.NAMED_QUERY_CORTES_FORMATO_ACTIVO_POR_FORMATO);				
				return (CortesFormato) query.uniqueResult();
			}
		};
		
		
		return getHibernateTemplate().execute(callback);
	}

	public CortesFormato obtenerCortesFormatoInActivoPorFormato(
			) {

		
		HibernateCallback<CortesFormato> callback = new HibernateCallback<CortesFormato>() {
			
			public CortesFormato doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query query = session.getNamedQuery(CortesFormato.NAMED_QUERY_CORTES_FORMATO_INACTIVO_POR_FORMATO);				
				List<CortesFormato> lista= (List<CortesFormato>)query.list();
				return lista.get(0);
			}
		};
		
		
		return getHibernateTemplate().execute(callback);
	}
	
	

}
