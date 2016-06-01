package udpclient.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import udpclient.stream.UdpStream;

public class UdpStreamTest {
	
	UdpStream stream;
	
	@Before
	public void testInit(){
		
		stream = new UdpStream();
	}
	
	@Test
	public void setStreamStateSetsState(){
		stream.setState(true);
		assertEquals(true, stream.getStreamState());
		
		stream.setState(false);
		assertEquals(false, stream.getStreamState());
	}
	

}
