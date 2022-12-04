/*
 * AdventOfCode2016
 * Copyright (C) 2021 SizableShrimp
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.sizableshrimp.adventofcode2016.days;

import me.sizableshrimp.adventofcode2016.templates.SeparatedDay;

public class Day07 extends SeparatedDay {
    public static void main(String[] args) {
        new Day07().run();
    }

    @Override
    protected Object part1() {
        int count = 0;
        for (String line : lines) {
            if (validAbba(line))
                count++;
        }
        return count;
    }

    @Override
    protected Object part2() {
        int count = 0;
        for (String line : lines) {
            if (validSsl(line))
                count++;
        }
        return count;
    }

    private boolean validAbba(String ip) {
        for (int i = 0; i < ip.length() - 2; i++) {
            char a = ip.charAt(i);
            char b = ip.charAt(i);
            if (a == b || !Character.isLetter(a) || !Character.isLetter(b) || ip.charAt(i + 2) != b || ip.charAt(i + 3) != a)
                continue;
            int openIdx = ip.lastIndexOf('[', i);
            int closeIdx = ip.lastIndexOf(']', i);
            if (openIdx <= closeIdx) {
                return true;
            }
        }

        return false;
    }

    private boolean validSsl(String ip) {
        return validSsl(ip, '0', '0', false);
    }

    private boolean validSsl(String ip, char targetA, char targetB, boolean reversed) {
        for (int i = 0; i < ip.length() - 2; i++) {
            char a = ip.charAt(i);
            char b = ip.charAt(i + 1);
            if (a == b || !Character.isLetter(a) || !Character.isLetter(b) || ip.charAt(i + 2) != a)
                continue;
            int openIdx = ip.lastIndexOf('[', i);
            int closeIdx = ip.lastIndexOf(']', i);
            if (openIdx > closeIdx && reversed) {
                if (a == targetA && b == targetB)
                    return true;
            } else if (openIdx <= closeIdx && !reversed && validSsl(ip, b, a, true)) { // Flip a and b to get BAB
                return true;
            }
        }

        return false;
    }

    @Override
    protected void parse() {

    }
}
