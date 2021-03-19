import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class udpClientCheckForPackets implements Runnable {

    public DatagramSocket createSocket() {
        DatagramSocket socketToTransmitData = null;
        try {
            //socketToTransmitData = new DatagramSocket(1235);
            socketToTransmitData = new DatagramSocket(1234);
            System.out.println("Successfully created socket.");
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
}
