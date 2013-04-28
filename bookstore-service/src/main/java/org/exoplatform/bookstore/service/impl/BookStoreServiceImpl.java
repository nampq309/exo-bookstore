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
package org.exoplatform.bookstore.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;

import org.exoplatform.bookstore.jcr.model.Book;
import org.exoplatform.bookstore.jcr.model.Category;
import org.exoplatform.bookstore.service.api.BookStoreService;
import org.exoplatform.bookstore.service.api.BookstoreNodeTypes;
import org.exoplatform.bookstore.storage.impl.BookStorageImpl;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.core.ManageableRepository;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.jcr.impl.core.query.QueryImpl;
import org.picocontainer.Startable;

public class BookStoreServiceImpl implements BookStoreService, Startable, BookstoreNodeTypes {

  private Map<String, Book>      data                   = new HashMap<String, Book>();

  //
  public static final String     DEFAULT_WORKSPACE_NAME = "portal-system";

  public BookStoreServiceImpl() {

  }

  @Override
  public Book insert(Book book) {
    SessionProvider sessionProvider = SessionProvider.createSystemProvider();
    Node bookNode = null;
    try {
      Node parentNode = (Node) getSession(sessionProvider).getItem(book.getParentPath());
      bookNode = parentNode.addNode(book.getId(), EXO_BOOK);
      bookNode.setProperty(EXO_TITLE, book.getTitle());
      bookNode.setProperty(EXO_ISBN, book.getIsbn());

      // Do save into repository
      parentNode.getSession().save();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      sessionProvider.close();
    }
    return book;
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
    SessionProvider sessionProvider = SessionProvider.createSystemProvider();

    List<Book> list = new ArrayList<Book>();
    try {
      String strQuery = "//element(*, " + EXO_BOOK + ") jcr:like(@" + EXO_TITLE+ ",'%"+ title + "%')";
      QueryManager qm = getSession(sessionProvider).getWorkspace().getQueryManager();
      Query query = qm.createQuery(strQuery, Query.XPATH);
      QueryImpl queryImpl = (QueryImpl) query;
      NodeIterator nodeItr = queryImpl.execute().getNodes();
      // read book to list
      while (nodeItr.hasNext()) {
        Node bookNode = nodeItr.nextNode();
        Book book = new Book();
        book.setId(bookNode.getName());
        book.setTitle(bookNode.getProperty(EXO_TITLE).getString());
        book.setIsbn(bookNode.getProperty(EXO_ISBN).getString());
        
        list.add(book);
      }

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      sessionProvider.close();
    }

    return null;
  }

  @Override
  public List<Book> findByPublisher(String publisher) {
    return null;
  }

  @Override
  public List<Book> findAll() {

    // just for test
    initialNodes();

    SessionProvider sessionProvider = SessionProvider.createSystemProvider();

    List<Book> list = new ArrayList<Book>();
    try {

      // Get all of Books by SELECT query
      String strQuery = "//element(*, " + EXO_BOOK + ")";

      QueryManager qm = getSession(sessionProvider).getWorkspace().getQueryManager();
      Query query = qm.createQuery(strQuery, Query.XPATH);
      QueryImpl queryImpl = (QueryImpl) query;
      NodeIterator nodeItr = queryImpl.execute().getNodes();

      // read book to list
      while (nodeItr.hasNext()) {
        Node bookNode = nodeItr.nextNode();
        String bId = bookNode.getName();
        String bTitle = bookNode.getProperty(EXO_TITLE).getString();
        String bIsbn = bookNode.getProperty(EXO_ISBN).getString();

        Book book = new Book(bookNode.getParent().getPath(), bIsbn, bTitle, "NXB XXX");
        book.setId(bId);

        list.add(book);
      }

    } catch (Exception e) {
      // TODO: handle exception
    } finally {
      sessionProvider.close();
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
  public List<Category> getAllCategories() {
    SessionProvider sessionProvider = SessionProvider.createSystemProvider();
    
    List<Category> list = new ArrayList<Category>();
    try {

      String strQuery = "//element(*, " + EXO_CATEGORY + ")";

      QueryManager qm = getSession(sessionProvider).getWorkspace().getQueryManager();
      Query query = qm.createQuery(strQuery, Query.XPATH);
      QueryImpl queryImpl = (QueryImpl) query;
      NodeIterator nodeItr = queryImpl.execute().getNodes();

      //Read categories to list
      while (nodeItr.hasNext()) {
        Node cateNode = nodeItr.nextNode();
        Category cate = new Category(cateNode.getProperty(EXO_LBL_CATEGORY).getString());
        cate.setId(cateNode.getName());

        System.out.println("Add Category '"+cate.getLblCategory() + "to list");
        list.add(cate);
      }

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      sessionProvider.close();
    }

    return list;
  }

  /** Just for test purpose */
  private void initialNodes() {
    SessionProvider sessionProvider = SessionProvider.createSystemProvider();
    // Initial structure of Nodes

    try {
      System.out.println("Initial Bookstore Tree ...");
      Session session = getSession(sessionProvider);
      Node rootNode = session.getRootNode();
      Node categoryNode = null;
      if (!rootNode.hasNode(EXO_BOOKSTORE)) {
        Node bookStoreNode = rootNode.addNode(BOOKSTORE, EXO_BOOKSTORE);

        System.out.println("Bookstore Node: '" + bookStoreNode.getPath());
        System.out.println("Bookstore Name: '" + bookStoreNode.getName());

        Node categoriesNode = bookStoreNode.addNode(CATEGORIES, EXO_CATEGORIES);
        System.out.println("categoriesNode Node: '" + categoriesNode.getPath());
        System.out.println("categoriesNode Name: '" + categoriesNode.getName());

        // Add Categories to repository
        Category cate = new Category("Novel");
        categoryNode = categoriesNode.addNode(cate.getId(), EXO_CATEGORY);
        //categoryNode.setProperty(EXO_LBL_CATEGORY, cate.getLblCategory());
        System.out.println("categoryNode Node: '" + categoryNode.getPath());
        System.out.println("categoryNode Name: '" + categoryNode.getName());
        
        cate = new Category("Story");
        //categoryNode = categoriesNode.addNode(cate.getId(), EXO_CATEGORY);
        //categoryNode.setProperty(EXO_LBL_CATEGORY, cate.getLblCategory());
        
        //add Book
        Node bookNode = categoryNode.addNode(new Book().getId(), EXO_BOOK);
        bookNode.setProperty(EXO_TITLE, "Title first");
        bookNode.setProperty(EXO_ISBN, "ISBN XXX");
        bookNode.setProperty(EXO_PUBLISHER, "Publisher YYY");
      }

      // set parent path for insert book
      // book.setParentPath(categoryNode.getPath());

      // Do save the session
      session.save();

    } catch (RepositoryException e) {
      e.printStackTrace();
    } finally {
      sessionProvider.close();
    }

    // add Book
    // insert(book);
  }

  /**
   * Gets the sesison.
   * 
   * @return the sesison
   * @throws Exception if Repository or RepositoryConfiguration occur exception
   */
  public Session getSession(SessionProvider sessionProvider) {
    Session session = null;
    RepositoryService repositoryService = (RepositoryService) ExoContainerContext.getCurrentContainer()
                                                                                 .getComponentInstanceOfType(RepositoryService.class);
    try {
      ManageableRepository repository = repositoryService.getCurrentRepository();
      session = sessionProvider.getSession(DEFAULT_WORKSPACE_NAME, repository);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return session;
  }

  @Override
  public void start() {

  }

  @Override
  public void stop() {

  }

}
