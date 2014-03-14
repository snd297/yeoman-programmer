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
public class Rectangle extends Shape {

  private Integer length;
  private Integer width;

  Rectangle() {}

  public Rectangle(
      int length,
      int width) {
    checkArgument(length >= 1);
    checkArgument(width >= 1);
    this.length = length;
    this.width = width;
  }

  @Min(1)
  @NotNull
  public Integer getLength() {
    return length;
  }

  @SuppressWarnings("unused")
  private void setLength(Integer length) {
    this.length = length;
  }

  @Min(1)
  @NotNull
  public Integer getWidth() {
    return width;
  }

  @SuppressWarnings("unused")
  private void setWidth(Integer width) {
    this.width = width;
  }

}
