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

import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;

import java.util.Collection;
import java.util.stream.Stream;

public class OccurrenceHelper {
    private static <T> Object2LongOpenHashMap<T> getOccurrences(Stream<T> stream) {
        Object2LongOpenHashMap<T> counts = new Object2LongOpenHashMap<>();
        stream.forEach(k -> counts.addTo(k, 1));
        return counts;
    }

    public static <T> Object2LongOpenHashMap<T> getOccurrences(Collection<T> collection) {
        Object2LongOpenHashMap<T> counts = new Object2LongOpenHashMap<>();
        collection.forEach(k -> counts.addTo(k, 1));
        return counts;
    }

    public static <T> T getMostOccurring(Stream<T> stream) {
        Object2LongOpenHashMap<T> counts = getOccurrences(stream);
        return getMostOccurring(counts);
    }

    public static <T> T getMostOccurring(Collection<T> collection) {
        Object2LongOpenHashMap<T> counts = getOccurrences(collection);
        return getMostOccurring(counts);
    }

    public static <T> T getMostOccurring(Object2LongMap<T> counts) {
        return getOccurrence(counts, true);
    }

    public static <T> T getLeastOccurring(Stream<T> stream) {
        Object2LongOpenHashMap<T> counts = new Object2LongOpenHashMap<>();
        stream.forEach(k -> counts.addTo(k, 1));
        return getLeastOccurring(counts);
    }

    public static <T> T getLeastOccurring(Collection<T> collection) {
        Object2LongOpenHashMap<T> counts = new Object2LongOpenHashMap<>();
        collection.forEach(k -> counts.addTo(k, 1));
        return getLeastOccurring(counts);
    }

    public static <T> T getLeastOccurring(Object2LongMap<T> counts) {
        return getOccurrence(counts, false);
    }

    private static <T> T getOccurrence(Object2LongMap<T> counts, boolean most) {
        T result = null;
        long targetCount = 0;
        for (var entry : counts.object2LongEntrySet()) {
            long count = entry.getLongValue();
            if (result == null || most && count > targetCount || !most && count < targetCount) {
                result = entry.getKey();
                targetCount = count;
            }
        }
        return result;
    }
}
