package ai.verta.client.entities

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import ai.verta.swagger._public.modeldb.model.{ModeldbCreateExperimentRun, ModeldbExperiment}
import ai.verta.swagger.client.ClientSet

import scala.concurrent.ExecutionContext

class Experiment(clientSet: ClientSet, val proj: Project, val expt: ModeldbExperiment) {
  def getOrCreateExperimentRun(name: String = "")(implicit ec: ExecutionContext) = {
    val internalName = if (name == "") LocalDateTime.now.format(DateTimeFormatter.ofPattern("YYYY/MM/dd HH:mm:ss.SSSS")) else name

    GetOrCreateEntity.getOrCreate[ExperimentRun](
      get = () => {
        clientSet.experimentRunService.getExperimentRunByName(internalName, expt.id.get)
          .map(r => if (r.experiment_run.isEmpty) null else new ExperimentRun(clientSet, this, r.experiment_run.get))
      },
      create = () => {
        clientSet.experimentRunService.createExperimentRun(ModeldbCreateExperimentRun(
          name = Some(internalName),
          experiment_id = expt.id,
          project_id = proj.proj.id // TODO: remove since we can get from the experiment
        ))
          .map(r => if (r.experiment_run.isEmpty) null else new ExperimentRun(clientSet, this, r.experiment_run.get))
      }
    )
  }
}
