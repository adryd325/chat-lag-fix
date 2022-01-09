# chat-lag-fix

Fixes Minecraft client lag from receiving chat messages.

Mojang in their infinite wisdom make a blocking HTTP request in the render thread when chat messages are received. This mod lazy-loads the block-list and prevents lag spikes caused by fetching the block list

There are no config options, it just works out of the box.

### Alternatives

If you use the player blocking feature, this mod by @KosmX optimizes the http request by spawning a thread:  
https://github.com/KosmX/non-blocking_chat

### Download

Modrinth: https://modrinth.com/mod/chat-lag-fix  
Direct: https://adryd.co/chatlagfix/chatlagfix-2.0.0.jar
