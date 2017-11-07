/**
 * 
 */
package la.netco.sgc.persistence.dao.impl;

import java.sql.SQLException;
import java.util.List;

import la.netco.core.persitencia.dao.commos.GenericCommonDaoHibernateImpl;
import la.netco.sgc.persistence.dao.InformeEntidadDao;
import la.netco.sgc.persistence.dto.InformeEntidad;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Service;

/**
 * @author cguzman
 *
 */
@Service("informeEntidadDao")
public class InformeEntidadDaoImpl extends GenericCommonDaoHibernateImpl implements InformeEntidadDao {


	/* (non-Javadoc)
	 * @see la.netco.sgc.persistence.dao.RegistroCargueFormatoDao#tomarRegistroCargueFormatoPorCargue(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	public List<InformeEntidad> tomarInformesEntidad(Integer codEntidad) {
		
		final Integer codigoEntidad = codEntidad;
		return getHibernateTemplate().executeFind(
				new HibernateCallback<Object>() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session
								.getNamedQuery(InformeEntidad.NAMED_QUERY_INFORME_ENTIDAD_BY_ENTIDAD);
						query.setParameter(0, codigoEntidad);
						return query.list();
					}
				});		
		
	}
	
	
	public List<InformeEntidad> obtenerInformesFaltantes(final Integer codCorte) {

		return (List<InformeEntidad>) getHibernateTemplate().execute(
				new HibernateCallback<Object>() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						try {
							StringBuffer queryString = new StringBuffer();
							queryString
									.append("SELECT * FROM SGC.Informes_Entidad IE left JOIN SGC.Registro_Cargue_Formato CF on IE.ENT_Codigo=CF.ENT_Codigo and IE.FOR_Codigo=CF.FOR_Codigo and cf.FCR_Codigo=:codCorte inner join SGC.Entidades e  on ie.ENT_Codigo=e.ENT_Codigo where ie.Obligatorio=1 and cf.FOR_Codigo is null and cf.ENT_Codigo is null order by e.ENT_ObjetoSocial");
							Query query = session.createSQLQuery(
									queryString.toString()).addEntity(
									InformeEntidad.class);
							query.setParameter("codCorte", codCorte);
							return query.list();
						} catch (Exception e) {
							e.printStackTrace();
							return null;
						}

					}
				});

	}

	

}
