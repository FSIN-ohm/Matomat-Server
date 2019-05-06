package org.fsin.matomat.rest.v1;

import org.fsin.matomat.database.Database;
import org.fsin.matomat.database.model.UserEntry;
import org.fsin.matomat.rest.auth.Authenticator;
import org.fsin.matomat.rest.auth.UserPwdTocken;
import org.fsin.matomat.rest.exceptions.AlreadyExistsException;
import org.fsin.matomat.rest.exceptions.BadRequestException;
import org.fsin.matomat.rest.exceptions.ResourceNotFoundException;
import org.fsin.matomat.rest.model.*;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import javax.annotation.security.RolesAllowed;

import static org.fsin.matomat.rest.Utils.checkIfNotNull;

import java.util.List;

@RestController
public class UsersController {

    private User mapEntryToUser(UserEntry entry) {
        User user = new User();
        user.setId(entry.getId());
        user.setName(entry.getName());
        user.setLast_seen(entry.getLastSeen().toLocalDateTime());
        user.setAvailable(entry.isAvailable());
        user.setBalance((int)(entry.getBalance().doubleValue()*100.00));
        return user;
    }

    @RolesAllowed("ROLE_ADMIN")
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

    @RolesAllowed("ROLE_ADMIN")
    @RequestMapping("/v1/users/{id}")
    public User user(@PathVariable("id") int id)
        throws Exception {
        return getUser(id);
    }

    @RolesAllowed("ROLE_USER")
    @RequestMapping("/v1/users/me")
    public User user(@AuthenticationPrincipal UserPwdTocken user)
        throws Exception {
        return getUser(user.getId());
    }

    private User getUser(int id)
        throws Exception {
        try {
            Database database = Database.getInstance();
            return mapEntryToUser(database.getUser(id));
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException();
        }
    }

    @RolesAllowed({"ROLE_ADMIN", "ROLE_DEVICE"})
    @PostMapping("/v1/users")
    public ResponseEntity createUser(@RequestBody CreateUser userCreate)
        throws Exception {

        checkIfNotNull(userCreate.getAuth_hash());

        try {
            Database database = Database.getInstance();
            database.userCreate(userCreate.getAuth_hash().getBytes());
            Authenticator.getInstance().invalidate();
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            throw new AlreadyExistsException();
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @PatchMapping("/v1/users/{id}")
    public ResponseEntity patchUser(@PathVariable int id,
            @RequestBody UpdateUser updateUser)
        throws Exception {

        checkIfNotNull(updateUser.getAuth_hash());
        checkIfNotNull(updateUser.getName());

        try {
            Database db = Database.getInstance();
            db.userUpdate(id, updateUser.getAuth_hash().getBytes(), updateUser.getName());
            Authenticator.getInstance().invalidate();
            return new ResponseEntity(HttpStatus.ACCEPTED);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException();
        }
    }

    @RolesAllowed("ROLE_ADMIN")
    @DeleteMapping("/v1/users/{id}")
    public ResponseEntity deleteUser(@PathVariable int id)
        throws Exception {
        try {
            if(id <= 3) throw new BadRequestException();

            Database db = Database.getInstance();
            db.userDelete(id);
            Authenticator.getInstance().invalidate();
            return new ResponseEntity(HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException();
        }
    }

}
