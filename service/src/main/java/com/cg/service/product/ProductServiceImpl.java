package com.cg.service.product;

import com.cg.domain.dto.product.ProductCreReqDTO;
import com.cg.domain.dto.product.ProductDTO;
import com.cg.domain.dto.product.ProductUpReqDTO;
import com.cg.domain.entity.Avatar;
import com.cg.domain.entity.Category;
import com.cg.domain.entity.Product;
import com.cg.domain.entity.Unit;
import com.cg.exception.DataInputException;
import com.cg.repository.avatar.AvatarRepository;
import com.cg.repository.product.ProductRepository;
import com.cg.service.upload.IUploadService;
import com.cg.utils.UploadUtils;
import com.cg.utils.ValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AvatarRepository avatarRepository;
    @Autowired
    private IUploadService uploadService;
    @Autowired
    private UploadUtils uploadUtils;
    @Autowired
    private ValidateUtils validateUtils;

    @Override
    public List<ProductDTO> findAllProductDTO() {
        return productRepository.findAllProductDTO();
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void delete(Product product) {
        productRepository.delete(product);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Optional<Product> findByIdAndDeletedFalse(Long id) {
        return productRepository.findByIdAndDeletedFalse(id);
    }

    @Override
    public void deleteByIdTrue(Product product) {
        product.setDeleted(true);
        productRepository.save(product);
    }

    @Override
    public List<ProductDTO> findProductByName(String keySearch) {
        return productRepository.findProductsByTitleContainingIgnoreCase(keySearch);
    }


    @Override
    public Product createProduct(ProductCreReqDTO productCreReqDTO, Category category, Unit unit) {
        Avatar avatar = new Avatar();
        avatarRepository.save(avatar);
        uploadAndSaveProductImage(productCreReqDTO, avatar);
        Product product = productCreReqDTO.toProduct(category, unit);
        product.setProductAvatar(avatar);
        productRepository.save(product);
        return product;
    }

    @Override
    public Product update(Long id, ProductUpReqDTO productUpReqDTO, Category category, Unit unit) {
        Avatar avatar = new Avatar();
        avatarRepository.save(avatar);
        uploadAndSaveProductImage(productUpReqDTO.toProductCreReqDTO(), avatar);
        Product productUpdate = productUpReqDTO.toProductChangeImage(unit, category);
        productUpdate.setId(id);
        productUpdate.setProductAvatar(avatar);
        productRepository.save(productUpdate);
        return productUpdate;
    }

    public void uploadAndSaveProductImage(ProductCreReqDTO productCreReqDTO, Avatar avatar) {
        try {
            Map uploadResult = uploadService.uploadImage(productCreReqDTO.getProductAvatar(), uploadUtils.buildImageUploadParams(avatar));
            String fileUrl = (String) uploadResult.get("secure_url");
            String fileFormat = (String) uploadResult.get("format");

            avatar.setFileName(avatar.getId() + "." + fileFormat);
            avatar.setFileUrl(fileUrl);
            avatar.setFileFolder(UploadUtils.IMAGE_UPLOAD_FOLDER);
            avatar.setCloudId(avatar.getFileFolder() + "/" + avatar.getId());
            avatarRepository.save(avatar);
        } catch (IOException e) {
            e.printStackTrace();
            throw new DataInputException("Upload hình ảnh thất bại");
        }
    }
}
