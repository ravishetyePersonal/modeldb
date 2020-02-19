#!/bin/bash

set -e

BASE="../../protos/gen/swagger/protos"

rm -rf src/main/scala/ai/verta/swagger/_public

for f in $(find $BASE -type f | sort)
do
    echo "Processing $f"
    package=$(echo $f | sed "s,^$BASE/,," | xargs dirname | sed 's,/,.,g')
    echo "Package is $package"
    api_package="ai.verta.swagger.$package.api"
    model_package="ai.verta.swagger.$package.model"
    # swagger-codegen generate -l scala -o scala_swagger -i $f --api-package $api_package --model-package $model_package --model-name-prefix "" --template-dir $PWD/swagger_template --template-engine mustache > /dev/null
    ./swagger_codegen.py $f

    echo ""
    # break
done

# echo "Fixing variable names"
# # For some reason, the generation script mixes up capitalization of the first letter
# for basepackage in $(find $BASE -type f | sort | sed "s,^$BASE/,," | xargs -n 1 dirname | sed 's,/,.,g' | sed 's,\.,/,g' | xargs -n 1 basename | sort | uniq) runtime protobuf
# do
#     echo "Package $basepackage"
#     upperbasepackage="$(echo ${basepackage:0:1} | tr  '[a-z]' '[A-Z]')${basepackage:1}"
#     for f2 in $(find scala_swagger/src/main/scala/ai/verta -type f)
#     do
#         sed -E "s,$basepackage([a-zA-Z0-9]+),$upperbasepackage\1,g" $f2 > /tmp/foo.txt
#         mv /tmp/foo.txt $f2
#     done
# done

# echo "Fixing param matching"
# for f in $(find scala_swagger/src/main/scala/ai/verta/swagger -type f)
# do
#     sed 's,^\( *\)case Some(param) => queryParams += "path" -> param.toString$,\1case param => queryParams += "path" -> param.toString,' $f > /tmp/foo.txt
#     mv /tmp/foo.txt $f
# done

# for f in $(find scala_swagger/src -type f)
# do
#     new_f=$(echo $f | sed 's,^scala_swagger/,,')
#     mkdir -p $(dirname $new_f)
#     cp $f $new_f
# done

# rm -rf scala_swagger
