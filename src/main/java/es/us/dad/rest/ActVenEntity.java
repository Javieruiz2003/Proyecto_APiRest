package es.us.dad.rest;

import java.util.Objects;

public class ActVenEntity {
	Integer idVent;
	Integer onoff;
	Long timestampf;

	Integer idPlaca; // id de la placa
	Integer idGroup; // id del group

	@Override
	public int hashCode() {
		return Objects.hash(idVent);
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
		return Objects.equals(idVent, other.idVent);
	}

	public Integer getIdVent() {
		return idVent;
	}

	public void setIdVent(Integer idVent) {
		this.idVent = idVent;
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

	public Integer getIdPlaca() {
		return idPlaca;
	}

	public void setIdPlaca(Integer idPlaca) {
		this.idPlaca = idPlaca;
	}

	public Integer getIdGroup() {
		return idGroup;
	}

	public void setIdGroup(Integer idGroup) {
		this.idGroup = idGroup;
	}

	public ActVenEntity(Integer idVent, Integer onoff, Long timestampf, Integer idPlaca, Integer idGroup) {
		super();
		this.idVent = idVent;
		this.onoff = onoff;
		this.timestampf = timestampf;
		this.idPlaca = idPlaca;
		this.idGroup = idGroup;
	}

	public ActVenEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

}
