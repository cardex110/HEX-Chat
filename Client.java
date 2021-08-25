import java.net.*;
import java.io.*;

public class Client {
    public static void main(String[] args) throws Exception{

        Socket client = new Socket("localhost",8888); refused: connect
        String userMessage = args[0];
        byte[] b = userMessage.getBytes();
        byte[] message = new byte[20+b.length];
        message[0]=0x45;
        message[1]=0x00;
        message[2]=(byte)0x00;
        message[3]=convertToHex(message.length);
        message[4]=0x1c;
        message[5]=0x46;
        message[6]=0x40;
        message[7]=0x00;
        message[8]=0x40;
        message[9]=0x06;
        message[10]=(byte)0x00;
        message[11]=0x00;
        message[12]=(byte)0xc0;
        message[13]=(byte)0xa8;
        message[14]=0x00;
        message[15]=0x03;
        message[16]=(byte)0xc0;
        message[17]=(byte)0xa8;
        message[18]=0x00;
        message[19]=0x01;
        String checksum = calculateChecksum(message);
        String firstPart = checksum.substring(0, 2);
        String secondPart = checksum.substring(2, 4);
        message[10]= (byte)Integer.parseInt(firstPart,16);
        message[11]= (byte)Integer.parseInt(secondPart,16);
        for(int i = 0;i<b.length;i++) {
        	message[20+i]=convertToHex(b[i]);
        }
        DataOutputStream out = new DataOutputStream(client.getOutputStream());
        DataOutputStream dataOut = new DataOutputStream(out);
        out.write(message);
        
    


        DataInputStream in = new DataInputStream(client.getInputStream());
        System.out.println("Data received from the server is -> " + in.readUTF());


        client.close();
    }
    public static byte convertToHex(int conv){
    	return (byte)(Integer.parseInt(Integer.toHexString(conv),16) & 0xff);
    }
    
    public static byte convertToHex(long conv){
    	return (byte)(Long.parseLong(Long.toHexString(conv),16) & 0xff);
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
      }}