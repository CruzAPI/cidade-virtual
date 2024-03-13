#!/bin/bash

shopt -s extglob

BASE_DIR="$( dirname "$BASH_SOURCE"; )"
COMMONS_DIR="$BASE_DIR/commons"
PLUGIN_DIR="$BASE_DIR/plugin"

### FLAGS ###

flags=""

for flag in "$@"; do
  case $flag in
  -+([a-zA-Z])) flags="${flags}${flag:1}" ;;
  esac
done

commons=$( [[ $flags =~ "c" ]]; echo $? )

### BUILD ###
cd "$BASE_DIR"

#pwd
#if [[ $commons -eq 0 ]] ; then
#  ./gradlew clean build || exit
#fi

./gradlew clean :plugin:build || exit

### DEPLOY ###

PLUGIN_PATH="$PLUGIN_DIR/build/libs/*.jar"
COMMONS_PATH="$COMMONS_DIR/build/libs/*.jar"

echo "Deploying in remote..."
USER="cruzapi"
HOSTNAME="192.168.1.35"
scp $PLUGIN_PATH $USER@$HOSTNAME:~/cidade-virtual/server/plugins || return
scp $COMMONS_PATH $USER@$HOSTNAME:~/cidade-virtual/server/plugins || return
ssh -t $USER@$HOSTNAME /bin/bash -ic "stop\ cidade-virtual-server"

###############

cd "$BASE_DIR"
