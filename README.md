# PositionalChat [![Build Status](http://jenkins.carrade.eu/job/PositionalChat/badge/icon)](http://jenkins.carrade.eu/job/PositionalChat/)
Just like Mumble's positional audio, but with text chat.

This plugin brings back positional conversatons to text chat, sending semi-obfuscated messages based on the distance.

There's also a `/yell` command, if you need to be read clearely by everyone.

![Preview](http://raw.carrade.eu/s/1446771408.png)


### Commands

 - `/yell <message>` — sends a clear message to the whole server.  
   *Permission: `positionalchat.yell` — default: operators.*


### Other permissions

 - `positionalchat.clearview` — Players with this permission always receive clear messages, regardless of the distance.  
   *Default: no-one.*
 - `positionalchat.clearvoice` — Players with this permission always send clear messages, regardless of the distance.  
   Please note that players with this permission are not just like players constantly using `/yell`, as this permission will only unobfuscate the message, without the use of the `/yell` prefixes.  
   *Default: no-one.*


### Config

```yml
distances:
    # Chat before this distance will always be non-obfuscated (in blocks)
    clearBefore: 30

    # Chat after this distance will always be fully obfuscated (in blocks)
    obfuscatedAfter: 150

# Remove colors from all partially or completly obfuscated messages?
# The color is only removed from obfuscated parts.
clearObfuscatedMessagesColors: false

replacements:
    # Hide the obfuscated part using Minecraft's MAGIC formatter.
    magic: true

    # Hide the obfuscated part by replacing the characters with a random value.
    # Use this to prevent players to consult their game log to see through the obfuscation.
    # You'll lose the characters' width.
    replaceObfuscatedText: false

yell:
    # The prefixes of the yell messages.
    # The full-text prefix is prepended to the whole message, before "<nickname>".
    # The message prefix is prepended to the player's message, after the "<nickname> ".
    # You can use formatting codes here, either using '§' or '&'.
    prefix:
        full-text: "\u2756 "
        message: ""
```


### License

Published under the CeCILL-B license (see `LICENSE` file).
