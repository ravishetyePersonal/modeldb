// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.uac.model

import ai.verta.swagger._public.uac.model.IdServiceProviderEnumIdServiceProvider._
import ai.verta.swagger._public.uac.model.UacFlagEnum._

case class UacUserInfo (
  user_id: Option[String] = None,
  full_name: Option[String] = None,
  first_name: Option[String] = None,
  last_name: Option[String] = None,
  email: Option[String] = None,
  id_service_provider: Option[IdServiceProviderEnumIdServiceProvider] = None,
  roles: Option[List[String]] = None,
  image_url: Option[String] = None,
  dev_key: Option[String] = None,
  verta_info: Option[UacVertaUserInfo] = None
)
