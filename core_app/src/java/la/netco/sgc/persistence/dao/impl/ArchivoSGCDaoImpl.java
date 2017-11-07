package la.netco.sgc.persistence.dao.impl;


import java.sql.SQLException;
import java.util.List;

import la.netco.core.persitencia.dao.commos.GenericCommonDaoHibernateImpl;
import la.netco.sgc.persistence.dao.ArchivoSGCDao;
import la.netco.sgc.persistence.dto.ArchivoSGC;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Service;

@Service("archivoSGCDao")
public class ArchivoSGCDaoImpl extends GenericCommonDaoHibernateImpl implements ArchivoSGCDao {

	@Override
	public List<ArchivoSGC> obtenerArchivos(Integer codigoEntidad,
			Integer codigoFormato, String periodo, String ano, String consulta) {
			
				final Integer codEntidad = codigoEntidad;
				final Integer codFormato = codigoFormato;
				final String periodoH = periodo;
				final String anoH = ano;
				final String querys = consulta;
				return getHibernateTemplate().executeFind(
						new HibernateCallback<Object>() {
							public Object doInHibernate(Session session)
									throws HibernateException, SQLException {
								Query query = session
										.createQuery(querys);
								if (codEntidad != null && codEntidad != 0) {
									query.setParameter("codigoEntidad", codEntidad);
								}
								if (codFormato != null && codFormato != 0) {
									query.setParameter("codigoFormato", codFormato);	
								}
								if (periodoH != null && !periodoH.equals("")) {
									query.setParameter("periodo", periodoH);	
								}								
								if (anoH != null && !anoH.equals("")) {
									query.setParameter("ano", anoH);	
								}
								
								return query.list();
							}
						});
	}

	
	
	

}
