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

import javax.persistence.Entity;

import org.hibernate.annotations.Immutable;

import com.github.snd297.yp.utils.hibernate.LongId;

@Entity
@Immutable
public class Rectangle extends LongId {

  private Integer width;

  private Integer height;

  Rectangle() {}

  public Rectangle(Integer width, Integer height) {
    this.width = width;
    this.height = height;
  }

  public Integer getHeight() {
    return height;
  }

  public Integer getWidth() {
    return width;
  }

  @SuppressWarnings("unused")
  private void setHeight(Integer height) {
    this.height = height;
  }

  @SuppressWarnings("unused")
  private void setWidth(Integer width) {
    this.width = width;
  }

}
