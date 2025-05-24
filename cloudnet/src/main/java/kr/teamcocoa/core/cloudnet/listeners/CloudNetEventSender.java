package kr.teamcocoa.core.cloudnet.listeners;

import java.io.NotSerializableException;
import java.io.Serializable;
import java.util.function.Consumer;

import eu.cloudnetservice.driver.channel.ChannelMessage;
import eu.cloudnetservice.driver.service.ServiceEnvironmentType;
import lombok.Getter;
import lombok.Setter;

@Getter
public class CloudNetEventSender {

    public enum Targets {
        ALL, NODE, MINECRAFT_SERVER, BUNGEECORD, LOBBY, FREEFIGHT, REPLAY, PROXY
    }

    private boolean query;

    private String pluginName;

    private String listenerName;

    private Object buffer;

    private Targets taskTarget;

    @Setter
    private Consumer<Object> queryCallBack;

    public void setBuffer(Object buffer) throws NotSerializableException {
        if(!(buffer instanceof Serializable)) {
            throw new NotSerializableException();
        }
        this.buffer = buffer;
    }

    public CloudNetEventSender(
        String pluginName,
        String listenerName,
        Targets taskTarget,
        boolean query
    ) {
        this.pluginName = pluginName;
        this.listenerName = listenerName;
        this.taskTarget = taskTarget;
        this.query = query;
    }

    public void broadcast() {
        ChannelMessage.Builder messageBuilder = ChannelMessage.builder()
            .channel("teamcocoa:" + pluginName)
            .message(listenerName)
            .buffer(DataBufConverter.objectToDataBuf(buffer));
        
        messageBuilder = switch(taskTarget) {
                    case ALL -> messageBuilder.targetAll();
                    case BUNGEECORD -> messageBuilder.targetEnvironment(ServiceEnvironmentType.BUNGEECORD);
                    case FREEFIGHT -> messageBuilder.targetTask("FreeFight");
                    case LOBBY -> messageBuilder.targetTask("Lobby");
                    case MINECRAFT_SERVER -> messageBuilder.targetEnvironment(ServiceEnvironmentType.MINECRAFT_SERVER);
                    case NODE -> messageBuilder.targetNodes();
                    case PROXY -> messageBuilder.targetTask("Proxy");
                    case REPLAY -> messageBuilder.targetTask("Replay");
                    default -> throw new IllegalArgumentException("Unexpected value: " + taskTarget);
            
        };

        ChannelMessage channelMessage = messageBuilder.build();
        
        if(!query) {
            channelMessage.send();
        }
        else {
            channelMessage.sendSingleQuery();

            Object response = DataBufConverter.byteArrayToObject(channelMessage.content().toByteArray());
            
            queryCallBack.accept(response);
        }
    }

}
