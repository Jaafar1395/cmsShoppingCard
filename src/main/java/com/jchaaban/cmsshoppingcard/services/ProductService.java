package com.jchaaban.cmsshoppingcard.services;

import com.jchaaban.cmsshoppingcard.models.ProductRepository;
import com.jchaaban.cmsshoppingcard.models.data.Product;
import com.jchaaban.cmsshoppingcard.utilities.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.IOException;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public List<Product> findAll(){
        return repository.findAll();
    }

    public Page<Product> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }

    public Product findById(Integer id){
        return repository.findById(id).get();
    }

    public Page<Product> findAllByCategoryId(Integer categoryId, Pageable pageable) {
        return repository.findAllByCategoryId(categoryId,pageable);
    }

    public Long count() {
        return repository.count();
    }

    public Long countByCategoryId(Integer categoryId) {
        return repository.countByCategoryId(categoryId);
    }


    public boolean productExist(Product product, String slug, boolean editMode){
        Product existingProduct = repository.findBySlug(slug);
        if (existingProduct != null){
            if (editMode)
                return existingProduct.getId() != product.getId();
            return true;
        }
        return false;
    }

    public boolean noFileUploaded(MultipartFile file){
        return file.getSize() == 0;
    }

    public boolean invalidFileFormat(String filename){
        return !(filename.endsWith("jpg") || filename.endsWith("png") || filename.endsWith("jpeg"));
    }

    public void deleteProduct(Integer id) throws IOException {
        Product product = repository.findById(id).get();
        repository.deleteById(id);
        FileUploadUtil.deleteFile("media/" + product.getCategoryName(), product.getImage());
    }

    public void saveNewProduct(Product product,
                               MultipartFile file,
                               String filename, String slug,
                               RedirectAttributes attributes) throws IOException {
        product.setSlug(slug);
        product.setImage(filename);
        String uploadDirectory = "media/" + product.getCategoryName();
        FileUploadUtil.saveFile(uploadDirectory, filename, file);
        handelSuccessOperation(product,"Product was successfully added",attributes);
        repository.save(product);
    }

    public void saveEditedProduct(Product product,Product oldProduct, MultipartFile file,
                                  String filename, String slug, boolean newFileUploaded,
                                  RedirectAttributes attributes) throws IOException {
        product.setSlug(slug);
        product.setImage(filename);
        String oldProductCategoryName = oldProduct.getCategoryName();
        String productCategoryName = product.getCategoryName();
        String uploadDirectory = "media/" + productCategoryName;
        if (newFileUploaded || oldProductCategoryName != productCategoryName) {
            FileUploadUtil.saveFile(uploadDirectory, filename, file);
            FileUploadUtil.deleteFile("media/" + oldProductCategoryName, oldProduct.getImage());
        }
        handelSuccessOperation(null, "Product was successfully edited",attributes);
        repository.save(product);
    }



    public void handelNoFileUploaded(Product product, RedirectAttributes attributes){
        handelFailOperation(product, "Please select an image for the product", attributes);
    }

    public void handelInvalidFile(Product product, RedirectAttributes attributes){
        handelFailOperation(product,"Image must have on of the following extensions: jpg, jpeg, png", attributes);
    }

    public void handelProductExists(Product product, RedirectAttributes attributes){
        handelFailOperation(product,"The product you tried to add already exists",attributes);
    }

    public void handelSuccessOperation(Product product, String message, RedirectAttributes attributes){
        handelRedirectMessages(product, attributes, message, "alert-success");
    }

    public void handelFailOperation(Product product, String message, RedirectAttributes attributes){
        handelRedirectMessages(product, attributes, message, "alert-danger");
    }

    private void handelRedirectMessages(Product product, RedirectAttributes redirectAttributes, String message, String alertClass){
        redirectAttributes.addFlashAttribute("product", product);
        redirectAttributes.addFlashAttribute("message", message);
        redirectAttributes.addFlashAttribute("alertClass", alertClass);
    }
}
