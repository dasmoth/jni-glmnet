#!/bin/sh

ant
export CLASSPATH=glmnet.jar
javah -jni glmnet.GLMNet
gcc -c -I/System/Library/Frameworks/JavaVM.framework/Headers glmnet.c
gcc -c newGLMnet_f.f90  -m64 -fdefault-real-8 -ffixed-form -fPIC
# gcc -dynamiclib -o libglmnet.jnilib -lgfortran -lm -lquadmath glmnet.o newGLMnet_f.o
gcc -dynamiclib -o libglmnet.jnilib glmnet.o newGLMnet_f.o -lm -static-libgcc /usr/local/lib/libgfortran.a /usr/local/lib/libquadmath.a
