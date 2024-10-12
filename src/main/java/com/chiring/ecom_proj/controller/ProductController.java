package com.chiring.ecom_proj.controller;

import com.chiring.ecom_proj.model.Product;
import com.chiring.ecom_proj.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("/products")
   /* public List<Product> getAllProducts(){
        return service.getAllProducts();
    }
    */
    //instead of that we can also use this method
    public ResponseEntity<List<Product>> getALlProducts() {
        return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
   /* public Product getProduct(@PathVariable int id){
        return service.getProductById(id);
    }   */
    public ResponseEntity<Product> getProduct(@PathVariable int id) {
        return new ResponseEntity<>(service.getProductById(id), HttpStatus.OK);
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product,
                                        @RequestPart MultipartFile imageFile){
       try{
           System.out.println(product);
           Product product1 = service.addProduct(product, imageFile);
           return new ResponseEntity<>(product1, HttpStatus.CREATED);
       }
       catch(Exception e){
           return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
       }

    }

    @GetMapping("/product/{productId}/image")
    public ResponseEntity<byte[]> getImageByproductId(@PathVariable int productId){
        Product product=service.getProductById(productId);
        byte[] imageFile= product.getImageDate();

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(product.getImageType()))
                .body(imageFile);
    }
    @PutMapping("/product/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id, @RequestPart Product product,
                                                @RequestPart MultipartFile imageFile){
        Product product1 = null;
        try {
            product1 = service.updateProduct(id, product, imageFile);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upadte", HttpStatus.BAD_REQUEST);
        }
        if(product1 !=null)
            return new ResponseEntity<>("Upadte", HttpStatus.OK);
        else
            return new ResponseEntity<>("Failed to upadte", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<String>  deleteProduct(@PathVariable int id){
        Product product = service.getProductById(id);
        if(product != null){
            service.deleteProduct(id);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        }
        else
            return new ResponseEntity<>("Product not found",HttpStatus.NOT_FOUND);
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword){
        List<Product> products = service.searchProducts(keyword);
        System.out.println("searching with "+ keyword);
        return new ResponseEntity<>(products, HttpStatus.OK);

    }
}
