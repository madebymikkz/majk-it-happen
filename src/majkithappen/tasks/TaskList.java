/*
 * TaskList -- a named list of tasks
 * Copyright (c) 2015 Michael Wihlborg
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package majkithappen.tasks;

import java.util.ArrayList;

/**
 * Represents a list of tasks
 * @author Michael Wihlborg
 */
public class TaskList
{
// Member variables
	private String name;
	private ArrayList<Task> tasklist;
	
// Constructor
	/**
	 * Creates a new task list
	 * @param name name of the new task list
	 */
	public TaskList(String name)
	{
	// Initialize variables
		this.name = name;
		tasklist = new ArrayList<>();
	}
	
// Methods
	/**
	 * Adds a task to the task list
	 * @param t Task to be added
	 */
	public void addTask(Task t)
	{
	// Add task
		tasklist.add(t);
	}
	
	/**
	 * Returns the task list's name
	 * @return name of task list
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Marks a task in the list as done
	 * @param taskName name of Task to be marked as done
	 */
	public void setDoneTask(String taskName)
	{
	// Get task
		int i = findTaskIndex(taskName);
		if (i < 0)
			return;	// if no task by that name, error
		Task t = tasklist.get(i);
	// Set task done
		t.setDone();
	}
	
	/**
	 * Retrieves a task from the task list at a certain position
	 * @param taskId position of task to be retrieved in task list
	 * @return task at position; null if no task in that position
	 */
	public Task getTask(int taskId)
	{
		if (taskId < 0)
			return null;	// if no task, throw error
		return tasklist.get(taskId);
	}
	
	/**
	 * Retrieves a task from the task list with a certain name
	 * @param taskName name of task to be retrieved
	 * @return requested task; null if no task with that name
	 */
	public Task getTask(String taskName)
	{
		int i = findTaskIndex(taskName);
		if (i < 0)
			return null;	// if no such task, error
		return tasklist.get(i);
	}
	
	/**
	 * Returns the number of tasks in the task lsit
	 * @return no of tasks in task list
	 */
	public int getNoOfTasks()
	{
		return tasklist.size();
	}
	
	/**
	 * Returns the position of a task with a certain name
	 * @param taskName name of task to get position of
	 * @return position of task; null if no such task
	 */
	private int findTaskIndex(String taskName)
	{
		for (int i = 0; i < tasklist.size(); i++)
		{
		// Get name of task
			Task t = tasklist.get(i);
			String n = t.getName();
		// Compare names
			if (n.equals(taskName))
				return i;
		}

		return -1;	// If no task with that name
	}
}
