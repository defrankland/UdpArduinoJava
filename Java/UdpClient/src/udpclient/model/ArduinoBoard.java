package udpclient.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import udpclient.data.ServerDataType;

public class ArduinoBoard implements ClientFacade {
	
	public static final int NUM_ANALOG_PINS = 6;
	public static final int NUM_DIGITAL_PINS = 10;
	
	private List<Pin<Integer>> pins = new ArrayList<>();
	private boolean isConnected;

	public ArduinoBoard(){

		isConnected = false; 
		
		for(int i = 0; i < NUM_ANALOG_PINS; i++){
			pins.add( new Pin<Integer>(i,ServerDataType.ANALOG_PIN, 0));
		}
		
		for(int i = 0; i < NUM_DIGITAL_PINS; i++){
			pins.add(new Pin<Integer>(i, ServerDataType.DIGITAL_PIN, 0));
		}
	}
	
	@Override
	public boolean getConnectionState(){
		return isConnected;
	}
	
	@Override
	public void setConnectionState(boolean state){
		isConnected = state;
	}
	
	@Override
	public void setData(int dataId, ServerDataType type, int value){
		
		int pinIndex = Collections.binarySearch(pins, 
												new Pin<Integer>(dataId, type, 0), 
												new ArduinoBoardPinComparator());
		
		pins.get(pinIndex).setValue(value);
		
	}
	
	@Override
	public List<? extends ServerData<Integer>> getData(){
		
		Collections.sort(pins, new ArduinoBoardPinComparator());
		return pins;
	}
	
	class ArduinoBoardPinComparator implements Comparator<Pin<Integer>>{

		@Override
		public int compare(Pin<Integer> one, Pin<Integer> two) {
			
			int compareByType = one.getType().compareTo(two.getType());
			if(compareByType != 0){
				return compareByType;
			}
			
			return one.getId().compareTo(two.getId());
		}
		
	}
}
