package la.netco.core.persistencia.vo;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Table(name = "seg_recurso", schema=Schemas.PRINCIPAL_SCHEMA)
@NamedQuery(name=Recurso.NAMED_QUERY_FIND_BY_PERFIL, query=Recurso.QUERY_FIND_BY_PERFIL)
public class Recurso implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;	
	private String 	keyRecurso;
	private String 	nombre;
	private String 	interceptUrl;
	private Modulo modulo;
	private Set<Perfil> perfiles = new HashSet<Perfil>();
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "REC_Id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	
	@Column(name = "REC_Keyrecurso", nullable = false, length = 6)
	public String getKeyRecurso() {
		return keyRecurso;
	}
	
	@Column(name = "REC_Nombre", nullable = true, length = 60)
	public String getNombre() {
		return nombre;
	}
	
	@Column(name = "REC_InterceptUrl", nullable = false, unique = true, length = 10)
	public String getInterceptUrl() {
		return interceptUrl;
	}
	
	@ManyToOne
	@JoinColumn(name = "MNU_Id", nullable = false)	
	@ForeignKey(name = "FK_seg_recurso_seg_modulo")
	@OnDelete(action = OnDeleteAction.CASCADE)	
	public Modulo getModulo() {
		return modulo;
	}
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "seg_recursos_perfiles",   schema=Schemas.PRINCIPAL_SCHEMA,
			joinColumns = { @JoinColumn(name = "REC_Id", nullable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "id_perfil", nullable = false) })
	@ForeignKey(name = "FK_seg_recursos_usuarios_seg_perfiles"   )
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	public Set<Perfil> getPerfiles() {
		return perfiles;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public void setInterceptUrl(String interceptUrl) {
		this.interceptUrl = interceptUrl;
	}
	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}
	public void setKeyRecurso(String keyRecurso) {
		this.keyRecurso = keyRecurso;
	}

	public void setPerfiles(Set<Perfil> perfiles) {
		this.perfiles = perfiles;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(obj != null && obj.toString().equals(toString())){
			return true;
		}else
			return false;
		
	}
	
	@Override
	public String toString() {
		return ""+id+"-"+keyRecurso;
	}
	
	public static final String NAMED_QUERY_FIND_BY_PERFIL= "findRecursoByPerfil";
	public static final String QUERY_FIND_BY_PERFIL = "SELECT recurso from Recurso recurso INNER JOIN recurso.perfiles perfil INNER JOIN recurso.modulo modulo  WHERE perfil.id=? order by modulo.nombre, recurso.nombre asc ";

	
	

}
