package com.junliang.helloworld;

import com.junliang.helloworld.dao.mapper.UserMapper;
import com.junliang.helloworld.dao.repository.UserRepository;
import com.junliang.helloworld.pojo.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloWorldApplicationTests {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserMapper userMapper;
	@Test
	public void contextLoads() {
	}

	@Test
	public void test() throws Exception {
		 User user= new User();
		 user.setName("admin");
		 user.setPassword("password");
		userRepository.save(user);
		//userRepository.save(new User());
		//userRepository.save(new User());
		//userMapper.insert(new com.junliang.helloworld.pojo.domain.User());
		//userMapper.insert(new com.junliang.helloworld.pojo.domain.User());

	}
}
