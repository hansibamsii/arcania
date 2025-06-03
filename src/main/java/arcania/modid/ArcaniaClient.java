package arcania.modid;

import arcania.modid.entity.ArcaniaEntityRenderers;
import net.fabricmc.api.ClientModInitializer;

public class ArcaniaClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ArcaniaKeybinds.register();
        ArcaniaEntityRenderers.registerEntityRenderers(); // Add this line
    }
}