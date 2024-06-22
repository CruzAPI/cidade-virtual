#!/bin/bash
echo "Stopping server..."
ssh -t cruzapi@192.168.1.36 /bin/bash -ic "stop\ cidade-virtual-server"