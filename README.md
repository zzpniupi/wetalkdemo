# wetalkdemo
The link of all files is: https://github.com/zzpniupi/wetalkdemo

My project is an online chat app, which consist of three parts: client, server and database. In this document, I’ll show you how to run my project in your computer.

Configuration environment：
Client:
	Android Studio
Server:
	Eclipse
Database:
	Mysql

First:
	You should build the database first. I create the database by XAMPP Mysql, I export the sql file in the “database” folder, import it to your Mysql and create database.
 
Second:
	Use Eclipse to open the server project, you should add the .jar file to your build path, so Eclipse can connect to your database.  
 Different database need different .jar file, so find the file which your database need. I provide the .jar file the XAMPP Mysql need in the database folder
 
After you add the .jar file into your build path, don’t forget to modify the set of the url in \wetalk\server\wetalkServer\src\Server\db\DBManager.java. In my case, my data base my database doesn’t have a password.
 
	Then, you can run the server.
Third:
	Use the Android Studio to open the client project. Because the server is based on the local area network, so before you run your client, change the ip address from my ip address to yours, the java file is in \wetalk\client\WeTalk\app\src\main\java\com\example\wetalk\server\ServerManager.java
 
Before you run the app, please make sure the server is online.

Run:

Flow of each step:
	Because I use the XAMPP, so I first start the database
 
 
Then, I start the server,
 
Finally, I run the app
 

As for what the app can do, I’ll show you in the video.
