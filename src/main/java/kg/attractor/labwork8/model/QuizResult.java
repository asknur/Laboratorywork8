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
    private Double score;
    private Long userId;
    private Long quizId;
}
