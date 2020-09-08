#!/bin/bash

NDK=C\:\\Develop\\Android\\android-ndk-r20b

function ndk_stack_logcat() {
    adb logcat | $NDK/ndk-stack -sym build/intermediates/cmake/debug/obj/armeabi-v7a
}

ndk_stack_logcat