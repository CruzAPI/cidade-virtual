#!/bin/bash
echo "Stopping server..."
ssh -t cruzapi@192.168.1.35 /bin/bash -ic "stop\ cidade-virtual-server"