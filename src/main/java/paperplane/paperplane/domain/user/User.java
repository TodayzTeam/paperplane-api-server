package paperplane.paperplane.domain.user;


import lombok.*;
import org.springframework.context.annotation.Primary;
import paperplane.paperplane.domain.post.Post;
import paperplane.paperplane.domain.postinterest.PostInterest;
import paperplane.paperplane.domain.user.dto.UserRequestDto;
import paperplane.paperplane.domain.usergroup.UserGroup;
import paperplane.paperplane.domain.userinterest.UserInterest;
import paperplane.paperplane.domain.userpost.UserPost;

import javax.persistence.*;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    private String email;

    @Column
    private String name;

    @Column
    private String profileImageUrl;

    @Column
    private String refreshToken;

    @Column
    private Boolean isReadWeb;

    @Column
    private Boolean isRepliedWeb;

    @Column
    private Boolean isPopularLetterWeb;

    @Column
    private Boolean isReadEmail;

    @Column
    private Boolean isRepliedEmail;

    @Column
    private Boolean isPopularLetterEmail;

    @OneToMany(mappedBy = "user",cascade = {CascadeType.ALL})
    private Set<UserGroup> userGroup;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, mappedBy = "user")
    private Set<UserInterest> userInterest;

    @OneToMany(cascade = {CascadeType.ALL},mappedBy = "receiver")
    private Set<UserPost> userPost;
    
    @OneToMany(mappedBy = "sender")
    private Set<Post> Post;

    @Column
    private int randId;

    @Column
    private Integer tempPost;
    
    public void updateProfile(UserRequestDto.Profile profile){
        this.name = profile.getName();
        this.userInterest.clear();
        this.isReadEmail = profile.getIsReadEmail();
        this.isReadWeb = profile.getIsReadWeb();
        this.isPopularLetterEmail = profile.getIsPopularLetterEmail();
        this.isPopularLetterWeb = profile.getIsPopularLetterWeb();
        this.isRepliedEmail = profile.getIsRepliedEmail();
        this.isRepliedWeb = profile.getIsRepliedWeb();
    }
    
}
