/*
 * MIHTextClient -- a text mode interface for Majk It Happen
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
package majkithappen.ui;

import java.util.Scanner;
import java.time.*;
import java.util.ArrayList;
import majkithappen.tasks.*;

public class MIHTextClient
{
// Global variables
	private static TaskListList lists;
	private static Scanner scan;
	
// Main method
	public static void main(String[] args)
	{
	// Create variables
		lists = new TaskListList();
		scan = new Scanner(System.in);
		String input = "";
	// Show title
		System.out.println("M A J K   I T   H A P P E N");
	// Main loop
		while (true)
		{
		// Show menu
			System.out.println("1 - show task lists");
			System.out.println("2 - add task list");
			System.out.println("3 - show tasks and task lists");
			System.out.println("4 - add task to task list");
			System.out.println("5 - mark task as done");
			System.out.println("6 - quit");
		// Get choice
			System.out.print("Choice: ");
			input = scan.nextLine().trim();
		// Handle choice
			switch (input)
			{
				case "1":
					printLists();
					break;
				case "2":
					newList();
					break;
				case "3":
					printTasks();
					break;
				case "4":
					newTask();
					break;
				case "5":
					markDoneTask();
					break;
				case "6":
					return;
				default:
					System.out.printf("%s is not a valid choice!%n", input);
			}
		}
	}
	
// Helper methods
	private static void printLists()
	{
		int noOfLists = lists.getNoOfLists();
		if (noOfLists > 0)	// Only print if there are lists
		{
			System.out.println("Lists:");
			for (int i = 0; i < noOfLists; i++)
				System.out.printf("\t%s%n", lists.getList(i).getName());
		}
		else
			System.out.println("No lists!");
	}
	
	private static void printTasks()
	{
		System.out.println("Show tasks:");
	// Check number of lists
		int noOfLists = lists.getNoOfLists();
		if (noOfLists < 1)
		{
			System.out.println("No lists!");
			return;
		}
	// Go through lists
		for (int i = 0; i < noOfLists; i++)
		{
		// Print name of list
			TaskList l = lists.getList(i);
			System.out.printf("\t%s:%n", l.getName());
		// Print content of list
			for (int j = 0; j < l.getNoOfTasks(); j++)
			{
				Task t = l.getTask(j);
				System.out.printf("\t\t%s ", t.getName());
				int days = t.getDaysRemaining();
				if (days < 1)
				{
					int hours = t.getHoursRemaining();
					if (hours < 1)
					{
						int mins = t.getMinutesRemaining();
						System.out.printf("%d minuter kvar ", hours);
					}
					else
						System.out.printf("%d timmar kvar ", hours);
				}
				else
					System.out.printf("%d dagar kvar ", days);
				if (l.getTask(j).getDone())
					System.out.println("Done");
				else
					System.out.println("Not done");
			}
		}
	}
	
	private static void newList()
	{
	// Read data
		System.out.println("New list:");
		System.out.print("Name: ");
		String input = scan.nextLine().trim();
	// Create list
		lists.newTaskList(input);
	}
	
	private static void newTask()
	{
	// Get list
		System.out.println("New task:");
		System.out.print("List: ");
		String list = scan.nextLine().trim();
		TaskList l = lists.getList(list);
		if (l == null)
		{
			System.out.println("No such list!");
			return;
		}
	// Get details for task
		System.out.print("Name: ");
		String name = scan.nextLine().trim();
		System.out.print("Description: ");
		String desc = scan.nextLine().trim();
		try
		{
		// Get date for task
			System.out.print("Year: ");
			int year = Integer.parseInt(scan.nextLine().trim());
			System.out.print("Month: ");
			int month = Integer.parseInt(scan.nextLine().trim());
			System.out.print("Day: ");
			int day = Integer.parseInt(scan.nextLine().trim());
			System.out.print("Hour: ");
			int hour = Integer.parseInt(scan.nextLine().trim());
			System.out.print("Minute: ");
			int minute = Integer.parseInt(scan.nextLine().trim());
			LocalDateTime newDate = LocalDateTime.of(year, month, day, hour, minute);
		// Add task
			l.addTask(new Task(name, desc, newDate));
		}
		catch (NumberFormatException e)
		{
			System.out.println("Not a number!");
			return;
		}
	}
	
	private static void markDoneTask()
	{
	// Get list
		System.out.println("Mark done task:");
		System.out.print("List: ");
		String list = scan.nextLine().trim();
		TaskList l = lists.getList(list);
		if (l == null)
		{
			System.out.println("No such list!");
			return;
		}
	// Get task
		System.out.print("Name: ");
		String name = scan.nextLine().trim();
	// Mark task
		Task t = l.getTask(name);
		if (t == null)
		{
			System.out.println("No such task!");
			return;
		}
		t.setDone();
	}
}
