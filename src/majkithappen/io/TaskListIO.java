/*
 * TaskListIO -- handles saving and loading of TaskListLists to disk
 * Copyright (c) 2016 Michael Wihlborg
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
package majkithappen.io;

import majkithappen.tasks.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.io.BufferedReader;

public class TaskListIO
{
	// Variables
	private static BufferedReader reader;
	private static PrintWriter writer;
	
	// Methods
	/**
	 * Saves a TaskListList to a file on disk
	 * @param file name of file to be created
	 * @param list TaskListList to be saved to disk
	 * @throws IOException is thrown if there is an error with file handling
	 */
	public static void saveTaskListList(String file, TaskListList list) throws IOException
	{
		// Check that TLL is not null
		if (list == null)
			return;
		
		try
		{
			// Open file
			writer = new PrintWriter(file);
			// Write TaskLists
			for (int i = 0; i < list.getNoOfLists(); i++)
			{
				TaskList tl = list.getList(i);
				writer.println("List");
				writer.println(tl.getName());
				// Write Tasks
				for (int j = 0; j < tl.getNoOfTasks(); j++)
				{
					Task t = tl.getTask(j);
					writer.println("Task");
					writer.println(t.getName());
					writer.println("Description");
					writer.println(t.getDescription());
					writer.println("EndDescription");
					writer.println(t.getDone());
					writer.println(t.getDate());
					writer.println("EndTask");
				}
				writer.println("EndList");
			}
		}
		finally
		{
			if (writer != null)
				writer.close();
		}
	}
	
	/**
	 * Loads a TaskListList from a file on disk
	 * @param file name of TaskListList to be loaded
	 * @return a TaskListList, or null if there is an error during loading
	 * @throws IOException is thrown if there is an error with file handling
	 */
	public static TaskListList loadTaskListList(String file) throws IOException
	{
		TaskListList list = new TaskListList();
		try
		{
			// Open file
			reader = new BufferedReader(new FileReader(file));
			
			// Outer loop
			String line = reader.readLine();
			while (line != null)
			{
				// Read in a list
				if (line.equals("List"))
				{
					TaskList tl;
					
					// List name
					line = reader.readLine();
					if (line == null)
						return null;	// Error
					
					tl = new TaskList(line);	// Create TaskList with the given name
					
					// Start reading tasks, or quit if there are no tasks
					line = reader.readLine();
					if (line == null)
						return null;
					
					while (line.equals("Task"))
					{
						// Task loop
						Task t;
						
						// Task name
						line = reader.readLine();
						if (line == null)
							return null;
						
						String name = line;
						
						// Task description
						String desc = "";
						line = reader.readLine();
						if (line == null)
							return null;
						
						if (line.equals("Description"))
						{
							// Description loop
							line = reader.readLine();
							if (line == null)
								return null;
							
							while (!line.equals("EndDescription"))
							{
								desc += line;
								
								line = reader.readLine();
								if (line == null)
									return null;
							}
						}
						else
							return null;	// Error
						
						// Done status
						line = reader.readLine();
						if (line == null)
							return null;
						
						boolean done;
						if (line.equals("true"))
							done = true;
						else
							done = false;
						
						// Due date
						LocalDateTime date;
						
						line = reader.readLine();
						if (line == null)
							return null;
						
						try
						{
							date = LocalDateTime.parse(line);
						}
						catch (DateTimeParseException e)
						{
							return null;	// Error, wasn't a valid date
						}
						
						// End of task
						line = reader.readLine();
						if (line == null)
							return null;
						
						if (!line.equals("EndTask"))
							return null;	// Error, malformed task
						
						// Create task and add it to the list
						t = new Task(name, desc, date);
						if (done)
							t.setDone();
						tl.addTask(t);
						
						// Next line
						line = reader.readLine();
					}
					
					// Add TaskList to TaskListList
					list.newTaskList(tl);
				}
				else
					return null;	// File was not valid
				
				line = reader.readLine();
			}
		}
		finally
		{
			if (reader != null)
				reader.close();
		}
		return list;
	}
}
