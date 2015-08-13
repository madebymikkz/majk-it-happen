/*
 * TaskListList -- handles a collection of TaskLists
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
 * Represents a collection of several task lists
 * @author Michael Wihlborg
 */
public class TaskListList
{
// Variables
	private ArrayList<TaskList> lists;
	
// Constructor
	/**
	 * Creates a new task list
	 */
	public TaskListList()
	{
		lists = new ArrayList<>();
	}
	
// Methods
	/**
	 * Creates a new task list and adds it to the collection
	 * @param listName name of new task list to be created
	 */
	public void newTaskList(String listName)
	{
		lists.add(new TaskList(listName));
	}
	
// Get-set methods
	/**
	 * Returns number of task lists in collection
	 * @return no of task lists in collection
	 */
	public int getNoOfLists()
	{
		return lists.size();
	}
	
	/**
	 * Retrieves a task list with a specified name
	 * @param list name of task list to be retrieved
	 * @return requested task list; null if no such task list
	 */
	public TaskList getList(String list)
	{
	// Sequentially step through lists until we find one with a matching name
		for (TaskList l : lists)
		{
			if (l.getName().equals(list))
			{
				return l;
			}
		}
		return null;	// return null if no list has that name
	}
	
	/**
	 * Retrieves task list with specified position in collection
	 * @param list position of task list to be retrieved
	 * @return requested task list; null if no task list in requested position
	 */
	public TaskList getList(int list)
	{
		if (list < 0 || list >= lists.size())
			return null;	// throw error later
		return lists.get(list);
	}
}
