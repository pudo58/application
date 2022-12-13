package com.insmart.app.api;

import com.insmart.app.domain.Role;
import com.insmart.app.service.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/role")
@SuppressWarnings("unused")
public class RoleResource {
    private final Service<Role> roleService;
    @GetMapping("/get")
    public ResponseEntity<List<?>> getAll(Authentication authentication, Principal principal){
        return roleService.getAll(authentication, principal);
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<?>getOne(Authentication authentication, Principal principal,@PathVariable Long id){
        return roleService.getOne(authentication, principal, id);
    }
    @PostMapping("/save")
    public ResponseEntity<?> create(Authentication authentication,Principal principal,@RequestBody Role role){
        return roleService.create(authentication, principal, role);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(Authentication authentication,Principal principal,@RequestBody Role role,@PathVariable Long id){
        return roleService.update(authentication,principal,id,role);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(Authentication authentication,Principal principal,@PathVariable Long id){
        return roleService.delete(authentication,principal,id);
    }
}
