package com.example.crud_spring.entities;

import lombok.Data;
import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @ManyToOne(cascade = CascadeType.ALL)
    private Author author;
    private int countInPlace;
    private int countOverall;
    private boolean readOnly = false;
    private LocalDate dateAdded = LocalDate.now();
    private Date dateTaken;
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Customer> takenBy = new ArrayList<>();
    private boolean inPDF;
    private boolean inWrittenForm;
    @OneToMany
    private List<Language> languages;
    private boolean inStock;
    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<Donator> donators = new ArrayList<>();
    public String getProportion() {
        return countInPlace + "/" + countOverall;
    }
    public String getLanguagesAvailable() {
        if (languages.size() == 0)
            return "";
        StringBuilder s = new StringBuilder();
        for (Language language : languages) {
            s.append(language.getName()).append(", ");
        }
        s.deleteCharAt(s.length() - 1);
        s.deleteCharAt(s.length() - 1);
        return s.toString();
    }
    public String written() {
        if (inWrittenForm)
            return "on";
        return "off";
    }
    public String pdf() {
        if (inPDF)
            return "on";
        return "off";
    }
    public String readOnlyOn() {
        if (readOnly)
            return "on";
        return "off";
    }
}
