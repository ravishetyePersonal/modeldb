// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.modeldb.versioning.model

import scala.util.Try

import net.liftweb.json._

import ai.verta.swagger._public.modeldb.versioning.model.WorkspaceTypeEnumWorkspaceType._
import ai.verta.swagger.client.objects._

case class VersioningBlobDiff (
  location: Option[List[String]] = None,
  dataset: Option[VersioningDatasetDiff] = None
) extends BaseSwagger {
  def toJson(): JValue = VersioningBlobDiff.toJson(this)
}

object VersioningBlobDiff {
  def toJson(obj: VersioningBlobDiff): JObject = {
    new JObject(
      List[Option[JField]](
        obj.location.map(x => JField("location", ((x: List[String]) => JArray(x.map(JString)))(x))),
        obj.dataset.map(x => JField("dataset", ((x: VersioningDatasetDiff) => VersioningDatasetDiff.toJson(x))(x)))
      ).flatMap(x => x match {
        case Some(y) => List(y)
        case None => Nil
      })
    )
  }

  def fromJson(value: JValue): VersioningBlobDiff =
    value match {
      case JObject(fields) => {
        val fieldsMap = fields.map(f => (f.name, f.value)).toMap
        VersioningBlobDiff(
          // TODO: handle required
          location = fieldsMap.get("location").map((x: JValue) => x match {case JArray(elements) => elements.map(JsonConverter.fromJsonString); case _ => throw new IllegalArgumentException(s"unknown type ${x.getClass.toString}")}),
          dataset = fieldsMap.get("dataset").map(VersioningDatasetDiff.fromJson)
        )
      }
      case _ => throw new IllegalArgumentException(s"unknown type ${value.getClass.toString}")
    }
}
