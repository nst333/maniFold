/*
 * Copyright (c) 2014-2025 maniFold-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.maniFoldclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.resource.language.LanguageManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import net.maniFoldclient.maniFoldClient;

@Mixin(LanguageManager.class)
public abstract class LanguageManagerMixin
	implements SynchronousResourceReloader
{
	@Inject(at = @At("HEAD"),
		method = "reload(Lnet/minecraft/resource/ResourceManager;)V")
	private void onReload(ResourceManager manager, CallbackInfo ci)
	{
		// Using a mixin for this because maniFoldClient.initialize() runs too
		// early to call ResourceManager.registerReloader()
		maniFoldClient.INSTANCE.getTranslator().reload(manager);
	}
}
