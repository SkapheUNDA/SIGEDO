package la.netco.uilayer.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import la.netco.commons.utils.CriteriaAlias;
import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.businesslogic.services.impl.SpringApplicationContext;
import la.netco.core.persitencia.dao.commos.GenericCommonDao;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

public abstract class PrimeDataModel<T, PK extends Serializable> extends LazyDataModel<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	private Order orderBy;
	private List<Criterion> filtros;
	private List<CriteriaAlias> aliasCriteria;
	private Class<T> entityClass;
	private List<T> data;
	private Map<String, String> alias;
	
	private Projection rowCounter;
	
	
	public PrimeDataModel(Class<T> entityClass) {
	        super();
	        this.entityClass = entityClass;
	    }
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,String> filters) {		
		GenericCommonDao daoAccess = lookupDataProvider();
		if(aliasCriteria != null && !aliasCriteria.isEmpty()){
			data =  (List<T>) daoAccess.executeCriteriaFindPaged(entityClass, filtros,aliasCriteria, first, pageSize, orderBy);
			if(rowCounter == null )
				rowCount = daoAccess.executeCriteriaCount(entityClass, filtros, aliasCriteria);
			else
				rowCount = daoAccess.executeCriteriaCount(entityClass, filtros, aliasCriteria, rowCounter);
			
		}else{
			data =  (List<T>) daoAccess.executeCriteriaFindPaged(entityClass, filtros,alias, first, pageSize, orderBy);
			rowCount = daoAccess.executeCriteriaCount(entityClass, filtros, alias);
		}
		
		System.out.println(" first "  + first );

		System.out.println(" pageSize  " + pageSize );
		
		setPageSize(pageSize);
		return data;
	}
	
	private Long rowCount = null; 

	@Override
	public int getRowCount() {
		if(rowCount == null){
			GenericCommonDao daoAccess = lookupDataProvider();
			if(aliasCriteria != null && !aliasCriteria.isEmpty()){
				rowCount = daoAccess.executeCriteriaCount(entityClass, filtros, aliasCriteria);
			}else{
				rowCount = daoAccess.executeCriteriaCount(entityClass, filtros, alias);
			}
			
			System.out.println("  rowCount.intValue() " + rowCount);
			return rowCount.intValue();
		}else{
			return rowCount.intValue();
		}
		
	}
	
	private GenericCommonDao lookupDataProvider() {
    	ServiceDao dataProvider = (ServiceDao) SpringApplicationContext.getBean("serviceDao");
    	GenericCommonDao daoAccess = dataProvider.getGenericCommonDao();
    	return daoAccess;
    }
	
	@Override
	public Object getRowKey(T object) {
		return  getId(object);
	}

	@Override
	public T  getRowData(String rowKey) {
		for (T  object : data) {
			if (getId(object).equals(rowKey))
				return object;
		}

		return null;
	}

	public Order getOrderBy() {
		return orderBy;
	}

	public List<Criterion> getFiltros() {
		return filtros;
	}


	public void setOrderBy(Order orderBy) {
		this.orderBy = orderBy;
	}

	public void setFiltros(List<Criterion> filtros) {
		this.filtros = filtros;
	}


	public Class<T> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	protected abstract Object getId(T t);

	public List<CriteriaAlias> getAliasCriteria() {
		return aliasCriteria;
	}

	public void setAliasCriteria(List<CriteriaAlias> aliasCriteria) {
		this.aliasCriteria = aliasCriteria;
	}

	public Map<String, String> getAlias() {
		return alias;
	}

	public void setAlias(Map<String, String> alias) {
		this.alias = alias;
	}

	public Projection getRowCounter() {
		return rowCounter;
	}

	public void setRowCounter(Projection rowCounter) {
		this.rowCounter = rowCounter;
	}

}
