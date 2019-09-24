#!/bin/bash
# First input is the name of the file to be made
# Second input is the name of the folder to save the audio file
# Third input is the voice to be used (kal_diphone akl_nz_jdt_diphone akl_nz_cw_cg_cg)
# Fourth input is the text to be parsed (eg "excuse me what the fu")

FESTIVAL_DIR="./temps/festival"
AUDIO_DIR="./audio/${2}"

mkdir -p ${FESTIVAL_DIR}
mkdir -p "${AUDIO_DIR}"

cd ${FESTIVAL_DIR}

# will overwrite
echo "(voice_${3})" > chunk.scm
echo "(utt.save.wave (SayText \"${4}\") \"${1}.wav\" \`riff)" >> chunk.scm

# run scm file made
festival -b chunk.scm

cd ..
cd ..

mv "${FESTIVAL_DIR}/${1}.wav" "${AUDIO_DIR}/${1}.wav"

