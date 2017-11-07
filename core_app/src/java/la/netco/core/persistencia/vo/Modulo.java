package la.netco.core.persistencia.vo;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "seg_modulo", schema=Schemas.PRINCIPAL_SCHEMA)
@NamedQueries(value = {  
@NamedQuery(name=Modulo.NAMED_QUERY_FIND_BY_PARENT_ID, query=Modulo.QUERY_FIND_BY_PARENT_ID),
@NamedQuery(name=Modulo.NAMED_QUERY_FIND_ROOTS, query=Modulo.QUERY_FIND_BY_ROOTS)
})
public class Modulo implements Serializable {

	
	private static final long serialVersionUID = 1L;
	private Integer id;	
	private String 	keyMod;
	private String 	nombre;
	private String 	reglaNavegacion;
	
	
	private Modulo parentMod;
	private List<Modulo> children;
	private List<Recurso> recursos;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "MNU_Id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	@Column(name = "MNU_Keymod", nullable = false, unique=true, length = 6)
	public String getKeyMod() {
		return keyMod;
	}
	
	@ManyToOne
	@JoinColumn(name = "MNU_IdPadre",  nullable = true)
	@ForeignKey(name = "FK_seg_modulo_seg_modulo")
	@OnDelete(action = OnDeleteAction.NO_ACTION)	
	public Modulo getParentMod() {
		return parentMod;
	}

	@OneToMany(mappedBy = "parentMod")
	public List<Modulo> getChildren() {
		return children;
	}

	@Column(name = "MNU_Navegacion", nullable = false, length = 10)
	public String getReglaNavegacion() {
		return reglaNavegacion;
	}

	@Column(name = "MNU_Nombre", nullable = true, length = 60)
	public String getNombre() {
		return nombre;
	}

	@OneToMany(mappedBy = "modulo")
	public List<Recurso> getRecursos() {
		return recursos;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setReglaNavegacion(String reglaNavegacion) {
		this.reglaNavegacion = reglaNavegacion;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	public void setParentMod(Modulo parentMod) {
		this.parentMod = parentMod;
	}
	public void setChildren(List<Modulo> children) {
		this.children = children;
	}

	public void setKeyMod(String keyMod) {
		this.keyMod = keyMod;
	}

	public void setRecursos(List<Recurso> recursos) {
		this.recursos = recursos;
	}
	@Transient
	public boolean isLeaf() {
          if (children == null || children.isEmpty()) {
              return true;
          }
          return false;
    }
	
	public static final String NAMED_QUERY_FIND_BY_PARENT_ID= "findModuloByParentId";
	public static final String QUERY_FIND_BY_PARENT_ID = "SELECT modulo from Modulo modulo INNER JOIN modulo.parentMod parent  WHERE parent.id=? order by modulo.nombre asc ";
	
	public static final String NAMED_QUERY_FIND_ROOTS	= "findModulosRoot";
	public static final String QUERY_FIND_BY_ROOTS	= "SELECT modulo from Modulo modulo WHERE modulo.parentMod.id IS NULL order by modulo.nombre asc ";


	
}
