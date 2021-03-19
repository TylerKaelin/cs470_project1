import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.URL;
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
            System.out.println("Sent availible nodes to Ip: " + getIpAddress() + ", List of Availible Nodes: " + getMessage());


            InetAddress myIp;
            if(whatIsNetworkType().equals("truman")) {
                myIp = InetAddress.getByName(getIpAddress().substring(1));
                System.out.println("the substring of supposed ip" + getIpAddress().substring(1));
            } else {
                myIp = InetAddress.getLocalHost();
            }

            System.out.println("Prior to socet creation");
            System.out.println("After socket creation");

//            InetAddress eachNodeIpObject = InetAddress.getByName();
//            System.out.println("After Inet stuff");
//            System.out.println("indv ip: " + eachNodeIpObject);

            byte[] eachNodeIpInBytes = message.getBytes();

            DatagramPacket DpSend = new DatagramPacket(eachNodeIpInBytes, eachNodeIpInBytes.length, myIp, 1235);
            getSocketToTransmitData().send(DpSend);
            System.out.println("After sent");

        } catch(Exception e) {

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
