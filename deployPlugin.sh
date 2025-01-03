#!/bin/bash

SCRIPT_DIR=$(dirname "$(realpath "$0")")
USERNAME=cruzapi
HOSTNAME=192.168.1.34
CONTAINER_NAME=cidade-virtual

PLUGIN_PATH="$SCRIPT_DIR/plugin/build/libs/plugin-1.0-SNAPSHOT-dev-all.jar"

TARGET_DIR="~/cidade-virtual/server/plugins"

#Build plugin
./gradlew plugin:clean plugin:build || exit 1

#Stop remote container gracefully
ssh -t $USERNAME@$HOSTNAME /bin/bash -ic "stop\ $CONTAINER_NAME"

#Wait remote container to stop
while ssh -t "$USERNAME"@"$HOSTNAME" /bin/bash -ic "check-container\ $CONTAINER_NAME"; do
    echo "Waiting \"$CONTAINER_NAME\" container to stop..."
    sleep 1
done

echo "\"$CONTAINER_NAME\" container stopped!"

#Copy plugin to remote
scp "$PLUGIN_PATH" "$USERNAME@$HOSTNAME:$TARGET_DIR"

#Start remote container
if ssh -t "$USERNAME@$HOSTNAME" /bin/bash -ic "docker\ start\ $CONTAINER_NAME"; then
  echo "\"$CONTAINER_NAME\" container has been started!"
else
  echo "Failed to start \"$CONTAINER_NAME\" container!"
fi
