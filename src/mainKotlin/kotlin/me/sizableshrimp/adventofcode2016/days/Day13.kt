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

package me.sizableshrimp.adventofcode2016.days

import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import me.sizableshrimp.adventofcode2016.templates.Coordinate
import me.sizableshrimp.adventofcode2016.templates.Day
import me.sizableshrimp.adventofcode2016.templates.Direction
import java.util.PriorityQueue
import java.util.function.Predicate

class Day13 : Day() {
    private var designNumber: Long = 0

    override fun evaluate(): Result {
        val walls = Object2BooleanOpenHashMap<Coordinate>()
        val target = Coordinate.of(31, 39)
        val maxBounds = target.multiply(2)
        val isWall: (Coordinate) -> Boolean = { coord ->
            walls.computeIfAbsent(coord, Predicate {
                if (it.x < 0 || it.x > maxBounds.x || it.y < 0 || it.y > maxBounds.y)
                    return@Predicate true

                val num = it.x * it.x + 3 * it.x + 2 * it.x * it.y + it.y + it.y * it.y + this.designNumber
                num.countOneBits() % 2 == 1
            })
        }
        val queue = PriorityQueue<Node>(Comparator.comparingInt {
            it.coord.distance(target)
        })
        queue.add(Node(Coordinate.of(1, 1), 0))
        val minSteps = Object2IntOpenHashMap<Coordinate>()
        var minStepsTarget = Int.MAX_VALUE
        val reachablePart2 = HashSet<Coordinate>()

        while (!queue.isEmpty()) {
            val node = queue.remove()

            Direction.cardinalDirections().forEach {
                val newCoord = node.coord.resolve(it)
                if (isWall(newCoord))
                    return@forEach

                val newSteps = node.steps + 1
                if (newSteps < minStepsTarget && (!minSteps.containsKey(newCoord) || newSteps < minSteps.getInt(newCoord))) {
                    if (newCoord == target) {
                        minStepsTarget = newSteps
                    }
                    if (newSteps <= 50)
                        reachablePart2.add(newCoord)
                    minSteps.put(newCoord, newSteps)
                    queue.add(Node(newCoord, newSteps))
                }
            }
        }

        return Result.of(minStepsTarget, reachablePart2.size)
    }

    override fun parse() {
        this.designNumber = this.lines[0].toLong()
    }

    class Node(val coord: Coordinate, val steps: Int)

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day13().run()
        }
    }
}