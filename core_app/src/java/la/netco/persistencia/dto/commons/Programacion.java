package la.netco.persistencia.dto.commons;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import la.netco.core.persistencia.vo.Schemas;

@Entity
@Table(name = "PROGRAMACION", schema = Schemas.DBO_SCHEMA)
@NamedQueries({ @NamedQuery(name = Programacion.NAMED_QUERY_GET_BYDTL, query = Programacion.QUERY_GET_BYDTL) })
public class Programacion implements Serializable {

	private static final long serialVersionUID = 1L;
	private short prgId;
	private Depend depend;
	private Entidad entidad;
	private String prgNom;
	private String prgRes;
	private Set<Detalleprogramacion> detalleprogramacions = new HashSet<Detalleprogramacion>(
			0);

	public Programacion() {
	}

	public Programacion(short prgId) {
		this.prgId = prgId;
	}

	public Programacion(short prgId, Depend depend, Entidad entidad,
			String prgNom, String prgRes,
			Set<Detalleprogramacion> detalleprogramacions) {
		this.prgId = prgId;
		this.depend = depend;
		this.entidad = entidad;
		this.prgNom = prgNom;
		this.prgRes = prgRes;
		this.detalleprogramacions = detalleprogramacions;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PRG_ID", unique = true, nullable = false)
	public short getPrgId() {
		return this.prgId;
	}

	public void setPrgId(short prgId) {
		this.prgId = prgId;
	}

	/**
	 * Dependencia de orgien asociada a la programacion
	 * 
	 * @return Depend
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRG_DEP")
	public Depend getDepend() {
		return this.depend;
	}

	public void setDepend(Depend depend) {
		this.depend = depend;
	}

	/**
	 * Entidad o empresa al que presentada
	 * 
	 * @return Entidad igual a Empresa
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRG_PRE")
	public Entidad getEntidad() {
		return this.entidad;
	}

	public void setEntidad(Entidad entidad) {
		this.entidad = entidad;
	}

	/**
	 * Nombre de la programacion - Nombr del reporte segun diccionario de datos
	 * 
	 * @return NomProgramacion
	 */
	@Column(name = "PRG_NOM", length = 100)
	public String getPrgNom() {
		return this.prgNom;
	}

	public void setPrgNom(String prgNom) {
		this.prgNom = prgNom;
	}

	/**
	 * 
	 * Resolución ley o circular
	 * 
	 * @return resolucion , ley circular
	 */
	@Column(name = "PRG_RES", length = 100)
	public String getPrgRes() {
		return this.prgRes;
	}

	public void setPrgRes(String prgRes) {
		this.prgRes = prgRes;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "programacion")
	public Set<Detalleprogramacion> getDetalleprogramacions() {
		return this.detalleprogramacions;
	}

	public void setDetalleprogramacions(
			Set<Detalleprogramacion> detalleprogramacions) {
		this.detalleprogramacions = detalleprogramacions;
	}

	@Transient
	public List<Detalleprogramacion> getDetalles() {

		List<Detalleprogramacion> detalles = new ArrayList<Detalleprogramacion>();
		if (detalleprogramacions != null && !detalleprogramacions.isEmpty()) {
			for (Detalleprogramacion detalleprogramacion : detalleprogramacions) {
				detalles.add(detalleprogramacion);
			}
		}
		return detalles;
	}

	public static final String NAMED_QUERY_GET_BYDTL = "getByDtl";
	public static final String QUERY_GET_BYDTL = "FROM Programacion programacion INNER JOIN programacion.detalleprogramacions detalleprogramacion  where detalleprogramacion.usuario.usrId = ?";

}
