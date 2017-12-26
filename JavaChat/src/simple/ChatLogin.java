///**
// * 
// */
//package simple;
//
//import java.applet.Applet;
//import java.awt.BorderLayout;
//import java.awt.Button;
//import java.awt.GridLayout;
//import java.awt.Panel;
//import java.awt.TextArea;
//import java.awt.TextField;
//import java.io.DataOutputStream;
//import java.net.Socket;
//
//import javax.swing.DefaultListModel;
//import javax.swing.JList;
//import javax.swing.JTextField;
//import javax.swing.ListSelectionModel;
//
///**
// * @author Vato
// *
// */
//public class ChatLogin extends JApplet{
//	
//	private Client client = new Client();
//	
//	private Socket socket = null;
//	private CThread cThreat = null;
//	
//	private JTextField ServerAddressField = new JTextField();
//	private JTextField clientUsernameField = new JTextField();
//    DefaultListModel clientList = new DefaultListModel();
//	private JList clientJList = new JList(clientList);
//
//	private Button connect = new Button("Connect");
//	private String serverName = "0.0.0.0";
//	private int serverPort = 5566;
//	
//	public void init() {
//		Panel North = new Panel();
//		Panel Middle = new Panel();
//		Panel South = new Panel();
//
//		setLayout(new BorderLayout());
//		South.setLayout(new BorderLayout());
//		North.setLayout(new GridLayout(1, 3));
//		North.add(ServerAddressField);
//		North.add(clientUsernameField);
//		North.add(connect);
//		Middle.setLayout(new GridLayout(1, 2));
//		Middle.add("East", clientJList);
//
//
//	      clientJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//	      clientJList.setSelectedIndex(0);
//	      clientJList.setVisibleRowCount(3);  
//
//		add("North", North);
//		add("Center", Middle);
//		add("South", South);
//
//		ServerAddressField.setText(serverName);
//		ServerAddressField.setEditable(true);
//		clientUsernameField.setText("Client");
//		clientUsernameField.setEditable(true);
//		setSize(500, 50);
//	}
//	
//	
//	public void connect(String serverName, int serverPort) {
//		client.init();
//	}
//
//}