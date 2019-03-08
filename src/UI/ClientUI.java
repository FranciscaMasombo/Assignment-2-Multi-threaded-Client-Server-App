package UI;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
/*
 * Author:Francisca Masombo
 * Course:SSD
 * Student Number: 200700883
 *
 * This is the client GUI  
 *  
 */
public class ClientUI {

	public JFrame client;
	// Text fields
	public JTextField studentField, radiusField;
	// Text area to display contents
	public JTextArea outputArea;
	
	public JScrollPane scroll;
	// label
	public JLabel studentLabel, radiusLabel, output;
	// header
	public JLabel header;
	// Button
	public JButton login;
	public JButton radiusButton;
	
	
	public ClientUI(){
		// this will start the Gui that is created in the method Run 
		Run();
	}

	private void Run() {
		try {
			// This is the team of the GUI
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // This line gives Windows Theme
		
		client = new JFrame(); // client is the name of the JFrame 
		
		studentField = new JTextField();
		radiusField = new JTextField();
	
		studentField.setFont(new Font("Arial", Font.PLAIN, 20));
		radiusField.setFont(new Font("Arial", Font.PLAIN, 20));
		
		studentLabel = new JLabel("Enter Student ID");
		studentLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		
		radiusLabel = new JLabel("Enter Radius");
		radiusLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		
		output = new JLabel("Output");
		output.setFont(new Font("Arial", Font.PLAIN, 20));
		outputArea = new JTextArea();
		outputArea.setFont(new Font("Arial", Font.PLAIN, 20));
		scroll = new JScrollPane(outputArea);
		// Button
		login = new JButton("Login");
		login.setFont(new Font("Arial", Font.PLAIN, 20));

		radiusButton = new JButton("Get Radius");
		radiusButton.setFont(new Font("Arial", Font.PLAIN, 20));
		
		// header
		header = new JLabel("STUDENTS DETAILS");
		header.setFont(new Font("Arial", Font.BOLD, 35));
		
		radiusField.setEnabled(false);
		login.setBounds(440, 135, 100, 34);
		//login.addActionListener(this);
		radiusButton.setBounds(200, 210, 200, 34);
		//radiusButton.addActionListener(this);
		radiusButton.setEnabled(false);
		
		client.add(header).setBounds(120, 50, 500, 50);
		
		client.add(studentLabel).setBounds(20, 120, 150, 50);
		client.add(studentField).setBounds(200, 135, 200, 30);
		client.add(radiusLabel).setBounds(20, 160, 150, 50);
		client.add(radiusField).setBounds(200, 175, 200, 30);
		client.add(login);
		client.add(radiusButton);
		client.add(output).setBounds(20, 240, 150, 50);
		scroll = new JScrollPane(outputArea);
		client.add(scroll).setBounds(100, 270, 450, 280);
		client.add(studentField);
		client.add(radiusField);
		client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.setTitle("Client");
		client.setLayout(null);
		client.setResizable(false); //the GUI will not be resizeable 
		client.setSize(600, 640); // size of the GUI
		client.setVisible(true);
		
	}
}
