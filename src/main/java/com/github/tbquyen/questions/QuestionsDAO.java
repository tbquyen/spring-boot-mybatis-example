package com.github.tbquyen.questions;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import com.github.tbquyen.datatables.params.DataTablesOrder;
import com.github.tbquyen.entity.Answers;

@Mapper
public interface QuestionsDAO {
  @Select("SELECT * FROM questions WHERE id = #{id}")
  @Results(id = "question", value = { @Result(property = "id", column = "id"),
      @Result(property = "categoryId", column = "category_id"), @Result(property = "content", column = "content"),
      @Result(property = "type", column = "type"), @Result(property = "created", column = "created"),
      @Result(property = "answers", column = "id", javaType = List.class, many = @Many(select = "getAnswers")) })
  public QuestionAnswers getQuestionsById(long id);

  @Select("SELECT * FROM answers WHERE question_id = #{questionId}")
  @Results(id = "answer", value = { @Result(property = "id", column = "id"),
      @Result(property = "questionId", column = "question_id"), @Result(property = "content", column = "content"),
      @Result(property = "correct", column = "correct"), @Result(property = "created", column = "created") })
  public List<Answers> getAnswers(long questionId);

  @SelectProvider(type = SQLBuilder.class, method = "getCurrentPage")
  @Results(id = "questions", value = { @Result(property = "id", column = "id"),
      @Result(property = "categoryId", column = "category_id"), @Result(property = "content", column = "content"),
      @Result(property = "categoryName", column = "name"), @Result(property = "created", column = "created") })
  public List<QuestionAnswers> getCurrentPage(QuestionsDataTablesRequest form);

  @Select("SELECT COUNT(id) FROM questions")
  @ResultType(value = Long.class)
  public long getTotalRecord(QuestionsDataTablesRequest form);

  @Insert("INSERT INTO questions (category_id,content,type) VALUES (#{categoryId},#{content},#{type})")
  @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
  public int insertQuestions(QuestionAnswers form);

  @Insert("INSERT INTO answers (question_id,content,correct) VALUES (#{questionId},#{content},#{correct})")
  @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
  public int insertAnswers(Answers answer);

  @Update("UPDATE questions SET category_id=#{categoryId},content=#{content},type=#{type} WHERE id=#{id}")
  public int updateQuestions(QuestionAnswers form);

  @Update("UPDATE answers SET question_id=#{questionId},content=#{content},correct=#{correct} WHERE id=#{id}")
  public int updateAnswers(Answers answer);

  @Delete("DELETE FROM questions WHERE id=#{id}")
  public int deleteQuestions(long id);

  @Delete("DELETE FROM answers WHERE id=#{id}")
  public int deleteAnswers(long id);

  class SQLBuilder {
    public String getCurrentPage(QuestionsDataTablesRequest form) {
      return new SQL() {
        {
          SELECT("questions.*, categories.name");
          FROM("questions");
          INNER_JOIN("categories ON questions.category_id = categories.id");
          if (form.getCategoryId() > 0) {
            WHERE("category_id = #{categoryId}");
          }
          if (StringUtils.hasLength(form.getContent())) {
            form.setContent("%" + form.getContent() + "%");
            WHERE("content LIKE #{content}");
          }
          OFFSET(form.getStart());
          LIMIT(form.getLength());
          for (DataTablesOrder order : form.getOrder()) {
            ORDER_BY(order.toSQL());
          }
        }
      }.toString();
    }
  }
}
