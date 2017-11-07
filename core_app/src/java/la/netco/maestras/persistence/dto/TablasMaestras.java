package la.netco.maestras.persistence.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;

@Entity
@Table(name = "TABLASMAESTRAS", schema = Schemas.REL_SCHEMA)
@NamedQuery(name=TablasMaestras.NAMED_QUERY_GET_ALL,query=TablasMaestras.QUERY_GET_ALL)
public class TablasMaestras implements Serializable {


	private static final long serialVersionUID = 4449083807217239489L;

	private int id_tma;

	private String nom_tma;
	
	private String eti_tma;
	

	public TablasMaestras() {
		this.id_tma = 0;
		this.nom_tma = "";
		this.eti_tma= "";
	}

	public TablasMaestras(int id_tma, String nom_tma,String eti_tma) {
		this.id_tma = id_tma;
		this.nom_tma = nom_tma;
		this.eti_tma = eti_tma;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_TMA", nullable = false, unique = true, insertable = false)
	public int getId_tma() {
		return id_tma;
	}

	public void setId_tma(int id_tma) {
		this.id_tma = id_tma;
	}

	@Column(name = "NOM_TMA", length = 50)
	public String getNom_tma() {
		return nom_tma;
	}

	public void setNom_tma(String nom_tma) {
		this.nom_tma = nom_tma;
	}

	
	@Override
	public String toString()
	{
		return "Id Tabla Maestra:" + id_tma + " - Nombre : " + id_tma;
	}
	
	@Column(name = "ETI_TMA", length =70)
	public String getEti_tma() {
		return eti_tma;
	}

	public void setEti_tma(String eti_tma) {
		this.eti_tma = eti_tma;
	}

	public static final String NAMED_QUERY_GET_ALL = "getAllTablasMaestras";
	public static final String  QUERY_GET_ALL = "from TablasMaestras tablasmaestras order by tablasmaestras.nom_tma asc";
	

}
