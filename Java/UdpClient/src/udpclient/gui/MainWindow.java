package udpclient.gui;


import java.awt.Color;
import javax.swing.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.ImageIcon;

import udpclient.model.ServerData;

public class MainWindow implements DataDisplayAdapter, DataControllerAdapter{
	JFrame frame = new JFrame("Arduino Monitor Window");
	JButton btnUdpControl = new JButton();
	
	private JLabel[] lblPinVal;
	
	private boolean isConnected = false;
	
	int numAnalogPins;
	int numDigitalPins;
	
	public MainWindow(int numAnalogPins, int numDigitalPins){
		
		this.numAnalogPins = numAnalogPins;
		this.numDigitalPins = numDigitalPins;
		
		int lblCol1XOffset = 45;
		int lblCol1YOffset = 544;
		int offsetIncrement = 25;
		int lblCol2XOffset = 775;
		int lblCol2YOffset = 730;

		frame.setContentPane(new JLabel(new ImageIcon(getClass().getResource("/udpclient/resources/ArduinoUno.png"))));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		
		lblPinVal = new JLabel[numDigitalPins + numAnalogPins];
		
		//setup & add all the value labels
		for(int i = 0; i < numDigitalPins + numAnalogPins; i++){
			lblPinVal[i] = new JLabel("#");
			lblPinVal[i].setForeground(Color.WHITE);
			frame.getContentPane().add(lblPinVal[i]);
			if (i < numAnalogPins){
				lblPinVal[i].setBounds(lblCol1XOffset, lblCol1YOffset + offsetIncrement*i, lblPinVal[i].getPreferredSize().width+20, lblPinVal[i].getPreferredSize().height);
			}
			else{
				if (i < numDigitalPins + numAnalogPins - 2){
					lblPinVal[i].setBounds(lblCol2XOffset, lblCol2YOffset - offsetIncrement*(i-numAnalogPins), lblPinVal[i].getPreferredSize().width+20, lblPinVal[i].getPreferredSize().height);
				}
				else{
					lblPinVal[i].setBounds(lblCol2XOffset, lblCol2YOffset - (3+offsetIncrement)*(i-numAnalogPins), lblPinVal[i].getPreferredSize().width+20, lblPinVal[i].getPreferredSize().height);
				}
			}
		}
		
		btnUdpControl = new JButton("Connect");
		btnUdpControl.setBackground(Color.GREEN);
		
		btnUdpControl.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(btnUdpControl.getText() == "Connect"){
					btnUdpControl.setText("Disconnect");
					btnUdpControl.setBackground(Color.RED);
					isConnected = true;
				}
				else {
					btnUdpControl.setText("Connect");
					btnUdpControl.setBackground(Color.GREEN);
					isConnected = false;
				}
			}
		});
		
		frame.getContentPane().add(btnUdpControl);
		btnUdpControl.setBounds(25, 100, btnUdpControl.getPreferredSize().width+20, btnUdpControl.getPreferredSize().height);
	}
	
	public void setTitle(String title){
		frame.setTitle(title);
	}
	
	@Override
	public void show(){
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
	}
	
	@Override
	public void adaptData(List<? extends ServerData<?>> data) {
			
		for(int i = 0; i < numDigitalPins + numAnalogPins; i++){
			
			if(i < numAnalogPins){
				Integer val = (Integer) data.get(i).getValue();
				lblPinVal[i].setText(val.toString());
			}
			else{
				Integer val = (Integer) data.get(i).getValue();
				lblPinVal[i].setText(val.toString());
			}
		}
	}
	
	@Override
	public int getRefreshTimeMs() {
		return 100;
	}

	@Override
	public boolean getConnectionState() {
		return isConnected;
	}
}
