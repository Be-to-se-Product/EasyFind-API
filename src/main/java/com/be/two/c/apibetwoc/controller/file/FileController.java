package com.be.two.c.apibetwoc.controller.file;


import com.be.two.c.apibetwoc.service.arquivo.ArquivoService;
import com.be.two.c.apibetwoc.service.arquivo.dto.ArquivoReponseDTO;
import com.be.two.c.apibetwoc.util.TipoArquivo;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/imagens")
@RequiredArgsConstructor
public class FileController {

    private final ArquivoService arquivoService;
    @GetMapping("/{nomeArquivo}")
    public ResponseEntity<Resource> getImagem(@PathVariable String nomeArquivo ) {
        ArquivoReponseDTO imagem = arquivoService.getArquivo(nomeArquivo, TipoArquivo.IMAGEM);

        return ResponseEntity.ok().contentType(imagem.getTipoArquivo())
                .body(imagem.getUrl());

    }

}
