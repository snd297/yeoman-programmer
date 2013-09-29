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
package com.github.snd297.yp.proxies.model;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Square extends Shape {

  private Integer sideLength;

  Square() {}

  public Square(Integer sideLength) {
    this.sideLength = checkNotNull(sideLength);
    checkArgument(sideLength >= 1);
  }

  @Size(min = 1)
  @NotNull
  public Integer getSideLength() {
    return sideLength;
  }

  public void setSideLength(Integer sideLength) {
    this.sideLength = sideLength;
  }

}
