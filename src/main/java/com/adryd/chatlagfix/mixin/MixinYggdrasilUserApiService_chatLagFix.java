package com.adryd.chatlagfix.mixin;

import com.mojang.authlib.exceptions.MinecraftClientException;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.authlib.yggdrasil.YggdrasilUserApiService;
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

@Mixin(value = YggdrasilUserApiService.class, remap = false)
public abstract class MixinYggdrasilUserApiService_chatLagFix {

    @Final
    @Shadow
    private URL routeBlocklist;

    @Final
    @Shadow
    private MinecraftClient minecraftClient;

    @Shadow
    private Set<UUID> blockList;

    @Inject(method = "canMakeBlockListRequest", at = @At("HEAD"), cancellable = true)
    private void dontReFetchBlockList(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(this.blockList != null);
    }

    @Inject(method = "forceFetchBlockList", at = @At("HEAD"), cancellable = true)
    private void safeForceFetchBlockList(CallbackInfoReturnable<Set<UUID>> cir) {
        // Return an empty set immediately and update the list once the request has finished
        CompletableFuture.runAsync(() -> {
            try {
                final BlockListResponse response = minecraftClient.get(routeBlocklist, BlockListResponse.class);
                this.blockList = response.getBlockedProfiles();
            } catch (final MinecraftClientException ignored) {
                this.blockList = Set.of(new UUID(0,0));
            }
        });
        cir.setReturnValue(Set.of());
    }
}
