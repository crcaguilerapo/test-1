package org.crcaguilerapo.franquicia.infrastructure.adapters.in.http;

import org.crcaguilerapo.franquicia.domain.dtos.Branch;
import org.crcaguilerapo.franquicia.domain.dtos.Error;
import org.crcaguilerapo.franquicia.domain.exceptions.ErrorCode;
import org.crcaguilerapo.franquicia.domain.exceptions.UseCaseException;
import org.crcaguilerapo.franquicia.domain.usecases.BranchUsecase;
import org.crcaguilerapo.franquicia.infrastructure.service.Serialization;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value="/branch", produces = "application/json")
public class BranchController {

    private final BranchUsecase branchUsecase;

    private final Serialization serialization;

    public BranchController(BranchUsecase branchUsecase, Serialization serialization) {
        this.branchUsecase = branchUsecase;
        this.serialization = serialization;
    }

    @PostMapping
    public Mono<ResponseEntity<String>> createBranch(
            @RequestBody Branch branch
    ) {
        return branchUsecase
                .create(branch)
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
    public Mono<ResponseEntity<String>> updateBranch(
            @RequestBody Branch branch
    ) {
        return branchUsecase
                .update(branch).map(v -> ResponseEntity.status(HttpStatus.OK).body(serialization.objectToJson(v)))
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