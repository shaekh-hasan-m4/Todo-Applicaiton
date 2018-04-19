package com.shoron.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shoron.service.TodoService;
import com.shoron.model.Todo;

@Controller
public class TodoController {

	@Autowired
	TodoService todoService;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
	
	
	// Welcome Page
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage() {
		return "login";
	}

	// Retriving Username
	private String retriveLoggedinUserName() {

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			return ((UserDetails) principal).getUsername();
		}
		return principal.toString();
	}

	// Welcome Page
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String indexPage(ModelMap model) {
		model.addAttribute("name", retriveLoggedinUserName());
		return "welcome";
	}

	// List of todos page
	@RequestMapping(value = "/listtodo", method = RequestMethod.GET)
	public String listOfTodos(Model model) {
		model.addAttribute("name", retriveLoggedinUserName());
		model.addAttribute("todoList", todoService.retrieveTodos(retriveLoggedinUserName()));
		return "list-todo";
	}

	
	// add todo page
	@RequestMapping(value = "/addtodo", method = RequestMethod.GET)
	public String addTodoPage(Model model) {
		model.addAttribute("todo", new Todo());
		return "add-todo";
	}

	// add todo
	@RequestMapping(value = "/addtodo", method = RequestMethod.POST)
	public String addTodo(ModelMap model, @Valid Todo todo, BindingResult result,
			RedirectAttributes redirectAttributes) {

		if (todo.getTargetDate() == null && todo.isDone() == true) {

			todoService.addTodo(retriveLoggedinUserName(), todo.getDescription(), new Date(0), todo.isDone());
		
		} else {
			redirectAttributes.addFlashAttribute("message", "Failed");
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
			
			if (result.hasErrors()) {
				model.addAttribute("date_format_error", "date format is dd/MM/yyyy");
				return "add-todo";
			}
			todoService.addTodo(retriveLoggedinUserName(), todo.getDescription(), todo.getTargetDate(), todo.isDone());
		}

		redirectAttributes.addFlashAttribute("message", "Successfully Added");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");

		model.clear(); // for not letting any extra parameter
		return "redirect:/listtodo";
	}	
		

	// delete todo
	@RequestMapping(value = "/deletetodo", method = RequestMethod.GET)
	public String deleteTodo(ModelMap model, @RequestParam int id, RedirectAttributes redirectAttributes) {
		todoService.deleteTodo(id);
		redirectAttributes.addFlashAttribute("message", "Successfully Deleted");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");
		model.clear();
		return "redirect:/listtodo";
	}	
	
	

	// update todo page
	@RequestMapping(value = "/updatetodo", method = RequestMethod.GET)
	public String getSingleTodo(ModelMap model, @RequestParam int id) {
		Todo todo = todoService.retrieveSingleTodo(id);
		model.clear();
		model.addAttribute("isDoneCheck", todo.isDone());
		model.addAttribute("todo", todo);
		return "update-todo";
	}
	
	
	// update todo
	@RequestMapping(value = "/updatetodo", method = RequestMethod.POST)
	public String updateTodo(ModelMap model, @Valid Todo todo, BindingResult result,
			RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", "Failed");
		redirectAttributes.addFlashAttribute("alertClass", "alert-danger");

		if (result.hasErrors()) {
			return "update-todo";
		}
		redirectAttributes.addFlashAttribute("message", "Successfully Updated");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");
		model.clear();
		todoService.updateTodo(todo);
		return "redirect:/listtodo";
	}	
	
	// logout
	@RequestMapping(value = "/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {

		org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
			request.getSession().invalidate();
		}

		return "redirect:/";
	}

}
