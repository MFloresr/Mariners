package mario;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Controller {
    @FXML
    private TextField textCapita;
    @FXML
    private ListView listVaixells;
    @FXML
    private ListView listMariners;
    @FXML
    private ListView listColles;
    @FXML
    private Button btndades;

    private String dato=null;
    private List<String> nombres = new ArrayList<String>();
    private List<String> apellidos = new ArrayList<String>();
    private Random rand = new Random();
    private static final List<String> abecedario= Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "Ñ", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" );
    private static final List<String> rango= Arrays.asList("Capita", "Mariner", "Cap de colla");
    private int CANTIDADBARCOS = 100;
    private List<String> dnis = new ArrayList<String>();
    private List<String> matriculas = new ArrayList<String>();
    private boolean esCapitan = false;
    @FXML
    public void carregarDades(Event event){
        FileChooser fichero= new FileChooser();
        fichero.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT Files", "*.txt"));
        File archivoEscogido = fichero.showOpenDialog(null);
        System.out.println(archivoEscogido);
        try {
            FileReader fr=new FileReader(archivoEscogido);
            BufferedReader br = new BufferedReader(fr);
            String linea;
            while((linea=br.readLine())!=null){
                if(linea.contains("nombres")){
                    dato = "nombres";
                }
                if(linea.contains("apellidos")){
                    dato = "apellidos";
                }

                if(dato.contains("nombres")){
                    if (!linea.contains("nombres")) {
                        nombres.add(linea);
                    }
                }
                if(dato.contains("apellidos")) {
                    if (!linea.contains("apellidos")) {
                        apellidos.add(linea);
                    }
                }
            }


            EntityManagerFactory emf = Persistence.createEntityManagerFactory("MarinersUnit");
            EntityManager e = emf.createEntityManager();

            for(int i = 0;i<100;i++){
                System.out.println();
            }


            while (CANTIDADBARCOS !=0){
                String matricula = abecedario.get(rand.nextInt(abecedario.size()))+rand.nextInt(((99999 - 00000) + 1))+abecedario.get(rand.nextInt(abecedario.size()))+abecedario.get(rand.nextInt(abecedario.size()))+abecedario.get(rand.nextInt(abecedario.size()));
                if(!matriculas.contains(matricula)){
                    String nombreBarco = nombres.get(rand.nextInt(nombres.size()))+"_"+nombres.get(rand.nextInt(nombres.size()));
                    List<Tripulant> mariners = new ArrayList<Tripulant>();
                    int tripulantes = rand.nextInt(8)+3;
                    while(tripulantes !=0) {
                        String dni = rand.nextInt(((99999 - 00000) + 1)) + abecedario.get(rand.nextInt(abecedario.size()));
                        String nombre = nombres.get(rand.nextInt(nombres.size())) + " " + apellidos.get(rand.nextInt(apellidos.size())) + apellidos.get(rand.nextInt(apellidos.size()));
                        String rang = rango.get(rand.nextInt(rango.size()));
                      //  int capita = 0;
                      //  if(rang.contains("Capita")){
                       //     capita++;
                      //  }

                        if(!dnis.contains("dni")){// //capita == 1){
                            Tripulant tripulant = new Tripulant();
                            tripulant.setDni(dni);
                            tripulant.setNom(nombre);
                            tripulant.setRang(rang);

                            e.getTransaction().begin();
                            e.persist(tripulant);
                            e.getTransaction().commit();
                            mariners.add(tripulant);
                            tripulantes--;

                        }
                    }
                    Vaixell vaixell = new Vaixell();
                    vaixell.setMatricula(matricula);
                    vaixell.setNom(nombreBarco);
                    vaixell.setMariners(mariners);

                    e.getTransaction().begin();
                    e.persist(vaixell);
                    e.getTransaction().commit();
                    CANTIDADBARCOS--;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
