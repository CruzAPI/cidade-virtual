#!/bin/bash
echo "Stopping server..."
ssh -t cruzapi@45.233.112.53 /bin/bash -ic "stop\ cidade-virtual-server"