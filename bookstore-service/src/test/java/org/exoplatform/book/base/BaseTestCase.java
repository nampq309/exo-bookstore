package org.exoplatform.book.base;

import javax.jcr.Node;
import javax.jcr.Session;

import org.apache.log4j.Logger;
import org.exoplatform.bookstore.service.api.BookStoreService;
import org.exoplatform.bookstore.service.api.BookstoreNodeTypes;
import org.exoplatform.bookstore.service.impl.BookStoreServiceImpl;
import org.exoplatform.commons.testing.BaseExoTestCase;
import org.exoplatform.component.test.ConfigurationUnit;
import org.exoplatform.component.test.ConfiguredBy;
import org.exoplatform.component.test.ContainerScope;
import org.exoplatform.services.jcr.ext.common.SessionProvider;

@ConfiguredBy({
		@ConfigurationUnit(scope = ContainerScope.PORTAL, path = "conf/exo.portal.component.portal-configuration.xml"),
		@ConfigurationUnit(scope = ContainerScope.PORTAL, path = "conf/exo.portal.component.test.jcr-configuration.xml"),
		@ConfigurationUnit(scope = ContainerScope.PORTAL, path = "conf/exo.portal.component.identity-configuration.xml"),
		@ConfigurationUnit(scope = ContainerScope.PORTAL, path = "conf/standalone/component.core.test.configuration.xml"),
		@ConfigurationUnit(scope = ContainerScope.PORTAL, path = "conf/standalone/component.service.test.configuration.xml"),
		@ConfigurationUnit(scope = ContainerScope.PORTAL, path = "conf/standalone/test.jcr-configuration.xml"),
		@ConfigurationUnit(scope = ContainerScope.PORTAL, path = "conf/standalone/test.portal-configuration.xml") })
public abstract class BaseTestCase extends BaseExoTestCase {
	
	protected BookStoreService bookStoreService;
	
	protected String categoryId = "categoryIdOftest";
	
	protected String categoryIdUpdate = "categoryIdUpdate";
	
	Logger logger = Logger.getLogger(BaseTestCase.class);

	@Override
	public void setUp() throws Exception {
		begin();
		bookStoreService = (BookStoreService) getService(BookStoreService.class);
		intialBookstoreData();
	}

	@Override
	public void tearDown() throws Exception {
		//clear data
		removeCategories();
		end();
	}

	@SuppressWarnings("unchecked")
	public <T> T getService(Class<T> clazz) {
		return (T) getContainer().getComponentInstanceOfType(clazz);
	}

	public BookStoreService getBookStoreService() {
		return bookStoreService;
	}
	
	/**
	 * Initial Bookstore data
	 */
	private void intialBookstoreData() {
		SessionProvider sessionProvider = SessionProvider.createSystemProvider();
		// Initial structure of Nodes
		try {
			Session session = getSession(sessionProvider);
			Node categoriesNode = bookStoreService.getCategoriesHome(sessionProvider);
			if(!categoriesNode.hasNode(categoryId)) {
				logger.info("Initial Bookstore Tree ...");
				// Add first category to repository
				Node categoryNode = categoriesNode.addNode(categoryId, BookstoreNodeTypes.EXO_CATEGORY);
				categoryNode.setProperty(BookstoreNodeTypes.EXO_ID, categoryId);
				categoryNode.setProperty(BookstoreNodeTypes.EXO_LBL_CATEGORY, "Novel");
				logger.info("categoryNode Node: '" + categoryNode.getPath());
				logger.info("categoryNode Name: '" + categoryNode.getName());
			} else if (!categoriesNode.hasNode(categoryIdUpdate)) {
				//second Category
				Node categoryNode = categoriesNode.addNode(categoryIdUpdate, BookstoreNodeTypes.EXO_CATEGORY);
				categoryNode.setProperty(BookstoreNodeTypes.EXO_ID, categoryIdUpdate);
				categoryNode.setProperty(BookstoreNodeTypes.EXO_LBL_CATEGORY, "Story");
				logger.info("categoryNode Node: '" + categoryNode.getPath());
				logger.info("categoryNode Name: '" + categoryNode.getName());
			}
			// Do save the session
			session.save();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sessionProvider.close();
		}
	}
	
	private void removeCategories() throws Exception {
		SessionProvider sessionProvider = SessionProvider.createSystemProvider();
		try {
			logger.info("Clear data ...");
			Node homeNode = bookStoreService.getCategoriesHome(sessionProvider);
			if(homeNode.hasNode(categoryId)){
				homeNode.getNode(categoryId).remove();
				homeNode.getNode(categoryIdUpdate).remove();
			}
			if(homeNode.isNew()){
				homeNode.getSession().save();
			} else {
				homeNode.save();
			}
		} catch (Exception e) {
		} finally {
			sessionProvider.close();
		}
	}
	
	/**
	 * Gets the sesison.
	 * @return the sesison
	 */
	public Session getSession(SessionProvider sessionProvider) {
		return bookStoreService.getSession(sessionProvider);
	}

}
