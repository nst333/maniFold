/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.update;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.maniFoldclient.maniFoldClient;
import net.maniFoldclient.events.UpdateListener;
import net.maniFoldclient.util.ChatUtils;
import net.maniFoldclient.util.json.JsonException;
import net.maniFoldclient.util.json.JsonUtils;
import net.maniFoldclient.util.json.WsonArray;
import net.maniFoldclient.util.json.WsonObject;

public final class maniFoldUpdater implements UpdateListener
{
	private Thread thread;
	private boolean outdated;
	private Text component;
	
	@Override
	public void onUpdate()
	{
		if(thread == null)
		{
			thread = new Thread(this::checkForUpdates, "maniFoldUpdater");
			thread.start();
			return;
		}
		
		if(thread.isAlive())
			return;
		
		if(component != null)
			ChatUtils.component(component);
		
		maniFoldClient.INSTANCE.getEventManager().remove(UpdateListener.class,
			this);
	}
	
	public void checkForUpdates()
	{
		Version currentVersion = new Version(maniFoldClient.VERSION);
		Version latestVersion = null;
		
		try
		{
			WsonArray wson = JsonUtils.parseURLToArray(
				"https://api.github.com/repos/maniFold-Imperium/maniFold-MCX2/releases");
			
			for(WsonObject release : wson.getAllObjects())
			{
				if(!currentVersion.isPreRelease()
					&& release.getBoolean("prerelease"))
					continue;
				
				if(!containsCompatibleAsset(release.getArray("assets")))
					continue;
				
				String tagName = release.getString("tag_name");
				latestVersion = new Version(tagName.substring(1));
				break;
			}
			
			if(latestVersion == null)
				throw new NullPointerException("Latest version is missing!");
			
			System.out.println("[Updater] Current version: " + currentVersion);
			System.out.println("[Updater] Latest version: " + latestVersion);
			outdated = currentVersion.shouldUpdateTo(latestVersion);
			
		}catch(Exception e)
		{
			System.err.println("[Updater] An error occurred!");
			e.printStackTrace();
		}
		
		String currentVersionEncoded = URLEncoder.encode(
			"maniFold " + currentVersion + " MC" + maniFoldClient.MC_VERSION,
			StandardCharsets.UTF_8);
		
		String baseUrl = "https://www.maniFoldclient.net/download/";
		String utmSource = "maniFold+Client";
		String utmMedium = "maniFoldUpdater+chat+message";
		
		if(latestVersion == null || latestVersion.isInvalid())
		{
			String text = "An error occurred while checking for updates."
				+ " Click \u00a7nhere\u00a7r to check manually.";
			String url = baseUrl + "?utm_source=" + utmSource + "&utm_medium="
				+ utmMedium + "&utm_content=" + currentVersionEncoded
				+ "+error+checking+updates+chat+message";
			showLink(text, url);
			return;
		}
		
		if(!outdated)
			return;
		
		String text = "maniFold " + latestVersion
			+ " is now available for Minecraft " + maniFoldClient.MC_VERSION
			+ ". \u00a7nUpdate now\u00a7r to benefit from new features and/or bugfixes!";
		String utmContent = currentVersionEncoded + "+update+chat+message";
		
		String url = baseUrl + "?utm_source=" + utmSource + "&utm_medium="
			+ utmMedium + "&utm_content=" + utmContent;
		
		showLink(text, url);
	}
	
	private void showLink(String text, String url)
	{
		ClickEvent event = new ClickEvent.OpenUrl(URI.create(url));
		component = Text.literal(text).styled(s -> s.withClickEvent(event));
	}
	
	private boolean containsCompatibleAsset(WsonArray wsonArray)
		throws JsonException
	{
		String compatibleSuffix = "MC" + maniFoldClient.MC_VERSION + ".jar";
		
		for(WsonObject asset : wsonArray.getAllObjects())
		{
			String assetName = asset.getString("name");
			if(!assetName.endsWith(compatibleSuffix))
				continue;
			
			return true;
		}
		
		return false;
	}
	
	public boolean isOutdated()
	{
		return outdated;
	}
}
