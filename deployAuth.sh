#!/bin/bash

SCRIPT_DIR=$(dirname "$(realpath "$0")")
USERNAME=cruzapi
HOSTNAME=192.168.1.34
CONTAINER_NAME=proxy

AUTH_PATH="$SCRIPT_DIR/authenticator/build/libs/authenticator-1.0-SNAPSHOT-all.jar"

TARGET_DIR="~/cidade-virtual/proxy/plugins"

#Build plugin
./gradlew authenticator:clean authenticator:build || exit 1

#Stop remote container gracefully
ssh -t $USERNAME@$HOSTNAME /bin/bash -ic "stop\ $CONTAINER_NAME"

#Wait remote container to stop
while ssh -t "$USERNAME"@"$HOSTNAME" /bin/bash -ic "check-container\ $CONTAINER_NAME"; do
    echo "Waiting \"$CONTAINER_NAME\" container to stop..."
    sleep 1
done

echo "\"$CONTAINER_NAME\" container stopped!"

#Copy plugin to remote
scp "$AUTH_PATH" "$USERNAME@$HOSTNAME:$TARGET_DIR"

#Start remote container
if ssh -t "$USERNAME@$HOSTNAME" /bin/bash -ic "docker\ start\ $CONTAINER_NAME"; then
  echo "\"$CONTAINER_NAME\" container has been started!"
else
  echo "Failed to start \"$CONTAINER_NAME\" container!"
fi
