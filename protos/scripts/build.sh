#!/bin/bash
set -e
make gw
make code
cp -R ../gen/python/protos ../../client/verta/verta/_protos