package udpclient.model;

import java.util.List;

import udpclient.data.ServerDataType;

public interface ClientFacade {

	public List<? extends ServerData<?>> getData();
	public void setData(int dataId, ServerDataType type, int value);
	public boolean getConnectionState();
	public void setConnectionState(boolean state);
}
