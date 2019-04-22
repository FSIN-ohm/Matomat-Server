package org.fsin.matomat.rest;

import org.fsin.matomat.rest.exceptions.BadRequestException;

public class Utils {
    public static void checkRequest(Object object) throws BadRequestException {
        if(object == null) {
            throw new BadRequestException();
        }
    }
}
