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

import org.exoplatform.book.base.BaseTestCase;
import org.exoplatform.bookstore.jcr.model.Book;
import org.exoplatform.bookstore.jcr.model.Category;
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
		super.tearDown();
	}
	
	/** 
	 * Check Bookstore service is available or not
	 * @throws Exception
	 */
	public void testBookstoreService() throws Exception {
		assertNotNull(getBookStoreService());
	}
	
	
	public void testGetAllCategories() throws Exception {
		System.out.println("Test getAllCategories() function ...");
		for(Category category : bookStoreService.getAllCategories()){
			System.out.println("Category id: '"+ category.getId());
			System.out.println("Category label: '"+ category.getLblCategory());
		}
	}
	
	
	public void testInsertBook() throws Exception {
		Book book = new Book();
		book.setCategory(categoryId);
		book.setTitle("Title of Book");
		book.setIsbn("ISBN of Book");
		book.setPublisher("Publisher of Book");
		bookStoreService.insert(book);
		tearDownPollList.add(book);
	}
	
	
	/** Test findAll() */
	public void testFindAll() throws Exception {
		System.out.println("Test findAll() function ...");
		for(Book b : bookStoreService.findAll()){
			System.out.println("Book id: '"+ b.getId());
			System.out.println("Book title: '"+ b.getTitle());
			System.out.println("Book ISBN: '"+ b.getIsbn());
			System.out.println("Book Publisher: '"+ b.getPublisher());
		}
	}

}
