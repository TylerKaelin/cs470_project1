import java.io.File;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Scanner;

public class udpClientCheckForPackets implements Runnable {

    DatagramSocket socketToTransmitData;

    udpClientCheckForPackets(DatagramSocket socket) {
        socketToTransmitData = socket;
    }

//    public DatagramSocket createSocket() {
//            DatagramSocket socketToTransmitData = null;
//            try {
//                if(isModeTypeInFile("client server")) {
//                    socketToTransmitData = new DatagramSocket(1235);
//                    System.out.println("Socket on 1235 for client server");
//                } else {
//                    socketToTransmitData = new DatagramSocket(1234);
//                    System.out.println("Socket on 1234 for peer to peer");
//                }
//                System.out.println("Successfully created socket.");
//                return socketToTransmitData;
//            } catch(Exception e) {
//
//            }
//            return socketToTransmitData;
//        }
//        DatagramSocket socketToTransmitData = null;
//        try {
//            if(isModeTypeInFile("client server")) {
//                socketToTransmitData = new DatagramSocket(1235);
//            } else {
//                socketToTransmitData = new DatagramSocket(1234);
//            }
//            System.out.println("Successfully created socket.");
//            System.out.println("Socket is: " + socketToTransmitData.getPort());
//            return socketToTransmitData;
//        } catch(Exception e) {
//            return  socketToTransmitData;
//        }


//    DatagramSocket socketToTransmitData = null;

    @Override
    public void run() {


        byte[] byteRepresentationOfMessage = new byte[65535];

        String stringRepresentationOfEachMessage = "";
        DatagramPacket packetToRecieve;

//        System.out.println("before boolean " + alreadyExecuted);
//
//        if(!alreadyExecuted) {
//            socketToTransmitData = createSocket();
//            System.out.println("executed creation of socket");
//            alreadyExecuted = true;
//        }
//
//        System.out.println(" after boolean " + alreadyExecuted);


        while (true)
        {
            packetToRecieve = new DatagramPacket(byteRepresentationOfMessage, byteRepresentationOfMessage.length);

            try {
                socketToTransmitData.receive(packetToRecieve);
                System.out.println("Success in creating socket to recieve data");
            } catch(Exception e) {
                System.out.println("error" + e);
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
