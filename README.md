# Assignment 1 

## 1. PUT endpoint (update book)

First, I tested retrieving the first book:
**GET** `/api/books/1`


![GET Example](images/first-api/GET-1.png)

---

Then, I updated the title in the existing book:


**PUT** `/api/books/1`
![PUT Example](images/first-api/PUT-1.png)

<b>Result</b>: The `"title"` field was updated successfully. Since **PUT** performs a full update, the `"author"` and `"price"` fields were replaced with `null` because they were not included in the request body. <br>
<b>Console Output</b>:
![Console Out](images/first-api/console-updated.png)


Attempting to update a non-existing resource:

**PU** `/api/books/100`
![PUT Not Existing Ex](images/first-api/postman-not-found.png)
<b>Result</b>: A `ResponseStatusException` was thrown, returning `HTTP 404` to indicate that the requested resource was not found.
<br><b>Console Output</b>:
![Console Out1](images/first-api/book-not-found-console.png)


## 2. PATCH endpoint (partial update)
First, I retrieved an existing book to confirm the current values:

**GET** `/api/books/2`

![GET book 2](images/second-api/GET-2.png)

Then, I partially updated the book:

**PATCH** `/api/books/partial-update/2`
![PATCH book 2](images/second-api/update1.png)
<b>Result</b>: The `"title"`and `"price"` fields were updated successfully. Since **PATCH** performs a partial update, the `"author"` field stayed the same because it was not included in the request body. <br>


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

## 4. GET endpoint with pagination

## 5. Advanced GET endpoint with filtering, sorting, and pagination combined in the valid order


