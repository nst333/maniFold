package net.maniFoldclient.ui2.widgets;

import net.maniFoldclient.ui2.core.MFWidget;
import net.maniFoldclient.ui2.draw.MFDraw;
import net.maniFoldclient.ui2.theme.MFTheme;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public final class MFTextField extends MFWidget {
    private final TextFieldWidget tf;
    private String placeholder = "";
    private Consumer<String> onChange;

    public MFTextField(){
        this.h = MFTheme.dp(36);
        var mc = MinecraftClient.getInstance();
        this.tf = new TextFieldWidget(mc.textRenderer, 0,0,100,h, Text.empty());
        this.tf.setEditableColor(0xFFFFFFFF);
        this.tf.setUneditableColor(0x88FFFFFF);
        this.tf.setChangedListener(s -> { if (onChange!=null) onChange.accept(s); });
    }

    public MFTextField placeholder(String p){ this.placeholder=p; return this; }
    public MFTextField onChanged(Consumer<String> c){ this.onChange = c; return this; }

    @Override
    public void render(DrawContext g, int mouseX, int mouseY, float delta) {
        MFDraw.pill(g, x, y, x+w, y+h, 0xCC111111);
        var tr = MinecraftClient.getInstance().textRenderer;
        g.drawText(tr, "⌕", x + MFTheme.dp(10), y + (h-9)/2, MFTheme.TEXT_DIM, false);

        tf.setX(x + MFTheme.dp(24));
        tf.setY(y + MFTheme.dp(2));
        tf.setWidth(w - MFTheme.dp(32));
        tf.render(g, mouseX, mouseY, delta);

        if (tf.getText().isEmpty() && !placeholder.isEmpty()){
            g.drawText(tr, placeholder, x + MFTheme.dp(26), y + (h-9)/2, 0x66FFFFFF, false);
        }
    }

    @Override public boolean mouseClicked(double mx, double my, int button){ return tf.mouseClicked(mx, my, button); }
    @Override public boolean mouseReleased(double mx, double my, int button){ return tf.mouseReleased(mx, my, button); }
    @Override public boolean keyPressed(int keyCode, int scanCode, int modifiers){ return tf.keyPressed(keyCode, scanCode, modifiers); }
    @Override public boolean charTyped(char chr, int modifiers){ return tf.charTyped(chr, modifiers); }

    public String text(){ return tf.getText(); }
    public void setText(String s){ tf.setText(s); }
    public void focus(){ tf.setFocused(true); }
}
