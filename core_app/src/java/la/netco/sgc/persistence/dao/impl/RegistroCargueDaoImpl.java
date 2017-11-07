package la.netco.sgc.persistence.dao.impl;


import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Service;

import la.netco.core.persitencia.dao.commos.GenericCommonDaoHibernateImpl;
import la.netco.sgc.persistence.dao.RegistroCargueDao;
import la.netco.sgc.persistence.dto.Autorizaciones;
import la.netco.sgc.persistence.dto.RegistroCargue;

@Service("registroCargueDao")
public class RegistroCargueDaoImpl extends GenericCommonDaoHibernateImpl implements RegistroCargueDao {

	

	/**
	 * 
	 * @author cguzman
	 * @param idFormato
	 * 			{@link Integer}
	 * 
	 * @return {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<RegistroCargue> tomarRegistroCarguePorFormato(
			Integer idFormato) {
		final Integer codFormato= idFormato;
		return getHibernateTemplate().executeFind(
				new HibernateCallback<Object>() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session
								.getNamedQuery(RegistroCargue.NAMED_QUERY_GET_REGISTRO_CARGUE_BY_FORMATO);
						query.setParameter(0, codFormato);
						return query.list();
					}
				});
		
	}
	
	
	/**
	 * 
	 * @author cguzman
	 * 
	 * @param codFormato
	 * 			{@link Integer}
	 * 
	 * @param codEntidad
	 * 			{@link Integer}
	 * 
	 * @param codCorte
	 * 			{@link Integer}
	 * 
	 * 
	 * @return {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<RegistroCargue> tomarRegistrosCarguePorFormatoEntidadCorte(Integer codFormato, Integer codEntidad, Integer codCorte) {
		
		final Integer codFor = codFormato;
		final Integer codEnt = codEntidad;
		final Integer codCrt = codCorte;
	
		HibernateCallback<List<RegistroCargue>> callback = new HibernateCallback<List<RegistroCargue>>() {
			
			public List<RegistroCargue> doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query query = session.getNamedQuery(RegistroCargue.NAMED_QUERY_GET_REGISTRO_CARGUE_BY_FORMATO_ENTIDAD_CORTE);
				query.setParameter("codFormato", codFor);
				query.setParameter("codCorte", codCrt);
				query.setParameter("entCodigo", codEnt);
				
				return (List<RegistroCargue>) query.list();
			}
		};
		
		
		return getHibernateTemplate().executeFind(callback);
	}


	@SuppressWarnings("unchecked")
	public List<RegistroCargue> tomarRegistrosCarguePorFormatoEntidadCorteDetVal(Integer codFormato, Integer codEntidad, Integer codCorte,
			Integer detCodigo, String valor) {
		
		final Integer codFor = codFormato;
		final Integer codEnt = codEntidad;
		final Integer codCrt = codCorte;
		final Integer codDet = detCodigo;
		final String val = valor;
	
		HibernateCallback<List<RegistroCargue>> callback = new HibernateCallback<List<RegistroCargue>>() {
			
			public List<RegistroCargue> doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query query = session.getNamedQuery(RegistroCargue.NAMED_QUERY_GET_REGISTRO_CARGUE_BY_FORMATO_ENTIDAD_CORTE_DET_VALOR);
				query.setParameter("codFormato", codFor);
				query.setParameter("codCorte", codCrt);
				query.setParameter("entCodigo", codEnt);
				query.setParameter("detCod", codDet);
				query.setParameter("valor", val);
				
				return (List<RegistroCargue>) query.list();
			}
		};
		
		
		return getHibernateTemplate().executeFind(callback);
	}


	public RegistroCargue tomarRegistrosCarguePorFormatoEntidadCorteDetReg(Integer codFormato, Integer codEntidad, Integer codCorte,
			Integer detCodigo, Integer reg) {
		final Integer codFor = codFormato;
		final Integer codEnt = codEntidad;
		final Integer codCrt = codCorte;
		final Integer codDet = detCodigo;
		final Integer idReg = reg;
	
		HibernateCallback<RegistroCargue> callback = new HibernateCallback<RegistroCargue>() {
			
			public RegistroCargue doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query query = session.getNamedQuery(RegistroCargue.NAMED_QUERY_GET_REGISTRO_CARGUE_BY_FORMATO_ENTIDAD_CORTE_DET_REG);
				query.setParameter("codFormato", codFor);
				query.setParameter("codCorte", codCrt);
				query.setParameter("entCodigo", codEnt);
				query.setParameter("detCod", codDet);
				query.setParameter("reg", idReg);
				
				return (RegistroCargue) query.uniqueResult();
			}
		};
		
		
		return getHibernateTemplate().execute(callback);
	}


	@SuppressWarnings("unchecked")
	public List<RegistroCargue> tomarRegistrosCarguePorFormatoEntidadCorteDet(Integer codFormato, Integer codEntidad, Integer codCorte,
			Integer detCodigo) {
		
		final Integer codFor = codFormato;
		final Integer codEnt = codEntidad;
		final Integer codCrt = codCorte;
		final Integer codDet = detCodigo;
	
		HibernateCallback<List<RegistroCargue>> callback = new HibernateCallback<List<RegistroCargue>>() {
			
			public List<RegistroCargue> doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query query = session.getNamedQuery(RegistroCargue.NAMED_QUERY_GET_REGISTRO_CARGUE_BY_FORMATO_ENTIDAD_CORTE_DET);
				query.setParameter("codFormato", codFor);
				query.setParameter("codCorte", codCrt);
				query.setParameter("entCodigo", codEnt);
				query.setParameter("detCod", codDet);
				
				return (List<RegistroCargue>) query.list();
			}
		};
		
		
		return getHibernateTemplate().executeFind(callback);
	}
	

	/**
	 * 
	 * @author cguzman
	 * @param idCorte
	 * 			{@link Integer}
	 * 
	 * @return {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<RegistroCargue> tomarRegistroCarguePorCorte(
			Integer idCorte) {
		final Integer codCorte= idCorte;
		return getHibernateTemplate().executeFind(
				new HibernateCallback<Object>() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session
								.getNamedQuery(RegistroCargue.NAMED_QUERY_GET_REGISTRO_CARGUE_BY_CORTE);
						query.setParameter(0, codCorte);
						return query.list();
					}
				});
		
	}
	
	/**
	 * 
	 * @author cguzman
	 * @param idCorte
	 * 			{@link Integer}
	 * 
	 * @return {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<RegistroCargue> tomarRegistroCarguePorCampo(
			Integer idCampo) {
		final Integer codCampo= idCampo;
		return getHibernateTemplate().executeFind(
				new HibernateCallback<Object>() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session
								.getNamedQuery(RegistroCargue.NAMED_QUERY_GET_REGISTRO_CARGUE_BY_CAMPO);
						query.setParameter(0, codCampo);
						return query.list();
					}
				});
		
	}


	public List<RegistroCargue> tomarRegistrosCarguePorFormatoEntidadCorteCuenta(
			Integer codFormato, Integer codEntidad, Integer codCorte,
			String cuenta, String valor) {
		final Integer codFor = codFormato;
		final Integer codEnt = codEntidad;
		final Integer codCrt = codCorte;
		final String cuenta1 = cuenta;
		final String valor1 = valor;
	
		HibernateCallback<List<RegistroCargue>> callback = new HibernateCallback<List<RegistroCargue>>() {
			
			public List<RegistroCargue> doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query query = session.getNamedQuery(RegistroCargue.NAMED_QUERY_GET_REGISTRO_CARGUE_BY_FORMATO_ENTIDAD_CORTE_CUENTA);
				query.setParameter("codFormato", codFor);
				query.setParameter("codCorte", codCrt);
				query.setParameter("entCodigo", codEnt);
				query.setParameter("valor", cuenta1);
				query.setParameter("valor1", valor1);
				
				return (List<RegistroCargue>) query.list();
			}
		};
		
		
		return getHibernateTemplate().executeFind(callback);
	}

}
