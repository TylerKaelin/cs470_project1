// Tyler Kaelin and Logan Morris
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class udpServer
{
    public static void main(String[] args) throws IOException
    {

        DatagramSocket socketToTransmitData = new DatagramSocket(1234);
        byte[] byteRepresentationOfMessage = new byte[65535];

        String stringRepresentationOfEachMessage;
        DatagramPacket packetToRecieve;
        createIpConfigFile();
        System.out.println(); //Spacing


        //isModeTypeInFile(String currentModeType)
        Scanner modeDoneInput = new Scanner(System.in);
        System.out.println("Has the mode type been entered in the client? Please enter yes or no. If not start the client.");
        String modeInputString = modeDoneInput.nextLine();

        while(!modeInputString.toLowerCase().equals("yes")) {
            System.out.println("Please enter the mode input on the client first then come back and type \"yes\" when you have.");
            modeInputString = modeDoneInput.nextLine();
        }

        //if in client server mode then the server will send all nodes availibility
        if(isModeTypeInFile("client server")) {
            initiateAllNodeAvailibility(socketToTransmitData);
        }

//        String[] allIps = getAllIpsInIpConfig().clone();
        //initiateAllNodeAvailibility(socketToTransmitData);

        InetAddress serverIp = InetAddress.getByName(GetPublicIp());
        System.out.println("Connect to Servers Ip (do not add the \"/\"): " + serverIp);

//        initiateAllNodeAvailibility(socketToTransmitData);


        while (true)
        {

            packetToRecieve = new DatagramPacket(byteRepresentationOfMessage, byteRepresentationOfMessage.length);

            socketToTransmitData.receive(packetToRecieve);

            // if ip is valid
            stringRepresentationOfEachMessage = convertByteMessageToString(byteRepresentationOfMessage).toString();
            if(isValidIpAddress(stringRepresentationOfEachMessage)) {

                //If ip is not in config file log it
                if(!isIpInConfigFile(stringRepresentationOfEachMessage)) {
                    logIpAddressInIpConfigFile(stringRepresentationOfEachMessage);
                }

            } else {
                System.out.println("Client: " + stringRepresentationOfEachMessage);
            }


            // System.out.println("Client: " + convertByteMessageToString(byteRepresentationOfMessage));

            if (stringRepresentationOfEachMessage.toLowerCase().equals("close"))
            {
                System.out.println("Client has closed the connection. Closing Connection.");
                System.exit(0);
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



    public static void createIpConfigFile() {
        try {
            File IpConfigFile = new File("IpConfigFile.txt");
            if (IpConfigFile.createNewFile()) {
                System.out.println("File created: " + IpConfigFile.getName());
            } else {
                System.out.println("IpConfigFile already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred in creating the config file.");
            e.printStackTrace();
        }

    }

    public static void logIpAddressInIpConfigFile(String currentNodeIp) {
        try {
            FileWriter IpConfigFile = new FileWriter("IpConfigFile.txt", true);
            IpConfigFile.write(currentNodeIp);
            IpConfigFile.write("\n");
            IpConfigFile.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred. The Ip was not logged.");
            e.printStackTrace();
        }
    }

    public static boolean isIpInConfigFile(String currentNodeIp) {
        try {
            File IpConfigFile = new File("IpConfigFile.txt");
            Scanner myReader = new Scanner(IpConfigFile);
            boolean ipIsInConfig = false;

            // While reading each IP address
            while (myReader.hasNextLine()) {
                String eachIpInConfigFile = myReader.nextLine();

                if(eachIpInConfigFile.equals(currentNodeIp)) {
                    //ip in config file
                    ipIsInConfig = true;
                    break;
                }

            }

            if(ipIsInConfig) {
                System.out.println("The Ip is already in the config file");
            } else {
                System.out.println("The Ip was not in the config file");
            }

            myReader.close();
            return ipIsInConfig;
        } catch (Exception e) {
            System.out.println("An error occurred. The Ip existence in config file was not checked.");
            e.printStackTrace();
            return false;
        }

    }

    public static boolean isValidIpAddress(String currentNodeIp) {
        int periodCount = 0;
        char[] currentNodeIpCharRepresentation = currentNodeIp.toCharArray();
        char eachCharacterInCurrentNodeIp;
        String substringOfIp = "";
        for(int ipIndex = 0; ipIndex < currentNodeIp.length(); ipIndex++) {
            eachCharacterInCurrentNodeIp = currentNodeIpCharRepresentation[ipIndex];

            if (Character.toString(eachCharacterInCurrentNodeIp).equals("/")) {
                substringOfIp = currentNodeIp.substring(ipIndex);

                break;
            }
        }

        if (substringOfIp.equals("")) {
            System.out.println("Invalid Ip");
            return false;
        } else {
            for(int secondIpIndex = 0; secondIpIndex < substringOfIp.length(); secondIpIndex++) {
                eachCharacterInCurrentNodeIp = substringOfIp.charAt(secondIpIndex);

                if(Character.toString(eachCharacterInCurrentNodeIp).equals(".")) {
                    periodCount++;
                }
            }
        }

        if (periodCount == 3) {
            System.out.println("Valid Ip, Node " + currentNodeIp + " alive.");
            return true;
        }

        System.out.println("Non valid Ip, Node " + currentNodeIp + " alive.");
        return false;
    }

    public static String[] getAllIpsInIpConfig() {
    String[] allAvailibleIps = {}; //Assumes their will be no more than 100 nodes/clients, but can change to arraylist to fix

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

        // System.out.println("Line count: " + lineCount);


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
                            System.out.println("Server up");

                            for(int indexForEachIp = 0; indexForEachIp < allIps.length; indexForEachIp++) {
                                Thread t1 = new Thread(new udpServerNoteAvailibility(allIps[indexForEachIp], "server up", socketToTransmitData));
                                t1.run();
                            }

                            Thread.sleep(10000); //Will wait 10 seconds before sending node availibility

                            System.out.println("System sending all availible nodes..");

                            for(int indexForEachIp = 0; indexForEachIp < allIps.length; indexForEachIp++) {
                                Thread t1 = new Thread(new udpServerNoteAvailibility(allIps[indexForEachIp], Arrays.toString(allIps), socketToTransmitData));
                                t1.run();
                            }

                            Thread.sleep(10000); //Will wait 10 seconds before crashing
                            System.out.println("Server crash! Server down");

                            // Send server down every 1 minute to all nodes
                            for(int indexForEachIp = 0; indexForEachIp < allIps.length; indexForEachIp++) {
                                Thread t1 = new Thread(new udpServerNoteAvailibility(allIps[indexForEachIp], "server down", socketToTransmitData));
                                t1.run();
                            }

                            Thread.sleep(60000); // Server will go down for 1 minute after it sends all availble nodes

                        } catch(Exception ie) {

                        }

                    }
                },
                TWENTYFIVESECONDSINMILLISECONDS,
                TWENTYFIVESECONDSINMILLISECONDS);

    }

}
