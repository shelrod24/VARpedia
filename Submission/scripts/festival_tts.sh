#!/bin/bash
# First input is the voice to be used (kal_diphone akl_nz_jdt_diphone akl_nz_cw_cg_cg)
# Second input is the text to be parsed (eg "excuse me what the fu")


output=`echo "(voice_${1}) (SayText \"${2}\")" | festival 2>&1 | grep ERROR`

if [ ! -z "$output" ] ; then
	exit 1
fi 
exit 0

