//Tyler Kaelin and Logan Morris
import java.io.*;
import java.net.InetAddress;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.URL;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class udpClient
{

    public static void main(String[] args) throws IOException
    {
        DatagramSocket socketToTransmitData = new DatagramSocket();
        createNetworkTypeFile();


        Scanner networkInput = new Scanner(System.in);
        System.out.println("Please enter \"truman\" or \"local\" for network type: ");
        String userNetworkInput = networkInput.nextLine();

        while(!userNetworkInput.toLowerCase().equals("local") && !userNetworkInput.toLowerCase().equals("truman"))
        {
            System.out.println("Please enter \"truman\" or \"local\" for network type: ");
            userNetworkInput = networkInput.nextLine();
        }

        InetAddress myIp;
        if(userNetworkInput.equals("truman")) {
            myIp = InetAddress.getByName(GetPublicIp());
        } else {
            myIp = InetAddress.getLocalHost();
        }


        System.out.println("Current Machines Ip: " + myIp);

        if(!isNetworkTypeInFile(userNetworkInput)) {
            logNetworkTypeInFile(userNetworkInput);
        }



        initiateAutomaticNodeAvailibility(myIp, socketToTransmitData); // Does heart beat
        initiateUdpServerPackageCheck();


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


//    public static void initiateUdpServerPackageCheck(DatagramSocket socketToTransmitData) {
//
//        final int FIVESECONDSINMILLISECONDS = 5000;
//        Timer t = new Timer();
//
//        //final String messageBeforeByteConversion = myIp.toString();
//
//
//        t.schedule(
//                new TimerTask()
//                {
//                    public void run()
//                    {
//
//                        byte[] byteRepresentationOfMessage = new byte[65535];
//                        try {
//                            String stringRepresentationOfEachMessage;
//                            DatagramPacket packetToRecieve;
//
//
//                            System.out.println("Looking for data from Server");
//
//                            packetToRecieve = new DatagramPacket(byteRepresentationOfMessage, byteRepresentationOfMessage.length);
//
//                            socketToTransmitData.receive(packetToRecieve);
//
//                                // if ip is valid
//                            stringRepresentationOfEachMessage = convertByteMessageToString(byteRepresentationOfMessage).toString();
//
//                            System.out.println("Server: " + stringRepresentationOfEachMessage);
//
//
//                                // System.out.println("Client: " + convertByteMessageToString(byteRepresentationOfMessage));
//
//                            if (stringRepresentationOfEachMessage.toLowerCase().equals("close"))
//                            {
//                                System.out.println("Client has closed the connection. Closing Connection.");
//                                System.exit(0);
//                            }
//
//                            byteRepresentationOfMessage = new byte[65535];
//
//
//                        } catch(Exception ie) {
//
//                        }
//
//                    }
//                },
//                0,
//                FIVESECONDSINMILLISECONDS);
//
//    }

    public static void initiateUdpServerPackageCheck() {

        final int FIVESECONDSINMILLISECONDS = 5000;
        Timer t = new Timer();

        //final String messageBeforeByteConversion = myIp.toString();


        t.schedule(
                new TimerTask()
                {
                    public void run()
                    {

                        try {

                            Thread t2 = new Thread(new udpClientCheckForPackets());
                            t2.start();
                            System.out.println("Checking for packets");

                        } catch(Exception ie) {

                        }

                    }
                },
                0,
                FIVESECONDSINMILLISECONDS);

    }

    public static void createNetworkTypeFile() {
        try {
            File NetworkTypeConfig = new File("networkType.txt");
            if (NetworkTypeConfig.createNewFile()) {
                System.out.println("File created: " + NetworkTypeConfig.getName());
            } else {
                System.out.println("NetworkType already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred in creating the network type file.");
            e.printStackTrace();
        }

    }

    public static boolean isNetworkTypeInFile(String currentNetworkType) {
        try {
            File NetworkTypeConfig = new File("networkType.txt");
            Scanner myReader = new Scanner(NetworkTypeConfig);
            boolean isNetworkTypeInConfig = false;

            // While reading each IP address
            while (myReader.hasNextLine()) {
                String eachNetworkType = myReader.nextLine();

                if(eachNetworkType.equals(currentNetworkType)) {
                    //ip in config file
                    isNetworkTypeInConfig = true;
                    break;
                }

            }

            if(isNetworkTypeInConfig) {
                System.out.println("The Network Type is already in the config file");
            } else {
                System.out.println("The Network Type was not in the config file");
            }

            myReader.close();
            return isNetworkTypeInConfig;
        } catch (Exception e) {
            System.out.println("An error occurred. The Ip existence in config file was not checked.");
            e.printStackTrace();
            return false;
        }

    }

    public static void logNetworkTypeInFile(String networkType) {
        try {
            FileWriter IpConfigFile = new FileWriter("networkType.txt");
            IpConfigFile.write(networkType);
            IpConfigFile.write("\n");
            IpConfigFile.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred. The networkType was not logged.");
            e.printStackTrace();
        }
    }

}
