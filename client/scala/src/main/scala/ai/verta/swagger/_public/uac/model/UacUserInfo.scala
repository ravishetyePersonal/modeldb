// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.uac.model

import ai.verta.swagger._public.uac.model.IdServiceProviderEnumIdServiceProvider._
import ai.verta.swagger._public.uac.model.UacFlagEnum._

case class UacUserInfo (
  userId: Option[String],
  fullName: Option[String],
  firstName: Option[String],
  lastName: Option[String],
  email: Option[String],
  idServiceProvider: Option[IdServiceProviderEnumIdServiceProvider],
  roles: Option[List[String]],
  imageUrl: Option[String],
  devKey: Option[String],
  vertaInfo: Option[UacVertaUserInfo]
)
