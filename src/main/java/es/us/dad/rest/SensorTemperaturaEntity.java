package es.us.dad.rest;

import java.util.Objects;

public class SensorTemperaturaEntity {
	// El sensor tiene temperatura y humedada, pero de momento solo usaré la temperatura
	private Integer idTemp;
	private Double temperatura;
	private Long timestampt;
	private Integer idPlaca; // id de la placa
	private Integer idGroup; // id del group

	public SensorTemperaturaEntity(Integer idTemp, Double temperatura, Long timestampt, Integer idPlaca, Integer idGroup) {
		super();
		this.idTemp = idTemp;
		this.temperatura = temperatura;
		this.timestampt = timestampt;
		this.idPlaca = idPlaca;
		this.idGroup = idGroup;
	}

	public Integer getidPlaca() {
		return idPlaca;
	}

	public void setidPlaca(Integer idPlaca) {
		this.idPlaca = idPlaca;
	}

	public Integer getidGroup() {
		return idGroup;
	}

	public void setidGroup(Integer idGroup) {
		this.idGroup = idGroup;
	}

	public SensorTemperaturaEntity() {
		super();
	}

	@Override
	public int hashCode() {
		return Objects.hash(idTemp);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SensorTemperaturaEntity other = (SensorTemperaturaEntity) obj;
		return Objects.equals(idTemp, other.idTemp);
	}

	public Integer getidTemp() {
		return idTemp;
	}

	public void setidTemp(Integer idTemp) {
		this.idTemp = idTemp;
	}

	public Double getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(Double temperatura) {
		this.temperatura = temperatura;
	}

	public Long getTimestampt() {
		return timestampt;
	}

	public void setTimestampt(Long timestampt) {
		this.timestampt = timestampt;
	}

}
