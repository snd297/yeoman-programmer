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
package com.github.snd297.yp.hibernatecollections.persistence;

import static com.google.common.collect.Sets.newHashSet;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.github.snd297.yp.utils.hibernate.LongIdAndVersion;

@Entity
public class BandWithForeignKey extends LongIdAndVersion {
  private Set<BandMemberWithForeignKey> members = newHashSet();

  @OneToMany(orphanRemoval = true)
  @JoinColumn(name = "band_id")
  public Set<BandMemberWithForeignKey> getMembers() {
    return members;
  }

  public void setMembers(Set<BandMemberWithForeignKey> members) {
    this.members = members;
  }
}
