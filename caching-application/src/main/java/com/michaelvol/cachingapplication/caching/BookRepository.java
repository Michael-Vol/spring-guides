package com.michaelvol.cachingapplication.caching;

public interface BookRepository {

    Book getByIsbn(String isbn);
}
