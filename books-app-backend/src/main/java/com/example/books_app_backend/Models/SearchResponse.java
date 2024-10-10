package com.example.books_app_backend.Models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {
    
    // private String status;
    private int totalItems;
    private List<Book> books;
    
}