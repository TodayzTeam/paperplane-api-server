package paperplane.paperplane.domain.userpost;


import lombok.*;
import paperplane.paperplane.domain.post.Post;
import paperplane.paperplane.domain.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Embeddable
public class UserPost {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    //받는 사람
    @ManyToOne (fetch = FetchType.LAZY)
    private User receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column
    private Boolean isReply;

    @Column
    private Boolean isReport;

    @Column
    private Boolean isRead;
}