package la.netco.core.persistencia.vo;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "seg_grupos", schema=Schemas.PRINCIPAL_SCHEMA)
public class Grupo implements Serializable {


	private static final long serialVersionUID = 1069385091962635395L;
	private Integer id;
	private String nombre;
	private String nombreMostrar;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id_grupo", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	@Column(name = "nombre", unique = true, nullable = false, length = 100)
	public String getNombre() {
		return nombre;
	}
	@Column(name = "nombre_mostrar", nullable = false, length = 100)
	public String getNombreMostrar() {
		return nombreMostrar;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public void setNombreMostrar(String nombreMostrar) {
		this.nombreMostrar = nombreMostrar;
	}
	
	@Override
	public String toString() {
		return "id : " + id + " nombre : " + nombre ; 
	}
	
}
