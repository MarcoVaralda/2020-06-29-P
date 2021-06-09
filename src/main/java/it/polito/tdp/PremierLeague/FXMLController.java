/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnConnessioneMassima"
    private Button btnConnessioneMassima; // Value injected by FXMLLoader

    @FXML // fx:id="btnCollegamento"
    private Button btnCollegamento; // Value injected by FXMLLoader

    @FXML // fx:id="txtMinuti"
    private TextField txtMinuti; // Value injected by FXMLLoader

    @FXML // fx:id="cmbMese"
    private ComboBox<Integer> cmbMese; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM1"
    private ComboBox<Match> cmbM1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM2"
    private ComboBox<Match> cmbM2; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doConnessioneMassima(ActionEvent event) {
    	List<Adiacenza> connMax = this.model.cercaConnessioneMax();
    	
    	String ris = "Connessione massima (peso = "+connMax.get(0).getPeso() +"):\n";
    	for(Adiacenza a : connMax) 
    		ris = ris +a.getM1() +" - " +a.getM2() +"\n";
    	
    	this.txtResult.appendText("\n\n"+ris);
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	int mese = this.cmbMese.getValue();
    	String stringaMin = this.txtMinuti.getText();
    	
    	int min = 0;
    	try {
    		min = Integer.parseInt(stringaMin);
    	}
    	catch(NumberFormatException nbe) {
    		this.txtResult.setText("Inserisci un valore intero nel campo MIN!");
    		return;
    	}
    	
    	this.model.creaGrafo(mese, min);
    	
    	this.txtResult.setText("Grafo creato!\n\n" +this.model.getNumeroVertici() +this.model.getNumeroArchi());
    	
    	this.cmbM1.getItems().addAll(this.model.getVertexSet());
    	this.cmbM2.getItems().addAll(this.model.getVertexSet());
    }

    @FXML
    void doCollegamento(ActionEvent event) {
    	Match m1 = this.cmbM1.getValue();
    	Match m2 = this.cmbM2.getValue();
    	
    	if(m1==null || m2==null) {
    		this.txtResult.setText("Devi selezionare un match di partenza e uno di arrivo!");
    		return;
    	}
    	
    	String risultato = this.model.cercaCammino(m1,m2);
    	if(risultato==null) {
    		this.txtResult.setText("Non è stato possibile trovare un percorso tra i match selezionati!");
    		return;
    	}
    	
    	this.txtResult.appendText("\n\nCammino a peso massimo tra "+m1 +" e " +m2 +":\n" +risultato);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnConnessioneMassima != null : "fx:id=\"btnConnessioneMassima\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCollegamento != null : "fx:id=\"btnCollegamento\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMinuti != null : "fx:id=\"txtMinuti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbMese != null : "fx:id=\"cmbMese\" was not injected: check your FXML file 'Scene.fxml'.";        assert cmbM1 != null : "fx:id=\"cmbM1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbM2 != null : "fx:id=\"cmbM2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	List<Integer> mesi = new LinkedList<>();
    	for(int i=1;i<=12;i++)
    		mesi.add(i);
    	this.cmbMese.getItems().addAll(mesi);
    }
    
    
}
