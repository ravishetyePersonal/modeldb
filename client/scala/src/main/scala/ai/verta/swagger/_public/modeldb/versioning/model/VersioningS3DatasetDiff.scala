// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.modeldb.versioning.model

import scala.util.Try

import net.liftweb.json._

import ai.verta.swagger._public.modeldb.versioning.model.WorkspaceTypeEnumWorkspaceType._
import ai.verta.swagger.client.objects._

case class VersioningS3DatasetDiff (
  path: Option[VersioningPathDatasetDiff] = None
) extends BaseSwagger {
  def toJson(): JValue = VersioningS3DatasetDiff.toJson(this)
}

object VersioningS3DatasetDiff {
  def toJson(obj: VersioningS3DatasetDiff): JObject = {
    new JObject(
      List[Option[JField]](
        obj.path.map(x => JField("path", ((x: VersioningPathDatasetDiff) => VersioningPathDatasetDiff.toJson(x))(x)))
      ).flatMap(x => x match {
        case Some(y) => List(y)
        case None => Nil
      })
    )
  }

  def fromJson(value: JValue): VersioningS3DatasetDiff =
    value match {
      case JObject(fields) => {
        val fieldsMap = fields.map(f => (f.name, f.value)).toMap
        VersioningS3DatasetDiff(
          // TODO: handle required
          path = fieldsMap.get("path").map(VersioningPathDatasetDiff.fromJson)
        )
      }
      case _ => throw new IllegalArgumentException(s"unknown type ${value.getClass.toString}")
    }
}
