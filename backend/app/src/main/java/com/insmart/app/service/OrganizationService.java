package com.insmart.app.service;
import com.insmart.app.domain.Organization;
import com.insmart.app.domain.Permission;
import com.insmart.app.domain.User;
import com.insmart.app.repo.OrganizationRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Transactional(rollbackOn = RuntimeException.class)
@org.springframework.stereotype.Service
public class OrganizationService implements Service<Organization> {
    private final OrganizationRepo organizationRepo;
    private final UserServiceImpl userService;
    @Override
    public synchronized ResponseEntity<List<?>> getAll(Authentication authentication, Principal principal) {
        try{
            User user = userService.findByUsername(principal.getName());
            boolean isAdmin = user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
            boolean isPermitted = user.getPermissions().stream().anyMatch(Permission::getRead) &&
                    user.getPermissions().stream().anyMatch(p -> p.getResource().getUrl().contains("organization"));
            if(isAdmin || isPermitted){
                log.info("Getting all organizations");
                return ResponseEntity.ok(organizationRepo.findAll());
            }
            log.error("User {} is not authorized to get all organizations",user.getUsername());
            return ResponseEntity.badRequest().build();
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return ResponseEntity.badRequest().build();
    }

    @Override
    public synchronized ResponseEntity<?> getOne(Authentication authentication, Principal principal,Long id) {
        User user = userService.findByUsername(principal.getName());
        boolean isAdmin = user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
        boolean isPermitted = user.getPermissions().stream().anyMatch(Permission::getRead) &&
                user.getPermissions().stream().anyMatch(p -> p.getResource().getUrl().contains("organization"));
        if(isAdmin || isPermitted){
            log.info("Getting organization with id {}",id);
            return ResponseEntity.ok(organizationRepo.findById(id).orElseThrow(()->new RuntimeException("Organization not found")));
        }
        log.error("User {} is not authorized to get organization with id {}",user.getUsername(),id);
        return ResponseEntity.ok(Map.of("message","You are not authorized to get organization with id "+id));
    }

    @Override
    public synchronized ResponseEntity<?> create(Authentication authentication, Principal principal,Organization organization) {
        User user = userService.findByUsername(principal.getName());
        boolean isAdmin = user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
        boolean isPermitted = user.getPermissions().stream().anyMatch(Permission::getWrite) &&
                user.getPermissions().stream().anyMatch(p -> p.getResource().getUrl().contains("organization"));
        if(isAdmin || isPermitted){
            log.info("Creating organization {}",organization);
            return ResponseEntity.ok(organizationRepo.save(organization));
        }
        log.error("User {} is not authorized to create organization {}",user.getUsername(),organization);
        return ResponseEntity.ok(Map.of("message","You are not authorized to create organization "+organization));
    }

    @Override
    public synchronized ResponseEntity<?> update(Authentication authentication, Principal principal,Long id, Organization organization) {
        User user = userService.findByUsername(principal.getName());
        boolean isAdmin = user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
        boolean isPermitted = user.getPermissions().stream().anyMatch(Permission::getUpdate) &&
                user.getPermissions().stream().anyMatch(p -> p.getResource().getUrl().contains("organization"));
        if(isAdmin || isPermitted){
            Organization organization1 = organizationRepo.
                    findById(id).orElseThrow(()->new RuntimeException("Organization not found"));
            organization1.setName(organization.getName());
            organization1.setDescription(organization.getDescription());
            log.info("Update organization {}",organization1);
            return ResponseEntity.ok(organizationRepo.save(organization1));
        }
        log.error("User {} is not authorized to update organization {}",user.getUsername(),organization);
        return ResponseEntity.ok(Map.of("message","You are not authorized to update organization "+organization));
    }

    @Override
    public synchronized ResponseEntity<?> delete(Authentication authentication, Principal principal,Long id) {
        User user = userService.findByUsername(principal.getName());
        boolean isAdmin = user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
        boolean isPermitted = user.getPermissions().stream().anyMatch(Permission::getDelete) &&
                user.getPermissions().stream().anyMatch(p -> p.getResource().getUrl().contains("organization"));
        if(isAdmin || isPermitted){
            Organization organization = organizationRepo.findById(id).orElseThrow(()->new RuntimeException("Organization not found"));
            organizationRepo.delete(organization);
            log.info("Delete organization {}",organization);
            return ResponseEntity.ok(organization);
        }
        log.error("User {} is not authorized to delete organization with id {}",user.getUsername(),id);
        return ResponseEntity.ok(Map.of("message","You are not authorized to delete organization with id "+id));
    }
}
