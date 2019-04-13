package org.fsin.matomat.rest.v1;

import org.fsin.matomat.database.Database;
import org.fsin.matomat.database.model.UserEntry;
import org.fsin.matomat.rest.exceptions.AlreadyExistsException;
import org.fsin.matomat.rest.exceptions.ResourceNotFoundException;
import org.fsin.matomat.rest.model.*;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.rmi.AlreadyBoundException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@RestController
public class UsersController {

    private User mapEntryToUser(UserEntry entry) {
        User user = new User();
        user.setId(entry.getId());
        user.setName(entry.getName());
        user.setLast_seen(entry.getLastSeen());
        user.setAvailable(entry.isAvailable());
        user.setBalance(entry.getBalance());
        return user;
    }


    @RequestMapping("/v1/users")
    public User[] users(@RequestParam(value="count", defaultValue="-1") int count,
                        @RequestParam(value="page", defaultValue="0") int page,
                        @RequestParam(value="onlyAvailable", defaultValue="false") boolean onlyAvailable)
    throws Exception {
        Database database = Database.getInstance();
        List<UserEntry> userEntrys = count == -1
                ? database.usersGetAll(1,1000000, onlyAvailable)
                : database.usersGetAll(1+count*page, 1+count*(page+1), onlyAvailable);
        User[] users = new User[userEntrys.size()];
        for(int i = 0; i < users.length; i++) {
            users[i] = mapEntryToUser(userEntrys.get(i));
        }

        return users;
    }

    @RequestMapping("/v1/users/{id}")
    public User user(@PathVariable("id") int id)
        throws Exception {
        try {
            Database database = Database.getInstance();
            return mapEntryToUser(database.getUser(id));
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException();
        }
    }

    @PostMapping("/v1/users")
    public ResponseEntity createUser(@RequestBody UserCreate userCreate)
        throws Exception {
        try {
            Database database = Database.getInstance();
            database.userCreate(userCreate.getAuth_hash().getBytes());
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            throw new AlreadyExistsException();
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PatchMapping("/v1/users/{id}")
    public ResponseEntity patchUser(@PathVariable("id") int id,
            @RequestBody UserUpdate userUpdate)
        throws Exception {
        Database db = Database.getInstance();
        db.userUpdate(id, userUpdate.getAuth_hash().getBytes(), userUpdate.getName());
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

}
