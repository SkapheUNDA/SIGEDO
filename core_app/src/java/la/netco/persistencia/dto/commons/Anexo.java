package la.netco.persistencia.dto.commons;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import la.netco.core.persistencia.vo.Schemas;

@Entity
@Table(name = "ANEXO", schema = Schemas.DBO_SCHEMA)
@NamedQuery(name=Anexo.NAMED_QUERY_ALL_BY_TIPO_CLASIFICACION,query=Anexo.QUERY_ALL_BY_TIPO_CLASIFICACION)
public class Anexo implements Serializable {


	private static final long serialVersionUID = 1L;
	private short anxId;
    private String anxNom;
    private String anxAbr;
    //private Set<Anexosalida> anexosalidas = new HashSet<Anexosalida>(0);
    // private Set<Anexoentrada> anexoentradas = new HashSet<Anexoentrada>(0);
    private Set<Clasificacion> clasificacions = new HashSet<Clasificacion>(0);
    //private Set<Expedienteanexo> expedienteanexos = new HashSet<Expedienteanexo>(0);
    
    private Short numFolios;
    
    
    public Anexo() {
    }

    public Anexo(short anxId) {
        this.anxId = anxId;
    }

    public Anexo(short anxId, String anxNom, String anxAbr, Set<Anexosalida> anexosalidas, Set<Anexoentrada> anexoentradas, Set<Clasificacion> clasificacions, Set<Expedienteanexo> expedienteanexos) {
        this.anxId = anxId;
        this.anxNom = anxNom;
        this.anxAbr = anxAbr;
/*        this.anexosalidas = anexosalidas;
        this.anexoentradas = anexoentradas;*/
        this.clasificacions = clasificacions;
/*        this.expedienteanexos = expedienteanexos;*/
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ANX_ID", unique = true, nullable = false)
    public short getAnxId() {
        return this.anxId;
    }

    public void setAnxId(short anxId) {
        this.anxId = anxId;
    }

    @Column(name = "ANX_NOM", length = 50)
    public String getAnxNom() {
        return this.anxNom;
    }

    public void setAnxNom(String anxNom) {
        this.anxNom = anxNom;
    }

    @Column(name = "ANX_ABR", length = 20)
    public String getAnxAbr() {
        return this.anxAbr;
    }

    public void setAnxAbr(String anxAbr) {
        this.anxAbr = anxAbr;
    }

/*    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "anexo")
    public Set<Anexosalida> getAnexosalidas() {
        return this.anexosalidas;
    }

    public void setAnexosalidas(Set<Anexosalida> anexosalidas) {
        this.anexosalidas = anexosalidas;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "anexo")
    public Set<Anexoentrada> getAnexoentradas() {
        return this.anexoentradas;
    }

    public void setAnexoentradas(Set<Anexoentrada> anexoentradas) {
        this.anexoentradas = anexoentradas;
    }*/

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "ANEXOCLASIFICACION", schema = "dbo", catalog = "AUTOR", joinColumns = {
        @JoinColumn(name = "ANC_ANX", nullable = false, updatable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "ANC_CLA", nullable = false, updatable = false)})
    public Set<Clasificacion> getClasificacions() {
        return this.clasificacions;
    }

    public void setClasificacions(Set<Clasificacion> clasificacions) {
        this.clasificacions = clasificacions;
    }

/*    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "anexo")
    public Set<Expedienteanexo> getExpedienteanexos() {
        return this.expedienteanexos;
    }

    public void setExpedienteanexos(Set<Expedienteanexo> expedienteanexos) {
        this.expedienteanexos = expedienteanexos;
    }*/
    
    @Transient
    public Short getNumFolios() {
		return numFolios;
	}

	public void setNumFolios(Short numFolios) {
		this.numFolios = numFolios;
	}

	public static final String NAMED_QUERY_ALL_BY_TIPO_CLASIFICACION = "getAllAnexosByTipoClasificacion";
    public static final String QUERY_ALL_BY_TIPO_CLASIFICACION = "SELECT DISTINCT  anexo FROM Anexo anexo INNER JOIN anexo.clasificacions clasificacion WHERE  clasificacion.claTip = ? AND clasificacion.claId = ?  order by anexo.anxNom asc";
   
}
