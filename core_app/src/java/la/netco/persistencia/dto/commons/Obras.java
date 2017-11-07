package la.netco.persistencia.dto.commons;
// Generated 9/10/2012 04:26:59 PM by Hibernate Tools 3.2.1.GA

import java.io.Serializable;

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

@Entity
@Table(name = "OBRAS", schema = Schemas.DBO_SCHEMA)
public class Obras implements Serializable {

	private static final long serialVersionUID = 1L;
	private int obrId;
    private Registro registroByObrRgt;
    private Registro registroByObrReg;
    private Byte obrTip;
    private String obrObs;
    private String obrTit;
    private String obrAut;

    public Obras() {
    }

    public Obras(int obrId) {
        this.obrId = obrId;
    }

    public Obras(int obrId, Registro registroByObrRgt, Registro registroByObrReg, Byte obrTip, String obrObs, String obrTit, String obrAut) {
        this.obrId = obrId;
        this.registroByObrRgt = registroByObrRgt;
        this.registroByObrReg = registroByObrReg;
        this.obrTip = obrTip;
        this.obrObs = obrObs;
        this.obrTit = obrTit;
        this.obrAut = obrAut;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "OBR_ID", unique = true, nullable = false)
    public int getObrId() {
        return this.obrId;
    }

    public void setObrId(int obrId) {
        this.obrId = obrId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OBR_RGT")
    public Registro getRegistroByObrRgt() {
        return this.registroByObrRgt;
    }

    public void setRegistroByObrRgt(Registro registroByObrRgt) {
        this.registroByObrRgt = registroByObrRgt;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OBR_REG")
    public Registro getRegistroByObrReg() {
        return this.registroByObrReg;
    }

    public void setRegistroByObrReg(Registro registroByObrReg) {
        this.registroByObrReg = registroByObrReg;
    }

    @Column(name = "OBR_TIP")
    public Byte getObrTip() {
        return this.obrTip;
    }

    public void setObrTip(Byte obrTip) {
        this.obrTip = obrTip;
    }

    @Column(name = "OBR_OBS", length = 2500)
    public String getObrObs() {
        return this.obrObs;
    }

    public void setObrObs(String obrObs) {
        this.obrObs = obrObs;
    }

    @Column(name = "OBR_TIT", length = 1000)
    public String getObrTit() {
        return this.obrTit;
    }

    public void setObrTit(String obrTit) {
        this.obrTit = obrTit;
    }

    @Column(name = "OBR_AUT", length = 1000)
    public String getObrAut() {
        return this.obrAut;
    }

    public void setObrAut(String obrAut) {
        this.obrAut = obrAut;
    }
}
