#!/bin/bash
ssh -t "$1"@"$2" /bin/bash -ic "stop\ cidade-virtual-server"