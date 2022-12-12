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

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import me.sizableshrimp.adventofcode2016.helper.GridHelper
import me.sizableshrimp.adventofcode2016.templates.Coordinate
import me.sizableshrimp.adventofcode2016.templates.Day
import me.sizableshrimp.adventofcode2016.templates.Direction
import java.util.PriorityQueue

class Day22 : Day() {
    private lateinit var serverNodes: Map<Coordinate, ServerNode>
    private var highestX = 0
    private var highestY = 0

    override fun evaluate(): Result {
        var viable = 0

        for (a in this.serverNodes.values) {
            for (b in this.serverNodes.values) {
                if (a === b) continue
                if (a.used > 0 && a.used <= b.available) {
                    viable++
                }
            }
        }

        val steps = Object2IntOpenHashMap<Node>()
        val queue = PriorityQueue<Node>(Comparator.comparingInt(steps::getInt))
        queue.add(Node(Coordinate.of(this.highestX, 0), this.serverNodes.entries.find { it.value.used == 0 }!!.key))
        var minSteps = Int.MAX_VALUE
        val minSize = this.serverNodes.values.minBy { it.size }.size

        while (!queue.isEmpty()) {
            val node = queue.remove()
            val newSteps = steps.getInt(node) + 1
            if (newSteps >= minSteps)
                continue

            for (dir in Direction.cardinalDirections()) {
                val newPos = node.zeroPos.resolve(dir)
                if (!GridHelper.isValid(newPos.x, newPos.y, this.highestX + 1, this.highestY + 1)) continue

                if (this.serverNodes[node.zeroPos]!!.used <= minSize) {
                    if (node.zeroPos == Coordinate.ORIGIN && node.goalPos == newPos) {
                        minSteps = newSteps
                        break
                    }
                    val newNode = Node(if (newPos == node.goalPos) node.zeroPos else node.goalPos, newPos)
                    if (!steps.containsKey(newNode) || steps.getInt(newNode) > newSteps) {
                        steps.put(newNode, newSteps)
                        queue.add(newNode)
                    }
                }
            }
        }

        return Result.of(viable, minSteps)
    }

    override fun parse() {
        val serverNodes = HashMap<Coordinate, ServerNode>()
        this.serverNodes = serverNodes
        this.highestX = 0
        this.highestY = 0

        for (i in 2 until this.lines.size) {
            val line = this.lines[i]
            val split = line.split(" ").filterNot(String::isEmpty)
            val dir = split[0]
            val yIdx = dir.indexOf('y')
            val x = dir.substring(16, yIdx - 1).toInt()
            val y = dir.substring(yIdx + 1).toInt()
            val coord = Coordinate.of(x, y)

            if (x > this.highestX)
                this.highestX = x
            if (y > this.highestY)
                this.highestY = y

            serverNodes[coord] = ServerNode(split[1].dropLast(1).toInt(), split[2].dropLast(1).toInt(), split[3].dropLast(1).toInt())
        }
    }

    data class ServerNode(val size: Int, val used: Int, val available: Int)

    data class Node(val goalPos: Coordinate, val zeroPos: Coordinate)

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day22().run()
        }
    }
}