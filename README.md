se1by's ten.java submission
==============================

[![ten.java](https://cdn.mediacru.sh/hu4CJqRD7AiB.svg)](https://tenjava.com/)

This is a submission for the 2014 ten.java contest.

- __Theme:__ How can energy be harnessed and used in the Minecraft world?
- __Time:__ Time 2 (7/12/2014 09:00 to 7/12/2014 19:00 UTC)
- __MC Version:__ 1.7.9 (latest Bukkit beta)
- __Stream URL:__ http://www.twitch.tv/BitByter

<!-- put chosen theme above -->

---------------------------------------

Compilation
-----------

- Download & Install [Maven 3](http://maven.apache.org/download.html)
- Clone the repository: `git clone https://github.com/tenjava/se1by-t2`
- Compile and create the plugin package using Maven: `mvn`

Maven will download all required dependencies and build a ready-for-use plugin package!

---------------------------------------

Usage
-----

1. Install plugin
2. Add stuff in conversion.yml
3. Build a lightning rod! They should look like this:
[![image1](http://i.imgur.com/LNC3XRw.png)](http://i.imgur.com/LNC3XRw.png)
4. Connect it to a chest or a furnace with some redstone!
5. Wait for a thunder (or /toggledownfall ;) )
6. Enjoy your energy!

A little bit more information
-----------------------------

* The longer the cables (redstone) are, the less power they will supply (10 power at start, losing 1 power per cable)
* A connected furnace will gain burning power. A lot, if connected right (The equivalent of a lava bucket at power 10!)
* A connected chest will convert items listed in the conversion.yml
* Put spawn eggs in the chest and you just build your own, thunder powered spawner!


Things that don't work... yet!
------------------------------

* The battery device is still missing - sadly, as it adds so much more to it :/
* The redstone-finding algorithm needs some refactoring - it only finds connections on 1 level
* I'm sure there are some bugs I didn't see, feel free to open an issue!

<!-- Hi, se1by! This is the default README for every ten.java submission. -->
<!-- We encourage you to edit this README with some information about your submission – keep in mind you'll be scored on documentation! -->
