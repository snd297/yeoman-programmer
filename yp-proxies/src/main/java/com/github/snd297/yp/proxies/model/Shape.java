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

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.annotations.NaturalId;

import com.github.snd297.yp.utils.hibernate.LongIdAndVersion;

@Entity
public abstract class Shape extends LongIdAndVersion {
  private UUID ssn;

  protected Shape() {
    this.ssn = UUID.randomUUID();
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Shape)) {
      return false;
    }
    Shape other = (Shape) obj;
    if (ssn == null) {
      if (other.getSsn() != null) {
        return false;
      }
    } else if (!ssn.equals(other.getSsn())) {
      return false;
    }
    return true;
  }

  @NaturalId
  @Column
  @Nonnull
  public UUID getSsn() {
    return ssn;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((ssn == null) ? 0 : ssn.hashCode());
    return result;
  }

  @SuppressWarnings("unused")
  private void setSsn(UUID ssn) {
    this.ssn = ssn;
  }

}
