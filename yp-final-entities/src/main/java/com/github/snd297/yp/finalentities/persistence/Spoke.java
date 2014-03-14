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
package com.github.snd297.yp.finalentities.persistence;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.github.snd297.yp.utils.hibernate.LongIdAndVersion;

@Entity
public class Spoke extends LongIdAndVersion {
  private Wheel wheel;

  /** For Hibernate. */
  Spoke() {}

  public Spoke(Wheel wheel) {
    this.wheel = wheel;
    wheel.getSpokes().add(this);
  }

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  public Wheel getWheel() {
    return wheel;
  }

  public void setWheel(Wheel wheel) {
    this.wheel = wheel;
  }
}
