package com.adryd.chatlagfix.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(value = MinecraftClient.class)
public class MixinMinecraftClient_chatLagFix {
	@Inject(method = "shouldBlockMessages", at = @At("HEAD"), cancellable = true)
	private void chatLagFix(UUID uuid, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(false);
	}
}