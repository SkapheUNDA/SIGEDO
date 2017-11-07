package la.netco.example.persistence.dto;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;

//
//SE DEBE DEFINIR EL SCHEMA QUE USA EL MODULO
//
@Entity
@Table(name = "tbl_example", schema=Schemas.PRINCIPAL_SCHEMA)
public class Example implements Serializable  {

	
	private static final long serialVersionUID = -6682965823299263591L;

	private Integer id;
	private String nombre;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id_dept", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	
	@Column(name = "nombre", nullable = false, length = 100)
	public String getNombre() {
		return nombre;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
}
