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

import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Square extends Shape {

  private Integer sideLength;

  private String squareCode;

  Square() {}

  public Square(Integer sideLength) {
    this.sideLength = checkNotNull(sideLength);
    checkArgument(sideLength >= 1);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Square)) {
      return false;
    }
    Square other = (Square) obj;
    if (squareCode == null) {
      if (other.squareCode != null) {
        return false;
      }
    } else if (!squareCode.equals(other.squareCode)) {
      return false;
    }
    return true;
  }

  public Integer getSideLength() {
    return sideLength;
  }

  @Column(unique = true)
  @Nonnull
  public String getSquareCode() {
    return squareCode;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((squareCode == null) ? 0 : squareCode.hashCode());
    return result;
  }

  public void setSideLength(Integer sideLength) {
    this.sideLength = sideLength;
  }

  public void setSquareCode(String squareCode) {
    this.squareCode = squareCode;
  }
}
