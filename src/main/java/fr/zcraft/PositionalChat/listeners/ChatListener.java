/*
 * Copyright or © or Copr. AmauryCarrade (2015)
 * 
 * http://amaury.carrade.eu
 * 
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 * 
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 * 
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.zcraft.PositionalChat.listeners;

import fr.zcraft.PositionalChat.PositionalChat;
import fr.zcraft.PositionalChat.events.AsyncPlayerYellEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashSet;


public class ChatListener implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent ev)
    {
        // Yell messages are not obfuscated.
        if(ev instanceof AsyncPlayerYellEvent)
            return;

        // Players with this permission always send clear messages
        if(ev.getPlayer().hasPermission("positionalchat.clearvoice"))
            return;


        final String formattedMessage = String.format(ev.getFormat(), ev.getPlayer().getDisplayName(), ev.getMessage());

        final Location senderLocation = ev.getPlayer().getLocation();

        final double minDistance = PositionalChat.get().getPCConfig().getClearBefore();
        final double maxDistance = PositionalChat.get().getPCConfig().getObfuscatedAfter();

        // The message is sent for each player
        for(Player receiver : new HashSet<>(ev.getRecipients()))
        {
            if(receiver.equals(ev.getPlayer()) || receiver.hasPermission("positionalchat.clearview"))
                continue;


            double distanceSquared = senderLocation.getWorld().equals(receiver.getWorld()) ? senderLocation.distance(receiver.getLocation()) : maxDistance;

            // If the receiver is close enough to receive a clear message, the message isn't altered.
            // Here, the message will be altered.
            // This receiver is removed from the list, and a message is sent for him.
            if (distanceSquared >= minDistance)
            {
                ev.getRecipients().remove(receiver);

                // TODO real chat message (instead of system one).
                float obfuscationPercentage = (float) Math.min((distanceSquared - minDistance) / (maxDistance - minDistance), 1);
                receiver.sendMessage(
                        PositionalChat.get().getTextObfuscator().obfuscate(
                                formattedMessage, obfuscationPercentage,
                                PositionalChat.get().getPCConfig().clearObfuscatedMessagesColors(),
                                PositionalChat.get().getPCConfig().useMagicToObfuscate(),
                                PositionalChat.get().getPCConfig().useRandomCharsToObfuscate()
                        )
                );
            }
        }
    }
}
