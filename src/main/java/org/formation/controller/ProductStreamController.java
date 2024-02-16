package org.formation.controller;

import org.formation.dto.ProductDto;
import org.formation.service.IproductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("product")
public class ProductStreamController {

    @Autowired
    private Flux<ProductDto> flux;
    @Autowired
    private IproductService service;
    

    @GetMapping(value = "stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProductDto> getProductUpdates(){
        return flux;
    }
    

    @GetMapping(value = "streammax/{maxPrice}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProductDto> getProductUpdates(@PathVariable int maxPrice){
        return flux
                    .filter(dto -> dto.getPrice() <= maxPrice);
    }

    
//    @GetMapping(path = "stream-changes", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public void streamChanges() {
//    	service.watchDatabaseForNewProduct();
//    }

}
