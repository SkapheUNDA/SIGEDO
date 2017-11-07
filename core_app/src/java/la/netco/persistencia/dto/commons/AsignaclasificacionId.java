package la.netco.persistencia.dto.commons;
// Generated 9/10/2012 04:26:59 PM by Hibernate Tools 3.2.1.GA

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class AsignaclasificacionId implements Serializable {


	private static final long serialVersionUID = 1L;
	private short asgCla;
    private short asgDep;

    public AsignaclasificacionId() {
    }

    public AsignaclasificacionId(short asgCla, short asgDep) {
        this.asgCla = asgCla;
        this.asgDep = asgDep;
    }

    @Column(name = "ASG_CLA", nullable = false)
    public short getAsgCla() {
        return this.asgCla;
    }

    public void setAsgCla(short asgCla) {
        this.asgCla = asgCla;
    }

    @Column(name = "ASG_DEP", nullable = false)
    public short getAsgDep() {
        return this.asgDep;
    }

    public void setAsgDep(short asgDep) {
        this.asgDep = asgDep;
    }

    public boolean equals(Object other) {
        if ((this == other)) {
            return true;
        }
        if ((other == null)) {
            return false;
        }
        if (!(other instanceof AsignaclasificacionId)) {
            return false;
        }
        AsignaclasificacionId castOther = (AsignaclasificacionId) other;

        return (this.getAsgCla() == castOther.getAsgCla())
                && (this.getAsgDep() == castOther.getAsgDep());
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + this.getAsgCla();
        result = 37 * result + this.getAsgDep();
        return result;
    }
}
