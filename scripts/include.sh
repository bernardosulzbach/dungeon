#!/usr/bin/env bash

# The -e option will cause a bash script to exit immediately when a command fails. This is generally a vast improvement upon the default behavior where the script just ignores the failing command and continues with the next line
set -e

# Using -e without -E will cause an ERR trap to not fire in certain scenarios.
set -E

# The bash shell normally only looks at the exit code of the last command of a pipeline. This behavior is not ideal as it causes the -e option to only be able to act on the exit code of a pipeline's last command.
# This is where -o pipefail comes in. This particular option sets the exit code of a pipeline to that of the rightmost command to exit with a non-zero status, or to zero if all commands of the pipeline exit successfully.
set -o pipefail

# This option causes the bash shell to treat unset variables as an error and exit immediately.
set -u
