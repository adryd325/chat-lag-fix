# chat-lag-fix

Fixes Minecraft client lag from receiving chat messages, at the expense of not being able to block players in chat. 

Mojang in their infinite wisdom make a blocking HTTP request in the render thread when chat messages are received.

There are no config options, it just works out of the box.

### Alternatives

If you use the player blocking feature, this mod by @KosmX optimizes the http request by spawning a thread:  
https://github.com/KosmX/non-blocking_chat

### Download

Modrinth: https://modrinth.com/mod/chat-lag-fix  
Direct: https://adryd.co/chatlagfix/chatlagfix-1.0.0.jar
