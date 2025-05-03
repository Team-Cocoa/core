package kr.teamcocoa.core.cloudnet.listeners;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class CloudNetEventListener {
    
    private boolean query;

    public abstract Object onEvent(byte[] byteArray);

}
