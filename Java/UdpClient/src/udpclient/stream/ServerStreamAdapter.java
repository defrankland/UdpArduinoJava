package udpclient.stream;

import java.net.InetSocketAddress;

import udpclient.model.ClientFacade;

public interface ServerStreamAdapter {
	public void adaptData(ClientFacade board);
	public void setStreamState(boolean state);
	public boolean getStreamState();
	public void setStreamUpdateTimeMs(int updateTime);
	public void setSocket(InetSocketAddress socket);
	public void setServerSocket(InetSocketAddress serverSocket);
}
