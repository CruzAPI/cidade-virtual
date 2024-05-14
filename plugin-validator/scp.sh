#!/bin/bash
echo "Running plugin-validator/scp.sh..."
scp ./build/libs/*.jar cruzapi@192.168.1.35:~/cidade-virtual/server/plugins