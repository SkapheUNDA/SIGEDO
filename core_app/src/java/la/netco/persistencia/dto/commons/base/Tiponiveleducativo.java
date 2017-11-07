package la.netco.persistencia.dto.commons.base;
// Generated 9/10/2012 04:26:59 PM by Hibernate Tools 3.2.1.GA

import javax.persistence.*;

/**
 * Tiponiveleducativo generated by hbm2java
 */
@Entity
@Table(name = "TIPONIVELEDUCATIVO", schema = "dbo", catalog = "AUTOR")
public class Tiponiveleducativo implements java.io.Serializable {

    private int neduId;
    private String neduNivel;
    private Integer neduNroannos;

    public Tiponiveleducativo() {
    }

    public Tiponiveleducativo(int neduId) {
        this.neduId = neduId;
    }

    public Tiponiveleducativo(int neduId, String neduNivel, Integer neduNroannos) {
        this.neduId = neduId;
        this.neduNivel = neduNivel;
        this.neduNroannos = neduNroannos;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "NEDU_ID", unique = true, nullable = false)
    public int getNeduId() {
        return this.neduId;
    }

    public void setNeduId(int neduId) {
        this.neduId = neduId;
    }

    @Column(name = "NEDU_NIVEL", length = 80)
    public String getNeduNivel() {
        return this.neduNivel;
    }

    public void setNeduNivel(String neduNivel) {
        this.neduNivel = neduNivel;
    }

    @Column(name = "NEDU_NROANNOS")
    public Integer getNeduNroannos() {
        return this.neduNroannos;
    }

    public void setNeduNroannos(Integer neduNroannos) {
        this.neduNroannos = neduNroannos;
    }
}