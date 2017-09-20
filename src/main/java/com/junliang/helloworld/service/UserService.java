package com.junliang.helloworld.service;

import com.junliang.helloworld.dao.repository.UserRepository;
import com.junliang.helloworld.exception.BaseException;
import com.junliang.helloworld.pojo.bean.UserInfo;
import com.junliang.helloworld.pojo.entity.User;
import com.junliang.helloworld.util.BeanCopierUtils;
import com.junliang.helloworld.util.RSAHelper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.introspector.PropertyUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by Toni_ on 2017/9/20.
 */
@Slf4j
@Service
public class UserService {

    @Resource
    private UserRepository userRepository;

    @Value("${jwt.rsa-private-key-file-path}")
    private String privateKeyFilePath;

    public String login(String username, String password){
       User user = userRepository.findByName(username);
       if (user ==null)
           throw new BaseException(4001,"用户名不存在");

       if (!user.getPassword().equals(password))
           throw new BaseException(4002,"用户密码错误");

        UserInfo userInfo =new UserInfo();
        BeanCopierUtils.copyProperties(user,userInfo);


        //PropertyUtils
        //BeanCopier.create() 难用

        return generateToken(userInfo);
    }

    private String generateToken(UserInfo userInfo) {

        String compactJws = null;
        try {
            compactJws = Jwts.builder()
                    .setSubject(userInfo.getName())
                    .setExpiration(Date.from(LocalDateTime.now().plusWeeks(1).atZone(ZoneId.of(ZoneId.SHORT_IDS.get("CTT"))).toInstant()))
                    .claim(userInfo.getId(), userInfo.getName())
                    .signWith(SignatureAlgorithm.RS256, RSAHelper.getPrivateKey(privateKeyFilePath))
                    .compact();
        } catch (Exception e) {
           throw new BaseException(5001,"生成Token失败。");
           //log.error(e.getMessage());
        }
        return compactJws;
    }


}
