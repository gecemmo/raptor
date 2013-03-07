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

package com.gecemmo.raptor.api

import akka.actor.{Props, Actor, ActorSystem}
import akka.pattern.ask
import spray.routing._
import scala.concurrent.ExecutionContext.Implicits._
import spray.routing.authentication._
import spray.httpx.marshalling;
import com.gecemmo.raptor.core._

/**
 * Custom matcher for generated IDs
 */
object HashMatcher {
  def regexp = """[\da-zA-Z]{12}""".r
}

/**
 * Tenant routes
 */
class TenantService(implicit val actorSystem: ActorSystem) extends Directives with DefaultTimeout {

  def tenantActor = actorSystem.actorFor("/user/application/tenant")
  def userActor = actorSystem.actorFor("/user/application/user")

  val route = pathPrefix("v1") {
    path("tenants" / HashMatcher.regexp) { id =>
      get {
        complete {
          (tenantActor ? GetTenantByApiKey(id)).mapTo[String]
        }
      }
    }
  }
}

/**
 * Raptor routes
 * Build in functionality for raptor
 */
class RaptorService(implicit val actorSystem: ActorSystem) extends Directives with CustomAuthentication with DefaultTimeout {

  import CustomMarshallers._

  val route = pathPrefix("v1") {
    path(Slash) {
      get {
        complete {
          "Raptor API v.0.1"
        }
      }
    } ~
    path ("auth") {
       authenticate(BasicAuth(realm = "admin area")) { user =>
        get {
          _.complete("dsa")
        }
      }
    } ~
    path ("doc") {
      get {
        complete {
          "Raptor API Documentation"
        }
      }
    }
  }
}
