package com.example.crud_spring.repositories;

import com.example.crud_spring.entities.Donator;
import org.springframework.data.repository.CrudRepository;

public interface DonatorRepository extends CrudRepository<Donator, Long> {
    Donator findDonatorByName(String name);
}
