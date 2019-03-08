package Server_and_Client;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JFrame;

import UI.ClientUI;
/*
 * Author:Francisca Masombo
 * Course:SSD
 * Student Number: 200700883
 *
 *  Description: This is the client side of the application the client gui is called from here 
 *  and info is requested from the server and also sent to the server 
 *  
 *  class main:  will display the GUI 
 *  ValidateStudent(): will check is the Student is validated so they can get the radius 
 *  Radius() : will calculate the radius
 *  
 *  
 */
public class ClientA2 extends JFrame implements ActionListener {
	Statement stmt;
	PreparedStatement pst = null;
	ResultSet rs;
	String sql;
	Connection con;
	ClientUI clientUI;
	private InetAddress address;
	private String fname = "";
	private String sname = "";

	// IO streams
	private DataOutputStream toServer;
	private DataInputStream fromServer;
	private Socket socket;

	public static void main(String[] args) {
		ClientA2 app = new ClientA2();
		app.getContentPane().setBackground(Color.WHITE); //this will set the app color as WHITE 
	}

	public ClientA2() {
		clientUI = new ClientUI(); // this will run the ClientUI class this in the client GUI
		clientUI.login.addActionListener(this); // this is listening for the login button
		clientUI.radiusButton.addActionListener(this); // this is listening for the radius button
		/*
		 * once the buttons are clicked then the actions will be done the student needs
		 * to be logged in first
		 */
		try {
			socket = new Socket("localhost", 8000);
			address = socket.getInetAddress();
			// Create an input stream to receive data from the server
			fromServer = new DataInputStream(socket.getInputStream());
			// Create an output stream to send data to the server
			toServer = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			clientUI.outputArea.append(e.toString());
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == clientUI.radiusButton) {
			/*
			 * When the button radius is pressed then the method below will be called 
			 * the Radius(); method will calculate the radius of a circle
			 */
			Radius();
		} else if (event.getSource() == clientUI.login) {
			try {
				/*
				 * When the login button is pressed the ValidateStudent(); method is called 
				 */
				ValidateStudent();
			} catch (IOException ex) {
				clientUI.outputArea.append("Invalid Radius" + "\n");
				System.err.println(ex);
			}

		}
	}

	private void ValidateStudent() throws IOException {
		int studID = 0; // the student ID should be  0 first
		try {
			studID = Integer.parseInt(clientUI.studentField.getText().trim()); // this gets the student Id from the textField
			String student = String.valueOf(studID); //save the ID in student as a string 
			toServer.writeUTF(student); //send the Id to the server
			toServer.flush();
			String result = fromServer.readUTF(); //this will get the result from the server which will be if the StudentId is in the Database 
			String ss = "You are not a registered student. Try again"; // this is an error message is the StudentID is not in the database
			if (result.equals(ss)) {
				/*
				 *If the result is the error message saved in ss  
				 *then the user will get a message to try again
				 */
				System.out.print(result);
				clientUI.studentField.setText("");
				clientUI.outputArea.append("Server/" + address + '\n');
				clientUI.outputArea.append(result + '\n');
				clientUI.outputArea.append("Please try again" + '\n');
			} else if (!result.equals(ss)) {
				clientUI.outputArea.append(result + '\n');
				/*
				 * Enable the radius field and and the radius button to calculate the radius
				 * the field and the button are disabled until the StudentID is valid to one in the database
				 */
				clientUI.radiusField.setEnabled(true); 
				clientUI.radiusButton.setEnabled(true);
				
				/*
				 * Disable the login field and the button once the user is valid 
				 */
				clientUI.login.setEnabled(false);
				clientUI.studentField.setEnabled(false);
			}

		} catch (NumberFormatException e) {
			clientUI.outputArea.append("ERROR: Please enter a number" + '\n');
			clientUI.radiusField.setText("");
		}
	}

	private void Radius() {
		try {
			double radius = 0;
			try {
				// Get the radius from the text field
				radius = Double.parseDouble(clientUI.radiusField.getText().trim());
				// Send the radius to the server
				toServer.writeDouble(radius);
				// Get area from the server
				toServer.flush();
				
				String answer = fromServer.readUTF(); // the answer is from the server
				clientUI.radiusField.setText(""); // clear the radius field so the student can calculate a new radius 
				clientUI.outputArea.append("THE AREA : " + answer + '\n');
			} catch (NumberFormatException e) {
				clientUI.outputArea.append("ERROR: Please enter a number \n");
				clientUI.radiusField.setText(""); // clear the radius field so the student can calculate a new radius 
				
			}
		} catch (IOException ex) {
			clientUI.outputArea.append("Invalid Radius" + "\n");
			System.err.println(ex);
		}
	}

}
