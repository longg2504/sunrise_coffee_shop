package com.cg.domain.dto.avatar;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class AvatarDTO {
    private String id;

    private String fileName;

    private String fileFolder;

    private String fileUrl;

    private String fileType;
    private String cloudId;
    private Integer width;
    private Integer height;
}
