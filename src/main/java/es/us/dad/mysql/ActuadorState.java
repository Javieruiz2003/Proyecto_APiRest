package es.us.dad.mysql;

import java.util.Objects;

public class ActuadorState {

	private Integer id;
	private Integer id_actuador;
	private Boolean estado;
	private Long timeStamp;

	ActuadorState(Integer id, Integer id_actuador, Boolean estado, Long timeStamp) {
		super();
		this.id = id;
		this.id_actuador = id_actuador;
		this.estado = estado;
		this.timeStamp = timeStamp;
	}

	public ActuadorState() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId_actuador() {
		return id_actuador;
	}

	public void setId_actuador(Integer id_actuador) {
		this.id_actuador = id_actuador;
	}

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	@Override
	public int hashCode() {
		return Objects.hash(estado, id, id_actuador, timeStamp);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActuadorState other = (ActuadorState) obj;
		return Objects.equals(estado, other.estado) && Objects.equals(id, other.id)
				&& Objects.equals(id_actuador, other.id_actuador) && Objects.equals(timeStamp, other.timeStamp);
	}

	@Override
	public String toString() {
		return "ActuadorState [id=" + id + ", id_actuador=" + id_actuador + ", estado=" + estado + ", timeStamp="
				+ timeStamp + "]";
	}

}
