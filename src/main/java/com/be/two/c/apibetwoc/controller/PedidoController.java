package com.be.two.c.apibetwoc.controller;

import com.be.two.c.apibetwoc.dto.pedido.ItemVendaCriacaoDto;
import com.be.two.c.apibetwoc.dto.pedido.PedidoMapper;
import com.be.two.c.apibetwoc.dto.pedido.ResponsePedidoDTO;
import com.be.two.c.apibetwoc.model.Pedido;
import com.be.two.c.apibetwoc.service.ItemVendaService;
import com.be.two.c.apibetwoc.service.PedidoService;
import com.be.two.c.apibetwoc.util.ListaObj;
import com.be.two.c.apibetwoc.util.StatusPedido;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;
    private final ItemVendaService itemVendaService;

    @PostMapping
    public ResponseEntity<ResponsePedidoDTO> cadastrar(@RequestBody @Valid List<ItemVendaCriacaoDto> itensVenda) {
        Pedido pedido = itemVendaService.cadastrarItensVenda(itensVenda);
        return ResponseEntity.ok(PedidoMapper.of(pedido));
    }

    @PatchMapping("{idPedido}/status")
    public ResponseEntity<Void> atualizarStatusPedido(@PathVariable Long idPedido, @RequestBody StatusPedido status) {
        pedidoService.alterarStatusPedido(idPedido, status);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/consumidor/{idConsumidor}")
    public ResponseEntity<List<ResponsePedidoDTO>> buscarPedidosPorConsumidor(@PathVariable Long idConsumidor) {
        List<ResponsePedidoDTO> pedidos = pedidoService.listarPorConsumidor(idConsumidor);
        if (pedidos.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/estabelecimento/{idEstabelecimento}")
    public ResponseEntity<ListaObj<ResponsePedidoDTO>> buscarPedidosPorEstabelecimento(@PathVariable Long idEstabelecimento) {
        ListaObj<ResponsePedidoDTO> pedidos = pedidoService.listarPorEstabelecimento(idEstabelecimento);

        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/estabelecimento/{idEstabelecimento}/status")
    public ResponseEntity<List<Pedido>> buscarPedidosPorEstabelecimentoEStatus(@PathVariable Long idEstabelecimento, @RequestParam String status) {
        List<Pedido> pedidos = pedidoService.listarPorEstabelecimentoEStatus(idEstabelecimento, status);
        if (pedidos.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(pedidos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pedidoService.deletar(id);
        return ResponseEntity.noContent().build();
    }


}

