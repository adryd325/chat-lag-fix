package com.adryd.chatlagfix.mixin;

import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.response.BlockListResponse;
import com.mojang.authlib.yggdrasil.response.Response;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.net.URL;

@Mixin(value = YggdrasilAuthenticationService.class, remap = false)
public interface IMixinYggdrasilAuthenticationService_makeRequestInvoker {
    @Invoker
    <T extends Response> T invokeMakeRequest(URL routeBlocklist, Object o, Class<BlockListResponse> blockListResponseClass, String s) throws AuthenticationException;
}
