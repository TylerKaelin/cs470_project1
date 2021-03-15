// Tyler Kaelin and Logan Morris
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class udpServer
{
    public static void main(String[] args) throws IOException
    {

        DatagramSocket socketToTransmitData = new DatagramSocket(1234);
        byte[] byteRepresentationOfMessage = new byte[65535];

        DatagramPacket packetToRecieve;
        while (true)
        {

            packetToRecieve = new DatagramPacket(byteRepresentationOfMessage, byteRepresentationOfMessage.length);

            socketToTransmitData.receive(packetToRecieve);

            System.out.println("Client: " + convertByteMessageToString(byteRepresentationOfMessage));

            if (convertByteMessageToString(byteRepresentationOfMessage).toString().toLowerCase().equals("close connection"))
            {
                System.out.println("Client has closed the connection. Closing Connection.");
                break;
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
