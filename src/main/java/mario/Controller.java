package mario;

import javafx.beans.binding.StringBinding;
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
import java.util.List;
import java.util.Random;

/**
 * Created by Vipi on 05/04/2016.
 */
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
    private List<Tripulant> mariners = new ArrayList<Tripulant>();
    private Random rand = new Random();
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

            for(int x= 0 ;x<apellidos.size();x++){
                //System.out.println(apellidos.get(x));
            }


            EntityManagerFactory emf = Persistence.createEntityManagerFactory("MarinersUnit");
            EntityManager e = emf.createEntityManager();

            for (int i = 0; i<10;i++){
                String nombre= nombres.get(rand.nextInt(nombres.size()));
                System.out.println(nombre+"_------");
                String apellido= apellidos.get(rand.nextInt(apellidos.size()))+" "+ apellidos.get(rand.nextInt(apellidos.size()));
                String nomCognoms = nombre+" "+apellido;
                System.out.println(nomCognoms);

            }
            System.out.println("funcionando");
            Tripulant tripulant = new Tripulant();
            tripulant.setDni("34523L");
            tripulant.setNom("Mario");
            tripulant.setRang("Capita");

            e.getTransaction().begin();
            e.persist(tripulant);
            e.getTransaction().commit();

            mariners.add(tripulant);
            Vaixell vaixell = new Vaixell();
            vaixell.setMatricula("T4573T");
            vaixell.setNom("Los tigres");
            vaixell.setMariners(mariners);

            e.getTransaction().begin();
            e.persist(vaixell);
            e.getTransaction().commit();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
