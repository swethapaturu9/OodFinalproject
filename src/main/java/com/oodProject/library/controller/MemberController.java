package com.oodProject.library.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oodProject.library.pojo.Book;
import com.oodProject.library.pojo.Librarian;
import com.oodProject.library.pojo.Library;
import com.oodProject.library.pojo.Member;
import com.oodProject.library.pojo.PrivateRoom;
import com.oodProject.library.util.CsvFileUtil;

import jakarta.servlet.http.HttpSession;


@Controller
public class MemberController {
	


	@Autowired
	private final Library libraryService;

    public MemberController(Library libraryService) {
        this.libraryService = libraryService;
    }
    
    
    @PostMapping("/MemberLogin") 
	public String loginCheck(@RequestParam String username,HttpSession session, @RequestParam String password, Model model) {
		
    	
    	
    	if (libraryService.authenticateMember(username, password)) {
    		
			Member member = libraryService.getMemberByusername(username);
			
			
			session.setAttribute("member", member);
			
			System.out.println(member.getFirstName());
			
			System.out.println(member.getBooksBorrowed().size());
			
		    List<Book> returnRequests = member.getReturnRequests();
		
		    List<Book> borrowRequests = member.getBorrowRequests();	
			
		    List<Book> borrowedBooks = member.getBooksBorrowed();
			
			
	        List<Book> books = libraryService.getAllBooks();
			
		    List<PrivateRoom> rooms = member.getRoomsBooked();
		    
		    List<Librarian> librarians = libraryService.getAllLibrarians();
			
		
			model.addAttribute("books",books);
			
			model.addAttribute("borrowRequests",borrowRequests);
			model.addAttribute("returnRequests",returnRequests);
			
			model.addAttribute("borrowedBooks", borrowedBooks);
			
			model.addAttribute("rooms", rooms);

			
			model.addAttribute("member",member);
			
			model.addAttribute("librarians", librarians);
			
			
			
			return "member_home";		
     	   
     } 
    	
    	model.addAttribute("errorMessage", "Invalid credentials");
          return "member_login"; 
			
     	   
     }
    
    
    @GetMapping("/MemberSignup") 
    public String memberSignup( Model model) {
    	
    	Member member = new Member();
    	
    	model.addAttribute("member",member);
    	
    	return "member_signup";
    	
    }
    
    
    @PostMapping("/MemberSignUp")
    public String memberRegister(@ModelAttribute("member") Member member,Model model) {
    	
    	
    	libraryService.addMember(member);
    	
    	
    	 String filePath = "credentials";
         
         
         List<String[]> data = new ArrayList<>();
         data.add(new String[] { 
             String.valueOf(member.getMemberId()), 
             member.getUsername(), 
             member.getPassword(),
             "Member"
         });
        
         CsvFileUtil.writeCSV(filePath, data);
    	
    	model.addAttribute("message","Succefully signed up");
    	
    	return "member_signup";
    	
  
    	
    }
    
    
    @PostMapping("/borrowBook")
    public String borrowBook(@RequestParam("bookId") int bookId,HttpSession session, @RequestParam("librarianId") int librarianId,  Model model) {
    	
    	
    	Member member = (Member) session.getAttribute("member");
    	
    	Book book = libraryService.getBookByid(bookId);
    	
    	book.setBorrowedBy(member);
    	
    	Librarian librarian = libraryService.getLibrarianById(librarianId);
    	
    	librarian.getBorrowRequests().add(book);
    	
    	member.getBorrowRequests().add(book);

    	
    	return "success_page";
    	
    }
	
	

}
