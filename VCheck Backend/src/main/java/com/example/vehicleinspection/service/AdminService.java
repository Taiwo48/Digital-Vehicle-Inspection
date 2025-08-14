package com.example.vehicleinspection.service;

import com.example.vehicleinspection.model.Admin;
import com.example.vehicleinspection.dto.AdminDTO;

import java.util.List;
import java.util.Optional;

public interface AdminService extends BaseService<Admin, Long> {
    
    AdminDTO createAdmin(AdminDTO adminDTO);
    
    AdminDTO updateAdmin(Long id, AdminDTO adminDTO);
    
    Optional<AdminDTO> findByUsername(String username);
    
    Optional<AdminDTO> findByEmail(String email);
    
    List<AdminDTO> findAllAdmins();
    
    void deleteAdmin(Long id);
    
    List<AdminDTO> findByDepartment(String department);
    
    List<AdminDTO> findByRole(String role);
    
    List<AdminDTO> findAdminsWithClientManagement();
    
    List<AdminDTO> findAdminsWithOfficerManagement();
    
    List<AdminDTO> findAdminsWithAnalyticsAccess();
    
    AdminDTO updatePermissions(Long id, boolean canManageClients, boolean canManageOfficers,
                              boolean canViewAnalytics, boolean canManageSystem);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    void updateLastLogin(Long id);
    
    int getActiveSessionCount(Long id);
    
    String getLastActionPerformed(Long id);
}