#!/bin/bash

#The first argument ${1} is the audio to be previewed
#The second argumant ${2} is the subfolder with within which the audio is in 


SUBFOLDER=${2}
AUDIO="${1}"

aplay "./audio/${SUBFOLDER}/${AUDIO}"


exit $?
