package es.us.dad.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class TemperaturaEntityListWrapper {
	private List<SensorTemperaturaEntity> tempList;

	public TemperaturaEntityListWrapper() {
		super();
	}

	public TemperaturaEntityListWrapper(Collection<SensorTemperaturaEntity> TemperaturaList) {
		super();
		this.tempList = new ArrayList<SensorTemperaturaEntity>(TemperaturaList);
	}

	public TemperaturaEntityListWrapper(List<SensorTemperaturaEntity> TemperaturaList) {
		super();
		this.tempList = new ArrayList<SensorTemperaturaEntity>(TemperaturaList);

	}

	public List<SensorTemperaturaEntity> getTempList() {
		return tempList;
	}

	public void setTempList(List<SensorTemperaturaEntity> tempList) {
		this.tempList = tempList;
	}

	@Override
	public int hashCode() {
		return Objects.hash(tempList);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TemperaturaEntityListWrapper other = (TemperaturaEntityListWrapper) obj;
		return Objects.equals(tempList, other.tempList);
	}

	@Override
	public String toString() {
		return "TemperaturaEntityListWrapper [tempList=" + tempList + "]";
	}

}
