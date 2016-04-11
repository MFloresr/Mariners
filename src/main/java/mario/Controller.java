package mario;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
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
    private ListView listCaps;
    @FXML
    private Button btndades;
    @FXML
    private Button btnimportar;
    @FXML
    private Button btnexportar;

    private String dato=null;
    private List<String> nombres = new ArrayList<String>();
    private List<String> apellidos = new ArrayList<String>();
    private Random rand = new Random();
    private static final List<String> abecedario= Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "Ñ", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" );
    private static final List<String> rango= Arrays.asList("Capita", "Mariner", "Cap de colla");
    private ObservableList<String> itemsvaixells= FXCollections.observableArrayList ();
    private ObservableList<String> itemsmariners= FXCollections.observableArrayList ();
    private ObservableList<String> itemscaps= FXCollections.observableArrayList ();
    private ArrayList<String> tripusfalsos = new ArrayList<String>();
    private ArrayList<String> barcosfalsos = new ArrayList<String>();
    private int CANTIDADBARCOS = 100;
    private List<String> dnis = new ArrayList<String>();
    private List<String> matriculas = new ArrayList<String>();
    private String capita;
    private int comas =0;


    @FXML
    public void carregarDades(Event event){
        File archivoEscogido = buscarFichero();
        try {
            crearListaNombres(archivoEscogido);
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("MarinersUnit");
            EntityManager e = emf.createEntityManager();

            insertarDatosBDD(e);
            selectDatos(e);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void importarDatos(Event event){
        File archivoEscogido = buscarFichero();
        try {
            importardata(archivoEscogido);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void exportarDatos(Event event){

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MarinersUnit");
        EntityManager e = emf.createEntityManager();

        TypedQuery<Vaixell> query_vaixells = e.createQuery("SELECT e FROM Vaixell e", Vaixell.class);
        List<Vaixell> listVaixellsselect = query_vaixells.getResultList();

        TypedQuery<Tripulant> query_tripulants = e.createQuery("SELECT t FROM Tripulant t", Tripulant.class);
        List<Tripulant> listtripulants = query_tripulants.getResultList();


    }

    public void importardata(File archivoEscogido) throws IOException {
        FileReader fr=new FileReader(archivoEscogido);
        BufferedReader br = new BufferedReader(fr);
        String linea;
        while((linea=br.readLine())!=null){
            comas=0;
            for(int i=0;i<linea.length();i++){
                if(linea.charAt(i)==','){
                    comas = comas+1;
                }
            }
            if(comas==1){
                barcosfalsos.add(linea);
            }if(comas==3){
                tripusfalsos.add(linea);
            }
        }
        crearListaNombres(archivoEscogido);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MarinersUnit");
        EntityManager e = emf.createEntityManager();

        for(int i=0 ; i<barcosfalsos.size();i++){
            Vaixell vaixell=new Vaixell();
            String[] datosbarco= barcosfalsos.get(i).split(",");
            vaixell.setNom(datosbarco[1]);
            vaixell.setMatricula(datosbarco[0]);
            ArrayList<Tripulant> tripus= new ArrayList<Tripulant>();
            for(int x = 0;x<tripusfalsos.size();x++){
                if(tripusfalsos.get(x).contains(datosbarco[0])){
                    String[] datostripulante = tripusfalsos.get(x).split(",");
                    Tripulant tripu = new Tripulant();
                    tripu.setDni(datostripulante[0]);
                    tripu.setNom(datostripulante[1]);
                    tripu.setRang(datostripulante[3]);
                    e.getTransaction().begin();
                    e.persist(tripu);
                    e.getTransaction().commit();
                    tripus.add(tripu);
                }
            }
            vaixell.setMariners(tripus);

            e.getTransaction().begin();
            e.persist(vaixell);
            e.getTransaction().commit();
        }
    }

    public File buscarFichero(){
        FileChooser fichero= new FileChooser();
        fichero.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT Files", "*.txt"));
        File archivoEscogido = fichero.showOpenDialog(null);
        System.out.println(archivoEscogido);
        return archivoEscogido;
    }

    public void crearListaNombres(File archivoEscogido) throws IOException {
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
    }

    public void insertarDatosBDD(EntityManager e){
        while (CANTIDADBARCOS !=0){
            String matricula = abecedario.get(rand.nextInt(abecedario.size()))+rand.nextInt(((99999 - 00000) + 1))+abecedario.get(rand.nextInt(abecedario.size()))+abecedario.get(rand.nextInt(abecedario.size()))+abecedario.get(rand.nextInt(abecedario.size()));
            if(!matriculas.contains(matricula)){
                String nombreBarco = nombres.get(rand.nextInt(nombres.size()))+"_"+nombres.get(rand.nextInt(nombres.size()));
                List<Tripulant> mariners = new ArrayList<Tripulant>();
                int tripulantes = rand.nextInt(8)+3;
                int contador =0;
                int rangosrepes = 0;
                while(tripulantes !=0) {
                    String dni = rand.nextInt(((99999 - 00000) + 1)) + abecedario.get(rand.nextInt(abecedario.size()));
                    String nombre = nombres.get(rand.nextInt(nombres.size())) + " " + apellidos.get(rand.nextInt(apellidos.size())) +" "+ apellidos.get(rand.nextInt(apellidos.size()));
                    String rang = rango.get(rand.nextInt(rango.size()));
                    if (rang.contains("Capita")){
                        rangosrepes = 1;
                        contador++;
                        if(contador==1){
                            rangosrepes = 0;
                        }
                    }else{
                        rangosrepes = 0;
                    }

                    if(!dnis.contains("dni") && rangosrepes==0){
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
    }

    public void selectDatos(EntityManager e){
        TypedQuery<Vaixell> query_vaixells = e.createQuery("SELECT e FROM Vaixell e", Vaixell.class);
        List<Vaixell> listVaixellsselect = query_vaixells.getResultList();

        for(int i=0;i<listVaixellsselect.size();i++){
            itemsvaixells.add(listVaixellsselect.get(i).getNom());
        }
        listVaixells.setItems(itemsvaixells);
        listVaixells.setDisable(false);

        listVaixells.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                itemscaps.clear();
                itemsmariners.clear();
                for(int i = 0; i<listVaixellsselect.get(listVaixells.getSelectionModel().getSelectedIndex()).getMariners().size();i++){
                    if(listVaixellsselect.get(listVaixells.getSelectionModel().getSelectedIndex()).getMariners().get(i).getRang().contains("Mariner")){
                        itemsmariners.add(listVaixellsselect.get(listVaixells.getSelectionModel().getSelectedIndex()).getMariners().get(i).getNom());
                    }
                    if(listVaixellsselect.get(listVaixells.getSelectionModel().getSelectedIndex()).getMariners().get(i).getRang().contains("Cap de colla")){
                        itemscaps.add(listVaixellsselect.get(listVaixells.getSelectionModel().getSelectedIndex()).getMariners().get(i).getNom());
                    }
                    if(listVaixellsselect.get(listVaixells.getSelectionModel().getSelectedIndex()).getMariners().get(i).getRang().contains("Capita")){
                        capita = listVaixellsselect.get(listVaixells.getSelectionModel().getSelectedIndex()).getMariners().get(i).getNom();
                    }
                }
                listMariners.setItems(itemsmariners);
                listMariners.setDisable(false);
                listCaps.setItems(itemscaps);
                listCaps.setDisable(false);
                textCapita.setDisable(false);
                textCapita.setText(capita);
            }
        });
    }
}
