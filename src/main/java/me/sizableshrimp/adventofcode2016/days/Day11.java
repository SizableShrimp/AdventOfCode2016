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

import it.unimi.dsi.fastutil.longs.Long2BooleanMap;
import it.unimi.dsi.fastutil.longs.Long2BooleanOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2IntMap;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashBigSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.sizableshrimp.adventofcode2016.templates.Day;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day11 extends Day {
    private static final Pattern GENERATOR = Pattern.compile("(\\w+) generator");
    private static final Pattern MICROCHIP = Pattern.compile("(\\w+)-compatible microchip");
    private static final List<String> PART_2_DEVICES = List.of("elerium", "dilithium");
    private final List<Device> devices = new ArrayList<>();
    private final List<Device> devicesPart2 = new ArrayList<>();
    private long startingFloors;

    public static void main(String[] args) {
        new Day11().run();
    }

    @Override
    protected Result evaluate() {
        int minSteps = simulate(this.devices);
        int minStepsP2 = simulate(this.devicesPart2);

        return Result.of(minSteps, minStepsP2);
    }

    private int simulate(List<Device> devices) {
        Queue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(Node::steps));
        Map<Node, Node> prevMap = null; // new HashMap<>();
        queue.add(new Node(devices, devices.size(), 0, 0, this.startingFloors, 0));
        Long2IntMap seenMap = new Long2IntOpenHashMap();

        while (!queue.isEmpty()) {
            Node node = queue.remove();

            if (node.isFinished()) {
                return node.steps;
            }

            node.forEachOnCurFloor(a -> {
                node.tryMove(queue, prevMap, seenMap, a, null);
                node.forEachOnCurFloor(b -> {
                    if (a == b)
                        return;

                    node.tryMove(queue, prevMap, seenMap, a, b);
                });
            });
        }

        throw new IllegalStateException();
    }

    private static String writeSteps(Node node, Map<Node, Node> prevMap) {
        StringBuilder result = new StringBuilder();
        List<Node> nodes = new ArrayList<>();
        if (prevMap != null) {
            Node parent = prevMap.get(node);
            while (parent != null) {
                nodes.add(0, parent);
                parent = prevMap.get(parent);
            }
        }
        nodes.add(node);
        for (Node n : nodes) {
            for (int i = 3; i >= 0; i--) {
                result.append('F');
                result.append(i + 1);
                result.append(' ');
                result.append(i == n.curFloorIdx ? "E  " : ".  ");
                for (Device device : n.devices) {
                    if (n.getFloorIndex(device) == i) {
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
        }

        return result.toString();
    }

    private static String writeFloors(List<Device> devices, long floors) {
        StringBuilder result = new StringBuilder();

        for (int i = 3; i >= 0; i--) {
            result.append('F');
            result.append(i + 1);
            result.append(' ');
            for (Device device : devices) {
                if (Node.getFloorIndex(floors, device.index) == i) {
                    result.append(Character.toUpperCase(device.key.charAt(0)));
                    result.append(device.generator ? 'G' : 'M');
                    result.append(' ');
                } else {
                    result.append(".  ");
                }
            }
            result.append('\n');
        }

        return result.toString();
    }

    @Override
    protected void parse() {
        this.startingFloors = 0L;
        this.devices.clear();
        this.devicesPart2.clear();

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

        this.devicesPart2.addAll(this.devices);

        // All new devices in part 2 are on the first floor, so we don't need to change the starting floors number because they would just pad zeros.
        PART_2_DEVICES.forEach(extraDevice -> {
            int idx = this.devicesPart2.size();
            this.devicesPart2.add(new Device(extraDevice, true, idx, idx + 1));
            this.devicesPart2.add(new Device(extraDevice, false, idx + 1, idx));
        });
    }

    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    private static final class Device {
        private final String key;
        private final boolean generator;
        private final int index;
        private int oppositeIndex;

        boolean microchip() {
            return !this.generator;
        }
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
            return writeSteps(this, null);
        }

        boolean isFinished() {
            return allOnFloor(this.floors, 3);
        }

        boolean allOnFloor(long floors, int floorIdx) {
            long data = floors;

            for (int i = 0; i < this.devicesCount; i++) {
                if (data == 0 || (data & 3) != floorIdx)
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

        private static final LongSet checked = new LongOpenHashBigSet();
        private static final Long2BooleanMap checkedMap = new Long2BooleanOpenHashMap();

        private boolean isValid(Long2IntMap seenMap, int curFloorIdx, long floors) {
            boolean checkedVal = checkedMap.get(floors);
            if (checked.contains(floors) && !checkedVal)
                return false;

            long key = encodeKey(this.devices, floors, curFloorIdx);
            if (seenMap.containsKey(key) && this.steps + 1 >= seenMap.get(key))
                return false;

            if (checkedVal)
                return true;

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

        void tryMove(Queue<Node> queue, Map<Node, Node> prevMap, Long2IntMap seenMap, Device a, @Nullable Device b) {
            boolean canMoveDown = this.curFloorIdx > this.minFloorIdx;
            boolean canMoveUp = this.curFloorIdx < 3;

            if (canMoveUp)
                tryMove(1, queue, prevMap, seenMap, a, b);

            if (canMoveDown)
                tryMove(-1, queue, prevMap, seenMap, a, b);
        }

        void tryMove(int delta, Queue<Node> queue, Map<Node, Node> prevMap, Long2IntMap seenMap, Device a, @Nullable Device b) {
            int newFloorIdx = this.curFloorIdx + delta;
            long newFloors = updateFloors(delta, a, b);

            if (isValid(seenMap, newFloorIdx, newFloors)) {
                Node newNode = this.newNode(newFloorIdx, newFloors, false);
                if (prevMap != null)
                    prevMap.put(newNode, this);
                seenMap.put(encodeKey(this.devices, newFloors, newFloorIdx), newNode.steps);
                queue.add(newNode);
            }
        }

        private static long encodeKey(List<Device> devices, long floors, int curFloorIdx) {
            return (floors << 2) | curFloorIdx;
            // long result = 0;
            // for (Device device : devices) {
            //     if (device.generator) {
            //         int floorIdxGen = getFloorIndex(floors, device.index);
            //         int floorIdxChip = getFloorIndex(floors, device.oppositeIndex);
            //         int subPart = (Math.min(floorIdxGen, floorIdxChip) << 2 | Math.max(floorIdxGen, floorIdxChip));
            //         if (result == 0) {
            //             result = subPart;
            //         } else {
            //             int i = 0;
            //             for (long data = result; ; data >>= 4) {
            //                 if ((data & 0b1111) <= subPart) {
            //                     int place = i << 2;
            // TODO FIX

            //                     long mask = (Long.MAX_VALUE >> place) << place;
            //                     result = Math.abs((result & mask) << 4 | subPart | (result & mask));
            //                     break;
            //                 }
            //                 i++;
            //             }
            //         }
            //     }
            // }
            // return (result << 2) | curFloorIdx;
        }

        long updateFloors(int delta, Device a, @Nullable Device b) {
            long newData = this.curFloorIdx ^ (this.curFloorIdx + delta);
            int aIdx = a.index << 1;
            long newFloors = this.floors ^ (newData << aIdx);

            if (b != null) {
                int bIdx = b.index << 1;
                return newFloors ^ (newData << bIdx);
            }

            return newFloors;
        }

        // int getScore() {
        //     int score = 0;
        //     for (int i = 4; i > 0; i--) {
        //         Set<Device> floor = this.floors.get(i - 1);
        //         score += floor.size() * i;
        //     }
        //     return score;
        // }

        Node newNode(int curFloorIdx, long newFloors, boolean increaseMinFloorIdx) {
            return new Node(this.devices, this.devicesCount, increaseMinFloorIdx ? this.minFloorIdx + 1 : this.minFloorIdx, curFloorIdx, newFloors, steps + 1);
        }
    }
}
