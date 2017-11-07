package la.netco.core.persistencia.vo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PerfilUsuarioPK implements Serializable{

	
	private static final long serialVersionUID = 3470184965137007172L;
	private Integer id_perfil;
	private Short id_usuario;
	
	public PerfilUsuarioPK() {
	}
	public PerfilUsuarioPK(Integer idPerfil, Short idUsuario) {
		id_perfil = idPerfil;
		id_usuario = idUsuario;
	}

	@Column(name = "id_perfil", nullable = false)
	public Integer getId_perfil() {
		return id_perfil;
	}
	
	@Column(name = "id_usuario", nullable = false)
	public Short getId_usuario() {
		return id_usuario;
	}
	public void setId_perfil(Integer idPerfil) {
		id_perfil = idPerfil;
	}
	public void setId_usuario(Short idUsuario) {
		id_usuario = idUsuario;
	}
	
	@Override
	public String toString() {		
		return "id_perfil : "+id_perfil+" - id_usuario : " + id_usuario;
	}
}
