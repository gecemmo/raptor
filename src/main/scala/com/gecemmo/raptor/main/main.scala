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

package com.gecemmo.raptor.main

import akka.actor.ActorSystem
import spray.can.server.SprayCanHttpServerApp

import com.gecemmo.raptor.api._
import com.gecemmo.raptor.core._

class Application(val actorSystem: ActorSystem) extends ServerCore with Api with Web 

/**
*	Main application
*/
object MainApp extends SprayCanHttpServerApp {
  
  def startApplication() {

    implicit val system = ActorSystem("raptor")

    new Application(system)
  }

  def main(args: Array[String]) =
    startApplication()
}
