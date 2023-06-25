/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.yelp;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.yelp.model.Giornalista;
import it.polito.tdp.yelp.model.Model;
import it.polito.tdp.yelp.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnUtenteSimile"
    private Button btnUtenteSimile; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="txtX2"
    private TextField txtX2; // Value injected by FXMLLoader

    @FXML // fx:id="cmbAnno"
    private ComboBox<Integer> cmbAnno; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="cmbUtente"
    private ComboBox<User> cmbUtente; // Value injected by FXMLLoader

    @FXML // fx:id="txtX1"
    private TextField txtX1; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {

    	Integer anno =  this.cmbAnno.getValue();
    	String inputRecensioni = this.txtN.getText();
    	int recensioni = 0;
    	try {
    		recensioni = Integer.parseInt(inputRecensioni);
    	}catch(NumberFormatException e ) {
    		this.txtResult.appendText("Inserisci un valore numerico valido\n");
    	}
    	
    	if(recensioni == 0) {
    		this.txtResult.appendText("Inserisci numero recensioni\n");
    	}
    	if(anno == null) {
    		this.txtResult.appendText("Scegli un anno\n");
    	}else if(anno<2005 && anno>2013) {
    		this.txtResult.appendText("Scegli un anno compreso tra 2005 e 2013\n");
    	}
    	
    	model.creaGrafo(anno, recensioni);
    	this.cmbUtente.getItems().clear();
    	this.cmbUtente.getItems().addAll(model.getVertices());
   
    }

    @FXML
    void doUtenteSimile(ActionEvent event) {

    	User scelto = this.cmbUtente.getValue();
    	if(scelto == null) {
    		this.txtResult.appendText("Scegli un utente\n");
    	}
    	List<User> simili = model.utenteSimile(scelto);
    	this.txtResult.appendText(simili.toString()+"\n");
    }
    
    @FXML
    void doSimula(ActionEvent event) {

    	String inputGiornalisti = this.txtX1.getText();
    	int giornalisti = 0;
    	
    	try {
    		giornalisti = Integer.parseInt(inputGiornalisti);
    	}catch(NumberFormatException e ) {
    		this.txtResult.appendText("Inserisci un valore numerico valido\n");
    	}
    	
    	String inputIntervistati = this.txtX2.getText();
    	int intervistati = 0;
    	try {
    		intervistati = Integer.parseInt(inputIntervistati);
    	}catch(NumberFormatException e ) {
    		this.txtResult.appendText("Inserisci un valore numerico valido\n");
    	}
    	
    	if(intervistati>model.getVertices().size()) {
    		txtResult.appendText("x2 deve essere minore o uguale al numero di utenti\n");
			return;
    	}
    	
    	if(giornalisti>intervistati) {
			txtResult.appendText("x1 deve essere minore o uguale a x2\n");
			return;
		}
    	
    	model.simula(giornalisti,intervistati);

		txtResult.appendText("Numero di giorni: "+model.getTotGiorni()+"\n");

		for(Giornalista g: model.getGiornalisti()) {
			txtResult.appendText("Giornalista "+ g.getId()+ ": "+ g.getNumIntervistati()+ " intervistati\n");
		}
		
    }
    

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnUtenteSimile != null : "fx:id=\"btnUtenteSimile\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX2 != null : "fx:id=\"txtX2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbAnno != null : "fx:id=\"cmbAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbUtente != null : "fx:id=\"cmbUtente\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX1 != null : "fx:id=\"txtX1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

        for(int i= 2005; i<=2013; i++)
        	this.cmbAnno.getItems().add(i);
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
