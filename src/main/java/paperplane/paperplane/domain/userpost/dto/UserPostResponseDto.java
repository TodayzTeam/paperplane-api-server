package paperplane.paperplane.domain.userpost.dto;

import lombok.*;
import paperplane.paperplane.domain.post.Post;
import paperplane.paperplane.domain.user.User;
import paperplane.paperplane.domain.userpost.UserPost;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPostResponseDto {


    private Integer id;
    private Boolean isReply;
    private Boolean isReport;
    private Boolean isRead;
    private Boolean isLike;

    public static UserPostResponseDto of(UserPost userPost){
        return UserPostResponseDto.builder()
                .id(userPost.getId())
                .isReply(userPost.getIsReply())
                .isReport(userPost.getIsReport())
                .isRead(userPost.getIsRead())
                .isLike(userPost.getIsLike()).build();
    }


    public static List<UserPostResponseDto> of(List<UserPost> userPosts){
        return userPosts.stream().map(UserPostResponseDto::of).collect(Collectors.toList());
    }
}
