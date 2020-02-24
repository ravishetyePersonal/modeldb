package ai.verta.client.entities.utils

import ai.verta.swagger._public.modeldb.model.{CommonKeyValue, ProtobufValue}

import scala.util.{Failure, Success, Try}

object KVHandler {
  def convertValue(v: ProtobufValue, err: String): Try[Any] = {
    if (v.number_value.isDefined)
      Success(v.number_value.get)
    else if (v.string_value.isDefined)
      Success(v.string_value.get)
    else
      Failure(new IllegalArgumentException(err))
  }

  def convertValue(v: Any, err: String): Try[ProtobufValue] = {
    v match {
      case x: Int => Success(ProtobufValue(number_value = Some(x)))
      case x: Double => Success(ProtobufValue(number_value = Some(x)))
      case x: String => Success(ProtobufValue(string_value = Some(x)))
      case _ => Failure(new IllegalArgumentException(err))
    }
  }

  def mapToKVList(vals: Map[String, Any]) = {
    Try({
      vals.toList.map(arg => {
        val k = arg._1
        val v = arg._2
        convertValue(v, s"unknown type for hyperparameter ${arg._1}")
          .map(v => CommonKeyValue(key = Some(k), value = Some(v)))
          .get
      })
    })
  }

  def kvListToMap(vals: List[CommonKeyValue]): Try[Map[String, Any]] = {
    Try({
      val keys = vals.map(_.key.get)
      val values = vals.map(v => convertValue(v.value.get, s"unknown type for hyperparameter ${v.key.get}").get)
      (keys zip values).toMap
    })
  }
}
