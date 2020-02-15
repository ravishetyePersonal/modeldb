#!/bin/bash
set -e

GO_OUTPUT="../gen/go/"
PYTHON_OUTPUT="../gen/python/"

GATEWAY_FLAGS="-I.. -I/usr/local/include -I${GOPATH}/src/github.com/grpc-ecosystem/grpc-gateway/third_party/googleapis"
PYTHON_GRPC_FLAGS="--grpc_python_out=$PYTHON_OUTPUT --python_out=$PYTHON_OUTPUT"



for protodir in $(find ../protos -name '*.proto' | xargs -n 1 dirname | sort | uniq)
do
    echo "Building $protodir for python"
    python3 -m grpc_tools.protoc $PYTHON_GRPC_FLAGS $GATEWAY_FLAGS $protodir/*.proto

    echo "Building $protodir for golang"
    protoc $GATEWAY_FLAGS \
		--go_out=plugins=grpc,paths=source_relative:$GO_OUTPUT \
		--grpc-gateway_out=logtostderr=true,paths=source_relative,allow_delete_body=true:$GO_OUTPUT \
		--swagger_out=logtostderr=true,allow_delete_body=true:$GO_OUTPUT \
		$protodir/*.proto
    echo ''
done

echo "Fixing python folders"
for pythondir in $(find $PYTHON_OUTPUT -type d)
do
    touch $pythondir/__init__.py
    for filename in ${pythondir}/*_pb2*; do
        if [ -e $filename ]
        then
            sed -i '' 's|^from protos\.|from ...|g' $filename
        fi
    done
done

cp -R $PYTHON_OUTPUT/protos/ ../../client/verta/verta/_protos
