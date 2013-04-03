package com.github.snd297.hibernateequals.model;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NaturalId;

@Entity
public class GetClassCar extends LongIdAndVersion {
	private String vin;

	/** For JPA. */
	GetClassCar() {}

	public GetClassCar(String vin) {
		this.vin = vin;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		// GetClassCar other = (GetClassCar) obj;
		// if (vin == null) {
		// if (other.vin != null) {
		// return false;
		// }
		// } else if (!vin.equals(other.vin)) {
		// return false;
		// }
		return true;
	}

	@NaturalId
	@NotNull
	public String getVin() {
		return vin;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((vin == null) ? 0 : vin.hashCode());
		return result;
	}

	@SuppressWarnings("unused")
	private void setVin(String vin) {
		this.vin = vin;
	}

}
