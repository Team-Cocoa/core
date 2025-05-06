package kr.teamcocoa.core.cloudnet.utils;

import eu.cloudnetservice.wrapper.configuration.WrapperConfiguration;
import lombok.Getter;

public class CloudNetServiceUtils {

    @Getter
    private static String serviceName;
    
    public static void setInitServiceName(WrapperConfiguration configuration) {
        if(serviceName != null) {
            return;
        }
        
        serviceName = configuration.serviceConfiguration().serviceId().name();
    }

}
