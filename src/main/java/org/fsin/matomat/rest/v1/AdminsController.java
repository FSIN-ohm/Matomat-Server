package org.fsin.matomat.rest.v1;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.fsin.matomat.database.Database;
import org.fsin.matomat.database.model.AdminEntry;
import org.fsin.matomat.rest.exceptions.AlreadyExistsException;
import org.fsin.matomat.rest.exceptions.ResourceNotFoundException;
import org.fsin.matomat.rest.model.Admin;
import org.fsin.matomat.rest.model.AdminChange;
import org.fsin.matomat.rest.model.AdminCreate;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.dsig.DigestMethod;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.List;
import java.util.Random;

@RestController
public class AdminsController {

    private Admin mapEntryToAdmin(AdminEntry entry) {
        Admin admin = new Admin();

        admin.setId(entry.getId());
        admin.setAvailable(entry.isAvailable());
        admin.setBalance(entry.getBalance());
        admin.setEmail(entry.getEmail());
        admin.setLast_seen(entry.getLastSeen().toLocalDateTime());
        admin.setUser_id(entry.getCorespondingUserId());
        admin.setUser_name(entry.getUsername());
        return admin;
    }

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

    @RequestMapping("/v1/admins/{id}")
    public Admin admin(@PathVariable int id)
        throws Exception {
        try {
            Database db = Database.getInstance();
            return mapEntryToAdmin(db.adminGetDetail(id));
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException();
        }
    }

    @PostMapping("/v1/admins")
    public ResponseEntity createAdmin(@RequestBody AdminCreate adminCreate)
        throws Exception {
        try {
            Database db = Database.getInstance();
            AdminEntry entry = new AdminEntry();
            entry.setUsername(adminCreate.getUser_name());
            entry.setEmail(adminCreate.getEmail());
            byte[] salt = generateRandomSalt();
            entry.setPasswordSalt(salt);
            entry.setPassword(hexHashPwd(adminCreate.getPassword(), salt));

            db.adminCreate(entry);
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            throw new AlreadyExistsException();
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PatchMapping("/v1/admins/{id}")
    public ResponseEntity patchAdmin(@PathVariable int id,
                                     @RequestBody AdminChange adminChange)
        throws Exception {
        try {
            Database db = Database.getInstance();
            AdminEntry entry = db.adminGetDetail(id);
            entry.setUsername(adminChange.getUser_name());
            byte[] salt = generateRandomSalt();
            entry.setPassword(hexHashPwd(adminChange.getPassword(), salt));
            entry.setPasswordSalt(salt);
            entry.setEmail(adminChange.getEmail());
            db.adminUpdate(entry);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException();
        }
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

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
