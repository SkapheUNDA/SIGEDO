package la.netco.persistencia.dto.commons;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;


@Entity
@Table(name = "ACTIVIDAD", schema = Schemas.DBO_SCHEMA)
@NamedQuery(name=Actividad.NAMED_QUERY_GET_BY_CLASE_TIPO, query = Actividad.QUERY_GET_BY_CLASE_TIPO)
public class Actividad implements Serializable {


	private static final long serialVersionUID = 1L;
	private short actId;
    private String actNom;
    private Set<Actividadclaseregistro> actividadclaseregistros = new HashSet<Actividadclaseregistro>(0);

    public Actividad() {
    }

    public Actividad(short actId) {
        this.actId = actId;
    }

    public Actividad(short actId, String actNom, Set<Actividadclaseregistro> actividadclaseregistros) {
        this.actId = actId;
        this.actNom = actNom;
        this.actividadclaseregistros = actividadclaseregistros;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACT_ID", unique = true, nullable = false)
    public short getActId() {
        return this.actId;
    }

    public void setActId(short actId) {
        this.actId = actId;
    }

    @Column(name = "ACT_NOM", length = 50)
    public String getActNom() {
        return this.actNom;
    }

    public void setActNom(String actNom) {
        this.actNom = actNom;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "actividad")
    public Set<Actividadclaseregistro> getActividadclaseregistros() {
        return this.actividadclaseregistros;
    }

    public void setActividadclaseregistros(Set<Actividadclaseregistro> actividadclaseregistros) {
        this.actividadclaseregistros = actividadclaseregistros;
    }
    
    public static final String NAMED_QUERY_GET_BY_CLASE_TIPO = "getActividadByClaseregistroTipo";
    public static final String QUERY_GET_BY_CLASE_TIPO = "SELECT actividad FROM Actividad actividad  INNER JOIN actividad.actividadclaseregistros clase WHERE  clase.id.acrCre = ? and clase.id.acrTrp = ? order by actividad.actNom asc";


}
