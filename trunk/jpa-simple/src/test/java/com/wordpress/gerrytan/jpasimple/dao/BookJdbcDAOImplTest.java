package com.wordpress.gerrytan.jpasimple.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.log4j.Logger;
import org.hsqldb.cmdline.SqlFile;
import org.hsqldb.cmdline.SqlToolError;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.worpress.gerrytan.jpasimple.dao.BookDAO;
import com.worpress.gerrytan.jpasimple.dao.BookJdbcDAOImpl;
import com.worpress.gerrytan.jpasimple.entity.Book;

public class BookJdbcDAOImplTest {
	private ComboPooledDataSource dataSource;
	private BookDAO bookDAO;
	private Logger logger = Logger.getLogger(BookJPADAOImplTest.class);
	
	@Before public void setup() throws IOException, SqlToolError, SQLException, PropertyVetoException {
		dataSource = new ComboPooledDataSource();
		dataSource.setDriverClass("org.hsqldb.jdbc.JDBCDriver"); //TODO
		dataSource.setJdbcUrl("jdbc:hsqldb:mem:jdbchsqlmemdb");
		dataSource.setUser("SA");
		dataSource.setPassword("");
		
		bookDAO = new BookJdbcDAOImpl(dataSource);
		
		// Setup database by running setup sql script located on the classpath
		// (src/test/resources/setup-test-db.sql
		File initSql = new ClassPathResource("setup-test-db.sql").getFile();
		SqlFile sqlFile = new SqlFile(initSql);
		Connection connection = dataSource.getConnection();
		sqlFile.setConnection(connection);		
		sqlFile.execute();
		connection.close();
	}
	
	@After public void teardown() throws SQLException {
		Connection c = dataSource.getConnection();
		Statement s = c.createStatement();
		s.executeUpdate("SHUTDOWN");
		c.close();
	}
	
	@Test public void testCreateAndReadBook() {
		Book book = new Book (0, "gerry", "eating for dummies", 1996);
		int newBookId = bookDAO.save(book);
		logger.info("Persisted book " + book);
		assertTrue(newBookId == book.getBookId());
		assertEquals(0, book.getBookId());
		
		Book book2 = bookDAO.findById(0);
		assertEquals(book, book2);
	}
	
	@Test public void testList() {
		Book a = new Book(0, "shakespeare", "hamlet", 1603);
		Book b = new Book(0, "someguy", "coolbook", 2000);
		
		bookDAO.save(a);
		bookDAO.save(b);
		
		List<Book> books = bookDAO.list();
		logger.debug("listed books " + books);
		
		// The book object obtained from DAO is a new instance, although satisfying 'equal'
		// condition
		assertEquals(2, books.size());
		assertTrue(books.get(0) != a);
		assertTrue(books.get(1) != b);
		assertTrue(books.get(0).equals(a));
		assertTrue(books.get(1).equals(b));
	}
	
	@Test public void testUpdate() {
		Book book = new Book (0, "jcitizen", "good man", 1980);
		bookDAO.save(book);
		logger.debug("Saved book instance " + book);
		
		Book retrieved = bookDAO.findById(book.getBookId());
		assertTrue(book != retrieved);
		assertTrue(book.equals(retrieved));
		
		book.setYear(2010);
		bookDAO.update(book);
		retrieved = bookDAO.findById(book.getBookId());
		assertTrue(book != retrieved);
		assertTrue(book.equals(retrieved));
		
	}
	
	@Test public void testDelete() {
		Book book = new Book (0, "jcitizen", "good man", 1980);
		bookDAO.save(book);
		bookDAO.delete(book);
		Book retrieved = bookDAO.findById(book.getBookId());
		assertEquals(null, retrieved);
	}
}


