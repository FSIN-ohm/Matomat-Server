package org.fsin.matomat.rest.v1;

import org.fsin.matomat.rest.model.Admin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminsController {

    @RequestMapping("/v1/admins")
    public Admin[] admins(@RequestParam(value="count", defaultValue="-1") int count,
                          @RequestParam(value="page", defaultValue="0") int page) {
        return new Admin[]{};
    }

}
