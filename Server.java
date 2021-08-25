import java.net.*;
import java.util.Arrays;
import java.io.*;

public class Server extends Thread {
	
	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
	
    public static void main(String[] args) throws Exception {
    	InetAddress addr = InetAddress.getByName("localhost");
        System.out.println("Server Listening on 8888");
        ServerSocket serverSocket = new ServerSocket(8888);
        serverSocket.setSoTimeout(1000 * 60 * 60);
        Socket server = serverSocket.accept();


        DataInputStream in = new DataInputStream(server.getInputStream());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte buffer[] = new byte[8192];
        baos.write(buffer, 0 , in.read(buffer));
        
        byte result[] = baos.toByteArray();
        byte payload[] = Arrays.copyOfRange(result, 20, result.length);
        if(Integer.valueOf(calculateChecksum(Arrays.copyOfRange(result, 0, 20)))==0) {
        	
        
        System.out.print("Les donnees recues de 192.168.0.3 sont ");
        for(int i = 20; i<result.length;i++) {
        	System.out.print((char)result[i]);
        }
        System.out.print(". Les donnees ont "+payload.length*8+" bits ou "+payload.length+" octets. La longueur totale du paquet est de "+result.length+" octets. La"
        		+ " verification de la somme de controle confirme que le paquet recu est authentique.\r\n"
        		+ "");}else {System.out.println("La verification de la somme de controle montre que le paquet recu est corrompu. Paquet jete!");}
        DataOutputStream out = new DataOutputStream(server.getOutputStream());
        out.writeUTF("Welcome and Bye Client!");

        serverSocket.close();
    }
    
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
    
    public static String calculateChecksum(byte[] buf) {
        int length = buf.length;
        int i = 0;

        long sum = 0;
        long data;
        while (length > 1) {
          data = (((buf[i] << 8) & 0xFF00) | ((buf[i + 1]) & 0xFF));
          sum += data;
          if ((sum & 0xFFFF0000) > 0) {
            sum = sum & 0xFFFF;
            sum += 1;
          }

          i += 2;
          length -= 2;
        }

        if (length > 0) {
          sum += (buf[i] << 8 & 0xFF00);
          if ((sum & 0xFFFF0000) > 0) {
            sum = sum & 0xFFFF;
            sum += 1;
          }
        }

        sum = ~sum;
        sum = sum & 0xFFFF;
        String hexString=Long.toHexString(sum);
        return hexString;
      }
}
		