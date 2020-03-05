// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.modeldb.versioning.model

import scala.util.Try

import net.liftweb.json._

import ai.verta.swagger._public.modeldb.versioning.model.WorkspaceTypeEnumWorkspaceType._
import ai.verta.swagger.client.objects._

case class VersioningPathDatasetDiff (
  deleted: Option[Boolean] = None,
  added: Option[Boolean] = None,
  A: Option[VersioningPathDatasetBlob] = None,
  B: Option[VersioningPathDatasetBlob] = None
) extends BaseSwagger {
  def toJson(): JValue = VersioningPathDatasetDiff.toJson(this)
}

object VersioningPathDatasetDiff {
  def toJson(obj: VersioningPathDatasetDiff): JObject = {
    new JObject(
      List[Option[JField]](
        obj.deleted.map(x => JField("deleted", JBool(x))),
        obj.added.map(x => JField("added", JBool(x))),
        obj.A.map(x => JField("A", ((x: VersioningPathDatasetBlob) => VersioningPathDatasetBlob.toJson(x))(x))),
        obj.B.map(x => JField("B", ((x: VersioningPathDatasetBlob) => VersioningPathDatasetBlob.toJson(x))(x)))
      ).flatMap(x => x match {
        case Some(y) => List(y)
        case None => Nil
      })
    )
  }

  def fromJson(value: JValue): VersioningPathDatasetDiff =
    value match {
      case JObject(fields) => {
        val fieldsMap = fields.map(f => (f.name, f.value)).toMap
        VersioningPathDatasetDiff(
          // TODO: handle required
          deleted = fieldsMap.get("deleted").map(JsonConverter.fromJsonBoolean),
          added = fieldsMap.get("added").map(JsonConverter.fromJsonBoolean),
          A = fieldsMap.get("A").map(VersioningPathDatasetBlob.fromJson),
          B = fieldsMap.get("B").map(VersioningPathDatasetBlob.fromJson)
        )
      }
      case _ => throw new IllegalArgumentException(s"unknown type ${value.getClass.toString}")
    }
}
