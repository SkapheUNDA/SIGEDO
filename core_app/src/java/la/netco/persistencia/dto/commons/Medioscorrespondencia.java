package la.netco.persistencia.dto.commons;

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
@Table(name = "MEDIOSCORRESPONDENCIA", schema = Schemas.DBO_SCHEMA)

@NamedQuery(name=Medioscorrespondencia.NAMED_QUERY_ALL,query=Medioscorrespondencia.QUERY_ALL)
public class Medioscorrespondencia implements Serializable {

	private static final long serialVersionUID = 1L;
	private short medId;
    private String medNom;
    private boolean medLent;
    private boolean medLsal;
/*    private Set<Salida> salidas = new HashSet<Salida>(0);*/


    public static String MEDIO_REGISTRO_LINEA	= "17";
    public static String MEDIO_CONTACTENOS_SITIO_WEB = "23";
    public static Short MEDIO_SERVIENTREGA	= 11;
    
    public Medioscorrespondencia() {
    }

    public Medioscorrespondencia(short medId, boolean medLent, boolean medLsal) {
        this.medId = medId;
        this.medLent = medLent;
        this.medLsal = medLsal;
    }
/*
    public Medioscorrespondencia(short medId, String medNom, boolean medLent, boolean medLsal, Set<Salida> salidas) {
        this.medId = medId;
        this.medNom = medNom;
        this.medLent = medLent;
        this.medLsal = medLsal;
        this.salidas = salidas;
    }*/

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "MED_ID", unique = true, nullable = false)
    public short getMedId() {
        return this.medId;
    }

    public void setMedId(short medId) {
        this.medId = medId;
    }

    @Column(name = "MED_NOM", length = 50)
    public String getMedNom() {
        return this.medNom;
    }

    public void setMedNom(String medNom) {
        this.medNom = medNom;
    }

    @Column(name = "MED_LENT", nullable = false)
    public boolean isMedLent() {
        return this.medLent;
    }

    public void setMedLent(boolean medLent) {
        this.medLent = medLent;
    }

    @Column(name = "MED_LSAL", nullable = false)
    public boolean isMedLsal() {
        return this.medLsal;
    }

    public void setMedLsal(boolean medLsal) {
        this.medLsal = medLsal;
    }

/*    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "medioscorrespondencia")
    public Set<Salida> getSalidas() {
        return this.salidas;
    }

    public void setSalidas(Set<Salida> salidas) {
        this.salidas = salidas;
    }*/
    
    public static final String NAMED_QUERY_ALL = "getAllMediosCorrespondencia";
    public static final String QUERY_ALL = "FROM Medioscorrespondencia medio order by medio.medNom asc";
       
}
