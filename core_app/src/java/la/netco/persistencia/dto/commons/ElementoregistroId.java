package la.netco.persistencia.dto.commons;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class ElementoregistroId implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	private int elrReg;
    private int elrEle;

    public ElementoregistroId() {
    }

    public ElementoregistroId(int elrReg, int elrEle) {
        this.elrReg = elrReg;
        this.elrEle = elrEle;
    }

    @Column(name = "ELR_REG", nullable = false)
    public int getElrReg() {
        return this.elrReg;
    }

    public void setElrReg(int elrReg) {
        this.elrReg = elrReg;
    }

    @Column(name = "ELR_ELE", nullable = false)
    public int getElrEle() {
        return this.elrEle;
    }

    public void setElrEle(int elrEle) {
        this.elrEle = elrEle;
    }

    public boolean equals(Object other) {
        if ((this == other)) {
            return true;
        }
        if ((other == null)) {
            return false;
        }
        if (!(other instanceof ElementoregistroId)) {
            return false;
        }
        ElementoregistroId castOther = (ElementoregistroId) other;

        return (this.getElrReg() == castOther.getElrReg())
                && (this.getElrEle() == castOther.getElrEle());
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + this.getElrReg();
        result = 37 * result + this.getElrEle();
        return result;
    }
}
