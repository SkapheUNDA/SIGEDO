package la.netco.core.persistencia.vo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class GrupoUsuarioPK implements Serializable{

	
	private static final long serialVersionUID = 1468377022448257533L;
	private Integer id_grupo;
	private Short id_usuario;
	public GrupoUsuarioPK() {
	}
	
	public GrupoUsuarioPK(Integer idGrupo, Short idUsuario) {
		id_grupo = idGrupo;
		id_usuario = idUsuario;
	}

	@Column(name = "id_grupo", nullable = false)
	public Integer getId_grupo() {
		return id_grupo;
	}
	
	@Column(name = "id_usuario", nullable = false)
	public Short getId_usuario() {
		return id_usuario;
	}
	public void setId_grupo(Integer idGrupo) {
		id_grupo = idGrupo;
	}
	public void setId_usuario(Short idUsuario) {
		id_usuario = idUsuario;
	}
	
	@Override
	public String toString() {		
		return "id_grupo : "+id_grupo+" - id_usuario : " + id_usuario;
	}
}
