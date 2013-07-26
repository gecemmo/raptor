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

// ConcurrentMap, ConcurrentHashMap
import java.util.concurrent._

import com.gecemmo.raptor.core._

case class Book(title: String, author: String, isbn: String)

// Generic Mock DB layer
class MockDbLayer[T] {

	val db: ConcurrentMap[String, T] = new ConcurrentHashMap[String, T]

	def add(id: String, t: T): String = {
		db.putIfAbsent(id, t)
		id
	}

	def get(id: String): T = {
		db.get(id)
	}
}

/**
* Specifies all book operations.
* Basic CRUD-functionality
*/
trait BookOperations {

	def GetBooks() = "this is all books"
	def GetBookById() = ApiUser(Some("Gecemmo AB"), Some("551122-4321"))
}

trait CRUDOperations[T] {

	// Consider: BREAD
	
	//def GetAll() = "Get all"	
	//def GetById() = "Get by Id"
	
	// Create
	def Create(t: T) = "Create"

	// Remove
	def Read(id: String) = ApiUser(Some("Gecemmo AB"), Some("551122-4321"))

	// Update
	def Update(t: T) = "Update"

	// Delete
	def Delete(t: T) = "Update"
}

/**
* Service actor for book operations.
* Basic CRUD-functionality
*/
class BookActor() extends Actor with DefaultTimeout with CRUDOperations[Book] {

	def receive = {

		case "hello" =>
	    	sender ! "i got it, from book actor"

	    case GetBooksById(id) =>
	    	sender ! Read(id)
	}
}