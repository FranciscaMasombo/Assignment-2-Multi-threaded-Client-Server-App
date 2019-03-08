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
	private final String dbName = "Assing2";

	/** The name of the table we are testing with */
	private String tableName = "myStudents";

	private String studentID = "";
	private String fname = "";
	private String sname = "";
	private Statement stmt;
	private ResultSet rs;
	private String sql;
	private Connection con;

	// Text area for displaying contents
	private JTextArea jta = new JTextArea();
	private ServerSocket serverSocket;

	public static void main(String[] args) {
		MultiThreadedServerA2 app = new MultiThreadedServerA2();
		app.getContentPane().setBackground(Color.WHITE);
	}

	public MultiThreadedServerA2() {
		try {
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

		// Place text area on the frame
		setLayout(new BorderLayout());
		add(new JScrollPane(jta), BorderLayout.CENTER);
		setTitle("Server");
		setSize(500, 500); // size of frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		getConnection();

		try {
			System.out.println("Server Started......");
			// Create a server socket
			ServerSocket serverSocket = new ServerSocket(8000);
			jta.append("Server started at " + new Date() + '\n');

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
		con = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", userName);
		connectionProps.put("password", password);
		try {
			con = DriverManager.getConnection("jdbc:mysql://" + serverName + ":" + portNumber + "/" + dbName,
					connectionProps);
			System.out.println("connected to the database successfully");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			jta.append("Error with database");
		}
		return con;
	}

	private class ThreadClass extends Thread {
		private Socket socket;
		private InetAddress address;
		private DataInputStream inputFromClient;
		private DataOutputStream outputToClient;

		public ThreadClass(Socket socket) throws IOException {
			inputFromClient = new DataInputStream(socket.getInputStream());
			outputToClient = new DataOutputStream(socket.getOutputStream());
			address = socket.getInetAddress();
			jta.append("Client's host name is " + address.getHostName() + "\n");
			jta.append("Client's IP Address is " + address.getHostAddress() + "\n");

		}

		/*
		 * The method that runs when the thread starts
		 */
		@Override
		public void run() {
			boolean studentFound = false;
			try {
				String threadId = Thread.currentThread().getName();
				jta.append("Thread: " + threadId + "\n");
				while (true) {

					if (studentFound == true) {

						// Receive radius from the client
						double radius = inputFromClient.readDouble();
						// Compute area
						double area = radius * radius * Math.PI;
						String answer = String.valueOf(area);
						outputToClient.writeUTF(answer);

						jta.append("Message from client at address: " + address + '\n');
						jta.append("WELCOME: " + fname + " " + sname + '\n');
						jta.append("Student ID" + studentID + '\n');
						jta.append("Area is: " + area + '\n');
						System.out.println("Message from client at address: " + address + '\n');
						System.out.println("WELCOME: " + fname + " " + sname + '\n');
						System.out.println("Student ID" + studentID + '\n');
						System.out.println("Area is: " + area + '\n');

					} else {

						studentID = inputFromClient.readUTF();
						stmt = con.createStatement();
						sql = "Select * From mystudents WHERE STUD_ID =" + studentID;
						rs = stmt.executeQuery(sql);

						if (rs.next()) {
							studentFound = true;

							fname = rs.getString("FNAME");
							sname = rs.getString("SNAME");
							jta.append("Sever Processing............ " + '\n');
							jta.append("Student found : " + fname + " " + sname + '\n');
							outputToClient.writeUTF("Welcome"+" " + fname + " " + sname + "" + '\n');
							System.out.println("Student found : " + fname + " " + fname + '\n');
						} else {

							jta.append("You are not a registered student. Try again: " + studentID + '\n');
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

	}

	public void exit() throws IOException {
		try {
			if (con != null)
				con.close();
			serverSocket.close();
			System.exit(0);
		} catch (SQLException e) {
			jta.append("Error Closing! - Please try again!" + '\n');
		}
	}

}