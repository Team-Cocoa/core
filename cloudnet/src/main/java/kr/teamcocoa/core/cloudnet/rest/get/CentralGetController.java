package kr.teamcocoa.core.cloudnet.rest.get;

import com.google.gson.JsonObject;
import dev.derklaro.aerogel.Inject;
import dev.derklaro.aerogel.Singleton;
import eu.cloudnetservice.common.log.LogManager;
import eu.cloudnetservice.common.log.Logger;
import eu.cloudnetservice.driver.network.http.HttpContext;
import eu.cloudnetservice.driver.network.http.HttpResponseCode;
import eu.cloudnetservice.driver.network.http.annotation.HttpRequestHandler;
import eu.cloudnetservice.driver.network.http.annotation.RequestPathParam;
import eu.cloudnetservice.node.config.Configuration;
import eu.cloudnetservice.node.http.V2HttpHandler;
import kr.teamcocoa.core.cloudnet.listeners.CloudNetEventSender;
import kr.teamcocoa.core.utils.JsonUtils;
import lombok.NonNull;

@Singleton
public class CentralGetController extends V2HttpHandler {

    private static Logger logger = LogManager.logger(CentralGetController.class);

    @Inject
    public CentralGetController(
            @NonNull Configuration config
            ) {
        super(config.restConfiguration());
        // etc..
    }

    @HttpRequestHandler(paths = "/api/patchcommand/{target}/{command}", methods = "GET")
    private void onCommandExecute(
            @NonNull HttpContext context,
            @NonNull @RequestPathParam("target") String target,
            @NonNull @RequestPathParam("command") String command
            ) {

        JsonObject json = new JsonObject();
        json.addProperty("command", command);

        CloudNetEventSender.Targets enumTarget = switch(target.toLowerCase()) {
            case "proxy" -> CloudNetEventSender.Targets.BUNGEECORD;
            case "bukkit" -> CloudNetEventSender.Targets.MINECRAFT_SERVER;

        };

        CloudNetEventSender sender = new CloudNetEventSender("core", "patchcommand", CloudNetEventSender.Targets.BUNGEECORD, false);

        try {
            sender.setBuffer(JsonUtils.stringify(json));

            sender.broadcast();

            CentralGetController.this.ok(context)
                    .body("done")
                    .context()
                    .closeAfter(true)
                    .cancelNext(true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }



}
