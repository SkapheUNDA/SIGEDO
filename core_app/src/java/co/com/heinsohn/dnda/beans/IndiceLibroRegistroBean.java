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

/**
 * Bean Informe Indice Libro de registro
 *
 * @author ediaz
 *
 */
@ManagedBean(name = "indiceLibroRegistroBean")
@ViewScoped
public class IndiceLibroRegistroBean implements Serializable {

	/* Log */
	private static Logger log = LoggerFactory
			.getLogger(IndiceLibroRegistroBean.class);

	/* Atributos de nivel Visual Filtros */
	private static final long serialVersionUID = 5009426748293196316L;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;
	private String codRegistroN1;
	private Integer codRegistroN2;
	private Integer desde;
	private Integer hasta;

	private IndiceLibroRegistro indiceLibroRegistro;
	private List<IndiceLibroRegistro> listIndiceLibroRegistro;
	private AutoresIntervenientes autoresIntervenientes;
	private List<AutoresIntervenientes> listAutoresIntervenientes;
	
	private StringBuilder concatenaAutores;
	public static String nuevalinea = System.getProperty("line.separator");

	/* Constructor */
	public IndiceLibroRegistroBean() {
		listIndiceLibroRegistro = new ArrayList<IndiceLibroRegistro>();
		indiceLibroRegistro = new IndiceLibroRegistro();
		autoresIntervenientes = new AutoresIntervenientes();
		concatenaAutores = new StringBuilder();

	}

	/**
	 * Buscar Reporte Indice Libro de Registro Bean
	 * */
	public void buscar() {
		log.info("Ingreso a buscar" + getCodRegistroN1());
		Connection con = serviceDao.getGenericCommonDao()
				.connectionFromHibernate();
		listIndiceLibroRegistro = new ArrayList<IndiceLibroRegistro>();
		
		try {
			CallableStatement stm = con
					.prepareCall("{call dbo.SP_REP_REGISTRO_2004_00008(?, ?, ?,?)}");
			stm.setString(1, getCodRegistroN1());
			stm.setInt(2, getCodRegistroN2());
			stm.setInt(3, getDesde());
			stm.setInt(4, getHasta());
			stm.setQueryTimeout(60);
			ResultSet rs = stm.executeQuery();
			log.info("Ingreso a recorrer rpta SP");
			while (rs.next()) {
				indiceLibroRegistro = new IndiceLibroRegistro();
				int idRegistro = rs.getInt(1);
				indiceLibroRegistro.setId(idRegistro);
				indiceLibroRegistro.setCodigo(rs.getString(2).trim());
				// java.util.Date fechaRegistro = rs.getTimestamp(3);
				// indiceLibroRegistro.setFechaRegistro((Date) fechaRegistro);
				// Date fechaAuxDateSQL = rs.getDate(3);*/
				indiceLibroRegistro.setFechaRegistro(rs.getDate(3));
				

		
				indiceLibroRegistro.setTituloUobjeto(rs.getString(4).trim());
				/* Llamar segundo procedimiento datos Autores e intervenientes */
				int tipo = 1;
				CallableStatement stmAI = con
						.prepareCall("{call dbo.SP_REPAUX_REGISTRO_2004_000001(?, ?)}");
				stmAI.setInt(1, tipo);
				stmAI.setInt(2, idRegistro);
				stmAI.setQueryTimeout(60);
				ResultSet rsAI = stmAI.executeQuery();
				listAutoresIntervenientes = new ArrayList<AutoresIntervenientes>();
				concatenaAutores = new StringBuilder();
				while (rsAI.next()) {
					log.info("Ingreso a recorrer rpta SP Autores Intervenientes");
					autoresIntervenientes = new AutoresIntervenientes();
					autoresIntervenientes.setRol(rsAI.getString(3).trim());
					autoresIntervenientes.setAutorDoc(rsAI.getString(4).trim());
					autoresIntervenientes.setAutorNom(rsAI.getString(5).trim());
					listAutoresIntervenientes.add(autoresIntervenientes);
					concatenaAutores.append(autoresIntervenientes.getRol());
					concatenaAutores.append("\t");
					concatenaAutores.append(autoresIntervenientes.getAutorDoc());
					concatenaAutores.append("\t");
					concatenaAutores.append(autoresIntervenientes.getAutorNom());
					concatenaAutores.append(nuevalinea);
					concatenaAutores.toString();
					
					
				}
				log.info("Concatenar 1" + concatenaAutores.toString());
				if (listAutoresIntervenientes.size() > 0) {
					indiceLibroRegistro.setAutoresConcat(concatenaAutores);
					indiceLibroRegistro.setImprimirAutores(concatenaAutores.toString());
					log.info("Concatenar 2 " + indiceLibroRegistro.getImprimirAutores());
					indiceLibroRegistro
							.setAutoresIntervenientes(listAutoresIntervenientes);
				}
				
				/* Llenar lista final */
				listIndiceLibroRegistro.add(indiceLibroRegistro);
			}

			// listar(listIndiceLibroRegistro);
		} catch (Exception e) {
			RequestContext.getCurrentInstance().execute(
					"alert('Error: " + e.getMessage() + "')");
			e.printStackTrace();
		}

	}

	public void listar(List<IndiceLibroRegistro> lisReporte) {
		log.info("Recorrer List rpta");
		for (IndiceLibroRegistro indiceLibroRegistro : lisReporte) {
			log.info("codigo" + indiceLibroRegistro.getCodigo());
		}

	}

	/**
	 * Tamaño de la listas de los registros encontrados
	 *
	 * @return
	 */
	public String sizeReporte() {
		return Integer.toString(listIndiceLibroRegistro.size());
	}

	/* Setters and Getters */
	public String getCodRegistroN1() {
		return codRegistroN1;
	}

	public void setCodRegistroN1(String codRegistroN1) {
		this.codRegistroN1 = codRegistroN1;
	}

	public Integer getCodRegistroN2() {
		return codRegistroN2;
	}

	public void setCodRegistroN2(Integer codRegistroN2) {
		this.codRegistroN2 = codRegistroN2;
	}

	public Integer getDesde() {
		return desde;
	}

	public void setDesde(Integer desde) {
		this.desde = desde;
	}

	public Integer getHasta() {
		return hasta;
	}

	public void setHasta(Integer hasta) {
		this.hasta = hasta;
	}

	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	public List<IndiceLibroRegistro> getListIndiceLibroRegistro() {
		return listIndiceLibroRegistro;
	}

	public void setListIndiceLibroRegistro(
			List<IndiceLibroRegistro> listIndiceLibroRegistro) {
		this.listIndiceLibroRegistro = listIndiceLibroRegistro;
	}

	public StringBuilder getConcatenaAutores() {
		return concatenaAutores;
	}

	public void setConcatenaAutores(StringBuilder concatenaAutores) {
		this.concatenaAutores = concatenaAutores;
	}
	

}
