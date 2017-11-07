package la.netco.persistencia.dto.commons;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ExpedienteanexoId implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private int exaExp;
	private short exaAnx;

	public ExpedienteanexoId() {
	}

	public ExpedienteanexoId(int exaExp, short exaAnx) {
		this.exaExp = exaExp;
		this.exaAnx = exaAnx;
	}

	@Column(name = "EXA_EXP", nullable = false)
	public int getExaExp() {
		return this.exaExp;
	}

	public void setExaExp(int exaExp) {
		this.exaExp = exaExp;
	}

	@Column(name = "EXA_ANX", nullable = false)
	public short getExaAnx() {
		return this.exaAnx;
	}

	public void setExaAnx(short exaAnx) {
		this.exaAnx = exaAnx;
	}

	public boolean equals(Object other) {
		if ((this == other)) {
			return true;
		}
		if ((other == null)) {
			return false;
		}
		if (!(other instanceof ExpedienteanexoId)) {
			return false;
		}
		ExpedienteanexoId castOther = (ExpedienteanexoId) other;

		return (this.getExaExp() == castOther.getExaExp())
				&& (this.getExaAnx() == castOther.getExaAnx());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getExaExp();
		result = 37 * result + this.getExaAnx();
		return result;
	}
}
