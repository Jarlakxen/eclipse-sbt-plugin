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

##Release Note

###Coming Soon
+ Supports for SBT 0.11, 0.12 and 0.13
+ Automatic reload after change build.sbt
+ Add Web Project nature
+ Move large operations to jobs
+ Bug fixing

###TODO
+ Import sbt project
+ Automatic reload after changes in /project
+ Add build.sbt outline