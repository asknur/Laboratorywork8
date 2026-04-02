package kg.attractor.labwork8.model;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {
    private Long id;
    private Long creatorId;
    private String description;
    private String title;

    private Integer countOfQuestions;

}
