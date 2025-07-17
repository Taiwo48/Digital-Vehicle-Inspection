package com.example.vehicleinspection.service.impl;

import com.example.vehicleinspection.model.Admin;
import com.example.vehicleinspection.dto.AdminDTO;
import com.example.vehicleinspection.repository.AdminRepository;
import com.example.vehicleinspection.service.AdminService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminServiceImpl extends BaseServiceImpl<Admin, Long> implements AdminService {

    private final AdminRepository adminRepository;

    public AdminServiceImpl(AdminRepository adminRepository) {
        super(adminRepository);
        this.adminRepository = adminRepository;
    }

    @Override
    public AdminDTO createAdmin(AdminDTO adminDTO) {
        if (existsByUsername(adminDTO.getUsername())) {
            throw new RuntimeException("Username already exists: " + adminDTO.getUsername());
        }
        if (existsByEmail(adminDTO.getEmail())) {
            throw new RuntimeException("Email already exists: " + adminDTO.getEmail());
        }

        Admin admin = new Admin();
        BeanUtils.copyProperties(adminDTO, admin);
        admin = adminRepository.save(admin);
        return convertToDTO(admin);
    }

    @Override
    public AdminDTO updateAdmin(Long id, AdminDTO adminDTO) {
        return adminRepository.findById(id)
                .map(admin -> {
                    // Check if username is being changed and if new username exists
                    if (!admin.getUsername().equals(adminDTO.getUsername()) &&
                            existsByUsername(adminDTO.getUsername())) {
                        throw new RuntimeException("Username already exists: " + adminDTO.getUsername());
                    }
                    // Check if email is being changed and if new email exists
                    if (!admin.getEmail().equals(adminDTO.getEmail()) &&
                            existsByEmail(adminDTO.getEmail())) {
                        throw new RuntimeException("Email already exists: " + adminDTO.getEmail());
                    }

                    BeanUtils.copyProperties(adminDTO, admin, "id", "lastLogin");
                    admin = adminRepository.save(admin);
                    return convertToDTO(admin);
                })
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + id));
    }

    @Override
    public Optional<AdminDTO> findByUsername(String username) {
        return adminRepository.findByUsername(username)
                .map(this::convertToDTO);
    }

    @Override
    public Optional<AdminDTO> findByEmail(String email) {
        return adminRepository.findByEmail(email)
                .map(this::convertToDTO);
    }

    @Override
    public List<AdminDTO> findAllAdmins() {
        return adminRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AdminDTO> findByDepartment(String department) {
        return adminRepository.findByDepartment(department).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AdminDTO> findByRole(String role) {
        return adminRepository.findByRole(role).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AdminDTO> findAdminsWithClientManagement() {
        return adminRepository.findByCanManageClientsTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AdminDTO> findAdminsWithOfficerManagement() {
        return adminRepository.findByCanManageOfficersTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AdminDTO> findAdminsWithAnalyticsAccess() {
        return adminRepository.findByCanViewAnalyticsTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AdminDTO updatePermissions(Long id, boolean canManageClients, boolean canManageOfficers,
                                     boolean canViewAnalytics, boolean canManageSystem) {
        return adminRepository.findById(id)
                .map(admin -> {
                    admin.setCanManageClients(canManageClients);
                    admin.setCanManageOfficers(canManageOfficers);
                    admin.setCanViewAnalytics(canViewAnalytics);
                    admin.setCanManageSystem(canManageSystem);
                    admin = adminRepository.save(admin);
                    return convertToDTO(admin);
                })
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + id));
    }

    @Override
    public boolean existsByUsername(String username) {
        return adminRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return adminRepository.existsByEmail(email);
    }

    @Override
    public void updateLastLogin(Long id) {
        adminRepository.findById(id)
                .ifPresent(admin -> {
                    admin.setLastLogin(LocalDateTime.now());
                    adminRepository.save(admin);
                });
    }

    @Override
    public int getActiveSessionCount(Long id) {
        // This would typically be implemented with a session management system
        // For now, returning a placeholder value
        return 1;
    }

    @Override
    public String getLastActionPerformed(Long id) {
        // This would typically be implemented with an audit logging system
        // For now, returning a placeholder value
        return "Viewed dashboard";
    }

    private AdminDTO convertToDTO(Admin admin) {
        AdminDTO dto = new AdminDTO();
        BeanUtils.copyProperties(admin, dto);
        
        // Set additional fields
        dto.setActiveSessionCount(getActiveSessionCount(admin.getId()));
        dto.setLastActionPerformed(getLastActionPerformed(admin.getId()));
        
        // These would typically come from actual service calls
        dto.setManagedClientsCount(100);
        dto.setManagedOfficersCount(20);
        
        return dto;
    }
}