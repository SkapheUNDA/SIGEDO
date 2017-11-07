package la.netco.core.persistencia.vo;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name = "seg_grupos_usuarios", schema=Schemas.PRINCIPAL_SCHEMA)
@NamedQuery(name=GrupoUsuario.NAMED_QUERY_FIND_BY_USUARIO, query=GrupoUsuario.QUERY_FIND_BY_BY_USUARIO)
public class GrupoUsuario implements Serializable {


	private static final long serialVersionUID = -207579207941704984L;


	public GrupoUsuario(GrupoUsuarioPK compositePK) {
		this.compositePK = compositePK;
	}

	public GrupoUsuario() {
	}

	
	private GrupoUsuarioPK compositePK;

	
	public GrupoUsuario(Integer id_perfil, Short id_usuario){
		setCompositePK(new GrupoUsuarioPK(id_perfil, id_usuario));
	}
	
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "id_grupo", column = @Column(name = "id_grupo", nullable = false)),
			@AttributeOverride(name = "id_usuario", column = @Column(name = "id_usuario", nullable = false)) })
	public GrupoUsuarioPK getCompositePK() {
		return compositePK;
	}
	
	public void setCompositePK(GrupoUsuarioPK compositePK) {
		this.compositePK = compositePK;
	}

	
	@Override
	public String toString() {		
		return "id_perfil : "+compositePK.getId_grupo()+" - id_usuario : " + compositePK.getId_usuario();
	}
	
	public static final String NAMED_QUERY_FIND_BY_USUARIO  = "findGrupoUsuarioByUsuario";
	public static final String QUERY_FIND_BY_BY_USUARIO 		= "from GrupoUsuario grupo   WHERE grupo.compositePK.id_grupo=? ";
	
}
