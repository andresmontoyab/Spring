package com.serviceoauth.services;

import com.commonsusers.models.entity.User;

public interface IUserService {

    User findByUsernmae(String username);
}
