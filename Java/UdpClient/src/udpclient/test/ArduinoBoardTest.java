package udpclient.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import udpclient.data.ServerDataType;
import udpclient.model.ArduinoBoard;
import udpclient.model.ServerData;

public class ArduinoBoardTest {

	ArduinoBoard board;
	
	@Before
	public void testInit(){
		
		board = new ArduinoBoard();
	}
	
	@Test
	public void getDataSortsCorrectly(){
		boolean testFail = false;
		List<? extends ServerData<?>> lst = board.getData();
		
		for (int i = 0; i < ArduinoBoard.NUM_ANALOG_PINS; i++){
			
			if ( i < ArduinoBoard.NUM_ANALOG_PINS){
				if(  (i != lst.get(i).getId())
				  || (lst.get(i).getType() != ServerDataType.ANALOG_PIN)){
					
					testFail = true;
				}
			}
			else{
				if(  ((i-ArduinoBoard.NUM_ANALOG_PINS) != lst.get(i).getId())
				  || (lst.get(i).getType() != ServerDataType.DIGITAL_PIN)){
					
					testFail = true;
				}
			}
		}
		
		assertEquals(false, testFail);
	}
	
}
