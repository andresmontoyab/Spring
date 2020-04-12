package com.security.example.usermicroservice.reposity;

import com.security.example.usermicroservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(path = "users")
public interface UserRepository extends JpaRepository<User, Long> {

    @RestResource(path = "searchByUsername")
    public User findByUsername(@Param("username") String username);

    @RestResource(path = "findEnabledUser")
    public List<User> findByEnabled(@Param("enabled") Boolean enabled);
}
