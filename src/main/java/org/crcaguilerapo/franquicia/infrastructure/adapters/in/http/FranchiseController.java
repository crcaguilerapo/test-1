package org.crcaguilerapo.franquicia.infrastructure.adapters.in.http;

import org.crcaguilerapo.franquicia.domain.dtos.Error;
import org.crcaguilerapo.franquicia.domain.dtos.Franchise;
import org.crcaguilerapo.franquicia.domain.exceptions.ErrorCode;
import org.crcaguilerapo.franquicia.domain.exceptions.UseCaseException;
import org.crcaguilerapo.franquicia.domain.usecases.FranchiseUsecase;
import org.crcaguilerapo.franquicia.infrastructure.service.Serialization;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value="/franchise", produces = "application/json")
public class FranchiseController {

    private final FranchiseUsecase franchiseUsecase;
    private final Serialization serialization;

    public FranchiseController(FranchiseUsecase franchiseUsecase, Serialization serialization) {
        this.franchiseUsecase = franchiseUsecase;
        this.serialization = serialization;
    }

    @PostMapping
    public Mono<ResponseEntity<String>> createFranchise(
            @RequestBody Franchise franchise
    ) {
        return franchiseUsecase
                .create(franchise)
                .map(v -> ResponseEntity.status(HttpStatus.OK).body(serialization.objectToJson(v)))
                .onErrorResume(e -> {
                    if (e instanceof UseCaseException useCaseException) {
                        return Mono.just(
                                ResponseEntity.status(HttpStatus.OK)
                                        .body(serialization.objectToJson(new Error(useCaseException.errorCode)))
                        );
                    } else {
                        return Mono.just(
                                ResponseEntity.status(HttpStatus.OK)
                                        .body(serialization.objectToJson(new Error(ErrorCode.UNKNOWN)))
                        );
                    }
                });
    }

    @PutMapping
    public Mono<ResponseEntity<String>> updateFranchise(
            @RequestBody Franchise franchise
    ) {
        return franchiseUsecase
                .update(franchise)
                .map(v -> ResponseEntity.status(HttpStatus.OK).body(serialization.objectToJson(v)))
                .onErrorResume(e -> {
                    if (e instanceof UseCaseException useCaseException) {
                        return Mono.just(
                                ResponseEntity.status(HttpStatus.OK)
                                        .body(serialization.objectToJson(new Error(useCaseException.errorCode)))
                        );
                    } else {
                        return Mono.just(
                                ResponseEntity.status(HttpStatus.OK)
                                        .body(serialization.objectToJson(new Error(ErrorCode.UNKNOWN)))
                        );
                    }
                });
    }

}
