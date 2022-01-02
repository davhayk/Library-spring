package com.example.crud_spring.controllers;

import com.example.crud_spring.entities.Donator;
import com.example.crud_spring.repositories.DonatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Controller
public class DonatorController {
    @Autowired
    DonatorRepository donatorRepository;

    @GetMapping("/donators")
    public String getDonators(Model model) {
        Iterable<Donator> donators = donatorRepository.findAll();
        model.addAttribute("donators", donators);
        return "donators";
    }



    @GetMapping("/booksDonated")
    public String showBooksDonated(Model model, HttpServletRequest request) {
        Long donatorId = Long.parseLong(request.getParameter("donatorId"));
        Donator donator = donatorRepository.findById(donatorId).orElse(null);
        model.addAttribute("donator", donator);
        return "booksDonated";
    }

    @GetMapping("/editDonator{donatorId}")
    public String editDonator(@PathVariable("donatorId") long donatorId, Model model) {
        Donator donator = donatorRepository.findById(donatorId).orElse(null);
        model.addAttribute("donator", donator);
        return "editDonator";
    }
    @GetMapping("/editDonatorAndRedirect")
    public void editDonatorAndRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Donator donator = donatorRepository.findById(Long.parseLong(request.getParameter("donatorId"))).orElse(null);
        donator.setName(request.getParameter("name"));
        donator.setSurname(request.getParameter("surname"));
        donator.setPhoneNumber(request.getParameter("phoneNumber"));
        donator.setEmail(request.getParameter("email"));
        response.sendRedirect("/donators");
    }
}
