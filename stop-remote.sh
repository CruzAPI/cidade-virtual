#!/bin/bash
ssh -t "$1"@"$2" /bin/bash -ic "stop\ $3"