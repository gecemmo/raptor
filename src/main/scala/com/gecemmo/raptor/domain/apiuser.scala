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

package com.gecemmo.raptor.domain

/**
 * Base type for failures
 */
trait Failure {
  /**
   * The error code for the failure
   * @return the error code
   */
  def code: String
}

case class NotFoundFailure(status: Int, code: Int, message: String, developerMessage: Option[String])

case class ApiUserLoginAttempt(stype: String, svalue: String)
case class ApiUser(username: Option[String], password: Option[String])

/**
 * Reply to successful api user login
 */
case class RegisteredApiUser(username: String, password: String)

/**
 * Reply to unsuccessful api user login
 */
case class NotRegisteredApiUser(code: String) extends Failure
