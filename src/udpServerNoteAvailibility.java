//Tyler Kaelin and Logan Morris
import java.io.File;
import java.net.InetAddress;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.util.Scanner;


public class udpServerNoteAvailibility implements Runnable {

    private String ipAddress;
    private String message;
    private DatagramSocket socketToTransmitData;

    public udpServerNoteAvailibility(String userIpAddress, String message, DatagramSocket userSocketToTransmitData) {
        this.ipAddress = userIpAddress;
        this.message = message;
        this.socketToTransmitData = userSocketToTransmitData;
    }

    private String getIpAddress() {
        return this.ipAddress;
    }

    private String getMessage() {
        return this.message;
    }

    private DatagramSocket getSocketToTransmitData() {
        return this.socketToTransmitData;
    }

    @Override
    public void run() {

        try {

            InetAddress myIp;
            if(whatIsNetworkType().equals("truman")) {
                myIp = InetAddress.getByName(getIpAddress().substring(1));
            } else {
                myIp = InetAddress.getLocalHost();
            }

            byte[] eachNodeIpInBytes = message.getBytes();

            DatagramPacket DpSend = new DatagramPacket(eachNodeIpInBytes, eachNodeIpInBytes.length, myIp, 1235);
            getSocketToTransmitData().send(DpSend);
            System.out.println("Sent message to: " + getIpAddress());

        } catch(Exception e) {

        }
    }


    public static String whatIsNetworkType() {
        String eachNetworkType = "";
        try {
            File NetworkTypeConfig = new File("networkType.txt");
            Scanner myReader = new Scanner(NetworkTypeConfig);

            // While reading each IP address
            while (myReader.hasNextLine()) {
                eachNetworkType = myReader.nextLine();
                return eachNetworkType;

            }


            myReader.close();
        } catch (Exception e) {
            System.out.println("An error occurred. The Ip existence in config file was not checked.");
            e.printStackTrace();
            return eachNetworkType;
        }
        return eachNetworkType;
    }
}
