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

import java.util.List;

import org.exoplatform.bookstore.BookstoreUtils;
import org.exoplatform.bookstore.jcr.model.Book;
import org.exoplatform.bookstore.service.api.BookStoreService;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIComponent;
import org.exoplatform.webui.core.UIPopupWindow;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.Event.Phase;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.form.UIForm;
import org.exoplatform.webui.form.UIFormStringInput;

@ComponentConfig(
  lifecycle = UIFormLifecycle.class,
  template = "app:/groovy/webui/component/UIBookList.gtmpl",
  events = {
    @EventConfig(listeners = UIBookList.SearchBookActionListener.class, phase = Phase.DECODE),
    @EventConfig(listeners = UIBookList.NewCategoryActionListener.class, phase = Phase.DECODE),
    @EventConfig(listeners = UIBookList.NewBookActionListener.class, phase = Phase.DECODE),
    @EventConfig(listeners = UIBookList.EditActionListener.class, phase = Phase.DECODE),
    @EventConfig(listeners = UIBookList.ViewActionListener.class, phase = Phase.DECODE),
    @EventConfig(listeners = UIBookList.DeleteActionListener.class, confirm = "Are you sure to delete this book?")
  }
)
public class UIBookList extends UIForm {

  public static final String TXT_BOOK_SEARCH = "txtBookSearch";

  public static final String LBL_BOOK_SEARCH = "Search by Title of book";

  public UIBookList() throws Exception {
    addUIFormInput(new UIFormStringInput(TXT_BOOK_SEARCH,null, null));
    UIPopupWindow popup = addChild(UIPopupWindow.class, null, null);
    popup.setRendered(false);
    addChild(popup);
  }

  public static List<Book> bookList = null;

  public static List<Book> getBookList() {
    if (bookList != null) {
      return bookList;
    }
    return BookstoreUtils.getBookstoreService().findAll();
  }

  /**
   * Listens to search book by title
   *
   */
  public static class SearchBookActionListener extends EventListener<UIBookList> {
    @Override
    public void execute(Event<UIBookList> event) throws Exception {
      UIBookList uiBookList = event.getSource();
      UIFormStringInput txtSearch = uiBookList.getUIStringInput(TXT_BOOK_SEARCH);
      if(txtSearch != null){
        String title = txtSearch.getValue();
        System.out.println("Get text of search: '"+title);
        if(title != null && title != "")
          bookList = BookstoreUtils.getBookstoreService().findByTitle(title);
        else 
          bookList = BookstoreUtils.getBookstoreService().findAll();
        event.getRequestContext().addUIComponentToUpdateByAjax(uiBookList);
      }else {
        System.out.println("Couldn't find the text search input !!!");
      }
    }
  }

  /**
   * Listens to create new Category
   *
   */
  public static class NewCategoryActionListener extends EventListener<UIBookList> {
    @Override
    public void execute(Event<UIBookList> event) throws Exception {
      UIBookList form = event.getSource();
      CategoryForm categoryForm = form.createUIComponent(CategoryForm.class, null, null);
      form.setUIComponentForPopupWindow(form, categoryForm);
    }
  }

  /**
   * Listens to create new Book
   *
   */
  public static class NewBookActionListener extends EventListener<UIBookList> {
    @Override
    public void execute(Event<UIBookList> event) throws Exception {
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
      BookStoreService bookService = BookstoreUtils.getBookstoreService();
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
      BookStoreService bookService = BookstoreUtils.getBookstoreService();
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
      BookstoreUtils.getBookstoreService().deleteBook(bookId);
    }
  }

  /**Set content for UIPopupWindow */
  private void setUIComponentForPopupWindow(UIBookList form, UIComponent uiComponent) throws Exception {
    UIPopupWindow popup = form.getChild(UIPopupWindow.class);
    if(popup == null) {
      popup = form.addChild(UIPopupWindow.class, null, null);
      form.addChild(popup);
    }
    popup.setWindowSize(400, 250);
    popup.setUIComponent(uiComponent);
    popup.setRendered(true);
    popup.setShow(true);
  }

}
