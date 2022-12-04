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

package me.sizableshrimp.adventofcode2016;

import me.sizableshrimp.adventofcode2016.helper.DataManager;
import me.sizableshrimp.adventofcode2016.templates.Day;

import java.lang.reflect.Constructor;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
    private static final String BASE_PACKAGE = Main.class.getPackageName() + ".days.";

    /**
     * Runs the AOC challenges.
     * <p>
     * If "wait" is supplied as an argument, then the main thread will wait until the next day unlocks. This includes waiting until
     * the upcoming challenge opens and downloading the input file automatically shortly after the unlock time.
     * After downloading the input file, the process ends.
     * <p>
     * If the first argument supplied is "all", then this code will run all existing days and exit.
     * <p>
     * If the first argument supplied is a valid number 1 through 25, inclusive, then that day will be run.
     * <p>
     * If it is the month of December and {@link AOCUtil#YEAR} is equal to the current year, then the current day's code is run.
     * Otherwise, runs all existing days.
     *
     * @param args Used to modify what is executed.
     */
    public static void main(String[] args) {
        List<String> list = Arrays.asList(args);
        if (list.contains("wait")) {
            waitForDay();
            return;
        } else if (list.contains("all")) {
            runAll();
            return;
        } else if (!list.isEmpty()) {
            try {
                int day = Integer.parseInt(list.get(0));
                if (day >= 1 && day <= 25) {
                    run(day);
                    return;
                }
            } catch (NumberFormatException ignored) {
            }
        }

        LocalDateTime time = LocalDateTime.now(AOCUtil.EST_ZONE_ID);
        int dayOfMonth = time.getDayOfMonth();
        if (time.getMonth() == Month.DECEMBER && dayOfMonth <= 25) {
            run(dayOfMonth);
        } else {
            runAll();
        }
    }

    private static void waitForDay() {
        LocalDateTime now = LocalDateTime.now(AOCUtil.EST_ZONE_ID);
        LocalDateTime release = now.plus(Duration.ofDays(1)).withNano(0).withSecond(0).withMinute(0).withHour(0);
        int dayOfMonth = release.getDayOfMonth();
        if (AOCUtil.YEAR != release.getYear())
            throw new IllegalArgumentException("The year is not " + AOCUtil.YEAR + "!");
        if (release.getMonth() != Month.DECEMBER || dayOfMonth > 25)
            throw new IllegalArgumentException("Cannot use \"wait\" if the upcoming day is not during AOC!");

        System.out.println("Releases at " + release.format(FORMATTER));

        try {
            boolean doneThisMinute = false;
            while (true) {
                now = LocalDateTime.now(AOCUtil.EST_ZONE_ID);
                if (now.getMinute() % 5 == 0) {
                    if (!doneThisMinute) {
                        System.out.println(now.format(FORMATTER));
                        doneThisMinute = true;
                    }
                } else {
                    doneThisMinute = false;
                }
                if (now.isAfter(release)) {
                    // int rand = ThreadLocalRandom.current().nextInt(10, 31);
                    // Thread.sleep(rand * 1000L);
                    Thread.sleep(5_000L);
                    DataManager.read(dayOfMonth);
                    System.out.println("Downloaded input file");
                    break;
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Runs a specific {@link Day} and prints out the results.
     *
     * @param day The advent day of the month between 1 and 25, inclusive.
     */
    public static void run(int day) {
        if (day < 1 || day > 25)
            throw new IllegalArgumentException("The day cannot be less than 1 or greater than 25!");
        try {
            System.out.println("Day " + day + ":");
            getDayConstructor(day).newInstance().run();
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Day " + day + " does not exist.", e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static Constructor<Day> getDayConstructor(int day) throws ClassNotFoundException, NoSuchMethodException {
        Class<?> clazz = Class.forName(BASE_PACKAGE + "Day" + AOCUtil.padDay(day));
        if (Day.class.isAssignableFrom(clazz)) {
            Class<Day> dayClazz = (Class<Day>) clazz;
            return dayClazz.getDeclaredConstructor();
        } else {
            throw new IllegalArgumentException("Detected day class does not extend Day");
        }
    }

    public static Constructor<Day> getCurrentDayConstructor() throws ClassNotFoundException, NoSuchMethodException {
        LocalDateTime time = LocalDateTime.now(AOCUtil.EST_ZONE_ID);
        int dayOfMonth = time.getDayOfMonth();

        if (time.getMonth() == Month.DECEMBER && dayOfMonth <= 25) {
            return getDayConstructor(dayOfMonth);
        }

        throw new IllegalStateException("Cannot get current day if it is not during AOC!");
    }

    /**
     * Runs and prints all days existing up to and including 25 and provides an estimate of the runtime.
     */
    public static void runAll() {
        List<Day> days = new ArrayList<>(25);
        for (int i = 0; i < 25; i++) {
            try {
                days.add(getDayConstructor(i).newInstance());
            } catch (ClassNotFoundException ignored) {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        List<Day.TimedResult> results = new ArrayList<>(25);
        long before = System.nanoTime();
        for (Day value : days) {
            // Have to print separately to not skew runtimes
            results.add(value.runTimed());
        }
        long after = System.nanoTime();

        System.out.println("All Days\n");
        for (int i = 0; i < results.size(); i++) {
            int day = i + 1;
            Day.TimedResult result = results.get(i);
            System.out.println("Day " + day + ":");
            System.out.println("Part 1: " + result.part1());
            System.out.println("Part 2: " + result.part2());
            System.out.printf("Completed in %.3fms%n%n", result.timeTaken() / 1_000_000f);
        }
        System.out.printf("Completed all days in %.3fms%n%n", (after - before) / 1_000_000f);
    }
}
