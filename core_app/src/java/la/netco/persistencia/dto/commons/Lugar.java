package la.netco.persistencia.dto.commons;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;




@Entity
@Table(name = "LUGAR", schema = Schemas.DBO_SCHEMA)
@NamedQuery(name=Lugar.NAMED_QUERY_ALL,query=Lugar.QUERY_ALL)
public class Lugar implements Serializable {


	private static final long serialVersionUID = 1L;
	private short lugId;
    private Paises paises;
    private String lugCiu;
    private String lugDep;
/*    private Set<Persona> personasForPerSeulug = new HashSet<Persona>(0);
    private Set<Persona> personasForPerLug = new HashSet<Persona>(0);
    private Set<Salida> salidas = new HashSet<Salida>(0);
    private Set<Entidad> entidads = new HashSet<Entidad>(0);*/

    public Lugar() {
    }

    public Lugar(short lugId) {
        this.lugId = lugId;
    }

/*    public Lugar(short lugId, Paises paises, String lugCiu, String lugDep, Set<Persona> personasForPerSeulug, Set<Persona> personasForPerLug, Set<Salida> salidas, Set<Entidad> entidads) {
        this.lugId = lugId;
        this.paises = paises;
        this.lugCiu = lugCiu;
        this.lugDep = lugDep;
        this.personasForPerSeulug = personasForPerSeulug;
        this.personasForPerLug = personasForPerLug;
        this.salidas = salidas;
        this.entidads = entidads;
    }*/

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "LUG_ID", unique = true, nullable = false)
    public short getLugId() {
        return this.lugId;
    }

    public void setLugId(short lugId) {
        this.lugId = lugId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LUG_PAI")
    public Paises getPaises() {
        return this.paises;
    }

    public void setPaises(Paises paises) {
        this.paises = paises;
    }

    @Column(name = "LUG_CIU", length = 50)
    public String getLugCiu() {
        return this.lugCiu;
    }

    public void setLugCiu(String lugCiu) {
        this.lugCiu = lugCiu;
    }

    @Column(name = "LUG_DEP", length = 50)
    public String getLugDep() {
        return this.lugDep;
    }

    public void setLugDep(String lugDep) {
        this.lugDep = lugDep;
    }

   /* @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "lugarByPerSeulug")
    public Set<Persona> getPersonasForPerSeulug() {
        return this.personasForPerSeulug;
    }

    public void setPersonasForPerSeulug(Set<Persona> personasForPerSeulug) {
        this.personasForPerSeulug = personasForPerSeulug;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "lugarByPerLug")
    public Set<Persona> getPersonasForPerLug() {
        return this.personasForPerLug;
    }

    public void setPersonasForPerLug(Set<Persona> personasForPerLug) {
        this.personasForPerLug = personasForPerLug;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "lugar")
    public Set<Salida> getSalidas() {
        return this.salidas;
    }

    public void setSalidas(Set<Salida> salidas) {
        this.salidas = salidas;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "lugar")
    public Set<Entidad> getEntidads() {
        return this.entidads;
    }

    public void setEntidads(Set<Entidad> entidads) {
        this.entidads = entidads;
    }*/
    

    
    public static final String NAMED_QUERY_ALL = "getAllLugares";
    public static final String QUERY_ALL = "FROM Lugar lugar order by lugar.lugCiu asc";
}
