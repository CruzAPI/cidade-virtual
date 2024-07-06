#!/bin/bash
ssh -t "$1"@"$2" /bin/bash -ic "reset-towns"