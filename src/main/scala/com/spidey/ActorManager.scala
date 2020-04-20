package com.spidey

import akka.actor.ActorSystem

object ActorManager {
  implicit val system: ActorSystem = ActorSystem("actorSystem")
  implicit val executionContext = system.dispatcher
  val producer = system.actorOf(MasterActor.props(1))

  def registerActor(id: Int, action: IAction): Unit = {
      val worker = system.actorOf(WorkerActor.props(action))
      producer ! RegisterActor(id, worker)
  }

  def unregisterActor(id: Int) = {
    producer ! UnregisterActor(id)
  }
}
