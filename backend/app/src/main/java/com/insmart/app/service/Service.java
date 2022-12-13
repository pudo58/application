package com.insmart.app.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import java.io.Serializable;
import java.security.Principal;
import java.util.List;

public interface Service <T extends Serializable> {
    ResponseEntity<List<?>> getAll(Authentication authentication, Principal principal);
    ResponseEntity<?> getOne(Authentication authentication, Principal principal,Long id);
    ResponseEntity<?> create(Authentication authentication, Principal principal,T t);
    ResponseEntity<?> update(Authentication authentication, Principal principal,Long id, T t);
    ResponseEntity<?> delete(Authentication authentication, Principal principal,Long id);

}
