package Server_and_Client;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import UI.ServerUI;
/*
 * Author:Francisca Masombo
 * Course:SSD
 * Date: 8/03/2019
 * Student Number: 200700883
 *
 *  Description: This is the server 
 *  
 */
public class MultiThreadedServerA2 extends JFrame {

	// Database credentials

	/** useName */
	private final String userName = "root";

	/** The password */
	private final String password = "";

	/** The name of the computer running MySQL */
	private final String serverName = "localhost";

	/** The port of the MySQL server (default is 3306) */
	private final int portNumber = 3306;

	// name of Table
	private final String dbName = "Assign2";

	/** The name of the table we are testing with */
	private String tableName = "myStudents";
	
	// The student ID that will be used to find the student 
	private String studentID = "";
	
	// First and last name of the student that will be display when looked in 
	private String fname = "";
	private String sname = "";
	
	private Statement stmt;
	
	// Results from db will be saved 
	private ResultSet rs;
	
	// SQL statement to be executed 
	private String sql;
	// the connection to the database 
	private Connection con;

	ServerUI serverUI;
	 // Name of server socket
	private ServerSocket serverSocket;

	public static void main(String[] args) {
		MultiThreadedServerA2 app = new MultiThreadedServerA2();
		app.getContentPane().setBackground(Color.WHITE);
	}

	
	public MultiThreadedServerA2() {
		serverUI = new ServerUI(); // This will run the Server GUI
		getConnection(); // this gets the database connection 

		try {
			System.out.println("Server Started......");
			
			// Create a server socket
			ServerSocket serverSocket = new ServerSocket(8000);
			serverUI.jta.append("Server started at " + new Date() + '\n');

			while (true) {
				// Listen for a connection request
				Socket socket = serverSocket.accept();
				// Connect to a client Thread
				ThreadClass thread = new ThreadClass(socket);
				thread.start();

			}
		} catch (IOException x) {
			System.err.println(x);
		}
	}

	/**
	 * Get a new database connection
	 *
	 */
	public Connection getConnection() {
		con = null; // set the connection to null 
		Properties connectionProps = new Properties();
		connectionProps.put("user", userName);
		connectionProps.put("password", password);
		try {
			con = DriverManager.getConnection("jdbc:mysql://" + serverName + ":" + portNumber + "/" + dbName,
					connectionProps);
			System.out.println("connected to the database successfully"); 
		} catch (SQLException e) {
			// if there are any issues they will be displaced in the text error 
			e.printStackTrace();
			serverUI.jta.append("Error with database");
		}
		return con; // return the connect if error it will be null 
	}

	private class ThreadClass extends Thread {
		
		private Socket socket; // this will be the socket the client is connected with 
		private InetAddress address; // this is the IP address that the client is using 
		private DataInputStream inputFromClient; // input to the client 
		private DataOutputStream outputToClient; // output to the client 

		public ThreadClass(Socket socket) throws IOException {
			inputFromClient = new DataInputStream(socket.getInputStream());
			outputToClient = new DataOutputStream(socket.getOutputStream());
			address = socket.getInetAddress();
			serverUI.jta.append("Client's host name is " + address.getHostName() + "\n");
			serverUI.jta.append("Client's IP Address is " + address.getHostAddress() + "\n");

		}

		/*
		 * The method that runs when the thread starts
		 */
		@Override
		public void run() {
			boolean studentFound = false;
			try {
				String threadId = Thread.currentThread().getName();
				serverUI.jta.append("Thread: " + threadId + "\n");
				while (true) {

					if (studentFound == true) {
						CalculateRadius(); // this is will calculate the radius 
					} else {

						studentID = inputFromClient.readUTF();
						stmt = con.createStatement();
						sql = "Select * From mystudents WHERE STUD_ID =" + studentID;
						rs = stmt.executeQuery(sql);

						if (rs.next()) {
							studentFound = true;

							fname = rs.getString("FNAME");
							sname = rs.getString("SNAME");
							serverUI.jta.append("Sever Processing............ " + '\n');
							serverUI.jta.append("Student found : " + fname + " " + sname + '\n');
							outputToClient.writeUTF("Welcome"+" " + fname + " " + sname + "" + '\n');
							System.out.println("Student found : " + fname + " " + fname + '\n');
						} else {

							serverUI.jta.append("You are not a registered student. Try again: " + studentID + '\n');
							outputToClient.writeUTF("You are not a registered student. Try again");
						}
					}
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void CalculateRadius() throws IOException {
			
			double radius = inputFromClient.readDouble(); // Receive number entered from the client
			
			double area = radius * radius * Math.PI; // Calculate the radius
			String answer = String.valueOf(area);
		
			outputToClient.writeUTF(answer); // send the answer to the client 
			
			// will display in the info area 
			serverUI.jta.append("Message from client at address: " + address + '\n');
			serverUI.jta.append("WELCOME: " + fname + " " + sname + '\n');
			serverUI.jta.append("Student ID" + studentID + '\n');
			serverUI.jta.append("Area is: " + area + '\n');
			
			// will display is the console 
			System.out.println("Message from client at address: " + address + '\n');
			System.out.println("WELCOME: " + fname + " " + sname + '\n');
			System.out.println("Student ID" + studentID + '\n');
			System.out.println("Area is: " + area + '\n');
		}

	}

	public void exit() throws IOException {
		try {
			if (con != null)
				con.close();
			serverSocket.close();
			System.exit(0);
		} catch (SQLException e) {
			serverUI.jta.append("Error Closing! - Please try again!" + '\n');
		}
	}

}