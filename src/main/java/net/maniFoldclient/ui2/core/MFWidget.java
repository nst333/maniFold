package net.maniFoldclient.ui2.core;

import net.minecraft.client.gui.DrawContext;.client.gui.DrawContext;

public abstract class MFWidget {
    protected int x, y, w, h;
    protected MFWidget parent;

    public MFWidget frame(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        return this;
    }

    public MFWidget parent(MFWidget p) {
        this.parent = p;
        return this;
    }

    public abstract void render(DrawContext g, int mouseX, int mouseY, float delta);

    public boolean mouseClicked(double mx, double my, int button) {
        return false;
    }
    public boolean mouseReleased(double mx, double my, int button) {
        return false;
    }
    public boolean mouseScrolled(double mx, double my, double amount) {
        return false;
    }
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }
    public boolean charTyped(char chr, int modifiers) {
        return false;
    }

    protected boolean hit(double mx, double my) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }
}
