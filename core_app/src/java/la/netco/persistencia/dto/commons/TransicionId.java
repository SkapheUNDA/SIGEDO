package la.netco.persistencia.dto.commons;
// Generated 9/10/2012 04:26:59 PM by Hibernate Tools 3.2.1.GA

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class TransicionId implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	private short trsTrm;
    private short trsEve;
    private short trsEstini;
    private short trsEstfin;

    public TransicionId() {
    }

    public TransicionId(short trsTrm, short trsEve, short trsEstini, short trsEstfin) {
        this.trsTrm = trsTrm;
        this.trsEve = trsEve;
        this.trsEstini = trsEstini;
        this.trsEstfin = trsEstfin;
    }

    @Column(name = "TRS_TRM", nullable = false)
    public short getTrsTrm() {
        return this.trsTrm;
    }

    public void setTrsTrm(short trsTrm) {
        this.trsTrm = trsTrm;
    }

    @Column(name = "TRS_EVE", nullable = false)
    public short getTrsEve() {
        return this.trsEve;
    }

    public void setTrsEve(short trsEve) {
        this.trsEve = trsEve;
    }

    @Column(name = "TRS_ESTINI", nullable = false)
    public short getTrsEstini() {
        return this.trsEstini;
    }

    public void setTrsEstini(short trsEstini) {
        this.trsEstini = trsEstini;
    }

    @Column(name = "TRS_ESTFIN", nullable = false)
    public short getTrsEstfin() {
        return this.trsEstfin;
    }

    public void setTrsEstfin(short trsEstfin) {
        this.trsEstfin = trsEstfin;
    }

    public boolean equals(Object other) {
        if ((this == other)) {
            return true;
        }
        if ((other == null)) {
            return false;
        }
        if (!(other instanceof TransicionId)) {
            return false;
        }
        TransicionId castOther = (TransicionId) other;

        return (this.getTrsTrm() == castOther.getTrsTrm())
                && (this.getTrsEve() == castOther.getTrsEve())
                && (this.getTrsEstini() == castOther.getTrsEstini())
                && (this.getTrsEstfin() == castOther.getTrsEstfin());
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + this.getTrsTrm();
        result = 37 * result + this.getTrsEve();
        result = 37 * result + this.getTrsEstini();
        result = 37 * result + this.getTrsEstfin();
        return result;
    }
}
