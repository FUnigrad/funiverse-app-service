package com.unigrad.funiverseappservice.payload.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Long id;

    private String content;

    private EntityBaseDTO owner;

    private LocalDateTime createdDateTime;

    private EntityBaseDTO group;

    private List<CommentDTO> comments;
}