/**
 * 
 */
package org.zdxue.microservice.xxx.mybatis.mapper;

import org.apache.ibatis.annotations.Select;
import org.zdxue.microservice.xxx.model.User;

/**
 * @author zdxue
 *
 */
public interface UserMapper {

    @Select("select a.id, a.name, a.email from tbl_xxx_user a where a.id = #{id}")
    public User findById(int id);

}
