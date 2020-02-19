// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.uac.model

import ai.verta.swagger._public.uac.model.IdServiceProviderEnumIdServiceProvider._
import ai.verta.swagger._public.uac.model.UacFlagEnum._

case class UacVertaUserInfo (
  individualUser: Option[Boolean],
  username: Option[String],
  refreshTimestamp: Option[String],
  lastLoginTimestamp: Option[String],
  userId: Option[String],
  publicProfile: Option[UacFlagEnum]
)
