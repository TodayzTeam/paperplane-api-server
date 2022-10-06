package paperplane.paperplane.domain.Interest;

import lombok.*;

import javax.persistence.*;


@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Interest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    private String keyword;

    @Column
    private Integer count;
}
