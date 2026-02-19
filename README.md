# Assignment 1 

## 1. PUT endpoint (update book)

First, I tested retrieving the first book:
`GET /api/books/1`


![GET Example](images/first-api/GET-1.png)

---

Then, I updated the title in the existing book:


`PUT /api/books/1`
![PUT Example](images/first-api/PUT-1.png)

<b>Result</b>: The `"title"` field was updated successfully. Since `PUT` performs a full update, the `"author"` and `"price"` fields were replaced with `null` because they were not included in the request body. <br>
<b>Console Output</b>:
![Console Out](images/first-api/console-updated.png)


Attempting to update a non-existing resource:

`PUT /api/books/100`
![PUT Not Existing Ex](images/first-api/postman-not-found.png)
<b>Result</b>: A `ResponseStatusException` was thrown, returning `HTTP 404` to indicate that the requested resource was not found.
<br><b>Console Output</b>:
![Console Out1](images/first-api/book-not-found-console.png)


## 2. PATCH endpoint (partial update)

## 3. DELETE endpoint (remove book)

## 4. GET endpoint with pagination

## 5. Advanced GET endpoint with filtering, sorting, and pagination combined in the valid order


