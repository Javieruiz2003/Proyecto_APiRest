package es.us.dad.mysql;





import java.util.Objects;

public class SensorValue {
	
	protected Integer id;
	protected Integer id_sensor;
	protected Float valor;
	protected Long timeStamp;
	


	public SensorValue() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SensorValue(Integer id, Integer id_sensor, Float valor, Long timeStamp) {
		super();
		this.id = id;
		this.id_sensor = id_sensor;
		this.valor = valor;
		this.timeStamp = timeStamp;
		
	}
	
	public SensorValue( Integer id_sensor, Float valor, Long timeStamp) {
		super();
		this.id_sensor = id_sensor;
		this.valor = valor;
		this.timeStamp = timeStamp;
		
	}
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getId_sensor() {
		return id_sensor;
	}
	public void setId_sensor(Integer id_sensor) {
		this.id_sensor = id_sensor;
	}
	public Float getValor() {
		return valor;
	}
	public void setValor(Float valor) {
		this.valor = valor;
	}
	public Long getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	
	@Override
	public int hashCode() {
		return Objects.hash(id,  id_sensor, timeStamp, valor);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SensorValue other = (SensorValue) obj;
		return Objects.equals(id, other.id)  && Objects.equals(id_sensor, other.id_sensor)
				&& Objects.equals(timeStamp, other.timeStamp)
				&& Objects.equals(valor, other.valor);
	}
	@Override
	public String toString() {
		return "SensorValue [id=" + id + ", id_sensor=" + id_sensor + ", valor=" + valor + ", timeStamp=" + timeStamp
				+  "]";
	}
	
	
}
