package com.insmart.app.service;
import com.insmart.app.domain.Permission;
import com.insmart.app.domain.Resource;
import com.insmart.app.domain.User;
import com.insmart.app.repo.ResourceRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@org.springframework.stereotype.Service
@Slf4j
@Transactional(rollbackOn = RuntimeException.class)
public class ResourceService implements Service<Resource> {
    private final ResourceRepo resourceRepo;
    private final UserServiceImpl userService;
    @Override
    public ResponseEntity<List<?>> getAll(Authentication authentication, Principal principal) {
        log.info(principal.getName());
        User user = userService.findByUsername(principal.getName());
        boolean isAdmin = user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
        boolean isPermitted = user.getPermissions().stream().anyMatch(Permission::getRead) &&
                user.getPermissions().stream().anyMatch(p -> p.getResource().getUrl().contains("resource"));
        if(isAdmin || isPermitted){
            log.info("Getting all resources");
            return ResponseEntity.ok(resourceRepo.findAll());
        }
        log.error("User {} is not authorized to get all resources",user.getUsername());
        return ResponseEntity.badRequest().build();
    }
    @Override
    public ResponseEntity<?> getOne(Authentication authentication, Principal principal,Long id) {
        User user = userService.findByUsername(principal.getName());
        boolean isAdmin = user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
        boolean isPermitted = user.getPermissions().stream().anyMatch(Permission::getRead) &&
                user.getPermissions().stream().anyMatch(p -> p.getResource().getUrl().contains("resource"));
        if(isAdmin || isPermitted){
            log.info("Getting resource with id {}",id);
            return ResponseEntity.ok(resourceRepo.findById(id).orElseThrow(()->new RuntimeException("Resource not found")));
        }
        log.error("User {} is not authorized to get resource with id {}",user.getUsername(),id);
        return ResponseEntity.ok(Map.of("message","You are not authorized to get resource with id "+id));
    }

    @Override
    public ResponseEntity<?> create(Authentication authentication, Principal principal,Resource resource) {
        User user = userService.findByUsername(principal.getName());
        boolean isAdmin = user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
        boolean isPermitted = user.getPermissions().stream().anyMatch(Permission::getWrite) &&
                user.getPermissions().stream().anyMatch(p -> p.getResource().getUrl().contains("resource"));
        if(isAdmin || isPermitted){
            log.info("Creating resource {}",resource);
            return ResponseEntity.ok(resourceRepo.save(resource));
        }
        log.error("User {} is not authorized to create resource {}",user.getUsername(),resource);
        return ResponseEntity.ok(Map.of("message","You are not authorized to create resource "+resource));
    }

    @Override
    public ResponseEntity<?> update(Authentication authentication, Principal principal,Long id, Resource resource) {
        User user = userService.findByUsername(principal.getName());
        boolean isAdmin = user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
        boolean isPermitted = user.getPermissions().stream().anyMatch(Permission::getUpdate) &&
                user.getPermissions().stream().anyMatch(p -> p.getResource().getUrl().contains("resource"));
        if(isAdmin || isPermitted){
            Resource resource1 = resourceRepo.findById(id).orElseThrow(()->new RuntimeException("Resource not found"));
            resource1.setCode(resource.getCode());
            resource1.setName(resource.getName());
            resource1.setUrl(resource.getUrl());
            resource1.setDescription(resource.getDescription());
            //resource1.setParent(resource.getParent());
            resource1.setResources(resource.getResources());
            log.info("Update resource {}",resource1);
            return ResponseEntity.ok(resourceRepo.save(resource1));
        }
        log.error("User {} is not authorized to update resource {}",user.getUsername(),resource);
        return ResponseEntity.ok(Map.of("message","You are not authorized to update resource "+resource));
    }

    @Override
    public ResponseEntity<?> delete(Authentication authentication, Principal principal,Long id) {
        User user = userService.findByUsername(principal.getName());
        boolean isAdmin = user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
        boolean isPermitted = user.getPermissions().stream().anyMatch(Permission::getDelete) &&
                user.getPermissions().stream().anyMatch(p -> p.getResource().getUrl().contains("resource"));
        if(isAdmin || isPermitted){
            Resource resource = resourceRepo.findById(id).orElseThrow(()->new RuntimeException("Resource not found"));
            log.info("Delete resource {}",resource);
            resourceRepo.delete(resource);
            return ResponseEntity.ok(resource);
        }
        log.error("User {} is not authorized to delete resource with id {}",user.getUsername(),id);
        return ResponseEntity.ok(Map.of("message","You are not authorized to delete resource with id "+id));
    }
}
