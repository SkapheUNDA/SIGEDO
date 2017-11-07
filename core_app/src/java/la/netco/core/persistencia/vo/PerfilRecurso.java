package la.netco.core.persistencia.vo;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name = "seg_recursos_perfiles", schema=Schemas.PRINCIPAL_SCHEMA)
@NamedQuery(name=PerfilRecurso.NAMED_QUERY_FIND_BY_PERFIL, query=PerfilRecurso.QUERY_FIND_BY_PERFIL)
public class PerfilRecurso implements Serializable {


	private static final long serialVersionUID = 1L;


	public PerfilRecurso(PerfilRecursoPK compositePK) {
		this.compositePK = compositePK;
	}

	public PerfilRecurso() {
	}
	
	private PerfilRecursoPK compositePK;

	
	public PerfilRecurso(Integer id_perfil, Integer id_recurso){
		setCompositePK(new PerfilRecursoPK(id_perfil, id_recurso));
	}
	
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "id_perfil", column = @Column(name = "id_perfil", nullable = false)),
			@AttributeOverride(name = "REC_Id", column = @Column(name = "REC_Id", nullable = false)) })
	public PerfilRecursoPK getCompositePK() {
		return compositePK;
	}
	
	public void setCompositePK(PerfilRecursoPK compositePK) {
		this.compositePK = compositePK;
	}

	
	@Override
	public String toString() {		
		return "id_perfil : "+compositePK.getId_perfil()+" - id_recurso : " + compositePK.getId_recurso();
	}


	
	@Embeddable
	public static class PerfilRecursoPK implements Serializable{

		private static final long serialVersionUID = 1L;
		private Integer id_perfil;
		private Integer id_recurso;
		
		public PerfilRecursoPK() {
			
		}
		
		public PerfilRecursoPK(Integer idPerfil, Integer idRecurso) {
			id_perfil = idPerfil;
			id_recurso = idRecurso;
		}

		@Column(name = "id_perfil", nullable = false)
		public Integer getId_perfil() {
			return id_perfil;
		}
		
		@Column(name = "REC_Id", nullable = false)
		public Integer getId_recurso() {
			return id_recurso;
		}
		public void setId_perfil(Integer idPerfil) {
			id_perfil = idPerfil;
		}
		public void setId_recurso(Integer idRecurso) {
			id_recurso = idRecurso;
		}
		
		@Override
		public String toString() {		
			return "id_perfil : "+id_perfil+" - id_recurso : " + id_recurso;
		}
		
	}
	
	public static final String NAMED_QUERY_FIND_BY_PERFIL  = "findPerfilRecursoByPerfil";
	public static final String QUERY_FIND_BY_PERFIL 		= "from PerfilRecurso perfil   WHERE perfil.compositePK.id_perfil=? ";
	
}
