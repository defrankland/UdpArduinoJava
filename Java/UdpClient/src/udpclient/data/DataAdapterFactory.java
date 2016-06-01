package udpclient.data;

import udpclient.stream.ArduinoUdpStream;
import udpclient.stream.ServerStreamAdapter;

public class DataAdapterFactory {

	public static ServerStreamAdapter createAdapter(String clientDataType){
		
		switch(clientDataType){
		
			case "Arduino":
			default:
				return new ArduinoUdpStream();
		}
	}
}
