package kr.teamcocoa.core.cloudnet.listeners;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class CloudNetEventListener {
    
    private boolean query;

    private String pluginName;

    private String listenerName;

    public abstract Object onEvent(Object object);

}
