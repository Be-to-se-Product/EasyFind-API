package com.be.two.c.apibetwoc.service;

import com.be.two.c.apibetwoc.model.Imagem;
import com.be.two.c.apibetwoc.model.Produto;
import com.be.two.c.apibetwoc.repository.ImagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class ImagemService {

    private final ImagemRepository imagemRepository;
    private final Path caminho = Path.of(System.getProperty("java.io.tmpdir") + "/arquivos");
    public void salvarImagem(String base64Image, String nomeReferencia, Produto produto) {
           byte[] bytes = Base64.getDecoder().decode(base64Image);
        Imagem imagem = new Imagem();
        imagem.setNomeReferencia(nomeReferencia);
        imagem.setProduto(produto);
        String nomeImagem = produto.getNome() + "-" + nomeReferencia;
        imagem.setNomeImagem(nomeImagem);
        imagemRepository.save(imagem);
        try {
            Files.write(caminho.resolve(nomeImagem), bytes);
            System.out.println("Banana frita frita frita frita frita frita");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
