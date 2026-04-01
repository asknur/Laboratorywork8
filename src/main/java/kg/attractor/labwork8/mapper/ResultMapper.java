package kg.attractor.labwork8.mapper;

import kg.attractor.labwork8.model.QuizResult;
import org.springframework.jdbc.core.RowMapper;;import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultMapper implements RowMapper<QuizResult> {
    @Override
    public QuizResult mapRow(ResultSet rs, int rowNum) throws SQLException {
        QuizResult result = new QuizResult();
        result.setQuizId(rs.getLong("quiz_id"));
        result.setUserId(rs.getLong("user_id"));
        result.setScore(rs.getInt("score"));
        result.setId(rs.getLong("id"));
        return result;
    }
}
