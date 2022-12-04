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

import com.google.common.hash.Hashing;
import lombok.SneakyThrows;
import me.sizableshrimp.adventofcode2016.templates.Day;

import java.nio.charset.StandardCharsets;

public class Day05 extends Day {

    private String password;

    public static void main(String[] args) {
        new Day05().run();
    }

    @SneakyThrows
    @Override
    protected Result evaluate() {
        // StringBuilder code = new StringBuilder();
        // for (int i = 0; code.length() < 8; i++) {
        //     String hashed = Hashing.md5().hashString(password + i, StandardCharsets.UTF_8).toString();
        //     if (hashed.startsWith("00000"))
        //         code.append(hashed.charAt(5));
        // }
        StringBuilder code = new StringBuilder("        ");
        int found = 0;
        for (int i = 0; found < 8; i++) {
            String hashed = Hashing.md5().hashString(password + i, StandardCharsets.UTF_8).toString();
            if (hashed.startsWith("00000")) {
                char c = hashed.charAt(5);
                if (c >= '0' && c <= '7') {
                    int pos = c - '0';
                    if (code.charAt(pos) == ' ') {
                        code.setCharAt(pos, hashed.charAt(6));
                        found++;
                    }
                }
            }
        }
        return Result.of(null, code.toString());
    }

    @Override
    protected void parse() {
        password = lines.get(0);
    }
}
