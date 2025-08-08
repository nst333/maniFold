/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.hud;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.maniFoldclient.maniFoldClient;
import net.maniFoldclient.other_features.maniFoldLogoOtf;
import net.maniFoldclient.util.RenderUtils;

public final class maniFoldLogo
{
	private static final maniFoldClient MANIFOLD = maniFoldClient.INSTANCE;
	private static final Identifier LOGO_TEXTURE =
		Identifier.of("manifold", "manifold_128.png");
	
	public void render(DrawContext context)
	{
		maniFoldLogoOtf otf = MANIFOLD.getOtfs().maniFoldLogoOtf;
		if(!otf.isVisible())
			return;
		
		String version = getVersionString();
		TextRenderer tr = maniFoldClient.MC.textRenderer;
		
		// background
		int bgColor;
		if(MANIFOLD.getHax().rainbowUiHack.isEnabled())
			bgColor =
				RenderUtils.toIntColor(MANIFOLD.getGui().getAcColor(), 0.5F);
		else
			bgColor = otf.getBackgroundColor();
		context.fill(0, 6, tr.getWidth(version) + 76, 17, bgColor);
		
		context.state.goUpLayer();
		
		// version string
		context.drawText(tr, version, 74, 8, otf.getTextColor(), false);
		
		// maniFold logo
		context.drawTexture(RenderPipelines.GUI_TEXTURED, LOGO_TEXTURE, 0, 3, 0,
			0, 72, 18, 72, 18);
		
		context.state.goDownLayer();
	}
	
	private String getVersionString()
	{
		String version = "v" + maniFoldClient.VERSION;
		version += " MC" + maniFoldClient.MC_VERSION;
		
		if(MANIFOLD.getUpdater().isOutdated())
			version += " (outdated)";
		
		return version;
	}
}
