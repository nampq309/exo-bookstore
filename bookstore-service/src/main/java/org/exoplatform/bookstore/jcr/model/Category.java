/*
 * Copyright (C) 2003-2013 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU Affero General Public License
* as published by the Free Software Foundation; either version 3
* of the License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.bookstore.jcr.model;

import org.exoplatform.bookstore.service.api.BookstoreNodeTypes;
import org.exoplatform.services.jcr.util.IdGenerator;

public class Category {
  
  public Category(String lblCategory) {
    this.id = BookstoreNodeTypes.CATEGORY + IdGenerator.generate();
    this.lblCategory = lblCategory;
  }
  
  private String id;
  
  private String lblCategory;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLblCategory() {
    return lblCategory;
  }

  public void setLblCategory(String lblCategory) {
    this.lblCategory = lblCategory;
  }

}
