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

import spray.routing.directives._
import spray.routing._
import spray.http._

import java.util.concurrent._

import com.yammer.metrics._


trait SampleMetrics {
  import BasicDirectives._

  private val hists: ConcurrentMap[String, core.Histogram] = new ConcurrentHashMap[String, core.Histogram]
  private val meters: ConcurrentMap[String, core.Meter] = new ConcurrentHashMap[String, core.Meter]

  def getHist(label: String):core.Histogram = hists.get(label)
  def getMeter(label: String):core.Meter = meters.get(label)

  def meter(label: String) {
    meters.putIfAbsent(label, Metrics.newMeter(new core.MetricName("org","testing","metrics"), label, java.util.concurrent.TimeUnit.SECONDS))
    getMeter(label).mark
  }

  def meterAndGet(label: String) = {
    meter(label)
    getMeter(label)
  }

  // directives

  def onRequest(label: String): Directive0 = {
      mapRequest { request => meter(label); request }
    }

  def onResponse(label: String): Directive0 = {
      mapRouteResponse { response => meter(label); response }
    }

  def time(label: String): Directive0 =
    mapRequestContext { ctx =>
      hists.putIfAbsent(label, Metrics.newHistogram(new core.MetricName("a","b","c"), true))
      val startTime = System.nanoTime
      ctx.mapRouteResponse { response => getHist(label).update((System.nanoTime - startTime) / 1000) ; response }
    }
}

object SampleMetrics extends SampleMetrics