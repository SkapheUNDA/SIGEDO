package la.netco.persistencia.dto.commons;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;


@Entity
@Table(name = "DEVOLUCIONES", schema = Schemas.DBO_SCHEMA)
@NamedQueries({
	@NamedQuery(name=Devoluciones.NAMED_QUERY_ALL_DEVOLUCION, query = Devoluciones.QUERY_ALL_DEVOLUCION)
})
public class Devoluciones implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	private int devId;
    private String devNom;
    private String devDes;
    private Boolean devLdev;
    private Boolean devLrec;

    public Devoluciones() {
    }

    public Devoluciones(int devId) {
        this.devId = devId;
    }

    public Devoluciones(int devId, String devNom, String devDes, Boolean devLdev, Boolean devLrec) {
        this.devId = devId;
        this.devNom = devNom;
        this.devDes = devDes;
        this.devLdev = devLdev;
        this.devLrec = devLrec;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "DEV_ID", unique = true, nullable = false)
    public int getDevId() {
        return this.devId;
    }

    public void setDevId(int devId) {
        this.devId = devId;
    }

    @Column(name = "DEV_NOM", length = 50)
    public String getDevNom() {
        return this.devNom;
    }

    public void setDevNom(String devNom) {
        this.devNom = devNom;
    }

    @Column(name = "DEV_DES")
    public String getDevDes() {
        return this.devDes;
    }

    public void setDevDes(String devDes) {
        this.devDes = devDes;
    }

    @Column(name = "DEV_LDEV")
    public Boolean getDevLdev() {
        return this.devLdev;
    }

    public void setDevLdev(Boolean devLdev) {
        this.devLdev = devLdev;
    }

    @Column(name = "DEV_LREC")
    public Boolean getDevLrec() {
        return this.devLrec;
    }

    public void setDevLrec(Boolean devLrec) {
        this.devLrec = devLrec;
    }
    
    public static final String NAMED_QUERY_ALL_DEVOLUCION ="getAllDevolucion";
    public static final String QUERY_ALL_DEVOLUCION ="FROM Devoluciones devoluciones";
    
   
    
}
