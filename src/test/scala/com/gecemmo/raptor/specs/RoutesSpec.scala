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

import org.specs2.mutable._
import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import spray.routing.HttpService

import com.gecemmo.raptor.api.RaptorService

class CustomAuthenticationSpec extends Specification with Specs2RouteTest with HttpService {
	def actorRefFactory = system // connect the DSL to the test ActorSystem

	val raptorService = new RaptorService()

	"the Raptor service" should {
		"return a `Raptor API v.0.1` for GET requests to the `/v1` path" in {
			Get("/v1") ~> raptorService.route ~> check {
				entityAs[String] must contain("Raptor API v.0.1")
			}
		}
	}
}