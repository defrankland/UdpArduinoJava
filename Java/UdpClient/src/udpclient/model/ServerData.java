package udpclient.model;

import udpclient.data.ServerDataType;

public abstract class ServerData<T> {

	protected Integer dataId;
	protected T dataValue;
	protected ServerDataType dataType;
	
	
	public abstract T getValue();
	
	public abstract void setValue(T value);
	
	public abstract Integer getId();
	
	public abstract ServerDataType getType();
}
