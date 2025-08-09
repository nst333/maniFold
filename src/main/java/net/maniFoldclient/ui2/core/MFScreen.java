package net.maniFoldclient.ui2.core;

import net.maniFoldclient.ui2.theme.MFTheme;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public abstract class MFScreen extends Screen {
    protected MFWidget root;

    protected MFScreen(Text title){ super(title); }

    @Override
    public void render(DrawContext g, int mouseX, int mouseY, float delta) {
        g.fill(0, 0, width, height, MFTheme.OVERLAY_DIM);
        if (root != null) root.render(g, mouseX, mouseY, delta);
        super.render(g, mouseX, mouseY, delta);
    }

    @Override public boolean mouseClicked(double mx, double my, int button){
        return (root != null && root.mouseClicked(mx, my, button)) || super.mouseClicked(mx, my, button);
    }
    @Override public boolean mouseReleased(double mx, double my, int button){
        return (root != null && root.mouseReleased(mx, my, button)) || super.mouseReleased(mx, my, button);
    }
    @Override public boolean mouseScrolled(double mx, double my, double amount){
        return (root != null && root.mouseScrolled(mx, my, amount)) || super.mouseScrolled(mx, my, amount);
    }
    @Override public boolean keyPressed(int keyCode, int scanCode, int modifiers){
        return (root != null && root.keyPressed(keyCode, scanCode, modifiers)) || super.keyPressed(keyCode, scanCode, modifiers);
    }
    @Override public boolean charTyped(char chr, int modifiers){
        return (root != null && root.charTyped(chr, modifiers)) || super.charTyped(chr, modifiers);
    }
}
