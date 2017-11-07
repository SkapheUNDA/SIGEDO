package la.netco.core.persistencia.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import la.netco.core.persistencia.dao.SpringGenericDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service("springGenericDao")
public class SpringGenericDaoImpl implements SpringGenericDao {

	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	public DataSource getDataSource() {
		return dataSource;
	}

	@Autowired
	@Resource(name = "dataSource")
	public void setDataSource(DataSource dataSource) {
		setJdbcTemplate(new JdbcTemplate(dataSource));
		this.dataSource = dataSource;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public List<Map<String, Object>> executeQuery(String query) {

		return jdbcTemplate.queryForList(query);

	}
	
	public List<Map<String, Object>> executeQuery(String query, Object... args) {

		return jdbcTemplate.queryForList(query, args);

	}

	public void insertQuery(String query) {
		jdbcTemplate.batchUpdate(new String[] { query });

	}

	public void updateQuery(String query) {
		jdbcTemplate.update(query);

	}
	
	public void updateQuery(String query, Object... args) {
		jdbcTemplate.update(query, args);

	}

}
