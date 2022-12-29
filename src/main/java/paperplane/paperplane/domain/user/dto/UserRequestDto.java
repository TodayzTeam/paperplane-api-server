package paperplane.paperplane.domain.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.elasticsearch.client.license.LicensesStatus;
import org.springframework.web.multipart.MultipartFile;
import paperplane.paperplane.domain.Interest.Interest;
import paperplane.paperplane.domain.Interest.dto.InterestRequestDto;
import paperplane.paperplane.domain.user.User;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.util.List;

public class UserRequestDto {

    @ApiModel(value = "회원 가입/로그인")
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Join{
        @NotNull
        @ApiModelProperty(value = "이름", required = true)
        private String name;

        @NotNull
        @ApiModelProperty(value = "이메일", required = true)
        private String email;



        public User toEntity(){
            return User.builder()
                    .email(this.email)
                    .name(this.name)
                    .isReadWeb(false)
                    .isRepliedWeb(false)
                    .isPopularLetterWeb(false)
                    .isReadEmail(false)
                    .isRepliedEmail(false)
                    .isPopularLetterEmail(false).build();
        }
    }

    @ApiModel(value = "이름, 유저 관심사, 이미지 저장/수정")
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Profile{
        @NotNull
        @ApiModelProperty(value = "이름", required = true)
        private String name;

        @ApiModelProperty(value = "관심사 리스트", required = true)
        private String interest;

        @ApiModelProperty(value = "유저 이미지", required = true)
        private MultipartFile profileImage;

        @ApiModelProperty(value = "내가 보낸 편지가 읽었을때 웹 알림 여부", required = false)
        private Boolean isReadWeb;

        @ApiModelProperty(value = "답장 오는거 확인 웹 알림 여부", required = false)
        private Boolean isRepliedWeb;

        @ApiModelProperty(value = "인기 편지 등록됬을때 웹 알림 여부", required = false)
        private Boolean isPopularLetterWeb;

        @ApiModelProperty(value = "내가 보낸 편지가 읽었을때 이메일 알림 여부", required = false)
        private Boolean isReadEmail;

        @ApiModelProperty(value = "답장 오는거 확인 이메일 알림 여부", required = false)
        private Boolean isRepliedEmail;

        @ApiModelProperty(value = "인기 편지 등록됬을때 이메일 알림 여부", required = false)
        private Boolean isPopularLetterEmail;
    }


}
