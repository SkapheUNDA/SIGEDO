package la.netco.sgc.persistence.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Service;

import la.netco.sgc.persistence.dao.AlertasDao;
import la.netco.core.persitencia.dao.commos.GenericCommonDaoHibernateImpl;
import la.netco.sgc.persistence.dto.Alertas;
import la.netco.sgc.persistence.dto.RegistroCargue;

@Service("alertasDao")
public class AlertasDaoImpl extends GenericCommonDaoHibernateImpl implements AlertasDao {
	
	
	public AlertasDaoImpl() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 
	 * @author cguzman
	 * @param forCodigo
	 * 			{@link Integer}
	 * 
	 * @return {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<Alertas> obtenerAlertasPorFormato(Integer forCodigo) {
		final Integer codFormato = forCodigo;
		return getHibernateTemplate().executeFind(
				new HibernateCallback<Object>() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session
								.getNamedQuery(Alertas.NAMED_QUERY_GET_ALERTAS_BY_FORMATO);
						query.setParameter(0, codFormato);
						return query.list();
					}
				});	
	}
	
	/**
	 * 
	 * @author cguzman
	 * @param codigoEntidad
	 * 			{@link Integer}
	 * 
	 * @param codigoFormato
	 * 			{@link Integer}
	 * 
	 * @param codigoCorteFormato
	 * 			{@link Integer}
	 * 
	 * @return {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<Alertas> obtenerAlertasFiltro(Integer codigoEntidad, Integer codigoFormato, List<Integer> codigoCorteFormato) {
		final Integer codFormato = codigoFormato;
		final Integer codEntidad = codigoEntidad;
		final List<Integer> codCorteFormato = codigoCorteFormato;
		
		String sql = "from Alertas a ";
		
		boolean esAsigandoWhere = false;
		
		if (codEntidad != null) {
			sql = sql + " where ";
			esAsigandoWhere = true;		
		} 
		
		if (codFormato != null) {
		
			if (!esAsigandoWhere) {
				sql += "where a.formatoOrigen.forCodigo = :forCodigo ";
				esAsigandoWhere = true;
			
			} else { 
				sql += " a.formatoOrigen.forCodigo = :forCodigo ";
			
			}		
		}
		
		if (codCorteFormato != null) {
			
			if (!esAsigandoWhere) {
				sql += "where a.cortesFormatoOrigen.fcrCodigo in (:fcrCodigos) ";
				esAsigandoWhere = true;
			
			} else { 
				sql += "and a.cortesFormatoOrigen.fcrCodigo in (:fcrCodigos) ";		
			}		
		}
		
		System.out.println("hql >>>> " + sql);
		final String hql = sql;
		
		
		return getHibernateTemplate().executeFind(
				new HibernateCallback<Object>() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createQuery(hql);
						
						if (codFormato != null)
							query.setParameter("forCodigo", codFormato);
						
						if (codCorteFormato != null)
							query.setParameter("fcrCodigos", codCorteFormato);
					
						return query.list();
					}
				});	
	}
	
	
	/**
	 * 
	 * @param codigoFormato
	 * @param codigoCorteFormato
	 * 
	 * @return {@link List}
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Alertas> obtenerAlertasFormatoCorte(Integer codigoFormato, Integer codigoCorteFormato) {
		final Integer codFormato = codigoFormato;
		final Integer codCorte = codigoCorteFormato;
	
		HibernateCallback<List<Alertas>> callback = new HibernateCallback<List<Alertas>>() {
			
			public List<Alertas> doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query query = session.getNamedQuery(Alertas.NAMED_QUERY_GET_ALERTAS_BY_FORMATO_CORTE);
				query.setParameter(0, codFormato);
				query.setParameter(1, codCorte);
				
				return (List<Alertas>) query.list();
			}
		};
		
		
		return getHibernateTemplate().executeFind(callback);
	}
	
	/**
	 * 
	 * @author cguzman
	 * @param fcrCodigo
	 * 			{@link Integer}
	 * 
	 * @return {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<Alertas> obtenerAlertasPorCorte(Integer fcrCodigo) {
		final Integer codCorte = fcrCodigo;
		return getHibernateTemplate().executeFind(
				new HibernateCallback<Object>() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session
								.getNamedQuery(Alertas.NAMED_QUERY_GET_ALERTAS_BY_CORTE);
						query.setParameter("codCorte", codCorte);
						return query.list();
					}
				});	
	}
	
	/**
	 * 
	 * @author cguzman
	 * @param detCodigo
	 * 			{@link Integer}
	 * 
	 * @return {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<Alertas> obtenerAlertasPorCampo(Integer detCodigo) {
		final Integer codDetalle = detCodigo;
		return getHibernateTemplate().executeFind(
				new HibernateCallback<Object>() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session
								.getNamedQuery(Alertas.NAMED_QUERY_GET_ALERTAS_BY_CAMPOS);
						query.setParameter("codDetalle", codDetalle);
						return query.list();
					}
				});	
	}

}
