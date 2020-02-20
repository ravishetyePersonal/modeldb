#!/usr/bin/env python3

import functools
import json
import os
import sys
import collections

FieldType = collections.namedtuple('FieldType', ['safe_name', 'name', 'type'])

def main():
    if len(sys.argv) != 2:
        print("Usage: %s file" % sys.argv[0])
        sys.exit(1)

    filename = sys.argv[1]

    with open(filename) as f:
        content = json.load(f)

    def basedirReducer(init, val):
        if val[1] != 'protos':
            return init
        return val[0]

    protos_index = functools.reduce(basedirReducer, enumerate(filename.split('/')), None)
    basedir = filename.split('/')[protos_index+1:-1]
    api_name = filename.split('/')[-1].split('.')[0]
    basedir[0] = "_"+basedir[0]
    result_dir = os.path.join('src/main/scala/ai/verta/swagger', *basedir)
    result_package = 'ai.verta.swagger.' + '.'.join(basedir)
    # print(result_dir)
    # print(result_package)

    create_models(result_dir, result_package, content)
    create_api(result_dir, result_package, api_name, content)

def create_api(result_dir, result_package, api_name, content):
    if 'paths' not in content:
        return

    paths = []
    operations = []
    for path, p_content in content['paths'].items():
        for op, op_content in p_content.items():
            operation_id = op_content['operationId']
            operations.append((operation_id, path+"", op+""))
    operations = sorted(operations, key=lambda x: x[0])

    for (operation_id, path, op) in operations:
            # while operation_id[-1].isnumeric():
            #     operation_id = operation_id[:-1]
            op_content = content['paths'][path][op]
            parameters = []
            query = []
            path_components = []
            body = []
            got_body_parameter = False
            body_type = 'Any'
            # print(op_content)

            for param_def in op_content.get('parameters', []):
                name = param_def['name']
                # print(name)
                safe_name = to_camel_case(name.replace('.', '_'))
                parameters.append(FieldType(safe_name, name, resolve_type(param_def)))

                if param_def.get('required', False):
                    body += ['if (%(param)s == null) throw new Exception("Missing required parameter \\\"%(param)s\\\"")' % {'param': safe_name}]

                if param_def['in'] == 'body':
                    got_body_parameter = True
                    body_type = resolve_type(param_def)
                    # body += ['bodyString = write(body)']
                elif param_def['in'] == 'query':
                    query.append(FieldType(safe_name, name, resolve_type(param_def)))
                elif param_def['in'] == 'path':
                    path = path.replace('{%s}' % name, "$%s" % safe_name)
                    path_components.append(FieldType(safe_name, name, resolve_type(param_def)))
                else:
                    # print(path)
                    raise ValueError(param_def['in'])

            if not got_body_parameter:
                body += ['val body: Any = null']

            # if operation_id == 'ComputeRepositoryDiff':
            #     asdffasdfsdf

            success_response = None
            code_mapping = {}
            for code, definition in op_content['responses'].items():
                if code == "200":
                    success_response = resolve_type(definition)
                else:
                    code_mapping[code] = resolve_type(definition)

            # print(code_mapping)

            operation_content = '''
  def %(operation_id)sAsync(%(parameters)s)(implicit ec: ExecutionContext): Future[Try[%(return_type)s]] = {
    val __query = Map[String,String](
      %(query)s
    )
    %(body)s
    return client.request[%(body_type)s, %(return_type)s]("%(op)s", basePath + s"%(path)s", __query, body)
  }

  def %(operation_id)s(%(parameters)s)(implicit ec: ExecutionContext): Try[%(return_type)s] = Await.result(%(operation_id)sAsync(%(parameter_names)s), Duration.Inf)
''' % {
    'operation_id': operation_id,
    'parameters': ', '.join([p.safe_name + ': ' + p.type for p in parameters]),
    'return_type': success_response,
    'parameter_names': ', '.join([p.safe_name for p in parameters]),
    'body': '\n    '.join(body),
    'query': ',\n      '.join(['"%s" -> client.toQuery(%s)' % (q.name, q.safe_name) for q in query]),
    'op': op.upper(),
    'path': path,
    'body_type': body_type,
    }

            paths.append(operation_content)

    api_content = '''
// THIS FILE IS AUTO-GENERATED. DO NOT EDIT
package %(package)s.api

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration
import scala.util.Try

import ai.verta.swagger.client.Client
import %(package)s.model._

class %(api)sApi(client: Client, val basePath: String = "%(base_path)s") {
%(body)s
}
''' % {
    'package': result_package,
    'api': api_name,
    'base_path': content.get('basePath', ''),
    'body': '\n'.join(paths),
}
    # print(api_content)

    os.makedirs(os.path.join(result_dir, 'api'), exist_ok=True)
    with open(os.path.join(result_dir, 'api', api_name+'Api.scala'), 'w') as f:
        f.write(api_content)

def keyword_safe(s):
    if s in ['type']:
        return '`%s`' % s
    return s


def create_models(result_dir, result_package, content):
    enums = []
    for k, v in content['definitions'].items():
        if 'enum' in v:
            enums.append(capitalize_first(k))
    for k, v in content['definitions'].items():
        create_model(result_dir, result_package, k, v, enums)

def create_model(result_dir, result_package, definition_name, definition, enums):
    capitalized_definition_name = capitalize_first(definition_name)
    filename = os.path.join(result_dir, 'model', capitalized_definition_name+'.scala')
    os.makedirs(os.path.join(result_dir, 'model'), exist_ok=True)

    content = ["// THIS FILE IS AUTO-GENERATED. DO NOT EDIT","package " + result_package + ".model\n"]

    if definition['type'] == 'object':
        properties = definition.get('properties', dict())
        # print(properties)
        properties = {to_camel_case(k): resolve_type(v) for k, v in properties.items()}

        # TODO
        if 'required' in definition:
            raise ValueError("")

        if enums:
            for e in enums:
                content += ['import %s.model.%s._' % (result_package, e)]
            content += ['']

        properties = {k: 'Option[' + v + ']' for k, v in properties.items()}

        content += ["case class " + capitalized_definition_name + " ("]
        content += ["  " + ',\n  '.join([keyword_safe(k) + ": " + v for k, v in properties.items()])]
        content += [")"]
        content = '\n'.join(content) + '\n'

        with open(filename, 'w') as f:
            f.write(content)
    elif 'enum' in definition:
        content += ['object %s extends Enumeration {' % capitalized_definition_name]
        content += ['  type %s = Value' % capitalized_definition_name]
        for e in definition['enum']:
            content += ['  val %s = Value("%s")' % (e, e)]

        content += ['}']
        content = '\n'.join(content) + '\n'

        with open(filename, 'w') as f:
            f.write(content)
    else:
        raise ValueError(definition['type'])

def resolve_type(typedef):
    # print(typedef)

    if '$ref' in typedef:
        ref = typedef['$ref'].split('/')[-1]
        return capitalize_first(ref)

    if 'schema' in typedef:
        return resolve_type(typedef['schema'])

    if typedef['type'] == 'string':
        return 'String'
    elif typedef['type'] == 'boolean':
        return 'Boolean'
    elif typedef['type'] == 'integer':
        if typedef['format'] == 'int32':
            return 'Integer'
        else:
            raise ValueError(typedef['format'])
    elif typedef['type'] == 'number':
        if typedef['format'] == 'double':
            return 'Double'
        else:
            raise ValueError(typedef['format'])
    elif typedef['type'] == 'array':
        return 'List[' + resolve_type(typedef['items']) + ']'
    elif typedef['type'] == 'object' and len(typedef) == 1:
        return 'Any'
    elif typedef['type'] == 'object' and 'additionalProperties' in typedef and len(typedef) == 2:
        return 'Map[String,%s]' % resolve_type(typedef['additionalProperties'])
    else:
        raise ValueError(typedef['type'])

def capitalize_first(s):
    return s[0].upper() + s[1:]

def to_camel_case(snake_str):
    components = snake_str.split('_')
    # We capitalize the first letter of each component except the first one
    # with the 'title' method and join them together.
    return components[0] + ''.join(x.title() for x in components[1:])

if __name__ == "__main__":
    main()
