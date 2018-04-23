package com.shoron.test;

import static org.mockito.Mockito.mock;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.shoron.model.Todo;
import com.shoron.service.TodoService;

import junit.framework.TestCase;


@RunWith(MockitoJUnitRunner.class)
public class TestTodoService extends TestCase {

	
	@Mock
	TodoService todoService;	

	@Before
	public void setUp(){
		todoService = new TodoService();
	}	
		
	@Test
	public void testRetriveTodo(){	
		
		List<Todo> check = todoService.retrieveTodos("shoron");
		
		assertEquals(3,check.size());
	}
	
	@Test
	public void testDeleteTodo(){	
		
		todoService.deleteTodo(3);
		
		List<Todo> check = todoService.retrieveTodos("shoron");
		
		assertEquals(3,check.size());
	}
	
	@Test
	public void testAddTodo(){		
		todoService.addTodo("shoron", "hello_ world", new Date(0), true);
		
		List<Todo> check = todoService.retrieveTodos("shoron");
		
		assertEquals(4, check.size());
	}
	
	@Test
	public void testUpdateTodo(){
		
		Todo todo = new Todo(3, "shoron", "hello y", new Date(), true);
		
		todoService.updateTodo(todo);
		
		List<Todo> check=todoService.retrieveTodos("shoron");
		
		assertEquals(4,check.size());
	}
}
