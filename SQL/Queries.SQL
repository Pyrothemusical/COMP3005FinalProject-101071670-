
--UserLogin.java

SELECT user_id FROM regular_user WHERE username = ? AND password = ?
--Query used to confirm user’s login by ensuring that the submitted username and password matches with any record in relations regular_user
--If successfully run, query returns user_id that will later be used to track which user is making new orders

SELECT username FROM regular_user WHERE username = ? 
-- Query used to confirm if a new username submitted by an unregistered user has already been used or not.
-- If no existing usernames are present, the query returns an empty record which will confirm that this new username submitted by the user is valid.

SELECT password FROM regular_user WHERE password = ? 
--Query used to confirm if a new password submitted by an unregistered user has already been used or not.
--If no existing passwords are present, the query returns an empty record which will confirm that this new password submitted by the user is valid.

--UnregisteredUserInterface.java

SELECT user_id FROM regular_user ORDER BY length(user_id), user_id ASC
--Query used to retrieve the highest user ID present in the regular_user relation in order to increment the returned value and assign it to the user who is attempting to register as a new customer.

insert into regular_user (User_ID, First_Name, Last_Name, Email_Address, Username, Password,  Billing_Card_Name, Billing_Card_Number, Billing_Card_Expiry_Date, Billing_Card_CVV_CVC,  Delivery_Street_Address, Delivery_Zip_Code, Delivery_City, Delivery_Country) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
--Query used to insert a new record for the user who wishes to register to the application in order to add books to the cart and check them out.
--Required user information to register a new user includes the user’s first and last name, email, billing information and delivery information.

--RegisteredUserInterface.java

SELECT * FROM warehouse
--Query used to retrieve information from the warehouse, particularly the warehouse name and location.

SELECT order_id FROM order_placement ORDER BY length(order_id), order_id ASC
----Query used to retrieve the highest order ID present in the order_placement relations in order to increment the returned value and assign it to the new order that is being created.

INSERT INTO order_placement (order_id, order_item_id, item_quantity, date_of_order, order_current_city, order_current_country, user_id, order_billing_name, order_billing_number, order_billing_expiry_date, order_billing_cvc_cvv, order_delivery_street_address, order_delivery_zip_code, order_delivery_city, order_delivery_country) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)

INSERT INTO consists (order_id, order_item_id, isbn_number) VALUES (?, ?, ?)
--Following 2 queries used to insert a new record for the new order created by the specific user along with the order’s books in the order_placement and consists relations.

SELECT quantity FROM book_collection WHERE isbn_number = ?

UPDATE book_collection SET quantity = ? WHERE isbn_number = ?
--Following 2 queries are used to update the amount of books present in the book collection after a shopping cart is checked out.

SELECT * FROM book_collection NATURAL JOIN consists  NATURAL JOIN order_placement WHERE isbn_number = ?

SELECT * FROM book_collection NATURAL JOIN publisher WHERE isbn_number = ?

UPDATE public.book_collection SET quantity = ? WHERE isbn_number = ?
--Following 3 queries are used when the application requires to send an email to the publisher of a book whose quantity is less than or equal to 10 in the book collection.
--Publisher information is retrieved from the publisher relations to correctly display the email sent message in the application's log message, as well as order information is retrieved to confirm how many books need to be ordered.

SELECT * FROM consists NATURAL JOIN book_collection NATURAL JOIN order_placement NATURAL JOIN writes WHERE order_id = ? AND user_id = ?
--Query is used to retrieve the specific order that the user wishes to track by using the given order ID and user ID in the consists relation.

--OwnerInterface.java

SELECT publisher_id FROM publisher WHERE publisher_id = ?

SELECT name FROM warehouse WHERE name = ?
-Following 2 queries are used to confirm the information submitted by the owner whenever they attempt to insert a new book is valid.

INSERT INTO book_collection (isbn_number, title, genre, language, price, quantity, publisher_earnings, publisher_id, warehouse_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)

INSERT INTO author (first_name, last_name) VALUES (?, ?)

INSERT INTO writes (author_first_name, author_last_name, isbn_number) VALUES (?, ?, ?)

INSERT INTO manages (owner_id, isbn_number) VALUES (?, ?)
--Following 4 queries are used when a new book is inserted into the book store’s book collection.

SELECT count(*) FROM book_collection

SELECT * FROM consists WHERE isbn_number = ?

DELETE FROM writes WHERE isbn_number = ?

DELETE FROM book_collection WHERE isbn_number = ?
--Following 4 queries are used when an owner wishes to remove a book from the book collection of the book store.
--First query is used to confirm if there are any books in the book collection.
--Second query is used to check if any order uses this book.
--Third and forth queries are used to delete any records that contain the specified ISBN number in relations consists and writes.

INSERT INTO bank_account (account_id, bank_name, branch_name) VALUES (?, ?, ?)
--Query used to insert a new record for the new bank account registered to the publisher by the owner in the bank_account relation.

SELECT publisher_id FROM publisher ORDER BY length(publisher_id), publisher_id ASC
--Query used to retrieve the highest publisher ID present in the publisher relation in order to increment the returned value and assign it to the new publisher that the owner is attempting to create.

SELECT account_ID FROM bank_account WHERE account_ID = ?

SELECT banking_account_id FROM publisher WHERE banking_account_id = ?
--Following 2 queries are used to confirm if the account ID from the publisher’s input is accurate.

INSERT INTO publisher (publisher_id, first_name, last_name, street_address, zip_code, city, country,  publisher_email_address, main_phone_number, banking_account_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
--Query used to insert a new record for the new publisher created by the owner along with in the publisher relations.
--Publisher information includes publisher ID, name, email address, address, phone number and banking account ID.

--BookStoreCreateReport.java

SELECT report_id FROM sales_report ORDER BY length(report_id), report_id ASC
--Query used to retrieve the highest reportID present in the sales_report relation in order to increment the returned value and assign it to the new report that is being created by the owner.

INSERT INTO sales_report (report_id, date_of_report, type_of_report) VALUES (?, ?, ?)

INSERT INTO accesses (owner_id, report_id) VALUES (?, ?)
--Following 2 queries used to insert the corresponding records in both sales_report and access relations whenever a new report is created.

SELECT * FROM owner WHERE owner_id = ?
--Query is used to retrieve specific information about the owner who requests the report in order to display it in the report’s header.

SELECT * FROM consists NATURAL JOIN order_placement NATURAL JOIN book_collection NATURAL JOIN writes NATURAL JOIN publisher ORDER BY title

SELECT * FROM consists NATURAL JOIN order_placement NATURAL JOIN book_collection NATURAL JOIN writes NATURAL JOIN publisher ORDER BY author_last_name

SELECT * FROM consists NATURAL JOIN order_placement NATURAL JOIN book_collection NATURAL JOIN writes NATURAL JOIN publisher ORDER BY language

SELECT * FROM consists NATURAL JOIN order_placement NATURAL JOIN book_collection NATURAL JOIN writes NATURAL JOIN publisher ORDER BY genre

SELECT * FROM consists NATURAL JOIN order_placement NATURAL JOIN book_collection NATURAL JOIN writes NATURAL JOIN publisher ORDER BY last_name
--Following 5 queries are used to retrieve the requested information sorted by the type of report that the owner wishes to create.

INSERT INTO contains (report_id, order_item_id, order_id) VALUES (?, ?, ?)
--Query is used to insert the orders that the report is composed of into the relation contains.

--BookCollectionSearch.java

SELECT * FROM writes NATURAL JOIN book_collection NATURAL JOIN publisher WHERE title LIKE '%' || ? || '%'

SELECT * FROM writes NATURAL JOIN book_collection NATURAL JOIN publisher WHERE isbn_number LIKE '%' || ? || '%'

SELECT * FROM writes NATURAL JOIN book_collection NATURAL JOIN publisher WHERE genre LIKE '%' || ? || '%'

SELECT * FROM writes NATURAL JOIN book_collection NATURAL JOIN publisher WHERE language LIKE '%' || ? || '%'

SELECT * FROM writes NATURAL JOIN book_collection NATURAL JOIN publisher WHERE last_name LIKE '%' || ? || '%'
--Following 5 queries are used for the implemented search feature by filtering specific rows of the category that the user would like to search in.
