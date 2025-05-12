package es.us.dad.mysql;

import java.util.Objects;

public class Dispositivo {

	
	protected Integer id;
	protected String nombre;
	protected Integer id_grupo;
	
	public Dispositivo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Dispositivo(Integer id, String nombre, Integer id_grupo) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.id_grupo = id_grupo;
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
	public Integer getId_grupo() {
		return id_grupo;
	}
	public void setId_grupo(Integer id_grupo) {
		this.id_grupo = id_grupo;
	}
	@Override
	public int hashCode() {
		return Objects.hash(id, id_grupo, nombre);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dispositivo other = (Dispositivo) obj;
		return Objects.equals(id, other.id) && Objects.equals(id_grupo, other.id_grupo)
				&& Objects.equals(nombre, other.nombre);
	}
	@Override
	public String toString() {
		return "Dispositivo [id=" + id + ", nombre=" + nombre + ", id_grupo=" + id_grupo + "]";
	}
	
	
}
