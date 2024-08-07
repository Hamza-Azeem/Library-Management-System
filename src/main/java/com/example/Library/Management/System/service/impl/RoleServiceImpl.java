package com.example.Library.Management.System.service.impl;

import com.example.Library.Management.System.entity.Role;
import com.example.Library.Management.System.repository.RoleRepository;
import com.example.Library.Management.System.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role getRoleByAuthority(String authority) {
        return roleRepository.findByAuthority(authority).orElse(null);
    }
}
