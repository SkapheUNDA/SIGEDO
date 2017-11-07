package la.netco.persistencia.dto.commons;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

import la.netco.core.persistencia.vo.Schemas;


@Entity
@Table(name = "SECTOR", schema =Schemas.DBO_SCHEMA)
public class Sector implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	private short secId;
    private String secNom;
    private Set<Persona> personas = new HashSet<Persona>(0);
    private Set<Entidad> entidads = new HashSet<Entidad>(0);

    public Sector() {
    }

    public Sector(short secId) {
        this.secId = secId;
    }

    public Sector(short secId, String secNom, Set<Persona> personas, Set<Entidad> entidads) {
        this.secId = secId;
        this.secNom = secNom;
        this.personas = personas;
        this.entidads = entidads;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "SEC_ID", unique = true, nullable = false)
    public short getSecId() {
        return this.secId;
    }

    public void setSecId(short secId) {
        this.secId = secId;
    }

    @Column(name = "SEC_NOM", length = 50)
    public String getSecNom() {
        return this.secNom;
    }

    public void setSecNom(String secNom) {
        this.secNom = secNom;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "sector")
    public Set<Persona> getPersonas() {
        return this.personas;
    }

    public void setPersonas(Set<Persona> personas) {
        this.personas = personas;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "sector")
    public Set<Entidad> getEntidads() {
        return this.entidads;
    }

    public void setEntidads(Set<Entidad> entidads) {
        this.entidads = entidads;
    }
}
