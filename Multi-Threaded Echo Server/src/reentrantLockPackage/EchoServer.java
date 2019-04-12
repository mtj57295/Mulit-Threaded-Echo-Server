/*
    This process is the server part of the program and will allow for clients
    to connect to the server and interact with it. The server creates an object
    of the ThreadPool Class to store clients connections.
 */
package reentrantLockPackage;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
    
    private final static int PORT = 1112;
    
    public static void main(String[] args) throws IOException {
        ServerSocket server;
        server = new ServerSocket(PORT);
        System.out.println("Server started");
        ThreadPool threadPool = new ThreadPool();
        System.out.println("Thread Pool Created");
        System.out.println("Awaiting Clients");
        while(true){            
            Socket socket = server.accept();
            System.out.println(socket.getLocalAddress() + " Connected!");
            threadPool.addTaskToQueue(new Connection(socket));          
        }
    }   
}
