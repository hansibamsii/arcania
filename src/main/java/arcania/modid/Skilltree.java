package arcania.modid;

import arcania.modid.skilltree.SkillNode;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import java.util.ArrayList;
import java.util.List;

public class Skilltree extends Screen {
    private final List<SkillNode> skillNodes = new ArrayList<>();

    public Skilltree(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        PlayerEntity player = this.client.player;
        if (player == null) return;

        SkillNode skill1 = new SkillNode("Skill 1");
        SkillNode skill2 = new SkillNode("Skill 2", skill1);
        SkillNode skill3 = new SkillNode("Skill 3", skill2);

        skillNodes.add(skill1);
        skillNodes.add(skill2);
        skillNodes.add(skill3);

        this.addDrawableChild(skill1.createButton(this.width / 2 - 50, 50, player));
        this.addDrawableChild(skill2.createButton(this.width / 2 - 50, 100, player));
        this.addDrawableChild(skill3.createButton(this.width / 2 - 50, 150, player));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawText(this.textRenderer, "Skill Tree", this.width / 2 - 40, 20, 0xFFFFFF, true);
    }
}
