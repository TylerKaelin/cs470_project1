import java.io.File;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class udpClientCheckForPackets implements Runnable {

    DatagramSocket socketToTransmitData;

    udpClientCheckForPackets(DatagramSocket socket) {
        socketToTransmitData = socket;
    }


    @Override
    public void run() {


        byte[] byteRepresentationOfMessage = new byte[65535];

        String stringRepresentationOfEachMessage = "";
        DatagramPacket packetToRecieve;


        while (true)
        {
            packetToRecieve = new DatagramPacket(byteRepresentationOfMessage, byteRepresentationOfMessage.length);

            try {
                socketToTransmitData.receive(packetToRecieve);
            } catch(Exception e) {
                System.out.println("error" + e);
            }

            stringRepresentationOfEachMessage = convertByteMessageToString(byteRepresentationOfMessage).toString();
            if(!stringRepresentationOfEachMessage.equals("")) {
                System.out.println("Server: " + stringRepresentationOfEachMessage);
            }


            if (stringRepresentationOfEachMessage.toLowerCase().equals("close"))
            {
                System.out.println("Client has closed the connection. Closing Connection.");
                System.exit(0);
            }

            if(stringRepresentationOfEachMessage.toLowerCase().equals("server down")) {
                System.out.println("Server down, becoming new server");
                System.out.println(); //spacing
                initiateAllNodeAvailibility(socketToTransmitData);
            }

            if(stringRepresentationOfEachMessage.toLowerCase().equals("server up")) {
                System.out.println("Returning to client mode if transitioned to server mode");
                System.out.println(); //spacing
            }

            byteRepresentationOfMessage = new byte[65535];
        }
    }

    public static StringBuilder convertByteMessageToString(byte[] clientMessageInByteRepresentation)
    {
        if (clientMessageInByteRepresentation == null) return null;

        StringBuilder convertedStringRepresentationOfMessage = new StringBuilder();

        int characterIndex = 0;

        while (clientMessageInByteRepresentation[characterIndex] != 0)
        {
            convertedStringRepresentationOfMessage.append((char) clientMessageInByteRepresentation[characterIndex]);
            characterIndex++;
        }

        return convertedStringRepresentationOfMessage;
    }


    public static String[] getAllIpsInIpConfig() {
        String[] allAvailibleIps = {};

        try {
            File IpConfigFile = new File("IpConfigFile.txt");
            Scanner myReader = new Scanner(IpConfigFile);

            int ipIndex = 0;
            int lineCount = 0;

            while (myReader.hasNextLine()) {
                myReader.nextLine();
                lineCount++;
            }

            myReader.close();
            allAvailibleIps = new String[lineCount];

            Scanner newReader = new Scanner(IpConfigFile);


            // Reading each IP address
            while (newReader.hasNextLine()) {
                String eachIpInConfigFile = newReader.nextLine();
                allAvailibleIps[ipIndex] = eachIpInConfigFile;
                ipIndex++;
            }

            newReader.close();
            return allAvailibleIps;
        } catch (Exception e) {
            System.out.println("An error occurred. The could not get all Ips in the config file.");
            e.printStackTrace();
            return allAvailibleIps;
        }
    }

    public static void initiateAllNodeAvailibility(DatagramSocket socketToTransmitData) {


        final int TWENTYFIVESECONDSINMILLISECONDS = 25000;
        Timer t = new Timer();

        t.schedule(
                new TimerTask()
                {
                    public void run()
                    {

                        try {
                            String[] allIps = getAllIpsInIpConfig().clone();

                            System.out.println(); // Spacing
                            System.out.println("New Server System sending all availible nodes..");

                            for(int indexForEachIp = 0; indexForEachIp < allIps.length; indexForEachIp++) {
                                Thread t1 = new Thread(new udpServerNoteAvailibility(allIps[indexForEachIp], "From new node server "+ Arrays.toString(allIps), socketToTransmitData));
                                t1.run();
                            }

                            Thread.sleep(Long.MAX_VALUE); // Sleep Forever unless called again.

                        } catch(Exception ie) {

                        }

                    }
                },
                TWENTYFIVESECONDSINMILLISECONDS,
                TWENTYFIVESECONDSINMILLISECONDS);

    }
}
