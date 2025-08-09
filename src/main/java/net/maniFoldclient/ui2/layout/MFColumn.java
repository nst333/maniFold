package net.maniFoldclient.ui2.layout;

import net.maniFoldclient.ui2.core.MFWidget;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

public final class MFColumn extends MFWidget {
    private final List<MFWidget> children = new ArrayList<>();
    private int spacing = 8;
    private int padding = 8;

    public MFColumn spacing(int s){ this.spacing = s; return this; }
    public MFColumn padding(int p){ this.padding = p; return this; }

    public MFColumn add(MFWidget w){
        children.add(w.parent(this));
        return this;
    }

    @Override
    public void render(DrawContext g, int mouseX, int mouseY, float delta) {
        int cy = y + padding;
        for (MFWidget c : children){
            // child's height should be preset by caller
            c.frame(x + padding, cy, w - padding*2, h);
            c.render(g, mouseX, mouseY, delta);
            cy += h + spacing;
        }
    }

    @Override public boolean mouseClicked(double mx, double my, int button){
        for (MFWidget c : new ArrayList<>(children))
            if (c.mouseClicked(mx, my, button)) return true;
        return false;
    }
    @Override public boolean keyPressed(int keyCode, int scanCode, int modifiers){
        for (MFWidget c : new ArrayList<>(children))
            if (c.keyPressed(keyCode, scanCode, modifiers)) return true;
        return false;
    }
    @Override public boolean charTyped(char chr, int modifiers){
        for (MFWidget c : new ArrayList<>(children))
            if (c.charTyped(chr, modifiers)) return true;
        return false;
    }
}
