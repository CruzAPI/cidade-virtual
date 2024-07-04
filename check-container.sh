#!/bin/bash

CONTAINER_NAME="cidade-virtual"

STATUS=$(docker inspect -f '{{.State.Running}}' $CONTAINER_NAME 2>/dev/null)

if [ $? -eq 0 ] && [ "$STATUS" == "true" ]; then
  echo "O container $CONTAINER_NAME está em execução."
  exit 0
else
  echo "O container $CONTAINER_NAME não está em execução."
  exit 1
fi