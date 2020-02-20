# THIS FILE IS AUTO-GENERATED. DO NOT EDIT

class {{api_name}}Api:
  def __init__(self, client, base_path = "{{base_path}}"):
    self.client = client
    self.base_path = base_path
{{#operations}}

  def {{operation_id}}(self, {{#parameters}}{{safe_name}}=None{{^last}}, {{/last}}{{/parameters}}):
    __query = {
      {{#query}}
      "{{name}}": client.to_query({{safe_name}}){{^last}},{{/last}}
      {{/query}}
    }
    {{#required}}
    if {{safe_name}} is None:
      raise Exception("Missing required parameter \"{{safe_name}}\"")
    {{/required}}
    {{^body_present}}
    body = None
    {{/body_present}}
    return self.client.request("{{op}}", self.base_path + s"{{path}}", __query, body)
{{/operations}}
