package com.example.restful.service;

import com.example.restful.domain.User;
import com.example.restful.repository.UserRepository;
import com.example.restful.util.Util;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: shudj
 * @time: 2019/2/15 16:32
 * @description:
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User addUser(User user) {

        return userRepository.save(user);
    }

    public List<User> getUserList() {

        return userRepository.findAll();
    }

    public User getUser(String id) {

        return userRepository.getOne(id);
    }

    public void deleteUser(String id) {

        userRepository.deleteById(id);
    }

    public User update(String id, User user) {

        User currentInstance = userRepository.getOne(id);

        // 支持部分更新
        String[] nullPropertyNames = Util.getNullPropertyNames(user);
        BeanUtils.copyProperties(user, currentInstance, nullPropertyNames);

        return userRepository.save(currentInstance);
    }
}
