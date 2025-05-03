package kr.teamcocoa.core.cloudnet.main;

import dev.derklaro.aerogel.Singleton;
import eu.cloudnetservice.driver.event.EventManager;
import eu.cloudnetservice.driver.module.ModuleLifeCycle;
import eu.cloudnetservice.driver.module.ModuleTask;
import eu.cloudnetservice.driver.module.driver.DriverModule;
import lombok.NonNull;

@Singleton
public class CloudNetCoreBootstrap extends DriverModule {

    @ModuleTask(lifecycle = ModuleLifeCycle.STARTED)
    public void loadListener(
        @NonNull EventManager eventManager
    ) {
        eventManager.registerListener(eventManager);
    }

    @ModuleTask(lifecycle = ModuleLifeCycle.RELOADING)
    public void onReload(
        @NonNull EventManager eventManager
    ) {
        eventManager.unregisterListener();
        loadListener(eventManager);
    }


}
