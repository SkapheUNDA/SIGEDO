package la.netco.core.persitencia.dao.commos;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import la.netco.commons.utils.CriteriaAlias;
import la.netco.core.persistencia.dao.impl.Auditable;
import la.netco.core.persistencia.vo.Auditoria;



import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;



@Service("genericCommonDao")
public class GenericCommonDaoHibernateImpl   extends HibernateDaoSupport implements GenericCommonDao{ 


//implements GenericDao{

	@Autowired
	@Resource(name = "sessionFactory")
	public void init(SessionFactory factory) {
		setSessionFactory(factory);
	}

	public GenericCommonDaoHibernateImpl() {

	}

    public GenericCommonDaoHibernateImpl(HibernateTemplate hibernateTemplate) {    
        setHibernateTemplate(hibernateTemplate);
    }
	
    
	public Object read(Class<?> type, Serializable id) {
        return  getHibernateTemplate().get(type, id);
   }
	@Auditable(tipoOperacion = Auditoria.OPERACION_SAVE)  
	@Transactional(propagation = Propagation.REQUIRED)
	public Serializable create(Class<?> type, Object o) {
        return  getHibernateTemplate().save(type.getName(), o);
  
    }

   	@Auditable(tipoOperacion = Auditoria.OPERACION_SAVE_OR_UPDATE)  
	@Transactional(propagation = Propagation.REQUIRED)
    public void saveOrUpdate(Class<?> type, Object o) {
         getHibernateTemplate().saveOrUpdate(type.getName(), o);
    }
   	
   	@Auditable(tipoOperacion = Auditoria.OPERACION_MERGE)  
	@Transactional(propagation = Propagation.REQUIRED)
    public Object merge(Class<?> type, Object o) {
        return  getHibernateTemplate().merge(type.getName(), o);
    }
    
	@Auditable(tipoOperacion = Auditoria.OPERACION_UPDATE)  
	@Transactional(propagation = Propagation.REQUIRED)
    public void update(Class<?> type, Object o) {
    	getHibernateTemplate().update(type.getName(), o);
    }

	@Auditable(tipoOperacion = Auditoria.OPERACION_DELETE)  
	@Transactional(propagation = Propagation.REQUIRED)
    public void delete(Class<?> type, Object o) {
    	getHibernateTemplate().delete(type.getName(), o);
    }

	
	public List<?> executeCriteriaFindPaged(Class<?> typeClass, final List<Criterion> criterios,	final Map<String, String> mapAlias ,  final int startRecord, final int endRecord,  final Order orderBy) {

		final Class<?> type  = typeClass;
	
		return getHibernateTemplate().executeFind(new HibernateCallback<Object>(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				Criteria criteria       = session.createCriteria(type);
							
				criteria.setFirstResult(startRecord);
				criteria.setMaxResults(endRecord);	
				
				if(mapAlias != null){
					for (Entry<String, String> alias : mapAlias.entrySet()) {
						criteria.createAlias(alias.getKey(), alias.getValue());
					}
				}
				if(criterios != null){
					for (Criterion iterable_element :  criterios) {
						
						criteria.add(iterable_element);

					}					
				}
				
				criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY) ;
				
				if(orderBy != null)
					criteria.addOrder(orderBy);
			
				
				return criteria.list();
			}
		});
	
	}
	public List<?> executeCriteriaFindPaged(Class<?> typeClass, final int startRecord, final int endRecord, final Order orderBy) {

		final Class<?> type  = typeClass;
	
		return getHibernateTemplate().executeFind(new HibernateCallback<Object>(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				Criteria criteria       = session.createCriteria(type);
							
				criteria.setFirstResult(startRecord);
				criteria.setMaxResults(endRecord);	
			
				if(orderBy != null)
					criteria.addOrder(orderBy);
			
				return criteria.list();
			}
		});
	
	}
	
	public List<?> executeCriteriaFindPaged(Class<?> typeClass, final List<Criterion> criterios, final int startRecord, final int endRecord, final Order orderBy ) {

		final Class<?> type  = typeClass;
	
		return getHibernateTemplate().executeFind(new HibernateCallback<Object>(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				Criteria criteria       = session.createCriteria(type);
							
				criteria.setFirstResult(startRecord);
				criteria.setMaxResults(endRecord);	
				
				if (criterios != null) {
					for (Criterion iterable_element : criterios) {
						criteria.add(iterable_element);
					}
				}
				
				if(orderBy != null)
					criteria.addOrder(orderBy);

				criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY) ;
				
				return criteria.list();
			}
		});
	
	}
	
	public Long executeCriteriaCount(Class<?> typeClass, final List<Criterion> criterios,	final Map<String, String> mapAlias) {

		final Class<?> type  = typeClass;

		Long count = (Long) getHibernateTemplate().execute(
				new HibernateCallback<Object>() {
					public Long doInHibernate(Session session)
							throws HibernateException, SQLException {

						Criteria criteria = session.createCriteria(type);

						if (mapAlias != null) {
							for (Entry<String, String> alias : mapAlias.entrySet()) {
								criteria.createAlias(alias.getKey(),alias.getValue());
							}
						}

						if (criterios != null) {
							for (Criterion iterable_element : criterios) {
								criteria.add(iterable_element);
							}
						}

						criteria.setProjection(Projections.rowCount());
						criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY) ;
						
						return ((Long) criteria.list().get(0));
					}
				});
		if (count != null)
			return count;
		else
			return null;
	}
	
	public List<?> executeCriteriaFind(Class<?> typeClass, final List<Criterion> criterios ) {

		final Class<?> type  = typeClass;
	
		return getHibernateTemplate().executeFind(new HibernateCallback<Object>(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				Criteria criteria       = session.createCriteria(type);		
				
				if (criterios != null) {
					for (Criterion iterable_element : criterios) {
						criteria.add(iterable_element);
					}
					
				}
				
				return criteria.list();
			}
		});
	
	}
	
	public List<?> loadAll(Class<?> type) {	    
	    return (List<?> ) getHibernateTemplate().loadAll(type);
	}

	public List<?> executeCriteriaFind(Class<?> typeClass, final List<Criterion> criterios ,	final Map<String, String> mapAlias ) {

		final Class<?> type  = typeClass;
	
		return getHibernateTemplate().executeFind(new HibernateCallback<Object>(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
			Criteria criteria       = session.createCriteria(type);	
			
				if(mapAlias != null){
					for (Entry<String, String> alias : mapAlias.entrySet()) {
						criteria.createAlias(alias.getKey(), alias.getValue());
					}
				}
				
				for (Criterion iterable_element :  criterios) {				
					criteria.add(iterable_element);
				}	
				return criteria.list();
			}
		});
	
	}
	
	
	public List<?> executeFind(final Class<?> type, final Map<Integer, ?> params, final String namedQuery) {
		
		return getHibernateTemplate().executeFind(new HibernateCallback<Object>(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.getNamedQuery(namedQuery);
				
				for (Entry<Integer, ?> iterable_element :  params.entrySet()) {
					Integer position = iterable_element.getKey();
					query.setParameter(position, iterable_element.getValue());
				}
				return query.list();
			}
		});
	
	}
	
	public List<?> executeFindPaged(final int startRecord, final int endRecord, final Map<Integer, ?> params, final String namedQuery) {
		
		return getHibernateTemplate().executeFind(new HibernateCallback<Object>(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.getNamedQuery(namedQuery).setFirstResult(startRecord).setMaxResults(endRecord);
				
				for (Entry<Integer, ?> iterable_element :  params.entrySet()) {
					Integer position = iterable_element.getKey();
					query.setParameter(position, iterable_element.getValue());
				}
				return query.list();
			}
		});
	
	}
	
	public int count(final Class<?> typeClass) {
		class ReturnValue {
			Long value;
		}
		final ReturnValue rv = new ReturnValue();
		final Class<?> type  = typeClass;
		getHibernateTemplate().execute(new HibernateCallback<Object>() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {			
				rv.value = (Long) session.createQuery("select count(*) from " + type.getSimpleName()).uniqueResult();
				return null;
			}
		});
		return rv.value.intValue();
	}

		public List<?> executeFind(final String namedQuery) {			
			return getHibernateTemplate().executeFind(new HibernateCallback<Object>(){
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					Query query = session.getNamedQuery(namedQuery);
				
					return query.list();
				}
			});
		
		}
		
		public Object executeCriteriaUniqueResult(final Class<?> type,final List<Criterion> criterios) {

		
			return (Object) getHibernateTemplate().execute(new HibernateCallback<Object>(){
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					
					Criteria criteria       = session.createCriteria(type);					
					for (Criterion iterable_element :  criterios) {				
						criteria.add(iterable_element);
					}			
					
					return criteria.uniqueResult();
				}
			});
		
		}
		
		public Object executeUniqueResult(final String namedQuery, final Map<Integer, ?> params) {

				
			return (Object) getHibernateTemplate().execute(new HibernateCallback<Object>(){
					public Object doInHibernate(Session session) throws HibernateException, SQLException {
						Query query = session.getNamedQuery(namedQuery);
						if(params != null){
							for (Entry<Integer, ?> iterable_element :  params.entrySet()) {
								Integer position = iterable_element.getKey();
								query.setParameter(position, iterable_element.getValue());
							}
						}						
						return query.uniqueResult();
					}
			});
			
		}
		
		
		public Long executeCriteriaCount(Class<?> typeClass, final List<Criterion> criterios,	final  List<CriteriaAlias> mapAlias) {

			final Class<?> type  = typeClass;

			Long count = (Long) getHibernateTemplate().execute(
					new HibernateCallback<Object>() {
						public Long doInHibernate(Session session)
								throws HibernateException, SQLException {

							Criteria criteria = session.createCriteria(type);

							if (mapAlias != null) {
								for (CriteriaAlias alias : mapAlias) {
									criteria.createAlias(alias.getColumna(),alias.getAlias(), alias.getTipo());
								}
							}

							if (criterios != null) {
								for (Criterion iterable_element : criterios) {
									criteria.add(iterable_element);
								}
							}
							criteria.setProjection(Projections.rowCount());
							criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY) ;
							
							return ((Long) criteria.list().get(0));
						}
					});
			if (count != null)
				return count;
			else
				return null;
		}
		
		public List<?> executeCriteriaFindPaged(Class<?> typeClass, final List<Criterion> criterios,	final  List<CriteriaAlias> mapAlias ,  final int startRecord, final int endRecord,  final Order orderBy) {

			final Class<?> type  = typeClass;
		
			return getHibernateTemplate().executeFind(new HibernateCallback<Object>(){
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					
					Criteria criteria       = session.createCriteria(type);
								
					criteria.setFirstResult(startRecord);
					criteria.setMaxResults(endRecord);	
					
					if (mapAlias != null) {
						for (CriteriaAlias alias : mapAlias) {
							criteria.createAlias(alias.getColumna(),alias.getAlias(), alias.getTipo());
						}
					}

					if(criterios != null){
						for (Criterion iterable_element :  criterios) {
							
							criteria.add(iterable_element);

						}					
					}
					
					criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY) ;
					
					if(orderBy != null)
						criteria.addOrder(orderBy);
				
					return criteria.list();
				}
			});
		
		}		

		public Long executeCriteriaCount(Class<?> typeClass, final List<Criterion> criterios,	final  List<CriteriaAlias> mapAlias, final Projection rowCounter) {

			final Class<?> type  = typeClass;

			Long count = (Long) getHibernateTemplate().execute(
					new HibernateCallback<Object>() {
						public Long doInHibernate(Session session)
								throws HibernateException, SQLException {

							Criteria criteria = session.createCriteria(type);

							if (mapAlias != null) {
								for (CriteriaAlias alias : mapAlias) {
									criteria.createAlias(alias.getColumna(),alias.getAlias(), alias.getTipo());
								}
							}

							if (criterios != null) {
								for (Criterion iterable_element : criterios) {
									criteria.add(iterable_element);
								}
							}
							
							if(rowCounter != null){

								criteria.setProjection(rowCounter);
							}else{

								criteria.setProjection(Projections.rowCount());
								criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY) ;
							}
							
							
							return ((Long) criteria.list().get(0));
						}
					});
			if (count != null)
				return count;
			else
				return null;
		}
		

	@SuppressWarnings("deprecation")
	public Connection connectionFromHibernate() {
		return getHibernateTemplate().getSessionFactory().getCurrentSession()
				.connection();
	}
	
	public Session getSessionFromHibernate() {
		return getSession();
	}
		
}
