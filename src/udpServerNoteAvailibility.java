import java.net.InetAddress;
import java.net.DatagramSocket;
import java.net.DatagramPacket;


public class udpServerNoteAvailibility implements Runnable {

    private String ipAddress;
    private String message;

    public udpServerNoteAvailibility(String userIpAddress, String message) {
        this.ipAddress = userIpAddress;
        this.message = message;
    }

    private String getIpAddress() {
        return this.ipAddress;
    }

    private String getMessage() {
        return this.message;
    }

    @Override
    public void run() {

        try {
            System.out.println("Sent availible nodes to Ip: " + this.ipAddress + ", List of Availible Nodes: " + this.message);

            DatagramSocket socketToTransmitData = new DatagramSocket(1234);
            InetAddress eachNodeIpObject = InetAddress.getByName(getIpAddress());
            byte[] eachNodeIpInBytes = message.getBytes();
            DatagramPacket DpSend = new DatagramPacket(eachNodeIpInBytes, eachNodeIpInBytes.length, eachNodeIpObject, 1234);
            socketToTransmitData.send(DpSend);

        } catch(Exception e) {

        }
    }
}
