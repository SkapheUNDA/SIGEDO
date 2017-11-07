package la.netco.core.persistencia.vo;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
@Entity
@Table(name = "aud_auditoria", schema=Schemas.PRINCIPAL_SCHEMA)
@NamedQueries(value = { 
	@NamedQuery(name = Auditoria.NAMED_QUERY_GET_ALL, query = Auditoria.QUERY_GET_ALL),
	@NamedQuery(name = Auditoria.NAMED_QUERY_GET_BY_VALOR_PROPIEDAD, query = Auditoria.QUERY_GET_BY_VALOR_PROPIEDAD)
}
)
public class Auditoria implements Serializable{


	private static final long serialVersionUID = 517264145706217261L;
	private Integer id;
	private String operacion;
	private String ipHost;
	private String nombreHost;
	private String userAgent;
	private String via;
	private String idioma;
	private Date fechaRegistro;
	private String propiedadesObjeto;
	private String valoresObjeto;
	private String returnValue;
	private String returnClass;
	private String username;
	private EntidadAuditada entidadAuditada;
	
	public static final String OPERACION_SAVE = "SAVE";
	public static final String OPERACION_SAVE_OR_UPDATE = "SAVE OR UPDATE";
	public static final String OPERACION_MERGE = "MERGE";
	public static final String OPERACION_UPDATE = "UPDATE";
	public static final String OPERACION_DELETE = "DELETE";
	
	private String propiedadID;
	private String valueID;
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	
	@Column(name = "operacion",  nullable = false , length = 15)
	public String getOperacion() {
		return operacion;
	}
	
	@Column(name = "ipHost",  nullable = false , length = 20)
	public String getIpHost() {
		return ipHost;
	}
	
	@Column(name = "nombreHost",  nullable = false , length = 255)
	public String getNombreHost() {
		return nombreHost;
	}
	
	@Column(name = "userAgent",  nullable = false , length = 255 )
	public String getUserAgent() {
		return userAgent;
	}
	
	@Column(name = "via",  nullable = true , length = 255)
	public String getVia() {
		return via;
	}
	
	@Column(name = "idioma",  nullable = false , length = 255 )
	public String getIdioma() {
		return idioma;
	}
	@Column(name = "fechaRegistro",  nullable = false)
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	
	@Column(name = "propiedadesObjeto" ,  nullable = false)
	public String getPropiedadesObjeto() {
		return propiedadesObjeto;
	}
	
	@Column(name = "valoresObjeto" ,  nullable = false)
	public String getValoresObjeto() {
		return valoresObjeto;
	}
	
		
	@Column(name = "returnValue",  nullable = true , length = 100)
	public String getReturnValue() {
		return returnValue;
	}
	
	@Column(name = "returnClass",  nullable = true , length = 100)
	public String getReturnClass() {
		return returnClass;
	}
	

	@Column(name = "username",  nullable = false, length = 25)
	public String getUsername() {
		return username;
	}
	
	@ManyToOne(fetch= FetchType.LAZY )
	@JoinColumn( name = "id_entidad_auditada", nullable = false)	
	@ForeignKey(name="FK_tut_auditoria_tut_entidad_auditada")
	@OnDelete(action=OnDeleteAction.CASCADE)
	public EntidadAuditada getEntidadAuditada() {
		return entidadAuditada;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}
	public void setIpHost(String ipHost) {
		this.ipHost = ipHost;
	}
	public void setNombreHost(String nombreHost) {
		this.nombreHost = nombreHost;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public void setVia(String via) {
		this.via = via;
	}
	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	public void setPropiedadesObjeto(String propiedadesObjeto) {
		this.propiedadesObjeto = propiedadesObjeto;
	}
	public void setValoresObjeto(String valoresObjeto) {
		this.valoresObjeto = valoresObjeto;
	}

	public void setReturnValue(String returnValue) {
		this.returnValue = returnValue;
	}
	public void setReturnClass(String returnClass) {
		this.returnClass = returnClass;
	}

	public void setEntidadAuditada(EntidadAuditada entidadAuditada) {
		this.entidadAuditada = entidadAuditada;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "propiedadID",  nullable = true , length = 50)
	public String getPropiedadID() {
		return propiedadID;
	}
	
	@Column(name = "valueID",  nullable = true , length = 100)
	public String getValueID() {
		return valueID;
	}

	public void setPropiedadID(String propiedadID) {
		this.propiedadID = propiedadID;
	}

	public void setValueID(String valueID) {
		this.valueID = valueID;
	}

	public static final String NAMED_QUERY_GET_ALL  = "getAllAuditorias";
	public static final String QUERY_GET_ALL 		= "from Auditoria auditoria order by  auditoria.id  desc ";

	
	public static final String NAMED_QUERY_GET_BY_VALOR_PROPIEDAD  = "getAuditoriaByValorPropiedad";
	public static final String QUERY_GET_BY_VALOR_PROPIEDAD		= " FROM Auditoria auditoria WHERE auditoria.propiedadID = ? AND auditoria.valueID  = ? AND  auditoria.entidadAuditada.id = ? order by  auditoria.id  desc ";

	
}
