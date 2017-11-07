package la.netco.sgc.persistence.dao.impl;


import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Service;


import la.netco.sgc.persistence.dao.DetallesFormatoDao;
import la.netco.core.persitencia.dao.commos.GenericCommonDaoHibernateImpl;
import la.netco.sgc.persistence.dto.DetallesFormato;

@Service("detallesFormatoDao")
public class DetallesFormatoDaoImpl extends GenericCommonDaoHibernateImpl implements DetallesFormatoDao {

	
	/**
	 * @param codigoFormato
	 * 			{@link Integer}
	 * 
	 * @return {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<DetallesFormato> obtenerDetallesFormatoPorFormato(
			Integer codigoFormato) {
		// TODO Auto-generated method stub
		final Integer codFormato = codigoFormato;
		return getHibernateTemplate().executeFind(
				new HibernateCallback<Object>() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session
								.getNamedQuery(DetallesFormato.NAMED_QUERY_DETALLE_FORMATO_POR_FORMATO);
						query.setParameter("codigoFormato", codFormato);
						return query.list();
					}
				});
	}

}
