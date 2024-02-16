package org.formation.service;

import org.formation.dto.ProductDto;
import org.formation.entity.Product;
import org.springframework.data.mongodb.core.ChangeStreamEvent;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IproductService {

	Flux<ProductDto> getAll();

	Flux<ProductDto> getProductByPriceRange(int min, int max);

	Mono<ProductDto> getProductById(String id);

	Mono<ProductDto> insertProduct(Mono<ProductDto> productDtoMono);

	Mono<ProductDto> updateProduct(String id, Mono<ProductDto> productDtoMono);

	Mono<Void> deleteProduct(String id);
	
//	Flux<ChangeStreamEvent<Product>> watchDatabaseForNewProduct();

}