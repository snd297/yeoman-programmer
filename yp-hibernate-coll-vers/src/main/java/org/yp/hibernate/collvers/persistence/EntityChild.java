/*
 * Copyright 2014 Sam Donnelly
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
package org.yp.hibernate.collvers.persistence;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Immutable;

import com.github.snd297.yp.utils.hibernate.LongId;

@Entity
@Immutable
public class EntityChild extends LongId {

  private Parent parent;

  EntityChild() {}

  public EntityChild(Parent parent) {
    this.parent = checkNotNull(parent);
  }

  // @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "parent_id",
      insertable = false,
      updatable = false)
  public Parent getParent() {
    return parent;
  }

  @SuppressWarnings("unused")
  private void setParent(Parent parent) {
    this.parent = parent;
  }

}
