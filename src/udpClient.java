//Tyler Kaelin and Logan Morris
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class udpClient
{
    public static void main(String[] args) throws IOException
    {

        DatagramSocket socketToTransmitData = new DatagramSocket();

        Scanner clientInput = new Scanner(System.in);

        // String logansIpTextRepresentation = "192.168.1.148";

        //My machines local ip
        InetAddress ip = InetAddress.getLocalHost();
        //InetAddress logansIP = InetAddress.getByName(logansIpTextRepresentation);

        //InetAddress logansIP = ip.getByName(logansIpTextRepresentation);


        byte[] messageToBeSent;

        while (true)
        {
            String messageBeforeByteConversion = clientInput.nextLine();

            messageToBeSent = messageBeforeByteConversion.getBytes(); //Turning message into byte representation

            DatagramPacket DpSend =
                    new DatagramPacket(messageToBeSent, messageToBeSent.length, ip, 1234);

            socketToTransmitData.send(DpSend);

            if (messageBeforeByteConversion.toLowerCase().equals("close connection")) {
                break; //client can close connection
            }

        }
    }
}
