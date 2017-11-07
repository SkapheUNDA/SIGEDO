/**
 * 
 */
package la.netco.sgc.persistence.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Service;

import la.netco.core.persitencia.dao.commos.GenericCommonDaoHibernateImpl;
import la.netco.sgc.persistence.dao.FormatosTiposDatoDao;
import la.netco.sgc.persistence.dto.FormatosTiposDato;

/**
 * @author carlos
 *
 */
@Service("formatosTiposDatoDao")
public class FormatosTiposDatosDaoImpl extends GenericCommonDaoHibernateImpl implements FormatosTiposDatoDao {
	
	/**
	 * 
	 * @author cguzman
	 * @param idTipoDato
	 * 			{@link Integer}
	 * 
	 * @return {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<FormatosTiposDato> tomarFormatosTiposDatoPorTiposDato(
			Integer idTipoDato) {
		final Integer codTipoDato = idTipoDato;
		return getHibernateTemplate().executeFind(
				new HibernateCallback<Object>() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session
								.getNamedQuery(FormatosTiposDato.TOMAR_FORMATOS_TIPOS_DATO_POR_TIPO_DATO);
						query.setParameter(0, codTipoDato);
						return query.list();
					}
				});
		
	}



}
