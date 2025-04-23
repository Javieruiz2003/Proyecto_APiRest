package es.us.dad.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class ActLedEntityListWrapper {
	private List<ActLedEntity> ledList;

	public ActLedEntityListWrapper() {
		super();
	}

	public ActLedEntityListWrapper(List<ActLedEntity> ledList) {
		super();
		this.ledList = ledList;
	}

	public ActLedEntityListWrapper(Collection<ActLedEntity> ledList) {
		super();
		this.ledList = new ArrayList<ActLedEntity>(ledList);
	}

	public List<ActLedEntity> getLedList() {
		return ledList;
	}

	public void setLedList(List<ActLedEntity> ledList) {
		this.ledList = ledList;
	}

	@Override
	public int hashCode() {
		return Objects.hash(ledList);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActLedEntityListWrapper other = (ActLedEntityListWrapper) obj;
		return Objects.equals(ledList, other.ledList);
	}

	@Override
	public String toString() {
		return "ActLedEntityListWrapper [ledList=" + ledList + "]";
	}
}
