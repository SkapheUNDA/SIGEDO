/**
 * 
 */
package la.netco.sgc.persistence.dao.impl;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import la.netco.core.persitencia.dao.commos.GenericCommonDaoHibernateImpl;
import la.netco.sgc.persistence.dao.AutorizacionesDao;
import la.netco.sgc.persistence.dto.Autorizaciones;
import la.netco.sgc.persistence.dto.CortesFormato;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Service;

/**
 * @author cguzman
 *
 */
@Service("autorizacionesDao")
public class AutorizacionesDaoImpl extends GenericCommonDaoHibernateImpl implements AutorizacionesDao {

	
	public AutorizacionesDaoImpl() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @author cguzman
	 * @param forCodigo
	 * 			{@link Integer}
	 * 
	 * @return {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<Autorizaciones> obtenerAutorizacionesPorFormato(
			Integer forCodigo) {
		final Integer codFormato = forCodigo;
		return getHibernateTemplate().executeFind(
				new HibernateCallback<Object>() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session
								.getNamedQuery(Autorizaciones.NAMED_QUERY_GET_AUTORIZACIONES_BY_FORMATO);
						query.setParameter(0, codFormato);
						return query.list();
					}
				});	
	}
	
	/**
	 * 
	 * @param codFormato
	 * 			{@link Integer}
	 * @param codEntidad
	 * 			{@link Integer}
	 * 
	 * @return {@link Autorizaciones}
	 * 
	 */
	public Autorizaciones obtenerAutorizacionPorFormatoEntidadVigente(Integer codFormato, Integer codEntidad) {
		
		final Integer codFor = codFormato;
		final Integer codEnt = codEntidad;
		
		Calendar calInicial = Calendar.getInstance(), calFinal = Calendar.getInstance();
		calInicial.setTimeInMillis(System.currentTimeMillis());
		calFinal.setTimeInMillis(System.currentTimeMillis());
		calInicial.add(Calendar.DAY_OF_YEAR, -1);
		
		final Date fechaInicial = calInicial.getTime();
		final Date fechaFinal = calFinal.getTime();
		
		SimpleDateFormat  f = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		System.out.println("Fecha Inicial >>> " + f.format(fechaInicial));
		System.out.println("Fecha Final >>> " + f.format(fechaFinal));
		
		HibernateCallback<Autorizaciones> callback = new HibernateCallback<Autorizaciones>() {
			
			public Autorizaciones doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query query = session.getNamedQuery(Autorizaciones.NAMED_QUERY_GET_AUTORIZACIONES_BY_FORMATO_ENTIDADES_VIGENTE);
				query.setParameter("codFormato", codFor);
				query.setParameter("entCod", codEnt);
				query.setParameter("fechaInicial", fechaInicial);
				query.setParameter("fechaFinal", fechaFinal);

				return (Autorizaciones) query.uniqueResult();
			}
		};
		
		
		return getHibernateTemplate().execute(callback);
	}
	
}
