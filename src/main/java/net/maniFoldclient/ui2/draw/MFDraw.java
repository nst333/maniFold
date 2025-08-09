package net.maniFoldclient.ui2.draw;

import net.maniFoldclient.ui2.theme.MFTheme;
import net.minecraft.client.gui.DrawContext;

public final class MFDraw {
    private MFDraw() {}

    public static void glassPanel(DrawContext g, int x1, int y1, int x2, int y2) {
        g.fill(x1, y1, x2, y2, MFTheme.GLASS_FILL);
        g.fill(x1, y1, x2, y1 + 1, MFTheme.GLASS_EDGE_T);
        g.fill(x1, y2 - 1, x2, y2, MFTheme.GLASS_EDGE_B);
    }

    public static void pill(DrawContext g, int x1, int y1, int x2, int y2, int color) {
        g.fill(x1, y1, x2, y2, color);
        g.fill(x1, y1, x2, y1 + 1, 0x22FFFFFF);
        g.fill(x1, y2 - 1, x2, y2, 0x22000000);
    }

    public static void switchToggle(DrawContext g, int x, int y, int w, int h, boolean on) {
        int track = on ? MFTheme.ACCENT : 0xFF3A3A3A;
        int knob  = 0xFFFFFFFF;
        g.fill(x, y, x + w, y + h, track);
        int cx = on ? (x + w - h) : x;
        g.fill(cx, y, cx + h, y + h, knob);
    }

    public static void slider(DrawContext g, int x, int y, int w, int h, float value) {
        g.fill(x, y + (h/2) - 2, x + w, y + (h/2) + 2, 0xFF3A3A3A);
        int prog = (int)(w * Math.max(0f, Math.min(1f, value)));
        g.fill(x, y + (h/2) - 2, x + prog, y + (h/2) + 2, MFTheme.ACCENT_SOFT);
        int knobX = x + prog - 6;
        g.fill(knobX, y + (h/2) - 6, knobX + 12, y + (h/2) + 6, 0xFFFFFFFF);
    }
}
