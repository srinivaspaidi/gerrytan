package com.worpress.gerrytan.jpasimple.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Book {
	@Id @GeneratedValue private int bookId;
	@Column private String title;
	@Column private String author;
	@Column private int year;
	
	/**
	 * Default constructor without parameter is required for a class to be treated as JPA
	 * entity
	 */
	public Book() {}
	
	/**
	 * This constructor is typically used to instantiate a new entity object. When an entity
	 * object just got created it doesn't yet have an identity, hence the id field is left at
	 * its default value (0 for integer)
	 */
	public Book(String title, String author, int year) {
		this(0, title, author, year);
	}
	
	/**
	 * This constructor is typically used when mapping resultset row when using JDBC
	 */
	public Book(int bookId, String title, String author, int year) {
		this.bookId = bookId;
		this.title = title;
		this.author = author;
		this.year = year;
	}
	
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String name) {
		this.title = name;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + bookId;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + year;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Book other = (Book) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (bookId != other.bookId)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (year != other.year)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Book [bookId=" + bookId + ", title=" + title + ", author="
				+ author + ", year=" + year + "]";
	}
}
