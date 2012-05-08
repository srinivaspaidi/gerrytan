package com.worpress.gerrytan.jpasimple.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.worpress.gerrytan.jpasimple.entity.Book;

/**
 * Implementation of BookDAO using jdbc, creates and close a new connection for every
 * CRUD method calls. Use connection pooled datasource for efficiency.
 * 
 * @author gerry
 *
 */
public class BookJdbcDAOImpl implements BookDAO {

	private DataSource dataSource;
	
	public BookJdbcDAOImpl (DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public Book findById(int bookId) {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM book WHERE book_id = " + bookId);
			if (!rs.next()) { 
				return null;
			}
			Book result = mapRow(rs);
			statement.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
		
	}

	@Override
	public List<Book> list() {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM book");
			ArrayList<Book> result = new ArrayList<Book>();
			while (rs.next()) {
				result.add(mapRow(rs));
			}
			statement.close();
			return result;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
	}
	
	@Override
	public int save(Book book) {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			Statement statement = connection.createStatement();
			statement.executeUpdate(
				String.format("INSERT INTO book (title, author, year) VALUES ('%s','%s',%s)",
					book.getTitle(),
					book.getAuthor(),
					book.getYear()), 
				Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = statement.getGeneratedKeys();
			if (!rs.next()) { 
				throw new IllegalStateException("Error when inserting book to database " + book);
			}
			int generatedKey = rs.getInt(1);
			book.setBookId(generatedKey);
			statement.close();
			return generatedKey;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
	}

	@Override
	public void update(Book book) {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			Statement statement = connection.createStatement();
			int rowUpdated = statement.executeUpdate(
					String.format(
						"UPDATE book " +
						"SET title = '%s', " +
						"author = '%s', " +
						"year = %s " +
						"WHERE book_id = %s", 
						book.getTitle(),
						book.getAuthor(),
						book.getYear(),
						book.getBookId()));
			if (rowUpdated != 1) {
				throw new IllegalStateException("Error when trying to update " + book);
			}
			statement.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}

	}

	@Override
	public void delete(Book book) {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			Statement statement = connection.createStatement();
			int rowUpdated = statement.executeUpdate(
					String.format(
						"DELETE FROM book WHERE book_id = %s", 
						book.getBookId()));
			if (rowUpdated != 1) {
				throw new IllegalStateException("Error when trying to delete " + book);
			}
			statement.close();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}

	}

	private Book mapRow(ResultSet rs) throws SQLException {
		return new Book (
			rs.getInt("book_id"),
			rs.getString("title"),
			rs.getString("author"),
			rs.getInt("year"));
	}

}

