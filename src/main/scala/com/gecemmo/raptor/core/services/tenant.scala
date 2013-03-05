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

package com.gecemmo.raptor.core.services

import akka.actor.{Props, Actor, ActorSystem}
import akka.pattern.ask
import akka.util.Timeout

import com.gecemmo.raptor.core.{DefaultTimeout, GetTenantByApiKey}

/**
* Specifies all tenant operations.
*/
trait TenantOperations {

  def GetTenantByApiKey() = "i will send over a valid response in the future"
}

/**
* Service actor for tenant operations.
*/
class TenantActor() extends Actor with DefaultTimeout with TenantOperations {

  def receive = {

    case "hello" =>
      sender ! "i got it, from tenant actor"

    case GetTenantByApiKey(key) =>
      sender ! GetTenantByApiKey()
  }
}