package la.netco.persistencia.dto.commons;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class AnexosalidaId implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	private int dasSal;
    private short dasAnx;

    public AnexosalidaId() {
    }

    public AnexosalidaId(int dasSal, short dasAnx) {
        this.dasSal = dasSal;
        this.dasAnx = dasAnx;
    }

    @Column(name = "DAS_SAL", nullable = false)
    public int getDasSal() {
        return this.dasSal;
    }

    public void setDasSal(int dasSal) {
        this.dasSal = dasSal;
    }

    @Column(name = "DAS_ANX", nullable = false)
    public short getDasAnx() {
        return this.dasAnx;
    }

    public void setDasAnx(short dasAnx) {
        this.dasAnx = dasAnx;
    }

    public boolean equals(Object other) {
        if ((this == other)) {
            return true;
        }
        if ((other == null)) {
            return false;
        }
        if (!(other instanceof AnexosalidaId)) {
            return false;
        }
        AnexosalidaId castOther = (AnexosalidaId) other;

        return (this.getDasSal() == castOther.getDasSal())
                && (this.getDasAnx() == castOther.getDasAnx());
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + this.getDasSal();
        result = 37 * result + this.getDasAnx();
        return result;
    }
}
