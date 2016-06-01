package udpclient.gui;

import java.util.List;

import udpclient.model.ServerData;

public interface DataDisplayAdapter {
	public void adaptData(List<? extends ServerData<?>> data);
	public void show();
}
