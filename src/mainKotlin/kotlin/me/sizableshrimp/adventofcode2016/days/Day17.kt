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

import com.google.common.hash.Hashing
import me.sizableshrimp.adventofcode2016.templates.Coordinate
import me.sizableshrimp.adventofcode2016.templates.Day
import me.sizableshrimp.adventofcode2016.templates.Direction
import java.util.EnumSet
import java.util.PriorityQueue

class Day17 : Day() {
    override fun evaluate(): Result {
        val target = Coordinate.of(3, 3)
        val queue = PriorityQueue<Node>(Comparator.comparingInt {
            it.coord.distance(target)
        })
        queue.add(Node(Coordinate.ORIGIN, getOpenDoors(this.lines[0], Coordinate.ORIGIN), 0, this.lines[0]))
        var targetMinSteps = Int.MAX_VALUE
        var targetMaxSteps = Int.MIN_VALUE
        var targetMinHashKey: String? = null

        while (!queue.isEmpty()) {
            val node = queue.remove()

            for (dir in node.openDoors) {
                val newCoord = node.coord.resolve(dir)
                val newHashKey = node.hashKey + dir.charURDL
                val newSteps = node.steps + 1
                if (newCoord == target) {
                    if (newSteps < targetMinSteps) {
                        targetMinSteps = newSteps
                        targetMinHashKey = newHashKey.substring(this.lines[0].length)
                    }
                    if (newSteps > targetMaxSteps) targetMaxSteps = newSteps
                    continue
                }
                val newOpenDoors = getOpenDoors(newHashKey, newCoord)
                if (newOpenDoors.isEmpty()) continue

                queue.add(Node(newCoord, newOpenDoors, newSteps, newHashKey))
            }
        }

        return Result.of(targetMinHashKey, targetMaxSteps)
    }

    private fun getOpenDoors(hashKey: String, coord: Coordinate): Set<Direction> {
        val openDoors = EnumSet.noneOf(Direction::class.java)

        @Suppress("DEPRECATION")
        val hash = Hashing.md5().hashString(hashKey, Charsets.UTF_8).toString()
        for (i in 0 until 4) {
            val dir = DIRECTION_ORDER[i]
            val newCoord = coord.resolve(dir)
            if (newCoord.x < 0 || newCoord.y < 0 || newCoord.x >= 4 || newCoord.y >= 4)
                continue
            val c = hash[i].code
            if (c >= 'b'.code && c <= 'f'.code) {
                openDoors.add(dir)
            }
        }

        return openDoors
    }

    data class Node(val coord: Coordinate, val openDoors: Set<Direction>, val steps: Int, val hashKey: String)

    companion object {
        private val DIRECTION_ORDER = arrayOf(Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST)

        @JvmStatic
        fun main(args: Array<String>) {
            Day17().run()
        }
    }
}