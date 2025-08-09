package net.maniFoldclient.ui2.widgets;

import net.maniFoldclient.ui2.core.MFWidget;
import net.maniFoldclient.ui2.draw.MFDraw;
import net.minecraft.client.gui.DrawContext;

public final class MFCard extends MFWidget {
    private MFWidget child;

    public MFCard child(MFWidget w){
        this.child = w.parent(this);
        return this;
    }

    @Override
    public void render(DrawContext g, int mouseX, int mouseY, float delta) {
        MFDraw.glassPanel(g, x, y, x+w, y+h);
        if (child != null){
            int pad = 12;
            child.frame(x+pad, y+pad, w-pad*2, h-pad*2);
            child.render(g, mouseX, mouseY, delta);
        }
    }

    @Override public boolean mouseClicked(double mx, double my, int button){
        return child != null && child.mouseClicked(mx, my, button);
    }
    @Override public boolean keyPressed(int keyCode, int scanCode, int modifiers){
        return child != null && child.keyPressed(keyCode, scanCode, modifiers);
    }
    @Override public boolean charTyped(char chr, int modifiers){
        return child != null && child.charTyped(chr, modifiers);
    }
}
