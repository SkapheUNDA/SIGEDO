package la.netco.core.persistencia.dao.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import la.netco.core.persistencia.dao.GenericDao;
import la.netco.core.persistencia.vo.Auditoria;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service("genericDao")
public class GenericDaoHibernateImpl <T, PK extends Serializable>  extends HibernateDaoSupport implements GenericDao<T, PK> {
   
	private Class<T> type;

	@Autowired
	@Resource(name="sessionFactory")
	public void init(SessionFactory factory) {
	    setSessionFactory(factory);
	}
	
	 public GenericDaoHibernateImpl() {
	
	 }
	
    public GenericDaoHibernateImpl(Class<T> type) {
        this.type = type;
    }
    
    public GenericDaoHibernateImpl(Class<T> type, HibernateTemplate hibernateTemplate) {
        this.type = type;
        setHibernateTemplate(hibernateTemplate);
    }

    @SuppressWarnings("unchecked")
   	@Auditable(tipoOperacion = Auditoria.OPERACION_SAVE)  
	@Transactional(propagation = Propagation.REQUIRED)
	public PK create(T o) {
        return (PK) getHibernateTemplate().save(o);
  
    }
    
    @SuppressWarnings("unchecked")
   	@Auditable(tipoOperacion = Auditoria.OPERACION_SAVE)  
	@Transactional(propagation = Propagation.REQUIRED)
	public PK create(Object o, String entityName) {
        return (PK) getHibernateTemplate().save(entityName, o);  
    }

   	@Auditable(tipoOperacion = Auditoria.OPERACION_SAVE_OR_UPDATE)  
	@Transactional(propagation = Propagation.REQUIRED)
    public void saveOrUpdate(T o) {
         getHibernateTemplate().saveOrUpdate(o);
    }
   	
   	@Auditable(tipoOperacion = Auditoria.OPERACION_MERGE)  
	@Transactional(propagation = Propagation.REQUIRED)
    public T merge(T o) {
        return (T) getHibernateTemplate().merge(o);
    }
    
	@Auditable(tipoOperacion = Auditoria.OPERACION_UPDATE)  
	@Transactional(propagation = Propagation.REQUIRED)
    public void update(T o) {
    	getHibernateTemplate().update(o);
    }

	@Auditable(tipoOperacion = Auditoria.OPERACION_DELETE)  
	@Transactional(propagation = Propagation.REQUIRED)
    public void delete(T o) {
    	getHibernateTemplate().delete(o);
    }

    
    public T read(PK id) {
         return (T) getHibernateTemplate().get(type, id);
    }

    public T read(Class<T> type, PK id ) {
    	   	
        return (T) getHibernateTemplate().get(type, id);
   }
    
   public Object readGenericIntegerId(Class<?> type, Integer id ) {	    	
        return getHibernateTemplate().get(type, id);
   }
    
    
    public List<T> loadAll(Class<T> type) {	    
    	
    
        return (List<T> ) getHibernateTemplate().loadAll(type);
   }


	public int count() {
		class ReturnValue {
			Long value;
		}
		final ReturnValue rv = new ReturnValue();
		final Class<T> type = this.type;
		getHibernateTemplate().execute(new HibernateCallback<Object>() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {			
				rv.value = (Long) session.createQuery("select count(*) from " + type.getSimpleName()).uniqueResult();
				return null;
			}
		});
		return rv.value.intValue();
	}
	
    
    @SuppressWarnings("unchecked")
	public List<T> executeFindPaged(final Map<Integer, ?> params, final String namedQuery,final int startRecord, final int endRecord) {

		
		return getHibernateTemplate().executeFind(new HibernateCallback<Object>(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.getNamedQuery(namedQuery);	
				query.setFirstResult(startRecord);
				query.setMaxResults(endRecord);						
				for (Entry<Integer, ?> iterable_element :  params.entrySet()) {
					Integer position = iterable_element.getKey();
					query.setParameter(position, iterable_element.getValue());
				}
			
				return query.list();
			}
		});
	
	}
    
    
    @SuppressWarnings("unchecked")
	public List<T> executeFind(final Map<Integer, ?> params, final String namedQuery) {

		
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
    
    
    @SuppressWarnings("unchecked")
	public List<T> executeFind( final String namedQuery) {

		
		return getHibernateTemplate().executeFind(new HibernateCallback<Object>(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.getNamedQuery(namedQuery);
			
				return query.list();
			}
		});
	
	}
    @SuppressWarnings("unchecked")
    public T executeUniqueResult( final String namedQuery, final Map<Integer, ?> params) {

		
		return (T) getHibernateTemplate().execute(new HibernateCallback<Object>(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.getNamedQuery(namedQuery);
				for (Entry<Integer, ?> iterable_element :  params.entrySet()) {
					Integer position = iterable_element.getKey();
					query.setParameter(position, iterable_element.getValue());
				}
				return query.uniqueResult();
			}
		});
	
	}
    
	
	@SuppressWarnings("unchecked")
	public List<T> executeFindPaged(final String namedQuery,final int startRecord, final int endRecord) {

		
		return getHibernateTemplate().executeFind(new HibernateCallback<Object>(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.getNamedQuery(namedQuery);
				query.setFirstResult(startRecord);
				query.setMaxResults(endRecord);				
				return query.list();
			}
		});
	
	}
	
	
	@SuppressWarnings("unchecked")
	public List<T> executeCriteriaFindPaged(final List<Criterion> criterios, final int startRecord, final int endRecord ) {

		final Class<T> type  = this.type;
	
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
				return criteria.list();
			}
		});
	
	}
	
	@SuppressWarnings("unchecked")
	public List<T> executeCriteriaFind(final List<Criterion> criterios ) {

		final Class<T> type  = this.type;
	
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
	
	@SuppressWarnings("unchecked")
	public List<T> executeCriteriaFind(final List<Criterion> criterios ,	final Map<String, String> mapAlias ) {

		final Class<T> type  = this.type;
	
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
	
	
	@SuppressWarnings("unchecked")
	public T executeCriteriaUniqueResult(final List<Criterion> criterios) {

		final Class<T> type  = this.type;
	
		return (T) getHibernateTemplate().execute(new HibernateCallback<Object>(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				Criteria criteria       = session.createCriteria(type);					
				for (Criterion iterable_element :  criterios) {				
					criteria.add(iterable_element);
				}			
				
				return criteria.uniqueResult();
			}
		});
	
	}
	
	
	@SuppressWarnings("unchecked")
	public List<T> executeCriteriaFindPaged(final List<Criterion> criterios,	final Map<String, String> mapAlias ,  final int startRecord, final int endRecord) {

		final Class<T> type  = this.type;
	
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
				for (Criterion iterable_element :  criterios) {
				
					criteria.add(iterable_element);

				}
				
				return criteria.list();
			}
		});
	
	}
	
	@SuppressWarnings("unchecked")
	public List<T> executeCriteriaFindPaged(final List<Criterion> criterios,	final Map<String, String> mapAlias ,  final int startRecord, final int endRecord , final String orderBy) {

		final Class<T> type  = this.type;
	
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
				for (Criterion iterable_element :  criterios) {
				
					criteria.add(iterable_element);

				}
				
				criteria.addOrder(Order.desc(orderBy));
				
				
				return criteria.list();
			}
		});
	
	}
	
	@SuppressWarnings("unchecked")
	public List<T> executeCriteriaFindPaged(final List<Criterion> criterios,	final Map<String, String> mapAlias ,  final int startRecord, final int endRecord , final String orderBy,final Map<String, Integer> joinType) {

		final Class<T> type  = this.type;
	
		return getHibernateTemplate().executeFind(new HibernateCallback<Object>(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				Criteria criteria       = session.createCriteria(type);
							
				criteria.setFirstResult(startRecord);
				criteria.setMaxResults(endRecord);	
				
				if(mapAlias != null){
					for (Entry<String, String> alias : mapAlias.entrySet()) {
						
						if(joinType != null && joinType.containsKey(alias.getKey())){
							criteria.createAlias(alias.getKey(), alias.getValue(), joinType.get(alias.getKey()));
						}else{
							criteria.createAlias(alias.getKey(), alias.getValue());
						}
						
						
			
					}
				}
				for (Criterion iterable_element :  criterios) {
				
					criteria.add(iterable_element);

				}
				
				criteria.addOrder(Order.desc(orderBy));
				
				
				return criteria.list();
			}
		});
	
	}
	
	public Long executeCriteriaCount(final List<Criterion> criterios) {

		final Class<T> type  = this.type;
	
		Long count =  (Long) getHibernateTemplate().execute(new HibernateCallback<Object>(){
			public Long doInHibernate(Session session) throws HibernateException, SQLException {
				
				Criteria criteria       = session.createCriteria(type);
													
				for (Criterion iterable_element :  criterios) {
				
					criteria.add(iterable_element);

				}
				criteria.setProjection(Projections.rowCount());
				return ((Long )criteria.list().get(0)); 
				
			}
		});
		if(count != null)
			return count;
			else return null;
	}
	
	public Long executeCriteriaCount(final List<Criterion> criterios ,	final Map<String, String> mapAlias) {
		
		final Class<T> type  = this.type;
	
		Long count =  (Long) getHibernateTemplate().execute(new HibernateCallback<Object>(){
			public Long doInHibernate(Session session) throws HibernateException, SQLException {
				
				Criteria criteria       = session.createCriteria(type);
			
				if(mapAlias != null){
					for (Entry<String, String> alias : mapAlias.entrySet()) {
						criteria.createAlias(alias.getKey(), alias.getValue());
					}
				}
				
				for (Criterion iterable_element :  criterios) {
				
					criteria.add(iterable_element);

				}
				
				
				criteria.setProjection(Projections.rowCount());
				
				
				return ((Long )criteria.list().get(0)); 
			}
		});
		if(count != null)
		return count;
		else return null;
	}
	
public Long executeCriteriaCountDistinctSQL(final List<Criterion> criterios ,	final Map<String, String> mapAlias, final String property ) {
		
		final Class<T> type  = this.type;
	
		Long count =  (Long) getHibernateTemplate().execute(new HibernateCallback<Object>(){
			public Long doInHibernate(Session session) throws HibernateException, SQLException {
				
				Criteria criteria       = session.createCriteria(type);
				
				
				
				//criteria.setProjection(Projections.countDistinct(property));
				
				criteria.setProjection(Projections.sqlProjection(" count (Distinct this_."+property+") as contador ", new String[]{"contador"}, new Type[]{Hibernate.LONG}));
				
				
				if(mapAlias != null){
					for (Entry<String, String> alias : mapAlias.entrySet()) {
						criteria.createAlias(alias.getKey(), alias.getValue());
					}
				}
				for (Criterion iterable_element :  criterios) {				
					criteria.add(iterable_element);
				}
				
				System.out.println(" count distint >>>>>> ");
				return ((Long )criteria.list().get(0)); 
			}
		});
		if(count != null)
		return count;
		else return null;
	}

public Long executeCriteriaCount(final List<Criterion> criterios ,	final Map<String, String> mapAlias, final Map<String, Integer> joinType  ) {
		
		final Class<T> type  = this.type;
	
		Long count =  (Long) getHibernateTemplate().execute(new HibernateCallback<Object>(){
			public Long doInHibernate(Session session) throws HibernateException, SQLException {
				
				Criteria criteria       = session.createCriteria(type);
			
				if(mapAlias != null){
					for (Entry<String, String> alias : mapAlias.entrySet()) {
						if(joinType != null && joinType.containsKey(alias.getKey())){
							criteria.createAlias(alias.getKey(), alias.getValue(), joinType.get(alias.getKey()));
						}else{
							criteria.createAlias(alias.getKey(), alias.getValue());
						}
						
					}
				}
				
				for (Criterion iterable_element :  criterios) {
				
					criteria.add(iterable_element);

				}
	criteria.setProjection(Projections.rowCount());
				
				return ((Long )criteria.list().get(0)); 
			}
		});
		if(count != null)
		return count;
		else return null;
	}
	
	
	public List<?> loadAllOrderBY(final Class<?> type,  final String orderBy, final String typeOrder ) {

	return getHibernateTemplate().executeFind(new HibernateCallback<Object>(){
		public Object doInHibernate(Session session) throws HibernateException, SQLException {
			
			Criteria criteria       = session.createCriteria(type);	
			if(typeOrder == null)
				criteria.addOrder(Order.asc(orderBy));
			else if (typeOrder.equals("asc")){
				criteria.addOrder(Order.asc(orderBy));
			}else if (typeOrder.equals("desc")){
				criteria.addOrder(Order.desc(orderBy));
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

	public Object executeFindUniqueResult(final Class<?> type, final Map<Integer, ?> params, final String namedQuery) {
		
		return getHibernateTemplate().execute(new HibernateCallback<Object>(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.getNamedQuery(namedQuery);
				
				for (Entry<Integer, ?> iterable_element :  params.entrySet()) {
					Integer position = iterable_element.getKey();
					query.setParameter(position, iterable_element.getValue());
				}
				return query.uniqueResult();
			}
		});
	
	}

	
	public List<?> executeDetachedCriteria(final DetachedCriteria criteria, final int startRecord, final int endRecord ) {

	
		return getHibernateTemplate().executeFind(new HibernateCallback<Object>(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				Criteria criteria2       = criteria.getExecutableCriteria(getSession());
							
				criteria2.setFirstResult(startRecord);
				criteria2.setMaxResults(endRecord);	
				
				criteria2.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP );			
				return criteria2.list();
			}
		});
	
	}
	
	
	public void setType(Class<T> type) {
		this.type = type;
	}
    
    

 }
