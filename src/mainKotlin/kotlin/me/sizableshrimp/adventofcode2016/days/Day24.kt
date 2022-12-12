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

import it.unimi.dsi.fastutil.objects.Object2IntMap
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import me.sizableshrimp.adventofcode2016.helper.GridHelper
import me.sizableshrimp.adventofcode2016.templates.Coordinate
import me.sizableshrimp.adventofcode2016.templates.Day
import me.sizableshrimp.adventofcode2016.templates.Direction
import java.util.PriorityQueue
import kotlin.math.max

class Day24 : Day() {
    private lateinit var pois: Object2IntMap<Coordinate>
    private lateinit var start: Coordinate
    private lateinit var grid: Array<BooleanArray>

    override fun evaluate(): Result {
        val steps = Object2IntOpenHashMap<Node>()
        val queue = PriorityQueue<Node>(Comparator.comparingInt(steps::getInt))
        queue.add(Node(this.start, 0))
        val targetVisited = (1 shl this.pois.size) - 1
        var minStepsPart1 = Int.MAX_VALUE
        var minStepsPart2 = Int.MAX_VALUE

        while (!queue.isEmpty()) {
            val node = queue.remove()
            val newSteps = steps.getInt(node) + 1
            if (newSteps >= max(minStepsPart1, minStepsPart2))
                continue

            for (dir in Direction.cardinalDirections()) {
                val newPosX = node.pos.x + dir.x
                val newPosY = node.pos.y + dir.y
                if (!GridHelper.isValid(this.grid, newPosX, newPosY) || this.grid[newPosY][newPosX]) continue

                val newPos = Coordinate.of(newPosX, newPosY)
                val visited = if (this.pois.contains(newPos)) node.visited or (1 shl this.pois.getInt(newPos)) else node.visited
                if (targetVisited == visited) {
                    if (newSteps < minStepsPart1)
                        minStepsPart1 = newSteps
                    if (newPos == this.start && newSteps < minStepsPart2) {
                        minStepsPart2 = newSteps
                        continue
                    }
                }

                val newNode = Node(newPos, visited)
                if (!steps.containsKey(newNode) || steps.getInt(newNode) > newSteps) {
                    steps.put(newNode, newSteps)
                    queue.add(newNode)
                }
            }
        }

        return Result.of(minStepsPart1, minStepsPart2)
    }

    override fun parse() {
        this.pois = Object2IntOpenHashMap()
        this.grid = GridHelper.convertBool(this.lines) { it == '#' }

        for ((y, line) in this.lines.withIndex()) {
            for ((x, c) in line.withIndex()) {
                if (c == '0') {
                    this.start = Coordinate.of(x, y)
                } else if (c.isDigit()) {
                    // Exclude 0 as that is our start pos
                    this.pois.put(Coordinate.of(x, y), c.code - '1'.code)
                }
            }
        }
    }

    data class Node(val pos: Coordinate, val visited: Int)

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day24().run()
        }
    }
}