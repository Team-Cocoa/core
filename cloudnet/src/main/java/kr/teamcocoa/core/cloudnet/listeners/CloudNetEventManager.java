package kr.teamcocoa.core.cloudnet.listeners;

import java.util.HashMap;

import eu.cloudnetservice.driver.event.EventListener;
import eu.cloudnetservice.driver.event.events.channel.ChannelMessageReceiveEvent;

public class CloudNetEventManager {

    /**
     * teamcocoa : name of plugin
     * name of plugin : name of listener
     */

    private static HashMap<String, HashMap<String, CloudNetEventListener>> eventMap = new HashMap<>();

    // TODO : 

    public static void addListener(String pluginName, String listenerName, CloudNetEventListener listener) {
        if(!eventMap.containsKey(pluginName)) {
            eventMap.put(pluginName, new HashMap<>());
        }

        HashMap<String, CloudNetEventListener> listenerMap = eventMap.get(pluginName);

        listenerMap.put(listenerName, listener);
    }
    
    @EventListener
    public void onChannel(ChannelMessageReceiveEvent e) {
        if(!e.channel().startsWith("teamcocoa")) {
            return;
        }

        String pluginName = e.channel().split(":")[1];

        if(!eventMap.containsKey(pluginName)) {
            return;
        }

        HashMap<String, CloudNetEventListener> listenerMap = eventMap.get(pluginName);

        String listenerName = e.message();

        if(!listenerMap.containsKey(listenerName)) {
            return;
        }

        CloudNetEventListener listener = listenerMap.get(listenerName);

        if(listener.isQuery()) {
            if(!e.query()) {
                return;
            }
        }

        Object response = listener.onEvent(DataBufConverter.byteArrayToObject(e.content().toByteArray()));

        if(listener.isQuery()) {
            e.binaryResponse(DataBufConverter.objectToDataBuf(response));
        }
    }

    
}
