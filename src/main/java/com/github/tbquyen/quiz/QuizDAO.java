package com.github.tbquyen.quiz;

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
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import com.github.tbquyen.datatables.params.DataTablesOrder;
import com.github.tbquyen.entity.Quiz;

@Mapper
public interface QuizDAO {
  @Select("SELECT * FROM quiz WHERE id = #{id}")
  @Results(id = "quiz", value = { @Result(property = "id", column = "id"), @Result(property = "name", column = "name"),
      @Result(property = "maxQuestion", column = "max_question"),
      @Result(property = "minQuestion", column = "min_question"), @Result(property = "remark", column = "remark"),
      @Result(property = "duration", column = "duration"), @Result(property = "created", column = "created") })
  public Quiz getById(long id);

  @SelectProvider(type = SQLBuilder.class, method = "getCurrentPage")
  @ResultMap(value = "quiz")
  public List<Quiz> getCurrentPage(QuizDataTablesRequest form);

  @Select("SELECT COUNT(id) FROM quiz")
  @ResultType(value = Long.class)
  public long getTotalRecord(QuizDataTablesRequest form);

  @Insert("INSERT INTO quiz (name,max_question,min_question,remark,duration) VALUES (#{name},#{maxQuestion},#{minQuestion},#{remark},#{duration})")
  @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
  public int insert(Quiz form);

  @Update("UPDATE quiz SET name=#{name},max_question=#{maxQuestion},min_question=#{minQuestion},remark=#{remark},duration=#{duration} WHERE id=#{id}")
  public int update(Quiz form);

  @Delete("DELETE FROM quiz WHERE id=#{id}")
  public int delete(long id);

  class SQLBuilder {
    public String getCurrentPage(QuizDataTablesRequest form) {
      return new SQL() {
        {
          SELECT("*");
          FROM("quiz");
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
  }
}
