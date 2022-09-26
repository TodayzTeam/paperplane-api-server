package paperplane.paperplane.domain.postinterest;

import lombok.*;
import paperplane.paperplane.domain.Interest.Interest;
import paperplane.paperplane.domain.post.Post;

import javax.persistence.*;


@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostInterest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interest_id")
    private Interest interest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
}
