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

import scala.concurrent.Future
import spray.routing.{RequestContext, AuthenticationRequiredRejection}
import spray.routing.authentication._
import spray.http.HttpHeaders._
import scala.reflect.{classTag, ClassTag}
import spray.http.{ HttpCredentials, BasicHttpCredentials}
import scala.concurrent.ExecutionContext.Implicits._

import com.gecemmo.raptor.core.ApiUser

/**
 * Implements custom digest based authentication
 * TODO: Implement together with Neo4j
 */
trait CustomAuthentication {
	import spray.util._

	private def scheme = "FS"

	private def authenticate(credentials: Option[HttpCredentials], ctx: RequestContext) = Future[Option[ApiUser]] {
		credentials match {
			case Some(creds) => {				
				println(ctx)				
				creds.value.split("""\s+""") match {
					case Array("Basic", digest) => println("Basic: " + digest)
					// Extract Api id and signature (=> correct /incorrect)
					// TODO: Add unit tests to test request signing
					case Array("FS", digest) => println("FS: " + digest)
					case _ => None
				}				
				Some(ApiUser(Some("test1"), Some("test1")))
			}
			case None => None
		}
	}

	val digestAuthenticator: ContextAuthenticator[ApiUser] = { ctx =>		
		val authHeader = ctx.request.headers.findByType[`Authorization`]
		val credentials = authHeader.map { case Authorization(creds) => creds }		
		authenticate(credentials, ctx) map {
			case Some(str:ApiUser) => { println("Got from auth service: " + str); Right(str) }
			case None => Left(AuthenticationRequiredRejection("FS", "wrong", Map.empty))
		}
	}
}