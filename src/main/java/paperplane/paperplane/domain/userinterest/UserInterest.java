package paperplane.paperplane.domain.userinterest;


import lombok.*;
import paperplane.paperplane.domain.Interest.Interest;
import paperplane.paperplane.domain.user.User;

import javax.persistence.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserInterest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "interest_id")
    private Interest interest;
}
