/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.yelp;

import java.net.URL;
import java.time.Year;
import java.util.List;
import java.util.ResourceBundle;

import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.yelp.model.Arco;
import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Model;
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

    @FXML // fx:id="btnLocaleMigliore"
    private Button btnLocaleMigliore; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="cmbCitta"
    private ComboBox<String> cmbCitta; // Value injected by FXMLLoader

    @FXML // fx:id="txtX"
    private TextField txtX; // Value injected by FXMLLoader

    @FXML // fx:id="cmbAnno"
    private ComboBox<Year> cmbAnno; // Value injected by FXMLLoader

    @FXML // fx:id="cmbLocale"
    private ComboBox<Business> cmbLocale; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	this.txtResult.clear();
    	if(this.model.getLocaleMigliore()==null) {
    		this.txtResult.setText("Errore! Locale migliore non trovato!");
    	}
    	Business partenza = cmbLocale.getValue();
    	Business arrivo = model.getLocaleMigliore();
    	double soglia = Double.parseDouble(this.txtX.getText());
    	//Aggiungi controlli
    	
    	List<Business> percorso = model.percorsoMigliore(partenza, arrivo, soglia);
    	if(percorso==null) {
    		this.txtResult.setText("Mi spiace, percorso non trovato!");
    		return;
    	}
    	if(soglia<0) {
    		this.txtResult.setText("Insira una soglia prima di calcolare il percorso");
    		return;
    	}
    	
    	this.txtResult.appendText("Percorso migliore:\n"+percorso.toString()+"\n");
    	
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	String citta = this.cmbCitta.getValue();
    	Year anno ;
    	try {
    		anno = this.cmbAnno.getValue();
    	}catch(NumberFormatException e) {
    		this.txtResult.clear();
    		throw new RuntimeException("Error creaGrafo");
    	}
    	if(citta==null || anno==null) {
    		this.txtResult.clear();
    		this.txtResult.appendText("ERRORE: parametri non selezionati!");
    	}
    	String msg = model.creaGrafo(citta,anno);
    	this.txtResult.appendText(msg);
    	cmbLocale.getItems().addAll(model.getVertexSet());
    	
    	
    	
    	
    }

    @FXML
    void doLocaleMigliore(ActionEvent event) {
    	this.txtResult.clear();
    
    	Business best = model.getLocaleMigliore();
    	if(best==null) {
    		this.txtResult.appendText("Mi dispiace, best non trovato");
    		return;
    	}
    	this.txtResult.appendText("Locale Migliore: "+best.getBusinessName()+"\n");
    	
    	
    	
    	
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnLocaleMigliore != null : "fx:id=\"btnLocaleMigliore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbCitta != null : "fx:id=\"cmbCitta\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbAnno != null : "fx:id=\"cmbAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbLocale != null : "fx:id=\"cmbLocale\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.cmbCitta.getItems().addAll(model.getCitta());
    	for( int anno = 2005; anno <= 2013; anno++) {
    		this.cmbAnno.getItems().add(Year.of(anno));
    	}
    	
    }
}
