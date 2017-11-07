package co.com.heinsohn.dnda.dto;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;



/**
 * DTO control de informe indice libro de registro
 * @author ediaz
 *
 */

public class IndiceLibroRegistro implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String codigo;
	private Date fechaRegistro;
	private String tituloUobjeto;
	// list de Autores e Intervenientes
	private List<AutoresIntervenientes> autoresIntervenientes;
	private StringBuilder autoresConcat;
	private String imprimirAutores;
	
	
	
	
	public IndiceLibroRegistro() {
		
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	public String getTituloUobjeto() {
		return tituloUobjeto;
	}
	public void setTituloUobjeto(String tituloUobjeto) {
		this.tituloUobjeto = tituloUobjeto;
	}

	public List<AutoresIntervenientes> getAutoresIntervenientes() {
		return autoresIntervenientes;
	}

	public void setAutoresIntervenientes(
			List<AutoresIntervenientes> autoresIntervenientes) {
		this.autoresIntervenientes = autoresIntervenientes;
	}

	public StringBuilder getAutoresConcat() {
		return autoresConcat;
	}

	public void setAutoresConcat(StringBuilder autoresConcat) {
		this.autoresConcat = autoresConcat;
	}

	public String getImprimirAutores() {
		return imprimirAutores;
	}

	public void setImprimirAutores(String imprimirAutores) {
		this.imprimirAutores = imprimirAutores;
	}
	
	

	
	
	
	
	

}
