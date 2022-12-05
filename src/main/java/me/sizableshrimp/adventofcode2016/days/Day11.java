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

package me.sizableshrimp.adventofcode2016.days;

import it.unimi.dsi.fastutil.longs.Long2IntMap;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.sizableshrimp.adventofcode2016.templates.Day;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day11 extends Day {
    private static final Pattern GENERATOR = Pattern.compile("(\\w+) generator");
    private static final Pattern MICROCHIP = Pattern.compile("(\\w+)-compatible microchip");
    private final List<Device> devices = new ArrayList<>();
    private long startingFloors;

    public static void main(String[] args) {
        new Day11().run();
    }

    @Override
    protected Result evaluate() {
        int minSteps = simulate();

        // Part 2 - LOL!
        return Result.of(minSteps, minSteps + 24);
    }

    private int simulate() {
        int startSteps = 0;
        long startingFloors = this.startingFloors;
        List<Device> newDevices = new ArrayList<>(this.devices);

        for (Device device : this.devices) {
            if (!device.generator)
                continue;

            int floorIndex = Node.getFloorIndex(startingFloors, device.index);
            if (floorIndex == 3)
                continue;

            if (floorIndex == Node.getFloorIndex(startingFloors, device.oppositeIndex)) {
                if (Node.getCountOnFloor(this.devices.size(), startingFloors, floorIndex) == 2)
                    continue;

                // Smart thing that someone on the subreddit mega thread found
                startSteps += (3 - floorIndex) * 4;
                startingFloors = Node.updateFloors(floorIndex, startingFloors, 3 - floorIndex, device.index, device.oppositeIndex);
                newDevices.remove(device);
                newDevices.remove(this.devices.get(device.oppositeIndex));
            }
        }

        Queue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(Node::steps));
        // Intentionally use the old size so that higher device indices still work
        queue.add(new Node(newDevices, this.devices.size(), 0, 0, startingFloors, startSteps));
        Long2IntMap seenMap = new Long2IntOpenHashMap();

        while (!queue.isEmpty()) {
            Node node = queue.remove();

            if (node.isFinished()) {
                return node.steps;
            }

            node.forEachOnCurFloor(a -> {
                node.tryMove(queue, seenMap, a, null);
                node.forEachOnCurFloor(b -> {
                    if (a == b)
                        return;

                    node.tryMove(queue, seenMap, a, b);
                });
            });
        }

        throw new IllegalStateException();
    }

    @Override
    protected void parse() {
        this.startingFloors = 0L;
        this.devices.clear();

        for (int i = 0; i < 4; i++) {
            Matcher matcher = GENERATOR.matcher(lines.get(i));
            while (matcher.find()) {
                int idx = this.devices.size();
                this.startingFloors |= ((long) i) << (2 * idx);
                this.devices.add(new Device(matcher.group(1), true, idx));
            }
            matcher = MICROCHIP.matcher(lines.get(i));
            while (matcher.find()) {
                int idx = this.devices.size();
                this.startingFloors |= ((long) i) << (2 * idx);
                this.devices.add(new Device(matcher.group(1), false, idx));
            }
        }

        this.devices.forEach(a -> this.devices.forEach(b -> {
            if (a != b && a.key.equals(b.key)) {
                a.setOppositeIndex(b.index);
                b.setOppositeIndex(a.index);
            }
        }));
    }

    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    private static final class Device {
        private final String key;
        private final boolean generator;
        private final int index;
        private int oppositeIndex;
    }

    private record Node(List<Device> devices, int devicesCount, int minFloorIdx, int curFloorIdx, long floors, int steps) {
        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Node node = (Node) o;
            return curFloorIdx == node.curFloorIdx && floors == node.floors;
        }

        @Override
        public int hashCode() {
            return Objects.hash(curFloorIdx, floors);
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();

            for (int i = 3; i >= 0; i--) {
                result.append('F');
                result.append(i + 1);
                result.append(' ');
                result.append(i == this.curFloorIdx ? "E  " : ".  ");
                for (Device device : this.devices) {
                    if (this.getFloorIndex(device) == i) {
                        result.append(Character.toUpperCase(device.key.charAt(0)));
                        result.append(device.generator ? 'G' : 'M');
                        result.append(' ');
                    } else {
                        result.append(".  ");
                    }
                }
                result.append('\n');
            }
            result.append('\n');

            return result.toString();
        }

        boolean isFinished() {
            long data = this.floors;

            for (int i = 0; i < this.devicesCount; i++) {
                if (data == 0 || (data & 3) != 3)
                    return false;

                data >>= 2;
            }

            return true;
        }

        boolean noneOnFloor(long floors, int floorIdx) {
            long data = floors;

            for (int i = 0; i < this.devicesCount; i++) {
                if ((data & 3) == floorIdx)
                    return false;

                data >>= 2;
            }

            return true;
        }

        static int getCountOnFloor(int devicesCount, long floors, int floorIdx) {
            int count = 0;
            long data = floors;

            for (int i = 0; i < devicesCount; i++) {
                if ((data & 3) == floorIdx)
                    count++;

                data >>= 2;
            }

            return count;
        }

        int getFloorIndex(Device device) {
            return getFloorIndex(device.index);
        }

        int getFloorIndex(int deviceIndex) {
            if (deviceIndex >= this.devicesCount)
                throw new IllegalArgumentException();

            return getFloorIndex(this.floors, deviceIndex);
        }

        private static int getFloorIndex(long floors, int deviceIndex) {
            return (int) ((floors >> (2 * deviceIndex)) & 3);
        }

        void forEachOnCurFloor(Consumer<Device> consumer) {
            this.forEachOnFloor(consumer, this.curFloorIdx);
        }

        void forEachOnFloor(Consumer<Device> consumer, int floor) {
            for (Device device : this.devices) {
                if (this.getFloorIndex(device) == floor)
                    consumer.accept(device);
            }
        }

        private boolean isValid(Long2IntMap seenMap, long key, long floors) {
            if (seenMap.containsKey(key) && this.steps + 1 >= seenMap.get(key))
                return false;

            for (Device microchip : this.devices) {
                if (microchip.generator)
                    continue;

                int floorIdx = getFloorIndex(floors, microchip.index);

                if (floorIdx != getFloorIndex(floors, microchip.oppositeIndex)) {
                    for (Device generator : this.devices) {
                        if (generator.generator && floorIdx == getFloorIndex(floors, generator.index))
                            return false; // Microchip irradiated!
                    }
                }
            }

            return true;
        }

        void tryMove(Queue<Node> queue, Long2IntMap seenMap, Device a, @Nullable Device b) {
            boolean canMoveDown = this.curFloorIdx > this.minFloorIdx;
            boolean canMoveUp = this.curFloorIdx < 3;

            if (canMoveUp)
                tryMove(1, queue, seenMap, a, b);

            if (canMoveDown)
                tryMove(-1, queue, seenMap, a, b);
        }

        void tryMove(int delta, Queue<Node> queue, Long2IntMap seenMap, Device a, @Nullable Device b) {
            int newFloorIdx = this.curFloorIdx + delta;
            long newFloors = updateFloors(this.curFloorIdx, this.floors, delta, a.index, b == null ? -1 : b.index);
            long key = (newFloors << 2) | newFloorIdx;

            if (isValid(seenMap, key, newFloors)) {
                Node newNode = this.newNode(newFloorIdx, newFloors, delta > 1 && this.minFloorIdx + 1 == this.curFloorIdx && this.noneOnFloor(newFloors, this.curFloorIdx));
                seenMap.put(key, newNode.steps);
                queue.add(newNode);
            }
        }

        static long updateFloors(int curFloorIdx, long floors, int delta, int aIndex, int bIndex) {
            long newData = curFloorIdx ^ (curFloorIdx + delta);
            int aIdx = aIndex << 1;
            long newFloors = floors ^ (newData << aIdx);

            if (bIndex != -1) {
                int bIdx = bIndex << 1;
                return newFloors ^ (newData << bIdx);
            }

            return newFloors;
        }

        Node newNode(int curFloorIdx, long newFloors, boolean increaseMinFloorIdx) {
            return new Node(this.devices, this.devicesCount, increaseMinFloorIdx ? this.minFloorIdx + 1 : this.minFloorIdx, curFloorIdx, newFloors, steps + 1);
        }
    }
}
