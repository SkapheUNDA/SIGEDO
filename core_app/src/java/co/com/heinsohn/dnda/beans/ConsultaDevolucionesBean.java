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
import co.com.heinsohn.dnda.dto.ConsultaDevoluciones;


/**
 * Bean Informe de devoluciones
 *
 * @author gmedellin
 *
 */
@ManagedBean(name = "consultaDevolucionesBean")
@ViewScoped
public class ConsultaDevolucionesBean implements Serializable {

	/* Log */
	private static Logger log = LoggerFactory
			.getLogger(ConsultaDevolucionesBean.class);

	/* Atributos de nivel Visual Filtros */
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;
	
	private java.util.Date fechaDesde;
	private java.util.Date fechaHasta;
	private int total;
	
	private ConsultaDevoluciones consultaDevoluciones;
	private List<ConsultaDevoluciones> listConsultaDevoluciones;

	/* Constructor */
	public ConsultaDevolucionesBean() {
		listConsultaDevoluciones = new ArrayList<ConsultaDevoluciones>();
		consultaDevoluciones = new ConsultaDevoluciones();
		total = 0;
	}

	/**
	 * Buscar Reporte consulta devoluciones
	 * */
	public void buscar() {
		
		log.info("Ingreso a buscar generar informe obras ingresadas");
		Connection con = serviceDao.getGenericCommonDao()
				.connectionFromHibernate();
		listConsultaDevoluciones = new ArrayList<ConsultaDevoluciones>();
		
		try {
			// Pasa fechas de Java util a Java Sql
			java.sql.Date fechaDesdeSql = new java.sql.Date(fechaDesde.getTime());
			java.sql.Date fechaHastaSql = new java.sql.Date(fechaHasta.getTime());
			CallableStatement stm = con
					.prepareCall("{call dbo.SP_REP_DEVOL_DET(?, ?)}");
			stm.setDate(1, fechaDesdeSql);
			stm.setDate(2, fechaHastaSql);
			stm.setQueryTimeout(60);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				consultaDevoluciones = new ConsultaDevoluciones();
				consultaDevoluciones.setNumeroSalida(rs.getString(1));
				consultaDevoluciones.setAsunto(rs.getString(2));
				listConsultaDevoluciones.add(consultaDevoluciones);
			}
			total = listConsultaDevoluciones.size();			
		} catch (Exception e) {
			RequestContext.getCurrentInstance().execute(
					"alert('Error: " + e.getMessage() + "')");
			e.printStackTrace();
		}
	}

	public void listar(List<ConsultaDevoluciones> lisReporte) {
		log.info("Recorrer List rpta");
		for (ConsultaDevoluciones devolucion : lisReporte) {
			log.info("codigo" + devolucion.getAsunto() + "-" + devolucion.getNumeroSalida());
		}

	}

	/**
	 * Tamao de la listas de los registros encontrados
	 *
	 * @return
	 */
	public String sizeReporte() {
		return Integer.toString(listConsultaDevoluciones.size());
	}
	
	/* Setters and Getters */
	public static Logger getLog() {
		return log;
	}

	public static void setLog(Logger log) {
		ConsultaDevolucionesBean.log = log;
	}

	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
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

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public ConsultaDevoluciones getConsultaDevoluciones() {
		return consultaDevoluciones;
	}

	public void setConsultaDevoluciones(ConsultaDevoluciones consultaDevoluciones) {
		this.consultaDevoluciones = consultaDevoluciones;
	}

	public List<ConsultaDevoluciones> getListConsultaDevoluciones() {
		return listConsultaDevoluciones;
	}

	public void setListConsultaDevoluciones(
			List<ConsultaDevoluciones> listConsultaDevoluciones) {
		this.listConsultaDevoluciones = listConsultaDevoluciones;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
