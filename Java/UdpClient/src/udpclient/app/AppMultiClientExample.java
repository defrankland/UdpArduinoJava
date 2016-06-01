package udpclient.app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;
import udpclient.data.DataAdapterFactory;
import udpclient.gui.MainWindow;
import udpclient.model.ArduinoBoard;
import udpclient.model.ClientFacade;
import udpclient.stream.ServerStreamAdapter;

public class AppMultiClientExample {

	static final int numClients = 2;
	List<Client> clients = new ArrayList<Client>();
	
	
	public AppMultiClientExample(){
		
		for(int i = 0; i < numClients; i++){
			clients.add(new Client(
					new MainWindow(ArduinoBoard.NUM_ANALOG_PINS, ArduinoBoard.NUM_DIGITAL_PINS),
					"Arduino",
					new ArduinoBoard()
					));
			
			clients.get(0).dataSource.setSocket(new InetSocketAddress("192.168.1.168", 5556));
			clients.get(0).gui.setTitle("Arduino 1");
		}
	}
	
	public static void main(String[] args) throws Exception {
		final AppMultiClientExample app = new AppMultiClientExample();
		
		app.runApp();
	}
	
	private void runApp(){

		for(Client c: clients){
			c.startListener();
			c.gui.show();
		}
	}
	
	
	public class Client{
		public MainWindow gui; 
		public ServerStreamAdapter dataSource;
		public ClientFacade clientModel;
		Timer timer;
		
		public Client(MainWindow gui,String clientType,  ClientFacade clientModel){
			this.gui = gui;
			this.dataSource = DataAdapterFactory.createAdapter(clientType);
			this.clientModel = clientModel;
			
			dataSource.setStreamUpdateTimeMs(gui.getRefreshTimeMs());
		}
		
		public void startListener(){
			//Timer setup
			ActionListener taskPerformer = new ActionListener() {
				public void actionPerformed(ActionEvent evt) {

						
					if(gui.getConnectionState() != dataSource.getStreamState()){
						dataSource.setStreamState(gui.getConnectionState());
					}
					
					
						if (dataSource.getStreamState()){
							dataSource.adaptData(clientModel);
							gui.adaptData(clientModel.getData());
						}
				}
			};
			timer = new Timer(gui.getRefreshTimeMs(), taskPerformer);
			timer.setRepeats(true);
	        timer.start();
		}
	}
}
