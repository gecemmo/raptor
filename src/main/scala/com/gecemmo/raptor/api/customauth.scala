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
import spray.routing.{RequestContext, AuthenticationRequiredRejection, AuthenticationFailedRejection}
import spray.routing.authentication._
import spray.http.HttpHeaders._
import spray.http.HttpHeader
import scala.reflect.{classTag, ClassTag}
import spray.http.{ HttpCredentials, GenericHttpCredentials}
import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.{ExecutionContext, Promise}
import org.specs2.mutable._
import com.gecemmo.raptor.core.ApiUser

import spray.util.LoggingContext
import org.apache.log4j.Logger;
import javax.crypto
import org.apache.commons.codec.binary.Base64.encodeBase64

/**
* The BasicHttpAuthenticator implements HTTP Basic Auth.
*/
class RaptorHttpAuthenticator[U](val realm: String, val userPassAuthenticator: UserPassAuthenticator[U])
                               (implicit val executionContext: ExecutionContext)
  extends HttpAuthenticator[U] {

  def scheme:String = "RA"
  def params(ctx: RequestContext) = Map.empty

  def authenticate(credentials: Option[HttpCredentials], ctx: RequestContext) = {
    println(credentials)
    userPassAuthenticator {
      credentials.flatMap {
        case GenericHttpCredentials("RA", params) => Some(UserPass("user", "pass"))
        case _ => None
      }
    }
  }
}

trait CustomAuthentication {
  import spray.util._

  private def scheme = "RA"
  private def realm = "Secured Resource"
  private def SHA1 = "HmacSHA1"

  def lookupKey(id: String) = {
    Some("id")
  }

  def signMessage(msg: String, keyStr: String) = {    
    val mac = crypto.Mac.getInstance(SHA1)
    mac.init(new crypto.spec.SecretKeySpec(keyStr.getBytes, SHA1))
    Some(new String(encodeBase64(mac.doFinal(msg.getBytes))))
  }

  // Validates digest
  def validate(id: String, sign: String, headers: List[HttpHeader]): Option[ApiUser] = {
    
    val msg = "PUT\nc8fdb181845a4ca6b8fec737b3581d76\ntext/html\nThu, 17 Nov 2005 18:49:58 GMT\n/v1/auth"    
    for {
      key <- lookupKey(id)
      sig <- signMessage(msg, key)

    } yield ApiUser(Some(id), Some(key))

    if (id == "144JVZINOF5EBNCMG9J8VALID") {
          //Some(ApiUser(Some("id"), Some("key_str")))

          // Sign headers and compare
          // 1. VERB
          // 2. CONTENT-MD5
          // 3. CONTENT-TYPE
          // 4. DATE
          // 5. CanonicalizedResource

          println("nisse")

          val SHA1 = "HmacSHA1";
          val key_str = "OtxrzxIsfpFjA7SwPzILwy8Bw21TLhquhboDYROV"
          val key = new crypto.spec.SecretKeySpec(key_str.getBytes, SHA1)
          val message = "PUT\nc8fdb181845a4ca6b8fec737b3581d76\ntext/html\nThu, 17 Nov 2005 18:49:58 GMT\n/v1/auth"

          val sig = {
            val mac = crypto.Mac.getInstance(SHA1)
            mac.init(key)
            new String(encodeBase64(mac.doFinal(message.getBytes)))
          }
          
          // Check if signing is correct
          if (sign == sig) Some(ApiUser(Some(id), Some(key_str)))
          else None

      } else None
    /*
    digest.split("!") match {
      case Array(id, signature) => {

        // Lookup key for id, if id is invalid return None

        if (id == "144JVZINOF5EBNCMG9J8VALID") {

          // Sign headers and compare
          // 1. VERB
          // 2. CONTENT-MD5
          // 3. CONTENT-TYPE
          // 4. DATE
          // 5. CanonicalizedResource

          println("nisse")

          val SHA1 = "HmacSHA1";
          val key_str = "OtxrzxIsfpFjA7SwPzILwy8Bw21TLhquhboDYROV"
          val key = new crypto.spec.SecretKeySpec(key_str.getBytes, SHA1)
          val message = "PUT\nc8fdb181845a4ca6b8fec737b3581d76\ntext/html\nThu, 17 Nov 2005 18:49:58 GMT\n/v1/auth"

          val sig = {
            val mac = crypto.Mac.getInstance(SHA1)
            mac.init(key)
            new String(encodeBase64(mac.doFinal(message.getBytes)))
          }
          
          // Check if signing is correct
          if (signature == sig) Some(ApiUser(Some(id), Some(key_str)))
          else None
        }
        else None
      }
      case _ => None
    }*/
  }

  class monkey[U]()(implicit ec: ExecutionContext, log: LoggingContext) extends UserPassAuthenticator[String] {
    def apply(userPassOption: Option[UserPass]) = Future(Some("dsadsa"))
  }

  def httpBasic[U](realm: String = "Secured Resource",
    authenticator: UserPassAuthenticator[U] = new monkey())
    : RaptorHttpAuthenticator[U] =
  new RaptorHttpAuthenticator[U](realm, authenticator)

  def authenticate(credentials: Option[HttpCredentials], ctx: RequestContext): Future[Option[ApiUser]] = {
      println(credentials)
      credentials match {
      case Some(creds) => {        
        creds.value.split("""\s+""") match {
          // TODO: Add unit tests to test request signing
          case Array("RA", digest) => Future(
            digest.split(":") match {case Array(id, sign) => validate(id, sign, ctx.request.headers)}
            //validate(digest, ctx.request.headers)
          )
          // Reject all other authentication schemes
          case _ => Future(None)
        }
      }
      case _ => Future(None)
    }
  }

  val digestAuthenticator: ContextAuthenticator[ApiUser] = { ctx =>
    val authHeader = ctx.request.headers.findByType[`Authorization`]
    println(authHeader)
    val credentials = authHeader.map { case Authorization(creds) => creds }
    authenticate(credentials, ctx) map {
      case Some(userContext) => Right(userContext)
      case None => Left {
        if (authHeader.isEmpty) AuthenticationRequiredRejection(scheme, realm, Map.empty)
        else AuthenticationFailedRejection(realm)
      }
    }
    /*
    authenticate(credentials, ctx) map {
      case Some(user) => Future(
          Right {
            user
          }
        )
      case _ => Future(
        Left {
          if (authHeader.isEmpty) AuthenticationRequiredRejection("RA", "insert realm", Map.empty)
          else AuthenticationFailedRejection("Invalid authentication")
        }
      )
    }*/
  }
}