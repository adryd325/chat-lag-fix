package com.adryd.chatlagfix.mixin;

import com.adryd.chatlagfix.ChatLagFixMod;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilSocialInteractionsService;
import com.mojang.authlib.yggdrasil.response.BlockListResponse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.URL;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static java.util.Collections.emptySet;

@Mixin(value = YggdrasilSocialInteractionsService.class, remap = false)
public abstract class MixinYggdrasilSocialInteractionsService_chatLagFix {

    @Final
    @Shadow
    private URL routeBlocklist;

    @Shadow
    private Set<UUID> blockList;


    @Shadow
    @Final
    private YggdrasilAuthenticationService authenticationService;

    @Shadow
    @Final
    private String accessToken;

    @Inject(method = "fetchBlockList", at = @At("HEAD"), cancellable = true)
    private void safeForceFetchBlockList(CallbackInfoReturnable<Set<UUID>> cir) {
        ChatLagFixMod.LOGGER.debug("YggdrasilUserApiService#forceFetchBlockList(): Fetching block list");
        cir.setReturnValue(emptySet());
        // Don't fetch block list multiple times
        if (this.blockList != null) {
            cir.setReturnValue(this.blockList);
            return;
        }
        // Return an empty set immediately and update the list once the request has finished
        // This should only create one thread
        new Thread(() -> {
            try {
                final BlockListResponse response = ((IMixinYggdrasilAuthenticationService_makeRequestInvoker) authenticationService).invokeMakeRequest(routeBlocklist, null, BlockListResponse.class, "Bearer " + accessToken);
                if (response == null) {
                    ChatLagFixMod.LOGGER.debug("YggdrasilUserApiService#fetchBlockList(): Block list fetch was empty");
                    this.blockList = emptySet();
                    return;
                }
                ChatLagFixMod.LOGGER.debug("YggdrasilUserApiService#fetchBlockList(): Block list fetch was successful");
                this.blockList = response.getBlockedProfiles();
            } catch (AuthenticationException e) {
                ChatLagFixMod.LOGGER.debug("YggdrasilUserApiService#fetchBlockList(): Block list fetch failed");
            }
        }).start();
        cir.setReturnValue(Collections.emptySet());
    }
}
