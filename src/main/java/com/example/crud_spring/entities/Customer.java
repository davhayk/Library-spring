package com.example.crud_spring.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<Book> booksTaken = new ArrayList<>();
    private boolean hasToReturn = true;
    private String phoneNumber;
    private String email;
}
