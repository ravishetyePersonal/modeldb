// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.modeldb.versioning.model

import scala.util.Try

import net.liftweb.json._

import ai.verta.swagger._public.modeldb.versioning.model.WorkspaceTypeEnumWorkspaceType._
import ai.verta.swagger.client.objects._

case class VersioningBlob (
  dataset: Option[VersioningDatasetBlob] = None,
  environment: Option[VersioningEnvironmentBlob] = None,
  code: Option[VersioningCodeBlob] = None,
  config: Option[VersioningConfigBlob] = None
) extends BaseSwagger {
  def toJson(): JValue = VersioningBlob.toJson(this)
}

object VersioningBlob {
  def toJson(obj: VersioningBlob): JObject = {
    new JObject(
      List[Option[JField]](
        obj.dataset.map(x => JField("dataset", ((x: VersioningDatasetBlob) => VersioningDatasetBlob.toJson(x))(x))),
        obj.environment.map(x => JField("environment", ((x: VersioningEnvironmentBlob) => VersioningEnvironmentBlob.toJson(x))(x))),
        obj.code.map(x => JField("code", ((x: VersioningCodeBlob) => VersioningCodeBlob.toJson(x))(x))),
        obj.config.map(x => JField("config", ((x: VersioningConfigBlob) => VersioningConfigBlob.toJson(x))(x)))
      ).flatMap(x => x match {
        case Some(y) => List(y)
        case None => Nil
      })
    )
  }

  def fromJson(value: JValue): VersioningBlob =
    value match {
      case JObject(fields) => {
        val fieldsMap = fields.map(f => (f.name, f.value)).toMap
        VersioningBlob(
          // TODO: handle required
          dataset = fieldsMap.get("dataset").map(VersioningDatasetBlob.fromJson),
          environment = fieldsMap.get("environment").map(VersioningEnvironmentBlob.fromJson),
          code = fieldsMap.get("code").map(VersioningCodeBlob.fromJson),
          config = fieldsMap.get("config").map(VersioningConfigBlob.fromJson)
        )
      }
      case _ => throw new IllegalArgumentException(s"unknown type ${value.getClass.toString}")
    }
}
