/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.options;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Util;
import net.minecraft.util.Util.OperatingSystem;
import net.maniFoldclient.maniFoldClient;
import net.maniFoldclient.analytics.PlausibleAnalytics;
import net.maniFoldclient.commands.FriendsCmd;
import net.maniFoldclient.hacks.XRayHack;
import net.maniFoldclient.other_features.VanillaSpoofOtf;
import net.maniFoldclient.settings.CheckboxSetting;
import net.maniFoldclient.util.ChatUtils;
import net.maniFoldclient.util.maniFoldColors;

public class maniFoldOptionsScreen extends Screen
{
	private Screen prevScreen;
	
	public maniFoldOptionsScreen(Screen prevScreen)
	{
		super(Text.literal(""));
		this.prevScreen = prevScreen;
	}
	
	@Override
	public void init()
	{
		addDrawableChild(ButtonWidget
			.builder(Text.literal("Back"), b -> client.setScreen(prevScreen))
			.dimensions(width / 2 - 100, height / 4 + 144 - 16, 200, 20)
			.build());
		
		addSettingButtons();
		addManagerButtons();
		addLinkButtons();
	}
	
	private void addSettingButtons()
	{
		maniFoldClient maniFold = maniFoldClient.INSTANCE;
		FriendsCmd friendsCmd = maniFold.getCmds().friendsCmd;
		CheckboxSetting middleClickFriends = friendsCmd.getMiddleClickFriends();
		PlausibleAnalytics plausible = maniFold.getPlausible();
		VanillaSpoofOtf vanillaSpoofOtf = maniFold.getOtfs().vanillaSpoofOtf;
		CheckboxSetting forceEnglish =
			maniFold.getOtfs().translationsOtf.getForceEnglish();
		
		new maniFoldOptionsButton(-154, 24,
			() -> "Click Friends: "
				+ (middleClickFriends.isChecked() ? "ON" : "OFF"),
			middleClickFriends.getWrappedDescription(200),
			b -> middleClickFriends
				.setChecked(!middleClickFriends.isChecked()));
		
		new maniFoldOptionsButton(-154, 48,
			() -> "Count Users: " + (plausible.isEnabled() ? "ON" : "OFF"),
			"Counts how many people are using maniFold and which versions are the"
				+ " most popular. This data helps me to decide when I can stop"
				+ " supporting old versions.\n\n"
				+ "These statistics are completely anonymous, never sold, and"
				+ " stay in the EU (I'm self-hosting Plausible in Germany)."
				+ " There are no cookies or persistent identifiers"
				+ " (see plausible.io).",
			b -> plausible.setEnabled(!plausible.isEnabled()));
		
		new maniFoldOptionsButton(-154, 72,
			() -> "Spoof Vanilla: "
				+ (vanillaSpoofOtf.isEnabled() ? "ON" : "OFF"),
			vanillaSpoofOtf.getDescription(),
			b -> vanillaSpoofOtf.doPrimaryAction());
		
		new maniFoldOptionsButton(-154, 96,
			() -> "Translations: " + (!forceEnglish.isChecked() ? "ON" : "OFF"),
			"Allows text in maniFold to be displayed in other languages than"
				+ " English. It will use the same language that Minecraft is"
				+ " set to.\n\n" + "This is an experimental feature!",
			b -> forceEnglish.setChecked(!forceEnglish.isChecked()));
	}
	
	private void addManagerButtons()
	{
		XRayHack xRayHack = maniFoldClient.INSTANCE.getHax().xRayHack;
		
		new maniFoldOptionsButton(-50, 24, () -> "Keybinds",
			"Keybinds allow you to toggle any hack or command by simply"
				+ " pressing a button.",
			b -> client.setScreen(new KeybindManagerScreen(this)));
		
		new maniFoldOptionsButton(-50, 48, () -> "X-Ray Blocks",
			"Manager for the blocks that X-Ray will show.",
			b -> xRayHack.openBlockListEditor(this));
		
		new maniFoldOptionsButton(-50, 72, () -> "Zoom",
			"The Zoom Manager allows you to change the zoom key and how far it"
				+ " will zoom in.",
			b -> client.setScreen(new ZoomManagerScreen(this)));
	}
	
	private void addLinkButtons()
	{
		OperatingSystem os = Util.getOperatingSystem();
		
		new maniFoldOptionsButton(54, 24, () -> "Official Website",
			"§n§lmaniFoldClient.net",
			b -> os.open("https://www.maniFoldclient.net/options-website/"));
		
		new maniFoldOptionsButton(54, 48, () -> "maniFold Wiki",
			"§n§lmaniFold.Wiki",
			b -> os.open("https://www.maniFoldclient.net/options-wiki/"));
		
		new maniFoldOptionsButton(54, 72, () -> "maniFoldForum",
			"§n§lmaniFoldForum.net",
			b -> os.open("https://www.maniFoldclient.net/options-forum/"));
		
		new maniFoldOptionsButton(54, 96, () -> "Twitter", "@maniFold_Imperium",
			b -> os.open("https://www.maniFoldclient.net/options-twitter/"));
		
		new maniFoldOptionsButton(54, 120, () -> "Donate",
			"§n§lmaniFoldClient.net/donate\n"
				+ "Donate now to help me keep the maniFold Client alive and free"
				+ " to use for everyone.\n\n"
				+ "Every bit helps and is much appreciated! You can also get a"
				+ " few cool perks in return.",
			b -> os.open("https://www.maniFoldclient.net/options-donate/"));
	}
	
	@Override
	public void close()
	{
		client.setScreen(prevScreen);
	}
	
	@Override
	public void render(DrawContext context, int mouseX, int mouseY,
		float partialTicks)
	{
		renderTitles(context);
		
		for(Drawable drawable : drawables)
			drawable.render(context, mouseX, mouseY, partialTicks);
		
		renderButtonTooltip(context, mouseX, mouseY);
	}
	
	private void renderTitles(DrawContext context)
	{
		TextRenderer tr = client.textRenderer;
		int middleX = width / 2;
		int y1 = 40;
		int y2 = height / 4 + 24 - 28;
		
		context.drawCenteredTextWithShadow(tr, "maniFold Options", middleX, y1,
			Colors.WHITE);
		
		context.drawCenteredTextWithShadow(tr, "Settings", middleX - 104, y2,
			maniFoldColors.VERY_LIGHT_GRAY);
		context.drawCenteredTextWithShadow(tr, "Managers", middleX, y2,
			maniFoldColors.VERY_LIGHT_GRAY);
		context.drawCenteredTextWithShadow(tr, "Links", middleX + 104, y2,
			maniFoldColors.VERY_LIGHT_GRAY);
	}
	
	private void renderButtonTooltip(DrawContext context, int mouseX,
		int mouseY)
	{
		for(ClickableWidget button : Screens.getButtons(this))
		{
			if(!button.isSelected()
				|| !(button instanceof maniFoldOptionsButton))
				continue;
			
			maniFoldOptionsButton woButton = (maniFoldOptionsButton)button;
			
			if(woButton.tooltip.isEmpty())
				continue;
			
			context.drawTooltip(textRenderer, woButton.tooltip, mouseX, mouseY);
			break;
		}
	}
	
	private final class maniFoldOptionsButton extends ButtonWidget
	{
		private final Supplier<String> messageSupplier;
		private final List<Text> tooltip;
		
		public maniFoldOptionsButton(int xOffset, int yOffset,
			Supplier<String> messageSupplier, String tooltip,
			PressAction pressAction)
		{
			super(maniFoldOptionsScreen.this.width / 2 + xOffset,
				maniFoldOptionsScreen.this.height / 4 - 16 + yOffset, 100, 20,
				Text.literal(messageSupplier.get()), pressAction,
				ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
			
			this.messageSupplier = messageSupplier;
			
			if(tooltip.isEmpty())
				this.tooltip = Arrays.asList();
			else
			{
				String[] lines = ChatUtils.wrapText(tooltip, 200).split("\n");
				
				Text[] lines2 = new Text[lines.length];
				for(int i = 0; i < lines.length; i++)
					lines2[i] = Text.literal(lines[i]);
				
				this.tooltip = Arrays.asList(lines2);
			}
			
			addDrawableChild(this);
		}
		
		@Override
		public void onPress()
		{
			super.onPress();
			setMessage(Text.literal(messageSupplier.get()));
		}
	}
}
