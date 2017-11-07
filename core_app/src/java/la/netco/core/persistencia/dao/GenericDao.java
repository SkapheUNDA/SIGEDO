package la.netco.core.persistencia.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import la.netco.core.persistencia.dao.impl.Auditable;
import la.netco.core.persistencia.vo.Auditoria;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate3.HibernateTemplate;


/**
 *  
 * GenericDao será desaprobada. En siguientes versiones se eliminara. Utilice {@link la.netco.core.persitencia.dao.commos.GenericCommonDao}
 * @author smontanez
 * @param <T> Entidad a implementar
 * @param <PK> Llave de la entidad
 */
public  interface  GenericDao <T, PK extends Serializable>  {
    
	@Auditable(tipoOperacion = Auditoria.OPERACION_SAVE)  
    PK create(T newInstance);
    @Auditable(tipoOperacion = Auditoria.OPERACION_SAVE)  
	public PK create(Object o, String entityName);
	@Auditable(tipoOperacion = Auditoria.OPERACION_SAVE_OR_UPDATE)  
	public void saveOrUpdate(T o) ;
	@Auditable(tipoOperacion = Auditoria.OPERACION_UPDATE)  
	void update(T transientObject);
	@Auditable(tipoOperacion = Auditoria.OPERACION_DELETE)  
	void delete(T persistentObject);
	@Auditable(tipoOperacion = Auditoria.OPERACION_MERGE) 
	public T merge(T o);
	T read(PK id);
  
    public HibernateTemplate getHibernateTemplate();
    
    public void setType(Class<T> type);
    public T read(Class<T> type, PK id) ;
    public List<T> loadAll(Class<T> type);
    public int count();
    public List<T> executeFindPaged(final Map<Integer, ?> params, final String namedQuery,final int startRecord, final int endRecord);
	public List<T> executeFindPaged(final String namedQuery,final int startRecord, final int endRecord) ;
	public Long executeCriteriaCount(final List<Criterion> criterios) ;
	public List<T> executeCriteriaFindPaged(final List<Criterion> criterios, final int startRecord, final int endRecord) ;
	public List<T> executeCriteriaFindPaged(final List<Criterion> criterios,	final Map<String, String> mapAlias ,  final int startRecord, final int endRecord) ;
	public Long executeCriteriaCount(final List<Criterion> criterios ,	final Map<String, String> mapAlias);
	public List<T> executeFind( final String namedQuery);
	public List<T> executeFind(final Map<Integer, ?> params, final String namedQuery);
    public T executeUniqueResult( final String namedQuery, final Map<Integer, ?> params);
	public List<T> executeCriteriaFind(final List<Criterion> criteriosr);
	public List<T> executeCriteriaFind(final List<Criterion> criterios ,	final Map<String, String> mapAlias);
	public T executeCriteriaUniqueResult(final List<Criterion> criterios);
	public List<T> executeCriteriaFindPaged(final List<Criterion> criterios,	final Map<String, String> mapAlias ,  final int startRecord, final int endRecord , final String orderBy);
	public List<T> executeCriteriaFindPaged(final List<Criterion> criterios,	final Map<String, String> mapAlias ,  final int startRecord, final int endRecord , final String orderBy,final Map<String, Integer> joinType);
	public Long executeCriteriaCount(final List<Criterion> criterios ,	final Map<String, String> mapAlias, final Map<String, Integer> joinType  );
	public Long executeCriteriaCountDistinctSQL(final List<Criterion> criterios ,	final Map<String, String> mapAlias,  final String property );
	
	public List<?> loadAllOrderBY(final Class<?> type,  final String orderBy, final String typeOrder );
	public List<?> executeFind(final Class<?> type, final Map<Integer, ?> params, final String namedQuery) ;
	public Object readGenericIntegerId(Class<?> type, Integer id ) ;
	public Object executeFindUniqueResult(final Class<?> type, final Map<Integer, ?> params, final String namedQuery);
	public List<?> executeDetachedCriteria(final DetachedCriteria criteria, final int startRecord, final int endRecord );
}
