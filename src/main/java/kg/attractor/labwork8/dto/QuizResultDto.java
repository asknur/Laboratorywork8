package kg.attractor.labwork8.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizResultDto {
    private Integer countQuestions;
    private List<QuestionOptionDto> questionOptionDto;
    private Integer countCorrectOptions;
    private Integer options;
}
