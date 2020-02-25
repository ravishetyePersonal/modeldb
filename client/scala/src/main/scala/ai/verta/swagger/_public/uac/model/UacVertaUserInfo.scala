// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.uac.model

import ai.verta.swagger._public.uac.model.IdServiceProviderEnumIdServiceProvider._
import ai.verta.swagger._public.uac.model.UacFlagEnum._

case class UacVertaUserInfo (
  individual_user: Option[Boolean] = None,
  username: Option[String] = None,
  refresh_timestamp: Option[String] = None,
  last_login_timestamp: Option[String] = None,
  user_id: Option[String] = None,
  publicProfile: Option[UacFlagEnum] = None
)
