package example

import ai.verta.client._

import scala.concurrent.ExecutionContext

object Hello extends App {
  implicit val ec = ExecutionContext.global

  val client = new Client(ClientConnection.fromEnvironment())
  try {
    println(client.getOrCreateProject("scala test")
      .flatMap(_.getOrCreateExperiment("experiment"))
      .flatMap(_.getOrCreateExperimentRun())
      .map(run => {
        run.hyperparameters += (("foo", 2), ("bar", "baz"))
      })
      .get)
  } finally {
    client.close()
  }
}
