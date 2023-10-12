package com.be.two.c.apibetwoc.dto.comerciante;

import com.be.two.c.apibetwoc.dto.UsuarioCriacaoDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ComercianteCriacaoDto {

    @NotBlank
    private String cnpj;
    @NotBlank
    private String nome;
    @NotBlank
    private String razaoSocial;
    @Valid
    private UsuarioCriacaoDTO usuarioCriacaoDTO;
    @NotBlank
    private String cep;
}
