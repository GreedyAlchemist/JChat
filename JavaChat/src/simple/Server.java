package simple;

import java.net.*;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Objects;
import java.io.*;

public class Server implements Runnable {
	private SThread clients[] = new SThread[50];
	private ServerSocket server = null;
	private Thread thread = null;
	private int clientCount = 0;
	HashMap<Integer, String> hmap = new HashMap<Integer, String>();

	public Server(int port) {
		System.out.println("Binding to port " + port);
		try {
			server = new ServerSocket(port);
			System.out.println("Server started: " + server);
			start();
		} catch (Exception e) {
			System.out.println( port + " [PORT] " + e.getMessage());
		}
	}
	

	public static void main(String args[]) {
		Server server = null;
		server = new Server(5566);
	}

	public void run() {
		while (thread != null) {
			try {
				System.out.println("Waiting for a client ...");
				newThread(server.accept());
			} catch (Exception e) {
				System.out.println(e.getMessage());
				stop();
			}
		}
	}

	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	public void stop() {
		if (thread != null) {
			thread.stop();
			thread = null;
		}
	}

	private int findClient(int ID) {
		for (int i = 0; i < clientCount; i++)
			if (clients[i].returnID() == ID)
				return i;
		return -1;
	}

	public synchronized void handle(int ID, String input) {
		if (input.equals("@@@")) {
			clients[findClient(ID)].send("Ending Connection for editing Server address and port");
			remove(ID);
		} else if (input.equals("$$$")) {
			clients[findClient(ID)].send("CLIENTS ONLINE:" + hmap.values().toString());
//		} else if (input.substring(0, 3).equals("###")) {
//			clients[findClient(ID)].send("Connected");
//			Integer theKey = getKeyByValue(hmap,input.substring(3, input.length()));
//			if(theKey != null) {
//				clients[theKey.intValue()].send("NEW MESSAGE");
//			}
		}else if (input.substring(0, 3).equals("###")) {
			clients[findClient(ID)].send("Connected");
			hmap.put(ID,input.substring(3, input.length()));
		} else
			for (int i = 0; i < clientCount; i++)
				clients[i].send(hmap.get(ID) + ": " + input);
	}
	
	public synchronized void updateList() {
		for (int i = 0; i < clientCount; i++) {
			System.out.println("UPDATING CLIENTS");
			clients[i].send("CLIENTS ONLINE:" + hmap.values().toString());
		}
	}
	
	public static <Integer, String> Integer getKeyByValue(HashMap<Integer, String> map, String value) {
	    for (Entry<Integer, String> entry : map.entrySet()) {
	        if (Objects.equals(value, entry.getValue())) {
	            return entry.getKey();
	        }
	    }
	    return null;
	}
	


	public synchronized void remove(int ID) {
		int pos = findClient(ID);
		hmap.remove(ID);
		if (pos >= 0) {
			SThread thread = clients[pos];
			System.out.println("Removing thread " + ID + " at " + pos);
			if (pos < clientCount - 1)
				for (int i = pos + 1; i < clientCount; i++)
					clients[i - 1] = clients[i];
			clientCount--;
			try {
				thread.close();
			} catch (Exception e) {
				System.out.println(e);
			}
			thread.stop();
		}
	}

	private void newThread(Socket socket) {
		if (clientCount < clients.length) {
			System.out.println("Client accepted: " + socket);
			clients[clientCount] = new SThread(this, socket);
			try {
				clients[clientCount].open();
				clients[clientCount].start();
				clientCount++;
			} catch (Exception e) {
				System.out.println(e);
			}
		} 
	}
}