package kg.attractor.labwork8.model;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Option {
    private Long id;
    private Boolean isCorrect;
    private String optionText;
    private Long questionId;
}
