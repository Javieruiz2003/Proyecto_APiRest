package es.us.dad.mysql;

import java.util.Objects;

public class Actuador {

	protected Integer id;
	protected String nombre;
	protected String tipo;
	protected String identificador;
	protected Integer id_dispositivo;
	
	public Actuador() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Actuador(Integer id, String nombre, String tipo, String identificador, Integer id_dispositivo) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.tipo = tipo;
		this.identificador = identificador;
		this.id_dispositivo = id_dispositivo;
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
	public Integer getid_dispositivo() {
		return id_dispositivo;
	}
	public void setid_dispositivo(Integer id_dispositivo) {
		this.id_dispositivo = id_dispositivo;
	}
	@Override
	public int hashCode() {
		return Objects.hash(id, id_dispositivo, identificador, nombre, tipo);
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
		return Objects.equals(id, other.id) && Objects.equals(id_dispositivo, other.id_dispositivo)
				&& Objects.equals(identificador, other.identificador) && Objects.equals(nombre, other.nombre)
				&& Objects.equals(tipo, other.tipo);
	}
	@Override
	public String toString() {
		return "Actuador [id=" + id + ", nombre=" + nombre + ", tipo=" + tipo + ", identificador=" + identificador
				+ ", id_dispositivo=" + id_dispositivo + "]";
	}
}
