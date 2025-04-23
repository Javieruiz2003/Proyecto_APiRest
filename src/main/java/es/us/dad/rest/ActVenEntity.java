package es.us.dad.rest;

import java.util.Objects;

public class ActVenEntity {
	Integer idfa;
	Integer onoff;
	Long timestampf;

	Integer idP; // id de la placa
	Integer idG; // id del group

	@Override
	public int hashCode() {
		return Objects.hash(idfa);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActVenEntity other = (ActVenEntity) obj;
		return Objects.equals(idfa, other.idfa);
	}

	public Integer getIdfa() {
		return idfa;
	}

	public void setIdfa(Integer idfa) {
		this.idfa = idfa;
	}

	public Integer getOnoff() {
		return onoff;
	}

	public void setOnoff(Integer onoff) {
		this.onoff = onoff;
	}

	public Long getTimestampf() {
		return timestampf;
	}

	public void setTimestampf(Long timestampf) {
		this.timestampf = timestampf;
	}

	public Integer getIdP() {
		return idP;
	}

	public void setIdP(Integer idP) {
		this.idP = idP;
	}

	public Integer getIdG() {
		return idG;
	}

	public void setIdG(Integer idG) {
		this.idG = idG;
	}

	public ActVenEntity(Integer idfa, Integer onoff, Long timestampf, Integer idP, Integer idG) {
		super();
		this.idfa = idfa;
		this.onoff = onoff;
		this.timestampf = timestampf;
		this.idP = idP;
		this.idG = idG;
	}

	public ActVenEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

}
