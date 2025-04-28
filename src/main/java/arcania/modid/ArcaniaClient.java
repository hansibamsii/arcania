package arcania.modid;

import net.fabricmc.api.ClientModInitializer;

public class ArcaniaClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ArcaniaKeybinds.register();
    }
}
