package com.github.tbquyen.categories;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
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
import com.github.tbquyen.entity.Categories;

@Mapper
public interface CategoriesDAO {
  @Select("SELECT * FROM categories WHERE id = #{id}")
  @Results(id = "categories", value = { @Result(property = "id", column = "id"),
      @Result(property = "name", column = "name"), @Result(property = "created", column = "created") })
  public Categories getEntityById(long id);

  @SelectProvider(type = SQLBuilder.class, method = "getCurrentPage")
  @ResultMap(value = "categories")
  public List<Categories> getCurrentPage(CategoriesDataTablesRequest form);

  @Select("SELECT COUNT(id) FROM categories")
  @ResultType(value = Long.class)
  public long getTotalRecord(CategoriesDataTablesRequest form);

  @Delete("DELETE FROM categories WHERE id=#{id}")
  public int delete(long id);

  @Insert("INSERT INTO categories (name) VALUES (#{name})")
  @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
  public int insert(Categories form);

  @UpdateProvider(type = SQLBuilder.class, method = "update")
  public int update(Categories form);

  class SQLBuilder {
    public String getCurrentPage(CategoriesDataTablesRequest form) {
      return new SQL() {
        {
          SELECT("*");
          FROM("categories");
          if (StringUtils.hasLength(form.getName())) {
            form.setName("%" + form.getName() + "%");
            WHERE("name LIKE #{name}");
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

    public String update(Categories form) {
      return new SQL() {
        {
          UPDATE("categories");
          SET("name=#{name}");
          WHERE("id=#{id}");
        }
      }.toString();
    }
  }
}
