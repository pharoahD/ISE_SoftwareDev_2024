package org.flitter.backend.controller;

import org.flitter.backend.dto.UserIDRoleDTO;
import org.flitter.backend.dto.UserSearchRequestDTO;
import org.flitter.backend.service.HumanResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/user")
public class HumanResourceController {
    private final HumanResourceService humanresourceService;

    public HumanResourceController(@Autowired HumanResourceService humanresourceService) {
        this.humanresourceService = humanresourceService;
    }


    @GetMapping("/all")
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(humanresourceService.fetchAllUsersIdNameWithLimit1000());
    }

    @PostMapping("/search")
    public ResponseEntity<?> findByName(@RequestBody UserSearchRequestDTO usdto) {
        if (usdto.getUsername() == null || usdto.getUsername().isEmpty()) {
            return ResponseEntity.badRequest().body("查询名称不能为空");
        }
        return ResponseEntity.ok(humanresourceService.searchUserIdNameLimit1000(usdto.getUsername()));
    }

    @PostMapping("/byproject")

    
    @GetMapping("/role/all")
    public ResponseEntity<?> allPriority() {
        return ResponseEntity.ok(humanresourceService.fetchAllUsersIdRoleWithLimit1000());
    } 

    @PostMapping("/role/update")
    public ResponseEntity<?> updatePriority(@RequestBody UserIDRoleDTO updateRequest) {
        if (updateRequest.getId() == null) {
            return ResponseEntity.badRequest().body("更新id不能为空");
        }
        if (updateRequest.getRole().toString() == null) {
            return ResponseEntity.badRequest().body("更新权限不能为空");
        }
        try {
            humanresourceService.updateRole(updateRequest);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return (ResponseEntity<?>) ResponseEntity.ok();
    }
}
