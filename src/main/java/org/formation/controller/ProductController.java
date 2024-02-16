package org.formation.controller;

import java.util.concurrent.ThreadLocalRandom;

import org.formation.dto.ProductDto;
import org.formation.service.IproductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("product")
public class ProductController {


    private IproductService service;
    

    public ProductController(IproductService service) {
		this.service = service;
	}

	@GetMapping("all")
    public Flux<ProductDto> all(){
        return this.service.getAll();
    }

    @GetMapping("price-range")
    public Flux<ProductDto> getByPriceRange(@RequestParam("min") int min,
                                            @RequestParam("max") int max){
        return this.service.getProductByPriceRange(min, max);
    }

    @GetMapping("{id}")
    public Mono<ResponseEntity<ProductDto>> getProductById(@PathVariable String id){
        return this.service.getProductById(id)
                            .map(ResponseEntity::ok)
                            .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
//    @GetMapping("{id}")
//    public Mono<ResponseEntity<ProductDto>> getProductById(@PathVariable String id){
//        this.simulateRandomException();
//        return this.service.getProductById(id)
//                            .map(ResponseEntity::ok)
//                            .defaultIfEmpty(ResponseEntity.notFound().build());
//    }

    @PostMapping
    public Mono<ProductDto> insertProduct(@RequestBody Mono<ProductDto> productDtoMono){
        return this.service.insertProduct(productDtoMono);
    }

    @PutMapping("{id}")
    public Mono<ResponseEntity<ProductDto>> updateProduct(@PathVariable String id, @RequestBody Mono<ProductDto> productDtoMono){
       return this.service.updateProduct(id, productDtoMono)
                    .map(ResponseEntity::ok)
                    .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public Mono<Void> deleteProduct(@PathVariable String id){
        return this.service.deleteProduct(id);
    }

    private void simulateRandomException(){
        int nextInt = ThreadLocalRandom.current().nextInt(1, 10);
        if(nextInt > 0)
            throw new RuntimeException("something is wrong");
    }

}
