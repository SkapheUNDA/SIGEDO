package la.netco.persistencia.dto.commons;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class EnlaceId implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private byte enlOrg;
    private int enlEnt;
    private int enlSal;

    public EnlaceId() {
    }

    public EnlaceId(byte enlOrg, int enlEnt, int enlSal) {
        this.enlOrg = enlOrg;
        this.enlEnt = enlEnt;
        this.enlSal = enlSal;
    }

    @Column(name = "ENL_ORG", nullable = false)
    public byte getEnlOrg() {
        return this.enlOrg;
    }

    public void setEnlOrg(byte enlOrg) {
        this.enlOrg = enlOrg;
    }

    @Column(name = "ENL_ENT", nullable = false)
    public int getEnlEnt() {
        return this.enlEnt;
    }

    public void setEnlEnt(int enlEnt) {
        this.enlEnt = enlEnt;
    }

    @Column(name = "ENL_SAL", nullable = false)
    public int getEnlSal() {
        return this.enlSal;
    }

    public void setEnlSal(int enlSal) {
        this.enlSal = enlSal;
    }

    public boolean equals(Object other) {
        if ((this == other)) {
            return true;
        }
        if ((other == null)) {
            return false;
        }
        if (!(other instanceof EnlaceId)) {
            return false;
        }
        EnlaceId castOther = (EnlaceId) other;

        return (this.getEnlOrg() == castOther.getEnlOrg())
                && (this.getEnlEnt() == castOther.getEnlEnt())
                && (this.getEnlSal() == castOther.getEnlSal());
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + this.getEnlOrg();
        result = 37 * result + this.getEnlEnt();
        result = 37 * result + this.getEnlSal();
        return result;
    }
}
