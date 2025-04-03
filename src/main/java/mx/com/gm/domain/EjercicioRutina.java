package mx.com.gm.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Entity
@Table(name = "ejercicio_rutina")
@Data
public class EjercicioRutina implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rutina", nullable = false)
    private Rutina rutina;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false)
    private Integer series;

    @Column(nullable = false, length = 50)
    private String repeticiones;

    @Column(length = 20)
    private String descanso; 

    @Column(nullable = false)
    private Integer orden;
    
    @OneToMany(mappedBy = "ejercicioRutina")
    @JsonManagedReference
    private List<RecursoRutina> recursos;

}
