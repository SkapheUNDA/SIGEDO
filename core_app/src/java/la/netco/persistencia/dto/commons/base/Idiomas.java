package la.netco.persistencia.dto.commons.base;
// Generated 9/10/2012 04:26:59 PM by Hibernate Tools 3.2.1.GA

import javax.persistence.*;

/**
 * Idiomas generated by hbm2java
 */
@Entity
@Table(name = "IDIOMAS", schema = "dbo", catalog = "AUTOR")
public class Idiomas implements java.io.Serializable {

    private short idiId;
    private String idiNom;

    public Idiomas() {
    }

    public Idiomas(short idiId) {
        this.idiId = idiId;
    }

    public Idiomas(short idiId, String idiNom) {
        this.idiId = idiId;
        this.idiNom = idiNom;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "IDI_ID", unique = true, nullable = false)
    public short getIdiId() {
        return this.idiId;
    }

    public void setIdiId(short idiId) {
        this.idiId = idiId;
    }

    @Column(name = "IDI_NOM", length = 50)
    public String getIdiNom() {
        return this.idiNom;
    }

    public void setIdiNom(String idiNom) {
        this.idiNom = idiNom;
    }
}
