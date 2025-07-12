package com.task.mathsequences.service.impl;


import com.task.mathsequences.entity.Role;
import com.task.mathsequences.repository.RoleRepository;
import com.task.mathsequences.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getUserRole() {
        return roleRepository.findByName("ROLE_USER").get();
    }

}
