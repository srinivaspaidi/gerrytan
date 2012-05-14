	package com.worpress.gerrytan.jpasimple.dao;
	
	import java.util.List;
	
	import javax.persistence.EntityManager;
	
	import com.worpress.gerrytan.jpasimple.entity.Book;
	
	/**
	 * Simple implementation of BookDAO using JPA. Each method simply starts and closes
	 * transaction. Entity object returned by these methods are always detached
	 *  
	 * @author gerry
	 *
	 */
	public class BookJPADAOImpl implements BookDAO {
	
		private EntityManager em;
		
		public BookJPADAOImpl (EntityManager entityManager) {
			this.em = entityManager;
		}
		
		@Override
		public Book findById(int bookId) {
			Book result = em.find(Book.class, bookId);
			em.detach(result);
			return result;
		}
	
		@Override
		public List<Book> list() {
			List<Book> result = em.createQuery("SELECT b FROM Book b", Book.class)
					.getResultList();
			for (Book b : result) { em.detach(b); }
			return result;
		}
	
		@Override
		public int save(Book book) {
			em.getTransaction().begin();
			em.persist(book);
			em.getTransaction().commit();
			em.detach(book);
			return book.getBookId();
		}
	
		@Override
		public void update(Book book) {
			em.getTransaction().begin();
			em.merge(book);
			em.getTransaction().commit();
			em.detach(book);
		}
	
		@Override
		public void delete(Book book) {
			em.remove(book);
			em.flush();
		}
	
	}
