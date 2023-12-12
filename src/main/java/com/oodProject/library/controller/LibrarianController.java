package com.oodProject.library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.oodProject.library.pojo.Application;
import com.oodProject.library.pojo.Book;
import com.oodProject.library.pojo.Librarian;
import com.oodProject.library.pojo.Library;
import com.oodProject.library.pojo.Person;

import jakarta.servlet.http.HttpServletRequest;


@Controller
public class LibrarianController {
	
	
	@Autowired
	private final Library libraryService;

    public LibrarianController(Library libraryService) {
        this.libraryService = libraryService;
    }
	
	@GetMapping("/applyLibrarian")
	public String applyLibrarian(Model model) {
		
		
		Application application = new Application();
		
		
		
		model.addAttribute("application", application);
	        return "apply_Librarian";
	}
	
	
	@PostMapping("/applyLibrarian")
    public String createLibrarian(@ModelAttribute("application") Application application, Model model) {
		
		
	    libraryService.addApplication(application);
	    
	   
	    for(Application app: libraryService.getAllApplications()) {
	    	
	    	System.out.println(app.getId());
	    	
	    }
		
		model.addAttribute("message", "Application submitted successfully!");

        return "apply_Librarian";
		
		
	}
	
	
	@PostMapping("/LibrarianLogin") 
	public String loginCheck(@RequestParam String username, @RequestParam String password, Model model, HttpServletRequest request) {
		
		if (libraryService.authenticateLibrarian(username, password)) {
			
			Librarian librarian = libraryService.getLibrarianByusername(username);
			Person librarianPersonType = (Person)librarian;
			librarianPersonType.setRole("Lib");
			request.getSession().setAttribute("user", librarianPersonType);
			
			
			List<Book> returnRequests = libraryService.getBorrowRequests(librarian);
			
			List<Book> borrowRequests = libraryService.getReturnRequests(librarian);
			
		
			model.addAttribute("borrowRequests",borrowRequests);
			model.addAttribute("returnRequests",returnRequests);
			model.addAttribute("librarian",librarian);
			
			return "librarian_home";
			
     	   
     } 
     	
       model.addAttribute("errorMessage", "Invalid credentials");
       return "librarian_login"; 
    
		
	}
	
	
	

}
