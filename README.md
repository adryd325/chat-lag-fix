# chat-lag-fix

Fixes Minecraft client lag from receiving chat messages, at the expense of not being able to block players in chat. 

Mojang in their infinite wisdom make a blocking HTTP request in the render thread when chat messages are received.

There are no config options, it just works out of the box.

Download: https://adryd.co/chatlagfix/chatlagfix-1.0.0.jar