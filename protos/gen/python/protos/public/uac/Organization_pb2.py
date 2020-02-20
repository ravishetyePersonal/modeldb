# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: protos/public/uac/Organization.proto

import sys
_b=sys.version_info[0]<3 and (lambda x:x) or (lambda x:x.encode('latin1'))
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()


from google.api import annotations_pb2 as google_dot_api_dot_annotations__pb2
from ...public.common import CommonService_pb2 as protos_dot_public_dot_common_dot_CommonService__pb2


DESCRIPTOR = _descriptor.FileDescriptor(
  name='protos/public/uac/Organization.proto',
  package='ai.verta.uac',
  syntax='proto3',
  serialized_options=_b('P\001Z:github.com/VertaAI/modeldb/protos/gen/go/protos/public/uac'),
  serialized_pb=_b('\n$protos/public/uac/Organization.proto\x12\x0c\x61i.verta.uac\x1a\x1cgoogle/api/annotations.proto\x1a(protos/public/common/CommonService.proto\"\x99\x01\n\x0cOrganization\x12\n\n\x02id\x18\x01 \x01(\t\x12\x0c\n\x04name\x18\x02 \x01(\t\x12\x12\n\nshort_name\x18\x07 \x01(\t\x12\x13\n\x0b\x64\x65scription\x18\x03 \x01(\t\x12\x10\n\x08owner_id\x18\x04 \x01(\t\x12\x19\n\x11\x63reated_timestamp\x18\x05 \x01(\x03\x12\x19\n\x11updated_timestamp\x18\x06 \x01(\x03\"c\n\x13GetOrganizationById\x12\x0e\n\x06org_id\x18\x01 \x01(\t\x1a<\n\x08Response\x12\x30\n\x0corganization\x18\x01 \x01(\x0b\x32\x1a.ai.verta.uac.Organization\"g\n\x15GetOrganizationByName\x12\x10\n\x08org_name\x18\x01 \x01(\t\x1a<\n\x08Response\x12\x30\n\x0corganization\x18\x01 \x01(\x0b\x32\x1a.ai.verta.uac.Organization\"n\n\x1aGetOrganizationByShortName\x12\x12\n\nshort_name\x18\x01 \x01(\t\x1a<\n\x08Response\x12\x30\n\x0corganization\x18\x01 \x01(\x0b\x32\x1a.ai.verta.uac.Organization\"T\n\x13ListMyOrganizations\x1a=\n\x08Response\x12\x31\n\rorganizations\x18\x01 \x03(\x0b\x32\x1a.ai.verta.uac.Organization\"\x9c\x02\n\x0fSetOrganization\x12\x30\n\x0corganization\x18\x01 \x01(\x0b\x32\x1a.ai.verta.uac.Organization\x12X\n\x18global_collaborator_type\x18\x02 \x01(\x0e\x32\x36.ai.verta.common.CollaboratorTypeEnum.CollaboratorType\x12?\n\x11global_can_deploy\x18\x03 \x01(\x0e\x32$.ai.verta.common.TernaryEnum.Ternary\x1a<\n\x08Response\x12\x30\n\x0corganization\x18\x01 \x01(\x0b\x32\x1a.ai.verta.uac.Organization\"@\n\x12\x44\x65leteOrganization\x12\x0e\n\x06org_id\x18\x01 \x01(\t\x1a\x1a\n\x08Response\x12\x0e\n\x06status\x18\x01 \x01(\x08\"9\n\tListUsers\x12\x0e\n\x06org_id\x18\x01 \x01(\t\x1a\x1c\n\x08Response\x12\x10\n\x08user_ids\x18\x01 \x03(\t\"9\n\tListTeams\x12\x0e\n\x06org_id\x18\x01 \x01(\t\x1a\x1c\n\x08Response\x12\x10\n\x08team_ids\x18\x01 \x03(\t\"I\n\x07\x41\x64\x64User\x12\x0e\n\x06org_id\x18\x01 \x01(\t\x12\x12\n\nshare_with\x18\x02 \x01(\t\x1a\x1a\n\x08Response\x12\x0e\n\x06status\x18\x01 \x01(\x08\"L\n\nRemoveUser\x12\x0e\n\x06org_id\x18\x01 \x01(\t\x12\x12\n\nshare_with\x18\x02 \x01(\t\x1a\x1a\n\x08Response\x12\x0e\n\x06status\x18\x01 \x01(\x08\x32\xdb\n\n\x13OrganizationService\x12\x92\x01\n\x13getOrganizationById\x12!.ai.verta.uac.GetOrganizationById\x1a*.ai.verta.uac.GetOrganizationById.Response\",\x82\xd3\xe4\x93\x02&\x12$/v1/organization/getOrganizationById\x12\x9a\x01\n\x15getOrganizationByName\x12#.ai.verta.uac.GetOrganizationByName\x1a,.ai.verta.uac.GetOrganizationByName.Response\".\x82\xd3\xe4\x93\x02(\x12&/v1/organization/getOrganizationByName\x12\xae\x01\n\x1agetOrganizationByShortName\x12(.ai.verta.uac.GetOrganizationByShortName\x1a\x31.ai.verta.uac.GetOrganizationByShortName.Response\"3\x82\xd3\xe4\x93\x02-\x12+/v1/organization/getOrganizationByShortName\x12\x92\x01\n\x13listMyOrganizations\x12!.ai.verta.uac.ListMyOrganizations\x1a*.ai.verta.uac.ListMyOrganizations.Response\",\x82\xd3\xe4\x93\x02&\x12$/v1/organization/listMyOrganizations\x12\x85\x01\n\x0fsetOrganization\x12\x1d.ai.verta.uac.SetOrganization\x1a&.ai.verta.uac.SetOrganization.Response\"+\x82\xd3\xe4\x93\x02%\" /v1/organization/setOrganization:\x01*\x12\x91\x01\n\x12\x64\x65leteOrganization\x12 .ai.verta.uac.DeleteOrganization\x1a).ai.verta.uac.DeleteOrganization.Response\".\x82\xd3\xe4\x93\x02(\"#/v1/organization/deleteOrganization:\x01*\x12j\n\tlistTeams\x12\x17.ai.verta.uac.ListTeams\x1a .ai.verta.uac.ListTeams.Response\"\"\x82\xd3\xe4\x93\x02\x1c\x12\x1a/v1/organization/listTeams\x12j\n\tlistUsers\x12\x17.ai.verta.uac.ListUsers\x1a .ai.verta.uac.ListUsers.Response\"\"\x82\xd3\xe4\x93\x02\x1c\x12\x1a/v1/organization/listUsers\x12\x65\n\x07\x61\x64\x64User\x12\x15.ai.verta.uac.AddUser\x1a\x1e.ai.verta.uac.AddUser.Response\"#\x82\xd3\xe4\x93\x02\x1d\"\x18/v1/organization/addUser:\x01*\x12q\n\nremoveUser\x12\x18.ai.verta.uac.RemoveUser\x1a!.ai.verta.uac.RemoveUser.Response\"&\x82\xd3\xe4\x93\x02 \"\x1b/v1/organization/removeUser:\x01*B>P\x01Z:github.com/VertaAI/modeldb/protos/gen/go/protos/public/uacb\x06proto3')
  ,
  dependencies=[google_dot_api_dot_annotations__pb2.DESCRIPTOR,protos_dot_public_dot_common_dot_CommonService__pb2.DESCRIPTOR,])




_ORGANIZATION = _descriptor.Descriptor(
  name='Organization',
  full_name='ai.verta.uac.Organization',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='id', full_name='ai.verta.uac.Organization.id', index=0,
      number=1, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
    _descriptor.FieldDescriptor(
      name='name', full_name='ai.verta.uac.Organization.name', index=1,
      number=2, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
    _descriptor.FieldDescriptor(
      name='short_name', full_name='ai.verta.uac.Organization.short_name', index=2,
      number=7, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
    _descriptor.FieldDescriptor(
      name='description', full_name='ai.verta.uac.Organization.description', index=3,
      number=3, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
    _descriptor.FieldDescriptor(
      name='owner_id', full_name='ai.verta.uac.Organization.owner_id', index=4,
      number=4, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
    _descriptor.FieldDescriptor(
      name='created_timestamp', full_name='ai.verta.uac.Organization.created_timestamp', index=5,
      number=5, type=3, cpp_type=2, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
    _descriptor.FieldDescriptor(
      name='updated_timestamp', full_name='ai.verta.uac.Organization.updated_timestamp', index=6,
      number=6, type=3, cpp_type=2, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=127,
  serialized_end=280,
)


_GETORGANIZATIONBYID_RESPONSE = _descriptor.Descriptor(
  name='Response',
  full_name='ai.verta.uac.GetOrganizationById.Response',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='organization', full_name='ai.verta.uac.GetOrganizationById.Response.organization', index=0,
      number=1, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=321,
  serialized_end=381,
)

_GETORGANIZATIONBYID = _descriptor.Descriptor(
  name='GetOrganizationById',
  full_name='ai.verta.uac.GetOrganizationById',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='org_id', full_name='ai.verta.uac.GetOrganizationById.org_id', index=0,
      number=1, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
  ],
  extensions=[
  ],
  nested_types=[_GETORGANIZATIONBYID_RESPONSE, ],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=282,
  serialized_end=381,
)


_GETORGANIZATIONBYNAME_RESPONSE = _descriptor.Descriptor(
  name='Response',
  full_name='ai.verta.uac.GetOrganizationByName.Response',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='organization', full_name='ai.verta.uac.GetOrganizationByName.Response.organization', index=0,
      number=1, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=321,
  serialized_end=381,
)

_GETORGANIZATIONBYNAME = _descriptor.Descriptor(
  name='GetOrganizationByName',
  full_name='ai.verta.uac.GetOrganizationByName',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='org_name', full_name='ai.verta.uac.GetOrganizationByName.org_name', index=0,
      number=1, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
  ],
  extensions=[
  ],
  nested_types=[_GETORGANIZATIONBYNAME_RESPONSE, ],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=383,
  serialized_end=486,
)


_GETORGANIZATIONBYSHORTNAME_RESPONSE = _descriptor.Descriptor(
  name='Response',
  full_name='ai.verta.uac.GetOrganizationByShortName.Response',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='organization', full_name='ai.verta.uac.GetOrganizationByShortName.Response.organization', index=0,
      number=1, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=321,
  serialized_end=381,
)

_GETORGANIZATIONBYSHORTNAME = _descriptor.Descriptor(
  name='GetOrganizationByShortName',
  full_name='ai.verta.uac.GetOrganizationByShortName',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='short_name', full_name='ai.verta.uac.GetOrganizationByShortName.short_name', index=0,
      number=1, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
  ],
  extensions=[
  ],
  nested_types=[_GETORGANIZATIONBYSHORTNAME_RESPONSE, ],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=488,
  serialized_end=598,
)


_LISTMYORGANIZATIONS_RESPONSE = _descriptor.Descriptor(
  name='Response',
  full_name='ai.verta.uac.ListMyOrganizations.Response',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='organizations', full_name='ai.verta.uac.ListMyOrganizations.Response.organizations', index=0,
      number=1, type=11, cpp_type=10, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=623,
  serialized_end=684,
)

_LISTMYORGANIZATIONS = _descriptor.Descriptor(
  name='ListMyOrganizations',
  full_name='ai.verta.uac.ListMyOrganizations',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
  ],
  extensions=[
  ],
  nested_types=[_LISTMYORGANIZATIONS_RESPONSE, ],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=600,
  serialized_end=684,
)


_SETORGANIZATION_RESPONSE = _descriptor.Descriptor(
  name='Response',
  full_name='ai.verta.uac.SetOrganization.Response',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='organization', full_name='ai.verta.uac.SetOrganization.Response.organization', index=0,
      number=1, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=321,
  serialized_end=381,
)

_SETORGANIZATION = _descriptor.Descriptor(
  name='SetOrganization',
  full_name='ai.verta.uac.SetOrganization',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='organization', full_name='ai.verta.uac.SetOrganization.organization', index=0,
      number=1, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
    _descriptor.FieldDescriptor(
      name='global_collaborator_type', full_name='ai.verta.uac.SetOrganization.global_collaborator_type', index=1,
      number=2, type=14, cpp_type=8, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
    _descriptor.FieldDescriptor(
      name='global_can_deploy', full_name='ai.verta.uac.SetOrganization.global_can_deploy', index=2,
      number=3, type=14, cpp_type=8, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
  ],
  extensions=[
  ],
  nested_types=[_SETORGANIZATION_RESPONSE, ],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=687,
  serialized_end=971,
)


_DELETEORGANIZATION_RESPONSE = _descriptor.Descriptor(
  name='Response',
  full_name='ai.verta.uac.DeleteOrganization.Response',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='status', full_name='ai.verta.uac.DeleteOrganization.Response.status', index=0,
      number=1, type=8, cpp_type=7, label=1,
      has_default_value=False, default_value=False,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=1011,
  serialized_end=1037,
)

_DELETEORGANIZATION = _descriptor.Descriptor(
  name='DeleteOrganization',
  full_name='ai.verta.uac.DeleteOrganization',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='org_id', full_name='ai.verta.uac.DeleteOrganization.org_id', index=0,
      number=1, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
  ],
  extensions=[
  ],
  nested_types=[_DELETEORGANIZATION_RESPONSE, ],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=973,
  serialized_end=1037,
)


_LISTUSERS_RESPONSE = _descriptor.Descriptor(
  name='Response',
  full_name='ai.verta.uac.ListUsers.Response',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='user_ids', full_name='ai.verta.uac.ListUsers.Response.user_ids', index=0,
      number=1, type=9, cpp_type=9, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=1068,
  serialized_end=1096,
)

_LISTUSERS = _descriptor.Descriptor(
  name='ListUsers',
  full_name='ai.verta.uac.ListUsers',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='org_id', full_name='ai.verta.uac.ListUsers.org_id', index=0,
      number=1, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
  ],
  extensions=[
  ],
  nested_types=[_LISTUSERS_RESPONSE, ],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=1039,
  serialized_end=1096,
)


_LISTTEAMS_RESPONSE = _descriptor.Descriptor(
  name='Response',
  full_name='ai.verta.uac.ListTeams.Response',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='team_ids', full_name='ai.verta.uac.ListTeams.Response.team_ids', index=0,
      number=1, type=9, cpp_type=9, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=1127,
  serialized_end=1155,
)

_LISTTEAMS = _descriptor.Descriptor(
  name='ListTeams',
  full_name='ai.verta.uac.ListTeams',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='org_id', full_name='ai.verta.uac.ListTeams.org_id', index=0,
      number=1, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
  ],
  extensions=[
  ],
  nested_types=[_LISTTEAMS_RESPONSE, ],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=1098,
  serialized_end=1155,
)


_ADDUSER_RESPONSE = _descriptor.Descriptor(
  name='Response',
  full_name='ai.verta.uac.AddUser.Response',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='status', full_name='ai.verta.uac.AddUser.Response.status', index=0,
      number=1, type=8, cpp_type=7, label=1,
      has_default_value=False, default_value=False,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=1011,
  serialized_end=1037,
)

_ADDUSER = _descriptor.Descriptor(
  name='AddUser',
  full_name='ai.verta.uac.AddUser',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='org_id', full_name='ai.verta.uac.AddUser.org_id', index=0,
      number=1, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
    _descriptor.FieldDescriptor(
      name='share_with', full_name='ai.verta.uac.AddUser.share_with', index=1,
      number=2, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
  ],
  extensions=[
  ],
  nested_types=[_ADDUSER_RESPONSE, ],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=1157,
  serialized_end=1230,
)


_REMOVEUSER_RESPONSE = _descriptor.Descriptor(
  name='Response',
  full_name='ai.verta.uac.RemoveUser.Response',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='status', full_name='ai.verta.uac.RemoveUser.Response.status', index=0,
      number=1, type=8, cpp_type=7, label=1,
      has_default_value=False, default_value=False,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=1011,
  serialized_end=1037,
)

_REMOVEUSER = _descriptor.Descriptor(
  name='RemoveUser',
  full_name='ai.verta.uac.RemoveUser',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='org_id', full_name='ai.verta.uac.RemoveUser.org_id', index=0,
      number=1, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
    _descriptor.FieldDescriptor(
      name='share_with', full_name='ai.verta.uac.RemoveUser.share_with', index=1,
      number=2, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=_b("").decode('utf-8'),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR),
  ],
  extensions=[
  ],
  nested_types=[_REMOVEUSER_RESPONSE, ],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=1232,
  serialized_end=1308,
)

_GETORGANIZATIONBYID_RESPONSE.fields_by_name['organization'].message_type = _ORGANIZATION
_GETORGANIZATIONBYID_RESPONSE.containing_type = _GETORGANIZATIONBYID
_GETORGANIZATIONBYNAME_RESPONSE.fields_by_name['organization'].message_type = _ORGANIZATION
_GETORGANIZATIONBYNAME_RESPONSE.containing_type = _GETORGANIZATIONBYNAME
_GETORGANIZATIONBYSHORTNAME_RESPONSE.fields_by_name['organization'].message_type = _ORGANIZATION
_GETORGANIZATIONBYSHORTNAME_RESPONSE.containing_type = _GETORGANIZATIONBYSHORTNAME
_LISTMYORGANIZATIONS_RESPONSE.fields_by_name['organizations'].message_type = _ORGANIZATION
_LISTMYORGANIZATIONS_RESPONSE.containing_type = _LISTMYORGANIZATIONS
_SETORGANIZATION_RESPONSE.fields_by_name['organization'].message_type = _ORGANIZATION
_SETORGANIZATION_RESPONSE.containing_type = _SETORGANIZATION
_SETORGANIZATION.fields_by_name['organization'].message_type = _ORGANIZATION
_SETORGANIZATION.fields_by_name['global_collaborator_type'].enum_type = protos_dot_public_dot_common_dot_CommonService__pb2._COLLABORATORTYPEENUM_COLLABORATORTYPE
_SETORGANIZATION.fields_by_name['global_can_deploy'].enum_type = protos_dot_public_dot_common_dot_CommonService__pb2._TERNARYENUM_TERNARY
_DELETEORGANIZATION_RESPONSE.containing_type = _DELETEORGANIZATION
_LISTUSERS_RESPONSE.containing_type = _LISTUSERS
_LISTTEAMS_RESPONSE.containing_type = _LISTTEAMS
_ADDUSER_RESPONSE.containing_type = _ADDUSER
_REMOVEUSER_RESPONSE.containing_type = _REMOVEUSER
DESCRIPTOR.message_types_by_name['Organization'] = _ORGANIZATION
DESCRIPTOR.message_types_by_name['GetOrganizationById'] = _GETORGANIZATIONBYID
DESCRIPTOR.message_types_by_name['GetOrganizationByName'] = _GETORGANIZATIONBYNAME
DESCRIPTOR.message_types_by_name['GetOrganizationByShortName'] = _GETORGANIZATIONBYSHORTNAME
DESCRIPTOR.message_types_by_name['ListMyOrganizations'] = _LISTMYORGANIZATIONS
DESCRIPTOR.message_types_by_name['SetOrganization'] = _SETORGANIZATION
DESCRIPTOR.message_types_by_name['DeleteOrganization'] = _DELETEORGANIZATION
DESCRIPTOR.message_types_by_name['ListUsers'] = _LISTUSERS
DESCRIPTOR.message_types_by_name['ListTeams'] = _LISTTEAMS
DESCRIPTOR.message_types_by_name['AddUser'] = _ADDUSER
DESCRIPTOR.message_types_by_name['RemoveUser'] = _REMOVEUSER
_sym_db.RegisterFileDescriptor(DESCRIPTOR)

Organization = _reflection.GeneratedProtocolMessageType('Organization', (_message.Message,), dict(
  DESCRIPTOR = _ORGANIZATION,
  __module__ = 'protos.public.uac.Organization_pb2'
  # @@protoc_insertion_point(class_scope:ai.verta.uac.Organization)
  ))
_sym_db.RegisterMessage(Organization)

GetOrganizationById = _reflection.GeneratedProtocolMessageType('GetOrganizationById', (_message.Message,), dict(

  Response = _reflection.GeneratedProtocolMessageType('Response', (_message.Message,), dict(
    DESCRIPTOR = _GETORGANIZATIONBYID_RESPONSE,
    __module__ = 'protos.public.uac.Organization_pb2'
    # @@protoc_insertion_point(class_scope:ai.verta.uac.GetOrganizationById.Response)
    ))
  ,
  DESCRIPTOR = _GETORGANIZATIONBYID,
  __module__ = 'protos.public.uac.Organization_pb2'
  # @@protoc_insertion_point(class_scope:ai.verta.uac.GetOrganizationById)
  ))
_sym_db.RegisterMessage(GetOrganizationById)
_sym_db.RegisterMessage(GetOrganizationById.Response)

GetOrganizationByName = _reflection.GeneratedProtocolMessageType('GetOrganizationByName', (_message.Message,), dict(

  Response = _reflection.GeneratedProtocolMessageType('Response', (_message.Message,), dict(
    DESCRIPTOR = _GETORGANIZATIONBYNAME_RESPONSE,
    __module__ = 'protos.public.uac.Organization_pb2'
    # @@protoc_insertion_point(class_scope:ai.verta.uac.GetOrganizationByName.Response)
    ))
  ,
  DESCRIPTOR = _GETORGANIZATIONBYNAME,
  __module__ = 'protos.public.uac.Organization_pb2'
  # @@protoc_insertion_point(class_scope:ai.verta.uac.GetOrganizationByName)
  ))
_sym_db.RegisterMessage(GetOrganizationByName)
_sym_db.RegisterMessage(GetOrganizationByName.Response)

GetOrganizationByShortName = _reflection.GeneratedProtocolMessageType('GetOrganizationByShortName', (_message.Message,), dict(

  Response = _reflection.GeneratedProtocolMessageType('Response', (_message.Message,), dict(
    DESCRIPTOR = _GETORGANIZATIONBYSHORTNAME_RESPONSE,
    __module__ = 'protos.public.uac.Organization_pb2'
    # @@protoc_insertion_point(class_scope:ai.verta.uac.GetOrganizationByShortName.Response)
    ))
  ,
  DESCRIPTOR = _GETORGANIZATIONBYSHORTNAME,
  __module__ = 'protos.public.uac.Organization_pb2'
  # @@protoc_insertion_point(class_scope:ai.verta.uac.GetOrganizationByShortName)
  ))
_sym_db.RegisterMessage(GetOrganizationByShortName)
_sym_db.RegisterMessage(GetOrganizationByShortName.Response)

ListMyOrganizations = _reflection.GeneratedProtocolMessageType('ListMyOrganizations', (_message.Message,), dict(

  Response = _reflection.GeneratedProtocolMessageType('Response', (_message.Message,), dict(
    DESCRIPTOR = _LISTMYORGANIZATIONS_RESPONSE,
    __module__ = 'protos.public.uac.Organization_pb2'
    # @@protoc_insertion_point(class_scope:ai.verta.uac.ListMyOrganizations.Response)
    ))
  ,
  DESCRIPTOR = _LISTMYORGANIZATIONS,
  __module__ = 'protos.public.uac.Organization_pb2'
  # @@protoc_insertion_point(class_scope:ai.verta.uac.ListMyOrganizations)
  ))
_sym_db.RegisterMessage(ListMyOrganizations)
_sym_db.RegisterMessage(ListMyOrganizations.Response)

SetOrganization = _reflection.GeneratedProtocolMessageType('SetOrganization', (_message.Message,), dict(

  Response = _reflection.GeneratedProtocolMessageType('Response', (_message.Message,), dict(
    DESCRIPTOR = _SETORGANIZATION_RESPONSE,
    __module__ = 'protos.public.uac.Organization_pb2'
    # @@protoc_insertion_point(class_scope:ai.verta.uac.SetOrganization.Response)
    ))
  ,
  DESCRIPTOR = _SETORGANIZATION,
  __module__ = 'protos.public.uac.Organization_pb2'
  # @@protoc_insertion_point(class_scope:ai.verta.uac.SetOrganization)
  ))
_sym_db.RegisterMessage(SetOrganization)
_sym_db.RegisterMessage(SetOrganization.Response)

DeleteOrganization = _reflection.GeneratedProtocolMessageType('DeleteOrganization', (_message.Message,), dict(

  Response = _reflection.GeneratedProtocolMessageType('Response', (_message.Message,), dict(
    DESCRIPTOR = _DELETEORGANIZATION_RESPONSE,
    __module__ = 'protos.public.uac.Organization_pb2'
    # @@protoc_insertion_point(class_scope:ai.verta.uac.DeleteOrganization.Response)
    ))
  ,
  DESCRIPTOR = _DELETEORGANIZATION,
  __module__ = 'protos.public.uac.Organization_pb2'
  # @@protoc_insertion_point(class_scope:ai.verta.uac.DeleteOrganization)
  ))
_sym_db.RegisterMessage(DeleteOrganization)
_sym_db.RegisterMessage(DeleteOrganization.Response)

ListUsers = _reflection.GeneratedProtocolMessageType('ListUsers', (_message.Message,), dict(

  Response = _reflection.GeneratedProtocolMessageType('Response', (_message.Message,), dict(
    DESCRIPTOR = _LISTUSERS_RESPONSE,
    __module__ = 'protos.public.uac.Organization_pb2'
    # @@protoc_insertion_point(class_scope:ai.verta.uac.ListUsers.Response)
    ))
  ,
  DESCRIPTOR = _LISTUSERS,
  __module__ = 'protos.public.uac.Organization_pb2'
  # @@protoc_insertion_point(class_scope:ai.verta.uac.ListUsers)
  ))
_sym_db.RegisterMessage(ListUsers)
_sym_db.RegisterMessage(ListUsers.Response)

ListTeams = _reflection.GeneratedProtocolMessageType('ListTeams', (_message.Message,), dict(

  Response = _reflection.GeneratedProtocolMessageType('Response', (_message.Message,), dict(
    DESCRIPTOR = _LISTTEAMS_RESPONSE,
    __module__ = 'protos.public.uac.Organization_pb2'
    # @@protoc_insertion_point(class_scope:ai.verta.uac.ListTeams.Response)
    ))
  ,
  DESCRIPTOR = _LISTTEAMS,
  __module__ = 'protos.public.uac.Organization_pb2'
  # @@protoc_insertion_point(class_scope:ai.verta.uac.ListTeams)
  ))
_sym_db.RegisterMessage(ListTeams)
_sym_db.RegisterMessage(ListTeams.Response)

AddUser = _reflection.GeneratedProtocolMessageType('AddUser', (_message.Message,), dict(

  Response = _reflection.GeneratedProtocolMessageType('Response', (_message.Message,), dict(
    DESCRIPTOR = _ADDUSER_RESPONSE,
    __module__ = 'protos.public.uac.Organization_pb2'
    # @@protoc_insertion_point(class_scope:ai.verta.uac.AddUser.Response)
    ))
  ,
  DESCRIPTOR = _ADDUSER,
  __module__ = 'protos.public.uac.Organization_pb2'
  # @@protoc_insertion_point(class_scope:ai.verta.uac.AddUser)
  ))
_sym_db.RegisterMessage(AddUser)
_sym_db.RegisterMessage(AddUser.Response)

RemoveUser = _reflection.GeneratedProtocolMessageType('RemoveUser', (_message.Message,), dict(

  Response = _reflection.GeneratedProtocolMessageType('Response', (_message.Message,), dict(
    DESCRIPTOR = _REMOVEUSER_RESPONSE,
    __module__ = 'protos.public.uac.Organization_pb2'
    # @@protoc_insertion_point(class_scope:ai.verta.uac.RemoveUser.Response)
    ))
  ,
  DESCRIPTOR = _REMOVEUSER,
  __module__ = 'protos.public.uac.Organization_pb2'
  # @@protoc_insertion_point(class_scope:ai.verta.uac.RemoveUser)
  ))
_sym_db.RegisterMessage(RemoveUser)
_sym_db.RegisterMessage(RemoveUser.Response)


DESCRIPTOR._options = None

_ORGANIZATIONSERVICE = _descriptor.ServiceDescriptor(
  name='OrganizationService',
  full_name='ai.verta.uac.OrganizationService',
  file=DESCRIPTOR,
  index=0,
  serialized_options=None,
  serialized_start=1311,
  serialized_end=2682,
  methods=[
  _descriptor.MethodDescriptor(
    name='getOrganizationById',
    full_name='ai.verta.uac.OrganizationService.getOrganizationById',
    index=0,
    containing_service=None,
    input_type=_GETORGANIZATIONBYID,
    output_type=_GETORGANIZATIONBYID_RESPONSE,
    serialized_options=_b('\202\323\344\223\002&\022$/v1/organization/getOrganizationById'),
  ),
  _descriptor.MethodDescriptor(
    name='getOrganizationByName',
    full_name='ai.verta.uac.OrganizationService.getOrganizationByName',
    index=1,
    containing_service=None,
    input_type=_GETORGANIZATIONBYNAME,
    output_type=_GETORGANIZATIONBYNAME_RESPONSE,
    serialized_options=_b('\202\323\344\223\002(\022&/v1/organization/getOrganizationByName'),
  ),
  _descriptor.MethodDescriptor(
    name='getOrganizationByShortName',
    full_name='ai.verta.uac.OrganizationService.getOrganizationByShortName',
    index=2,
    containing_service=None,
    input_type=_GETORGANIZATIONBYSHORTNAME,
    output_type=_GETORGANIZATIONBYSHORTNAME_RESPONSE,
    serialized_options=_b('\202\323\344\223\002-\022+/v1/organization/getOrganizationByShortName'),
  ),
  _descriptor.MethodDescriptor(
    name='listMyOrganizations',
    full_name='ai.verta.uac.OrganizationService.listMyOrganizations',
    index=3,
    containing_service=None,
    input_type=_LISTMYORGANIZATIONS,
    output_type=_LISTMYORGANIZATIONS_RESPONSE,
    serialized_options=_b('\202\323\344\223\002&\022$/v1/organization/listMyOrganizations'),
  ),
  _descriptor.MethodDescriptor(
    name='setOrganization',
    full_name='ai.verta.uac.OrganizationService.setOrganization',
    index=4,
    containing_service=None,
    input_type=_SETORGANIZATION,
    output_type=_SETORGANIZATION_RESPONSE,
    serialized_options=_b('\202\323\344\223\002%\" /v1/organization/setOrganization:\001*'),
  ),
  _descriptor.MethodDescriptor(
    name='deleteOrganization',
    full_name='ai.verta.uac.OrganizationService.deleteOrganization',
    index=5,
    containing_service=None,
    input_type=_DELETEORGANIZATION,
    output_type=_DELETEORGANIZATION_RESPONSE,
    serialized_options=_b('\202\323\344\223\002(\"#/v1/organization/deleteOrganization:\001*'),
  ),
  _descriptor.MethodDescriptor(
    name='listTeams',
    full_name='ai.verta.uac.OrganizationService.listTeams',
    index=6,
    containing_service=None,
    input_type=_LISTTEAMS,
    output_type=_LISTTEAMS_RESPONSE,
    serialized_options=_b('\202\323\344\223\002\034\022\032/v1/organization/listTeams'),
  ),
  _descriptor.MethodDescriptor(
    name='listUsers',
    full_name='ai.verta.uac.OrganizationService.listUsers',
    index=7,
    containing_service=None,
    input_type=_LISTUSERS,
    output_type=_LISTUSERS_RESPONSE,
    serialized_options=_b('\202\323\344\223\002\034\022\032/v1/organization/listUsers'),
  ),
  _descriptor.MethodDescriptor(
    name='addUser',
    full_name='ai.verta.uac.OrganizationService.addUser',
    index=8,
    containing_service=None,
    input_type=_ADDUSER,
    output_type=_ADDUSER_RESPONSE,
    serialized_options=_b('\202\323\344\223\002\035\"\030/v1/organization/addUser:\001*'),
  ),
  _descriptor.MethodDescriptor(
    name='removeUser',
    full_name='ai.verta.uac.OrganizationService.removeUser',
    index=9,
    containing_service=None,
    input_type=_REMOVEUSER,
    output_type=_REMOVEUSER_RESPONSE,
    serialized_options=_b('\202\323\344\223\002 \"\033/v1/organization/removeUser:\001*'),
  ),
])
_sym_db.RegisterServiceDescriptor(_ORGANIZATIONSERVICE)

DESCRIPTOR.services_by_name['OrganizationService'] = _ORGANIZATIONSERVICE

# @@protoc_insertion_point(module_scope)
