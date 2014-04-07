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

import static com.google.common.collect.Sets.newHashSet;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.github.snd297.yp.utils.hibernate.LongIdAndVersion;

@Entity
public class Parent extends LongIdAndVersion {
  private Set<EmbeddableChild> embeddableChildren = newHashSet();
  private Set<EntityChild> entityChildren = newHashSet();

  @ElementCollection
  public Set<EmbeddableChild> getEmbeddableChildren() {
    return embeddableChildren;
  }

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "parent_id")
  public Set<EntityChild> getEntityChildren() {
    return entityChildren;
  }

  @SuppressWarnings("unused")
  private void setEmbeddableChildren(Set<EmbeddableChild> embeddableChildren) {
    this.embeddableChildren = embeddableChildren;
  }

  @SuppressWarnings("unused")
  private void setEntityChildren(Set<EntityChild> entityChildren) {
    this.entityChildren = entityChildren;
  }
}
