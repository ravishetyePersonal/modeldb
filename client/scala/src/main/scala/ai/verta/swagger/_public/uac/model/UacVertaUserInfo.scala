// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.uac.model

import ai.verta.swagger._public.uac.model.IdServiceProviderEnumIdServiceProvider._
import ai.verta.swagger._public.uac.model.UacFlagEnum._

case class UacVertaUserInfo (
  individualUser: Option[Boolean] = None,
  username: Option[String] = None,
  refreshTimestamp: Option[String] = None,
  lastLoginTimestamp: Option[String] = None,
  userId: Option[String] = None,
  publicProfile: Option[UacFlagEnum] = None
)
