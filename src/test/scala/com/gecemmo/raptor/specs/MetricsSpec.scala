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

package com.gecemmo.specs

import org.specs2.mutable.Specification
import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}

import com.gecemmo.raptor.core.services.MetricsActor

/**
 * @author Johan Astborg
 */
class MetricsActorSpec extends TestKit(ActorSystem()) with Specification with ImplicitSender {

  val actor = TestActorRef(new MetricsActor())

  "Basic tenant operations" should {

    "Respond to test message" in {
      actor ! "hello"
      expectMsg("i got it, from metrics actor")
      success
    }
  }
}