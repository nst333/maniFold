package net.maniFoldclient.ui2.screens;

import net.maniFoldclient.hack.Hack;
import net.maniFoldclient.hack.HackList;
import net.maniFoldclient.ui2.core.MFScreen;
import net.maniFoldclient.ui2.core.MFWidget;
import net.maniFoldclient.ui2.layout.MFColumn;
import net.maniFoldclient.ui2.theme.MFTheme;
import net.maniFoldclient.ui2.widgets.MFCard;
import net.maniFoldclient.ui2.widgets.MFSwitch;
import net.maniFoldclient.ui2.widgets.MFTextField;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public final class QuickSearchScreen extends MFScreen {
    private final List<Hack> all = new ArrayList<>();
    private final List<Hack> filtered = new ArrayList<>();
    private final MFColumn listCol = new MFColumn().spacing(4).padding(8);
    private MFTextField tf;

    public QuickSearchScreen(){
        super(Text.literal("Search"));
    }

    @Override
    protected void init() {
        all.clear();
        all.addAll(HackList.getInstance().stream().sorted(Comparator.comparing(Hack::getName)).toList());
        filtered.clear();
        filtered.addAll(all);

        int searchW = Math.min(width - MFTheme.dp(32), 640);
        int searchX = (width - searchW) / 2;

        tf = new MFTextField()
                .placeholder("Search modules")
                .onChanged(s -> {
                    filtered.clear();
                    if (s==null || s.isBlank()) filtered.addAll(all);
                    else {
                        String q = s.toLowerCase(Locale.ROOT);
                        for (Hack h : all){
                            String n = h.getName().toLowerCase(Locale.ROOT);
                            if (n.startsWith(q) || n.contains(q)) filtered.add(h);
                        }
                    }
                    rebuildList();
                });

        MFCard card = new MFCard().child(listCol)
                .frame(searchX, MFTheme.dp(112), searchW, Math.max(MFTheme.dp(80), height - MFTheme.dp(160)));

        root = new MFColumn().spacing(MFTheme.dp(12)).padding(0)
                .add(tf.frame(searchX, MFTheme.dp(56), searchW, MFTheme.dp(36)))
                .add(card);

        tf.focus();
        rebuildList();
    }

    private void rebuildList(){
        // MFColumn의 children에 직접 접근이 필요하면, 아래 방식처럼 내부 필드 접근(리플렉션) 없이
        // listCol을 새로 구성하는 방법을 사용:
        try {
            var f = MFColumn.class.getDeclaredField("children");
            f.setAccessible(true);
            @SuppressWarnings("unchecked")
            List<MFWidget> kids = (List<MFWidget>) f.get(listCol);
            kids.clear();
            for (Hack h : filtered){
                kids.add(rowFor(h));
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private MFWidget rowFor(Hack h){
        return new MFWidget(){
            private final MFSwitch sw = new MFSwitch(h.isEnabled()).onChange(h::setEnabled);
            private final int rh = MFTheme.dp(28);
            { this.h = rh; }

            @Override
            public void render(DrawContext g, int mouseX, int mouseY, float delta) {
                var tr = MinecraftClient.getInstance().textRenderer;
                int tx = x + MFTheme.dp(8);
                int ty = y + (h - 9)/2;
                g.drawText(tr, h.getName(), tx, ty, MFTheme.TEXT, false);

                sw.frame(x + w - MFTheme.dp(52), y + (rh - 20)/2, 40, 20);
                sw.render(g, mouseX, mouseY, delta);
            }

            @Override public boolean mouseClicked(double mx, double my, int button){
                if (sw.mouseClicked(mx, my, button)) return true;
                if (button==0 && hit(mx,my)){
                    boolean nv = !sw.value();
                    sw.set(nv); h.setEnabled(nv);
                    return true;
                }
                return false;
            }
        }.frame(0,0, listCol.w - 16, MFTheme.dp(28));
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // Esc 닫기
        if (keyCode == 256){
            MinecraftClient.getInstance().setScreen(null);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(DrawContext g, int mouseX, int mouseY, float delta) {
        super.render(g, mouseX, mouseY, delta);
        var tr = MinecraftClient.getInstance().textRenderer;
        String title = "maniFold";
        String subtitle = "Quick Search";
        int tW = tr.getWidth(title), stW = tr.getWidth(subtitle);
        g.drawText(tr, title, (width - tW)/2, MFTheme.dp(18), MFTheme.TEXT, false);
        g.drawText(tr, subtitle, (width - stW)/2, MFTheme.dp(30), MFTheme.TEXT_DIM, false);
    }
}
