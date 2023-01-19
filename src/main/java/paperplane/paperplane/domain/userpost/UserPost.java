package paperplane.paperplane.domain.userpost;


import lombok.*;
import paperplane.paperplane.domain.post.Post;
import paperplane.paperplane.domain.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

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

    //받는 사람
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user")
    private User receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post")
    private Post post;


    //신고 여부
    @Column
    private Boolean isReport;

    //읽었는지 여부
    @Column
    private Boolean isRead;

    //좋아요 눌렀는지 여부
    @Column
    private Boolean isLike;

    @Column
    private Boolean isReply;
}
