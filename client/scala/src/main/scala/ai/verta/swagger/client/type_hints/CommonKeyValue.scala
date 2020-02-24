package ai.verta.swagger.client.type_hints

import ai.verta.swagger._public
import ai.verta.swagger._public.modeldb.model.ProtobufValue
import net.liftweb.json.JsonAST.{JDouble, JField, JString}
import net.liftweb.json.{JObject, ShortTypeHints}

object CommonKeyValue {
  val hints = new ShortTypeHints(classOf[_public.modeldb.model.CommonKeyValue] :: Nil) {
    override def serialize: PartialFunction[Any, JObject] = {
      case kv: _public.modeldb.model.CommonKeyValue => {
        val v = kv.value.get
        JObject(
          JField("key", JString(kv.key.get)) ::
            JField("value",
              if (v.string_value.isDefined)
                JString(v.string_value.get)
              else if (v.number_value.isDefined)
                JDouble(v.number_value.get)
              else
                throw new Exception(s"unknown type ${v.getClass.toString} for key ${kv.key.get}")
            ) ::
            Nil
        )
      }
    }

    override def deserialize: PartialFunction[(String, JObject), Any] = {
      case ("CommonKeyValue", JObject(JField("key", JString(key)) :: JField("value", value) :: Nil
      )) => {
        new _public.modeldb.model.CommonKeyValue(
          key = Some(key),
          value = Some(
            value match {
              case JString(x) => ProtobufValue(string_value = Some(x))
              case JDouble(x) => ProtobufValue(number_value = Some(x))
              case other => throw new Exception(s"unknown type ${other.toString} for key $key")
            }
          )
        )
      }
    }
  }
}
