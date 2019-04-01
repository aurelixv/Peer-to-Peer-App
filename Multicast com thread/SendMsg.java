import java.util.Scanner;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;


public class SendMsg extends Thread {

	private InetAddress group;
	private MulticastSocket socket;
	
	public SendMsg(MulticastSocket s, InetAddress g){
		group = g;
		socket = s;
	}

	public void Send(){

		try{
			Scanner scan = new Scanner(System.in);
			while(true){
				String arg = scan.nextLine();
				if (arg.equals("quit")) {
					socket.leaveGroup(group);
					break;
				}

			 	byte[] m = arg.getBytes();
				DatagramPacket messageOut = new DatagramPacket(m, arg.length(), group, 6789);
				socket.send(messageOut);
			}
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch(IOException e){
        } finally {
            if (socket != null) socket.close();
        }

	}

}