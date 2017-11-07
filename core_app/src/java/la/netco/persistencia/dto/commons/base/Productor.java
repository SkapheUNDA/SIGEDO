package la.netco.persistencia.dto.commons.base;
// Generated 9/10/2012 04:26:59 PM by Hibernate Tools 3.2.1.GA

import javax.persistence.*;

import la.netco.persistencia.dto.commons.Entidad;
import la.netco.persistencia.dto.commons.Persona;
import la.netco.persistencia.dto.commons.Registro;
import la.netco.persistencia.dto.commons.Tipospersona;


/**
 * Productor generated by hbm2java
 */
@Entity
@Table(name = "PRODUCTOR", schema = "dbo", catalog = "AUTOR")
public class Productor implements java.io.Serializable {

    private int proId;
    private Tipospersona tipospersona;
    private Registro registro;
    private Persona persona;
    private Entidad entidad;

    public Productor() {
    }

    public Productor(int proId) {
        this.proId = proId;
    }

    public Productor(int proId, Tipospersona tipospersona, Registro registro, Persona persona, Entidad entidad) {
        this.proId = proId;
        this.tipospersona = tipospersona;
        this.registro = registro;
        this.persona = persona;
        this.entidad = entidad;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRO_TPE")
    public Tipospersona getTipospersona() {
        return this.tipospersona;
    }

    public void setTipospersona(Tipospersona tipospersona) {
        this.tipospersona = tipospersona;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRO_REG")
    public Registro getRegistro() {
        return this.registro;
    }

    public void setRegistro(Registro registro) {
        this.registro = registro;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRO_PER")
    public Persona getPersona() {
        return this.persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRO_ETD")
    public Entidad getEntidad() {
        return this.entidad;
    }

    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }
}