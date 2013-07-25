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
				complete{
					(tenantActor ? GetTenantByApiKey(id)).mapTo[String]
				}
			}
		}
	}
}

/**
 * Book routes
 */
class BookService(implicit val actorSystem: ActorSystem) extends Directives with DefaultTimeout {

	import CustomMarshallers._

	def bookActor = actorSystem.actorFor("/user/application/book")	

	val route = pathPrefix("v1") {
		path("books") {
			get {
				complete{
					(bookActor ? GetBooks()).mapTo[String]
				}
			}
		} ~
		path("books" / HashMatcher.regexp) { id =>
			get {
				complete{
					(bookActor ? GetBooksById(id)).mapTo[ApiUser]
				}
			}
		}
	}
}

/**
 * Raptor routes
 * Build in functionality for raptor
 */
class RaptorService(implicit val actorSystem: ActorSystem) extends Directives with CustomAuthentication {

	import CustomMarshallers._
	
	val route = {
		path(Slash) {
			get {
				/* Raptor home - Start page for raptor, docs, services etc. */
				/*
				complete {
					"<h1>Raptor</h1>" +
					"<hr>Raptor v. 0.1 - Gecemmo Solutions AB"
				}*/
				getFromFile("/home/gecemmo/Documents/raptor/src/main/resources/index.html")
			}
		} ~
		path ("status") {
			get {
				// Eg. only return: Raptor lives! + timestamp
				complete {
					"<h1>Raptor</h1>" +
					"<hr>Raptor v. 0.1 - Gecemmo Solutions AB"
				}
			}
		} ~
		path ("auth") {
			authenticate(digestAuthenticator) { user =>
				get {
					_.complete(user)
				}
			}
		} ~
		path ("doc") {
			get {
				complete {
					"docs"
				}
			}
		}
	}
}
