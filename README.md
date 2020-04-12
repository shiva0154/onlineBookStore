# Online Book Store


### Use Cases:

* User can Search books with Title/Isbn/Author from the store
* Admin can add books to store
* User can buy book(s) from the store
* User can search media coverage for the books with ISBN


### High Level Design

![Imgur](https://i.imgur.com/BdECT0Z.png)


### Step 3: Design core components


#### Storage:

There are 3 tables used in this service. 
1.book
2.book_inventory
3.price_inventory

Book table stores information related to Book like ISBN,title,author,isActive and generate bookID unique for each book.
Book_inventory stores the quantity of  books w.r.t bookId.
price_inventory stores the information of price of a book w.r.t bookId.


#### Elastic Search Box:

Elastic search is very useful in searching with efficiency. Used ES for searching the media post which are matching with the book title.


#### Technology:
Language : Java 8
Storage  : Mongodb, Elastic Search
platform : Docker


### Use Case : User can Search books with Title/Isbn/Author from the store

![Imgur](https://i.imgur.com/WZNhJFm.png)

Request URL: 

```
curl --location --request GET 'http://<service_url>/v1/book_store/book' \
'{
	"author": "",
	"title" : "".
	"isbn" : ""
}'
```

##### Response:

###### success:
```

{
  status: "Success",
  books: {
    [
    book_id : <String>
    title   :<String>
    author  : <String>
    isbn    : <String>
    ]
   }
    
```
###### Failure:

```
{
      status: "Failure",
      error_code: <String>,
      message: <String>
    
}
```
User can search books with any of the field author/title/isbn. The search query will hit mongo and get the List of the books match with isbn/title/author. The result list will be processed and send as a json response to the client.


### Use Case : Admin can add books to store

*validation of admin is out of scope.

![Imgur](https://i.imgur.com/IRIp7l3.png)

Request: 

```
curl --location --request POST 'http://<service_url>/v1/book_store/book' \
--data-raw '{
	"isbn":"123456",
	"title":"OS",
	"author": "siva1hh",
	"price" : "100",
	"quantity":100
}'

```

##### Response:

###### success:
```
{
  status: "Success",
  book_id : <String> 
}
```

###### Failure:
```
{ 
    status: "Failure",
    error_code: <String>,
    message: <String>
}
```

Admin can add books to the online store and the admin validation is out of scope ot this project.
To add a book, mandatory information: (isbn,title,author,price). Quantity is optional and default value is 1.
if there is no book match with the isbn,title & author, new entries are created in 3 tables.
if there is a book match with the information, quantity will get updated in the book_inventory table.


### Use Case : User can buy book(s) from the store

*scope is limited to one book with multiple copies only. Multiple books are not allowed to buy.
*User information validation is out of scope of this project.

![Imgur](https://i.imgur.com/cTVxIU3.png)

Request:

```
curl --location --request POST 'http://<service_url>/v1/book_store/book/orderRequest' \
--data-raw '{
	
	"bookId" : "5ea6db17-d22f-446b-b60f-dda122137233",
	"userId": "12345",
	"quantity":10
}'
```

##### Response:

###### success:

```
{
  status: "Success",
  
  order_details: {
    order_id:<String>,
    user_id: <String>,
    book_id: <String>,
    quantity: <int>,
    book_price: <double>,
    total_cost: <double>,
    order_time: <String>    
  }
  
}
```

###### Failure:

```
{
    status: "Failure",
    error_code: <String>,
    message: <String>
}
```



### Use Case : User can search media coverage for the books with ISBN


![Imgur](https://i.imgur.com/hHH6l4T.png)

Request:
```
curl --location --request GET 'http://localhost:80/v1/book_store/mediaposts?isbn=1234569' \
--data-raw ''
```

##### Response:

###### success

```
{
    status: "Success",
    book_ids: [
    <String-1>,
    <String-2>
    ]
}
```

###### Failure:
```
{
  status: "Failure",
  error_code: <String>,
  message: <String>
}
```


### Run commands:

##### Github
run mvn clean install

##### Docker
```
docker pull docker pull shiva0154/online_bookstore
docker run docker run -p 80:80 <docker_id>
```







