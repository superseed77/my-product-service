package org.formation.service;

import org.formation.dto.ProductDto;
import org.formation.entity.Product;
import org.formation.repository.ProductRepository;
import org.formation.util.EntityDtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.data.mongodb.core.ChangeStreamEvent;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
public class ProductService implements IproductService {
	@Autowired
	private ReactiveMongoTemplate reactiveMongoTemplate;

	private ProductRepository repository;

	public ProductService(ProductRepository repository) {
		this.repository = repository;
	}

	@Autowired
	private Sinks.Many<ProductDto> sink;

	@Override
	public Flux<ProductDto> getAll() {
		return this.repository.findAll().map(EntityDtoUtil::toDto);
	}

	@Override
	public Flux<ProductDto> getProductByPriceRange(int min, int max) {
		return this.repository.findByPriceBetween(Range.closed(min, max)).map(EntityDtoUtil::toDto);
	}

	@Override
	public Mono<ProductDto> getProductById(String id) {
		return this.repository.findById(id).map(EntityDtoUtil::toDto);
	}

//	@Override
//	public Mono<ProductDto> insertProduct(Mono<ProductDto> productDtoMono) {
//		return productDtoMono.map(EntityDtoUtil::toEntity).flatMap(this.repository::insert).
//				map(EntityDtoUtil::toDto);
//				
//	}

	@Override
	public Mono<ProductDto> insertProduct(Mono<ProductDto> productDtoMono) {
		return productDtoMono.map(EntityDtoUtil::toEntity).flatMap(this.repository::insert).map(EntityDtoUtil::toDto)
				.doOnNext(this.sink::tryEmitNext);
	}

	@Override
	public Mono<ProductDto> updateProduct(String id, Mono<ProductDto> productDtoMono) {
		return this.repository.findById(id)
				.flatMap(p -> productDtoMono.map(EntityDtoUtil::toEntity).doOnNext(e -> e.setId(id)))
				.flatMap(this.repository::save).map(EntityDtoUtil::toDto);
	}

	@Override
	public Mono<Void> deleteProduct(String id) {
		return this.repository.deleteById(id);
	}

//	@Override
//	public Flux<ChangeStreamEvent<Product>> watchDatabaseForNewProduct() {
//
//		Flux<ChangeStreamEvent<Product>> flux = reactiveMongoTemplate
//				.changeStream(Product.class)
//				.watchCollection("product")
//				// .filter(where("age").gte(38))
//				.listen().doOnNext(System.out::println);
//		return flux;
//	}

}
