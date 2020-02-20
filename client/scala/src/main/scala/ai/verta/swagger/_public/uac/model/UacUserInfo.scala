// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.uac.model

import ai.verta.swagger._public.uac.model.IdServiceProviderEnumIdServiceProvider._
import ai.verta.swagger._public.uac.model.UacFlagEnum._

case class UacUserInfo (
  userId: Option[String] = None,
  fullName: Option[String] = None,
  firstName: Option[String] = None,
  lastName: Option[String] = None,
  email: Option[String] = None,
  idServiceProvider: Option[IdServiceProviderEnumIdServiceProvider] = None,
  roles: Option[List[String]] = None,
  imageUrl: Option[String] = None,
  devKey: Option[String] = None,
  vertaInfo: Option[UacVertaUserInfo] = None
)
