package arcania.modid;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ArcaniaKeybinds {
    public static KeyBinding openTreeKey;

    public static void register() {
        openTreeKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.arcania.openTree",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R, // Standard-Taste (R)
                "category.arcania.tree" // Eigene Kategorie
        ));

        // Event-Listener
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (openTreeKey.wasPressed()) {
                MinecraftClient.getInstance().setScreen(
                        new Skilltree(Text.empty())
                );
            }
        });
    }
}
