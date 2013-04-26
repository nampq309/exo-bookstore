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
package org.exoplatform.bookstore.webui;

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIComponent;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.core.model.SelectItemOption;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.event.Event.Phase;
import org.exoplatform.webui.form.UIForm;
import org.exoplatform.webui.form.UIFormSelectBox;
import org.exoplatform.webui.form.UIFormStringInput;
import org.exoplatform.webui.form.validator.MandatoryValidator;
import org.exoplatform.webui.form.validator.SpecialCharacterValidator;
import org.exoplatform.webui.form.validator.StringLengthValidator;

import org.exoplatform.bookstore.BookstoreUtils;
import org.exoplatform.bookstore.commons.Constants;
import org.exoplatform.bookstore.jcr.model.Book;
import org.exoplatform.bookstore.jcr.model.Category;

/**
 * 
 * Add/Edit a Book
 */
@ComponentConfig(
 lifecycle = UIFormLifecycle.class,
 template = "system:/groovy/webui/form/UIForm.gtmpl",
 events = {
   @EventConfig(listeners = BookForm.SaveActionListener.class),
   @EventConfig(listeners = BookForm.ResetActionListener.class, phase = Phase.DECODE),
   @EventConfig(listeners = BookForm.CancelActionListener.class, phase = Phase.DECODE)
 } 
)
public class BookForm extends UIForm {
  
  //Name of text Title
  public final String TXT_TITLE = "txtTitle";
      
  //Name of combo Category
  public final String CMB_CATEGORIES = "cmbCategory";
  
  //Name of text ISBN
  public final String TXT_ISBN = "txtIsbn";
  
  //Name of text Publisher
  public final String TXT_PUBLISHER = "txtPublisher";
  
  private Book book = null;
  
  private boolean isCreate = false;
  
  public List<SelectItemOption<String>> categoryList;
  
  public BookForm() throws Exception {
    
    // Create Book category select box.
    categoryList = new ArrayList<SelectItemOption<String>>();
    
    // Add form input.
    addUIFormInput(new UIFormStringInput(TXT_TITLE, TXT_TITLE, null)
                   .addValidator(MandatoryValidator.class)
                   .addValidator(SpecialCharacterValidator.class)
                   .addValidator(StringLengthValidator.class, 10, 50));
    
    //Combo Category
    UIFormSelectBox uiFormSelectBox = new UIFormSelectBox(CMB_CATEGORIES, CMB_CATEGORIES, categoryList);
    uiFormSelectBox.addValidator(MandatoryValidator.class);
    addUIFormInput(uiFormSelectBox);
    
    addUIFormInput(new UIFormStringInput(TXT_ISBN, TXT_ISBN, null)
                   .addValidator(MandatoryValidator.class)
                   .addValidator(StringLengthValidator.class, 10, 20));
    
    addUIFormInput(new UIFormStringInput(TXT_PUBLISHER, TXT_PUBLISHER, null)
                   .addValidator(MandatoryValidator.class)
                   .addValidator(StringLengthValidator.class, 10, 50));
    
    setSubmitAction("Save");
  }
  
  
  /**
   * Listens to create/update book
   *
   */
  public static class SaveActionListener extends EventListener<BookForm> {

    @Override
    public void execute(Event<BookForm> event) throws Exception {
      BookForm form = event.getSource();
      Book b = form.getBook();
      b.setTitle(form.getUIStringInput(form.TXT_TITLE).getValue());
      b.setIsbn(form.getUIStringInput(form.TXT_ISBN).getValue());
      b.setPublisher(form.getUIStringInput(form.TXT_PUBLISHER).getValue());
      b.setCategory(form.getUIFormSelectBox(form.CMB_CATEGORIES).getValue());
      if(form.isCreate)
        BookstoreUtils.getBookstoreService().insert(b);
      else 
        BookstoreUtils.getBookstoreService().updateBook(b);
    }
  }
  
  /**
   * Listens to reset form fields to default value
   *
   */
  public static class ResetActionListener extends EventListener<BookForm> {

    @Override
    public void execute(Event<BookForm> event) throws Exception {
      BookForm form = event.getSource();
      form.reset();
    }
  }
  
  /**
   * Listens to cancel event and back to list form
   *
   */
  public static class CancelActionListener extends EventListener<BookForm> {
    @Override
    public void execute(Event<BookForm> event) throws Exception {
      
    }
  }

  public Book getBook() {
    if(book == null){
      book = new Book();
    }
    return book;
  }

  public void setBook(Book book) {
    this.book = book;
    //Fill book's info
    this.getUIStringInput(TXT_TITLE).setValue(book.getTitle());
    this.getUIFormSelectBox(CMB_CATEGORIES).setValue(book.getCategory());
    this.getUIStringInput(TXT_ISBN).setValue(book.getIsbn());
    this.getUIStringInput(TXT_PUBLISHER).setValue(book.getPublisher());
  }
  
  
  private List<Category> getCategories() {
    List<Category> list = new ArrayList<Category>();
    if(categoryList != null){
      list = BookstoreUtils.getBookstoreService().getAllCategories();
      categoryList = new ArrayList<SelectItemOption<String>>();
      for(Category category : list){
        // Create Book category select box.
        SelectItemOption<String> categoryItem = new SelectItemOption<String>(category.getLblCategory(),
                                                                              category.getId());
        categoryList.add(categoryItem);
      }
    }
    return list;
  }

  public boolean isCreate() {
    return isCreate;
  }

  public void setCreate(boolean isCreate) {
    this.isCreate = isCreate;
  }

  public List<SelectItemOption<String>> getCategoryList() {
    return categoryList;
  }

  public void setCategoryList(List<SelectItemOption<String>> categoryList) {
    this.categoryList = categoryList;
  }

}
