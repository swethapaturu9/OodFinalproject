package com.oodProject.library.pojo;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import com.oodProject.library.util.CsvFileUtil;


 @Service
 public class Library {
	 
	 
	 private static int applicationIdCounter = 1;
	
	
    private  List<Member> members;
	
	private List<Librarian> librarians;
	
	private  List<PrivateRoom> rooms;
	
	private  List<Book> books;
	
	private  List<Book> borrowedBooks;
	
	private  Person admin;
	
	private  List<Application> applications;
	
	
	 private static class BookFactory {
	        public static Book createBook(String[] data) {
	            int bookId = Integer.parseInt(data[0]);
	            String title = data[1];
	            String author = data[2];
	            String genre = data[3];
	            String language = data[4];
	            return new Book(bookId, title, author, genre, language);
	        }
	    }
	
	
	 public Library(@Value("${library.books.csv.path}") String booksCSV,
             @Value("${library.admin.username}") String username,
             @Value("${library.admin.password}") String password) {
		
		
		books = new ArrayList<>();
		applications = new ArrayList<>();
		librarians = new ArrayList<>();
		
        List<String[]> rawData = CsvFileUtil.readCSV(booksCSV);

       
        for (int i = 0; i < rawData.size(); i++) {
            Book book = BookFactory.createBook(rawData.get(i));
            books.add(book);
        }
        
        admin = new Person();
        
        admin.setFirstName("Admin");
        admin.setLastName("Admin");
        
        admin.setUsername(username);
        admin.setPassword(password);
        admin.setRole("ADMIN");
        
		
	}
	 
	 
	 public boolean authenticateAdmin(String username, String password) {
	        return admin.getUsername().equals(username) && admin.getPassword().equals(password);
	    }
	 
	 
	 public List<Book> getAllBooks() {
	        return books;
	    }
	 
	 
	 public List<Application> getAllApplications() {
		 
		 return applications;
		 
	 }
	 
	 
    public List<Librarian> getAllLibrarians() {
		 
		 return librarians;
		 
	 }
	 
	 
	 
	 public void addApplication(Application application) {
	        application.setId(applicationIdCounter++);
	        applications.add(application);
	    }


	 public Application getApplicationById(int applicationId) {
	        for (Application application : applications) {
	            if (application.getId() == applicationId) {
	                return application;
	            }
	        }
	        return null;
	    }


	public boolean authenticateLibrarian(String username, String password) {
		
		String filePath = "../../../../../../../credentials"; 
		
		List<String[]> users = CsvFileUtil.readCSV(filePath);
		
		System.out.println(users.get(0));
		for (String[] librarian : users) {
	        System.out.println(librarian[0]);
	        String csvUsername = librarian[1];
	        
	       
	        String csvPassword = librarian[2];
	        String role = librarian[3];
	        
	        
	        System.out.println(csvUsername);
	        System.out.println(csvPassword);
	        System.out.println(role);

	        
	        if (csvUsername.equals(username) && csvPassword.equals(password) && role.equals("Lib")) {
	        
	            return true;
	        }
	      	
	}
		
		return false;
		
		
	}
		
	public Librarian getLibrarianByusername(String username) {
		
		for (Librarian librarian : librarians) {
            if (librarian.getUsername().equals(username)) {
                return librarian;
            }
        }
		
        return null;
        
    }
	
	
	public List<Book> getBorrowRequests(Librarian librarian) {
		
	
		return librarian.getBorrowRequests();
		
		
	}
	
	public List<Book> getReturnRequests(Librarian librarian) {
		
		return librarian.getReturnRequests();
		
	}
	
	

	
 }
