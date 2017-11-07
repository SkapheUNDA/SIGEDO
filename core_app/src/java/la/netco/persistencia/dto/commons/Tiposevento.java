package la.netco.persistencia.dto.commons;
// Generated 9/10/2012 04:26:59 PM by Hibernate Tools 3.2.1.GA

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;


@Entity
@Table(name = "TIPOSEVENTO", schema = Schemas.DBO_SCHEMA)
public class Tiposevento implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	private byte tevId;
    private String tevNom;
    private Set<Evento> eventos = new HashSet<Evento>(0);

    public static byte ETAPA_INICIAL = 1;
    
    public Tiposevento() {
    }

    public Tiposevento(byte tevId, String tevNom) {
        this.tevId = tevId;
        this.tevNom = tevNom;
    }

    public Tiposevento(byte tevId, String tevNom, Set<Evento> eventos) {
        this.tevId = tevId;
        this.tevNom = tevNom;
        this.eventos = eventos;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEV_ID", unique = true, nullable = false)
    public byte getTevId() {
        return this.tevId;
    }

    public void setTevId(byte tevId) {
        this.tevId = tevId;
    }

    @Column(name = "TEV_NOM", nullable = false, length = 50)
    public String getTevNom() {
        return this.tevNom;
    }

    public void setTevNom(String tevNom) {
        this.tevNom = tevNom;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tiposevento")
    public Set<Evento> getEventos() {
        return this.eventos;
    }

    public void setEventos(Set<Evento> eventos) {
        this.eventos = eventos;
    }
}
