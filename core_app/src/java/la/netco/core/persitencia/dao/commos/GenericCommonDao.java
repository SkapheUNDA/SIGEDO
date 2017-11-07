package la.netco.core.persitencia.dao.commos;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import la.netco.commons.utils.CriteriaAlias;
import la.netco.core.persistencia.dao.impl.Auditable;
import la.netco.core.persistencia.vo.Auditoria;

import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface GenericCommonDao {

	public abstract Object read(Class<?> type, Serializable id);

	@Transactional(propagation = Propagation.REQUIRED)
	@Auditable(tipoOperacion = Auditoria.OPERACION_SAVE) 
	public abstract Serializable create(Class<?> type, Object o);

	@Transactional(propagation = Propagation.REQUIRED)
	@Auditable(tipoOperacion = Auditoria.OPERACION_UPDATE)  
	public abstract void update(Class<?> type, Object o);

	@Transactional(propagation = Propagation.REQUIRED)
	@Auditable(tipoOperacion = Auditoria.OPERACION_DELETE)  
	public abstract void delete(Class<?> type, Object o);
	
	@Transactional(propagation = Propagation.REQUIRED)
	@Auditable(tipoOperacion = Auditoria.OPERACION_SAVE_OR_UPDATE) 
	public void saveOrUpdate(Class<?> type, Object o);
	
	@Transactional(propagation = Propagation.REQUIRED) 
	@Auditable(tipoOperacion = Auditoria.OPERACION_MERGE)
	public Object merge(Class<?> type, Object o) ;

	public abstract List<?> executeCriteriaFindPaged(Class<?> typeClass,
			final List<Criterion> criterios,
			final Map<String, String> mapAlias, final int startRecord,
			final int endRecord, final Order orderBy);

	public abstract List<?> executeCriteriaFindPaged(Class<?> typeClass,
			final int startRecord, final int endRecord, final Order orderBy);

	public abstract List<?> executeCriteriaFindPaged(Class<?> typeClass,
			final List<Criterion> criterios, final int startRecord,
			final int endRecord, final Order orderBy);

	public abstract Long executeCriteriaCount(Class<?> typeClass,
			final List<Criterion> criterios, final Map<String, String> mapAlias);

	public abstract List<?> executeCriteriaFind(Class<?> typeClass,
			final List<Criterion> criterios);

	public abstract List<?> loadAll(Class<?> type);

	public abstract List<?> executeCriteriaFind(Class<?> typeClass,
			final List<Criterion> criterios, final Map<String, String> mapAlias);

	public abstract List<?> executeFind(final Class<?> type,
			final Map<Integer, ?> params, final String namedQuery);

	public abstract int count(final Class<?> typeClass);
	
	public abstract List<?> executeFind( final String namedQuery);
	
	public abstract Object executeCriteriaUniqueResult(final Class<?> type,final List<Criterion> criterios);
	public abstract Object executeUniqueResult(final String namedQuery, final Map<Integer, ?> params);
	public List<?> executeFindPaged(final int startRecord, final int endRecord, final Map<Integer, ?> params, final String namedQuery);
	public Long executeCriteriaCount(Class<?> typeClass, final List<Criterion> criterios,	final  List<CriteriaAlias> mapAlias);
	public List<?> executeCriteriaFindPaged(Class<?> typeClass, final List<Criterion> criterios,	final  List<CriteriaAlias> mapAlias ,  final int startRecord, final int endRecord,  final Order orderBy);
	public Connection connectionFromHibernate();
	public Session getSessionFromHibernate();
	public Long executeCriteriaCount(Class<?> typeClass, final List<Criterion> criterios,	final  List<CriteriaAlias> mapAlias, final Projection rowCounter);


}