package com.example.crud_spring.controllers;

import com.example.crud_spring.entities.Author;
import com.example.crud_spring.repositories.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Controller
public class AuthorController {
    @Autowired
    private AuthorRepository repository;

    @GetMapping("/authors")
    public String getAuthors(Model model) {
        Iterable<Author> authors = repository.findAll();
        model.addAttribute("authors", authors);
        return "authors";
    }



    @GetMapping("/booksWritten")
    public String showBooksWritten(Model model, HttpServletRequest request) {
        Long authorId = Long.parseLong(request.getParameter("authorId"));
        Optional<Author> author = repository.findById(authorId);
        model.addAttribute("author", author);
        return "booksWritten";
    }



    @GetMapping("/editAuthor{authorId}")
    public String editAuthor(Model model, @PathVariable("authorId") Long authorId) {
        Author author = repository.findById(authorId).orElse(null);
        model.addAttribute("author", author);
        return "editAuthor";
    }



    @GetMapping("/editAuthorAndRespond")
    public void editAuthorAndRespond(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Author author = repository.findById(Long.parseLong(request.getParameter("authorId"))).orElse(null);
        author.setName(request.getParameter("name"));
        author.setSurname(request.getParameter("surname"));
        author.setPhoneNumber(request.getParameter(("phoneNumber")));
        author.setActive(request.getParameter("active") != null);
        repository.save(author);
        response.sendRedirect("/authors");
    }
}
