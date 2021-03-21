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

//        String[] allIps = getAllIpsInIpConfig().clone();
        initiateAllNodeAvailibility(socketToTransmitData);

        InetAddress serverIp = InetAddress.getByName(GetPublicIp());
        System.out.println("Servers Ip: " + serverIp);


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
            System.out.println("Each character in ip: " + eachCharacterInCurrentNodeIp);
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
            System.out.println("Valid Ip");
            return true;
        }

        System.out.println("Non valid Ip");
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

    public static void initiateAllNodeAvailibility(DatagramSocket socketToTransmitData) {


        final int TWENTYFIVESECONDSINMILLISECONDS = 25000;
        Timer t = new Timer();

        t.schedule(
                new TimerTask()
                {
                    public void run()
                    {

                        try {
                            System.out.println("System sending all availible nodes..");

                            String[] allIps = getAllIpsInIpConfig().clone();
                            System.out.println("Length of all ips: " + allIps.length);
                            for(int indexForEachIp = 0; indexForEachIp < allIps.length; indexForEachIp++) {
                                Thread t1 = new Thread(new udpServerNoteAvailibility(allIps[indexForEachIp], Arrays.toString(allIps), socketToTransmitData));
                                t1.run();
                            }


                        } catch(Exception ie) {

                        }

                    }
                },
                TWENTYFIVESECONDSINMILLISECONDS,
                TWENTYFIVESECONDSINMILLISECONDS);

    }

}
