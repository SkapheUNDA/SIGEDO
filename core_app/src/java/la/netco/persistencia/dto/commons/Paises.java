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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;


@Entity
@Table(name = "PAISES", schema = Schemas.DBO_SCHEMA)
@NamedQuery(name=Paises.NAMED_QUERY_ALL,query=Paises.QUERY_ALL)
public class Paises implements Serializable {

	private static final long serialVersionUID = 1L;
	private short paiId;
    private String paiNom;
    private String paiNac;
    private String paiSgl;
/*    private Set<Salida> salidas = new HashSet<Salida>(0);
    private Set<Persona> personas = new HashSet<Persona>(0);*/
    private Set<Lugar> lugars = new HashSet<Lugar>(0);

    public Paises() {
    }

    public Paises(short paiId) {
        this.paiId = paiId;
    }

    public Paises(short paiId, String paiNom, String paiNac, String paiSgl, Set<Salida> salidas, Set<Persona> personas, Set<Lugar> lugars) {
        this.paiId = paiId;
        this.paiNom = paiNom;
        this.paiNac = paiNac;
        this.paiSgl = paiSgl;
/*      this.salidas = salidas;
        this.personas = personas;*/
        this.lugars = lugars;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "PAI_ID", unique = true, nullable = false)
    public short getPaiId() {
        return this.paiId;
    }

    public void setPaiId(short paiId) {
        this.paiId = paiId;
    }

    @Column(name = "PAI_NOM", length = 50)
    public String getPaiNom() {
        return this.paiNom;
    }

    public void setPaiNom(String paiNom) {
        this.paiNom = paiNom;
    }

    @Column(name = "PAI_NAC", length = 50)
    public String getPaiNac() {
        return this.paiNac;
    }

    public void setPaiNac(String paiNac) {
        this.paiNac = paiNac;
    }

    @Column(name = "PAI_SGL", length = 2)
    public String getPaiSgl() {
        return this.paiSgl;
    }

    public void setPaiSgl(String paiSgl) {
        this.paiSgl = paiSgl;
    }

/*    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "paises")
    public Set<Salida> getSalidas() {
        return this.salidas;
    }

    public void setSalidas(Set<Salida> salidas) {
        this.salidas = salidas;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "paises")
    public Set<Persona> getPersonas() {
        return this.personas;
    }

    public void setPersonas(Set<Persona> personas) {
        this.personas = personas;
    }*/

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "paises")
    public Set<Lugar> getLugars() {
        return this.lugars;
    }

    public void setLugars(Set<Lugar> lugars) {
        this.lugars = lugars;
    }
    
    
    public static final String NAMED_QUERY_ALL = "getAllPaises";
    public static final String QUERY_ALL = "FROM Paises pais order by pais.paiNom asc";
     
    
}
