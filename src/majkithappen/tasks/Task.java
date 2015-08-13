/*
 * Task -- represents a task in the tasklist
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

import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 * Represents a task
 * @author Michael Wihlborg
 */
public class Task
{
// Member variables
	private String name;
	private String description;
	private boolean done;
	private LocalDateTime date;
	
// Constructor
	/**
	 * Creates a new task
	 * @param name Name of the new task
	 * @param description Description of the new task
	 * @param date Date the task is due to be done
	 */
	public Task(String name, String description, LocalDateTime date)
	{
	// Initialize variables
		this.name = name;
		this.description = description;
		done = false;
		this.date = date;
	}
	
// Methods
	/**
	 * Marks the task as done
	 */
	public void setDone()
	{
		done = true;
	}
	
	/**
	 * Returns the task's name
	 * @return name of task
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Returns the task's description
	 * @return description of task
	 */
	public String getDescription()
	{
		return description;
	}
	
	/**
	 * Returns wether the task is marked as done
	 * @return true if task is done; false if task is not done
	 */
	public boolean getDone()
	{
		return done;
	}
	
// Date functions
	/**
	 * Returns the number of days remaining in the tasks due date
	 * @return no of days until task is due
	 */
	public int getDaysRemaining()
	{
		return (int)LocalDateTime.now().until(date, ChronoUnit.DAYS);
	}
	
	/**
	 * Returns the number of hours remaining in the tasks due date
	 * @return no of hours until task is due
	 */
	public int getHoursRemaining()
	{
		return (int)LocalDateTime.now().until(date, ChronoUnit.HOURS);
	}
	
	/**
	 * Returns the number of minutes remaining in the tasks due date
	 * @return no of minutes until task is due
	 */
	public int getMinutesRemaining()
	{
		return (int)LocalDateTime.now().until(date, ChronoUnit.MINUTES);
	}
	
	/**
	 * Returns the number of seconds remaining in the tasks due date
	 * @return no of seconds until task is due
	 */
	public int getSecondsRemaining()
	{
		return (int)LocalDateTime.now().until(date, ChronoUnit.SECONDS);
	}
	
	/**
	 * Returns the due date as a String
	 * @return task's due date
	 */
	public String getDate()	// debug
	{
		return date.toString();
	}
}
