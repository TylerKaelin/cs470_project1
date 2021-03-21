import java.io.File;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Scanner;

public class udpClientCheckForPackets implements Runnable {

    public DatagramSocket createSocket() {
        DatagramSocket socketToTransmitData = null;
        try {
            if(isModeTypeInFile("client server")) {
                socketToTransmitData = new DatagramSocket(1235);
            } else {
                socketToTransmitData = new DatagramSocket(1234);
            }
            System.out.println("Successfully created socket.");
            System.out.println("Socket is: " + socketToTransmitData);
            return socketToTransmitData;
        } catch(Exception e) {
            return  socketToTransmitData;
        }
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
                createSocket().receive(packetToRecieve);
                System.out.println("Success in creating socket to recieve data");
            } catch(Exception e) {

            }

            stringRepresentationOfEachMessage = convertByteMessageToString(byteRepresentationOfMessage).toString();
            if(!stringRepresentationOfEachMessage.equals("")) {
                System.out.println("Server: " + stringRepresentationOfEachMessage);
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
                //System.out.println("The Mode Type is already in the config file");
            } else {
                //System.out.println("The Mode Type was not in the config file");
            }

            myReader.close();
            return isModeTypeInConfig;
        } catch (Exception e) {
            System.out.println("An error occurred. The Ip existence in config file was not checked.");
            e.printStackTrace();
            return false;
        }

    }
}
