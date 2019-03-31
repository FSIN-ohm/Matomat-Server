package org.fsin.matomat.rest.v1;

import org.fsin.matomat.database.Database;
import org.fsin.matomat.database.dao.AdminDAO;
import org.fsin.matomat.database.model.AdminEntry;
import org.fsin.matomat.rest.model.Admin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
                          @RequestParam(value="page", defaultValue="0") int page) throws Exception {
        Database database = Database.getInstance();
        List<AdminEntry> adminEntrys = database.adminGetAll();
        Admin[] admins = new Admin[adminEntrys.size()];
        for (int i = 0; i < admins.length; i++) {
            admins[i] = mapEntryToAdmin(adminEntrys.get(i));
        }
        return admins;
    }
}
