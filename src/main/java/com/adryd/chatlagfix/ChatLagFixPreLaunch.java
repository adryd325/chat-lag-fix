package com.adryd.chatlagfix;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.fabricmc.loader.impl.util.UrlConversionException;
import net.fabricmc.loader.impl.util.UrlUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Optional;

public class ChatLagFixPreLaunch implements PreLaunchEntrypoint {
    // Mixin to libraries code taken from https://github.com/kb-1000/no-telemetry/blob/main/src/main/java/de/kb1000/notelemetry/NoTelemetry.java

    private static final String[] libraryMixinTargets = {
            "com/mojang/authlib/yggdrasil/YggdrasilSocialInteractionsService.class"
    };

    private static Optional<URL> getSource(ClassLoader loader, String filename) {
        URL url;

        if ((url = loader.getResource(filename)) != null) {
            try {
                URL urlSource = UrlUtil.getSource(filename, url);
                return Optional.of(urlSource);
            } catch (UrlConversionException e) {
                e.printStackTrace();
                return Optional.empty();
            }
        }

        return Optional.empty();
    }

    @Override
    public void onPreLaunch() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            Method m = classLoader.getClass().getMethod("addURL", URL.class);
            m.setAccessible(true);
            for (String mixinTarget : libraryMixinTargets) {
                Optional<URL> sourceURL = getSource(classLoader.getParent().getParent().getParent(), mixinTarget);
                if (sourceURL.isPresent()) {
                    ChatLagFixMod.LOGGER.info("Added {} to loom", mixinTarget);
                    m.invoke(classLoader, sourceURL.get());
                }
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
