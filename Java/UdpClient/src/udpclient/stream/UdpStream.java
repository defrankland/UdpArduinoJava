package udpclient.stream;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;


public class UdpStream implements Runnable{
	
	private boolean isConnected = false;
	private int[] latestUdpData;
	
	private Thread udpThread;
	
	private InetSocketAddress udpSock = new InetSocketAddress("192.168.1.168", 5555);
	private InetSocketAddress svrUdpSock = new InetSocketAddress("192.168.1.177", 8888);
	
	static final int packetBufferSize = 255;
	
	private int updateTimeMs = 100;
	
	public void setState(boolean state){
		
		if(state == true){
			isConnected = true;
			
			if(  (udpSock != null)
		      && (svrUdpSock != null)){
				System.out.println("Init udp");
	
				try {
					udpThread = new Thread(this);
					udpThread.start();
				} catch (Exception e) {
					System.out.println("Failed to start UDP connection.");
				}
			}
			else {
				System.out.println("Udp could not connect. Check socket address.");
			}
		}
		else {
			isConnected = false;
			System.out.println("udp stopped");
		}	
	}
	
	public void setSocket(InetSocketAddress socket){
		udpSock = socket;
	}
	
	public void setServerSocket(InetSocketAddress serverSocket){
		svrUdpSock = serverSocket;
	}
	
	
	public boolean getStreamState(){
		return isConnected;
	}
	
	private void updateServerData(byte[] payload){
		
		latestUdpData = new int[payload.length];
		
		for (int i = 0; i < latestUdpData.length; i++){
			
			//convert byte to int
			latestUdpData[i] = payload[i];
			
			//un-sign
			if(latestUdpData[i] < 0){
				latestUdpData[i] = 127 + -latestUdpData[i];
			}
		}
	}
	
	public int[] getUdpStreamBuffer(){
		return latestUdpData;
	}
	
	public void setUpdateTimeMs(int updateTime){
		updateTimeMs = updateTime;
	}
	
		
	@Override
	public void run() {
		System.out.println("Udp client starting...");
		
		DatagramSocket socket = null;
		
		try{
			socket = new DatagramSocket(null);
		} catch (SocketException e){
			System.out.println("Failed to create socket: " + e);
		}
		
		while((socket != null) && isConnected){
			if(!socket.isBound()){
				try{
					socket.bind(udpSock);
					socket.setReceiveBufferSize(1);
					socket.setSoTimeout(500);
					requestUdpConnection();
					System.out.println("Bind successful");
				}catch(Exception e){
					System.out.println("Udp failed to bind: " + e);
				}
			}
			else {
				try{
					DatagramPacket packet = new DatagramPacket(new byte[packetBufferSize], packetBufferSize);
					
					socket.receive(packet);
					updateServerData(packet.getData());
					renewConnection();
				} catch(IOException e){
					System.out.println("Attempting to re-connect to server");
					try {
						requestUdpConnection();
					} catch (IOException e1) {
						System.out.println("Re-connection attempt failed" + e);
					}
				} catch(Exception e){
					System.out.println("Error reading. " + e);
				}
			}
			
			try{
				Thread.sleep(updateTimeMs); 
			} catch(InterruptedException e){
				System.out.println(e);
			}
		}
		socket.disconnect();
		socket.close();
	}
	
	public void requestUdpConnection() throws IOException{

		byte[] udpDataReq = new byte[]{121, 11};
		DatagramSocket svrSock;
		DatagramPacket svrReqPacket;
		
		try {
			svrReqPacket = new DatagramPacket(udpDataReq, udpDataReq.length, svrUdpSock);
			svrSock = new DatagramSocket();
			svrSock.send(svrReqPacket);
			svrSock.close();
		} catch (SocketException e) {
			System.out.println("Error on UDP_CLIENT_REQ " + e);
		}
        
	}
	
	public void renewConnection() throws IOException{
		byte[] udpDataReq = new byte[]{121,12};
		DatagramSocket svrSock;		
		DatagramPacket svrReqPacket;
		
		try {
			svrReqPacket = new DatagramPacket(udpDataReq, udpDataReq.length, svrUdpSock);
			svrSock = new DatagramSocket();
			svrSock.send(svrReqPacket);
			svrSock.close();
		} catch (SocketException e) {
			System.out.println(e);
		}
        
	}
}
