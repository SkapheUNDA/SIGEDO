package la.netco.core.persistencia.vo;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "seg_perfiles", schema=Schemas.PRINCIPAL_SCHEMA)

@NamedQuery(name=Perfil.NAMED_QUERY_FIND_BY_RECURSO, query=Perfil.QUERY_FIND_BY_RECURSO)
public class Perfil implements Serializable {


	private static final long serialVersionUID = -1429265791507003432L;
	private Integer id;
	private String nombre;
	private String nombreMostrar;
	private List<Recurso> recursos;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id_perfil", unique = true, nullable = false)
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
	@ManyToMany(mappedBy = "perfiles", cascade= CascadeType.ALL )
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	public List<Recurso> getRecursos() {
		return recursos;
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
	public void setRecursos(List<Recurso> recursos) {
		this.recursos = recursos;
	}
	
	public static final String NAMED_QUERY_FIND_BY_RECURSO= "findPerfilByRecurso";
	public static final String QUERY_FIND_BY_RECURSO = "SELECT perfil from Perfil perfil INNER JOIN perfil.recursos recurso  WHERE recurso.id=? order by perfil.nombre asc ";


}
