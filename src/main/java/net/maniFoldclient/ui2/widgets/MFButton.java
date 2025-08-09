package net.maniFoldclient.ui2.widgets;

import net.maniFoldclient.ui2.core.MFWidget;
import net.maniFoldclient.ui2.draw.MFDraw;
import net.maniFoldclient.ui2.theme.MFTheme;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public final class MFButton extends MFWidget {
    private String text;
    private Runnable onClick;

    public MFButton(String text, Runnable onClick){
        this.text = text;
        this.onClick = onClick;
        this.h = MFTheme.dp(28);
    }

    @Override
    public void render(DrawContext g, int mouseX, int mouseY, float delta) {
        boolean hover = hit(mouseX, mouseY);
        MFDraw.pill(g, x, y, x+w, y+h, hover ? 0xCC1A1A1A : 0xB3161616);
        var tr = MinecraftClient.getInstance().textRenderer;
        int tw = tr.getWidth(text);
        g.drawText(tr, text, x + (w - tw)/2, y + (h-9)/2, MFTheme.TEXT, false);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (button == 0 && hit(mx,my)){
            if (onClick != null) onClick.run();
            return true;
        }
        return false;
    }
}
