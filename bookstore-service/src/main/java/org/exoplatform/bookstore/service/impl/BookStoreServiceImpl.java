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
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.exoplatform.bookstore.jcr.model.Book;
import org.exoplatform.bookstore.jcr.model.Category;
import org.exoplatform.bookstore.service.api.BookStoreService;
import org.exoplatform.bookstore.service.api.BookstoreNodeTypes;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.core.ManageableRepository;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.jcr.impl.core.query.QueryImpl;
import org.picocontainer.Startable;

public class BookStoreServiceImpl implements BookStoreService, Startable, BookstoreNodeTypes {

  //
  public static final String DEFAULT_WORKSPACE_NAME = "portal-system";
  public static final String WORKSPACE_PARAM = "workspace";

  public static final String DEFAULT_CATEGORY_ID = CATEGORY + "defaultCategory";

  //workspace
  public String workspace;

  public BookStoreServiceImpl(InitParams params) {
    workspace = getParam(params, WORKSPACE_PARAM, DEFAULT_WORKSPACE_NAME);
  }

	@Override
	public Book insertBook(Book book) {
	  SessionProvider sessionProvider = SessionProvider.createSystemProvider();
	  Node bookNode = null;
	  try {
	    Node parentNode = getCategoryById(sessionProvider, book.getCategory());
	    bookNode = parentNode.addNode(book.getId(), EXO_BOOK);
	    bookNode.setProperty(EXO_TITLE, book.getTitle());
	    bookNode.setProperty(EXO_ISBN, book.getIsbn());
	    bookNode.setProperty(EXO_PUBLISHER, book.getPublisher());
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
	  SessionProvider sProvider = SessionProvider.createSystemProvider();
	  Book book = null;
	  try {
	    Node bookNode = getNodeById(sProvider, id, EXO_BOOK);
	    Node parent = bookNode.getParent();
	    book = new Book();
	    book.setId(bookNode.getName());
	    book.setParentPath(parent.getPath());
	    book.setCategory(parent.getName());
	    book.setLblCategory(getProperty(parent, EXO_LBL_CATEGORY, ""));
	    book.setTitle(getProperty(bookNode, EXO_TITLE, ""));
	    book.setIsbn(getProperty(bookNode, EXO_ISBN, ""));
	    book.setPublisher(getProperty(bookNode, EXO_PUBLISHER, ""));
	  } catch (Exception e) {
	    book = null;
	  } finally {
	    sProvider.close();
	  }
	  return book;
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
	    String strQuery = "//element(*, " + EXO_BOOK + ") [jcr:like(@"+ EXO_TITLE + ",'%" + title + "%')]";
	    QueryManager qm = getSession(sessionProvider).getWorkspace().getQueryManager();
	    Query query = qm.createQuery(strQuery, Query.XPATH);
	    QueryImpl queryImpl = (QueryImpl) query;
	    NodeIterator nodeItr = queryImpl.execute().getNodes();
	    // read book to list
	    while (nodeItr.hasNext()) {
	      Node bookNode = nodeItr.nextNode();
	      Node parent = bookNode.getParent();
	      Book book = new Book();
	      book.setId(bookNode.getName());
	      book.setParentPath(parent.getPath());
	      book.setCategory(parent.getName());
	      book.setLblCategory(getProperty(parent, EXO_LBL_CATEGORY, ""));
	      book.setTitle(getProperty(bookNode, EXO_TITLE, ""));
	      book.setIsbn(getProperty(bookNode, EXO_ISBN, ""));
	      book.setPublisher(getProperty(bookNode, EXO_PUBLISHER, ""));
	      list.add(book);
	    }

	  } catch (Exception e) {
	    e.printStackTrace();
	  } finally {
	    sessionProvider.close();
	  }

	  return list;
	}

	@Override
	public List<Book> findByPublisher(String publisher) {
		return null;
	}

	@Override
	public List<Book> findAll() {
		SessionProvider sessionProvider = SessionProvider.createSystemProvider();
		List<Book> list = new ArrayList<Book>();
		try {
			// Get all of Books by 'SELECT *' query
			String strQuery = "//element(*, " + EXO_BOOK + ")";
			QueryManager qm = getSession(sessionProvider).getWorkspace().getQueryManager();
			Query query = qm.createQuery(strQuery, Query.XPATH);
			QueryImpl queryImpl = (QueryImpl) query;
			NodeIterator nodeItr = queryImpl.execute().getNodes();
			// read book to list
			while (nodeItr.hasNext()) {
				Node bookNode = nodeItr.nextNode();
				Node parentNode = bookNode.getParent();
				String bId = bookNode.getName();
				//Set Book's informations
				Book book = new Book();
				book.setId(bId);
				book.setParentPath(parentNode.getPath());
				book.setCategory(parentNode.getName());
				book.setLblCategory(getProperty(parentNode, EXO_LBL_CATEGORY, ""));
				book.setTitle(getProperty(bookNode, EXO_TITLE, ""));
				book.setIsbn(getProperty(bookNode, EXO_ISBN, ""));
				book.setPublisher(getProperty(bookNode, EXO_PUBLISHER, ""));
				list.add(book);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sessionProvider.close();
		}
		return list;
	}

	@Override
	public void updateBook(Book book) {
		SessionProvider sProvider = SessionProvider.createSystemProvider();
		try {
			Node bookNode = getNodeById(sProvider, book.getId(), EXO_BOOK);
			Session session = getSession(sProvider);
			//Check if Category is changed
			String curParent = bookNode.getParent().getName();
			if(!book.getCategory().equals(curParent)) {
				// move Book to new location on tree
				Node parentNode = getNodeById(sProvider, book.getCategory(), EXO_CATEGORY);
				System.out.println("[updateBook] Current path: "+bookNode.getPath());
				System.out.println("[updateBook] new Parent path: "+ parentNode.getPath());
				session.move(bookNode.getPath(), parentNode.getPath() + "/" + book.getId());
				bookNode = parentNode.getNode(book.getId());
			}
			bookNode.setProperty(EXO_TITLE, book.getTitle());
			bookNode.setProperty(EXO_ISBN, book.getIsbn());
			bookNode.setProperty(EXO_PUBLISHER, book.getPublisher());
			// Do save into repository
			session.save();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[updateBook] Update Book fail !!!");
		} finally {
			sProvider.close();
		}
	}

	@Override
	public void deleteBook(String id) {
		SessionProvider sProvider = SessionProvider.createSystemProvider();
		try {
			Node bookNode = getNodeById(sProvider, id, EXO_BOOK);
			Node parentNode = bookNode.getParent();
			bookNode.remove();
			if(parentNode.isNew()){
				parentNode.getSession().save();
			}else {
				parentNode.save();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Remove Book fail !!!");
		} finally {
			sProvider.close();
		}
	}

	@Override
	public void deleteAll() {
		SessionProvider sProvider = SessionProvider.createSystemProvider();
		try {
			Node categoriesHomeNode = getCategoriesHome(sProvider);
			Session session = categoriesHomeNode.getSession();
			categoriesHomeNode.remove();
			session.save();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			sProvider.close();
		}
	}

	@Override
	public boolean isExists(String isbn) {
		return false;
	}

	@Override
	public List<Category> getAllCategories() {
	  SessionProvider sessionProvider = SessionProvider.createSystemProvider();

	  List<Category> list = new ArrayList<Category>();
	  try {
	    //SELECT all of Category
	    String strQuery = "//element(*, " + EXO_CATEGORY + ")";

	    QueryManager qm = getSession(sessionProvider).getWorkspace().getQueryManager();
	    Query query = qm.createQuery(strQuery, Query.XPATH);
	    QueryImpl queryImpl = (QueryImpl) query;
	    NodeIterator nodeItr = queryImpl.execute().getNodes();

	    // Read category to list
	    while (nodeItr.hasNext()) {
	      Node cateNode = nodeItr.nextNode();
	      Category cate = new Category(getProperty(cateNode, EXO_LBL_CATEGORY, ""));
	      cate.setId(cateNode.getName());
	      System.out.println("Add Category '" + cate.getLblCategory() + "to list");
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
	    Session session = getSession(sessionProvider);
	    Node rootNode = session.getRootNode();
	    Node categoriesNode = null;
	    if (!rootNode.hasNode(BOOKSTORE)) {
	      System.out.println("Initial Bookstore Tree ...");
	      Node bookStoreNode = rootNode.addNode(BOOKSTORE, EXO_BOOKSTORE);
	      System.out.println("Bookstore Node: '" + bookStoreNode.getPath());
	      System.out.println("Bookstore Name: '" + bookStoreNode.getName());

	      categoriesNode = bookStoreNode.addNode(CATEGORIES, EXO_CATEGORIES);
	      System.out.println("categoriesNode Node: '" + categoriesNode.getPath());
	      System.out.println("categoriesNode Name: '" + categoriesNode.getName());

	    } else {
	      categoriesNode = getCategoriesHome(sessionProvider);
	    }

	    if (!categoriesNode.hasNode(DEFAULT_CATEGORY_ID)) {
	      // Add the default Category to Categories Home
	      Category cate = new Category("Novel");
	      Node categoryNode = categoriesNode.addNode(DEFAULT_CATEGORY_ID, EXO_CATEGORY);
	      System.out.println("categoryNode Node: '" + categoryNode.getPath());
	      System.out.println("categoryNode Name: '" + categoryNode.getName());
	      categoryNode.setProperty(EXO_ID, cate.getId());
	      categoryNode.setProperty(EXO_LBL_CATEGORY, cate.getLblCategory());
	    }
	    // Do save the session
	    session.save();

	  } catch (RepositoryException e) {
	    e.printStackTrace();
	  } catch (Exception e) {
	    e.printStackTrace();
	  } finally {
	    sessionProvider.close();
	  }
	}

	/**
	 * Gets the sesison.
	 * 
	 * @return the sesison
	 * @throws Exception
	 *             if Repository or RepositoryConfiguration occur exception
	 */
	public Session getSession(SessionProvider sessionProvider) {
	  Session session = null;
	  RepositoryService repositoryService = (RepositoryService) ExoContainerContext.getCurrentContainer().getComponentInstanceOfType(RepositoryService.class);
	  try {
	    ManageableRepository repository = repositoryService.getCurrentRepository();
	    session = sessionProvider.getSession(getWorkspace(), repository);
	  } catch (Exception e) {
	    throw new RuntimeException(e);
	  }
	  return session;
	}

	@Override
	public void start() {
	  System.out.println("initializing Bookstore service...");
	  initialNodes();
	}

	@Override
	public void stop() {

	}

	/**
	 * Get parameter from InitParams set
	 * @param params
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	private String getParam(InitParams params, String name, String defaultValue) {
	  String result = null;
	  try {
	    result = params.getValueParam(name).getValue();
	  } catch (Exception e) {
	    System.out.println("No '" + name + "' value-param. Using default value: " + defaultValue);
	  }

	  if (result == null) {
	    result = defaultValue;
	  }
	  return result;
	}

	public String getWorkspace() {
		return workspace != null ? workspace : DEFAULT_WORKSPACE_NAME;
	}
	
	private Node getCategoryById(SessionProvider sProvider, String categoryId) throws Exception{
		return (Node) getSession(sProvider).getItem("/"+ BOOKSTORE + "/"+ CATEGORIES +"/"+ categoryId);
	}
	
	
	public String getProperty(Node node, String name, String defaultValue) {
		String value = null;
		try {
			value = node.getProperty(name).getString();
		} catch (Exception e) {
			value = defaultValue;
		}
		return value;
	}

	/**
	 * Get any Node by its id
	 * @param sessionProvider
	 * @param id
	 * @param nodeType
	 * @return node
	 * @throws Exception
	 */
	public Node getNodeById(SessionProvider sessionProvider, String id, String nodeType) throws Exception{
		QueryManager qm = getSession(sessionProvider).getWorkspace().getQueryManager();
		StringBuffer queryString = new StringBuffer(EXO_JCR_ROOT);
		queryString.append("//element(*,").append(nodeType).append(")").append("[fn:name() = '").append(id).append("']");
		Query query = qm.createQuery(queryString.toString(), Query.XPATH);
		QueryResult result = query.execute();
	    NodeIterator iter = result.getNodes();
	    if (iter.getSize() > 0)
	      return iter.nextNode();
	    return null;
	}
	
	/**
	 * Get the Categories home node
	 * @return node
	 */
	@Override
	public Node getCategoriesHome(SessionProvider sessionProvider) throws Exception{
		Node rootNode = getSession(sessionProvider).getRootNode();
		Node categoriesHome = null;
		if(rootNode.hasNode(BOOKSTORE)){
			categoriesHome = rootNode.getNode(BOOKSTORE).getNode(CATEGORIES);
		}
		return categoriesHome;
	}

	@Override
	public Category insertCategory(Category category) {
		SessionProvider sessionProvider = SessionProvider.createSystemProvider();
		Node categoryNode = null;
		try {
			Node parentNode = getCategoriesHome(sessionProvider);
			categoryNode = parentNode.addNode(category.getId(), EXO_CATEGORY);
			categoryNode.setProperty(EXO_LBL_CATEGORY, category.getLblCategory());
			// Do save into repository
			if(parentNode.isNew())
				parentNode.getSession().save();
			else
				parentNode.save();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sessionProvider.close();
		}
		return category;
	}

}
