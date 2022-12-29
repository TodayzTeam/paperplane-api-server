package paperplane.paperplane.domain.userpost.dto;

import lombok.*;
import paperplane.paperplane.domain.post.Post;
import paperplane.paperplane.domain.user.User;
import paperplane.paperplane.domain.userinterest.UserInterest;
import paperplane.paperplane.domain.userinterest.dto.UserInterestResponseDto;
import paperplane.paperplane.domain.userpost.UserPost;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPostResponseDto {


    private Integer id;
    private User user;
    private Post post;
    private Boolean isReply;
    private Boolean isReport;
    private Boolean isRead;

    public static UserPostResponseDto of(UserPost userPost){
        return UserPostResponseDto.builder()
                .id(userPost.getId())
                .user(userPost.getReceiver())
                .post(userPost.getPost())
                .isReply(userPost.getIsReply())
                .isReport(userPost.getIsReport())
                .isRead(userPost.getIsRead()).build();
    }


    public static List<UserPostResponseDto> of(List<UserPost> userInterests){
        return userInterests.stream().map(UserPostResponseDto::of).collect(Collectors.toList());
    }
}
