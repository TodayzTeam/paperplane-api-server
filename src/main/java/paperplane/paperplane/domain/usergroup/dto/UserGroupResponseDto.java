package paperplane.paperplane.domain.usergroup.dto;

import lombok.*;
import paperplane.paperplane.domain.user.dto.UserResponseDto;
import paperplane.paperplane.domain.usergroup.UserGroup;
import paperplane.paperplane.domain.usergroup.UserRole;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserGroupResponseDto {

   private Integer groupId;
   private String groupName;
   private Integer userId;
   private String userName;
   private UserRole userRole;

   public static UserGroupResponseDto of(UserGroup userGroup){
      return UserGroupResponseDto.builder()
              .groupId(userGroup.getGroup().getId())
              .groupName(userGroup.getGroup().getName())
              .userId(userGroup.getUser().getId())
              .userName(userGroup.getUser().getName())
              .userRole(userGroup.getUserRole())
              .build();
   }

   public static List<UserGroupResponseDto> of(List<UserGroup> userGroups){
      return userGroups.stream().map(UserGroupResponseDto::of).collect(Collectors.toList());
   }
}
