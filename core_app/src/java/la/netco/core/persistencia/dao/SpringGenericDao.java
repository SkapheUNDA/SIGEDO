package la.netco.core.persistencia.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

public interface SpringGenericDao {

	
	public List<Map<String, Object>>  executeQuery(String query);
	
	public List<Map<String, Object>>  executeQuery(String query, Object... args);
	
	public void insertQuery(String query);
	
	public void updateQuery(String query);
	
	public void updateQuery(String query, Object... args);
	
	public JdbcTemplate getJdbcTemplate();
	
}
