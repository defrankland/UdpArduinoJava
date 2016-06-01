#include <SPI.h>
#include <Ethernet.h>
#include <EthernetUdp.h>

#define MC_STD (0x00)
#define MC_PRG (0x01)
#define MC_TST (0x02)
#define FC_GET_STD  (0x00)
#define FC_SET_PRG  (0x01)
#define FC_SET_SND  (0x02)
#define EC_NONE     (0x00)
#define EC_CRC      (0x01)
#define EC_TIMEOUT  (0x02)
#define EC_PACKET   (0x03)

//UDP connection defines

//Client control commands

//UDP connection variables
byte ethMac[] ={ 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };  //Ethernet shield MAC address
IPAddress ip(192,168,1, 4);  //Ethernet shield IP address
IPAddress gateway(192,168,1, 1);
IPAddress subnet(255, 255, 255, 0);
IPAddress hostIp(192,168,1,168);
IPAddress dns1(0,0,0,0);
unsigned int localPort = 5544; //the local port for incoming client requests
unsigned int broadcastPort = 2112;
unsigned int udpServerPort = 5545; //the port to send packets to on the client machine
EthernetUDP Udp; //the UDP connection object
EthernetUDP UdpBroadcast;

//boolean packetRecvd[1000];

long internalIdTracker = 0;

long timeoutMon = 0;

//Data arrays for building UDP packets
byte packetOutBuffer[UDP_TX_PACKET_MAX_SIZE];


/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
~  setup() 
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
void setup(){
  Serial.begin(9600);
  Serial.println("Arduino initializing");
  
  Ethernet.begin(ethMac, ip);
  Ethernet.begin(ethMac, ip, dns1, gateway, subnet); 
  
  UdpBroadcast.begin(broadcastPort);
  Udp.begin(localPort);
//  for(int i = 0; i<1000; i++){
//    packetRecvd[i] = false; 
//  }
}

void loop(){
  
  checkHostMsgs();
   delay(5);
}


unsigned int calcCrc(byte *b, int bLen){
    unsigned int crc = 0;
    for(int i = 0; i < bLen - 2; i++){
      crc ^= b[i];
    }
    return crc;
}
  
  
/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
~  checkHostMsgs() - monitors the incoming UDP packets and checks for requests. 
~
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
void checkHostMsgs(){
 int packetSize = UdpBroadcast.parsePacket();
      
      
    //read broadcast buffer
   if(packetSize){
      int respSize = 10;
      byte packetInBuffer[packetSize];
      //IPAddress remote = UdpBroadcast.remoteIP(); //IP address of the client
      //byte remoIpAddr[] = {remote[0],remote[1],remote[2],remote[3]};
      UdpBroadcast.read(packetInBuffer, packetSize);
      byte respBuf[respSize];
      unsigned int recvdCrc = packetInBuffer[packetSize-2] << 8 | packetInBuffer[packetSize-1];
      unsigned int crc = calcCrc(packetInBuffer, packetSize);
      
      int module = packetInBuffer[0];
      int FC = (packetInBuffer[1]<<8) | packetInBuffer[2];
      int packetId = (packetInBuffer[3]<<8) | packetInBuffer[4];
//      int Error = packetInBuffer[5];
//      int ByteCnt = (packetInBuffer[6]<<8) | packetInBuffer[7];
 
//      Serial.print("Module: "); Serial.print(module);
//      Serial.print(", FuncCOde: "); Serial.print(FC);
      Serial.print(", PackId: "); Serial.println(packetId);
//      Serial.print(", ErrCode: "); Serial.print(Error);
//      Serial.print(", Bytes: "); Serial.println(ByteCnt);
      
      
     
    
      if(crc == recvdCrc){
        
        for(int i = 0; i < respSize-2; i++){
          respBuf[i] = packetInBuffer[i];
        }
        if(FC == 0x01){
          if(packetId+1 != internalIdTracker){
            respBuf[5] = EC_PACKET;
            //missedPackets[missedPacketIdx] = internalIdTracker;
            
          }
          else{
            internalIdTracker++; 
            //packetRecvd[packetId] = true;
          }
        }
        else if(FC == 0x03){
          Serial.println("FC 3 Received");
          for(int i = 0; i < 1000; i++){
              respBuf[3] = i >> 8;
              respBuf[4] = i & 0xFF;
              i = 9999;
          }
        }
     
        
        crc = calcCrc(respBuf, respSize);
        respBuf[respSize-2] = crc >> 8;
        respBuf[respSize-1] = crc & 0xFF;
        
        Udp.beginPacket(hostIp, udpServerPort);
        Udp.write(respBuf, respSize);
        Udp.endPacket();
        
        //Serial.print("Total read-to-write time: ");
        //Serial.println(millis() - xferTstTime);
 
        
      }
  }

  
  //read unicast buffer
//  packetSize = Udp.parsePacket();
//  if(packetSize){
//    byte packetInBuffer[UDP_TX_PACKET_MAX_SIZE];
//    IPAddress remote = Udp.remoteIP(); //IP address of the client
//      byte remoIpAddr[] = {remote[0],remote[1],remote[2],remote[3]};
//      Udp.read(packetInBuffer, UDP_TX_PACKET_MAX_SIZE);
//      Serial.println("Unicast recvd");
//      
//      Udp.beginPacket(remoIpAddr, udpServerPort);
//      Udp.write("Arduino unicast received\n");
//      Udp.endPacket();
//      delay(1);
//  }
     
  
}


