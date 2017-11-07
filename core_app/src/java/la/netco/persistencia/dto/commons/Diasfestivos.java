package la.netco.persistencia.dto.commons;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import la.netco.core.persistencia.vo.Schemas;


@Entity
@Table(name = "DIASFESTIVOS",schema = Schemas.DBO_SCHEMA)
@NamedQueries({
	@NamedQuery(name=Diasfestivos.NAMED_QUERY_GET_BY_DATE,query=Diasfestivos.QUERY_GET_BY_DATE),
	@NamedQuery(name=Diasfestivos.NAMED_QUERY_COUNT_BETWEEN,query=Diasfestivos.QUERY_COUNT_BETWEEN)
})
public class Diasfestivos implements Serializable {

	private static final long serialVersionUID = 1L;
	private Date dfeFec;

    public Diasfestivos() {
    }

    public Diasfestivos(Date dfeFec) {
        this.dfeFec = dfeFec;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DFE_FEC", unique = true, nullable = false, length = 23)
    public Date getDfeFec() {
        return this.dfeFec;
    }

    public void setDfeFec(Date dfeFec) {
        this.dfeFec = dfeFec;
    }
    
    public static final String NAMED_QUERY_GET_BY_DATE= "getDiaFestivoByDate"; 
    public static final String QUERY_GET_BY_DATE = "From Diasfestivos diasfestivos WHERE dfeFec = ? ";
    
    public static final String NAMED_QUERY_COUNT_BETWEEN= "countDiasFestivo"; 
    public static final String QUERY_COUNT_BETWEEN = "Select count(*) From Diasfestivos diasfestivos WHERE dfeFec between ? and ? ";
 
}
