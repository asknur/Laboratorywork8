package kg.attractor.labwork8.mapper;

import kg.attractor.labwork8.model.Question;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QuestionMapper implements RowMapper<Question> {
    @Override
    public Question mapRow(ResultSet rs, int rowNum) throws SQLException {
        Question question = new Question();
        question.setId(rs.getLong("id"));
        question.setQuestionText(rs.getString("question_text"));
        question.setQuizId(rs.getLong("quiz_id"));
        return question;
    }
}
