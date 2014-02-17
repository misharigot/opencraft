Glowstone
==========

Introduction
------------

Glowstone is a lightweight, from scratch, open source
[Minecraft](http://minecraft.net) server written in Java that supports plugins
written for the [Bukkit](http://bukkit.org) API.

The main goals of the project are to provide a lightweight implementation
of the Bukkit API and Minecraft server where exact vanilla functionality is
not needed or higher performance is desired than the official software can
deliver. Glowstone makes use of a thread-per-world model and performs
synchronization only when necessitated by the Bukkit API.

Building
--------

Glowstone can be built with the
[Java Development Kit](http://oracle.com/technetwork/java/javase/downloads) and
[Gradle](http://gradle.org). Gradle is also used for dependency
management.

The command `gradle` will build Glowstone and will put the compiled JAR in 
`~/build/distributions`, and `gradle install` will copy it to your local Maven 
repository. Additionally, if you prefer not to install Gradle you can simply 
use the provided `gradlew` or `gradlew.bat` scripts instead of `gradle`.

Running
-------

Running Glowstone is simple because its dependencies are shaded into the output
jar at compile time. Simply execute `java -jar glowstone.jar` along with any
extra JVM options desired.

By default, configuration is stored in the `config/` subdirectory and logs
are stored in the `logs/` subdirectory. The main configuration file is
`config/glowstone.yml`, which replaces CraftBukkit's `server.properties` and
`bukkit.yml`. Settings from these two files will be copied over to Glowstone's
configuration during the default configuration generation process.

Glowstone uses [JLine](http://jline.sf.net) for console input and colored
console output. The JLine console can be disabled in the configuration if a
flat console is desired.

Documentation
-------------

Javadocs can be generated by using the `gradle javadoc` command in the
terminal. To view the javadocs simply go to `~/build/docs/javadoc/` 
and open `index.html` in a web browser.

For documentation on the Bukkit API, see the
[Bukkit Javadocs](http://jd.bukkit.org/).

Credits
-------

 * [The Minecraft Coalition](http://wiki.vg/wiki) - protocol and file formats
   research.
 * [Trustin Lee](http://gleamynode.net) - author of the
   [Netty](http://jboss.org/netty) library.
 * Graham Edgecombe - author of the original
   [Lightstone](https://github.com/grahamedgecombe/lightstone) - and everyone
   else who has contributed to Lightstone.
 * All the people behind [Gradle](http://gradle.org) and
   [Java](http://java.oracle.com).
 * The [Bukkit](http://bukkit.org) team for their outstandingly well-designed
   plugin API.
 * [Notch](http://mojang.com/notch) and all the other people at
   [Mojang](http://mojang.com) - for making such an awesome game in the first
   place!

Copyright
---------

Glowstone is open-source software released under the MIT license. Please see
the `LICENSE` file for details.
