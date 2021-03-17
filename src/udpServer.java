// Tyler Kaelin and Logan Morris
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Scanner;

public class udpServer
{
    public static void main(String[] args) throws IOException
    {

        DatagramSocket socketToTransmitData = new DatagramSocket(1234);
        byte[] byteRepresentationOfMessage = new byte[65535];

        String stringRepresentationOfEachMessage;
        DatagramPacket packetToRecieve;
        createIpConfigFile();


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
                    System.out.println("The Ip is already in the config file");
                    ipIsInConfig = true;
                    break;
                }

                // The Ip was not in the config file
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
        for(int ipIndex = 0; ipIndex < currentNodeIp.length(); ipIndex++) {
            eachCharacterInCurrentNodeIp = currentNodeIpCharRepresentation[ipIndex];

            if (Character.toString(eachCharacterInCurrentNodeIp).equals(".")) {
                periodCount++;
            }
        }

        if (periodCount == 3) {
            System.out.println("Valid Ip");
            return true;
        }

        System.out.println("Non valid Ip");
        return false;
    }
}
