# THIS FILE IS AUTO-GENERATED. DO NOT EDIT

{{#__object_flag}}
class {{class_name}}(dict):
  def __init__(self, {{#properties}}{{name}}=None{{^last}}, {{/last}}{{/properties}}):
    {{#properties}}
    {{#required}}
    if {{name}} is None:
      raise ValueError('attribute {{name}} is required')
    {{/required}}
    self.{{name}} = {{name}}
    {{/properties}}

  def __setattr__(self, name, value):
    self[name] = value

  def __delattr__(self, name):
    del self[name]

  def __getattr__(self, name):
    if name in self:
      return self[name]
    else:
      raise AttributeError
{{/__object_flag}}
{{#__enum_flag}}
from enum import Enum

class {{class_name}}(Enum):
  {{#enum_values}}
  {{name}} = "{{name}}"
  {{/enum_values}}
{{/__enum_flag}}
