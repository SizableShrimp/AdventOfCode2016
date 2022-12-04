/*
 * AdventOfCode2016
 * Copyright (C) 2022 SizableShrimp
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

package me.sizableshrimp.adventofcode2016.helper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Permutator {
    /**
     * Permutes the input list into a set of all possible permutations.
     *
     * @param input The input list.
     * @param <T> The type of the list used when permuting.
     * @return A set of all possible permutations of the input list.
     */
    public static <T> Set<List<T>> permute(List<T> input) {
        return internalPermute(new ArrayList<>(input));
    }

    private static <T> Set<List<T>> internalPermute(List<T> base) {
        if (base.isEmpty()) {
            Set<List<T>> perms = new HashSet<>();
            perms.add(new ArrayList<>());
            return perms;
        }

        T first = base.remove(0);
        Set<List<T>> result = new HashSet<>();
        Set<List<T>> permutations = internalPermute(base);
        for (var list : permutations) {
            for (int i = 0; i <= list.size(); i++) {
                List<T> temp = new ArrayList<>(list);
                temp.add(i, first);
                result.add(temp);
            }
        }

        return result;
    }
}
