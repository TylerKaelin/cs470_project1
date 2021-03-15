//Tyler Kaelin and Logan Morris
import java.io.IOException;
import java.net.*;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class udpClient
{
    public static void main(String[] args) throws IOException
    {

        DatagramSocket socketToTransmitData = new DatagramSocket();

        Scanner clientInput = new Scanner(System.in);

        //My machines local ip
        //InetAddress ip = InetAddress.getLocalHost();
        //InetAddress myIp = InetAddress.getByName();

        //InetAddress logansIP = ip.getByName(myIP);


        InetAddress myIp = InetAddress.getByName(GetPublicIp());


        byte[] messageToBeSent;

        while (true)
        {
            String messageBeforeByteConversion = clientInput.nextLine();

            messageToBeSent = messageBeforeByteConversion.getBytes(); //Turning message into byte representation

            DatagramPacket DpSend =
                    new DatagramPacket(messageToBeSent, messageToBeSent.length, myIp, 1234);

            socketToTransmitData.send(DpSend);

            if (messageBeforeByteConversion.toLowerCase().equals("close connection")) {
                break; //client can close connection
            }

        }
    }

    public static String GetPublicIp()
    {
        String publicIpAddress = "";
        try
        {
            URL urlName = new URL("http://bot.whatismyipaddress.com");

            BufferedReader sc = new BufferedReader(new InputStreamReader(urlName.openStream()));

            publicIpAddress = sc.readLine().trim();
        }
        catch (Exception e)
        {
            publicIpAddress = "Cannot Execute Properly";
        }

        // System.out.println("Public IP Address: " + systemipaddress +"\n");
        return publicIpAddress;
    }
}
