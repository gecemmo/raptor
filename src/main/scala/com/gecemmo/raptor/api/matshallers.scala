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

import spray.json._
import spray.http.HttpBody
import spray.httpx.marshalling._
import spray.httpx.unmarshalling._
import spray.http.MediaTypes._
import scala.concurrent.ExecutionContext.Implicits._

import com.gecemmo.raptor.core._
import com.gecemmo.raptor.domain._

import CustomJsonProtocol._

/**
 * Custom marshallers for JSON
 */
object CustomMarshallers {
  
  implicit val UserLoginAttemptMarshaller =
    Marshaller.of[ApiUserLoginAttempt](`application/json`) { (value, contentType, ctx) =>
      ctx.marshalTo(HttpBody(contentType, value.toJson.prettyPrint))
    }
  
  implicit val UserLoginAttemptUnmarshaller =    
    Unmarshaller[ApiUserLoginAttempt](`application/json`) {
      case HttpBody(contentType, buffer) =>
        (new String(buffer)).asJson.convertTo[ApiUserLoginAttempt]
    }
      
  implicit val NoSuchEventFailureMarshaller =
    Marshaller.of[NotFoundFailure](`application/json`) { (value, contentType, ctx) =>
      ctx.marshalTo(HttpBody(contentType, value.toJson.prettyPrint))
    }
  
  implicit val NoSuchEventFailureUnmarshaller =
    Unmarshaller[NotFoundFailure](`application/json`) {
      case HttpBody(contentType, buffer) =>
        (new String(buffer)).asJson.convertTo[NotFoundFailure]
    }

  implicit val ApiUserMarshaller = 
    Marshaller.of[ApiUser](`application/json`) { (value, contentType, ctx) =>
      ctx.marshalTo(HttpBody(contentType, value.toJson.prettyPrint))
    }

  implicit val ApiUserUnmarshaller = 
    Unmarshaller[ApiUser](`application/json`) {
      case HttpBody(contentType, buffer) =>
        (new String(buffer)).asJson.convertTo[ApiUser]
    }
}
