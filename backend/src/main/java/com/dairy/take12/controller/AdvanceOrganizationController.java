package com.dairy.take12.controller;

import com.dairy.take12.model.AdvanceOrganization;
import com.dairy.take12.repository.AdvanceOrganizationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("advanceOrganization")
@CrossOrigin(origins = "*")

public class AdvanceOrganizationController {
    @Autowired
    AdvanceOrganizationRepo advanceOrganizationRepo;
    @PostMapping("/add")
    public void addAdvanceOrganization(@RequestBody AdvanceOrganization advanceOrganization) {
        advanceOrganizationRepo.save(advanceOrganization);
    }

}
