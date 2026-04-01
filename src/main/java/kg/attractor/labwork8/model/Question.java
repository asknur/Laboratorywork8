package kg.attractor.labwork8.model;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    private Long id;
    private Long quizId;
    private String questionText;
}
