package RMI;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server implements Hello {
	public Server() {}

	public String sayHello() { return "Hello, world!"; }

	public static void main(String[] args) { 
		Server obj = new Server(); 
		try { 
			Hello stub = (Hello) UnicastRemoteObject.exportObject(obj, 0); 
			// Bind the remote object's stub in the registry 
			Registry registry = LocateRegistry.getRegistry(); 
			registry.bind("Hello", stub); 
			System.out.println("Server ready"); 
			} catch (Exception e) { 
				System.out.println("Server exception: " + e.toString()); 
				} 
		} 
	}

