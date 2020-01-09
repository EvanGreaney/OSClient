import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Scanner;

public class Client 
{
	
	private Socket connection;
	private String message;
	private  Scanner console;
	private  String ipaddress;
	private  int portaddress;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	public Client()
	{
		console = new Scanner(System.in);
		
		System.out.println("Enter the IP Address of the server");
		ipaddress = console.nextLine();
		
		System.out.println("Enter the TCP Port");
		portaddress  = console.nextInt();
		
	}
	
	void sendMessage(String msg)
	{
		try{
			out.writeObject(msg);
			out.flush();
			System.out.println("client>" + msg);
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) 
	{
			Client temp = new Client();
			temp.clientapp();
	}

	public void clientapp()
	{
		
		try 
		{
			connection = new Socket(ipaddress,portaddress);
		
			out = new ObjectOutputStream(connection.getOutputStream());
			out.flush();
			in = new ObjectInputStream(connection.getInputStream());
			System.out.println("Client Side ready to communicate");
		
		
		    /// Client App.
			message = (String)in.readObject();
			System.out.println(message);
			message = console.next();
			sendMessage(message);
			
			//Register
			if(message.equals("1"))
			{
				message = (String)in.readObject();
				System.out.println(message);
				message = console.next();
				sendMessage(message);
				
				//Agent
				if (message.equals("1")) {
					//Agent Name
					message = (String)in.readObject();
					System.out.println(message);
					message = console.next();
					sendMessage(message);
					//Agent Email
					message = (String)in.readObject();
					System.out.println(message);
					message = console.next();
					sendMessage(message);
					//Agent ID
					message = (String)in.readObject();
					System.out.println(message);
					message = console.next();
					sendMessage(message);
					//Agent Final Message
					message = (String)in.readObject();
					System.out.println(message);
				}
				//Club
				else if (message.equals("2")) {
					//Club Name
					message = (String)in.readObject();
					System.out.println(message);
					message = console.next();
					sendMessage(message);
					//Club Email
					message = (String)in.readObject();
					System.out.println(message);
					message = console.next();
					sendMessage(message);
					//Club ID
					message = (String)in.readObject();
					System.out.println(message);
					message = console.next();
					sendMessage(message);
					//Club Funds
					message = (String)in.readObject();
					System.out.println(message);
					message = console.next();
					sendMessage(message);
					//Club Final Message
					message = (String)in.readObject();
					System.out.println(message);
				}
				
				
				
				
			}
			//Login
			else if(message.equals("2"))
			{
				
			}
			
			message = (String)in.readObject();
			System.out.println(message);
			
			
			out.close();
			in.close();
			connection.close();
		} 
		
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
