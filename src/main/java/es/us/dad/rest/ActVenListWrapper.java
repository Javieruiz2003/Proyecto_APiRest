package es.us.dad.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class ActVenListWrapper {
	private List<ActVenEntity> venList;

	public ActVenListWrapper() {
		super();
	}

	public ActVenListWrapper(Collection<ActVenEntity> venList) {
		super();
		this.venList = new ArrayList<ActVenEntity>(venList);
	}

	public List<ActVenEntity> getVenList() {
		return venList;
	}

	public void setVenList(List<ActVenEntity> venList) {
		this.venList = venList;
	}

	@Override
	public int hashCode() {
		return Objects.hash(venList);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActVenListWrapper other = (ActVenListWrapper) obj;
		return Objects.equals(venList, other.venList);
	}

	@Override
	public String toString() {
		return "ActVenListWrapper [venList=" + venList + "]";
	}

	public ActVenListWrapper(List<ActVenEntity> venList) {
		super();
		this.venList = venList;
	}

}
