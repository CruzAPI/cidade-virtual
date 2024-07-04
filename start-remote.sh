#!/bin/bash
ssh -t "$1"@"$2" /bin/bash -ic "docker start cidade-virtual-server"