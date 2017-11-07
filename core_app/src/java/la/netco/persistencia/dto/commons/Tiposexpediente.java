package la.netco.persistencia.dto.commons;
// Generated 9/10/2012 04:26:59 PM by Hibernate Tools 3.2.1.GA

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

import la.netco.core.persistencia.vo.Schemas;


@Entity
@Table(name = "TIPOSEXPEDIENTE", schema = Schemas.DBO_SCHEMA)
public class Tiposexpediente implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	private byte texId;
    private String texNom;
    private boolean texReg;
    private Set<Expediente> expedientes = new HashSet<Expediente>(0);

    public Tiposexpediente() {
    }

    public Tiposexpediente(byte texId, boolean texReg) {
        this.texId = texId;
        this.texReg = texReg;
    }

    public Tiposexpediente(byte texId, String texNom, boolean texReg, Set<Expediente> expedientes) {
        this.texId = texId;
        this.texNom = texNom;
        this.texReg = texReg;
        this.expedientes = expedientes;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEX_ID", unique = true, nullable = false)
    public byte getTexId() {
        return this.texId;
    }

    public void setTexId(byte texId) {
        this.texId = texId;
    }

    @Column(name = "TEX_NOM", length = 50)
    public String getTexNom() {
        return this.texNom;
    }

    public void setTexNom(String texNom) {
        this.texNom = texNom;
    }

    @Column(name = "TEX_REG", nullable = false)
    public boolean isTexReg() {
        return this.texReg;
    }

    public void setTexReg(boolean texReg) {
        this.texReg = texReg;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tiposexpediente")
    public Set<Expediente> getExpedientes() {
        return this.expedientes;
    }

    public void setExpedientes(Set<Expediente> expedientes) {
        this.expedientes = expedientes;
    }
}
