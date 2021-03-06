package cn.edu.shnu.fb.infrastructure.persistence;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import cn.edu.shnu.fb.domain.user.Teacher;
import cn.edu.shnu.fb.domain.user.User;

/**
 * Created by bytenoob on 15/11/7.
 */
public interface UserDao extends PagingAndSortingRepository<User,Integer> {
    User findByUsernameEquals(String name);
    User findByTeacher(Teacher teacher);

    List<User> findByRole(Integer role);

}
