package co.com.heinsohn.dnda.beans;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.spring.BeansSpringConst;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import co.com.heinsohn.dnda.dto.ConsultaCantidadRegistros;

/**
 * Bean Informe cantidad registros fisicos y en linea 
 *
 * @author gmedellin
 *
 */
@ManagedBean(name = "consultaCantidadRegistrosBean")
@ViewScoped
public class ConsultaCantidadRegistrosBean implements Serializable {

	/* Log */
	private static Logger log = LoggerFactory
			.getLogger(ConsultaCantidadRegistrosBean.class);

	/* Atributos de nivel Visual Filtros */
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;
	
	private java.util.Date fechaDesde;
	private java.util.Date fechaHasta;

	private int total;
	private ConsultaCantidadRegistros consultaCantidadRegistros;
	private List<ConsultaCantidadRegistros> listConsultaCantidadRegistros;

	/* Constructor */
	public ConsultaCantidadRegistrosBean() {
		listConsultaCantidadRegistros = new ArrayList<ConsultaCantidadRegistros>();
		consultaCantidadRegistros = new ConsultaCantidadRegistros();
		total = 0;
	}

	/**
	 * Buscar Reporte cantidad de registros fisicos y en linea 
	 * */
	public void buscar() {
		
		log.info("Ingreso a buscar generar informe cantidad de registros fisicos y en linea");
		Connection con = serviceDao.getGenericCommonDao()
				.connectionFromHibernate();
		listConsultaCantidadRegistros = new ArrayList<ConsultaCantidadRegistros>();
		
		try {
			// Pasa fechas de Java util a Java Sql
			java.sql.Date fechaDesdeSql = new java.sql.Date(fechaDesde.getTime());
			java.sql.Date fechaHastaSql = new java.sql.Date(fechaHasta.getTime());
			CallableStatement stm = con
					.prepareCall("{call dbo.SP_CAN_REG_FIS_LIN(?, ?)}");
			stm.setDate(1, fechaDesdeSql);
			stm.setDate(2, fechaHastaSql);
			stm.setQueryTimeout(60);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				consultaCantidadRegistros = new ConsultaCantidadRegistros();
				consultaCantidadRegistros.setUbicacion(rs.getString(1));
				consultaCantidadRegistros.setCantidad(rs.getInt(2));
				
				if (consultaCantidadRegistros.getUbicacion().equals("TOTAL")){
					total = consultaCantidadRegistros.getCantidad();
				}else{
					listConsultaCantidadRegistros.add(consultaCantidadRegistros);
				}
			}
		} catch (Exception e) {
			RequestContext.getCurrentInstance().execute(
					"alert('Error: " + e.getMessage() + "')");
			e.printStackTrace();
		}
	}

	public void listar(List<ConsultaCantidadRegistros> lisReporte) {
		log.info("Recorrer List rpta");
		for (ConsultaCantidadRegistros consultaReg : lisReporte) {
			log.info("codigo" + consultaReg.getUbicacion() + "-" + consultaReg.getCantidad());
		}

	}

	/**
	 * Tamao de la listas de los registros encontrados
	 *
	 * @return
	 */
	public String sizeReporte() {
		return Integer.toString(listConsultaCantidadRegistros.size());
	}

	/* Setters and Getters */

	public int getTotal() {
		return total;
	}

	public java.util.Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(java.util.Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public java.util.Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(java.util.Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public ConsultaCantidadRegistros getConsultaCantidadRegistros() {
		return consultaCantidadRegistros;
	}

	public void setConsultaCantidadRegistros(
			ConsultaCantidadRegistros consultaCantidadRegistros) {
		this.consultaCantidadRegistros = consultaCantidadRegistros;
	}

	public List<ConsultaCantidadRegistros> getListConsultaCantidadRegistros() {
		return listConsultaCantidadRegistros;
	}

	public void setListConsultaCantidadRegistros(
			List<ConsultaCantidadRegistros> listConsultaCantidadRegistros) {
		this.listConsultaCantidadRegistros = listConsultaCantidadRegistros;
	}

	public static Logger getLog() {
		return log;
	}

	public static void setLog(Logger log) {
		ConsultaCantidadRegistrosBean.log = log;
	}

	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
