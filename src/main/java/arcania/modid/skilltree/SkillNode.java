package arcania.modid.skilltree;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.entity.player.PlayerEntity;

public class SkillNode {
    private final String name;
    private final SkillNode[] prerequisites;
    private boolean unlocked = false;
    private ButtonWidget button;

    public SkillNode(String name, SkillNode... prerequisites) {
        this.name = name;
        this.prerequisites = prerequisites;
    }

    public void unlock(PlayerEntity player) {
        if (canUnlock()) {
            unlocked = true;
            saveProgress(player);
        }
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public boolean canUnlock() {
        if (unlocked) return false;
        for (SkillNode prerequisite : prerequisites) {
            if (!prerequisite.isUnlocked()) {
                return false;
            }
        }
        return true;
    }

    public void loadProgress(PlayerEntity player) {
        NbtCompound data = getPlayerData(player);
        this.unlocked = data.getBoolean("skill_" + name);
    }

    public void saveProgress(PlayerEntity player) {
        NbtCompound data = getPlayerData(player);
        data.putBoolean("skill_" + name, unlocked);
        savePlayerData(player, data);
    }

    public static void resetProgress(PlayerEntity player) {
        NbtCompound data = getPlayerData(player);
        for (String key : data.getKeys()) {
            if (key.startsWith("skill_")) {
                data.putBoolean(key, false);
            }
        }
        savePlayerData(player, data);
    }

    public ButtonWidget createButton(int x, int y, PlayerEntity player) {
        loadProgress(player);
        this.button = ButtonWidget.builder(Text.of(unlocked ? "✔ " + name : name), (btn) -> {
            if (canUnlock()) {
                unlock(player);
                btn.setMessage(Text.of("✔ " + name));
            }
        }).dimensions(x, y, 100, 20).build();
        return this.button;
    }

    // way to get and save player data (hoffentlich)
    private static NbtCompound getPlayerData(PlayerEntity player) {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            NbtCompound playerData = serverPlayer.writeNbt(new NbtCompound()); // Get NBT data
            if (!playerData.contains("arcania_skills")) {
                playerData.put("arcania_skills", new NbtCompound());
            }
            return playerData.getCompound("arcania_skills");
        }
        return new NbtCompound();
    }

    private static void savePlayerData(PlayerEntity player, NbtCompound data) {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            NbtCompound playerData = serverPlayer.writeNbt(new NbtCompound());
            playerData.put("arcania_skills", data);
            serverPlayer.readNbt(playerData); // Save data back to the player
        }
    }
}
