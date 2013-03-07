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

import spray.json.DefaultJsonProtocol
import spray.httpx.SprayJsonSupport
import spray.json._

/**
 * Custom protocol specifiers for Raptor domain objects
 */
object CustomJsonProtocol extends DefaultJsonProtocol {
 
  implicit val apiUserFormat = jsonFormat2(ApiUser)
  implicit val tenantFormat = jsonFormat2(Tenant)  
  implicit val applicationFormat = jsonFormat3(Application)  
  
  implicit val failureFormat = jsonFormat4(NotFoundFailure)

  implicit val failureResponseFormat = jsonFormat2(FailureResponse)
  
  // Custom converter due do mismatches in JSON and class names (ctype/type etc)
  implicit object ApiUserLoginAttemptFormat extends RootJsonFormat[ApiUserLoginAttempt] {
    def write(c: ApiUserLoginAttempt) =
      JsObject(("type", JsString(c.stype)), ("value", JsString(c.svalue)))
    
    def read(value: JsValue) = {
      value.asJsObject.getFields("type", "value") match {
       case Seq(JsString(stype), JsString(svalue)) =>
         new ApiUserLoginAttempt(stype, svalue)
       case _ => deserializationError("ApiUserLoginAttempt expected")
      }
    }
  }
}
