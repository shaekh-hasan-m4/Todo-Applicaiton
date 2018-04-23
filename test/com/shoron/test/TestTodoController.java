package com.shoron.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.swing.Spring;
import javax.xml.ws.Service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.shoron.controller.TodoController;
import com.shoron.model.Todo;
import com.shoron.security.SecurityConfiguration;
import com.shoron.service.TodoService;

import junit.framework.TestCase;

import static org.hamcrest.Matchers.*;



@RunWith(SpringRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/servlet-dispatcher.xml" })
@WebAppConfiguration
public class TestTodoController{
	

	  private MockMvc mockMvc;
	  
	  private TodoController mockTodoContontroller;
	  
	  private Todo mockTodo;
	  
	  @Mock
	  private Todo todo;

	  @Mock
	  private SecurityConfiguration securityConfig;

	  @Mock
	  private TodoService service;
	  
	  @InjectMocks
	  private TodoController controller;	  

	  @Before
	  public void setup() {		
		mockTodoContontroller = Mockito.mock(TodoController.class);
		  
		mockTodo = Mockito.mock(Todo.class);	
		  
	    MockitoAnnotations.initMocks(this); // Initialize according to annotations
	    mockMvc = MockMvcBuilders.standaloneSetup(controller)
	    		.build();
	  }

	
	@Test
	public void checkTest() throws Exception{
  
        mockMvc.perform(get("/check"))
	                .andExpect(status().isOk())
	                .andExpect(view().name("welcome"))
	                .andExpect(forwardedUrl("welcome"));
	}
	
//	@Test
//	@WithMockUser
//	public void loginTest() throws Exception{
//
//        mockMvc.perform(get("/login"))
//                .andExpect(status().isOk());
//	}
//	
	
	@Test
	@WithMockUser
	public void indexPageTest() throws Exception{

		when(mockTodoContontroller.retriveLoggedinUserName()).thenReturn("shoron");
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser("shoron")
	public void listOfTodosTest() throws Exception{
		
		Todo todo3 = new TodoBuilder()
					.id(1)
					.user("shoron")
					.description("Hello World")
					.build();		
		
		Todo todo1 = new Todo(1, "shoron", "hello", new Date(),true);
		
		Todo todo2 = new Todo(2, "hasan", "hello", new Date(),true);
	
		when(mockTodoContontroller.retriveLoggedinUserName()).thenReturn("shoron");
		
		when(service.retrieveTodos("shoron")).thenReturn(Arrays.asList(todo3));
		
        mockMvc.perform(get("/listtodo"))
                .andExpect(status().isOk())
                .andExpect(view().name("list-todo")) 
                .andExpect(forwardedUrl("list-todo"))
                .andExpect(model().attribute("todoList", hasSize(1)));
        
        
       assertThat(todo1, hasProperty("description"));
       assertThat(todo1, hasProperty("id", is(1)));
	}
	
	@Test
	@WithMockUser
	public void addTodoPageTest() throws Exception{		
	
		mockMvc.perform(get("/addtodo"))
                .andExpect(status().isOk());
                
	}
	
	@Test
	@WithMockUser("shoron")
	public void addTodoTest() throws Exception{		
		
		Todo todo = new Todo(1, "shoron", "hello", new Date(),true);		
		
		when(mockTodoContontroller.retriveLoggedinUserName()).thenReturn("shoron");
  
        when(mockTodo.getDescription()).thenReturn(todo.getDescription());
        
        when(mockTodo.getTargetDate()).thenReturn(todo.getTargetDate());
        
        when(mockTodo.isDone()).thenReturn(todo.isDone());
        
		mockMvc.perform(post("/addtodo")
				.param("description", "hello"))
		  .andExpect(status().isOk());
                
	}
	
	@Test
	@WithMockUser
	public void updateTodoPageTest() throws Exception{		
		
		Todo todo = new Todo(1, "shoron", "hello", new Date(),true);
		
        when(mockTodo.isDone()).thenReturn(todo.isDone());
        
        when(service.retrieveSingleTodo(1)).thenReturn(todo);        
        
		mockMvc.perform(get("/updatetodo/?id=1"))
				.andExpect(status().isOk());
                
	}
	
	@Test
	@WithMockUser
	public void deleteTodoTest() throws Exception{		
    
        mockMvc.perform(get("/deletetodo/"))
                .andExpect(status().isNotFound());
                
	}
	
	@Test
	public void check(){
		
	}

}
