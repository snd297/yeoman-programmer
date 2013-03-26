package com.github.snd297.hibernateequals.model;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NaturalId;

import com.google.common.base.Objects;

@Entity
public class Car extends LongIdAndVersion {
	private String vin;

	/** For JPA. */
	Car() {}

	public Car(String vin) {
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
		if (!(obj instanceof Car)) {
			return false;
		}
		Car other = (Car) obj;
		if (!Objects.equal(this.vin, other.getVin())) {
			return false;
		}
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
