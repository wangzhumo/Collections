#!/bin/bash

NDK=/Users/wangzhumo/Library/Android/sdk/ndk-bundle

function ndk_stack_logcat() {
    adb logcat | $NDK/ndk-stack -sym build/intermediates/cmake/debug/obj/armeabi-v7a
}

ndk_stack_logcat