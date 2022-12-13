package com.insmart.app.api;

import com.insmart.app.domain.Resource;
import com.insmart.app.service.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/resource")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SuppressWarnings("unused")
public class ResourceMappingEndPoint {
    private final Service<Resource> resourceService;
    @GetMapping("/get")
    public ResponseEntity<List<?>> getAll(Authentication authentication, Principal principal){
        return resourceService.getAll(authentication,principal);
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getOne(Authentication authentication, Principal principal,@PathVariable Long id){
        return resourceService.getOne(authentication, principal, id);
    }
    @PostMapping("/save")
    public ResponseEntity<?> create(Authentication authentication, Principal principal,@RequestBody Resource resource){
        return resourceService.create(authentication, principal, resource);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(Authentication authentication, Principal principal,@PathVariable Long id,@RequestBody Resource resource){
        return resourceService.update(authentication, principal, id, resource);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(Authentication authentication, Principal principal,@PathVariable Long id){
        return resourceService.delete(authentication, principal, id);
    }
}
