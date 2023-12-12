package com.oodProject.library.controller;

import java.util.List;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.oodProject.library.pojo.Application;
import com.oodProject.library.pojo.Book;
import com.oodProject.library.pojo.Library;

import com.oodProject.library.pojo.Member;
import com.oodProject.library.pojo.Person;
import com.oodProject.library.pojo.PrivateRoom;
import com.oodProject.library.util.CsvFileUtil;

import jakarta.servlet.http.HttpSession;

import com.oodProject.library.pojo.Librarian;


@Controller
public class AdminController {
	
	@Autowired
	private final Library libraryService;

    public AdminController(Library libraryService) {
        this.libraryService = libraryService;
    }

    @PostMapping("/AdminLogin")
    public String handleAdminLogin(@RequestParam String username, 
                                   @RequestParam String password, 
                                   Model model, HttpSession session) {
        if (libraryService.authenticateAdmin(username, password)) {
        		Person admin = new Person();
        		admin.setRole("Administrator");
        		admin.setUsername(username);
        		admin.setPassword(password);
        		
        		session.setAttribute("admin", admin);
        		
        	   List<Book> books = libraryService.getAllBooks();
        	   List<Application> applications = libraryService.getAllApplications();
        	   List<Librarian> librarians = libraryService.getAllLibrarians();
        	   List<PrivateRoom> privateRooms = libraryService.getAllRooms();
               model.addAttribute("books", books);
               model.addAttribute("applications", applications);
               model.addAttribute("librarians",librarians);
               model.addAttribute("privaterooms", privateRooms);
               
               return "admin_dashboard"; 
        } else {
        	
            model.addAttribute("errorMessage", "Invalid credentials");
            return "admin_login"; 
        }
    }
    
    @PostMapping("/acceptApplication")
    public String acceptApplication(@RequestParam("applicationId") int applicationId, Model model, HttpSession session) {
    	
    	
    	Person adminData = (Person)session.getAttribute("admin");
    	System.out.println(adminData.getUsername()+" "+adminData.getRole());
    	
        Application acceptedApplication = libraryService.getApplicationById(applicationId);
 
        
        Librarian librarian = new Librarian();
        
        librarian.setFirstName(acceptedApplication.getFirstName());
        librarian.setLastName(acceptedApplication.getLastName());
        librarian.setId(applicationId);
     
       
        model.addAttribute("librarian", librarian);
        return "add_librarian"; 
    }
    
    
    
    @PostMapping("/declineApplication")
    public String declineApplication(@RequestParam("applicationId") int applicationId, Model model) {
    	
    	Application declinedApplication = libraryService.getApplicationById(applicationId);
    	
    	 libraryService.getAllApplications().remove(declinedApplication);
    	 
       List<Book> books = libraryService.getAllBooks();
  	   List<Application> applications = libraryService.getAllApplications();
  	   List<Librarian> librarians = libraryService.getAllLibrarians();
         model.addAttribute("books", books);
         model.addAttribute("applications", applications);
         model.addAttribute("librarians",librarians);
         return "admin_dashboard"; 
    	
    	
    }
    
    
    @PostMapping("/addCredentials")
    public String createLibrarian(@RequestParam("applicationId") int applicationId, Model model,@ModelAttribute("librarian") Librarian librarian) {
    	
    	
    	Application acceptedApplication = libraryService.getApplicationById(applicationId);
    	
    	librarian.setFirstName(acceptedApplication.getFirstName());
    
    	librarian.setLastName(acceptedApplication.getLastName());
        librarian.setId(applicationId);
       
        
        libraryService.getAllLibrarians().add(librarian);
       
        
        libraryService.getAllApplications().remove(acceptedApplication);

        
        String filePath = "credentials";
        
        
        List<String[]> data = new ArrayList<>();
        data.add(new String[] { 
            String.valueOf(librarian.getId()), 
            librarian.getUsername(), 
            librarian.getPassword(),
            "Lib"
        });
        
        
        CsvFileUtil.writeCSV(filePath, data);

 
    	model.addAttribute("message", "Librarian can now log in !! ");
        return "add_librarian"; 
    }
    
    
    
   

    

    
  
    
    
    

}