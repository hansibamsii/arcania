package arcania.modid.entity;

import arcania.modid.projectile.FireBallEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ArcaniaEntityRenderers {
    public static void registerEntityRenderers() {
        EntityRendererRegistry.register(ArcaniaEntityTypes.FIREBALL, FireBallEntityRenderer::new);
    }

    public static class FireBallEntityRenderer extends ProjectileEntityRenderer<FireBallEntity> {
        public FireBallEntityRenderer(EntityRendererFactory.Context context) {
            super(context);
        }

        @Override
        public Identifier getTexture(FireBallEntity entity) {
            return Identifier.ofVanilla("textures/item/fire_charge.png");
        }
    }
}
