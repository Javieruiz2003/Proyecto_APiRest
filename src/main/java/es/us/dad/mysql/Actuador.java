package es.us.dad.mysql;

import java.util.Objects;

public class Actuador {

	protected Integer id;
	protected String nombre;
	protected String tipo;
	protected String identificador;
	protected Integer id_device;
	
	public Actuador() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Actuador(Integer id, String nombre, String tipo, String identificador, Integer id_device) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.tipo = tipo;
		this.identificador = identificador;
		this.id_device = id_device;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public int hashCode() {
		return Objects.hash(id, id_device, identificador, nombre, tipo);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Actuador other = (Actuador) obj;
		return Objects.equals(id, other.id) && Objects.equals(id_device, other.id_device)
				&& Objects.equals(identificador, other.identificador) && Objects.equals(nombre, other.nombre)
				&& Objects.equals(tipo, other.tipo);
	}
	@Override
	public String toString() {
		return "Actuador [id=" + id + ", nombre=" + nombre + ", tipo=" + tipo + ", identificador=" + identificador
				+ ", id_device=" + id_device + "]";
	}
}
