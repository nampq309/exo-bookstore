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
import org.exoplatform.bookstore.model.Book;

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
  
  public BookForm() throws Exception {
    
    // Create Book category select box.
    SelectItemOption<String> categoryNovel = new SelectItemOption<String>(Constants.CATEGORY_NOVEL,
                                                                          Constants.CATEGORY_NOVEL_VALUE);
    SelectItemOption<String> categoryStory = new SelectItemOption<String>(Constants.CATEGORY_STORY,
                                                                          Constants.CATEGORY_STORY_VALUE);
    List<SelectItemOption<String>> categoryList = new ArrayList<SelectItemOption<String>>();
    categoryList.add(categoryNovel);
    categoryList.add(categoryStory);
    
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
      
      BookstoreUtils.getBookservice().insert(b);
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

}
