package kg.attractor.labwork8.model;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizResult {
    private Long id;
    private Integer score;
    private Long userId;
    private Long quizId;
}
