package la.netco.persistencia.dto.commons;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;
import la.netco.persistencia.dto.commons.base.Parteinterviniente;


@Entity
@Table(name = "RELACIONCONTRATO", schema = Schemas.DBO_SCHEMA)
public class Relacioncontrato implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private short rcoId;
    private Tiposcontrato tiposcontrato;
    private String rcoNom;
    private Integer rcoTrl;
   // private Set<Parteinterviniente> parteintervinientes = new HashSet<Parteinterviniente>(0);
   // private Set<Registropersona> registropersonas = new HashSet<Registropersona>(0);

    public Relacioncontrato() {
    }

    public Relacioncontrato(short rcoId, String rcoNom) {
        this.rcoId = rcoId;
        this.rcoNom = rcoNom;
    }

    public Relacioncontrato(short rcoId, Tiposcontrato tiposcontrato, String rcoNom, Integer rcoTrl, Set<Parteinterviniente> parteintervinientes, Set<Registropersona> registropersonas) {
        this.rcoId = rcoId;
        this.tiposcontrato = tiposcontrato;
        this.rcoNom = rcoNom;
        this.rcoTrl = rcoTrl;
       // this.parteintervinientes = parteintervinientes;
       // this.registropersonas = registropersonas;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "RCO_ID", unique = true, nullable = false)
    public short getRcoId() {
        return this.rcoId;
    }

    public void setRcoId(short rcoId) {
        this.rcoId = rcoId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RCO_TCO")
    public Tiposcontrato getTiposcontrato() {
        return this.tiposcontrato;
    }

    public void setTiposcontrato(Tiposcontrato tiposcontrato) {
        this.tiposcontrato = tiposcontrato;
    }

    @Column(name = "RCO_NOM", nullable = false, length = 50)
    public String getRcoNom() {
        return this.rcoNom;
    }

    public void setRcoNom(String rcoNom) {
        this.rcoNom = rcoNom;
    }

    @Column(name = "RCO_TRL")
    public Integer getRcoTrl() {
        return this.rcoTrl;
    }

    public void setRcoTrl(Integer rcoTrl) {
        this.rcoTrl = rcoTrl;
    }

   /* @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "relacioncontrato")
    public Set<Parteinterviniente> getParteintervinientes() {
        return this.parteintervinientes;
    }

    public void setParteintervinientes(Set<Parteinterviniente> parteintervinientes) {
        this.parteintervinientes = parteintervinientes;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "relacioncontrato")
    public Set<Registropersona> getRegistropersonas() {
        return this.registropersonas;
    }

    public void setRegistropersonas(Set<Registropersona> registropersonas) {
        this.registropersonas = registropersonas;
    }*/
}
