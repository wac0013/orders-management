package br.com.foursales.app.infrastructure.web.v1;

import org.springframework.web.bind.annotation.RestController;

import br.com.foursales.app.application.dto.ProductCreateRequest;
import br.com.foursales.app.application.dto.ProductResponse;
import br.com.foursales.app.application.service.ProductService;
import jakarta.annotation.security.RolesAllowed;

import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@Validated
@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	@RolesAllowed({"ADMIN", "USER"})
    @GetMapping
    public Page<ProductResponse> getPaginatedProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        var pageable = PageRequest.of(page, size);
        return productService.findAll(pageable);
    }


    @RolesAllowed("ADMIN")
    @PostMapping
    public ProductResponse createProduct(@RequestBody @Validated ProductCreateRequest productRequest) {
        return productService.create(productRequest);
    }

    @RolesAllowed("ADMIN")
    @PutMapping("/{id}")
    public ProductResponse updateProduct(@PathVariable String id, @RequestBody @Validated ProductCreateRequest productRequest) {
        return productService.update(id, productRequest);
    }

    @RolesAllowed("ADMIN")
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable String id) {
        productService.delete(id);
    }

}
