package la.netco.persistencia.dto.commons;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;


@Entity
@Table(name = "TITULOS", schema = Schemas.DBO_SCHEMA)
public class Titulos implements Serializable {

	private static final long serialVersionUID = 1L;
	private int titId;
    private Registro registro;
    private String titNom;

    public Titulos() {
    }

    public Titulos(int titId) {
        this.titId = titId;
    }

    public Titulos(int titId, Registro registro, String titNom) {
        this.titId = titId;
        this.registro = registro;
        this.titNom = titNom;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TIT_ID", unique = true, nullable = false)
    public int getTitId() {
        return this.titId;
    }

    public void setTitId(int titId) {
        this.titId = titId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TIT_REG")
    public Registro getRegistro() {
        return this.registro;
    }

    public void setRegistro(Registro registro) {
        this.registro = registro;
    }

    @Column(name = "TIT_NOM", length = 100)
    public String getTitNom() {
        return this.titNom;
    }

    public void setTitNom(String titNom) {
        this.titNom = titNom;
    }
}
