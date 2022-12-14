package paperplane.paperplane.domain.post;


import lombok.*;
import paperplane.paperplane.domain.group.Group;
import paperplane.paperplane.domain.postinterest.PostInterest;
import paperplane.paperplane.domain.user.User;
import paperplane.paperplane.domain.userpost.UserPost;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Post implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
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

    @OneToMany(mappedBy = "post", cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
    private Set<PostInterest> postInterests;

    @OneToMany(mappedBy = "post", cascade = {CascadeType.ALL},orphanRemoval = true,fetch = FetchType.EAGER)
    private Set<UserPost> userPosts;

    @ManyToOne(fetch = FetchType.EAGER)
    private User sender;

}
