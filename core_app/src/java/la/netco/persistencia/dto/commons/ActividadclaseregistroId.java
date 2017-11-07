package la.netco.persistencia.dto.commons;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ActividadclaseregistroId implements Serializable {


	private static final long serialVersionUID = 1L;
	private int acrCre;
    private short acrAct;
    private byte acrTrp;

    public ActividadclaseregistroId() {
    }

    public ActividadclaseregistroId(int acrCre, short acrAct, byte acrTrp) {
        this.acrCre = acrCre;
        this.acrAct = acrAct;
        this.acrTrp = acrTrp;
    }

    @Column(name = "ACR_CRE", nullable = false)
    public int getAcrCre() {
        return this.acrCre;
    }

    public void setAcrCre(int acrCre) {
        this.acrCre = acrCre;
    }

    @Column(name = "ACR_ACT", nullable = false)
    public short getAcrAct() {
        return this.acrAct;
    }

    public void setAcrAct(short acrAct) {
        this.acrAct = acrAct;
    }

    @Column(name = "ACR_TRP", nullable = false)
    public byte getAcrTrp() {
        return this.acrTrp;
    }

    public void setAcrTrp(byte acrTrp) {
        this.acrTrp = acrTrp;
    }

    public boolean equals(Object other) {
        if ((this == other)) {
            return true;
        }
        if ((other == null)) {
            return false;
        }
        if (!(other instanceof ActividadclaseregistroId)) {
            return false;
        }
        ActividadclaseregistroId castOther = (ActividadclaseregistroId) other;

        return (this.getAcrCre() == castOther.getAcrCre())
                && (this.getAcrAct() == castOther.getAcrAct())
                && (this.getAcrTrp() == castOther.getAcrTrp());
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + this.getAcrCre();
        result = 37 * result + this.getAcrAct();
        result = 37 * result + this.getAcrTrp();
        return result;
    }
}
