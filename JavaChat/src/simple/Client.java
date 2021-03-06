package simple;

import java.net.*;
import java.applet.*;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import java.io.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Client extends Applet {
	private Socket socket = null;
	private CThread cThreat = null;
	private DataOutputStream streamOut = null;

	private TextArea display = new TextArea();
	private TextField textField = new TextField();
	private JTextField ServerAddressField = new JTextField();
	private JTextField clientUsernameField = new JTextField();
    DefaultListModel clientList = new DefaultListModel();
	private JList clientJList = new JList(clientList);

	private Button connect = new Button("Connect"), edit = new Button("edit");
	private String serverName = "0.0.0.0";
	private int serverPort = 5566;
	
	public Client() {
		
	}

	public void init() {
		Panel menu = new Panel();
		Panel North = new Panel();
		Panel Middle = new Panel();
		Panel South = new Panel();

		setLayout(new BorderLayout());
		South.setLayout(new BorderLayout());
		South.add("West", menu);
		South.add("Center", textField);
		North.setLayout(new GridLayout(1, 4));
		North.add(ServerAddressField);
		North.add(clientUsernameField);
		North.add(edit);
		North.add(connect);
		Middle.setLayout(new GridLayout(1, 2));
		Middle.add("West",display);
		Middle.add("East", clientJList);

	      clientJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	      clientJList.setSelectedIndex(0);
	      clientJList.setVisibleRowCount(3);
	      
	      clientJList.setEnabled(false);

		add("North", North);
		add("Center", Middle);
		add("South", South);

		ServerAddressField.setText(serverName);
		ServerAddressField.setEditable(true);
		clientUsernameField.setText("Client");
		clientUsernameField.setEditable(true);
		textField.setEnabled(false);
		edit.setEnabled(false);
		display.setEditable(false);
		setSize(500, 400);
	}
	
	
	public void updateClientList(String [] clients) {
		clientList.removeAllElements();
		for(String client : clients) {
			clientList.addElement(client);
		}
	}

	public void connect(String serverName, int serverPort) {
		display("Connecting");
		try {
			socket = new Socket(serverName, serverPort);
			display("Connected: " + socket);
			open();
			connect.setEnabled(false);
			edit.setEnabled(true);
			ServerAddressField.setEditable(false);
			clientUsernameField.setEditable(false);
			registerNickname();
			  clientJList.setEnabled(true);
		      clientJList.addMouseListener(new MouseAdapter() {
			  	    public void mouseClicked(MouseEvent evt) {
			  	        JList clientJlist = (JList)evt.getSource();
			  	        if (evt.getClickCount() == 2) {
			  	            // Double-click detected
			  	            int index = clientJlist.locationToIndex(evt.getPoint());
			  	            askforClients();
			  	        }
			  	    }
		      });
		      
		} catch (Exception e) {
			display(e.getMessage());
		}
	}

	private void registerNickname() {
		try {
			streamOut.writeUTF("###" + clientUsernameField.getText());
			streamOut.flush();
		} catch (Exception e) {
			display(e.getMessage());
			close();
		}
	}
	
	private void askforClients() {
		try {
			streamOut.writeUTF("$$$");
			streamOut.flush();
		} catch (Exception e) {
			display(e.getMessage());
			close();
		}
	}
	
	
	public boolean action(Event e, Object o) {
		if (e.target == edit) {
			textField.setEnabled(false);
			edit.setEnabled(false);
			connect.setEnabled(true);
			ServerAddressField.setEditable(true);
			clientUsernameField.setEditable(true);
			close();
		} else if (e.target == connect) {
			textField.setEnabled(true);
			connect(ServerAddressField.getName(), serverPort);
		} else if (e.target == textField) {
			send();
			textField.requestFocus();
		}
		return true;
	}

	public void close() {
		try {
			if (streamOut != null)
				streamOut.close();
			if (socket != null)
				socket.close();
		} catch (Exception e) {
			display(e.getMessage());
		}
		cThreat.close();
		cThreat.stop();
	}

	public void display(String text) {
		display.appendText(text + "\n");
	}

	private void send() {
		try {
			streamOut.writeUTF(textField.getText());
			streamOut.flush();
			textField.setText("");
		} catch (Exception e) {
			display(e.getMessage());
			close();
		}
	}

	public void open() {
		try {
			streamOut = new DataOutputStream(socket.getOutputStream());
			cThreat = new CThread(this, socket);
		} catch (Exception e) {
			display(e.getMessage());
		}

	}
}