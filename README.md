# chat-lag-fix

Fixes Minecraft client lag from receiving chat messages
<br><br>
**Technical Details**:  When receiving a chat message, Minecraft sends a HTTP request in the render thread to check your player block list. The current frame does not finish rendering until this request has finished, causing a lag spike. This mod lets chat messages through until a block-list can be fetched.
<br><br>
**Why is this happening now and not before?**: something happened to Mojang's API. Uppon joining a world, the blocklist would usually load fine, but Mojang's API has started returning errors for users who have migrated to Microsoft accounts, causing the block list to be fetched in 2 minute intervals uppon receiving a message.
<br><br>
If you'd like to see what happens for yourself, check out com.mojang.authlib.yggdrasil.YggdrasilUserApiService, remember that these methods get called on the render thread when receiving a chat message.
<br><br>
**Downloads**<br>
Modrinth: https://modrinth.com/mod/chat-lag-fix <br>
GitHub Releases: https://github.com/adryd325/chat-lag-fix/releases <br>
Curseforge: The site has such awful UX and is so slow that I can't be bothered. 
<br><br>
**Development is split into 3 branches**  
**1.16**: https://github.com/adryd325/chat-lag-fix/tree/1.16  
**1.17**: https://github.com/adryd325/chat-lag-fix/tree/1.17  
**1.18**: https://github.com/adryd325/chat-lag-fix/tree/1.18
<details>
  <summary>GIF Example of the bug</summary>
<br>
<img src="https://cdn.discordapp.com/attachments/857368936672526356/929654718660214804/chat-lag.gif" style="max-width: 100%">

</details>
