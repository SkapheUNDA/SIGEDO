package la.netco.persistencia.dto.commons;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;

@Entity
@Table(name = "TIPOSDOCUMENTO", schema = Schemas.DBO_SCHEMA	)
@NamedQuery(name=Tiposdocumento.NAMED_QUERY_ALL,query=Tiposdocumento.QUERY_ALL)
public class Tiposdocumento  implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer tdoId;
    private String tdoNom;
    private String tdoAbr;

    public Tiposdocumento() {
    }

    public Tiposdocumento(Integer tdoId) {
        this.tdoId = tdoId;
    }

    public Tiposdocumento(Integer tdoId, String tdoNom, String tdoAbr) {
        this.tdoId = tdoId;
        this.tdoNom = tdoNom;
        this.tdoAbr = tdoAbr;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TDO_ID", unique = true, nullable = false)
    public Integer getTdoId() {
        return this.tdoId;
    }

    public void setTdoId(Integer tdoId) {
        this.tdoId = tdoId;
    }

    @Column(name = "TDO_NOM", length = 100)
    public String getTdoNom() {
        return this.tdoNom;
    }

    public void setTdoNom(String tdoNom) {
        this.tdoNom = tdoNom;
    }

    @Column(name = "TDO_ABR", length = 3)
    public String getTdoAbr() {
        return this.tdoAbr;
    }

    public void setTdoAbr(String tdoAbr) {
        this.tdoAbr = tdoAbr;
    }
    
    
    public static final String NAMED_QUERY_ALL = "getAllTiposDocumentos";
    public static final String QUERY_ALL = "FROM Tiposdocumento tipoDoc order by tipoDoc.tdoNom asc";
      
}
