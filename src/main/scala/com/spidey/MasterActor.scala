package com.spidey

import akka.actor.{Actor, ActorRef, Cancellable, Props, Terminated}

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object MasterActor {
  def props(x: Int): Props = Props(new MasterActor(x))
}

class MasterActor(x: Int) extends Actor {

  case class Act(id: Int)

  var workerActors: Map[Int, ActorRef] = Map()
  var cancellableMap: Map[Int, Cancellable] = Map()

  override def receive: Receive = {
    case RegisterActor(id, workerActor) =>
      workerActors += (id -> workerActor)
      val cancellable = actAtFixedRate(id)
      cancellableMap += (id -> cancellable)
      context.watch(workerActor)

    case UnregisterActor(id) =>
      terminateWorker(id)

    case Terminated(worker) =>
      System.out.println("Worker Terminated")

    case Act(id) =>
      if (workerActors.contains(id)) {
        val workerActor = workerActors(id)
        workerActor ! Message("Hello")
      }
  }

  def actAtFixedRate(id: Int): Cancellable = {
    val cancellable = context.system.scheduler.scheduleAtFixedRate(
      0.milliseconds,
      500.milliseconds,
      self,
      Act(id))
    cancellable
  }

  def terminateWorker(id: Int) = {
    cancellableMap(id).cancel()
    if (workerActors.contains(id)) {
      val workerActor = workerActors(id)
      workerActors -= id
      context.system.stop(workerActor)
    }
  }
}
