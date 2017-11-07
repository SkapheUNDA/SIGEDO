package la.netco.persistencia.dto.commons;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

import la.netco.core.persistencia.vo.Schemas;


@Entity
@Table(name = "PROPIEDAD", schema = Schemas.DBO_SCHEMA)
@NamedQuery(name=Propiedad.NAMED_QUERY_GET_BY_CLASE,query=Propiedad.QUERY_GET_BY_CLASE)
public class Propiedad implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	private int proId;
    private String proNom;
    private Byte proTip;
    private String proDes;
    private Set<Claseregistro> claseregistros = new HashSet<Claseregistro>(0);
    private Set<Elemento> elementos = new HashSet<Elemento>(0);

    public Propiedad() {
    }

    public Propiedad(int proId) {
        this.proId = proId;
    }

    public Propiedad(int proId, String proNom, Byte proTip, String proDes, Set<Claseregistro> claseregistros, Set<Elemento> elementos) {
        this.proId = proId;
        this.proNom = proNom;
        this.proTip = proTip;
        this.proDes = proDes;
        this.claseregistros = claseregistros;
        this.elementos = elementos;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "PRO_ID", unique = true, nullable = false)
    public int getProId() {
        return this.proId;
    }

    public void setProId(int proId) {
        this.proId = proId;
    }

    @Column(name = "PRO_NOM", length = 50)
    public String getProNom() {
        return this.proNom;
    }

    public void setProNom(String proNom) {
        this.proNom = proNom;
    }

    @Column(name = "PRO_TIP")
    public Byte getProTip() {
        return this.proTip;
    }

    public void setProTip(Byte proTip) {
        this.proTip = proTip;
    }

    @Column(name = "PRO_DES", length = 300)
    public String getProDes() {
        return this.proDes;
    }

    public void setProDes(String proDes) {
        this.proDes = proDes;
    }

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "PROPIEDADREGISTRO", schema = "dbo", catalog = "AUTOR", joinColumns = {
        @JoinColumn(name = "PRE_PRO", nullable = false, updatable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "PRE_CRE", nullable = false, updatable = false)})
    public Set<Claseregistro> getClaseregistros() {
        return this.claseregistros;
    }

    public void setClaseregistros(Set<Claseregistro> claseregistros) {
        this.claseregistros = claseregistros;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "propiedad")
    public Set<Elemento> getElementos() {
        return this.elementos;
    }

    public void setElementos(Set<Elemento> elementos) {
        this.elementos = elementos;
    }
    
    public static final String NAMED_QUERY_GET_BY_CLASE = "getPropiedadByClaseregistro";
    public static final String QUERY_GET_BY_CLASE = "SELECT propiedad FROM Propiedad propiedad  INNER JOIN propiedad.claseregistros clase WHERE  clase.creId = ? order by propiedad.proNom asc";

}
