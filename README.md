# Assignment 1 
CPSC449<br>
Mariia Nikitash
## 1. PUT endpoint (update book)

First, I tested retrieving the first book:
**GET** `/api/books/1`


![GET Example](images/first-api/GET-1.png)


Then, I updated the title in the existing book:


**PUT** `/api/books/1`
![PUT Example](images/first-api/PUT-1.png)

<b>Result</b>: The `"title"` field was updated successfully. Since **PUT** performs a full update, the `"author"` and `"price"` fields were replaced with `null` because they were not included in the request body. <br>
<b>Console Output</b>:
![Console Out](images/first-api/console-updated.png)


Attempting to update a non-existing resource:

**PUT** `/api/books/100`
![PUT Not Existing Ex](images/first-api/postman-not-found.png)
<b>Result</b>: A `ResponseStatusException` was thrown, returning `HTTP 404` to indicate that the requested resource was not found.
<br><b>Console Output</b>:
![Console Out1](images/first-api/book-not-found-console.png)

---
## 2. PATCH endpoint (partial update)
First, I retrieved an existing book to confirm the current values:

**GET** `/api/books/2`

![GET book 2](images/second-api/GET-2.png)

Then, I partially updated the book:

**PATCH** `/api/books/partial-update/2`
![PATCH book 2](images/second-api/update1.png)
<b>Result</b>: The `"title"`and `"price"` fields were updated successfully. Since **PATCH** performs a partial update, the `"author"` field stayed the same because it was not included in the request body. <br>

---
## 3. DELETE endpoint (remove book)

First, I retrieved Book with ID 3 to confirm it exists:

**GET** `/api/books/3`

![GET book 3](images/third-api/get3.png)


Then, I deleted the book:

**DELETE** `/api/books/3`

![DELETE book 3](images/third-api/delete3.png)

<b>Result</b>:  
The request returned `200 OK` and responded with the deleted book object.  
The console log confirmed:

![Console delete log](images/third-api/console-delete.png)

---

Finally, I attempted to delete the same book again:

**DELETE** `/api/books/3`

![DELETE after removal (404)](images/third-api/delete-after.png)

<b>Result</b>:  
The request returned **404 Not Found**, confirming that the book no longer exists in the system.
---

## 4. GET endpoint with pagination
This endpoint supports pagination using query parameters:

GET `/api/books-paginated?page={page}&size={size}`

- `page` → page number (starting from 0)
- `size` → number of books per page
- Maximum page size is limited to 100 to prevent abuse


### Example 1


GET `/api/books-paginated?page=1&size=2`

![Pagination example 1](images/forth-api/page1.png)

**Result:**  
The API returned the second page (page index starts at 0) with 2 books.



### Example 2

GET `/api/books-paginated?page=2&size=4`

![Pagination example 2](images/forth-api/page2.png)

**Result:**  
The API skipped the first 8 books (page × size) and returned the next 4 books.


### Behavior

- Pagination is implemented using Java Streams:
    - `.skip(offset)` to move to the correct page
    - `.limit(size)` to restrict results
- If a page exceeds the available data, the API returns `200 OK` with an empty list.
---
## 5. Advanced GET endpoint with filtering, sorting, and pagination combined in the valid order

GET `/api/books-advanced`

This endpoint supports:

- Filtering (`title`, `author`, `minPrice`, `maxPrice`)
- Sorting (`sortBy=title|author|price`)
- Order (`asc` or `desc`)
- Pagination (`page`, `size`)


### Example 1
Filter by Author + Sort by Title (DESC)


GET `/api/books-advanced?author=martin&sortBy=title&order=desc&page=0&size=5`
![Pagination example 2](images/fifth-api/get2.png)

**Result:**
- Filters books where author contains "martin"
- Sorts by title in descending order
- Returns first 5 results


### Example 2
Filter by Title + Price Range + Sort by Price (ASC) + Pagination


GET `/api/books-advanced?title=java&minPrice=30&maxPrice=60&sortBy=price&order=asc&page=1&size=2`
![Pagination example 2](images/fifth-api/get3.png)
**Result:**

- Filters books with "java" in title
- Only books priced between 30–60
- Sorts by price ascending
- Skips first page and returns 2 results


### Example 3
Filter by Title + Sort by Price (DESC)

GET `/api/books-advanced?title=java&minPrice=30&sortBy=price&order=desc&page=0&size=3`
![Pagination example 2](images/fifth-api/get1.png)

**Result:**
- Filters books with "java" in title
- Minimum price of 30
- Sorts by price descending
- Returns first 3 results




