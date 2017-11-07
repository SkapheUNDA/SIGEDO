package la.netco.persistencia.dto.commons;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

import la.netco.core.persistencia.vo.Schemas;


@Entity
@Table(name = "TIPOREGISTROPERSONA", schema = Schemas.DBO_SCHEMA)
@NamedQuery(name=Tiporegistropersona.NAMED_QUERY_GET_BY_CLASE,query=Tiporegistropersona.QUERY_GET_BY_CLASE)
public class Tiporegistropersona implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private byte trpId;
    private String trpNom;
   // private Set<Actividadclaseregistro> actividadclaseregistros = new HashSet<Actividadclaseregistro>(0);
    private Set<Claseregistro> claseregistros = new HashSet<Claseregistro>(0);
   // private Set<Registropersona> registropersonas = new HashSet<Registropersona>(0);

    public Tiporegistropersona() {
    }

    public Tiporegistropersona(byte trpId) {
        this.trpId = trpId;
    }

    public Tiporegistropersona(byte trpId, String trpNom, Set<Actividadclaseregistro> actividadclaseregistros, Set<Claseregistro> claseregistros, Set<Registropersona> registropersonas) {
        this.trpId = trpId;
        this.trpNom = trpNom;
     //   this.actividadclaseregistros = actividadclaseregistros;
        this.claseregistros = claseregistros;
       // this.registropersonas = registropersonas;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "TRP_ID", unique = true, nullable = false)
    public byte getTrpId() {
        return this.trpId;
    }

    public void setTrpId(byte trpId) {
        this.trpId = trpId;
    }

    @Column(name = "TRP_NOM", length = 50)
    public String getTrpNom() {
        return this.trpNom;
    }

    public void setTrpNom(String trpNom) {
        this.trpNom = trpNom;
    }

/*    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tiporegistropersona")
    public Set<Actividadclaseregistro> getActividadclaseregistros() {
        return this.actividadclaseregistros;
    }

    public void setActividadclaseregistros(Set<Actividadclaseregistro> actividadclaseregistros) {
        this.actividadclaseregistros = actividadclaseregistros;
    }*/

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "CLASEREGISTROPERSONA",  schema = Schemas.DBO_SCHEMA, joinColumns = {
        @JoinColumn(name = "CRP_TRP", nullable = false, updatable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "CRP_CRE", nullable = false, updatable = false)})
    public Set<Claseregistro> getClaseregistros() {
        return this.claseregistros;
    }

    public void setClaseregistros(Set<Claseregistro> claseregistros) {
        this.claseregistros = claseregistros;
    }

  /*  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tiporegistropersona")
    public Set<Registropersona> getRegistropersonas() {
        return this.registropersonas;
    }

    public void setRegistropersonas(Set<Registropersona> registropersonas) {
        this.registropersonas = registropersonas;
    }*/
    
    public static final String NAMED_QUERY_GET_BY_CLASE = "getTiporegistropersonaByClaseregistro";
    public static final String QUERY_GET_BY_CLASE = "SELECT tipo FROM Tiporegistropersona tipo  INNER JOIN tipo.claseregistros clase WHERE  clase.creId = ? order by tipo.trpNom asc";

}
