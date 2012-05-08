package com.worpress.gerrytan.jpasimple.dao;

import java.util.List;

import com.worpress.gerrytan.jpasimple.entity.Book;

/**
 * Data Access Object interface to perform CRUD operation over Book entity
 * 
 * @author gerry
 *
 */
public interface BookDAO {
	/**
	 * Find a book by id
	 * @return null if can't find bookId
	 */
	public Book findById(int bookId);
	
	/**
	 * List all books
	 */
	public List<Book> list();
	
	/**
	 * Persist a new book object into database. The id attribute of the book object will be set 
	 * and returned.
	 * @return id of the newly inserted book
	 */
	public int save(Book book);
	
	/**
	 * Persist changes to existing book object into database.
	 * @param book
	 */
	public void update(Book book);
	
	/**
	 * Remove persisted book object from database
	 * @param book
	 */
	public void delete(Book book);
}
