package kg.attractor.labwork8.mapper;

import kg.attractor.labwork8.model.Option;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OptionMapper implements RowMapper<Option> {
    @Override
    public Option mapRow(ResultSet rs, int rowNum) throws SQLException {
        Option option = new Option();
        option.setId(rs.getLong("id"));
        option.setQuestionId(rs.getLong("question_id"));
        option.setOptionText(rs.getString("option_text"));
        option.setIsCorrect(rs.getBoolean("is_correct"));
        return option;
    }
}
