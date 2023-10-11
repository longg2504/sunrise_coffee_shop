package com.cg.domain.dto.product;

import com.cg.domain.entity.Category;
import com.cg.domain.entity.Product;
import com.cg.domain.entity.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class ProductUpReqDTO implements Validator {
    private String title;
    private String price;
    private String unitId;
    private String categoryId;
    private MultipartFile productAvatar;

    @Override
    public boolean supports(Class<?> clazz) {
        return ProductCreReqDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProductUpReqDTO productUpReqDTO= (ProductUpReqDTO) target;
        String title = productUpReqDTO.title;
        String priceStr = productUpReqDTO.price;
        if(title.isEmpty()) {
            errors.rejectValue("title","title.null","Tên sản phẩm không được phép rỗng");
        }

        if(priceStr.isEmpty()) {
            errors.rejectValue("price","price.length","Vui lòng nhập giá tiền");
        } else {
            if (!priceStr.matches("^-?\\d+$")){
                errors.rejectValue("price", "price.matches", "Vui lòng nhập giá tiền bằng chữ số");
            } else {
                BigDecimal price = BigDecimal.valueOf(Long.parseLong(priceStr));
                if(price.compareTo(BigDecimal.ZERO) <= 0) {
                    errors.rejectValue("price", "price.min", "Số tiền phải lớn hơn 0");
                }
            }
        }
    }

    public ProductCreReqDTO toProductCreReqDTO() {
        return new ProductCreReqDTO()
                .setTitle(title)
                .setPrice(price)
                .setUnitId(unitId)
                .setCategoryId(categoryId)
                .setProductAvatar(productAvatar);
    }

    public Product toProductChangeImage(Unit unit, Category category) {
        return new Product()
                .setTitle(title)
                .setPrice(BigDecimal.valueOf(Long.parseLong(price)))
                .setUnit(unit)
                .setCategory(category);
    }
}
