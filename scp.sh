#!/bin/bash

echo "ECHO"
echo "$1" "$2"@"$3":"$4"

scp "$1" "$2"@"$3":"$4"