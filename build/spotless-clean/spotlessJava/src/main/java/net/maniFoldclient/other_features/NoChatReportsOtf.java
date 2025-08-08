/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.other_features;

import java.net.URI;

import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.client.gui.hud.MessageIndicator.Icon;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.encryption.ClientPlayerSession;
import net.minecraft.network.message.MessageChain;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.maniFoldclient.Category;
import net.maniFoldclient.DontBlock;
import net.maniFoldclient.SearchTags;
import net.maniFoldclient.events.ChatInputListener;
import net.maniFoldclient.events.UpdateListener;
import net.maniFoldclient.other_feature.OtherFeature;
import net.maniFoldclient.settings.CheckboxSetting;
import net.maniFoldclient.util.ChatUtils;

@DontBlock
@SearchTags({"no chat reports", "NoEncryption", "no encryption",
	"NoChatSigning", "no chat signing"})
public final class NoChatReportsOtf extends OtherFeature
	implements UpdateListener, ChatInputListener
{
	private final CheckboxSetting disableSignatures =
		new CheckboxSetting("Disable signatures", true)
		{
			@Override
			public void update()
			{
				EVENTS.add(UpdateListener.class, NoChatReportsOtf.this);
			}
		};
	
	public NoChatReportsOtf()
	{
		super("NoChatReports",
			"description.maniFold.other_feature.nochatreports");
		addSetting(disableSignatures);
		
		ClientLoginConnectionEvents.INIT.register(this::onLoginStart);
		EVENTS.add(ChatInputListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		ClientPlayNetworkHandler netHandler = MC.getNetworkHandler();
		if(netHandler == null)
			return;
		
		if(isActive())
		{
			netHandler.session = null;
			netHandler.messagePacker = MessageChain.Packer.NONE;
			
		}else if(netHandler.session == null)
			MC.getProfileKeys().fetchKeyPair()
				.thenAcceptAsync(optional -> optional
					.ifPresent(profileKeys -> netHandler.session =
						ClientPlayerSession.create(profileKeys)),
					MC);
		
		EVENTS.remove(UpdateListener.class, this);
	}
	
	@Override
	public void onReceivedMessage(ChatInputEvent event)
	{
		if(!isActive())
			return;
		
		Text originalText = event.getComponent();
		if(!(originalText
			.getContent() instanceof TranslatableTextContent trContent))
			return;
		
		if(!trContent.getKey().equals("chat.disabled.missingProfileKey"))
			return;
		
		event.cancel();
		
		ClickEvent clickEvent = new ClickEvent.OpenUrl(
			URI.create("https://www.maniFoldclient.net/chat-disabled-mpk/"));
		HoverEvent hoverEvent = new HoverEvent.ShowText(
			Text.literal("Original message: ").append(originalText));
		
		ChatUtils.component(Text.literal(
			"The server is refusing to let you chat without enabling chat reports. Click \u00a7nhere\u00a7r to learn more.")
			.styled(
				s -> s.withClickEvent(clickEvent).withHoverEvent(hoverEvent)));
	}
	
	private void onLoginStart(ClientLoginNetworkHandler handler,
		MinecraftClient client)
	{
		EVENTS.add(UpdateListener.class, NoChatReportsOtf.this);
	}
	
	public MessageIndicator modifyIndicator(Text message,
		MessageSignatureData signature, MessageIndicator indicator)
	{
		if(!MANIFOLD.isEnabled() || MC.isInSingleplayer())
			return indicator;
		
		if(indicator != null || signature == null)
			return indicator;
		
		return new MessageIndicator(0xE84F58, Icon.CHAT_MODIFIED,
			Text.literal(ChatUtils.MANIFOLD_PREFIX
				+ "\u00a7cReportable\u00a7r - "
				+ MANIFOLD.translate(
					"description.maniFold.nochatreports.message_is_reportable")),
			"Reportable");
	}
	
	@Override
	public boolean isEnabled()
	{
		return disableSignatures.isChecked();
	}
	
	public boolean isActive()
	{
		return isEnabled() && MANIFOLD.isEnabled() && !MC.isInSingleplayer();
	}
	
	@Override
	public String getPrimaryAction()
	{
		return MANIFOLD.translate("button.maniFold.nochatreports."
			+ (isEnabled() ? "re-enable_signatures" : "disable_signatures"));
	}
	
	@Override
	public void doPrimaryAction()
	{
		disableSignatures.setChecked(!disableSignatures.isChecked());
	}
	
	@Override
	public Category getCategory()
	{
		return Category.CHAT;
	}
	
	// See ChatHudMixin, ClientPlayNetworkHandlerMixin.onOnServerMetadata(),
	// MinecraftClientMixin.onGetProfileKeys()
}
