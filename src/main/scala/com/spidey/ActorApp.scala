package com.spidey

object ActorApp extends App {
  val action = new Action();
  ActorManager.registerActor(1, action)
  ActorManager.registerActor(2, action)
  ActorManager.unregisterActor(1)

}
