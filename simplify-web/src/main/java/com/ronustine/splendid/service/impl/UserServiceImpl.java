package com.ronustine.splendid.service.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.ronustine.splendid.dao.UserMapper;
import com.ronustine.splendid.pojo.User;
import com.ronustine.splendid.service.IUserService;

@Service("userService")
public class UserServiceImpl implements IUserService {
	@Resource
	private UserMapper userDao;

	public User getUserById(int userId) {
		// TODO Auto-generated method stub
		return this.userDao.selectByPrimaryKey(userId);
	}
}

