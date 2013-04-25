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

import org.exoplatform.bookstore.BookstoreUtils;
import org.exoplatform.bookstore.model.Book;
import org.exoplatform.bookstore.storage.api.BookStorage;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIComponent;
import org.exoplatform.webui.core.UIPopupWindow;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.event.Event.Phase;
import org.exoplatform.webui.form.UIForm;

@ComponentConfig(
 lifecycle = UIFormLifecycle.class,
 template = "app:/groovy/webui/component/UIBookList.gtmpl",
 events = {
   @EventConfig(listeners = UIBookList.CreateActionListener.class, phase = Phase.DECODE),
   @EventConfig(listeners = UIBookList.EditActionListener.class, phase = Phase.DECODE),
   @EventConfig(listeners = UIBookList.ViewActionListener.class, phase = Phase.DECODE),
   @EventConfig(listeners = UIBookList.DeleteActionListener.class, confirm = "Are you sure to delete this book?")
 }
)
public class UIBookList extends UIForm {
  
  public UIBookList() throws Exception {

  }
  
  public static List<Book> bookList = new ArrayList<Book>();
  
  public static List<Book> getBookList() {
    //SET META DATA just for Test purpose
    if(bookList.size() > 0) {
      //bookList.add(new Book("id1", "Story", "ISBN XXX", "Story 1", "NXB Tuoi tre"));
      //bookList.add(new Book("id2", "Story", "ISBN YYY", "Story 2", "NXB Tuoi tre"));
      //bookList.add(new Book("id3", "Story", "ISBN ZZZ", "Story 3", "NXB Tuoi tre"));
      return bookList;
    }
    return BookstoreUtils.getBookservice().findAll();
  }
  
  
  /**
   * Listens to create new Book
   *
   */
  public static class CreateActionListener extends EventListener<UIBookList> {
    @Override
    public void execute(Event<UIBookList> event) throws Exception {
      WebuiRequestContext ctx = event.getRequestContext();
      String bookId = ctx.getRequestParameter("objectId");
      System.out.println("Get BookId: "+ bookId);
      UIBookList form = event.getSource();
      BookForm bookForm = form.createUIComponent(BookForm.class, null, null);
      bookForm.setCreate(true);
      form.setUIComponentForPopupWindow(form, bookForm);
    }
  }

  /**
   * Listens to edit a Book item
   *
   */
  public static class EditActionListener extends EventListener<UIBookList> {
    @Override
    public void execute(Event<UIBookList> event) throws Exception {
      WebuiRequestContext ctx = event.getRequestContext();
      String bookId = ctx.getRequestParameter("objectId");
      System.out.println("Get BookId: "+ bookId);
      UIBookList form = event.getSource();
      BookForm bookForm = form.createUIComponent(BookForm.class, null, null);
      //TODO Set book's info
      BookStorage bookService = BookstoreUtils.getBookservice();
      Book book = bookService.findById(bookId);
      bookForm.setBook(book);
      form.setUIComponentForPopupWindow(form, bookForm);
    }
  }
  
  /**
   * Listens to view a Book item
   *
   */
  public static class ViewActionListener extends EventListener<UIBookList> {

    @Override
    public void execute(Event<UIBookList> event) throws Exception {
      WebuiRequestContext ctx = event.getRequestContext();
      String bookId = ctx.getRequestParameter("objectId");
      System.out.println("Get BookId: "+ bookId);
      UIBookList form = event.getSource();
      BookFormViewer bookFormViewer = form.createUIComponent(BookFormViewer.class, null, null);
      //TODO Set book's info
      BookStorage bookService = BookstoreUtils.getBookservice();
      Book book = bookService.findById(bookId);
      bookFormViewer.setBook(book);
      form.setUIComponentForPopupWindow(form, bookFormViewer);
    }
  }
  
  /**
   * Listens to delete a Book item
   *
   */
  public static class DeleteActionListener extends EventListener<UIBookList> {

    @Override
    public void execute(Event<UIBookList> event) throws Exception {
      WebuiRequestContext ctx = event.getRequestContext();
      String bookId = ctx.getRequestParameter("objectId");
      BookstoreUtils.getBookservice().deleteBook(bookId);
    }
  }
  
  private void setUIComponentForPopupWindow(UIBookList form, UIComponent uiComponent) throws Exception {
    UIPopupWindow popup = form.getChild(UIPopupWindow.class);
    if(popup == null) {
      popup = form.addChild(UIPopupWindow.class, null, null);
      popup.setWindowSize(400, 250);
      form.addChild(popup);
    }
    popup.setUIComponent(uiComponent);
    popup.setRendered(true);
    popup.setShow(true);
  }
  
}
