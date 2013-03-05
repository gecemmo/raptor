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

package com.gecemmo.raptor.core.services

import akka.actor.{Props, Actor, ActorSystem}
import akka.pattern.ask
import akka.util.Timeout

import java.util.HashMap
import scala.collection.JavaConversions._
import com.espertech.esper.client.{ Configuration, EventBean, UpdateListener, EPServiceProviderManager }

import com.gecemmo.raptor.core.{DefaultTimeout, MetricsEvent}

/**
* Specifies all tenant operations.
*/
trait MetricsOperations {

  def GetTenantByApiKey() = "i will send over a valid response in the future"
}

// Sample setting
object SampleSetting {

  def query = "select avg(value) from MetricsEvent.win:time(30 sec)"

  def callback(newEvents: Array[EventBean], oldEvents: Array[EventBean]): Unit = {
    println("Triggered (SampleSetting)")
  }
}

/**
* Service actor for metrics operations using Esper and CEP.
*/
class MetricsActor() extends Actor with DefaultTimeout with TenantOperations {

  val config = new Configuration();
  val eventType = new HashMap[String, AnyRef]()

  eventType.put("key", classOf[String])
  eventType.put("value", classOf[Double])
  config.addEventType("MetricsEvent", eventType)

  val epService = EPServiceProviderManager.getDefaultProvider(config);
  val expression = SampleSetting.query;
  val statement = epService.getEPAdministrator.createEPL(expression);

  def receive = {

    case "hello" =>
      sender ! "i got it, from metrics actor" 

    case MetricsEvent(key, value) =>
      epService.getEPRuntime.sendEvent(Map("key" -> key, "value" -> value), "MetricsEvent")
  }
}