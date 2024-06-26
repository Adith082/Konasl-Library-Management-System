package com.konasl.user;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {
    public List<User> findByApprovalOfAdmin(boolean approvalOfAdmin);

    boolean existsByEmail(String email);

    public User findByEmail(String email);

}
