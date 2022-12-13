package com.insmart.app.api;
import com.insmart.app.domain.Organization;
import com.insmart.app.service.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/organization")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SuppressWarnings("unused")
public class OrganizationResource {
    private final Service<Organization> organizationService;
    @GetMapping("/get")
    public ResponseEntity<List<?>> getAll(Authentication authentication, Principal principal){
        return organizationService.getAll(authentication,principal);
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getOne(Authentication authentication, Principal principal,@PathVariable Long id){
        return organizationService.getOne(authentication,principal,id);
    }
    @PostMapping("/create")
    public ResponseEntity<?> create(Authentication authentication, Principal principal,@RequestBody Organization organization){
        return organizationService.create(authentication, principal, organization);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(Authentication authentication, Principal principal,@PathVariable Long id,@RequestBody Organization organization){
        return organizationService.update(authentication, principal, id, organization);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(Authentication authentication, Principal principal,@PathVariable Long id){
        return organizationService.delete(authentication, principal, id);
    }

}
