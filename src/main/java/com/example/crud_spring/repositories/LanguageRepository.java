package com.example.crud_spring.repositories;

import com.example.crud_spring.entities.Language;
import org.springframework.data.repository.CrudRepository;

public interface LanguageRepository extends CrudRepository<Language, Integer> {
}
