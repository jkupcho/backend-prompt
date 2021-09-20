#!/usr/bin/env bash

docker build --target rest-api -t vino-api:1.0.0 .
docker build --target listener -t vino-listener:1.0.0 .