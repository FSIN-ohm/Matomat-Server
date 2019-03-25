package org.fsin.matomat.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> hello() {
        return new ResponseEntity<String>("<html>This is the Matohmat REST intorface. "
                + "Please read in the <a href=\"https://fsin-ohm.github.io/Matomat-Documentation/\">Documentation</a> "+
                "how to use this.</html>", HttpStatus.OK);
    }
}
