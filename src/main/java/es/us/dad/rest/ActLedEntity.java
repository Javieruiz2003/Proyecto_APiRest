package es.us.dad.rest;

import java.util.Objects;

public class ActLedEntity {
	Integer idla;
	Double nivel_luz;
	Long timestample;
	Integer idP; // id de la placa
	Integer idG; // id del group

	public Integer getIdLA() {
		return idla;
	}

	public void setIdLA(Integer idLA) {
		this.idla = idLA;
	}

	public Double getNivel_luz() {
		return nivel_luz;
	}

	public void setNivel_luz(Double nivel_luz) {
		this.nivel_luz = nivel_luz;
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

	@Override
	public int hashCode() {
		return Objects.hash(idla);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActLedEntity other = (ActLedEntity) obj;
		return Objects.equals(idla, other.idla);
	}

	public Long getTimestample() {
		return timestample;
	}

	public void setTimestample(Long timestample) {
		this.timestample = timestample;
	}

	public ActLedEntity(Integer idla, Double nivel_luz, Long timestample, Integer idP, Integer idG) {
		super();
		this.idla = idla;
		this.nivel_luz = nivel_luz;
		this.timestample = timestample;
		this.idP = idP;
		this.idG = idG;
	}

	public ActLedEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
}
