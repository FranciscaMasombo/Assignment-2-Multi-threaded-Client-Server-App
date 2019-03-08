package UI;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ServerUI {
	// Text area for displaying contents
	public JTextArea jta = new JTextArea();
	public JFrame server;
	
	public ServerUI(){
		// this will start the Gui that is created in the method Run 
		Run();
	}

	private void Run() {
		server = new JFrame(); // client is the name of the JFrame 
		
		// Place text area on the frame
		server.setLayout(new BorderLayout());
		server.add(new JScrollPane(jta), BorderLayout.CENTER); // this is the main text area that will show responses  
		jta.setEditable(false);
		server.setTitle("Server");
		server.setSize(500, 500); // size of frame
		server.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		server.setVisible(true); 
		
	}
}
