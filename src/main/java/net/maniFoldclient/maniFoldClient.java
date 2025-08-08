/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.client.MinecraftClient;
import net.maniFoldclient.altmanager.AltManager;
import net.maniFoldclient.altmanager.Encryption;
import net.maniFoldclient.analytics.PlausibleAnalytics;
import net.maniFoldclient.clickgui.ClickGui;
import net.maniFoldclient.command.CmdList;
import net.maniFoldclient.command.CmdProcessor;
import net.maniFoldclient.command.Command;
import net.maniFoldclient.event.EventManager;
import net.maniFoldclient.events.ChatOutputListener;
import net.maniFoldclient.events.GUIRenderListener;
import net.maniFoldclient.events.KeyPressListener;
import net.maniFoldclient.events.PostMotionListener;
import net.maniFoldclient.events.PreMotionListener;
import net.maniFoldclient.events.UpdateListener;
import net.maniFoldclient.hack.Hack;
import net.maniFoldclient.hack.HackList;
import net.maniFoldclient.hud.IngameHUD;
import net.maniFoldclient.keybinds.KeybindList;
import net.maniFoldclient.keybinds.KeybindProcessor;
import net.maniFoldclient.mixinterface.IMinecraftClient;
import net.maniFoldclient.navigator.Navigator;
import net.maniFoldclient.other_feature.OtfList;
import net.maniFoldclient.other_feature.OtherFeature;
import net.maniFoldclient.settings.SettingsFile;
import net.maniFoldclient.update.ProblematicResourcePackDetector;
import net.maniFoldclient.update.maniFoldUpdater;
import net.maniFoldclient.util.json.JsonException;

public enum maniFoldClient
{
	INSTANCE;
	
	public static MinecraftClient MC;
	public static IMinecraftClient IMC;
	
	public static final String VERSION = "7.49";
	public static final String MC_VERSION = "1.21.8";
	
	private PlausibleAnalytics plausible;
	private EventManager eventManager;
	private AltManager altManager;
	private HackList hax;
	private CmdList cmds;
	private OtfList otfs;
	private SettingsFile settingsFile;
	private Path settingsProfileFolder;
	private KeybindList keybinds;
	private ClickGui gui;
	private Navigator navigator;
	private CmdProcessor cmdProcessor;
	private IngameHUD hud;
	private RotationFaker rotationFaker;
	private FriendsList friends;
	private maniFoldTranslator translator;
	
	private boolean enabled = true;
	private static boolean guiInitialized;
	private maniFoldUpdater updater;
	private ProblematicResourcePackDetector problematicPackDetector;
	private Path maniFoldFolder;
	
	public void initialize()
	{
		System.out.println("Starting maniFold Client...");
		
		MC = MinecraftClient.getInstance();
		IMC = (IMinecraftClient)MC;
		maniFoldFolder = createmaniFoldFolder();
		
		Path analyticsFile = maniFoldFolder.resolve("analytics.json");
		plausible = new PlausibleAnalytics(analyticsFile);
		plausible.pageview("/");
		
		eventManager = new EventManager(this);
		
		Path enabledHacksFile = maniFoldFolder.resolve("enabled-hacks.json");
		hax = new HackList(enabledHacksFile);
		
		cmds = new CmdList();
		
		otfs = new OtfList();
		
		Path settingsFile = maniFoldFolder.resolve("settings.json");
		settingsProfileFolder = maniFoldFolder.resolve("settings");
		this.settingsFile = new SettingsFile(settingsFile, hax, cmds, otfs);
		this.settingsFile.load();
		hax.tooManyHaxHack.loadBlockedHacksFile();
		
		Path keybindsFile = maniFoldFolder.resolve("keybinds.json");
		keybinds = new KeybindList(keybindsFile);
		
		Path guiFile = maniFoldFolder.resolve("windows.json");
		gui = new ClickGui(guiFile);
		
		Path preferencesFile = maniFoldFolder.resolve("preferences.json");
		navigator = new Navigator(preferencesFile, hax, cmds, otfs);
		
		Path friendsFile = maniFoldFolder.resolve("friends.json");
		friends = new FriendsList(friendsFile);
		friends.load();
		
		translator = new maniFoldTranslator();
		
		cmdProcessor = new CmdProcessor(cmds);
		eventManager.add(ChatOutputListener.class, cmdProcessor);
		
		KeybindProcessor keybindProcessor =
			new KeybindProcessor(hax, keybinds, cmdProcessor);
		eventManager.add(KeyPressListener.class, keybindProcessor);
		
		hud = new IngameHUD();
		eventManager.add(GUIRenderListener.class, hud);
		
		rotationFaker = new RotationFaker();
		eventManager.add(PreMotionListener.class, rotationFaker);
		eventManager.add(PostMotionListener.class, rotationFaker);
		
		updater = new maniFoldUpdater();
		eventManager.add(UpdateListener.class, updater);
		
		problematicPackDetector = new ProblematicResourcePackDetector();
		problematicPackDetector.start();
		
		Path altsFile = maniFoldFolder.resolve("alts.encrypted_json");
		Path encFolder = Encryption.chooseEncryptionFolder();
		altManager = new AltManager(altsFile, encFolder);
	}
	
	private Path createmaniFoldFolder()
	{
		Path dotMinecraftFolder = MC.runDirectory.toPath().normalize();
		Path maniFoldFolder = dotMinecraftFolder.resolve("maniFold");
		
		try
		{
			Files.createDirectories(maniFoldFolder);
			
		}catch(IOException e)
		{
			throw new RuntimeException(
				"Couldn't create .minecraft/maniFold folder.", e);
		}
		
		return maniFoldFolder;
	}
	
	public String translate(String key, Object... args)
	{
		return translator.translate(key, args);
	}
	
	public PlausibleAnalytics getPlausible()
	{
		return plausible;
	}
	
	public EventManager getEventManager()
	{
		return eventManager;
	}
	
	public void saveSettings()
	{
		settingsFile.save();
	}
	
	public ArrayList<Path> listSettingsProfiles()
	{
		if(!Files.isDirectory(settingsProfileFolder))
			return new ArrayList<>();
		
		try(Stream<Path> files = Files.list(settingsProfileFolder))
		{
			return files.filter(Files::isRegularFile)
				.collect(Collectors.toCollection(ArrayList::new));
			
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public void loadSettingsProfile(String fileName)
		throws IOException, JsonException
	{
		settingsFile.loadProfile(settingsProfileFolder.resolve(fileName));
	}
	
	public void saveSettingsProfile(String fileName)
		throws IOException, JsonException
	{
		settingsFile.saveProfile(settingsProfileFolder.resolve(fileName));
	}
	
	public HackList getHax()
	{
		return hax;
	}
	
	public CmdList getCmds()
	{
		return cmds;
	}
	
	public OtfList getOtfs()
	{
		return otfs;
	}
	
	public Feature getFeatureByName(String name)
	{
		Hack hack = getHax().getHackByName(name);
		if(hack != null)
			return hack;
		
		Command cmd = getCmds().getCmdByName(name.substring(1));
		if(cmd != null)
			return cmd;
		
		OtherFeature otf = getOtfs().getOtfByName(name);
		return otf;
	}
	
	public KeybindList getKeybinds()
	{
		return keybinds;
	}
	
	public ClickGui getGui()
	{
		if(!guiInitialized)
		{
			guiInitialized = true;
			gui.init();
		}
		
		return gui;
	}
	
	public Navigator getNavigator()
	{
		return navigator;
	}
	
	public CmdProcessor getCmdProcessor()
	{
		return cmdProcessor;
	}
	
	public IngameHUD getHud()
	{
		return hud;
	}
	
	public RotationFaker getRotationFaker()
	{
		return rotationFaker;
	}
	
	public FriendsList getFriends()
	{
		return friends;
	}
	
	public maniFoldTranslator getTranslator()
	{
		return translator;
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
		
		if(!enabled)
		{
			hax.panicHack.setEnabled(true);
			hax.panicHack.onUpdate();
		}
	}
	
	public maniFoldUpdater getUpdater()
	{
		return updater;
	}
	
	public ProblematicResourcePackDetector getProblematicPackDetector()
	{
		return problematicPackDetector;
	}
	
	public Path getmaniFoldFolder()
	{
		return maniFoldFolder;
	}
	
	public AltManager getAltManager()
	{
		return altManager;
	}
}
