import java.awt.Color;
import java.awt.Font;
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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ClientA2 extends JFrame implements ActionListener {
	Statement stmt;
	PreparedStatement pst = null;
	ResultSet rs;
	String sql;
	Connection con;
	private InetAddress address;

	// Text fields
	JTextField studentField, radiusField;
	// Text area to display contents
	JTextArea outputArea;
	JScrollPane scroll;
	// label
	JLabel studentLabel, radiusLabel, output;
	// header
	JLabel header;
	// Button
	JButton login, radiusButton;

	private String fname = "";
	private String sname = "";

	// IO streams
	private DataOutputStream toServer;
	private DataInputStream fromServer;
	private Socket socket;

	public static void main(String[] args) {
		ClientA2 app = new ClientA2();
		app.getContentPane().setBackground(Color.WHITE);
	}

	public ClientA2() {
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
		
		radiusField.setEnabled(false);
		login.setBounds(440, 135, 100, 34);
		login.addActionListener(this);
		radiusButton.setBounds(200, 210, 200, 34);
		radiusButton.addActionListener(this);
		radiusButton.setEnabled(false);
		
		this.add(header).setBounds(120, 50, 500, 50);
		header.setFont(new Font("Arial", Font.BOLD, 35));
		this.add(studentLabel).setBounds(20, 120, 150, 50);
		this.add(studentField).setBounds(200, 135, 200, 30);
		this.add(radiusLabel).setBounds(20, 160, 150, 50);
		this.add(radiusField).setBounds(200, 175, 200, 30);
		this.add(login);
		this.add(radiusButton);
		this.add(output).setBounds(20, 240, 150, 50);
		scroll = new JScrollPane(outputArea);
		this.add(scroll).setBounds(100, 270, 450, 280);
		this.add(studentField);
		this.add(radiusField);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Client");
		this.setLayout(null);
		this.setResizable(false);
		this.setSize(600, 640);
		this.setVisible(true);

		try {
			socket = new Socket("localhost", 8000);
			address = socket.getInetAddress();
			// Create an input stream to receive data from the server
			fromServer = new DataInputStream(socket.getInputStream());
			// Create an output stream to send data to the server
			toServer = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			outputArea.append(e.toString());
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == radiusButton) {
			try {
				double radius = 0;
				try {
					// Get the radius from the text field
					radius = Double.parseDouble(radiusField.getText().trim());
					// Send the radius to the server
					toServer.writeDouble(radius);
					// Get area from the server
					toServer.flush();
					String answer = fromServer.readUTF();
					radiusField.setText("");
					outputArea.append("THE AREA : " + answer + '\n');
				} catch (NumberFormatException e) {
					outputArea.append("ERROR: Please enter a number \n");
					radiusField.setText("");
				}
			} catch (IOException ex) {
				outputArea.append("Invalid Radius" + "\n");
				System.err.println(ex);
			}
		} else if (event.getSource() == login) {
			try {
				int studID = 0;
				try {
					studID = Integer.parseInt(studentField.getText().trim());
					String student = String.valueOf(studID);
					toServer.writeUTF(student);
					toServer.flush();
					String result = fromServer.readUTF();
					String ss = "You are not a registered student. Try again";
					if (result.equals(ss)) {
						System.out.print(result);
						studentField.setText("");
						outputArea.append("Server/" + address + '\n');
						outputArea.append(result + '\n');
						outputArea.append("Please try again" + '\n');
					} else if (!result.equals(ss)) {
						outputArea.append(result + '\n');
						radiusField.setEnabled(true);
						radiusButton.setEnabled(true);

						login.setEnabled(false);
						studentField.setEnabled(false);
					}

				} catch (NumberFormatException e) {
					outputArea.append("ERROR: Please enter a number" + '\n');
					radiusField.setText("");
				}

			} catch (IOException ex) {
				outputArea.append("Invalid Radius" + "\n");
				System.err.println(ex);
			}

		}
	}
//
//	private void clearFields() {
//		// TODO Auto-generated method stub
//		studentField.setText("");
//		radiusField.setText("");
//	}

}
