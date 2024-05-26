package com.adproc8.booku.cart.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor @AllArgsConstructor
public class PatchBooksByIdDto {
    private List<PatchBookDto> patchBookDtos;
}
