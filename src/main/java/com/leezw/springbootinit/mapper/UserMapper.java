package com.leezw.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leezw.springbootinit.model.entity.User;

/**
 * 用户数据库操作
 *
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 更新
     * @author verytools
     * @date 2025/06/21
     **/
    int update(User user);


}




