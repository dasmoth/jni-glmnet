#!/bin/sh

ant
export CLASSPATH=glmnet.jar
javah -jni glmnet.GLMNet
gcc -I${JAVA_HOME}/include -I${JAVA_HOME}/include/linux -c glmnet.c
gcc -c newGLMnet_f.f90  -fdefault-real-8 -ffixed-form -fPIC
# gcc -dynamiclib -o libglmnet.jnilib -lgfortran -lm -lquadmath glmnet.o newGLMnet_f.o
gcc -shared -o libglmnet.so glmnet.o newGLMnet_f.o -lm -lgfortran
