create table Bank_Account
	(Account_ID		varchar(10), 
	 Bank_Name		varchar(20), 
	 Branch_Name	varchar(20),
   primary key (Account_ID)
	);

create table Publisher
	(Publisher_ID		 	varchar(10), 
	 First_Name		 	varchar(20), 
	 Last_Name			 varchar(20), 
	 Street_Address		varchar(30),
	 Zip_Code			varchar(10),
	 City				varchar(20),
	 Country			varchar(20),
   Publisher_Email_Address	varchar(50),
	 Main_Phone_Number	varchar(20),
	 Banking_Account_ID 	varchar(10),
	 primary key (Publisher_ID),
	 foreign key (Banking_Account_ID) references Bank_Account (Account_ID)
		on delete set null
	);

create table Warehouse
	(Name				varchar(20),
	 Warehouse_Street_Address	varchar(30),
	 Warehouse_Zip_Code	varchar(10),
	 Warehouse_City		varchar(20),
	 Warehouse_Country		varchar(20),
	  primary key (Name)
	);

create table Book_Collection
	(ISBN_Number		varchar(17),
	 Title				varchar(50),
   Genre				varchar(20),
   Language			varchar(25),
   Price				numeric(4, 2),
   Quantity			numeric(4, 0),
   Publisher_Earnings		numeric(4, 2),
   Publisher_ID			varchar(10),
   Warehouse_Name		varchar(20),
	 primary key (ISBN_Number),
	 foreign key (Publisher_ID) references Publisher(Publisher_ID)
		on delete set null,
	 foreign key (Warehouse_Name) references Warehouse(Name)
		on delete set null
	);

create table Author
	(First_Name		varchar(20), 
	 Last_Name		varchar(20), 
	 primary key (First_Name, Last_Name)
	);

create table Writes
	(Author_First_Name		varchar(20), 
	 Author_Last_Name		varchar(20), 
	 ISBN_Number		varchar(50),
	 primary key (Author_First_Name, Author_Last_Name, ISBN_Number),
	 foreign key (ISBN_Number) references Book_Collection (ISBN_Number)
		on delete set null,
   foreign key (Author_First_Name, Author_Last_Name) references Author (First_Name, Last_Name)
		on delete cascade
	);

create table Regular_User
	(User_ID			varchar(10), 
	 First_Name			varchar(20),
	 Last_Name			varchar(20), 
   Email_Address		varchar(50),
   Username			varchar(20),
   Password			varchar(15),
   Billing_Card_Name		varchar(20),
   Billing_Card_Number	varchar(20),
   Billing_Card_Expiry_Date	date,
   Billing_Card_CVV_CVC	varchar(4),
   Delivery_Street_Address	varchar(30),
	 Delivery_Zip_Code		varchar(10),
	 Delivery_City			varchar(20),
	 Delivery_Country		varchar(20),
	 primary key (User_ID)
	);

create table Order_Placement
	(Order_ID				varchar(10),  
	 Order_Item_ID			varchar(3),
	 Item_Quantity			numeric(4, 0),
	 Date_of_Order			date,
	 Order_Current_City			varchar(30),
	 Order_Current_Country		varchar(30),
	 User_ID				varchar(10),
	 Order_Billing_Card_Name		varchar(20),
   Order_Billing_Card_Number	varchar(20),
   Order_Billing_Card_Expiry_Date	date,
   Order_Billing_Card_CVV_CVC	varchar(4),
   Order_Delivery_Street_Address	varchar(30),
	 Order_Delivery_Zip_Code		varchar(10),
	 Order_Delivery_City			varchar(20),
	 Order_Delivery_Country		varchar(20),
	 primary key (Order_ID, Order_Item_ID),
	 foreign key (User_ID) references Regular_User(User_ID)
		on delete cascade
	);

create table Consists
	(Order_ID		varchar(10), 
	 Order_Item_ID	varchar(10), 
	 ISBN_Number	varchar(17),
   primary key (Order_ID, Order_Item_ID, ISBN_Number),
   foreign key (ISBN_Number) references Book_Collection(ISBN_Number)
      on delete cascade,
   foreign key (Order_ID, Order_Item_ID) references Order_Placement(Order_ID,  
   Order_Item_ID)
		on delete cascade
	);

create table Owner
	(Owner_ID				varchar(10), 
	 First_Name				varchar(20), 
	 Last_Name				varchar(20), 
   Owner_Email_Address		varchar(50),
   Username				varchar(20),
   Password				varchar(15),
   Owner_Main_Phone_Number	varchar(20),
	 primary key (Owner_ID)
	 );

create table Manages
	(Owner_ID		varchar(10), 
	 ISBN_Number	varchar(17), 
   primary key (Owner_ID, ISBN_Number),
	 foreign key (Owner_ID) references Owner (Owner_ID)
		on delete cascade,
	 foreign key (ISBN_Number) references Book_Collection(ISBN_Number)
		on delete cascade
	);

create table Sales_Report
	(Report_ID 			varchar(10),
	 Date_of_Report		date,
	 Type_of_Report		varchar(20)
	 primary key (Report_ID)
	);

create table Accesses
	(Owner_ID		varchar(10), 
	 Report_ID		varchar(10), 
   primary key (Owner_ID, Report_ID),
	 foreign key (Owner_ID) references Owner (Owner_ID)
		on delete cascade,
	 foreign key (Report_ID) references Sales_Report(Report_ID)
		on delete cascade
	);

create table Contains
	(Report_ID		varchar(10), 
	 Order_Item_ID	varchar(10), 
	 Order_ID		varchar(10),
   primary key (Report_ID, Order_Item_ID, Order_ID),
   foreign key (Report_ID) references Sales_Report(Report_ID)
      on delete cascade,
   foreign key (Order_ID, Order_Item_ID) references Order_Placement(Order_ID, Order_Item_ID)
		on delete cascade
	);
