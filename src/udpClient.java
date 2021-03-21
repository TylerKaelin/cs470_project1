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
        createModeFile();

        System.out.println("MAKE SURE YOU HAVE STARTED THE SERVER FIRST!!");

        Scanner modeInput = new Scanner(System.in);
        System.out.println("Please enter the mode type as either \"peer to peer\" or \"client server\": ");
        String modeTypeStringRepresentation = modeInput.nextLine();

        while(!modeTypeStringRepresentation.toLowerCase().equals("peer to peer") && !modeTypeStringRepresentation.toLowerCase().equals("client server"))
        {
            System.out.println("Please enter the mode type as either \"peer to peer\" or \"client server\": ");
            modeTypeStringRepresentation = modeInput.nextLine();
        }

        if(!isModeTypeInFile(modeTypeStringRepresentation)) {
            logModeTypeInFile(modeTypeStringRepresentation);
        }


        Scanner networkInput = new Scanner(System.in);
        System.out.println("Please enter \"truman\" or \"local\" for network type: ");
        String userNetworkInput = networkInput.nextLine();

        while(!userNetworkInput.toLowerCase().equals("local") && !userNetworkInput.toLowerCase().equals("truman"))
        {
            System.out.println("Please enter \"truman\" or \"local\" for network type: ");
            userNetworkInput = networkInput.nextLine();
        }

        if(!isNetworkTypeInFile(userNetworkInput)) {
            logNetworkTypeInFile(userNetworkInput);
        }

        initiateUdpServerPackageCheck();

        InetAddress nodeSpecificIp = InetAddress.getByName(GetPublicIp());

        System.out.println("Current Node Ip: " + nodeSpecificIp); // Mainly for peer to peer mode

        Scanner serverIpInput = new Scanner(System.in);
        System.out.println("To get the servers Ip to connect to you must tell the server \"yes\" you have input in the mode type. Please enter the servers ip address: ");
        String serverIpStringRepresentation = serverIpInput.nextLine();


        InetAddress serverIp;
        if(userNetworkInput.equals("truman")) {
                //myIp = InetAddress.getByName(GetPublicIp());
            //serverIp = InetAddress.getByName("150.243.227.218"); //This is the manual server ip
            serverIp = InetAddress.getByName(serverIpStringRepresentation);

        } else {
            serverIp = InetAddress.getLocalHost();
        }

//        Scanner modeInput = new Scanner(System.in);
//        System.out.println("Please enter the mode type as either \"peer to peer\" or \"client server\": ");
//        String modeTypeStringRepresentation = modeInput.nextLine();
//
//        while(!modeTypeStringRepresentation.toLowerCase().equals("peer to peer") && !modeTypeStringRepresentation.toLowerCase().equals("client server"))
//        {
//            System.out.println("Please enter the mode type as either \"peer to peer\" or \"client server\": ");
//            modeTypeStringRepresentation = modeInput.nextLine();
//        }


//        if(!isNetworkTypeInFile(userNetworkInput)) {
//            logNetworkTypeInFile(userNetworkInput);
//        }

//        if(!isModeTypeInFile(modeTypeStringRepresentation)) {
//            logModeTypeInFile(modeTypeStringRepresentation);
//        }

//        InetAddress nodeSpecificIp = InetAddress.getByName(GetPublicIp());
//
//        System.out.println("Current Node Ip: " + nodeSpecificIp); // Mainly for peer to peer mode

        initiateAutomaticNodeAvailibility(serverIp, socketToTransmitData, nodeSpecificIp); // Does heart beat
//        initiateUdpServerPackageCheck();


        byte[] closingConnectionMessageToBeSent;

        Scanner input = new Scanner(System.in);

        while (true)
        {
            try {

                String messageToBeSent = input.nextLine();


                // If message is not empty then send it
                if(!messageToBeSent.equals("")) {
                    closingConnectionMessageToBeSent = messageToBeSent.getBytes();

                    DatagramPacket dataPacketMessage = new DatagramPacket(closingConnectionMessageToBeSent, closingConnectionMessageToBeSent.length, serverIp, 1234);

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

    public static void initiateAutomaticNodeAvailibility(InetAddress myIp, DatagramSocket socketToTransmitData, InetAddress tylersIp) {

        final int ONESECONDINMILLISECONDS = 1000;
        Timer t = new Timer();

        //final String messageBeforeByteConversion = myIp.toString();
        final String messageBeforeByteConversion = tylersIp.toString();
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

        DatagramSocket socket = createSocket();

        //final String messageBeforeByteConversion = myIp.toString();


        t.schedule(
                new TimerTask()
                {
                    public void run()
                    {

                        try {

                            Thread t2 = new Thread(new udpClientCheckForPackets(socket));
                            t2.start();
                            //System.out.println("Checking for packets");

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

    public static DatagramSocket createSocket() {
        DatagramSocket socketToTransmitData = null;
        try {
            if(isModeTypeInFile("client server")) {
                socketToTransmitData = new DatagramSocket(1235);
                System.out.println("Socket on 1235 for client server");
            } else {
                socketToTransmitData = new DatagramSocket(1234);
                System.out.println("Socket on 1234 for peer to peer");
            }
            System.out.println("Successfully created socket.");
            return socketToTransmitData;
        } catch(Exception e) {

        }
        return socketToTransmitData;
    }

    public static void createModeFile() {
        try {
            File modeConfig = new File("mode.txt");
            if (modeConfig.createNewFile()) {
                System.out.println("File created: " + modeConfig.getName());
            } else {
                System.out.println("Mode config file already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred in creating the mode type file.");
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
                    //network type in config file
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

    public static boolean isModeTypeInFile(String currentModeType) {
        try {
            File modeConfig = new File("mode.txt");
            Scanner myReader = new Scanner(modeConfig);
            boolean isModeTypeInConfig = false;

            // While reading each IP address
            while (myReader.hasNextLine()) {
                String eachModeType = myReader.nextLine();

                if(eachModeType.equals(currentModeType)) {
                    //mode in config file
                    isModeTypeInConfig = true;
                    break;
                }

            }

            if(isModeTypeInConfig) {
                System.out.println("The Mode Type is already in the config file");
            } else {
                System.out.println("The Mode Type was not in the config file");
            }

            myReader.close();
            return isModeTypeInConfig;
        } catch (Exception e) {
            System.out.println("An error occurred. The Ip existence in config file was not checked.");
            e.printStackTrace();
            return false;
        }

    }

    public static void logNetworkTypeInFile(String networkType) {
        try {
            FileWriter networkTypeConfigFile = new FileWriter("networkType.txt");
            networkTypeConfigFile.write(networkType);
            networkTypeConfigFile.write("\n");
            networkTypeConfigFile.close();
            System.out.println("Successfully wrote network type to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred. The network type was not logged.");
            e.printStackTrace();
        }
    }

    public static void logModeTypeInFile(String modeType) {
        try {
            FileWriter modeTypeConfigFile = new FileWriter("mode.txt");
            modeTypeConfigFile.write(modeType);
            modeTypeConfigFile.write("\n");
            modeTypeConfigFile.close();
            System.out.println("Successfully wrote mode type to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred. The mode type was not logged.");
            e.printStackTrace();
        }
    }

}
