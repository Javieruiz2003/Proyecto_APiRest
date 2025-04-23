package es.us.dad.rest;

import java.util.Objects;

public class SensorLuzEntity {
	private Integer idl;
	private Double nivel_luz;
	private Long timestampl;
	private Integer idP;
	private Integer idG;

	public SensorLuzEntity(Integer idl, Double nivel_luz, Long timestampl, Integer idP, Integer idG) {
		super();
		this.idl = idl;
		this.nivel_luz = nivel_luz;
		this.timestampl = timestampl;
		this.idP = idP;
		this.idG = idG;

	}

	public SensorLuzEntity() {
		super();
	}

	@Override
	public int hashCode() {
		return Objects.hash(idl);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SensorLuzEntity other = (SensorLuzEntity) obj;
		return Objects.equals(idl, other.idl);
	}

	public Integer getIdl() {
		return idl;
	}

	public void setIdl(Integer idl) {
		this.idl = idl;
	}

	public Double getNivel_luz() {
		return nivel_luz;
	}

	public void setNivel_luz(Double nivel_luz) {
		this.nivel_luz = nivel_luz;
	}

	public Long getTimestampl() {
		return timestampl;
	}

	public void setTimestampl(Long timestampl) {
		this.timestampl = timestampl;
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

}
