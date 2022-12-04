# AdventOfCode2016
This repository holds my solutions and helper code (in Java/Kotlin) for [Advent Of Code 2016](https://adventofcode.com/2016).
Usually, what I push to GitHub is fully formed and semi-optimized.

### Running A Specific Day
This program runs a specific day based on a few factors inside the `Main` class.
If it is the month of December in EST time and the 25th or earlier, whatever current day it is in EST time will be selected to run.
If this program is not run during Advent season, it will run ALL days by default.
An output from a day looks like the following:
```text
Day 1:
Part 1: 123
Part 2: 456
Completed in 5.298ms
```
Note that "Completed in X.XXXms" is not a real or accurate benchmark, but my own approximate measure of how long a day takes to run.
For real benchmarks, please see `gradle jmh` and the `benchmarks` package.
If you would like to run a specific day outside the month of the December, use `run(int)` in the `Main` class.

### Information About Data Manager
This program can *optionally* read input data for a specified day **using the Advent Of Code servers**.
To enable this feature, you must include a `session.txt` file in the working directory.
This file should hold your session cookie from the Advent Of Code website, which can be found with browser inspection.
This cookie expires after a month which means the `session.txt` file needs to be updated from time to time.

#### Retrieving Input File
If the input file is successfully retrieved from the servers, this data is cached in a text file relative to your run directory in the `aoc_input` directory.
Fetching data from the Advent Of Code servers is **only** used if a file with the input data for a specified day cannot be found to reduce load on the servers.
See the documentation on `DataManager#read` for more detail.
