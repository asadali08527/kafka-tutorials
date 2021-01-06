package com.geekglacier.library.repository;

import org.springframework.data.repository.CrudRepository;

import com.geekglacier.library.entity.LibraryEvent;

public interface LibraryEventsRepository extends CrudRepository<LibraryEvent, Integer> {


}
