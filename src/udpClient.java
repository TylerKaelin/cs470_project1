//Tyler Kaelin and Logan Morris
import java.io.IOException;
import java.net.InetAddress;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.URL;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

public class udpClient
{

    public static void main(String[] args) throws IOException
    {
        DatagramSocket socketToTransmitData = new DatagramSocket();


        InetAddress myIp = InetAddress.getByName(GetPublicIp());

        initiateAutomaticNodeAvailibility(myIp, socketToTransmitData); // Does heart beat


        byte[] closingConnectionMessageToBeSent;

        Scanner input = new Scanner(System.in);

        while (true)
        {
            try {

                String messageToBeSent = input.nextLine();


                // If message is not empty then send it
                if(!messageToBeSent.equals("")) {
                    closingConnectionMessageToBeSent = messageToBeSent.getBytes();

                    DatagramPacket dataPacketMessage = new DatagramPacket(closingConnectionMessageToBeSent, closingConnectionMessageToBeSent.length, myIp, 1234);

                    socketToTransmitData.send(dataPacketMessage);
                }


                if (messageToBeSent.toLowerCase().equals("close")) {
                    System.exit(0);
                    //client can close connection
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

    public static int generateRandomNumberBetween1and30()
    {
        int max = 30;
        int min = 1;
        int range = max - min + min;

        int randomNumber = (int)(Math.random() * range) + min;
        return randomNumber;
    }

    public static void initiateAutomaticNodeAvailibility(InetAddress myIp, DatagramSocket socketToTransmitData) {

        final int ONESECONDINMILLISECONDS = 1000;
        Timer t = new Timer();

        final String messageBeforeByteConversion = myIp.toString();
        final byte[] availbilityMessageToBeSent = messageBeforeByteConversion.getBytes();

        t.schedule(
                new TimerTask()
                {
                    public void run()
                    {

                        try {
                            System.out.println("System now sending availibility..");


                            DatagramPacket DpSend = new DatagramPacket(availbilityMessageToBeSent, availbilityMessageToBeSent.length, myIp, 1234);

                            socketToTransmitData.send(DpSend);


                            Thread.sleep(generateRandomNumberBetween1and30() * ONESECONDINMILLISECONDS);


                        } catch(Exception ie) {

                        }

                    }
                },
                0,
                ONESECONDINMILLISECONDS);

    }

}
