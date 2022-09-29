package paperplane.paperplane.domain.userpost;


import lombok.*;
import paperplane.paperplane.domain.post.Post;

import javax.persistence.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserPost {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private paperplane.paperplane.domain.user.User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column
    private Boolean isTempPost;

    @Column
    private Boolean isReport;
}
