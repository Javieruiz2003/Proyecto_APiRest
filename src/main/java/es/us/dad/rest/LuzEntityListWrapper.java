package es.us.dad.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class LuzEntityListWrapper {
	private List<SensorLuzEntity> luzList;

	public LuzEntityListWrapper() {
		super();
	}

	public LuzEntityListWrapper(Collection<SensorLuzEntity> lucesList) {
		super();
		this.luzList = new ArrayList<SensorLuzEntity>(lucesList);
	}

	public LuzEntityListWrapper(List<SensorLuzEntity> lucesList) {
		super();
		this.luzList = new ArrayList<SensorLuzEntity>(lucesList);
	}

	public List<SensorLuzEntity> getLuzList() {
		return luzList;
	}

	public void setLuzList(List<SensorLuzEntity> luzList) {
		this.luzList = luzList;
	}

	@Override
	public int hashCode() {
		return Objects.hash(luzList);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LuzEntityListWrapper other = (LuzEntityListWrapper) obj;
		return Objects.equals(luzList, other.luzList);
	}

	@Override
	public String toString() {
		return "LuzEntityListWrapper [luzList=" + luzList + "]";
	}

}
