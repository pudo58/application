package com.insmart.app.api;

import com.insmart.app.domain.Permission;
import com.insmart.app.service.Service;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/permission")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SuppressWarnings("unused")
public class PermissionResource {
    private final Service<Permission> permissionService;
    @GetMapping("/get")
    public ResponseEntity<List<?>> getAll(Authentication authentication, Principal principal){
        return permissionService.getAll(authentication,principal);
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getOne(Authentication authentication, Principal principal,@PathVariable Long id){
        return permissionService.getOne(authentication, principal, id);
    }
    @PostMapping("/create")
    public ResponseEntity<?> create(Authentication authentication, Principal principal,@RequestBody Permission permission){
        return permissionService.create(authentication, principal, permission);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(Authentication authentication, Principal principal,@PathVariable Long id,@org.springframework.web.bind.annotation.RequestBody Permission permission){
        return permissionService.update(authentication, principal, id, permission);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(Authentication authentication, Principal principal,@PathVariable Long id){
        return permissionService.delete(authentication, principal, id);
    }

}
