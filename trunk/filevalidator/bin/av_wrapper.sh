#! /bin/sh

#
# av_wrapper
# Wraps ClamAV software for FUV runs.
# 
# The script receives one argument of the file path to check and return 0 if no virus was found.

filepath=$1

# use $CLAMAV_PATH if explicitly defined and "clamscan" if not.
command="${CLAMAV_PATH-clamscan} $filepath"

output=`$command`

exit $?

	 