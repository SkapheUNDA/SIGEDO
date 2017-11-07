package la.netco.persistencia.dto.commons;

import javax.persistence.*;

import la.netco.core.persistencia.vo.Schemas;

@Entity
@Table(name = "EXPEDIENTEPERSONA", schema = Schemas.DBO_SCHEMA)
@NamedQueries({ @NamedQuery(name = Expedientepersona.NAMED_QUERY_GET_ALL_BYEXPPER, query = Expedientepersona.QUERY_GET_ALL_BYEXP) })
public class Expedientepersona implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private int epeId;
	private Tipospersona tipospersona;
	private Persona persona;
	private Entidad entidad;
	private Integer epeExp;
	private String epeDes;

	public Expedientepersona() {
	}

	public Expedientepersona(int epeId) {
		this.epeId = epeId;
	}

	public Expedientepersona(int epeId, Tipospersona tipospersona,
			Persona persona, Entidad entidad, Integer epeExp, String epeDes) {
		this.epeId = epeId;
		this.tipospersona = tipospersona;
		this.persona = persona;
		this.entidad = entidad;
		this.epeExp = epeExp;
		this.epeDes = epeDes;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "EPE_ID", unique = true, nullable = false)
	public int getEpeId() {
		return this.epeId;
	}

	public void setEpeId(int epeId) {
		this.epeId = epeId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EPE_TPE")
	public Tipospersona getTipospersona() {
		return this.tipospersona;
	}

	public void setTipospersona(Tipospersona tipospersona) {
		this.tipospersona = tipospersona;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EPE_PER")
	public Persona getPersona() {
		return this.persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EPE_ETD")
	public Entidad getEntidad() {
		return this.entidad;
	}

	public void setEntidad(Entidad entidad) {
		this.entidad = entidad;
	}

	@Column(name = "EPE_EXP")
	public Integer getEpeExp() {
		return this.epeExp;
	}

	public void setEpeExp(Integer epeExp) {
		this.epeExp = epeExp;
	}

	@Column(name = "EPE_DES", length = 300)
	public String getEpeDes() {
		return this.epeDes;
	}

	public void setEpeDes(String epeDes) {
		this.epeDes = epeDes;
	}

	public static final String NAMED_QUERY_GET_ALL_BYEXPPER = "getAllByExpPer";
	public static final String QUERY_GET_ALL_BYEXP = "FROM  Expedientepersona expedientepersona where expedientepersona.epeExp = ?";

}
