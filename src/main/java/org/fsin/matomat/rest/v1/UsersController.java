package org.fsin.matomat.rest.v1;

import org.fsin.matomat.rest.model.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersController {
    @RequestMapping("/v1/users")
    public User[] users(@RequestParam(value="count", defaultValue="-1") int count,
                        @RequestParam(value="page", defaultValue="0") int page) {
        return new User[]{};
    }

}
