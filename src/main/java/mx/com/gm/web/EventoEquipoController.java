package mx.com.gm.web;

import java.util.List;
import mx.com.gm.domain.EventoEquipo;
import mx.com.gm.dto.EventoEquipoDTO;
import mx.com.gm.service.EventoEquipoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventoEquipoController {
    @Autowired
    EventoEquipoService eeservice;
    
    @GetMapping("/eventosEquipo/equipos")
    public List<EventoEquipo> list(@RequestParam Long id){
        return eeservice.listByIdEvento(id);
    }
    @PostMapping("/eventosEquipo/vincular")
    public ResponseEntity<EventoEquipo> agregarJugador(@RequestBody EventoEquipoDTO vinculacion) {
        EventoEquipo nuevaVinculacion = eeservice.add(vinculacion);
        return ResponseEntity.ok(nuevaVinculacion);
    }
}
