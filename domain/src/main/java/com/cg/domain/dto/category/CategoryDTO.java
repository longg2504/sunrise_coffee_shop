package com.cg.domain.dto.category;

import com.cg.domain.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

//@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class CategoryDTO {
    private Long id;
    private String title;

    public CategoryDTO(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Category toCategory() {
        return new Category()
                .setId(id)
                .setTitle(title);
    }
}
