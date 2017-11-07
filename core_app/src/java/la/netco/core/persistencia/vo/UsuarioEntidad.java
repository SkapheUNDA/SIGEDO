package la.netco.core.persistencia.vo;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name = "seg_usuarios_sgc_entidades", schema=Schemas.PRINCIPAL_SCHEMA)
public class UsuarioEntidad implements Serializable {


	private static final long serialVersionUID = 1L;


	public UsuarioEntidad(UsuarioEntidadPK compositePK) {
		this.compositePK = compositePK;
	}

	public UsuarioEntidad() {
	}
	
	private UsuarioEntidadPK compositePK;

	
	public UsuarioEntidad(Short idUsuario, Integer idEntidad){
		setCompositePK(new UsuarioEntidadPK(idUsuario, idEntidad));
	}
	
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "USR_ID", column = @Column(name = "USR_ID", nullable = false)),
			@AttributeOverride(name = "ENT_Codigo", column = @Column(name = "ENT_Codigo", nullable = false)) })
	public UsuarioEntidadPK getCompositePK() {
		return compositePK;
	}
	
	public void setCompositePK(UsuarioEntidadPK compositePK) {
		this.compositePK = compositePK;
	}

	
	@Override
	public String toString() {		
		return "id_usuario : "+compositePK.getId_usuario()+" - id_entidad : " + compositePK.getId_entidad();
	}


	
	@Embeddable
	public static class UsuarioEntidadPK implements Serializable{

		private static final long serialVersionUID = 1L;
		private Short id_usuario;
		private Integer id_entidad;
		
		public UsuarioEntidadPK() {
			
		}
		
		public UsuarioEntidadPK(Short idUsuario, Integer idEntidad) {
			id_usuario = idUsuario;
			id_entidad = idEntidad;
		}

		@Column(name = "USR_ID", nullable = false)
		public Short getId_usuario() {
			return id_usuario;
		}
		
		@Column(name = "ENT_Codigo", nullable = false)
		public Integer getId_entidad() {
			return id_entidad;
		}
		public void setId_usuario(Short idUsuario) {
			id_usuario = idUsuario;
		}
		public void setId_entidad(Integer idEntidad) {
			id_entidad = idEntidad;
		}
		
		@Override
		public String toString() {		
			return "id_usuario : "+id_usuario+" - id_entidad : " + id_entidad;
		}
		
	}
	
}
