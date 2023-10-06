package com.cg.utils;

import com.cg.domain.entity.Avatar;
import com.cg.domain.entity.Product;
import com.cg.exception.DataInputException;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UploadUtils {
    public static final String IMAGE_UPLOAD_FOLDER = "product";
    public static final String IMAGE_UPLOAD_FOLDER_1 = "staff";


    public Map buildImageUploadParams(Avatar productAvatar) {
        if (productAvatar == null || productAvatar.getId() == null)
            throw new DataInputException("Không thể upload hình ảnh của sản phẩm chưa được lưu");

        String publicId = String.format("%s/%s", IMAGE_UPLOAD_FOLDER, productAvatar.getId());

        return ObjectUtils.asMap(
                "public_id", publicId,
                "overwrite", true,
                "resource_type", "image"
        );
    }


    public Map buildImageUploadParamsStaff(Avatar staffAvatar) {
        if (staffAvatar == null || staffAvatar.getId() == null)
            throw new DataInputException("Không thể upload hình ảnh của nhân viên chưa được lưu");

        String publicId = String.format("%s/%s", IMAGE_UPLOAD_FOLDER_1, staffAvatar.getId());

        return ObjectUtils.asMap(
                "public_id", publicId,
                "overwrite", true,
                "resource_type", "image"
        );
    }


    public Map buildImageDestroyParams(Product product, String publicId) {
        if (product == null || product.getId() == null)
            throw new DataInputException("Không thể destroy hình ảnh của sản phẩm không xác định");

        return ObjectUtils.asMap(
                "public_id", publicId,
                "overwrite", true,
                "resource_type", "image"
        );
    }

}