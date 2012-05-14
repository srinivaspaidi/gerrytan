package com.wordpress.gerrytan.jpasimple.dao;

import static org.junit.Assert.assertEquals;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.worpress.gerrytan.jpasimple.dao.BookDAO;
import com.worpress.gerrytan.jpasimple.dao.BookJPADAOImpl;
import com.worpress.gerrytan.jpasimple.entity.Book;

/**
 * Unit test class for BookJPADAOImpl. setup and teardown method will be executed before and
 * after each test case (test method). See junit doc for more info: 
 * http://junit.sourceforge.net/doc/cookbook/cookbook.htm
 * 
 * @author gerry
 *
 */
public class BookJPADAOImplTest {
	private BookDAO bookDAO;
	private EntityManagerFactory emf;
	private EntityManager em;
	private static Logger logger = Logger.getLogger(BookJPADAOImplTest.class);
	
	/**
	 * Create a new EntityManagerFactory, EntityManager and inject it to the DAO. EntityManager
	 * will create a datbase connection and normally we have to setup the db with schema first,
	 * however we utilize hibernate automatic DDL generation via hbm2ddl property. see:
	 * http://docs.jboss.org/hibernate/orm/4.1/manual/en-US/html/ch03.html#configuration-optional
	 * for more info
	 */
	@Before public void setup() {
		emf = Persistence.createEntityManagerFactory("unit-testing-jpu");
		em = emf.createEntityManager();
		bookDAO = new BookJPADAOImpl(em);
	}
	
	/**
	 * Execute hsql shutdown statement so database is wiped off per test case
	 */
	@After public void teardown() {
		em.getTransaction().begin();
		em.createNativeQuery("SHUTDOWN").executeUpdate();
		em.getTransaction().commit();
		em.close();
		emf.close();
	}
	
	/**
	 * Test creation and persisting entity instance. Notice that when you leave the bookId field
	 * uninitialized persistence context
	 */
	@Test public void testSave() {
		Book b1 = new Book("yipee", "somedude", 1997);
		int b1_id = bookDAO.save(b1);
		assertEquals(b1_id, b1.getBookId());
		
		Book b2 = bookDAO.findById(b1_id);
		assertEquals(b1, b2);
	}
	
	/**
	 * Create a new entity object and test updating it. Notice when we make a change to
	 * a detached entity it won't be persisted into the database
	 */
	@Test public void testUpdate() {
		// Create a new entity object and persist it
		Book b1 = new Book ("yadayada", "coolguy", 2012);
		int b1_id = bookDAO.save(b1);
		assertEquals(b1.getBookId(), b1_id);
		
		// These changes will not be persisted to db since it's detached
		b1.setAuthor("wohoo");
		b1.setTitle("A new title");
		
		Book b2 = bookDAO.findById(b1_id);
		assertEquals("yadayada", b2.getTitle());
		assertEquals("coolguy", b2.getAuthor());
		
		// When we merge the detached entity back into persistence context, the change will
		// be synchronized to database
		bookDAO.update(b2);
		Book b3 = bookDAO.findById(b2.getBookId());
		assertEquals("yadayada", b3.getTitle());
		assertEquals("coolguy", b3.getAuthor());
		
	}
	
	/**
	 * Insert an entity object using native query, and then test our DAO find method
	 */
	@Test public void testFind() {
		em.getTransaction().begin();
		int rowsUpdated = em.createNativeQuery("INSERT INTO Book VALUES (99, 'mamas cookbook', 'lovely mama', 1999)").executeUpdate();
		em.getTransaction().commit();
		assertEquals(1, rowsUpdated);
		
		Book book = bookDAO.findById(99);
		assertEquals(new Book(99, "lovely mama", "mamas cookbook", 1999), book);
	}
	
}
