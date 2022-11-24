package paperplane.paperplane.domain.post;


import lombok.*;
import paperplane.paperplane.domain.group.Group;
import paperplane.paperplane.domain.postinterest.PostInterest;
import paperplane.paperplane.domain.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Group group;

    @Column
    private  String title;

    @Column
    private String content;

    @Column
    private LocalDateTime date;

    @Column
    private Integer reportCount;

    @Column
    private Integer likeCount;

    @Column
    private PostColor postColor;

    @OneToMany(mappedBy = "post", cascade = {CascadeType.ALL})
    private Set<PostInterest> postInterests;

}
