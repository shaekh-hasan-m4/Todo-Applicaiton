package com.shoron.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.shoron.model.Todo;

@Service
public class TodoService {

	private static List<Todo> todoList = new ArrayList<Todo>();
	private static int numberOfTodos = 3;

	static {
		todoList.add(new Todo(1, "shoron", "Wake up early in the morning", new Date(), false));
		todoList.add(new Todo(2, "shoron", "Do some home workuout and running", new Date(), false));
		todoList.add(new Todo(3, "shoron", "Go to office for work", new Date(), false));
		todoList.add(new Todo(1, "hasan", "Learn Java", new Date(), false));
	}
	
	
	public void addTodo(String name, String desc, Date targetDate, boolean isDone) {
		numberOfTodos = numberOfTodos + 1;
		todoList.add(new Todo(numberOfTodos, name, desc, targetDate, isDone));
	}

	public void deleteTodo(int id) {
		Iterator<Todo> iterate = todoList.iterator();
		while (iterate.hasNext()) {
			Todo todo = iterate.next();
			if (todo.getId() == id) {
				iterate.remove();
			}
		}
	}

	public List<Todo> retrieveTodos(String user) {
		List<Todo> listOfTodos = new ArrayList<Todo>();
		for (Todo todo : todoList) {
			if (todo.getUser().equals(user)) {
				listOfTodos.add(todo);
			}
		}
		return listOfTodos;
	}

	public Todo retrieveSingleTodo(int id) {
		for (Todo todo : todoList) {
			if (todo.getId() == id)
				return todo;
		}
		return null;
	}

	public void updateTodo(Todo todo) {
		deleteTodo(todo.getId());
		todoList.add(todo);
	}
}
