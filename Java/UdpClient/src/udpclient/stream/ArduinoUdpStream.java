package udpclient.stream;

import java.net.InetSocketAddress;
import java.util.List;

import udpclient.model.ClientFacade;
import udpclient.model.ServerData;

public class ArduinoUdpStream implements ServerStreamAdapter {

	private UdpStream udpStream = new UdpStream();
	
	@Override
	public void adaptData(ClientFacade board) {
		int[] udpData = udpStream.getUdpStreamBuffer();
		
		List<? extends ServerData<?>> data = board.getData();
		
		if (udpData != null){
			int i = 0;
			for(ServerData<?> d: data){
				int val = udpData[i];
				board.setData(d.getId(), d.getType(), val);
				i++;
			}
		}
	}

	@Override
	public void setStreamState(boolean state) {
		udpStream.setState(state);
	}
	
	@Override
	public boolean getStreamState(){
		
		return udpStream.getStreamState();
	}

	@Override
	public void setStreamUpdateTimeMs(int updateTime) {
		udpStream.setUpdateTimeMs(updateTime);
		
	}

	@Override
	public void setSocket(InetSocketAddress socket) {
		udpStream.setSocket(socket);
		
	}

	@Override
	public void setServerSocket(InetSocketAddress serverSocket) {
		udpStream.setServerSocket(serverSocket);
		
	}
}
