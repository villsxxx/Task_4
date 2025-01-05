package com.cgvsu.objreader.exceptions;

public class ArgumentsException extends ObjReaderException {
    public ArgumentsException(ArgumentsErrorType errorType, int lineIndex) {
        super(errorType.getTextValue() + " arguments.", lineIndex);
    }
}
