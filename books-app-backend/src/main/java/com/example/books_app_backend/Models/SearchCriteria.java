package com.example.books_app_backend.Models;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteria {
    // This is known as a parameter/criteria object

    // To consider, what happens if we do not get a valid result back based on the criteria?
    
    private String title;
    private String author;
    private String publisher;
    private String isbn;

    public SearchCriteria setTitle(String title) {
        this.title = title;
        return this;
    }

    public SearchCriteria setAuthor(String author) {
        this.author = author;
        return this;
    }

    public SearchCriteria setPublisher(String publisher) {
        this.publisher = publisher;
        return this;
    }

    public SearchCriteria setIsbn(String isbn) {
        this.isbn = isbn;
        return this;
    }

    public Map<String, String> toQueryParams() {
        Map<String, String> fields = new HashMap<>();
        if (title != null) fields.put("intitle", title);
        if (author != null) fields.put("inauthor", author);
        if (publisher != null) fields.put("inpublisher", publisher);
        if (isbn != null) fields.put("isbn", isbn);

        return fields;
    }
}
