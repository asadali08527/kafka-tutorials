package com.geekglacier.library.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.geekglacier.library.enums.LibraryEventType;

@Entity
public class LibraryEvent {

	@Id
	private Integer libraryEventId;

	@Enumerated(EnumType.STRING)
	private LibraryEventType libraryEventType;

	@OneToOne(mappedBy = "libraryEvent", cascade = { CascadeType.ALL })
	private Book book;

	public LibraryEvent() {
	}

	public LibraryEvent(Integer libraryEventId, Book book) {
		super();
		this.libraryEventId = libraryEventId;
		this.book = book;
	}

	public Integer getLibraryEventId() {
		return libraryEventId;
	}

	public void setLibraryEventId(Integer libraryEventId) {
		this.libraryEventId = libraryEventId;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public LibraryEventType getLibraryEventType() {
		return libraryEventType;
	}

	public void setLibraryEventType(LibraryEventType libraryEventType) {
		this.libraryEventType = libraryEventType;
	}

	@Override
	public String toString() {
		return "LibraryEvent [libraryEventId=" + libraryEventId + ", libraryEventType=" + libraryEventType + "]";
	}

}
