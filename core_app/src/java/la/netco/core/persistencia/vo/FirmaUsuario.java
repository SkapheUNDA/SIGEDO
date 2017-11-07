package la.netco.core.persistencia.vo;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "seg_firma_usuarios",schema=Schemas.PRINCIPAL_SCHEMA , uniqueConstraints = {@UniqueConstraint(columnNames = {"id_usuario"},  name="UQ_CONSTRAINT_ID_USUARIO_FIRMA" )})
@NamedQueries({
	@NamedQuery(name = FirmaUsuario.NAMED_QUERY_FIND_BY_USUARIO, query = FirmaUsuario.QUERY_FIND_BY_USUARIO),
	@NamedQuery(name = FirmaUsuario.NAMED_QUERY_FIND_BY_LOGIN, query = FirmaUsuario.QUERY_FIND_BY_LOGIN),
}) 
public class FirmaUsuario {

	private Integer id;
	private Usuario usuario;
	private String mimeType;
	private byte[]  firmaStream;
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	
	//FetchType.LAZY fue la solucion a:
	//Hibernate Error: org.hibernate.NonUniqueObjectException: a different object with the same identifier value was already associated with the session
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "id_usuario",  nullable = false)
	@ForeignKey(name = "FK_seg_firma_usuarios_seg_usuario")
	@OnDelete(action = OnDeleteAction.CASCADE)	
	public Usuario getUsuario() {
		return usuario;
	}
	
	@Column(name = "firmaStream", nullable = false, length=1024*1024*4)
	public byte[] getFirmaStream() {
		return firmaStream;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public void setFirmaStream(byte[] firmaStream) {
		this.firmaStream = firmaStream;
	}
	
	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public static final String NAMED_QUERY_FIND_BY_USUARIO  = "findFirmaByUsuario";
	public static final String QUERY_FIND_BY_USUARIO 		= " FROM FirmaUsuario firma WHERE firma.usuario.usrId = ? ";
	
	public static final String NAMED_QUERY_FIND_BY_LOGIN  = "findFirmaByLogin";
	public static final String QUERY_FIND_BY_LOGIN 		= " FROM FirmaUsuario firma WHERE firma.usuario.usrLog = ? ";
}
