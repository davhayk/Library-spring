package com.example.crud_spring.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Data
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private boolean active;
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Book> booksWritten = new ArrayList<>();
    private String phoneNumber;

    public String getBooks() {
        if (booksWritten.size() == 0)
            return "";
        StringBuilder s = new StringBuilder();
        for (Book book : booksWritten) {
            s.append(book.getName()).append(", ");
        }
        s.deleteCharAt(s.length() - 1);
        s.deleteCharAt(s.length() - 1);
        return s.toString();
    }
    public String getFullName() {
        if (surname == null || name == null)
            return "";
        return surname + " " + name;
    }
    public int delete(Long bookId) {
        for (int i = 0; i < booksWritten.size(); i++) {
            if (Objects.equals(booksWritten.get(i).getId(), bookId)) {
                booksWritten.remove(i);
                return 1;
            }
        }
        return 0;
    }
    public void addBook(Book book) {
        booksWritten.add(book);
    }
}
