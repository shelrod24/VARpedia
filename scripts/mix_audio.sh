#!/bin/bash

# Mixes the TEMP audio in ./temp/final with the specified music
# If no or empty input, will just rename the TEMP.mp3 to TEMPFINAL.mp3
# First input is the filename of the music to be mixed with TEMP.mp3

FINAL_DIR="./temps/final"
MUSIC_DIR="./music"
mkdir -p ${FINAL_DIR}
mkdir -p ${MUSIC_DIR}

if [ -z $1 ] ; then
	cp -f "${FINAL_DIR}/TEMP.mp3" "${FINAL_DIR}/TEMPFINAL.mp3"
else
	ffmpeg -y -i "${FINAL_DIR}/TEMP.mp3" -i "${MUSIC_DIR}/${1}" -filter_complex amix=inputs=2:duration=first:dropout_transition=1 "${FINAL_DIR}/TEMPFINAL.mp3"
fi
