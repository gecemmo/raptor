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

import java.security.MessageDigest

/**
* Utils for unique id's, dates and formatting
*/
object Utils {

  def sha1SumString(bytes : Array[Byte]) : String = {
    val sha1 = MessageDigest.getInstance("SHA1")
    sha1.reset()
    sha1.update(bytes)
    sha1.digest().map(0xFF & _).map { "%02x".format(_) }.foldLeft(""){_ + _}
  }

  // Generate UID with standard raptor length  
  def genUID() : String = {
    val bytes = (new java.util.Date()).getTime().toString().getBytes
    sha1SumString(bytes).substring(2, 14)
  }

  // Generate UID with specified length
  def genUID(length: Int) : String = {
    val bytes = (new java.util.Date()).getTime().toString().getBytes
    sha1SumString(bytes).substring(0, Math.min(length, 32))
  }
}
