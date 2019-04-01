
import java.net.*;
import java.net.SocketException;


public class SisDist {
    public static void main(String[] args) {
        MulticastSocket s = null;
        
        try {
            InetAddress group = InetAddress.getByName("228.5.6.7"); /*Endereço IP */
            s = new MulticastSocket(6789); /* port */
            s.joinGroup(group);
            
            Receive msgIn = new Receive(s);
            SendMsg msg = new SendMsg(s, group);
            msg.Send();


        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("IO: " + e + e.getMessage());
        } finally {
            if (s != null) s.close();
        }
        
    }

}
