package la.netco.persistencia.dto.commons.base;
// Generated 9/10/2012 04:26:59 PM by Hibernate Tools 3.2.1.GA

import javax.persistence.*;

/**
 * Coincidenciasatendidas generated by hbm2java
 */
@Entity
@Table(name = "COINCIDENCIASATENDIDAS", schema = "dbo", catalog = "AUTOR")
public class Coincidenciasatendidas implements java.io.Serializable {

    private int coiPerid;

    public Coincidenciasatendidas() {
    }

    public Coincidenciasatendidas(int coiPerid) {
        this.coiPerid = coiPerid;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "COI_PERID", unique = true, nullable = false)
    public int getCoiPerid() {
        return this.coiPerid;
    }

    public void setCoiPerid(int coiPerid) {
        this.coiPerid = coiPerid;
    }
}
