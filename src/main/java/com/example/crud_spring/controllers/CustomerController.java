package com.example.crud_spring.controllers;

import com.example.crud_spring.entities.Book;
import com.example.crud_spring.entities.Customer;
import com.example.crud_spring.repositories.BookRepository;
import com.example.crud_spring.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class CustomerController {
    @Autowired
    BookRepository repository;
    @Autowired
    CustomerRepository customerRepository;


    @GetMapping("/customers")
    public String getCustomers(Model model) {
        Iterable<Customer> customers = customerRepository.findAll();
        model.addAttribute("customers", customers);
        return "customers";
    }


    @RequestMapping("/take{bookId}")
    public String take(Model model, @PathVariable("bookId") Long bookId, HttpServletResponse response) throws IOException {
        model.addAttribute("bookId", bookId);
        return "take";
    }


    @GetMapping("/takeAndReturnToMainPage")
    public void takeAndReturnToMainPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Customer customer = null;
        Book book = repository.findById(Long.parseLong(request.getParameter("bookId"))).orElse(null);
        if (request.getParameter("customerId") != "") {
            customer = customerRepository.findById(Long.parseLong(request.getParameter("customerId"))).orElse(null);
        }
        if (customer == null) {
            customer = new Customer();
            customer.setName(request.getParameter("customerName"));
            customer.setSurname(request.getParameter("customerSurname"));
            customer.setPhoneNumber(request.getParameter("customerNumber"));
            customer.setEmail(request.getParameter("customerEmail"));
        }
        book.setCountInPlace(book.getCountInPlace() - 1);
        if (book.getCountInPlace() == 0) {
            book.setInStock(false);
        }
        customer.getBooksTaken().add(book);
        book.getTakenBy().add(customer);
        customerRepository.save(customer);
        response.sendRedirect("/");
    }


    @GetMapping("/booksTakenBy")
    public String takenByCustomer(HttpServletRequest request, Model model) {
        Long customerId = Long.parseLong(request.getParameter("customerId"));
        Customer customer = customerRepository.findById(customerId).orElse(null);
        model.addAttribute("customer", customer);
        return "booksTakenBy";
    }


    @GetMapping("/takenBy")
    public String bookTakenBy(HttpServletRequest request, Model model) {
        Book book = repository.findById(Long.parseLong(request.getParameter("bookId"))).orElse(null);
        model.addAttribute("customers", book.getTakenBy());
        return "takenBy";
    }



    @GetMapping("/return{bookId}")
    public String returnABook(@PathVariable("bookId") Long bookId, Model model) {
        model.addAttribute("bookId", bookId);
        return "return";
    }


    @GetMapping("/returnAndRedirect")
    public void returnAndRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long bookId = Long.parseLong(request.getParameter("bookId"));
        Long customerId = Long.parseLong(request.getParameter("customerId"));
        Book book = repository.findById(bookId).orElse(null);
        Customer customer = customerRepository.findById(customerId).orElse(null);
        book.getTakenBy().remove(customer);
        book.setCountInPlace(book.getCountInPlace() + 1);
        customer.getBooksTaken().remove(book);
        if (customer.getBooksTaken().size() == 0) {
            customer.setHasToReturn(false);
        }
        repository.save(book);
        response.sendRedirect("/");
    }

}
