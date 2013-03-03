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

import spray.http.{HttpEntity, StatusCode}
import com.gecemmo.raptor.domain.NotFoundFailure

case class ErrorResponseException(responseStatus: StatusCode, message: NotFoundFailure) extends RuntimeException

//case class ErrorResponseException(responseStatus: StatusCode, message: String) extends RuntimeException
