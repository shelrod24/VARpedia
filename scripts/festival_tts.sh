#!/bin/bash
# First input is the voice to be used (kal_diphone akl_nz_jdt_diphone akl_nz_cw_cg_cg)
# Second input is the text to be parsed (eg "excuse me what the fu")

FESTIVAL_DIR="./temps/festival"
mkdir -p ${FESTIVAL_DIR}
mkdir -p ${AUDIO_DIR}

cd ${FESTIVAL_DIR}

# will overwrite
echo "(voice_${1})" > sample.scm
echo "(SayText \"${3}\")" >> sample.scm

# run scm file made
festival -b sample.scm



