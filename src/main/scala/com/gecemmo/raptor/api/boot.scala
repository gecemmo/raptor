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

import akka.actor.{ActorRef, Props, Actor, ActorSystem}
import akka.util.Timeout
import akka.pattern.ask
import scala.util.control.NonFatal
import spray.routing._
import spray.routing.directives._
import spray.http.StatusCodes._
import spray.util.LoggingContext
import spray._
import routing._
import http.{StatusCodes, HttpResponse, HttpBody, HttpEntity}
import concurrent.Await
import spray.routing.RejectionHandler
import spray.http.MediaTypes._
import akka.actor.OneForOneStrategy
import akka.actor.SupervisorStrategy._
import scala.concurrent.duration._
import akka.event.Logging

import com.gecemmo.raptor.core._

// Actor messages
case class GetImplementation()
case class Implementation(title: String, version: String, build: String)
case class Start()
case class Started()
case class Stop()

/**
 * TODO: Extend with JSON error messages
 */
class RoutedHttpService(route: Route) extends Actor with HttpService {

  import CustomMarshallers._

  implicit def actorRefFactory = context

  implicit val exceptionHandler = ExceptionHandler.fromPF {
    case NonFatal(ErrorResponseException(statusCode, message)) => ctx =>
      println("NonFatal 1")
      ctx.complete(statusCode, message)

    case NonFatal(e) => ctx =>
      println("NonFatal 2")
      ctx.complete(InternalServerError)
  }

  def jsonify(response: HttpResponse): HttpResponse = {
    
    // Extract valuable info from repsonse
    val value = response.status.value
    val reason = response.status.reason
    val msg = response.status.defaultMessage
    
    // Ad-hoc marshaller
    // TODO: look at routeRouteResponse directive when added to milestone
    HttpResponse(response.status, HttpBody(`application/json`, s"""{"status" : $value , "message" : "$reason", "details" : "$msg"}"""))
  }

  implicit val myRejectionHandler = RejectionHandler.fromPF {
    case rejections => mapHttpResponse(jsonify) {
      RejectionHandler.Default(rejections)
    } 
  }


  def receive = {
    runRoute(route)(exceptionHandler, myRejectionHandler, context,
                    RoutingSettings.Default, LoggingContext.fromActorRefFactory)
  }
}

/**
 * Main application actor responsible for service actors.
 */
class ApplicationActor extends Actor {

  val log = Logging(context.system, this)
  override def preStart() = {
    log.info(self.path + " started")
  }

  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
    case _: ArithmeticException => Resume
    case _: NullPointerException => Restart
    case _: Exception => Escalate
  }

  def receive = {
    case GetImplementation() =>
      val title = "raptor"
      val version = "0.1"
      val build = "1"
      title + " " + version + " " + build

    sender ! Implementation(title, version, build)

    /**
     * Starts the children actors
     */
    case Start() =>
      //context.actorOf(Props(new RaptorService()), "raptorapi")
      //context.actorOf(Props(new TenantActor()), "tenant")

      sender ! Started()

    /**
     * Stops this actor and all the child actors.
     */
    case Stop() =>
      context.children.foreach(context.stop _)
  }
}

/**
 * Responsible for starting the main actor
 */
trait ServerCore {
  implicit def actorSystem: ActorSystem
  implicit val timeout = Timeout(30000)

  val application = actorSystem.actorOf(
    props = Props[ApplicationActor],
    name = "application"
  )

  Await.ready(application ? Start(), timeout.duration)
}

/**
* REST API interface
*/
trait Api extends RouteConcatenation {
  this: ServerCore =>

  // Combines the various service routes
  val routes =
  new RaptorService().route ~ 
  new TenantService().route
  
  def rejectionHandler: PartialFunction[scala.List[Rejection], HttpResponse] = {
    case (rejections: List[Rejection]) => HttpResponse(StatusCodes.BadRequest)
  }

  val rootService = actorSystem.actorOf(Props(new RoutedHttpService(routes)), "httpservice")
}
