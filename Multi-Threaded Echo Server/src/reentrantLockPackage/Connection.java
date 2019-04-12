/*
    This class is for clients that are connecting to the server, each client
    in the server has  Connection object that implements the Runnable. This 
    allows for muli-users on the server. The class also handles the echo message
    of the server.
 */
package reentrantLockPackage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Connection implements Runnable {
    private Socket client;
    private Scanner in;
    private PrintWriter out;
    
    public Connection (Socket client) throws IOException {
        this.client = client;
        this.in = new Scanner(client.getInputStream());
        this.out = new PrintWriter(client.getOutputStream(), true);
    }

    @Override
    public void run(){   
        while(in.hasNextLine()) {
            String message = in.nextLine();
            System.out.println("Client: " + message);
            out.println(message);
            out.flush();
        }
        out.close();
        in.close();
    }
}
