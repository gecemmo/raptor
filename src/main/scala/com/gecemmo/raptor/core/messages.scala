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

// Tenants
case class GetTenantByApiKey(key: String)

// Applications
case class GetApplicationById(id: String)

// Users
case class GetLoginAttemptResult(username: String, password: String)

// Metrics
case class MetricsEvent(key: String, value: Double)