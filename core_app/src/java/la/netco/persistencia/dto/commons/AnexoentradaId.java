package la.netco.persistencia.dto.commons;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class AnexoentradaId implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private int daeEnt;
    private short daeAnx;

    public AnexoentradaId() {
    }

    public AnexoentradaId(int daeEnt, short daeAnx) {
        this.daeEnt = daeEnt;
        this.daeAnx = daeAnx;
    }

    @Column(name = "DAE_ENT", nullable = false)
    public int getDaeEnt() {
        return this.daeEnt;
    }

    public void setDaeEnt(int daeEnt) {
        this.daeEnt = daeEnt;
    }

    @Column(name = "DAE_ANX", nullable = false)
    public short getDaeAnx() {
        return this.daeAnx;
    }

    public void setDaeAnx(short daeAnx) {
        this.daeAnx = daeAnx;
    }

    public boolean equals(Object other) {
        if ((this == other)) {
            return true;
        }
        if ((other == null)) {
            return false;
        }
        if (!(other instanceof AnexoentradaId)) {
            return false;
        }
        AnexoentradaId castOther = (AnexoentradaId) other;

        return (this.getDaeEnt() == castOther.getDaeEnt())  && (this.getDaeAnx() == castOther.getDaeAnx());
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + this.getDaeEnt();
        result = 37 * result + this.getDaeAnx();
        return result;
    }
}
