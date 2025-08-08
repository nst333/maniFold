/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.commands;

import net.maniFoldclient.maniFoldClient;
import net.maniFoldclient.command.CmdException;
import net.maniFoldclient.command.CmdSyntaxError;
import net.maniFoldclient.command.Command;
import net.maniFoldclient.hack.Hack;
import net.maniFoldclient.other_feature.OtherFeature;
import net.maniFoldclient.util.ChatUtils;

public final class FeaturesCmd extends Command
{
	public FeaturesCmd()
	{
		super("features",
			"Shows the number of features and some other\n" + "statistics.",
			".features");
	}
	
	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length != 0)
			throw new CmdSyntaxError();
		
		if(maniFoldClient.VERSION.startsWith("7.0pre"))
			ChatUtils.warning(
				"This is just a pre-release! It doesn't (yet) have all of the features of maniFold 7.0! See download page for details.");
		
		int hax = MANIFOLD.getHax().countHax();
		int cmds = MANIFOLD.getCmds().countCmds();
		int otfs = MANIFOLD.getOtfs().countOtfs();
		int all = hax + cmds + otfs;
		
		ChatUtils.message("All features: " + all);
		ChatUtils.message("Hacks: " + hax);
		ChatUtils.message("Commands: " + cmds);
		ChatUtils.message("Other features: " + otfs);
		
		int settings = 0;
		for(Hack hack : MANIFOLD.getHax().getAllHax())
			settings += hack.getSettings().size();
		for(Command cmd : MANIFOLD.getCmds().getAllCmds())
			settings += cmd.getSettings().size();
		for(OtherFeature otf : MANIFOLD.getOtfs().getAllOtfs())
			settings += otf.getSettings().size();
		
		ChatUtils.message("Settings: " + settings);
	}
	
	@Override
	public String getPrimaryAction()
	{
		return "Show Statistics";
	}
	
	@Override
	public void doPrimaryAction()
	{
		MANIFOLD.getCmdProcessor().process("features");
	}
}
