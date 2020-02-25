package ai.verta.client.entities

import ai.verta.client.entities.subobjects._
import ai.verta.swagger._public.modeldb.model._
import ai.verta.swagger.client.ClientSet

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success, Try}

class ExperimentRun(clientSet: ClientSet, val expt: Experiment, val run: ModeldbExperimentRun) extends Taggable {
  def tags()(implicit ec: ExecutionContext) = new Tags(clientSet, ec, this)

  override def getTags()(implicit ec: ExecutionContext): Try[List[String]] = {
    clientSet.experimentRunService.getExperimentRunTags(run.id.get)
      .map(r => r.tags.getOrElse(Nil))
  }

  override def delTags(tags: List[String])(implicit ec: ExecutionContext): Try[Unit] = {
    clientSet.experimentRunService.deleteExperimentRunTags(ModeldbDeleteExperimentRunTags(
      id = run.id,
      tags = Some(tags)
    ))
      .map(_ => {})
  }

  override def addTags(tags: List[String])(implicit ec: ExecutionContext): Try[Unit] = {
    clientSet.experimentRunService.addExperimentRunTags(ModeldbAddExperimentRunTags(
      id = run.id,
      tags = Some(tags)
    ))
      .map(_ => {})
  }

  // TODO: add overwrite
  def hyperparameters()(implicit ec: ExecutionContext) = new Hyperparameters(clientSet, ec, this)

  def logHyperparameters(vals: Map[String, Any])(implicit ec: ExecutionContext): Try[Unit] = {
    val valsList = utils.KVHandler.mapToKVList(vals)
    if (valsList.isFailure) Failure(valsList.failed.get) else
      clientSet.experimentRunService.logHyperparameters(ModeldbLogHyperparameters(
        id = run.id,
        hyperparameters = valsList.toOption
      )).map(_ => {})
  }

  def logHyperparameter(key: String, value: Any)(implicit ec: ExecutionContext) =
    logHyperparameters(Map(key -> value))

  def getHyperparameters()(implicit ec: ExecutionContext): Try[Map[String, Any]] = {
    clientSet.experimentRunService.getHyperparameters(
      id = run.id.get
    )
      .flatMap(r => {
        if (r.hyperparameters.isEmpty)
          Success(Map[String, Any]())
        else
          utils.KVHandler.kvListToMap(r.hyperparameters.get)
      })
  }

  def getHyperparameter(key: String)(implicit ec: ExecutionContext) =
    getHyperparameters().map(_.get(key))

  // TODO: add overwrite
  def metrics()(implicit ec: ExecutionContext) = new Metrics(clientSet, ec, this)

  def logMetrics(vals: Map[String, Any])(implicit ec: ExecutionContext): Try[Unit] = {
    val valsList = utils.KVHandler.mapToKVList(vals)
    if (valsList.isFailure) Failure(valsList.failed.get) else
      clientSet.experimentRunService.logMetrics(ModeldbLogMetrics(
        id = run.id,
        metrics = valsList.toOption
      )).map(_ => {})
  }

  def logMetric(key: String, value: Any)(implicit ec: ExecutionContext) =
    logMetrics(Map(key -> value))

  def getMetrics()(implicit ec: ExecutionContext): Try[Map[String, Any]] = {
    clientSet.experimentRunService.getMetrics(
      id = run.id.get
    )
      .flatMap(r => {
        if (r.metrics.isEmpty)
          Success(Map[String, Any]())
        else
          utils.KVHandler.kvListToMap(r.metrics.get)
      })
  }

  def getMetric(key: String)(implicit ec: ExecutionContext) =
    getMetrics().map(_.get(key))

  // TODO: add overwrite
  def attributes()(implicit ec: ExecutionContext) = new Attributes(clientSet, ec, this)

  def logAttributes(vals: Map[String, Any])(implicit ec: ExecutionContext): Try[Unit] = {
    val valsList = utils.KVHandler.mapToKVList(vals)
    if (valsList.isFailure) Failure(valsList.failed.get) else
      clientSet.experimentRunService.logAttributes(ModeldbLogAttributes(
        id = run.id,
        attributes = valsList.toOption
      )).map(_ => {})
  }

  def logAttribute(key: String, value: Any)(implicit ec: ExecutionContext) =
    logAttributes(Map(key -> value))

  def getAttributes(keys: List[String] = Nil)(implicit ec: ExecutionContext): Try[Map[String, Any]] = {
    clientSet.experimentRunService.getExperimentRunAttributes(
      id = run.id.get,
      attribute_keys = keys,
      get_all = keys.isEmpty
    )
      .flatMap(r => {
        if (r.attributes.isEmpty)
          Success(Map[String, Any]())
        else
          utils.KVHandler.kvListToMap(r.attributes.get)
      })
  }

  def getAttribute(key: String)(implicit ec: ExecutionContext) =
    getAttributes(List(key)).map(_.get(key))
}
