package com.adryd.chatlagfix.mixin;

import com.adryd.chatlagfix.ChatLagFixMod;
import com.mojang.authlib.exceptions.MinecraftClientException;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.authlib.yggdrasil.YggdrasilSocialInteractionsService;
import com.mojang.authlib.yggdrasil.response.BlockListResponse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.URL;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Mixin(value = YggdrasilSocialInteractionsService.class, remap = false)
public abstract class MixinYggdrasilSocialInteractionsService_chatLagFix {

    @Final
    @Shadow
    private URL routeBlocklist;

    @Final
    @Shadow
    private MinecraftClient minecraftClient;

    @Shadow
    private Set<UUID> blockList;


    @Inject(method = "fetchBlockList", at = @At("HEAD"), cancellable = true)
    private void safeForceFetchBlockList(CallbackInfoReturnable<Set<UUID>> cir) {
        // Don't fetch block list multiple times
        if (this.blockList != null) {
            cir.setReturnValue(this.blockList);
            return;
        }
        ChatLagFixMod.LOGGER.debug("YggdrasilUserApiService#forceFetchBlockList(): Fetching block list");
        // Return an empty set immediately and update the list once the request has finished
        CompletableFuture.runAsync(() -> {
            try {
                final BlockListResponse response = minecraftClient.get(routeBlocklist, BlockListResponse.class);
                ChatLagFixMod.LOGGER.debug("YggdrasilUserApiService#forceFetchBlockList(): Block list fetch was successful");
                this.blockList = response.getBlockedProfiles();
            } catch (final MinecraftClientException ignored) {
                ChatLagFixMod.LOGGER.debug("YggdrasilUserApiService#forceFetchBlockList(): Block list fetch failed");
                this.blockList = Set.of(new UUID(0, 0));
            }
        });
        cir.setReturnValue(Set.of());
    }
}
