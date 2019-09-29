#!/bin/bash

STRING=`ls -h audio/${1}`

if [ "$STRING" == "" ]; then

 rmdir  "./audio/${1}"

fi