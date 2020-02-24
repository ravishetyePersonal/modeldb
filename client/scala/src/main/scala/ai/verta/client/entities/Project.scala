package ai.verta.client.entities

import ai.verta.swagger._public.modeldb.model.{ModeldbCreateExperiment, ModeldbProject}
import ai.verta.swagger.client.ClientSet

import scala.concurrent.ExecutionContext

class Project(clientSet: ClientSet, val proj: ModeldbProject) {
  def getOrCreateExperiment(name: String)(implicit ec: ExecutionContext) = {
    GetOrCreateEntity.getOrCreate[Experiment](
      get = () => {
        clientSet.experimentService.getExperimentByName(name, proj.id.get)
          .map(r => if (r.experiment.isEmpty) null else new Experiment(clientSet, this, r.experiment.get))
      },
      create = () => {
        clientSet.experimentService.createExperiment(ModeldbCreateExperiment(
          name = Some(name),
          project_id = proj.id
        ))
          .map(r => if (r.experiment.isEmpty) null else new Experiment(clientSet, this, r.experiment.get))
      }
    )
  }
}
