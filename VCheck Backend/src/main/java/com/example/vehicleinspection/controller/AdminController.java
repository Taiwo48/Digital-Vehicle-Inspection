package com.example.vehicleinspection.controller;

import com.example.vehicleinspection.dto.AdminDTO;
import com.example.vehicleinspection.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping
    public ResponseEntity<AdminDTO> createAdmin(@Valid @RequestBody AdminDTO adminDTO) {
        return ResponseEntity.ok(adminService.createAdmin(adminDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminDTO> updateAdmin(
            @PathVariable Long id,
            @Valid @RequestBody AdminDTO adminDTO) {
        return ResponseEntity.ok(adminService.updateAdmin(id, adminDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminDTO> getAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.findById(id));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<AdminDTO> getByUsername(@PathVariable String username) {
        return ResponseEntity.ok(adminService.findByUsername(username));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<AdminDTO> getByEmail(@PathVariable String email) {
        return ResponseEntity.ok(adminService.findByEmail(email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<AdminDTO>> getByDepartment(@PathVariable String department) {
        return ResponseEntity.ok(adminService.findByDepartment(department));
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<AdminDTO>> getByRole(@PathVariable String role) {
        return ResponseEntity.ok(adminService.findByRole(role));
    }

    @GetMapping("/client-managers")
    public ResponseEntity<List<AdminDTO>> getClientManagers() {
        return ResponseEntity.ok(adminService.findAdminsWithClientManagement());
    }

    @GetMapping("/officer-managers")
    public ResponseEntity<List<AdminDTO>> getOfficerManagers() {
        return ResponseEntity.ok(adminService.findAdminsWithOfficerManagement());
    }

    @GetMapping("/analytics-managers")
    public ResponseEntity<List<AdminDTO>> getAnalyticsManagers() {
        return ResponseEntity.ok(adminService.findAdminsWithAnalyticsManagement());
    }

    @PutMapping("/{id}/permissions")
    public ResponseEntity<AdminDTO> updatePermissions(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> permissions) {
        return ResponseEntity.ok(adminService.updatePermissions(id, permissions));
    }

    @PutMapping("/{id}/last-login")
    public ResponseEntity<AdminDTO> updateLastLogin(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.updateLastLogin(id, LocalDateTime.now()));
    }

    @GetMapping("/{id}/active-sessions")
    public ResponseEntity<Integer> getActiveSessions(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getActiveSessionCount(id));
    }

    @GetMapping("/{id}/last-action")
    public ResponseEntity<String> getLastAction(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getLastPerformedAction(id));
    }

    @GetMapping("/{id}/managed-entities")
    public ResponseEntity<Map<String, Integer>> getManagedEntitiesCounts(@PathVariable Long id) {
        Map<String, Integer> counts = Map.of(
            "managedClients", adminService.getManagedClientsCount(id),
            "managedOfficers", adminService.getManagedOfficersCount(id)
        );
        return ResponseEntity.ok(counts);
    }

    @GetMapping("/search")
    public ResponseEntity<List<AdminDTO>> searchAdmins(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String permission) {
        if (department != null) {
            return ResponseEntity.ok(adminService.findByDepartment(department));
        } else if (role != null) {
            return ResponseEntity.ok(adminService.findByRole(role));
        } else if ("client".equalsIgnoreCase(permission)) {
            return ResponseEntity.ok(adminService.findAdminsWithClientManagement());
        } else if ("officer".equalsIgnoreCase(permission)) {
            return ResponseEntity.ok(adminService.findAdminsWithOfficerManagement());
        } else if ("analytics".equalsIgnoreCase(permission)) {
            return ResponseEntity.ok(adminService.findAdminsWithAnalyticsManagement());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}