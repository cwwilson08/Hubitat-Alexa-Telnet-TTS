import groovy.json.JsonSlurper
/**
*  Alexa Telnet TTS
*
*
*  Copyright 2018 Chris Wilson
*
*  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License. You may obtain a copy of the License at:
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
*  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
*  for the specific language governing permissions and limitations under the License.
*
*  10/07/2018 - Added support to display various device info - Chris Wilson
*  9/2/2018 - Initial Release - Chris Wilson
*/

preferences {
		input "ip", "text", title: "RPi IP Address", description: "Ip address of your pi", required: true, displayDuringSetup: true
		input "port", "text", title: "RPi Telnet Port", description: "Port for your pi", required: true, displayDuringSetup: true
		input "username", "text", title: "RPi Username", description: "Input username for your pi", required: true, displayDuringSetup: true
		input "password", "text", title: "RPi Password", description: "Input password for your pi", required: true, displayDuringSetup: true
        input "echoName", "text", title: "Echo Device Name", description: "Name of your device", required: true, displayDuringSetup: true
        input "ttsPath", "text", title: "Path to Alexa TTS script", description: "Path to Alexa TTS script i.e. /opt/ha-alexa-tts-master", required: true, displayDuringSetup: true
        input "cookPath", "text", title: "Path to Alexa TTS cookie", description: "Path to Alexa TTS cookie - default /tmp", defaultValue: "/tmp", required: false, displayDuringSetup: true
}
metadata {
    definition (name: "Alexa Telnet TTS", namespace: "cw", author: "Chris Wilson") {
        capability "Speech Synthesis"
        capability "Telnet"
        capability "Switch"
        capability "Refresh"
        capability "Music Player"
        
        command "getDevices"
      
        attribute "Telnet", ""
            }        
    
}

def installed() {
    initialize()
     
}

def updated() {
    
    initialize() 
   
}

def initialize() {
    log.debug "Connecting to telnet - IP = ${ip}, Port = ${port.toInteger()}, Username = ${username}, Password = ${password}"
    telnetConnect([terminalType: 'VT100'], ip, port.toInteger(), username, password)
   }


def getDevices(){
    if (cookPath == null){
        cookPath = "/tmp"
        
        }
    // def msg = 'cat /opt/alexacookie/alexa.devicelist.json | jq \'.devices\''
    //def msg = 'jq -r \'.devices[].accountName\' /opt/alexacookie/.alexa.devicelist.json > device.txt && nl device.txt > numbereddevice.txt &&cat numbereddevice.txt'
    def msg = "jq -r \'.devices[].accountName\' " + "${cookPath}" + "/.alexa.devicelist.json > device.txt && nl device.txt > numbereddevice.txt && cat numbereddevice.txt"
    // def msg = 'nl /opt/alexacookie/test.txt'
    sendMsg(msg)
}
def on(){
    //def msg = "${ttsPath}" + '/alexa_remote_control.sh -d "' + "${echoName}" + '" -q'
    //def msg = "cat /opt/alexacookie/volume.txt | jq -r"
    //def msg = "/opt/ha-alexa-tts-master/alexa_remote_control.sh -d \"chris's Echo\" -q > /opt/alexacookie/deviceinfo.txt | jq '.volume' && sed -i 1d /opt/alexacookie/deviceinfo.txt && jq '.volume' /opt/alexacookie/deviceinfo.txt > /opt/alexacookie/volume.txt && sed -i '/null/d' /opt/alexacookie/volume.txt && sed -i -e 's/^/volume:/' /opt/alexacookie/volume.txt && echo test >> /opt/alexacookie/volume.txt && jq '.muted' /opt/alexacookie/deviceinfo.txt > /opt/alexacookie/muted.txt && sed -i '/null/d' /opt/alexacookie/muted.txt && sed -i -e 's/^/muted:/' /opt/alexacookie/muted.txt && jq -r '.currentState' /opt/alexacookie/deviceinfo.txt > /opt/alexacookie/currentstate.txt && sed -i '/null/d' /opt/alexacookie/currentstate.txt && sed -i -e 's/^/currentState:/' /opt/alexacookie/currentstate.txt && jq  '.playerInfo.infoText' /opt/alexacookie/deviceinfo.txt > /opt/alexacookie/trackinfo.txt && sed -i 's/[\"{},]//g' /opt/alexacookie/trackinfo.txt && sed -i '/^\$/d' /opt/alexacookie/trackinfo.txt && sed -i '/null/d' /opt/alexacookie/trackinfo.txt && sed \"s/^[ \t]*//\" -i /opt/alexacookie/trackinfo.txt &&  cat /opt/alexacookie/volume.txt /opt/alexacookie/currentstate.txt /opt/alexacookie/muted.txt /opt/alexacookie/trackinfo.txt> /opt/alexacookie/status.txt"
    def msg = "/opt/ha-alexa-tts-master/alexa_remote_control.sh -d \"chris's Echo Dot\" -q > /opt/alexacookie/deviceinfo.txt && sed -i 1d /opt/alexacookie/deviceinfo.txt && jq '.volume' /opt/alexacookie/deviceinfo.txt > /opt/alexacookie/volume.txt && sed -i '/null/d' /opt/alexacookie/volume.txt && sed -i -e 's/^/volume:/' /opt/alexacookie/volume.txt && jq '.muted' /opt/alexacookie/deviceinfo.txt > /opt/alexacookie/muted.txt && sed -i '/null/d' /opt/alexacookie/muted.txt && sed -i -e 's/^/muted:/' /opt/alexacookie/muted.txt && jq -r '.currentState' /opt/alexacookie/deviceinfo.txt > /opt/alexacookie/currentstate.txt && sed -i '/null/d' /opt/alexacookie/currentstate.txt && sed -i -e 's/^/currentState:/' /opt/alexacookie/currentstate.txt && jq  '.playerInfo.infoText' /opt/alexacookie/deviceinfo.txt > /opt/alexacookie/trackinfo.txt && sed -i 's/[\"{},]//g' /opt/alexacookie/trackinfo.txt && sed -i '/^\$/d' /opt/alexacookie/trackinfo.txt && sed -i '/null/d' /opt/alexacookie/trackinfo.txt && sed \"s/^[ \t]*//\" -i /opt/alexacookie/trackinfo.txt &&  cat /opt/alexacookie/volume.txt /opt/alexacookie/currentstate.txt /opt/alexacookie/muted.txt /opt/alexacookie/trackinfo.txt> /opt/alexacookie/status.txt && cat /opt/alexacookie/status.txt"
    sendMsg(msg)
}

def refresh(){
     //def msg =  "cat /opt/alexacookie/.alexa.devicelist.json"
    def msg = "${ttsPath}"+ "/alexa_remote_control.sh -d" + " \"${echoName}\"" + " -q >" + cookPath + "/deviceinfo.txt && sed -i 1d " + cookPath + "/deviceinfo.txt && sed -i '/null/d' " + cookPath + "/deviceinfo.txt && sed -i ':a;N;\$!ba;s/[\\n \\t]//g' " + cookPath +   "/deviceinfo.txt && cat " + cookPath + "/deviceinfo.txt"
     
  // sendEvent(name:'mute', value:'false')
    //sendEvent(name:'mute', value:'false')
    //state.lastLevel = level
    sendMsg(msg)

}
    
def setLevel(level){
    def msg =  "${ttsPath}" + '/alexa_remote_control.sh -d "' + "${echoName}" + '" -e vol:' + "${level}"
    sendEvent(name: "level", value: level, unit: "%")
    sendEvent(name:'mute', value:'false')
    state.lastLevel = level
    sendMsg(msg)

}
def mute(){
    def msg =  "${ttsPath}" + '/alexa_remote_control.sh -d "' + "${echoName}" + '" -e vol:' + "0"
    state.lastLevel = device.currentValue('level')
    sendEvent(name:'mute', value:'true')
    sendEvent(name:'level', value:0)
    sendMsg(msg)
}

def unmute(){
    if(device.currentValue('mute') == 'true'){
    def msg =  "${ttsPath}" + '/alexa_remote_control.sh -d "' + "${echoName}" + '" -e vol:' + "${state.lastLevel}"
    sendEvent(name:'level', value: state.lastLevel, unit: "%")
    sendEvent(name:'mute', value:'false')
    sendMsg(msg)
    }
}
    
    

def play(){
    def msg =  "${ttsPath}" + '/alexa_remote_control.sh -d "' + "${echoName}" + '" -e play'
    runIn(1, refresh)
    sendMsg(msg)

}
def pause(){
    def msg =  "${ttsPath}" + '/alexa_remote_control.sh -d "' + "${echoName}" + '" -e pause'
    runIn(1, refresh)
    sendMsg(msg)
   

}
def stop(){
    def msg =  "${ttsPath}" + '/alexa_remote_control.sh -d "' + "${echoName}" + '" -e pause'
    runIn(5, refresh)
    sendMsg(msg)

}
def playTrack(playlist){
    def msg =  "${ttsPath}" + '/alexa_remote_control.sh -d "' + "${echoName}" + '" -w' + " ${playlist}"
    runIn(5, refresh)
    sendMsg(msg)

}
def nextTrack(){
    def msg =  "${ttsPath}" + '/alexa_remote_control.sh -d "' + "${echoName}" + '" -e next'
    runIn(5, refresh)
    sendMsg(msg)

}
def previousTrack(){
    def msg =  "${ttsPath}" + '/alexa_remote_control.sh -d "' + "${echoName}" + '" -e prev'
    runIn(5, refresh)
    sendMsg(msg)

}
def speak(message) {
    //def msg = '/opt/ha-alexa-tts-master/alexa_remote_control.sh -d "' + "${echoName}" + '" -e speak:"' + "${message}" + '"\r\n'
    def msg =  "${ttsPath}" + '/alexa_remote_control.sh -d "' + "${echoName}" + '" -e speak:"' + "${message}\""
    sendEvent(name: "Telnet", value: "Connected")
    runIn(5, refresh)
    sendMsg(msg)
}

def sendMsg(String msg) {
    log.debug "Sending msg = ${msg}"
	
    return new hubitat.device.HubAction(msg, hubitat.device.Protocol.TELNET)
}



def parse(String msg) {
    

    log.debug "Telnet Response = ${msg}"
    if (msg == "permitted by applicable law.") {
        sendEvent(name: "Telnet", value: "Connected");
        
        
    }
    if (msg == "Sequence command: Alexa.Speak") {
        sendEvent(name: "Telnet", value: "Connected");
        
        }
    
    
    if (msg.startsWith("     1")) {
        sendEvent(name: "Device_1", value: msg.substring(7))
        sendEvent(name: "Telnet", value: "Connected");
        
    	}
     if (msg.startsWith("        \"subText1\"")) {
        sendEvent(name: "Device_1", value: msg.substring(7))
        sendEvent(name: "mediaId", value: "Connected");
        
    	}
    if (msg.startsWith("     2")) {
        sendEvent(name: "Device_2", value: msg.substring(7))
   		}
    if (msg.startsWith("     3")) {
       sendEvent(name: "Device_3", value: msg.substring(7))
    	}
    if (msg.startsWith("     4")) {
        sendEvent(name: "Device_4", value: msg.substring(7))
    	}
    if (msg.startsWith("     5")) {
        sendEvent(name: "Device_5", value: msg.substring(7))
        }
    if (msg.startsWith("     6")) {
        sendEvent(name: "Device_6", value: msg.substring(7))
        }
    if (msg.startsWith("     7")) {
        sendEvent(name: "Device_7", value: msg.substring(7))
        }
    if (msg.startsWith("     8")) {
        sendEvent(name: "Device_8", value: msg.substring(7))
        }
    if (msg.startsWith("     9")) {
        sendEvent(name: "Device_9", value: msg.substring(7))
        }
     if (msg.startsWith("    10")) {
        sendEvent(name: "Device_10", value: msg.substring(7))
        }
    if (msg.startsWith("    11")) {
        sendEvent(name: "Device_11", value: msg.substring(7))
        }
    if (msg.startsWith("    12")) {
        sendEvent(name: "Device_12", value: msg.substring(7))
        }
    if (msg.startsWith("    13")) {
        sendEvent(name: "Device_13", value: msg.substring(7))
        }
    if (msg.startsWith("    14")) {
        sendEvent(name: "Device_14", value: msg.substring(7))
        }
    if (msg.startsWith("    15")) {
        sendEvent(name: "Device_15", value: msg.substring(7))
        }
    if (msg.startsWith("    16")) {
        sendEvent(name: "Device_16", value: msg.substring(7))
        }
    if (msg.startsWith("    17")) {
        sendEvent(name: "Device_17", value: msg.substring(7))
        }
    if (msg.startsWith("    18")) {
        sendEvent(name: "Device_18", value: msg.substring(7))
        }
    if (msg.startsWith("    19")) {
        sendEvent(name: "Device_19", value: msg.substring(7))
        }
    if (msg.startsWith("    20")) {
        sendEvent(name: "Device_20", value: msg.substring(7))
        }
    if (msg.startsWith("    21")) {
        sendEvent(name: "Device_21", value: msg.substring(7))
        }
    if (msg.startsWith("    22")) {
        sendEvent(name: "Device_22", value: msg.substring(7))
        }
    if (msg.startsWith("    23")) {
        sendEvent(name: "Device_23", value: msg.substring(7))
        }
    if (msg.startsWith("    24")) {
        sendEvent(name: "Device_24", value: msg.substring(7))
        }
    if (msg.startsWith("    25")) {
        sendEvent(name: "Device_25", value: msg.substring(7))
        }
    if (msg.startsWith("muted:")) {
        sendEvent(name: "mute", value: msg.substring(6))
        }
    if (msg.startsWith("{\"playerInfo\"")) {
        def deviceRsp = msg
       // log.debug "it started with volume 177"
        
        def jsonSlurper = new JsonSlurper()
        def deviceInfo = jsonSlurper.parseText(deviceRsp)
        //log.debug "line 267 ${deviceInfo.playerInfo.mainArt.url}"
        sendEvent(name:'mute', value:deviceInfo.playerInfo.volume.muted)
        sendEvent(name:'level', value:deviceInfo.playerInfo.volume.volume)
        sendEvent(name:'status', value:deviceInfo.playerInfo.state)
        sendEvent(name:'artist', value:deviceInfo.playerInfo.infoText.subText1)
        sendEvent(name:'album', value:deviceInfo.playerInfo.infoText.subText2)
        sendEvent(name:'track', value:deviceInfo.playerInfo.infoText.title)
        if (deviceInfo.playerInfo.mainArt.url){
       // sendEvent(name:'imageUrlHtml', value: "<img src=\"" + deviceInfo.playerInfo.mainArt.url +  "\" width=\"256\" height=\"256\"" + "></img>")
        sendEvent(name:'imageUrlHtml', value: "<img src=\"" + deviceInfo.playerInfo.mainArt.url + "\"></img>")
       // sendEvent(name: "level", value: msg.substring(1))
        } 
    }
    if (msg.startsWith("{\"devices\"")){
        log.debug "caught the device refresh on line 260"
        def deviceList = msg
        
        def jsonSlurper = new JsonSlurper()
    
        def devices = jsonSlurper.parseText(deviceList)
    log.debug "device list 266 ${devices.devices.accountName}"
    def testList = devices.devices.accountName
    log.debug testList[2]
               
    }  
}

def telnetStatus(String status){
	log.info "telnetStatus- error: ${status}"
	if (status == "receive error: Stream is closed"){
		
		log.error "Telnet connection dropped..."
        sendEvent(name: "Telnet", value: "Disconnected")
		initialize()
	} else {
		sendEvent(name: "Telnet", value: "Connected")
	}
}

