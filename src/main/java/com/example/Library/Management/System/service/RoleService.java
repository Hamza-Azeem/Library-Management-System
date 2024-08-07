package com.example.Library.Management.System.service;

import com.example.Library.Management.System.entity.Role;

public interface RoleService {
    public Role createRole(Role role);
    public Role getRoleByAuthority(String authority);
}
