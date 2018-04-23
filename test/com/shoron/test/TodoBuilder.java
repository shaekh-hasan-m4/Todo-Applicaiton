package com.shoron.test;

import java.util.Date;

import org.springframework.test.util.ReflectionTestUtils;

import com.shoron.model.Todo;

public class TodoBuilder {

	private Todo todo;
	
	public TodoBuilder() {
		todo = new Todo();
	}

	public TodoBuilder id(int id) {
		
		ReflectionTestUtils.setField(todo, "id", id);
		return this;
	}

	public TodoBuilder user(String user) {
		
		ReflectionTestUtils.setField(todo, "user", user);
		return this;
	}
	

	@Override
	public String toString() {
		
		return "TodoBuilder [todo=" + todo + "]";
	}

	public TodoBuilder description(String description) {
		
		ReflectionTestUtils.setField(todo, "description", description);
		return this;
	}
	
	public TodoBuilder id() {
		
		ReflectionTestUtils.getField(todo, "id");
		return this;
	}

//	public TodoBuilder targetDate(Date date) {
//		ReflectionTestUtils.setField(todo, "date", date);
//		return this;
//	}
//	
//	public TodoBuilder isDone(Boolean isDone) {
//		ReflectionTestUtils.setField(todo, "isDone", isDone);
//		return this;
//	}

	public Todo build() {
		return todo;
	}
}
