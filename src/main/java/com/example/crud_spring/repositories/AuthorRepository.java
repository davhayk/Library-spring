package com.example.crud_spring.repositories;

import com.example.crud_spring.entities.Author;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, Long> {
    Author findAuthorByName(String name);
}
