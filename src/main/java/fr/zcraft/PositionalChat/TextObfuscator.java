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
package fr.zcraft.PositionalChat;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class TextObfuscator
{
    private final static char[] REPLACEMENT_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
    private final Random random;

    /**
     * Constructs a text obfuscator using a custom source of randomness
     *
     * @param random A source of randomness.
     */
    public TextObfuscator(Random random)
    {
        this.random = random;
    }

    /**
     * Constructs a text obfuscator using a random source of randomness.
     */
    public TextObfuscator()
    {
        this(new Random());
    }

    /**
     * Obfuscates a string by adding Minecraft's MAGIC formatters and optionally by replacing the
     * text with a random character.
     *
     * @param text                   The text to be obfuscated.
     * @param obfuscationPercentage  The percentage of obfuscated text in the final version.
     * @param removeColors           If true, the obfuscated parts will be color-less.
     * @param useMagic               If true, magic formatting will be used to obfuscate the texts.
     * @param replaceObfuscatedChars If true, the obfuscated characters will be replaced using a
     *                               random alphanumeric character.
     *
     * @return The obfuscated text.
     */
    public String obfuscate(String text, float obfuscationPercentage, boolean removeColors, boolean useMagic, boolean replaceObfuscatedChars)
    {
        List<Integer> map = generateObfuscationMap(ChatColor.stripColor(text).length(), obfuscationPercentage);

        char[] chars = text.toCharArray();
        int offset = 0;

        String previousColors = "";

        StringBuilder obfuscated = new StringBuilder();

        for (int i = 0; i < chars.length; i++)
        {
            // If this is a color code, we save it to know what is the current formatting,
            // and then skip it.
            if (isColorCode(i, chars))
            {
                obfuscated.append(chars[i]).append(chars[i + 1]);

                ChatColor color = ChatColor.getByChar(chars[i + 1]);

                if (color.isColor() || color == ChatColor.RESET)
                    previousColors = color.toString();
                else
                    previousColors += color.toString();

                offset += 2;
                i += 1; // Incremented another time by the loop

                continue;
            }

            // If this character needs to be obfuscated, the formatting codes are placed around
            // it if needed, and the character is replaced if wanted.
            if (map.contains(i - offset))
            {
                if (removeColors)
                    obfuscated.append(ChatColor.RESET);
                if (useMagic)
                    obfuscated.append(ChatColor.MAGIC);

                if (replaceObfuscatedChars && chars[i] != ' ')
                    obfuscated.append(REPLACEMENT_CHARS[random.nextInt(REPLACEMENT_CHARS.length)]);
                else
                    obfuscated.append(chars[i]);

                if(removeColors || useMagic)
                    obfuscated.append(ChatColor.RESET).append(previousColors);
            }
            else
            {
                obfuscated.append(chars[i]);
            }
        }

        return obfuscated.toString();
    }


    /**
     * Determines the obfuscated characters of a string.
     *
     * @param length                The length of the string.
     * @param obfuscationPercentage The percentage of the string that needs to be obfuscated.
     *
     * @return A list of the indexes of the obfuscated characters.
     */
    private List<Integer> generateObfuscationMap(int length, float obfuscationPercentage)
    {
        // If the percentage of obfuscation is greater than 50%, it's more interesting to
        // generate a mirrored obfuscation (ie to determine the clear parts), and to mirror
        // after the generation.
        boolean whitelist = false;
        if (obfuscationPercentage > 0.5f)
        {
            obfuscationPercentage = 1.0f - obfuscationPercentage;
            whitelist = true;
        }

        int stillNeededObfuscatedLength = (int) Math.ceil(length * obfuscationPercentage);


        List<Integer> availableStartingIndexes = new ArrayList<>();
        for (int i = 0; i < length; i++)
            availableStartingIndexes.add(i);


        // We pick a random obfuscated part until there isn't any obfuscation needed.
        while (stillNeededObfuscatedLength > 0)
        {
            int origin = availableStartingIndexes.remove(random.nextInt(availableStartingIndexes.size()));
            int partLength = random.nextInt(3) + 1;

            // We check for already-obfuscated parts next to this one, and
            // we mark the whole part as obfuscated.
            for (int i = 1; i < partLength; i++)
            {
                // Avoids a double call of `indexOf` (direct + in `contains`).
                int charIndex = availableStartingIndexes.indexOf(origin + i);

                // This char is still marked as available.
                if (charIndex > 0)
                {
                    availableStartingIndexes.remove(charIndex);
                }
                // This char is already taken, so it's not counted in this part. We stop here.
                else
                {
                    partLength -= (partLength - i);
                    break;
                }
            }

            stillNeededObfuscatedLength -= partLength;
        }


        // We generate the obfuscation map (mirror of the available indexes), if needed.
        if (whitelist)
        {
            return availableStartingIndexes;
        }
        else
        {
            List<Integer> obfuscatedChars = new ArrayList<>();
            for (int i = 0; i < length; i++)
                if (!availableStartingIndexes.contains(i))
                    obfuscatedChars.add(i);

            return obfuscatedChars;
        }
    }

    /**
     * Checks if there is a color code at the specified index.
     *
     * @param index  The index.
     * @param string The string, as a {@link char} array.
     *
     * @return {@code true} if there is a color code (§ + a formatting character) at this index of
     * the string.
     */
    private boolean isColorCode(int index, char[] string)
    {
        return string[index] == ChatColor.COLOR_CHAR
                && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(string[index + 1]) > -1;
    }
}
