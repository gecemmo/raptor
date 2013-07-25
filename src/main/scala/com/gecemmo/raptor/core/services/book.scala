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

import com.gecemmo.raptor.core._

/**
* Specifies all book operations.
* Basic CRUD-functionality
*/
trait BookOperations {

	def GetBooks() = "this is all books"
	def GetBookById() = ApiUser(Some("Gecemmo AB"), Some("551122-4321"))
}

/**
* Service actor for book operations.
* Basic CRUD-functionality
*/
class BookActor() extends Actor with DefaultTimeout with BookOperations {

	def receive = {

		case "hello" =>
	    	sender ! "i got it, from book actor"

	    case GetBooksById(id) =>
	    	sender ! GetBookById()

	    case GetBooks() =>
	    	sender ! GetBooks()
	}
}