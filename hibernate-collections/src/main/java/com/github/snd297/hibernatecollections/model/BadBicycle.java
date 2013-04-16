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

package com.github.snd297.hibernatecollections.model;

import static com.google.common.collect.Sets.newHashSet;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

@Entity
public class BadBicycle extends LongIdAndVersion {
	private Set<WheelInBadBicycle> wheels = newHashSet();

	@OneToMany(
			mappedBy = "bicycle",
			fetch = FetchType.EAGER,
			orphanRemoval = true)
	@Size(max = 2)
	public Set<WheelInBadBicycle> getWheels() {
		return wheels;
	}

	public void setWheels(Set<WheelInBadBicycle> wheels) {
		this.wheels = wheels;
	}

}