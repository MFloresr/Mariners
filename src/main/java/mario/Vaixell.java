package mario;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "vaixells")
public class Vaixell {
    @Id
    @Column(name = "matricula")
    private String matricula;
    @Column(name = "nom")
    private String nom;
    @OneToMany
    @JoinColumn(name = "tripulant_id")
    private List<Tripulant> mariners;

    private static final long serialVersionUID = 1L;

    public Vaixell(){

    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Tripulant> getMariners() {
        return mariners;
    }

    public void setMariners(List<Tripulant> mariners) {
        this.mariners = mariners;
    }
}