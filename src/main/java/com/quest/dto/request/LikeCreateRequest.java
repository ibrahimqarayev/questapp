package com.quest.dto.request;

import lombok.Data;

@Data
public class LikeCreateRequest {
    private Long id;
    private Long postId;
    private Long userId;
}
