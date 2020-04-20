package com.spidey

import akka.actor.ActorRef

case class Message(line:String)
case class Result(stats:Map[String,Int])

case class RegisterActor(id: Int, actorRef: ActorRef)
case class UnregisterActor(id: Int)
