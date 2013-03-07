/**
*                      _              
*    ____ _____ ____ _| |_ ___   ____ 
*   / ___|____ |  _ (_   _) _ \ / ___)
*  | |   / ___ | |_| || || |_| | |    
*  |_|   \_____|  __/  \__)___/|_|    
*              |_|                    
*   
* Raptor v.0.1
*
* Gecemmo Solutions AB
* (C) 2011-2013, Johan Astborg
*
* http://www.gecemmo.com
*/

package com.gecemmo.raptor.core

import akka.actor.{Props, ActorSystem}
import spray.io.IOExtension
import spray.can.server._
import spray.io.{SingletonHandler, IOBridge}
import com.typesafe.config.{ConfigFactory, Config}
import com.gecemmo.raptor.api._

/**
* Defines the trait for web service functionality
*/
trait Web {
  this: Api with ServerCore =>

  implicit def actorSystem: ActorSystem

  val ioBridge = IOExtension(actorSystem).ioBridge()
  val settings = ServerSettings(ConfigFactory.load)

  val httpServer = actorSystem.actorOf(
    Props(new HttpServer(ioBridge, SingletonHandler(rootService), settings)),
    name = "http-server"
  )

  httpServer.toString

  httpServer ! HttpServer.Bind("localhost", 9000)
}
