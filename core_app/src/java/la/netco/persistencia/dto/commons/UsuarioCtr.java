package la.netco.persistencia.dto.commons;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;

@Entity
@Table(name = "USUARIOCTR", schema=Schemas.PRINCIPAL_SCHEMA)
@NamedQuery(name=UsuarioCtr.NAMED_QUERY_FIND_BY_USU_DIRECTIVO, query=UsuarioCtr.QUERY_FIND_BY_USU_DIRECTIVO)
public class UsuarioCtr implements Serializable {


	private static final long serialVersionUID = 1L;


	public UsuarioCtr(UsuarioCtrPK compositePK) {
		this.compositePK = compositePK;
	}

	public UsuarioCtr() {
	}
	
	private UsuarioCtrPK compositePK;

	
	public UsuarioCtr(Integer uscSUB, Integer uscJEF) {
		setCompositePK(new UsuarioCtrPK(uscSUB, uscJEF));
	}
	
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "USC_JEF", column = @Column(name = "USC_JEF", nullable = false)),
			@AttributeOverride(name = "USC_SUB", column = @Column(name = "USC_SUB", nullable = false)) })
	public UsuarioCtrPK getCompositePK() {
		return compositePK;
	}
	
	public void setCompositePK(UsuarioCtrPK compositePK) {
		this.compositePK = compositePK;
	}

	
	@Override
	public String toString() {		
		return "USC_SUB : "+compositePK.getUscSUB()+" - USC_JEF : " + compositePK.getUscJEF();
	}


	
	@Embeddable
	public static class UsuarioCtrPK implements Serializable{

		private static final long serialVersionUID = 1L;
		private Integer uscSUB;
		private Integer uscJEF;
		
		public UsuarioCtrPK() {
			
		}
		
		public UsuarioCtrPK(Integer uscSUB, Integer uscJEF) {
			this.uscSUB = uscSUB;
			this.uscJEF = uscJEF;
		}
		
		@Column(name = "USC_SUB", nullable = false)
		public Integer getUscSUB() {
			return uscSUB;
		}
		
		@Column(name = "USC_JEF", nullable = false)
		public Integer getUscJEF() {
			return uscJEF;
		}

		public void setUscSUB(Integer uscSUB) {
			this.uscSUB = uscSUB;
		}

		public void setUscJEF(Integer uscJEF) {
			this.uscJEF = uscJEF;
		}
		

		@Override
		public String toString() {		
			return "uscSUB : "+uscSUB+" - uscJEF : " + uscJEF;
		}

	}
	
	public static final String NAMED_QUERY_FIND_BY_USU_DIRECTIVO  = "findUsuarioCtrDirectivo";
	public static final String QUERY_FIND_BY_USU_DIRECTIVO 		= "from UsuarioCtr usuario   WHERE usuario.compositePK.uscJEF=? ";
	
}
