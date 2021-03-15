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
        final int ONESECONDINMILLISECONDS = 1000;

        DatagramSocket socketToTransmitData = new DatagramSocket();

        Scanner clientInput = new Scanner(System.in);

        //My machines local ip
        //InetAddress ip = InetAddress.getLocalHost();
        //InetAddress myIp = InetAddress.getByName();

        //InetAddress logansIP = ip.getByName(myIP);


        InetAddress myIp = InetAddress.getByName(GetPublicIp());


        byte[] messageToBeSent;
        byte[] closingConnectionMessageToBeSent;

        while (true)
        {
            try {

                int randomSecondsToSendNodeAvailibility = GenerateRandomNumberBetween1and30();

                Thread.sleep(ONESECONDINMILLISECONDS * randomSecondsToSendNodeAvailibility);

                System.out.println("System now sending availibility..");

                String messageBeforeByteConversion = "Availible";

                messageToBeSent = messageBeforeByteConversion.getBytes();

                DatagramPacket DpSend = new DatagramPacket(messageToBeSent, messageToBeSent.length, myIp, 1234);

                socketToTransmitData.send(DpSend);

                Thread.sleep(ONESECONDINMILLISECONDS * randomSecondsToSendNodeAvailibility);



                if (messageBeforeByteConversion.toLowerCase().equals("close connection")) {
                    break; //client can close connection
                }



            } catch(Exception ie) {

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

    public static int GenerateRandomNumberBetween1and30()
    {
        int max = 30;
        int min = 1;
        int range = max - min + min;

        int randomNumber = (int)(Math.random() * range) + min;
        return randomNumber;
    }


}
