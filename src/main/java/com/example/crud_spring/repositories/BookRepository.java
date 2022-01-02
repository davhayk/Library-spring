package com.example.crud_spring.repositories;

import com.example.crud_spring.entities.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {
    Book findBookByName(String name);
}
