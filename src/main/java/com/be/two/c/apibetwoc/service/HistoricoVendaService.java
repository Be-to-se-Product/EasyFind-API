package com.be.two.c.apibetwoc.service;

import com.be.two.c.apibetwoc.model.Estabelecimento;
import com.be.two.c.apibetwoc.model.MetodoPagamentoAceito;
import com.be.two.c.apibetwoc.model.ItemVenda;
import com.be.two.c.apibetwoc.model.Produto;
import com.be.two.c.apibetwoc.model.Transacao;
import com.be.two.c.apibetwoc.repository.MetodoPagamentoAceitoRepository;
import com.be.two.c.apibetwoc.repository.TransacaoRepository;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service
@RequiredArgsConstructor
public class HistoricoVendaService {

    private final TransacaoRepository transacaoRepository;
    private final MetodoPagamentoAceitoRepository metodoPagamentoAceitoRepository;
    private final EstabelecimentoService estabelecimentoService;

    public Page<Transacao> getHistoricoVenda(int page, int size, Long id) {
        Pageable pageable = PageRequest.of(page, size);
        return transacaoRepository.findAllByPedidoMetodoPagamentoAceitoEstabelecimentoId(pageable, id);
    }

    public Page<Transacao> getHistoricoPorFiltro(String de,
                                                 String ate,
                                                 String status,
                                                 String nomeMetodoPagamento,
                                                 int page,
                                                 int size,
                                                 Long id) {
        Pageable pageable = PageRequest.of(page, size);
        Specification<Transacao> specification = Specification.where(null);

        if (de != null && ate != null) {
            LocalDate deData = LocalDate.parse(de);
            LocalDate ateData = LocalDate.parse(ate);
            specification = specification.or(TransacaoSpecification.entreDatas(deData, ateData));
        }
        if (status != null) {
            specification = specification.or(TransacaoSpecification.comStatus(status));
        }
        if (nomeMetodoPagamento != null) {
            specification = specification.or(TransacaoSpecification.comMetodoPagamento(nomeMetodoPagamento));
        }
        System.out.println(specification);
        System.out.println(id);
        System.out.println(pageable);
        return transacaoRepository.findAllByPedidoMetodoPagamentoAceitoEstabelecimentoId(specification, pageable, id);
    }

    public List<MetodoPagamentoAceito> listarMetodosPagamentoAceitos(Long id) {
        Estabelecimento estabelecimento = estabelecimentoService.listarPorId(id);
        return metodoPagamentoAceitoRepository.findByEstabelecimento(estabelecimento);
    }
    public byte[] downloadTxt(Long idEstabelecimento) {
        List<Transacao> vendas = transacaoRepository
                .findByPedidoMetodoPagamentoAceitoEstabelecimentoId(idEstabelecimento);

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream);

            String header = "00NOTA20232";
            header += LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
            header += "01";

            outputStreamWriter.write(header + "\n");

            for (Transacao t : vendas) {
                String corpo = "02 ";
                corpo += String.format("%06d ",t.getPedido().getId());
                corpo += String.format("%-10.10s ",t.getPedido().getDataHoraPedido().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                corpo += String.format("%-11.11s ",t.getPedido().getItens().get(0).getConsumidor().getCpf());
                corpo += String.format("%-14.14s ",t.getPedido().getIsPagamentoOnline() ? "Pago pelo site" : "Pago na loja");
                corpo += String.format("%-17.17s ",t.getPedido().getMetodoPagamentoAceito().getMetodoPagamento().getDescricao());
                corpo += String.format("%10.2f",t.getValor());

                outputStreamWriter.write(corpo + "\n");
            }

            String trailer = "01";
            trailer += String.format("%010d", vendas.size());

            outputStreamWriter.write(trailer);

            outputStreamWriter.close();
            return byteArrayOutputStream.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] downloadCsv(Long idPedido) {
        Transacao venda = transacaoRepository.findByPedidoId(idPedido);

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream);

            ICSVWriter csvWriter = new CSVWriterBuilder(outputStreamWriter)
                    .withSeparator(';')
                    .build();

            String[] cabecalho = {"Nome", "Codigo SKU", "Preço", "Descrição", "Categoria","Seção"};
            csvWriter.writeNext(cabecalho);

            for (ItemVenda item : venda.getPedido().getItens()) {
                Produto p = item.getProduto();

                String[] linha = {p.getNome(), p.getCodigoSku(), p.getPreco().toString(), p.getDescricao(), p.getCategoria(), p.getSecao().getDescricao()};

                csvWriter.writeNext(linha);
            }

            csvWriter.close();
            outputStreamWriter.close();
            byte[] csvBytes = byteArrayOutputStream.toByteArray();

            return csvBytes;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
