/**
 * 
 */
package la.netco.sgc.persistence.dao.impl;

import java.sql.SQLException;

import la.netco.core.persitencia.dao.commos.GenericCommonDaoHibernateImpl;
import la.netco.sgc.persistence.dao.RegistroRadicadoDao;
import la.netco.sgc.persistence.dto.RegistroRadicado;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Service;

/**
 * @author cguzman
 *
 */
@Service("registroRadicadoDao")
public class RegistroRadicadoDaoImpl extends GenericCommonDaoHibernateImpl implements RegistroRadicadoDao {


	/* (non-Javadoc)
	 * @see la.netco.sgc.persistence.dao.RegistroCargueFormatoDao#tomarRegistroCargueFormatoPorCargue(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	public RegistroRadicado tomarRegistroRadicado(Integer codEntidad, Integer codCorte) {		
		final Integer codEnt = codEntidad;
		final Integer codCrt = codCorte;		
	
		HibernateCallback<RegistroRadicado> callback = new HibernateCallback<RegistroRadicado>() {
			
			public RegistroRadicado doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query query = session.getNamedQuery(RegistroRadicado.NAMED_QUERY_REGISTRO_RADICADO_BY_ENTIDAD_AND_CORTE);				
				query.setParameter("corte", codCrt);
				query.setParameter("entidad", codEnt);								
				return (RegistroRadicado) query.uniqueResult();
			}
		};
		
		
		return getHibernateTemplate().execute(callback);
	}

	

}
