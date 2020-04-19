#!/bin/bash

set -e

REPO="git@github.com:ReactiveCircus/FlowBinding.git"
REMOTE_NAME="origin"
DIR=temp-clone

if [ -n "${CI}" ]; then
  REPO="https://github.com/${GITHUB_REPOSITORY}.git"
fi

# Clone project into a temp directory
rm -rf $DIR
git clone "$REPO" $DIR
cd $DIR

# Generate API docs
./gradlew dokka

# Copy *.md files into docs directory
cp README.md docs/index.md
mkdir -p docs/flowbinding-android && cp flowbinding-android/README.md docs/flowbinding-android/index.md
mkdir -p docs/flowbinding-activity && cp flowbinding-activity/README.md docs/flowbinding-activity/index.md
mkdir -p docs/flowbinding-appcompat && cp flowbinding-appcompat/README.md docs/flowbinding-appcompat/index.md
mkdir -p docs/flowbinding-core && cp flowbinding-core/README.md docs/flowbinding-core/index.md
mkdir -p docs/flowbinding-drawerlayout && cp flowbinding-drawerlayout/README.md docs/flowbinding-drawerlayout/index.md
mkdir -p docs/flowbinding-lifecycle && cp flowbinding-lifecycle/README.md docs/flowbinding-lifecycle/index.md
mkdir -p docs/flowbinding-material && cp flowbinding-material/README.md docs/flowbinding-material/index.md
mkdir -p docs/flowbinding-navigation && cp flowbinding-navigation/README.md docs/flowbinding-navigation/index.md
mkdir -p docs/flowbinding-preference && cp flowbinding-preference/README.md docs/flowbinding-preference/index.md
mkdir -p docs/flowbinding-recyclerview && cp flowbinding-recyclerview/README.md docs/flowbinding-recyclerview/index.md
mkdir -p docs/flowbinding-swiperefreshlayout && cp flowbinding-swiperefreshlayout/README.md docs/flowbinding-swiperefreshlayout/index.md
mkdir -p docs/flowbinding-viewpager && cp flowbinding-viewpager/README.md docs/flowbinding-viewpager/index.md
mkdir -p docs/flowbinding-viewpager2 && cp flowbinding-viewpager2/README.md docs/flowbinding-viewpager2/index.md
cp CHANGELOG.md docs/changelog.md

# If on CI, configure git remote with access token
if [ -n "${CI}" ]; then
  REMOTE_NAME="https://x-access-token:${DEPLOY_TOKEN}@github.com/${GITHUB_REPOSITORY}.git"
  git config --global user.name "${GITHUB_ACTOR}"
  git config --global user.email "${GITHUB_ACTOR}@users.noreply.github.com"
  git remote add deploy "$REMOTE_NAME"
  git fetch deploy && git fetch deploy gh-pages:gh-pages
fi

# Build the website and deploy to GitHub Pages
mkdocs gh-deploy --remote-name "$REMOTE_NAME"

# Delete temp directory
cd ..
rm -rf $DIR
