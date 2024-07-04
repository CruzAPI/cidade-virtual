#!/bin/bash
printf '\nstop\n' | socat EXEC:"docker attach cidade-virtual",pty STDIN