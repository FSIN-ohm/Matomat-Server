package org.fsin.matomat.rest.v1;

import org.apache.commons.codec.digest.DigestUtils;
import org.fsin.matomat.database.Database;
import org.fsin.matomat.database.model.AdminEntry;
import org.fsin.matomat.rest.auth.Authenticator;
import org.fsin.matomat.rest.auth.UserPwdTocken;
import org.fsin.matomat.rest.exceptions.AlreadyExistsException;
import org.fsin.matomat.rest.exceptions.ResourceNotFoundException;
import org.fsin.matomat.rest.model.Admin;
import org.fsin.matomat.rest.model.UpdateAdmin;
import org.fsin.matomat.rest.model.CreateAdmin;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.List;
import java.util.Random;

import static org.fsin.matomat.rest.Utils.checkIfNotNull;

@RestController
public class AdminsController {

    private Admin mapEntryToAdmin(AdminEntry entry) {
        Admin admin = new Admin();
        admin.setId(entry.getId());
        admin.setAvailable(entry.isAvailable());
        admin.setBalance(entry.getBalance());
        admin.setEmail(entry.getEmail());
        admin.setLast_seen(entry.getLastSeen().toLocalDateTime());
        admin.setUser_name(entry.getUsername());
        return admin;
    }

    @RolesAllowed("ROLE_ADMIN")
    @RequestMapping("/v1/admins")
    public Admin[] admins(@RequestParam(value="count", defaultValue="-1") int count,
                          @RequestParam(value="page", defaultValue="0") int page,
                          @RequestParam(value="onlyAvailable", defaultValue="false") boolean onlyAvailable)
            throws Exception {
        Database database = Database.getInstance();
        List<AdminEntry> adminEntrys = count == -1
                ? database.adminGetAll(1, 1000000, onlyAvailable)
                : database.adminGetAll(1+count*page, 1+count*(page+1), onlyAvailable);
        Admin[] admins = new Admin[adminEntrys.size()];
        for (int i = 0; i < admins.length; i++) {
            admins[i] = mapEntryToAdmin(adminEntrys.get(i));
        }
        return admins;
    }

    @RolesAllowed("ROLE_ADMIN")
    @RequestMapping("/v1/admins/{id}")
    public Admin admin(@PathVariable int id)
        throws Exception {
        return getAdmin(id);
    }

    @RolesAllowed("ROLE_ADMIN")
    @RequestMapping("/v1/admins/me")
    public Admin getMe(@AuthenticationPrincipal UserPwdTocken user)
        throws Exception {
        return getAdmin(user.getId());
    }

    private Admin getAdmin(int id)
        throws Exception {
        try {
            Database db = Database.getInstance();
            return mapEntryToAdmin(db.adminGetDetail(id));
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException();
        }
    }

    @RolesAllowed("ROLE_ADMIN")
    @PostMapping("/v1/admins")
    public ResponseEntity createAdmin(@RequestBody CreateAdmin createAdmin)
        throws Exception {

        checkIfNotNull(createAdmin.getEmail());
        checkIfNotNull(createAdmin.getUser_name());
        checkIfNotNull(createAdmin.getPassword());

        try {
            Database db = Database.getInstance();
            AdminEntry entry = new AdminEntry();
            entry.setUsername(createAdmin.getUser_name());
            entry.setEmail(createAdmin.getEmail());
            byte[] salt = generateRandomSalt();
            entry.setPasswordSalt(salt);
            entry.setPassword(hexHashPwd(createAdmin.getPassword(), salt));

            db.adminCreate(entry);
            Authenticator.getInstance().invalidate();
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            throw new AlreadyExistsException();
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RolesAllowed("ROLE_ADMIN")
    @PatchMapping("/v1/admins/{id}")
    public ResponseEntity patchAdmin(@PathVariable int id,
                                     @RequestBody UpdateAdmin adminChange)
        throws Exception {

        checkIfNotNull(adminChange.getEmail());
        checkIfNotNull(adminChange.getPassword());
        checkIfNotNull(adminChange.getUser_name());

        try {
            Database db = Database.getInstance();
            AdminEntry entry = db.adminGetDetail(id);
            entry.setUsername(adminChange.getUser_name());
            byte[] salt = generateRandomSalt();
            entry.setPassword(hexHashPwd(adminChange.getPassword(), salt));
            entry.setPasswordSalt(salt);
            entry.setEmail(adminChange.getEmail());
            db.adminUpdate(entry);
            Authenticator.getInstance().invalidate();
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException();
        }
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @RolesAllowed("ROLE_ADMIN")
    @DeleteMapping("/v1/admins/{id}")
    public ResponseEntity deleteAdmin(@PathVariable int id)
        throws Exception {
        try {
            Database db = Database.getInstance();
            AdminEntry entry = db.adminGetDetail(id);
            byte[] salt = generateRandomSalt();
            entry.setPassword(hexHashPwd("", salt));
            entry.setPasswordSalt(salt);
            entry.setAvailable(false);
            db.adminUpdate(entry);
            Authenticator.getInstance().invalidate();
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException();
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    /***************** UTILS **************************/

    private byte[] generateRandomSalt() {
        byte[] array = new byte[64]; // length is bounded by 7
        new Random().nextBytes(array);
        return array;
    }

    private byte[] hexHashPwd(String password, byte[] salt) {
        return DigestUtils.sha512((new String(salt) + password).getBytes());
    }
}
