package com.insmart.app.service;
import com.insmart.app.domain.Permission;
import com.insmart.app.domain.User;
import com.insmart.app.repo.PermissionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@org.springframework.stereotype.Service
@Transactional(rollbackOn = RuntimeException.class)
public class PermissionService implements Service<Permission> {
    private final PermissionRepo permissionRepo;
    private final UserServiceImpl userService;
    @Override
    public ResponseEntity<List<?>> getAll(Authentication authentication, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        boolean isAdmin = user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
        boolean isPermitted = user.getPermissions().stream().anyMatch(Permission::getRead) &&
                user.getPermissions().stream().anyMatch(p -> p.getResource().getUrl().contains("permission"));
        if(isAdmin || isPermitted){
            log.info("Getting all permissions");
            return ResponseEntity.ok(permissionRepo.findAll());
        }
        log.error("User {} is not authorized to get all permissions",user.getUsername());
        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity<?> getOne(Authentication authentication, Principal principal,Long id) {
        User user = userService.findByUsername(principal.getName());
        boolean isAdmin = user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
        boolean isPermitted = user.getPermissions().stream().anyMatch(Permission::getRead) &&
                user.getPermissions().stream().anyMatch(p -> p.getResource().getUrl().contains("permission"));
        if(isAdmin || isPermitted){
            log.info("Getting permission with id {}",id);
            return ResponseEntity.ok(permissionRepo.findById(id).orElseThrow(()->new RuntimeException("Permission not found")));
        }
        log.error("User {} is not authorized to get permission with id {}",user.getUsername(),id);
        return ResponseEntity.ok(Map.of("message","User is not authorized to get permission with id "+id));
    }

    @Override
    public ResponseEntity<?> create(Authentication authentication, Principal principal,Permission permission) {
        User user = userService.findByUsername(principal.getName());
        boolean isAdmin = user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
        boolean isPermitted = user.getPermissions().stream().anyMatch(Permission::getWrite) &&
                user.getPermissions().stream().anyMatch(p -> p.getResource().getUrl().contains("permission"));
        if(isAdmin || isPermitted){
            log.info("Creating permission {}",permission);
            return ResponseEntity.ok(permissionRepo.save(permission));
        }
        log.error("User {} is not authorized to create permission {}",user.getUsername(),permission);
        return ResponseEntity.ok(Map.of("message","User is not authorized to create permission "+permission));
    }

    @Override
    public ResponseEntity<?> update(Authentication authentication, Principal principal,Long id, Permission permission) {
        User user = userService.findByUsername(principal.getName());
        boolean isAdmin = user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
        boolean isPermitted = user.getPermissions().stream().anyMatch(Permission::getUpdate) &&
                user.getPermissions().stream().anyMatch(p -> p.getResource().getUrl().contains("permission"));
        if(isAdmin || isPermitted){
            Permission permission1 = permissionRepo.findById(id).orElseThrow(()->new RuntimeException("Permission not found"));
            permission1.setDelete(permission.getDelete());
            permission1.setUpdate(permission.getUpdate());
            permission1.setRead(permission.getRead());
            permission1.setWrite(permission.getWrite());
            permission1.setUser(permission.getUser());
            log.info("Update permission {}",permission1);
            return ResponseEntity.ok(permissionRepo.save(permission1));
        }
        log.error("User {} is not authorized to update permission {}",user.getUsername(),permission);
       return ResponseEntity.ok(Map.of("message","User is not authorized to update permission "+permission));
    }

    @Override
    public ResponseEntity<?> delete(Authentication authentication, Principal principal,Long id) {
        User user = userService.findByUsername(principal.getName());
        boolean isAdmin = user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
        boolean isPermitted = user.getPermissions().stream().anyMatch(Permission::getDelete) &&
                user.getPermissions().stream().anyMatch(p -> p.getResource().getUrl().contains("permission"));
        if(isAdmin || isPermitted){
            Permission permission = permissionRepo.findById(id).orElseThrow(()->new RuntimeException("Permission not found"));
            log.info("Delete permission {}",permission);
            permissionRepo.delete(permission);
            return ResponseEntity.ok(permission);
        }
        log.error("User {} is not authorized to delete permission with id {}",user.getUsername(),id);
        return ResponseEntity.ok(Map.of("message","User is not authorized to delete permission with id "+id));
    }
}
