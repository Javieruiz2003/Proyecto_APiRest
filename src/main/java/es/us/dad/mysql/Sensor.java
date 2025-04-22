package es.us.dad.mysql;

import java.util.Objects;


public class Sensor {

	private Integer id_sensor;
	private String nombre;
	private String tipo;
	private String identificador;
	private Integer id_device;
	
	public Sensor() {
		
		// TODO Auto-generated constructor stub
	}
	public Sensor(Integer id_sensor, String nombre, String tipo, String identificador, Integer id_device) {
		super();
		this.id_sensor = id_sensor;
		this.nombre = nombre;
		this.tipo = tipo;
		this.identificador = identificador;
		this.id_device = id_device;
	}
	
	public Integer getId_sensor() {
		return id_sensor;
	}
	public void setId_sensor(Integer id_sensor) {
		this.id_sensor = id_sensor;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getIdentificador() {
		return identificador;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	public Integer getId_device() {
		return id_device;
	}
	public void setId_device(Integer id_device) {
		this.id_device = id_device;
	}
	@Override
	public String toString() {
		return "Sensor [id_sensor=" + id_sensor + ", nombre=" + nombre + ", tipo=" + tipo + ", identificador="
				+ identificador + ", id_device=" + id_device + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(id_device, id_sensor, identificador, nombre, tipo);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sensor other = (Sensor) obj;
		return Objects.equals(id_device, other.id_device) && Objects.equals(id_sensor, other.id_sensor)
				&& Objects.equals(identificador, other.identificador) && Objects.equals(nombre, other.nombre)
				&& Objects.equals(tipo, other.tipo);
	}

}
