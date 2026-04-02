package kg.attractor.labwork8.dao;

import kg.attractor.labwork8.mapper.QuestionMapper;
import kg.attractor.labwork8.model.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class QuestionDao {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameter;

    public Optional<Question> getQuestionById(Long id){
        String sql = "select * from questions where id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new QuestionMapper(), id));
    }

    public List<Question> findQuestionsByQuizId(Long quizId) {
        String sql = "select * from questions where quiz_id = ?";
        return jdbcTemplate.query(sql, new QuestionMapper(), quizId);
    }

    public Long createQuestion(Question question) {
        String sql = "insert into QUESTIONS(quiz_id, question_text) " +
                "values(:quizId, :questionText);";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("quizId", question.getQuizId())
                .addValue("questionText", question.getQuestionText());


        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameter.update(sql, parameters, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }
}
