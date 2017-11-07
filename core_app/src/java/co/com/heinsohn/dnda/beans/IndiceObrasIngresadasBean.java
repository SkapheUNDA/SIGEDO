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

import co.com.heinsohn.dnda.dto.AutoresIntervenientes;
import co.com.heinsohn.dnda.dto.IndiceLibroRegistro;
import co.com.heinsohn.dnda.dto.IndiceObrasIngresadas;

/**
 * Bean Informe Indice obras ingresadas
 *
 * @author gmedellin
 *
 */
@ManagedBean(name = "indiceObrasIngresadasBean")
@ViewScoped
public class IndiceObrasIngresadasBean implements Serializable {

	/* Log */
	private static Logger log = LoggerFactory
			.getLogger(IndiceObrasIngresadasBean.class);

	/* Atributos de nivel Visual Filtros */
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;
	
	private java.util.Date fechaDesde;
	private java.util.Date fechaHasta;

	private int total;
	private IndiceObrasIngresadas indiceObrasIngresadas;
	private List<IndiceObrasIngresadas> listIndiceObrasIngresadas;

	/* Constructor */
	public IndiceObrasIngresadasBean() {
		listIndiceObrasIngresadas = new ArrayList<IndiceObrasIngresadas>();
		indiceObrasIngresadas = new IndiceObrasIngresadas();
		total = 0;
	}

	/**
	 * Buscar Reporte Indice obreas ingresadas
	 * */
	public void buscar() {
		
		log.info("Ingreso a buscar generar informe obras ingresadas");
		Connection con = serviceDao.getGenericCommonDao()
				.connectionFromHibernate();
		listIndiceObrasIngresadas = new ArrayList<IndiceObrasIngresadas>();
		
		try {
			// Pasa fechas de Java util a Java Sql
			java.sql.Date fechaDesdeSql = new java.sql.Date(fechaDesde.getTime());
			java.sql.Date fechaHastaSql = new java.sql.Date(fechaHasta.getTime());
			CallableStatement stm = con
					.prepareCall("{call dbo.SP_REP_OBRAS_ING(?, ?)}");
			stm.setDate(1, fechaDesdeSql);
			stm.setDate(2, fechaHastaSql);
			stm.setQueryTimeout(60);
			ResultSet rs = stm.executeQuery();
			log.info("Ingreso a recorrer rpta SP");
			while (rs.next()) {
				indiceObrasIngresadas = new IndiceObrasIngresadas();
				indiceObrasIngresadas.setClasificacion(rs.getString(1));
				indiceObrasIngresadas.setCantidad(rs.getInt(2));
				
				if (indiceObrasIngresadas.getClasificacion().equals("TOTAL")){
					total = indiceObrasIngresadas.getCantidad();
				}else{
					listIndiceObrasIngresadas.add(indiceObrasIngresadas);
				}
			}
		} catch (Exception e) {
			RequestContext.getCurrentInstance().execute(
					"alert('Error: " + e.getMessage() + "')");
			e.printStackTrace();
		}
	}

	public void listar(List<IndiceObrasIngresadas> lisReporte) {
		log.info("Recorrer List rpta");
		for (IndiceObrasIngresadas obrasIngresadas : lisReporte) {
			log.info("codigo" + obrasIngresadas.getClasificacion() + "-" + obrasIngresadas.getCantidad());
		}

	}

	/**
	 * Tamaño de la listas de los registros encontrados
	 *
	 * @return
	 */
	public String sizeReporte() {
		return Integer.toString(listIndiceObrasIngresadas.size());
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

	public IndiceObrasIngresadas getIndiceObrasIngresadas() {
		return indiceObrasIngresadas;
	}

	public void setIndiceObrasIngresadas(IndiceObrasIngresadas indiceObrasIngresadas) {
		this.indiceObrasIngresadas = indiceObrasIngresadas;
	}

	public List<IndiceObrasIngresadas> getListIndiceObrasIngresadas() {
		return listIndiceObrasIngresadas;
	}

	public void setListIndiceObrasIngresadas(
			List<IndiceObrasIngresadas> listIndiceObrasIngresadas) {
		this.listIndiceObrasIngresadas = listIndiceObrasIngresadas;
	}

	public static Logger getLog() {
		return log;
	}

	public static void setLog(Logger log) {
		IndiceObrasIngresadasBean.log = log;
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
