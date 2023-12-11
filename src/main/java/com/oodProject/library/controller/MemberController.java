package com.oodProject.library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.oodProject.library.pojo.Book;
import com.oodProject.library.pojo.Librarian;
import com.oodProject.library.pojo.Library;
import com.oodProject.library.pojo.Member;

public class MemberController {
	


	@Autowired
	private final Library libraryService;

    public MemberController(Library libraryService) {
        this.libraryService = libraryService;
    }
    
    
    @PostMapping("/MemberLogin") 
	public String loginCheck(@RequestParam String username, @RequestParam String password, Model model) {
		
		if (libraryService.authenticateMember(username, password)) {
		
			Librarian librarian = libraryService.getLibrarianByusername(username);
			
			
			List<Book> returnRequests = libraryService.getBorrowRequests(librarian);
			
			List<Book> borrowRequests = libraryService.getReturnRequests(librarian);
			
		
			model.addAttribute("borrowRequests",borrowRequests);
			model.addAttribute("returnRequests",returnRequests);
			model.addAttribute("librarian",librarian);
			
			return "member_home";
			
     	   
     } 
     	
       model.addAttribute("errorMessage", "Invalid credentials");
       return "member_login"; 
    
		
	}
    
    
    @GetMapping("/MemberSignup") 
    public String memberSignup(Model model) {
    	
    	Member member = new Member();
    	
    	model.addAttribute("member",member);
    	
    	return "member_signup";
    	
    	
    }
	
	

}
