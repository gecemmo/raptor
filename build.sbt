organization := "com.gecemmo"

version := "0.1"

scalaVersion := "2.10.0"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io/",
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases/",
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
)

libraryDependencies ++= Seq(
  "io.spray"             % "spray-can"        		% "1.1-M7",
  "io.spray"             % "spray-routing"    		% "1.1-M7",
  "io.spray"             % "spray-httpx"      		% "1.1-M7",
  "io.spray"             % "spray-util"       		% "1.1-M7",
  "io.spray"             % "spray-client"     		% "1.1-M7",
  "io.spray"             % "spray-json_2.10"  		% "1.2.3",
  "io.spray"             % "spray-testkit"    		% "1.1-M7",
  "com.typesafe.akka"    % "akka-actor_2.10"  		% "2.1.0",
  "com.typesafe.akka"    % "akka-testkit_2.10"  	% "2.1.0",
  "com.typesafe.akka"    % "akka-remote_2.10"     	% "2.1.0",
  "org.mongodb" 	 % "casbah_2.10" 	  	% "2.5.0",
  "org.scalatest"        % "scalatest_2.10"  		% "2.0.M6-SNAP8",
  "org.clapper" 	 % "grizzled-slf4j_2.10" 	% "1.0.1",
  "org.slf4j" 		 % "slf4j-simple" 		% "1.6.4",
  "me.prettyprint" 	 % "hector-core" 		% "1.0-5",
  "com.espertech"        % "esper"                	% "4.8.0"
)

parallelExecution in Test := false
