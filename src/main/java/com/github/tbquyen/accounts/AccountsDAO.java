package com.github.tbquyen.accounts;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import com.github.tbquyen.datatables.params.DataTablesOrder;
import com.github.tbquyen.entity.Accounts;

@Mapper
public interface AccountsDAO {
  @Select("SELECT * FROM accounts WHERE id = #{id}")
  @Results(id = "accounts", value = { @Result(property = "id", column = "id"),
      @Result(property = "username", column = "username"), @Result(property = "password", column = "password"),
      @Result(property = "role", column = "role"), @Result(property = "status", column = "status"), })
  public Accounts getEntityById(long id);

  @SelectProvider(type = SQLBuilder.class, method = "getCurrentPage")
  @ResultMap(value = "accounts")
  public List<Accounts> getCurrentPage(AccountsDataTablesRequest form);

  @Select("SELECT COUNT(id) FROM accounts")
  @ResultType(value = Long.class)
  public long getTotalRecord(AccountsDataTablesRequest form);

  @Delete("DELETE FROM accounts WHERE id=#{id}")
  public int delete(long id);

  @Insert("INSERT INTO accounts (username, password, role, status) VALUES (#{username}, #{password}, #{role}, #{status})")
  public int insert(Accounts form);

  @UpdateProvider(type = SQLBuilder.class, method = "update")
  public int update(Accounts form);

  class SQLBuilder {
    public String getCurrentPage(AccountsDataTablesRequest form) {
      return new SQL() {
        {
          SELECT("*");
          FROM("accounts");

          if (StringUtils.hasLength(form.getUsername())) {
            form.setUsername("%" + form.getUsername() + "%");
            WHERE("username LIKE #{username}");
          }

          if (StringUtils.hasLength(form.getRole())) {
            WHERE("role = #{role}");
          }

          OFFSET(form.getStart());

          if (form.getLength() > 0) {
            LIMIT(form.getLength());
          }

          for (DataTablesOrder order : form.getOrder()) {
            ORDER_BY(order.toSQL());
          }
        }
      }.toString();
    }

    public String update(Accounts form) {
      return new SQL() {
        {
          UPDATE("accounts");
          SET("username=#{username}");

          if (StringUtils.hasLength(form.getPassword())) {
            SET("password=#{password}");
            SET("status=0");
          }

          SET("role=#{role}");
          WHERE("id=#{id}");
        }
      }.toString();
    }
  }
}
