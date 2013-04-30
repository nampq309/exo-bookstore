/*
 * Copyright (C) 2003-2012 eXo Platform SAS.
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
package org.exoplatform.book.service;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;

import org.exoplatform.book.base.BaseTestCase;
import org.exoplatform.bookstore.jcr.model.Book;
import org.exoplatform.bookstore.jcr.model.Category;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.JVM)
public class BookServiceTest extends BaseTestCase {

	private List<Book> tearDownPollList;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		tearDownPollList = new ArrayList<Book>();
	}

	@Override
	public void tearDown() throws Exception {
		for(Book b : tearDownPollList){
			System.out.println("Remove book ...");
			bookStoreService.deleteBook(b.getId());
		}
		super.tearDown();
	}
	
	/** 
	 * Check Bookstore service is available or not
	 * @throws Exception
	 */
	public void testBookstoreService() throws Exception {
		assertNotNull(getBookStoreService());
	}
	
	//Test getAllCategories function
	public void testGetAllCategories() throws Exception {
		List<Category> list = bookStoreService.getAllCategories();
		for(Category category : list){
			System.out.println("Category id: '"+ category.getId());
			System.out.println("Category label: '"+ category.getLblCategory());
		}
		assertTrue(list.size()>0);
	}
	
	//Test insert Book function
	public void testInsertBook() throws Exception {
		Book book = new Book();
		book.setCategory(categoryId);
		book.setTitle("Title of Book");
		book.setIsbn("ISBN of Book");
		book.setPublisher("Publisher of Book");
		bookStoreService.insert(book);
		tearDownPollList.add(book);
		assertNotNull(bookStoreService.findById(book.getId()));
	}
	
	//Test update Book function
	public void testUpdateBook() throws Exception {
		//insert Book
		Book book = new Book();
		book.setCategory(categoryId);
		book.setTitle("Title of Book");
		book.setIsbn("ISBN of Book");
		book.setPublisher("Publisher of Book");
		bookStoreService.insert(book);
		
		String bookId = book.getId();
		Book result = bookStoreService.findById(bookId);
		assertEquals(categoryId, result.getCategory());
		
		//update
		book.setCategory(categoryIdUpdate);
		bookStoreService.updateBook(book);
		
		result = bookStoreService.findById(bookId);
		assertEquals(categoryIdUpdate, result.getCategory());
	}
	
	
	/** Test findAll() */
	public void testFindAll() throws Exception {
		List<Book> bookList = bookStoreService.findAll();
		for(Book b : bookList){
			System.out.println("Book id: '"+ b.getId());
			System.out.println("Book title: '"+ b.getTitle());
			System.out.println("Book ISBN: '"+ b.getIsbn());
			System.out.println("Book Publisher: '"+ b.getPublisher());
		}
		assertTrue(bookList.size() > 0);
	}
	
	/**Test deleteBook*/
	public void testDeleteBook() throws Exception {
		Book book = new Book();
		book.setCategory(categoryId);
		book.setTitle("Title of Book");
		book.setIsbn("ISBN of Book");
		book.setPublisher("Publisher of Book");
		bookStoreService.insert(book);
		
		//check insert is success
		assertNotNull(bookStoreService.findById(book.getId()));
		
		//Delete book
		bookStoreService.deleteBook(book.getId());
		
		assertNull(bookStoreService.findById(book.getId()));
	}
	
	public void testRemoveAll() throws Exception {
		SessionProvider sessionProvider = SessionProvider.createSystemProvider();
		Node homeNode = bookStoreService.getCategoriesHome(sessionProvider);
		System.out.println("Home node path '"+ homeNode.getPath());
		System.out.println("Home node name '"+ homeNode.getName());
		
	}

}
