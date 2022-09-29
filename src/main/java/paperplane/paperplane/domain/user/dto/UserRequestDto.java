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

    @ApiModel(value = "유저 관심사, 이미지 저장")
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Profile{

        @ApiModelProperty(value = "관심사 리스트", required = true)
        private String interest;

        @ApiModelProperty(value = "유저 이미지", required = true)
        private MultipartFile profileImage;
    }
}
