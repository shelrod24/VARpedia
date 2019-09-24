#!/bin/bash

# Converts text given to an audio file
# First input will be the name of the audio file without extension (eg chunk)
# Second input will be tbe tts string (eg "I cant find my string")
# Output will be put in a folder named audio
# If output filename already exists will overwrite
# Author: Leon Chin (lchi184)

AUDIO_DIR="./temps/audio"
mkdir -p $AUDIO_DIR

# delete file if file already exists
if [ -e "${AUDIO_DIR}/${1}.wav" ] ; then
	rm -f "${AUDIO_DIR}/${1}.wav"
fi

# make audio wav file, then convert to mp3
echo "${2}" | text2wave -o "${AUDIO_DIR}/${1}.wav" &> /dev/null
