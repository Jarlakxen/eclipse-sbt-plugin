Welcome to eclipse-sbt-plugin [![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/Jarlakxen/eclipse-sbt-plugin/trend.png)](https://bitdeli.com/free "Bitdeli Badge")
==================
eclipse-sbt-plugin provides a small Eclipse plug-ins for SBT. It supplements [ScalaIDE for Eclipse](http://scala-ide.org/).

This project is a fork of [takezoe's eclipse-scala-tools](https://bitbucket.org/takezoe/eclipse-scala-tools/)

##Instalation
Download a plug-in JAR file from the Download page and put it into ECLIPSE_HOME/plugins.

> Note for eclipse-sbt-plugin requires a latest version of [ScalaIDE](http://scala-ide.org/) for Eclipse 3.x.

##Features

###SBT Support
eclipse-sbt-plugin supports all of SBT 0.11, 0.12 and 0.13.

You can create a SBT project using a wizard. Choose File > New > Project from the main menu of Eclipse and choose Scala > SBT Project in the new project creation wizard.

[![Create Wizard I](http://i.imgur.com/NW6z9MP.png)](https://github.com/Jarlakxen/eclipse-sbt-plugin)

[![Create Wizard II](http://i.imgur.com/z1J9HbX.png)](https://github.com/Jarlakxen/eclipse-sbt-plugin)

##Release Note

###1.0.0
+ Supports for SBT 0.11, 0.12 and 0.13
+ Automatic reload after change in build.sbt or /project
+ Configurable Scala version
+ Add Web Project nature
+ Add Spec2 library on creation
+ Move large operations to background jobs
+ Bug fixing

###TODO
+ Import sbt project
+ Improve outline
+ A lot of things ;)