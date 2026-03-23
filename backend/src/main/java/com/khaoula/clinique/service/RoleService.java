package com.khaoula.clinique.service;

import java.util.List;
import java.util.Optional;

import com.khaoula.clinique.entities.Role;

public interface RoleService {
    Role saveRole(Role role);
    Role updateRole(Role role);
    void deleteRole(Role role);
    void deleteRoleById(Long id);
    Optional<Role> getRole(Long id);
    List<Role> getAllRoles();
    Role findByRole(String role);
}