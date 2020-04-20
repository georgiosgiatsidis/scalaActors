package com.spidey

import akka.actor.{Actor, PoisonPill, Props}
import akka.io.UdpConnected.Disconnect

object WorkerActor {
  def props(action: IAction): Props = Props(new WorkerActor(action))
}

class WorkerActor(action: IAction) extends Actor {

  override def receive: Receive = {
    case Message(x: String) =>
      action.act()
      System.out.println(x)
    case Disconnect => {
      println("Poison Pill")
      self ! PoisonPill
    }
  }
}