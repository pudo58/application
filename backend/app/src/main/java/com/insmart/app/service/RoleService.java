package com.insmart.app.service;

import com.insmart.app.domain.Role;
import com.insmart.app.repo.RoleRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import java.security.Principal;
import java.util.List;
@Slf4j@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class RoleService implements Service<Role>{
    private final RoleRepo roleRepo;
    @Override
    public ResponseEntity<List<?>> getAll(Authentication authentication, Principal principal) {
        log.info("Getting list role ....");
        return ResponseEntity.ok(roleRepo.findAll());
    }

    @Override
    public ResponseEntity<?> getOne(Authentication authentication, Principal principal, Long id) {
        log.info("get role with id {}",id);
        return ResponseEntity.ok(roleRepo.findById(id).orElseThrow(()->new RuntimeException("Role not found")));
    }

    @Override
    public ResponseEntity<?> create(Authentication authentication, Principal principal, Role role) {
        log.info("Create role name {} success",role.getName());
        return ResponseEntity.ok(roleRepo.save(role));
    }

    @Override
    public ResponseEntity<?> update(Authentication authentication, Principal principal, Long id, Role role) {
        Role role1 = roleRepo.findById(id).orElseThrow(()-> new RuntimeException("Role not found"));
        role1.setName(role.getName());
        log.info("update role id {}",id);
        try {
            log.info("update role id {} success",id);
            return ResponseEntity.ok(roleRepo.save(role1));
        }catch (Exception e){
            log.error("Update role with id {} fail !!!",id);
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<?> delete(Authentication authentication, Principal principal, Long id) {
        Role role1 = roleRepo.findById(id).orElseThrow(()-> new RuntimeException("Role id not found " + id));
        log.info("Delete role id {}",id);
        roleRepo.delete(role1);
        log.info("Delete role id {} success" , id);
        return ResponseEntity.ok(role1);
    }
}
