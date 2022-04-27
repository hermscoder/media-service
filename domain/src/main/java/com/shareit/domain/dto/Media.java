package com.shareit.domain.dto;

import com.shareit.domain.entity.MediaType;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Media {
    private Long id;
    private String url;
    private MediaType type;
}
