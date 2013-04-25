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

import org.exoplatform.bookstore.commons.Constants;

public class Book {
  
  /** Id. */
  private String id;
  
  /** Book category. */
  private String category;
  private String lblCategory;
  
  /** Book ISBN. */
  private String isbn;
  
  /** Book title. */
  private String title;
  
  /** Book publisher. */
  private String publisher;
  
  public Book(String id, String category, String isbn, String title, String publisher){
    this.id = id;
    this.category = category;
    //Set Category label
    if(this.category.equals(Constants.CATEGORY_NOVEL_VALUE)){
      this.lblCategory = Constants.CATEGORY_NOVEL;
    } else if(this.category.equals(Constants.CATEGORY_STORY_VALUE)){
      this.lblCategory = Constants.CATEGORY_STORY;
    }
    this.isbn = isbn;
    this.title = title;
    this.publisher = publisher;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

  public String getLblCategory() {
    return lblCategory;
  }

  public void setLblCategory(String lblCategory) {
    this.lblCategory = lblCategory;
  }

}