#!/bin/bash

# Copies inputted images into temp folder with a specifix filename format
# Depending on the order, image filenames will be comverted to the format image-###.jpg
# First input is the list of images to be converted, in order, inside the directory image with extensions (eg "porche1.jpg porche2.jpg porche3.jpg")
# Author: Leon Chin (lchi184)

IMAGE_DIR="./temps/image"
mkdir -p ${IMAGE_DIR}

counter=0
for image in ${1} ; do
	# formats counter so that it has leading zeros
	# if we have more than 1000 images we are fucked
	counter_format=`printf "%03d" $counter`
	# copy images over to temps with the specified format
	cp "${IMAGE_DIR}/${image}" "${IMAGE_DIR}/image-${counter_format}.jpg"
	let counter++
done