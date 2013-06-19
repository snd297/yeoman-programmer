/*
 * Copyright 2013 Sam Donnelly
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.snd297.hibernateequals.model;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NaturalId;

@Entity
public class BrokenEqualsCar extends LongIdAndVersion {
	private String vin;

	/** For JPA. */
	BrokenEqualsCar() {}

	public BrokenEqualsCar(String vin) {
		this.vin = vin;
	}

	@Override
	public boolean equals(@Nullable Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof BrokenEqualsCar)) {
			return false;
		}
		BrokenEqualsCar other = (BrokenEqualsCar) obj;
		if (vin == null) {
			if (other.vin != null) {
				return false;
			}
		} else if (!vin.equals(other.vin)) {
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
