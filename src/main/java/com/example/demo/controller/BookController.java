package com.example.demo.controller;
import com.example.demo.entity.Book;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController // this class handles API requests
@RequestMapping("/api") // base path
public class BookController {
    private List<Book> books = new ArrayList<>();
    private Long nextId = 1L;

    public BookController() {
        books.add(new Book(nextId++, "Spring Boot in Action", "Craig Walls", 39.99));
        books.add(new Book(nextId++, "Effective Java", "Joshua Bloch", 45.00));
        books.add(new Book(nextId++, "Clean Code", "Robert Martin", 42.50));
        books.add(new Book(nextId++, "Java Concurrency in Practice", "Brian Goetz", 49.99));
        books.add(new Book(nextId++, "Design Patterns", "Gang of Four", 54.99));
        books.add(new Book(nextId++, "Head First Java", "Kathy Sierra", 35.00));
        books.add(new Book(nextId++, "Spring in Action", "Craig Walls", 44.99));
        books.add(new Book(nextId++, "Clean Architecture", "Robert Martin", 39.99));
        books.add(new Book(nextId++, "Refactoring", "Martin Fowler", 47.50));
        books.add(new Book(nextId++, "The Pragmatic Programmer", "Andrew Hunt", 41.99));
        books.add(new Book(nextId++, "You Don't Know JS", "Kyle Simpson", 29.99));
        books.add(new Book(nextId++, "JavaScript: The Good Parts", "Douglas Crockford", 32.50));
        books.add(new Book(nextId++, "Eloquent JavaScript", "Marijn Haverbeke", 27.99));
        books.add(new Book(nextId++, "Python Crash Course", "Eric Matthes", 38.00));
        books.add(new Book(nextId++, "Automate the Boring Stuff", "Al Sweigart", 33.50));
    }

    // get all books - /api/books
    @GetMapping("/books")
    public List<Book> getBooks() {
        return books;
    }

    // get book by id
    @GetMapping("/books/{id}")
    public Book getBook(@PathVariable Long id) {
        return books.stream().filter(book -> book.getId().equals(id))
                .findFirst().orElse(null);
    }

    // create a new book
    @PostMapping("/books")
    public List<Book> createBook(@RequestBody Book book) {
        books.add(book);
        return books;
    }

    // search by title
    @GetMapping("/books/search")
    public List<Book> searchByTitle(
            @RequestParam(required = false, defaultValue = "") String title
    ) {
        if (title.isEmpty()) {
            return books;
        }
        return books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }


    // price range
    @GetMapping("/books/price-range")
    public List<Book> getBooksByPrice(
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice
    ) {
        return books.stream()
                .filter(book -> {
                    boolean min = minPrice == null || book.getPrice() >= minPrice;
                    boolean max = maxPrice == null || book.getPrice() <= maxPrice;

                    return min && max;
                }).collect(Collectors.toList());
    }

    //sort
    @GetMapping("/books/sorted")
    public List<Book> getSortedBooks(
            @RequestParam(required = false, defaultValue = "title") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order
    ){
        Comparator<Book> comparator;

        switch(sortBy.toLowerCase()) {
            case "author":
                comparator = Comparator.comparing(Book::getAuthor);
                break;
            case "title":
                comparator = Comparator.comparing(Book::getTitle);
            default:
                comparator = Comparator.comparing(Book::getTitle);
                break;
        }
        if("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }
        return books.stream().sorted(comparator)
                .collect(Collectors.toList());
    }

    // ---------------------------- HW -------------------------------

    // PUT endpoint (update book)
    @PutMapping("/books/update/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        Book bookToUpdate = books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (bookToUpdate == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found!");
        }
        // update
        bookToUpdate.setTitle(updatedBook.getTitle());
        bookToUpdate.setAuthor(updatedBook.getAuthor());
        bookToUpdate.setPrice(updatedBook.getPrice());

        System.out.println("Book with id: " + id + " has been updated");
        return bookToUpdate;
    }

    //  *   *   *   *   *   *   *   *   *   *   *   *   *   *   *

    // PATCH endpoint (partial update)
    @PatchMapping("/books/partial-update/{id}")
    public Book partialUpdateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        Book bookToUpdate = books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (bookToUpdate == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found!");
        }
        // update
        if (updatedBook.getTitle() != null) {
            bookToUpdate.setTitle(updatedBook.getTitle());
        }
        if (updatedBook.getAuthor() != null) {
            bookToUpdate.setAuthor(updatedBook.getAuthor());
        }
        if (updatedBook.getPrice() != null) {
            bookToUpdate.setPrice(updatedBook.getPrice());
        }

        System.out.println("Book with id: " + id + " has been partially updated");
        return bookToUpdate;
    }

    //  *   *   *   *   *   *   *   *   *   *   *   *   *   *   *

    // DELETE endpoint (remove book)
    @DeleteMapping("/books/delete/{id}")
    public Book deleteBook(@PathVariable Long id) {
    Book bookToDelete = books.stream()
            .filter(book -> book.getId().equals(id))
            .findFirst()
            .orElse(null);
    if (bookToDelete == null) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found!");
    }
    books.remove(bookToDelete);
    System.out.println("Book with id: " + id + " has been removed");
    return bookToDelete;
    }

    //  *   *   *   *   *   *   *   *   *   *   *   *   *   *   *

    // GET endpoint with pagination
    @GetMapping("/books-paginated")
    public List<Book> getPaginatedBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        int limit = Math.min(size, 100); // // Set maximum page size to prevent abuse
        int offset = page * limit; // How many books to skip over

        return books.stream()
                .skip(offset) // Jump to the start of page
                .limit(limit) // Only take size of page
                .collect(Collectors.toList());

    }

    //  *   *   *   *   *   *   *   *   *   *   *   *   *   *   *

    // Advanced GET endpoint with filtering, sorting, and pagination combined in the valid order
    // filter -> sort -> pagination
    @GetMapping("/books-advanced")
    public List<Book> getFilteredBooks(
        // // 1. filter params (required=false so they can be null)
        @RequestParam(required = false) String title,
        @RequestParam(required = false) String author,
        @RequestParam(required = false) Double minPrice,
        @RequestParam(required = false) Double maxPrice,
        // 2. sort params
        @RequestParam(defaultValue = "title") String sortBy,
        @RequestParam(defaultValue = "asc") String order,
        // 3. pagination params
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
        ) {

        int safeSize = Math.min(size, 100); // Max 100 items
        int safePage = Math.max(0, page); // avoid negatives
        int offset = safePage * safeSize;

        // STEP 1: START THE STREAM & FILTER
        // do this first to "shrink" the list to only relevant books
        Stream<Book> bookStream = books.stream()
                .filter(book -> title == null || book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .filter(book -> author == null || book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .filter(book -> (minPrice == null || book.getPrice() >= minPrice) &&
                        (maxPrice == null || book.getPrice() <= maxPrice));

        // STEP 2: SORT
        // Organize the filtered results before we cut them into pages
        Comparator<Book> comparator = switch (sortBy.toLowerCase()) {
            case "price" -> Comparator.comparing(Book::getPrice);
            case "author" -> Comparator.comparing(Book::getAuthor);
            default -> Comparator.comparing(Book::getTitle);
        };

        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }

        // STEP 3: PAGINATE (The final "slice")
        // Use .skip() for the offset and .limit() for the page size
        return bookStream
                .sorted(comparator) // sort after filtering
                .skip(offset)       // pagination is the very last step
                .limit(safeSize)
                .collect(Collectors.toList());

    }

}