package la.netco.persistencia.dto.commons;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

import la.netco.core.persistencia.vo.Schemas;

@Entity
@Table(name = "UBICACION", schema = Schemas.DBO_SCHEMA)
public class Ubicacion implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private int ubiId;
	private String ubiNom;
	private Set<Expediente> expedientes = new HashSet<Expediente>(0);

	public Ubicacion() {
	}

	public Ubicacion(int ubiId) {
		this.ubiId = ubiId;
	}

	public Ubicacion(int ubiId, String ubiNom, Set<Expediente> expedientes) {
		this.ubiId = ubiId;
		this.ubiNom = ubiNom;
		this.expedientes = expedientes;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "UBI_ID", unique = true, nullable = false)
	public int getUbiId() {
		return this.ubiId;
	}

	public void setUbiId(int ubiId) {
		this.ubiId = ubiId;
	}

	@Column(name = "UBI_NOM", length = 50)
	public String getUbiNom() {
		return this.ubiNom;
	}

	public void setUbiNom(String ubiNom) {
		this.ubiNom = ubiNom;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "ubicacion")
	public Set<Expediente> getExpedientes() {
		return this.expedientes;
	}

	public void setExpedientes(Set<Expediente> expedientes) {
		this.expedientes = expedientes;
	}
}
