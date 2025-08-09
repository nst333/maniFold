package net.maniFoldclient.ui2.widgets;

import net.maniFoldclient.ui2.core.MFWidget;
import net.maniFoldclient.ui2.draw.MFDraw;
import net.minecraft.client.gui.DrawContext;

import java.util.function.Consumer;

public final class MFSwitch extends MFWidget {
    private boolean on;
    private Consumer<Boolean> onChange;

    public MFSwitch(boolean initial){
        this.on = initial;
        this.h = 20;
        this.w = 40;
    }

    public MFSwitch onChange(Consumer<Boolean> c){ this.onChange=c; return this; }
    public boolean value(){ return on; }
    public void set(boolean v){ on = v; }

    @Override
    public void render(DrawContext g, int mouseX, int mouseY, float delta) {
        MFDraw.switchToggle(g, x, y, w, h, on);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (button==0 && hit(mx,my)){
            on = !on;
            if (onChange!=null) onChange.accept(on);
            return true;
        }
        return false;
    }
}
