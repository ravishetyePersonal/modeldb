// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package ai.verta.swagger._public.modeldb.versioning.model

import ai.verta.swagger._public.modeldb.versioning.model.WorkspaceTypeEnumWorkspaceType._

case class VersioningCreateCommitRequest (
  repository_id: Option[VersioningRepositoryIdentification] = None,
  commit: Option[VersioningCommit] = None,
  blobs: Option[List[VersioningBlobExpanded]] = None
)
