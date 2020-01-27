#!/bin/bash
set -e
rm -rf tmp
rm -rf ../output/go/*
make gw
make code
