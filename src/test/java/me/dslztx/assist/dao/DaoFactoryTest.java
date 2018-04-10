package me.dslztx.assist.dao;

import com.alibaba.druid.pool.DruidDataSource;
import javax.sql.DataSource;
import junit.framework.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaoFactoryTest {

  private static final Logger logger = LoggerFactory.getLogger(DaoFactoryTest.class);

  @Test
  public void obtainDao() throws Exception {
    try {
      DataSource dataSource = new DruidDataSource();
      System.out.println(dataSource.hashCode());

      UserDao userDaoFirst = DaoFactory.obtainDao(dataSource, UserDao.class);

      UserDao userDaoSecond = DaoFactory.obtainDao(dataSource, UserDao.class);

      Assert.assertTrue(userDaoFirst == userDaoSecond);

      userDaoFirst.insertUser(new User());
      userDaoSecond.insertUser(new User());
    } catch (Exception e) {
      logger.error("", e);
      Assert.fail();
    }
  }

  static class UserDao extends Dao {

    public UserDao(DataSource dataSource) {
      super(dataSource);
    }

    public void insertUser(User user) {
      DataSource dataSource = getDataSource();
      //使用dataSource把User插入到数据库

      System.out.println(dataSource.hashCode());
    }
  }

  static class User {

    Long id;
    String name;

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }

}