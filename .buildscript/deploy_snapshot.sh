#!/bin/bash
#
# Deploy a jar, source jar, and javadoc jar to Sonatype's snapshot repo.
#
# Adapted from https://coderwall.com/p/9b_lfq and
# https://benlimmer.com/2013/12/26/automatically-publish-javadoc-to-gh-pages-with-travis-ci/

SLUG="ReactiveCircus/FlowBinding"
BRANCH="master"

set -e

if [ "$CIRCLE_PROJECT_USERNAME/$CIRCLE_PROJECT_REPONAME" != "$SLUG" ]; then
  echo "Skipping snapshot deployment: wrong repository. Expected '$SLUG' but was '$CIRCLE_PROJECT_USERNAME/$CIRCLE_PROJECT_REPONAME'."
elif [ "$CIRCLE_BRANCH" != "$BRANCH" ]; then
  echo "Skipping snapshot deployment: wrong branch. Expected '$BRANCH' but was '$CIRCLE_BRANCH'."
elif [ ! -z "$CIRCLE_PULL_REQUEST" ]; then
  echo "Skipping snapshot deployment: was pull request."
else
  echo "Deploying snapshot..."
  ./gradlew clean androidSourcesJar androidJavadocsJar uploadArchives --no-daemon --no-parallel
  echo "Snapshot deployed!"
fi
