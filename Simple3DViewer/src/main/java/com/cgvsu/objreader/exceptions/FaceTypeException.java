package com.cgvsu.objreader.exceptions;

public class FaceTypeException extends ObjReaderException {
    public FaceTypeException(int lineIndex) {
        super("Several argument types in one polygon.", lineIndex);
    }
}
