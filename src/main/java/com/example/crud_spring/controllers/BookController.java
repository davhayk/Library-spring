package com.example.crud_spring.controllers;

import com.example.crud_spring.entities.Author;
import com.example.crud_spring.entities.Book;
import com.example.crud_spring.entities.Customer;
import com.example.crud_spring.entities.Donator;
import com.example.crud_spring.repositories.AuthorRepository;
import com.example.crud_spring.repositories.BookRepository;
import com.example.crud_spring.repositories.CustomerRepository;
import com.example.crud_spring.repositories.DonatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Controller
public class BookController {
    @Autowired
    private BookRepository repository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private DonatorRepository donatorRepository;
    @Autowired
    private CustomerRepository customerRepository;


    @GetMapping("/")
    public String getBooks(Model model) {
        Iterable<Book> books = repository.findAll();
        model.addAttribute("books", books);
        return "index";
    }


    @GetMapping("/edit")
    public String getEditPage(Model model, HttpServletRequest request) {
        Long bookId = Long.parseLong(request.getParameter("bookId"));
        model.addAttribute("book", repository.findById(bookId));
        return "edit";
    }


    @GetMapping("/delete")
    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long bookId = Long.parseLong(request.getParameter("bookId"));
        Book book = repository.findById(bookId).orElse(null);
        Author author = authorRepository.findById(repository.findById(bookId).get().getAuthor().getId()).orElse(null);
        for (int i = 0; i < book.getDonators().size(); i++) {
            Donator donator = donatorRepository.findById(book.getDonators().get(i).getId()).orElse(null);
            donator.getBooksDonated().remove(book);
        }
        author.getBooksWritten().remove(book);
        repository.deleteById(bookId);
        response.sendRedirect("/");
    }


    @GetMapping("/editandredirect")
    public void edit(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long bookId = Long.parseLong(request.getParameter("bookId"));
        Book book = repository.findById(bookId).orElse(null);
        String name = request.getParameter("name");

        String[] props = request.getParameter("proportion").split("/");
        int countInPlace = Integer.parseInt(props[0]);
        int countOverall = Integer.parseInt(props[1]);

        String pdf = request.getParameter("inPDF");
        boolean isPdf = pdf != null;
        String written = request.getParameter("inWrittenForm");
        boolean isWritten = written != null;
        String readonly = request.getParameter("isReadOnly");
        boolean isReadOnly = readonly != null;
        assert book != null;
        // Give values
        book.setName(name);
        book.setCountInPlace(countInPlace);
        book.setCountOverall(countOverall);
        book.setInStock(book.getCountInPlace() != 0);
        book.setInPDF(isPdf);
        book.setInWrittenForm(isWritten);
        book.setReadOnly(isReadOnly);

        repository.save(book);
        response.sendRedirect("/");
    }


    @GetMapping("/addForm")
    public String getAddForm() {
        return "addForm";
    }


    @GetMapping("/add")
    public void add(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String bookName = request.getParameter("name");
        Book book = repository.findBookByName(bookName);
        Long donatorId;
        Donator donator;
        Author author;

        if (request.getParameter("donatorId") != "") {
            donatorId = Long.parseLong(request.getParameter("donatorId"));
            donator = donatorRepository.findById(donatorId).orElse(null);
        } else {
            donator = new Donator();
            donatorRepository.save(donator);
            donatorId = donator.getId();
            System.out.println(donatorId);
        }

        if (request.getParameter("authorId") != "") {
            Long authorId;
            authorId = Long.parseLong(request.getParameter("authorId"));
            author = authorRepository.findById(authorId).orElse(null);
        } else {
            author = new Author();
            authorRepository.save(author);
        }


        if (book != null) {
            System.out.println("IS NOT NULL");
            book.setCountOverall(book.getCountOverall() + Integer.parseInt(request.getParameter("count")));
            book.setCountInPlace(book.getCountInPlace() + Integer.parseInt(request.getParameter("count")));
            book.setInWrittenForm(Objects.equals(request.getParameter("inWrittenForm"), "on"));
            book.setInPDF(Objects.equals(request.getParameter("inPDF"), "on"));
            book.setInStock(true);
            if (!donator.getBooksDonated().contains(book)) {
                donator.getBooksDonated().add(book);
            }
            if (!book.getDonators().contains(donator)) {
                book.getDonators().add(donator);
            }
        } else {
            book = new Book();
            book.setName(bookName);
            if (author.getName() == null) {
                author.setName(request.getParameter("authorName"));
                author.setSurname(request.getParameter("authorSurname"));
            }
            book.setAuthor(author);
            if (!book.getDonators().contains(donator)) {
                book.getDonators().add(donator);
            }
            book.setCountOverall(Integer.parseInt(request.getParameter("count")));
            book.setCountInPlace(Integer.parseInt(request.getParameter("count")));
            book.setInStock(true);
            book.setInWrittenForm(Objects.equals(request.getParameter("inWrittenForm"), "on"));
            book.setInPDF(Objects.equals(request.getParameter("inPDF"), "on"));
            book.setReadOnly(Objects.equals(request.getParameter("readOnly"), "on"));
            author.addBook(book);
            donator.getBooksDonated().add(book);
        }
        if (donator.getName() == null) {
            donator.setName(request.getParameter("donatorName"));
            donator.setSurname(request.getParameter("donatorSurname"));
            donator.setPhoneNumber(request.getParameter("donatorNumber"));
        }
        repository.save(book);
        response.sendRedirect("/");
    }


    @GetMapping("/searchByBookId")
    public String searchBooksByID(Model model) {
        Iterable<Book> books = repository.findAll();
        model.addAttribute("books", books);
        return "index";
    }


    @GetMapping("/certainBookDonator")
    public String getDonators(HttpServletRequest request, Model model) {
        Long bookId = Long.parseLong(request.getParameter("bookId"));
        Book book = repository.findById(bookId).orElse(null);
        Iterable<Donator> donators;
        assert book != null;
        donators = book.getDonators();
        model.addAttribute("donators", donators);
        return "certainBookDonator";
    }

}