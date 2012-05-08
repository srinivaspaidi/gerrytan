package com.wordpress.gerrytan.jpasimple.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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


public class BookJPADAOImplTest {
	private BookDAO bookDAO;
	private EntityManagerFactory emf;
	private EntityManager em;
	private static Logger logger = Logger.getLogger(BookJPADAOImplTest.class);
	
	@Before public void setup() {
		emf = Persistence.createEntityManagerFactory("unit-testing-jpu");
		em = emf.createEntityManager();
		bookDAO = new BookJPADAOImpl(em);
	}
	
	@After public void teardown() {
		em.getTransaction().begin();
		em.createNativeQuery("SHUTDOWN").executeUpdate();
		em.getTransaction().commit();
		em.close();
		emf.close();
	}
	
	@Test public void testSaveGetAndDelete() {
		Book book1 = new Book (0, "funny cat", "anonymous", 1992);
		int book1_id = bookDAO.save(book1);
		logger.debug("Saved book entity " + book1_id);
		assertEquals(1, book1.getBookId());
		
		Book book2 = new Book (0, "moo", "foo", 2020);
		int book2_id = bookDAO.save(book2);
		logger.debug("Saved book entity " + book2_id);
		assertEquals(2, book2.getBookId());
		
		Book book1_find = bookDAO.findById(1);
		Book book2_find = bookDAO.findById(2);
		assertEquals(book1, book1_find);
		assertEquals(book2, book2_find);
		assertTrue(book1 == book1_find);
		assertTrue(book2 == book2_find);
		
		bookDAO.delete(book1);
		bookDAO.delete(book2);
		
		book1_find = bookDAO.findById(1);
		book2_find = bookDAO.findById(2);
		assertTrue(book1_find == null);
		assertTrue(book2_find == null);
		
		
	}
	
	@Test public void testSaveUpdateAndDelete() {
		Book book = new Book (0, "book title goes here", "author goes here", 1901);
		bookDAO.save(book);
		assertEquals(1, book.getBookId());
		
		book.setTitle("bang");
		bookDAO.update(book);
		Book retrieved = bookDAO.findById(1);
		assertEquals("bang", retrieved.getTitle());
	}
	
	@Test public void testList() {
		Book b1 = new Book (0, "aaa", "some writer", 1900);
		Book b2 = new Book (0, "bbb", "another writer", 2000);
		bookDAO.save(b1);
		bookDAO.save(b2);
		
		List<Book> books =bookDAO.list();
		assertEquals(2, books.size());
		assertEquals(b1, books.get(0));
		assertEquals(b2, books.get(1));
	}

}
