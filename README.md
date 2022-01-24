# chat-lag-fix

Fixes Minecraft client lag from receiving chat messages
<br>
### YOU LIKELY DO NOT NEED THIS MOD ANYMORE

**Mojang has fixed the bug in their API that was causing it to return an error instead of an empty list. Minecraft will fetch the block-list once when joining the world. This mod may still improve world loading times by the time it takes to make that HTTP request; especially if you have a slow internet connection.**

**Technical Details**: When receiving a chat message, Minecraft sends a HTTP request in the render thread to check your player block list. The current frame does not finish rendering until this request has finished, causing a lag spike. This mod lets chat messages through until a block-list can be fetched.

**Why is this happening now and not before?**: something happened to Mojang's API. When joining a world, the blocklist would usually load fine, but Mojang's API has started returning errors for users who have migrated to Microsoft accounts, causing the block list to be fetched in 2 minute intervals when receiving a message.   

**A commented walkthrough of why the bug occurs**

```java
private static final long BLOCKLIST_REQUEST_COOLDOWN = 120;
private Instant nextRequest;
private final blockList Set<UUID>;

// This method is called when receiving a chat message
public boolean isBlockedPlayer(UUID playerID) {
    // If we don't have the blocklist yet, fetch it
    // Note that when there's an error, fetchBlockList returns null
    // 
    if (this.blockList == null) {
        this.blockList = fetchBlockList();
        // If we still don't have it, assume the player is not blocked
        if (this.blockList == null) {
            return false;
        }
    }
    return this.blockList.contains(playerID);
}

public Set<UUID> fetchBlockList() {
    // Only check at least every 2 minutes.
    // This is why the lagspike only occurs every 2 minutes or later
    if (this.nextRequest == null || Instant.now().isAfter(this.nextRequest)) {
        return null;
    }
    // Reset the 2 minute timer
    this.nextRequest = Instant.now().plusSeconds(BLOCKLIST_REQUEST_COOLDOWN);
    try {
        // Make the HTTP request
        BlockListResponse response = minecraftClient.get(routeBlocklist, BlockListResponse.class);
        return response.getBlockedProfiles();
    } catch (/* exception */) {
        // If there's an error return null
        return null;
    }
}
```

**Downloads:**  
Modrinth: https://modrinth.com/mod/chat-lag-fix <br>
GitHub Releases: https://github.com/adryd325/chat-lag-fix/releases <br>
Curseforge: The site has such awful UX and is so slow that I can't be bothered.  

**Development is split into 3 branches:**  
**1.16**: https://github.com/adryd325/chat-lag-fix/tree/1.16  
**1.17**: https://github.com/adryd325/chat-lag-fix/tree/1.17  
**1.18**: https://github.com/adryd325/chat-lag-fix/tree/1.18
<details>
  <summary>GIF Example of the bug showing that it occurs in vanilla as well as modded environments</summary>
  <br>
  <img src="https://cdn.discordapp.com/attachments/857368936672526356/929654718660214804/chat-lag.gif" style="max-width: 100%">
</details>
