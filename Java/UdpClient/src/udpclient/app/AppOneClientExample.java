package udpclient.app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import udpclient.data.DataAdapterFactory;
import udpclient.gui.MainWindow;
import udpclient.model.ArduinoBoard;
import udpclient.model.ClientFacade;
import udpclient.stream.ServerStreamAdapter;

public class AppOneClientExample {

	MainWindow gui; 
	ServerStreamAdapter dataSource;
	ClientFacade client = new ArduinoBoard();
	Timer timer;
	String clientType = "Arduino";
	
	public AppOneClientExample(){
		gui = new MainWindow(ArduinoBoard.NUM_ANALOG_PINS, ArduinoBoard.NUM_DIGITAL_PINS);
		dataSource = DataAdapterFactory.createAdapter(clientType);
		
		dataSource.setStreamUpdateTimeMs(gui.getRefreshTimeMs());
	}
	
	public static void main(String[] args) throws Exception {
		final AppOneClientExample app = new AppOneClientExample();
		
		app.runApp();
	}
	
	private void runApp(){
		gui.show();
		
		//Timer setup
		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {

				if(gui.getConnectionState() != dataSource.getStreamState()){
					dataSource.setStreamState(gui.getConnectionState());
				}
				
				if (dataSource.getStreamState()){
					dataSource.adaptData(client);
					gui.adaptData(client.getData());
				}
			}
		};
		
		timer = new Timer(gui.getRefreshTimeMs(), taskPerformer);
		timer.setRepeats(true);
        timer.start();
	}

}
