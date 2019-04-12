/*
    This program is the client side on the server. It will allow for clients
    to mssage the server and get an echo response of there message.
 */
package synchronizedPackage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {

    public static void main(String[] args) throws IOException {
        final String HOST = "localhost";
        System.out.println("Client Starting ...");
        Socket socket = new Socket(HOST, 1111);

        Scanner in = new Scanner(System.in);
        Scanner serverInput = new Scanner(socket.getInputStream());
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        System.out.println("Enter commands: ");
        
        while (in.hasNextLine()) { 
            String command = in.nextLine();
            if(command.equals(".")) {
                break;
            }
            out.println(command); 
            out.flush();          
            String serverResponse = serverInput.nextLine();
            System.out.println("Server Echo: " + serverResponse);        
        } 
        System.out.println("Client Done");
        socket.close();
    }
}
