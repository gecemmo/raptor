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
import org.specs2.specification.{Fragments, Step}
import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import spray.routing.HttpService
import spray.http.StatusCodes._
import spray.http.HttpHeaders._
import spray.http.HttpHeader
import spray.http.{HttpCredentials, BasicHttpCredentials}
import spray.routing.{AuthenticationFailedRejection, AuthenticationRequiredRejection}

import com.gecemmo.raptor.api.RaptorService

class CustomAuthenticationSpec extends Specification with Specs2RouteTest with HttpService {
	def actorRefFactory = system // connect the DSL to the test ActorSystem

	val raptorService = new RaptorService()
	
	// Mock header to mimic a wrong raptor auth signature
	case class RaptorAuthHeader() extends HttpHeader {
    def name = "Authorization"
    def lowercaseName = "authorization"
    def value = "RA invalid:ddsadsa"
  }

	"the custom authentication" should {
		"reject requests without the proper Authentication header" in {
			Get("/v1/auth") ~> raptorService.route ~> check {
				handled must beFalse

				rejection must beAnInstanceOf[AuthenticationRequiredRejection]
			}
		}
		"reject requests with a Basic Authentication header" in {
		  Get("/v1/auth") ~> addHeader(Authorization(BasicHttpCredentials("user", "password"))) ~> 
		                                    raptorService.route ~> 
		                                    check {
		    handled must beFalse
		    
		    rejection must beAnInstanceOf[AuthenticationFailedRejection]
		  }
		}
		"reject requests with Raptor Authentication header and wrong signature" in {
			Get("/v1/auth") ~> addHeader(RaptorAuthHeader()) ~>
																			raptorService.route ~>
																			check {
					handled must beFalse

					rejection must beAnInstanceOf[AuthenticationRequiredRejection]
			}
		}
	}
}