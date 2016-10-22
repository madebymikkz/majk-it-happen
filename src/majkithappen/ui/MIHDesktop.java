/*
 * MIHDesktop -- the Majk It Happen desktop client
 * Copyright (c) 2015, 2016 Michael Wihlborg
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

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import majkithappen.io.TaskListIO;
import majkithappen.tasks.*;
import madebymikkz.simpledateinput.*;

/**
 * "Majk It Happen" GUI client
 * @author Michael Wihlborg
 */
public class MIHDesktop extends JFrame
{
/* ******************************* Variables ******************************* */
	protected TaskListList list = new TaskListList();
	private JComboBox<String> tlBox;
	private DefaultListModel<String> taskLstMdl = new DefaultListModel<>();
	private JButton newTaskBtn, setDoneBtn;
	private String selectedList = null;
	private JLabel nameLbl, descLbl, dateLbl;
	private JList<String> taskLst;
	private JFileChooser fc = new JFileChooser();
	private boolean unsavedChanges = false;
	
/* ****************************** Constructor ****************************** */
	/**
	 * Creates the program window
	 */
	public MIHDesktop()
	{
		super("Majk It Happen");
		
	// Menu bar
		JMenuBar menu = new JMenuBar();
	// Menu bar components
		JMenu fileMenu = new JMenu("File");
		JMenuItem saveItem = new JMenuItem("Save");
		saveItem.addActionListener(new SaveLnr());
		JMenuItem loadItem = new JMenuItem("Load");
		loadItem.addActionListener(new OpenLnr());
		JMenuItem quitItem = new JMenuItem("Quit");
		quitItem.addActionListener(new ExitLnr());
	// Create menu bar
		fileMenu.add(saveItem);
		fileMenu.add(loadItem);
		fileMenu.addSeparator();
		fileMenu.add(quitItem);
		menu.add(fileMenu);
		setJMenuBar(menu);
		
	// Top panel
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
	// Top panel components
		tlBox = new JComboBox<>();
		tlBox.addActionListener(new TlBoxLnr());
		topPanel.add(tlBox);
		populateBox();
		JButton newListBtn = new JButton("New task list");
		newListBtn.addActionListener(new NewListLnr());
		topPanel.add(newListBtn);
		
	// Left panel
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setBorder(BorderFactory.createTitledBorder("Tasks"));
		leftPanel.setPreferredSize(new Dimension(200, 400));
	// Left panel components
		taskLst = new JList<>(taskLstMdl);
		taskLst.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		taskLst.addListSelectionListener(new TaskLstLnr());
		JScrollPane taskLstScr = new JScrollPane(taskLst);
		taskLstScr.setPreferredSize(new Dimension(200, 400));
		leftPanel.add(taskLstScr);
		
	// Center panel
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		centerPanel.setBorder(BorderFactory.createTitledBorder("Task description"));
		centerPanel.setPreferredSize(new Dimension(200, 400));
	// Center panel components
		nameLbl = new JLabel("Name:");
		centerPanel.add(nameLbl);
		descLbl = new JLabel("Description:");
		centerPanel.add(descLbl);
	/*	JTextArea descTxa = new JTextArea();
		descTxa.setEditable(false);
		centerPanel.add(descTxa); */
		dateLbl = new JLabel("Due:");
		centerPanel.add(dateLbl);
		
	// Right panel
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.setPreferredSize(new Dimension(150, 400));
	// Right panel components
		newTaskBtn = new JButton("Add new task");
		newTaskBtn.addActionListener(new NewTaskLnr());
		newTaskBtn.setEnabled(false);
		rightPanel.add(newTaskBtn);
		setDoneBtn = new JButton("Mark task as done");
		setDoneBtn.addActionListener(new SetDoneLnr());
		setDoneBtn.setEnabled(false);
		rightPanel.add(setDoneBtn);
		
	// Add panels to window
		add(topPanel, BorderLayout.NORTH);
		add(leftPanel, BorderLayout.WEST);
		add(centerPanel, BorderLayout.CENTER);
		add(rightPanel, BorderLayout.EAST);
		
	// Add display update timer
		Timer t = new Timer(1000, new DateLnr());
		t.start();
		
	// Prepare window
		pack();
		setResizable(false);
		addWindowListener(new CloseLnr());
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(true);
	}
	
/* ******************************** Methods ******************************** */
	/**
	 * Creates the program window
	 * @param args command line arguments
	 */
	public static void main(String[] args)
	{
		new MIHDesktop();
	}
	
	/**
	 * Updates the lists in the task list JComboBox
	 */
	private void populateBox()
	{
		int noOfLists = list.getNoOfLists();
	// clear current items in box
		tlBox.removeAllItems();
	// if there are any lists, add them to the combo box
		for (int i = 0; i < noOfLists; i++)
			tlBox.addItem(list.getList(i).getName());
	}
	
	/**
	 * Updates the list of tasks in the task JList
	 * @param l the TaskList whose tasks are to be displayed
	 */
	private void populateLst(TaskList l)
	{
	// don't crash if not given a task list
		if (l == null)
			return;
		
		int noOfTasks = l.getNoOfTasks();
	// clear list of tasks
		taskLstMdl.clear();
	// add all tasks from given task list
		if (noOfTasks > 0)
			for (int i = 0; i < noOfTasks; i++)
			{
				Task t = l.getTask(i);
				taskLstMdl.addElement(t.getName());
			}
	}
	
	/**
	 * Returns the currently selected task list
	 * @return the currently selected TaskList; null if no selected task list
	 */
	private TaskList getSelectedList()
	{
		if (selectedList != null)
		{
			return list.getList(selectedList);
		}
		else
		{
			return null;	// add error handling
		}
	}
	
	/**
	 * Returns the selected task
	 * @return the currently selected Task; null if no selected task
	 */
	private Task getSelectedTask()
	{
		String selection = taskLst.getSelectedValue();
		if (selection != null)
		{
			return getSelectedList().getTask(selection);
		}
		else
			return null;
	}
	
	/**
	 * Ends the program
	 */
	private void exitProgram()
	{
		// Warn if unsaved changes
		if (unsavedChanges)
		{
			int yn = JOptionPane.showConfirmDialog(MIHDesktop.this, "All unsaved changes will be lost. Do you want to continue?", "Warning", JOptionPane.YES_NO_OPTION);
			if (yn == JOptionPane.NO_OPTION)
				return;
		}
		System.exit(0);
	}
	
/* ******************************* Listeners ******************************* */
	/**
	 * Listener class for the "New List" button
	 * @author Michael Wihlborg
	 */
	private class NewListLnr implements ActionListener
	{
		/**
		 * Creates a new list with data supplied by the user
		 * @param ave the ActionEvent that triggered the listener 
		 */
		public void actionPerformed(ActionEvent ave)
		{
		// New List panel
			JPanel newListPnl = new JPanel();
			newListPnl.setLayout(new BoxLayout(newListPnl, BoxLayout.Y_AXIS));
			JPanel firstRowPnl = new JPanel();
			JLabel newListNameLbl = new JLabel("Name:");
			JTextField newListNameTxf = new JTextField(20);
			firstRowPnl.add(newListNameLbl);
			firstRowPnl.add(newListNameTxf);
			newListPnl.add(firstRowPnl);
			
		// Show panel
			int answer = JOptionPane.showConfirmDialog(MIHDesktop.this, newListPnl, "New list", JOptionPane.OK_CANCEL_OPTION);
		// Check result
			if (answer != JOptionPane.YES_OPTION)
				return;
			String newListName = newListNameTxf.getText().trim();
			if (newListName.length() < 1)
			{
				JOptionPane.showMessageDialog(MIHDesktop.this, "Name must not be empty!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
		// Create new list
			list.newTaskList(newListName);
			
		// Update
			populateBox();
			unsavedChanges = true;
		}
	}
	
	/**
	 * Listener class for the list selection JComboBox
	 * @author Michael Wihlborg
	 */
	private class TlBoxLnr implements ActionListener
	{
		/**
		 * Shows tasks in the selected task list, if any
		 * @param ave the ActionEvent that triggered the listener
		 */
		public void actionPerformed(ActionEvent ave)
		{
			if (list.getNoOfLists() > 0)	// Check if there are any task lists
			{
			// Get selected task name
				String listName = (String)tlBox.getSelectedItem();
				selectedList = listName;
			// Show tasks in list
				populateLst(list.getList(listName));
				newTaskBtn.setEnabled(true);
			}
		}
	}
	
	/**
	 * Listener for the "New Task" JButton
	 * @author Michael Wihlborg
	 */
	private class NewTaskLnr implements ActionListener
	{
		/**
		 * Gets data for new task from the user, and then creates the new task
		 * @param ave the ActionEvent that triggered the listener
		 */
		public void actionPerformed(ActionEvent ave)
		{
		// Create popup panel
			JPanel newTaskPnl = new JPanel();
			newTaskPnl.setLayout(new BoxLayout(newTaskPnl, BoxLayout.Y_AXIS));
		// First row
			JPanel row1 = new JPanel();
			JLabel nameLbl = new JLabel("Name:");
			JTextField nameTxf = new JTextField(10);
			row1.add(nameLbl);
			row1.add(nameTxf);
		// Second row
			JPanel row2 = new JPanel();
			JLabel descLbl = new JLabel("Description");
			JTextField descTxf = new JTextField(10);
			row2.add(descLbl);
			row2.add(descTxf);
		// Date/time row
			SimpleDateInput row3 = new SimpleDateInput();
		// Add rows to panel
			newTaskPnl.add(row1);
			newTaskPnl.add(row2);
			newTaskPnl.add(row3);
			
		// Show panel
			int answer = JOptionPane.showConfirmDialog(MIHDesktop.this, newTaskPnl, "New task", JOptionPane.OK_CANCEL_OPTION);
		// Check result
			if (answer != JOptionPane.YES_OPTION)
				return;
		// Get and parse results from panel
			String newName = nameTxf.getText().trim();
			String newDesc = descTxf.getText().trim();
			String newDate = row3.getDate();
			String newTime = row3.getTime();			
			if (newName.length() < 1)	// Error checking
			{
				JOptionPane.showMessageDialog(MIHDesktop.this, "Name must not be empty!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			try
			{
				LocalDateTime newDateTime = LocalDateTime.parse(String.format("%sT%s:00", newDate, newTime));
				
			// Create new task
				getSelectedList().addTask(new Task(newName, newDesc, newDateTime));
				populateLst(getSelectedList());
				unsavedChanges = true;
			}
			catch (DateTimeParseException e)	// Error checking
			{
				JOptionPane.showMessageDialog(MIHDesktop.this, "Malformed date or time", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
	}
	
	/**
	 * Listener class for the JList of tasks
	 * @author Michael Wihlborg
	 */
	private class TaskLstLnr implements ListSelectionListener
	{
		/**
		 * Shows the details of a selected task
		 * @param lev the ListSelectionEvent that triggered the listener
		 */
		public void valueChanged(ListSelectionEvent lev)
		{
			if (!lev.getValueIsAdjusting())
			{
			// Get task
				Task selectedTask = getSelectedTask();
			// Do nothing if there is no selected task, as this function is fired twice in certain situations
				if (selectedTask == null)
					return;
			// Show task details
				nameLbl.setText(String.format("Name: %s", selectedTask.getName()));
				descLbl.setText(String.format("Description: %s", selectedTask.getDescription()));
			///////////////// THE BIG "FIGURE OUT WHAT DUE DATE TO SHOW" FUNCTION /////////////////
			//                            (should be moved, but where?)
				String due;
				if (selectedTask.getDone())
					due = "done";
				else
				{
					if (selectedTask.getHoursRemaining() > 0)
					{
						if (selectedTask.getHoursRemaining() > 1)
							due = String.format("%d hours remaining", selectedTask.getHoursRemaining());
						else
							due = "1 hour remaining";
					}
					else if (selectedTask.getMinutesRemaining() > 0)
					{
						if (selectedTask.getMinutesRemaining() > 1)
							due = String.format("%d minutes remaining", selectedTask.getMinutesRemaining());
						else
							due = "1 minute remaining";
					}
					else if (selectedTask.getSecondsRemaining() > 0)
					{
						if (selectedTask.getSecondsRemaining() > 1)
							due = String.format("%d seconds remaining", selectedTask.getSecondsRemaining());
						else
							due = "1 second remaining";
					}
					else
					// add code to show how late a task is
						due = "task is late";
				}
				dateLbl.setText(String.format("Due: %s", due));
				
			// Activate "set done" button if task is not done
				if (!selectedTask.getDone())
					setDoneBtn.setEnabled(true);
				else
					setDoneBtn.setEnabled(false);
			}
		}
	}
	
	/**
	 * Listener class for the "mark task as done" button
	 * @author Michael Wihlborg
	 */
	private class SetDoneLnr implements ActionListener
	{
		/**
		 * Marks the selected task as done
		 * @param ave the ActionEvent that triggered the listener
		 */
		public void actionPerformed(ActionEvent ave)
		{
			if (!getSelectedTask().getDone())	// if the task is not already set as done
			{
			// Get name of selected task
				String name = getSelectedTask().getName();
			// Mark task as done
				getSelectedTask().setDone();
			// Disable button
				setDoneBtn.setEnabled(false);
			// Refresh list
				populateLst(getSelectedList());
			// Set the selected task as selected again
				taskLst.setSelectedValue(name, true);
			// Note that changes have been made
				unsavedChanges = true;
			}
		}
	}
	
	/**
	 * Listener for closing the program windows
	 * @author Michael Wihlborg
	 */
	private class CloseLnr extends WindowAdapter
	{
		/**
		 * Closes the program using the exitProgram() method
		 * @param wev the WindowEvent that fired the listener
		 */
		public void windowClosing(WindowEvent wev)
		{
			exitProgram();
		}
	}
	
	/**
	 * Listener for the "exit program" button in the menu bar
	 * @author Michael Wihlborg
	 */
	private class ExitLnr implements ActionListener
	{
		/**
		 * Closes the program using the exitProgram() method
		 * @param ave the ActionEvent that fired the listener
		 */
		public void actionPerformed(ActionEvent ave)
		{
			exitProgram();
		}
	}
	
	/**
	 * Listener called by the timer that updates the displayed due date
	 * @author Michael Wihlborg
	 */
	private class DateLnr implements ActionListener
	{
		/**
		 * Updates the displayed due date
		 * @param ave the ActionEvent that fired the listener
		 */
		public void actionPerformed(ActionEvent ave)
		{
		// Get task
			Task selectedTask = getSelectedTask();
		// Do nothing if there is no selected task, as this function is fired twice in certain situations
			if (selectedTask == null)
				return;
		///////////////// THE BIG "FIGURE OUT WHAT DUE DATE TO SHOW" FUNCTION /////////////////
		//                            (should be moved, but where?)
			String due;
			if (selectedTask.getDone())
				due = "done";
			else
			{
				if (selectedTask.getHoursRemaining() > 0)
				{
					if (selectedTask.getHoursRemaining() > 1)
						due = String.format("%d hours remaining", selectedTask.getHoursRemaining());
					else
						due = "1 hour remaining";
				}
				else if (selectedTask.getMinutesRemaining() > 0)
				{
					if (selectedTask.getMinutesRemaining() > 1)
						due = String.format("%d minutes remaining", selectedTask.getMinutesRemaining());
					else
						due = "1 minute remaining";
				}
				else if (selectedTask.getSecondsRemaining() > 0)
				{
					if (selectedTask.getSecondsRemaining() > 1)
						due = String.format("%d seconds remaining", selectedTask.getSecondsRemaining());
					else
						due = "1 second remaining";
				}
				else
				// add code to show how late a task is
					due = "task is late";
			}
			dateLbl.setText(String.format("Due: %s", due));
			
		// Activate "set done" button if task is not done
			if (!selectedTask.getDone())
				setDoneBtn.setEnabled(true);
			else
				setDoneBtn.setEnabled(false);
		}
	}

	/**
	 * Listener for the "Open" button
	 * @author Michael Wihlborg
	 */
	private class OpenLnr implements ActionListener
	{
		public void actionPerformed(ActionEvent ave)
		{
			// Warn if unsaved changes
			if (unsavedChanges)
			{
				int yn = JOptionPane.showConfirmDialog(MIHDesktop.this, "All unsaved changes will be lost. Do you want to continue?", "Warning", JOptionPane.YES_NO_OPTION);
				if (yn == JOptionPane.NO_OPTION)
					return;
			}
			
			// Get file to open
			int open = fc.showOpenDialog(MIHDesktop.this);
			if (open == JFileChooser.APPROVE_OPTION)
			{
				// Load task lists from file
				try
				{
					TaskListList tll = TaskListIO.loadTaskListList(fc.getSelectedFile().getPath());
					if (tll == null)
					{
						JOptionPane.showMessageDialog(MIHDesktop.this,"Not a valid Majk It Happen file", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					// Clear old data and load new
					nameLbl.setText("Name:");
					descLbl.setText("Description:");
					dateLbl.setText("Due:");
					taskLstMdl.clear();
					
					list = tll;
					populateBox();
					unsavedChanges = false;
					
				}
				catch (IOException e)
				{
					JOptionPane.showMessageDialog(MIHDesktop.this, String.format("Error loading file: %s", e.getMessage()), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
	
	/**
	 * Listener for the "Save" button
	 * @author Michael Wihlborg
	 */
	private class SaveLnr implements ActionListener
	{
		public void actionPerformed(ActionEvent ave)
		{
			// Get filename to save to
			int save = fc.showSaveDialog(MIHDesktop.this);
			if (save == JFileChooser.APPROVE_OPTION)
			{
				// Save to file
				try
				{
					TaskListIO.saveTaskListList(fc.getSelectedFile().getPath(), list);
					unsavedChanges = false;
				}
				catch (IOException e)
				{
					JOptionPane.showMessageDialog(MIHDesktop.this, String.format("Error saving file: %s", e.getMessage()), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
}
