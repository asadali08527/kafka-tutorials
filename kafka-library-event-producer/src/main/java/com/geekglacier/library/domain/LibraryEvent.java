package com.geekglacier.library.domain;

import com.geekglacier.library.enums.LibraryEventType;

public class LibraryEvent {

	private Integer libraryEventId;

	private LibraryEventType libraryEventType;

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
		return "LibraryEvent [libraryEventId=" + libraryEventId + ", libraryEventType=" + libraryEventType + ", book="
				+ book + "]";
	}

}
