package org.fsin.matomat.rest;

import org.fsin.matomat.rest.exceptions.BadRequestException;

public class Utils {
    public static void checkIfNotNull(Object object) throws BadRequestException {
        if(object == null) {
            throw new BadRequestException();
        }
    }

    public static void checkIfBelowOne(int i) throws BadRequestException {
        System.err.println(i);
        if(i < 1) {
            throw new BadRequestException();
        }
    }

    public static void checkIfBelowZero(int i) throws BadRequestException {
        System.err.println(i);
        if(i < 0) {
            throw new BadRequestException();
        }
    }
}
