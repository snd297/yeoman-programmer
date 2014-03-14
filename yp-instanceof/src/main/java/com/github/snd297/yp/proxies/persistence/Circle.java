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
package com.github.snd297.yp.proxies.persistence;

import static com.google.common.base.Preconditions.checkArgument;

import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
public class Circle extends Shape {

  private Integer radius;

  Circle() {}

  public Circle(int radius) {
    checkArgument(radius >= 1);
    this.radius = radius;
  }

  @Min(1)
  @NotNull
  public Integer getRadius() {
    return radius;
  }

  @SuppressWarnings("unused")
  private void setRadius(Integer radius) {
    this.radius = radius;
  }

}
