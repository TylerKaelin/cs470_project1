//Tyler Kaelin and Logan Morris
import java.io.IOException;
import java.net.*;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

public class udpClient
{
    private static int randomSecondsInMilliSecondsToWait;

    public static void main(String[] args) throws IOException
    {
        final int ONESECONDINMILLISECONDS = 1000;

        DatagramSocket socketToTransmitData = new DatagramSocket();


        //My machines local ip
        //InetAddress ip = InetAddress.getLocalHost();
        //InetAddress myIp = InetAddress.getByName();

        //InetAddress logansIP = ip.getByName(myIP);


        InetAddress myIp = InetAddress.getByName(GetPublicIp());

        randomSecondsInMilliSecondsToWait = ONESECONDINMILLISECONDS;

        Timer t = new Timer();

        final String messageBeforeByteConversion = "Availible";
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


                            Thread.sleep(GenerateRandomNumberBetween1and30() * ONESECONDINMILLISECONDS);


                        } catch(Exception ie) {

                        }

                    }
                },
                0,
                randomSecondsInMilliSecondsToWait);


        byte[] closingConnectionMessageToBeSent;

        Scanner input = new Scanner(System.in);

        while (true)
        {
            try {

                String messageToBeSent = input.nextLine();

                // If message is not empty then send it
                if(messageToBeSent != "" || messageToBeSent != null) {
                    closingConnectionMessageToBeSent = messageToBeSent.getBytes();

                    DatagramPacket dataPacketMessage = new DatagramPacket(closingConnectionMessageToBeSent, closingConnectionMessageToBeSent.length, myIp, 1234);

                    socketToTransmitData.send(dataPacketMessage);
                }

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
