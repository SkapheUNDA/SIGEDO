package la.netco.core.persistencia.vo;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name = "seg_perfiles_usuarios", schema = Schemas.PRINCIPAL_SCHEMA)
@NamedQueries({
		@NamedQuery(name = PerfilUsuario.NAMED_QUERY_FIND_BY_PERFIL, query = PerfilUsuario.QUERY_FIND_BY_PERFIL),
		@NamedQuery(name = PerfilUsuario.NAMED_QUERY_FIND_BY_USUARIO, query = PerfilUsuario.QUERY_FIND_BY_USUARIO) })
public class PerfilUsuario implements Serializable {

	public PerfilUsuario(PerfilUsuarioPK compositePK) {
		this.compositePK = compositePK;
	}

	public PerfilUsuario() {
	}

	private static final long serialVersionUID = 7927418387785654961L;

	private PerfilUsuarioPK compositePK;

	public PerfilUsuario(Integer id_perfil, Short id_usuario) {
		setCompositePK(new PerfilUsuarioPK(id_perfil, id_usuario));
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "id_perfil", column = @Column(name = "id_perfil", nullable = false)),
			@AttributeOverride(name = "id_usuario", column = @Column(name = "id_usuario", nullable = false)) })
	public PerfilUsuarioPK getCompositePK() {
		return compositePK;
	}

	public void setCompositePK(PerfilUsuarioPK compositePK) {
		this.compositePK = compositePK;
	}


	@Override
	public String toString() {
		return "id_perfil : " + compositePK.getId_perfil() + " - id_usuario : "
				+ compositePK.getId_usuario();
	}

	public static final String NAMED_QUERY_FIND_BY_PERFIL = "findPerfilUsuarioByPerfil";
	public static final String QUERY_FIND_BY_PERFIL = "from PerfilUsuario perfil   WHERE perfil.compositePK.id_perfil=? ";

	public static final String NAMED_QUERY_FIND_BY_USUARIO = "findPerfilUsuarioByUsuario";
	public static final String QUERY_FIND_BY_USUARIO = "from PerfilUsuario perfil   WHERE perfil.compositePK.id_usuario=? ";

}
