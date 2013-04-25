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
package org.exoplatform.bookstore.storage.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.exoplatform.bookstore.commons.Constants;
import org.exoplatform.bookstore.model.Book;
import org.exoplatform.bookstore.storage.api.BookStorage;

public class BookStorageImpl implements BookStorage {
  

  private Map<String, Book> data = new HashMap<String, Book>();
  private static BookStorageImpl instance = null;
  
  @Override
  public Book insert(Book book) {
    String bookId = book.getId();
    if(!bookId.equals("")){
      while(true){
        String tmpBookId = "id"+ new Random().nextInt(1000);
        if(findById(tmpBookId) == null){
          book.setId(tmpBookId);
          break;
        }
      }
    }
    return null;
  }

  @Override
  public Book findById(String id) {
    return data.get(id);
  }

  @Override
  public Book findByIsbn(String isbn) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Book> findByTitle(String title) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Book> findByPublisher(String publisher) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Book> findAll() {
    loadTmpData();
    List<Book> list = new ArrayList<Book>();
    for(Book b : data.values()){
      list.add(b);
    }
    return list;
  }

  @Override
  public void updateBook(Book book) {
    data.put(book.getId(), book);
  }

  @Override
  public void deleteBook(String id) {
    data.remove(id);
  }

  @Override
  public void deleteAll() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public boolean isExists(String isbn) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public List<String> getAllCategories() {
    // TODO Auto-generated method stub
    return null;
  }
  
  /** Just for test purpose */
  private void loadTmpData() {
    if(data.size() == 0){
      for(int i =1; i <= 10; i++){
        String bookId = "id"+i;
        String category = Constants.CATEGORY_NOVEL_VALUE;
        if(i%2 == 0) category = Constants.CATEGORY_STORY_VALUE;
        Book b = new Book("id"+i, category, "ISBN "+i, "Title "+i, "Publisher "+i);
        data.put(bookId, b);
      }
    }
  }
  
  public static BookStorageImpl getInstance() {
    if(instance == null) instance = new BookStorageImpl();
    return instance;
  }

}
