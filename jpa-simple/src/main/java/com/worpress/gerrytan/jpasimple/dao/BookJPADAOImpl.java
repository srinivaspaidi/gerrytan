	package com.worpress.gerrytan.jpasimple.dao;
	
	import java.util.List;
	
	import javax.persistence.EntityManager;
	
	import com.worpress.gerrytan.jpasimple.entity.Book;
	
	public class BookJPADAOImpl implements BookDAO {
	
		private EntityManager entityManager;
		
		public BookJPADAOImpl (EntityManager entityManager) {
			this.entityManager = entityManager;
		}
		
		@Override
		public Book findById(int bookId) {
			entityManager.getTransaction().begin();
			Book result = entityManager.find(Book.class, bookId);
			entityManager.getTransaction().commit();
			return result;
		}
	
		@Override
		public List<Book> list() {
			entityManager.getTransaction().begin();
			List<Book> result = entityManager.createQuery("SELECT b FROM Book b", Book.class)
					.getResultList();
			entityManager.getTransaction().commit();
			return result;
		}
	
		@Override
		public int save(Book book) {
			entityManager.getTransaction().begin();
			entityManager.persist(book);
			entityManager.getTransaction().commit();
			return book.getBookId();
		}
	
		@Override
		public void update(Book book) {
			entityManager.getTransaction().begin();
			entityManager.merge(book);
			entityManager.getTransaction().commit();
		}
	
		@Override
		public void delete(Book book) {
			entityManager.getTransaction().begin();
			entityManager.remove(book);
			entityManager.getTransaction().commit();
		}
	
	}
