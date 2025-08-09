package net.maniFoldclient.ui2.widgets;

import net.maniFoldclient.ui2.core.MFWidget;
import net.maniFoldclient.ui2.draw.MFDraw;
import net.minecraft.client.gui.DrawContext;

import java.util.function.Consumer;

public final class MFSlider extends MFWidget {
    private float value; // 0..1
    private Consumer<Float> onChange;

    public MFSlider(float initial){
        this.value = clamp01(initial);
        this.h = 20;
    }

    public MFSlider onChange(Consumer<Float> c){ this.onChange=c; return this; }
    public float value(){ return value; }
    public void set(float v){ value = clamp01(v); }

    @Override
    public void render(DrawContext g, int mouseX, int mouseY, float delta) {
        MFDraw.slider(g, x, y, w, h, value);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (button==0 && hit(mx,my)){
            float p = (float)((mx - x) / Math.max(1f, w));
            set(p);
            if (onChange!=null) onChange.accept(value);
            return true;
        }
        return false;
    }

    private static float clamp01(float v){ return Math.max(0f, Math.min(1f, v)); }
}
