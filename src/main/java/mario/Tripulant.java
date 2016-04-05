package mario;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tripulants")
public class Tripulant {
    @Id
    @Column(name = "dni")
    private String dni;
    @Column(name = "nom")
    private String nom;
    @Column(name = "rang")
    private String rang;

    private static  final long serialVersionUID =1L;

    public Tripulant(){

    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getRang() {
        return rang;
    }

    public void setRang(String rang) {
        this.rang = rang;
    }
}
