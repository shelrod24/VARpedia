#!/bin/bash

#The first argument ${1} is the path to the audio


aplay "${1}"


exit $?
