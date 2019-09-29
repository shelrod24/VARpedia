#!/bin/bash
# First input is the name of the file to be made
# Second input is the name of the folder to save the audio file
# Third input is the voice to be used (kal_diphone akl_nz_jdt_diphone akl_nz_cw_cg_cg)
# Fourth input is the text to be parsed (eg "excuse me what the fu")


AUDIO_DIR="./audio/${2}"

mkdir -p "${AUDIO_DIR}"

cd "${AUDIO_DIR}"

output=`echo "(voice_${3}) (set! chunk (Utterance Text \"${4}\")) (utt.save.wave (utt.synth chunk) \"${1}.wav\" 'riff)" | festival 2>&1 | grep ERROR`

if [ ! -z "$output" ] ; then
	exit 1
fi 
exit 0
