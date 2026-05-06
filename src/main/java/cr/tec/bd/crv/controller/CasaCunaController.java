package cr.tec.bd.crv.controller;

import cr.tec.bd.crv.database.CatalogRepository;
import cr.tec.bd.crv.database.FosterHomeRepository;
import cr.tec.bd.crv.model.CatalogOption;
import cr.tec.bd.crv.model.FosterHomeProfile;
import cr.tec.bd.crv.util.NavigationUtil;
import cr.tec.bd.crv.util.SessionContext;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller for foster home activation and condition management.
 */
public class CasaCunaController {

    private final CatalogRepository catalogRepository = new CatalogRepository();
    private final FosterHomeRepository fosterHomeRepository = new FosterHomeRepository();

    @FXML
    private VBox boxTiposAceptados;

    @FXML
    private VBox boxTamanosAceptados;

    @FXML
    private ComboBox<CatalogOption> cbDonacionAlimento;

    @FXML
    private TextArea txtNotasCasaCuna;

    @FXML
    private Label lblEstadoCasaCuna;

    @FXML
    private Label lblMensajeCasaCuna;

    @FXML
    public void initialize() {
        loadCatalogsAndProfile();
    }

    @FXML
    public void guardarCasaCuna() {
        try {
            fosterHomeRepository.saveProfile(
                    SessionContext.getCurrentPersonId(),
                    idOf(cbDonacionAlimento),
                    idsOf(boxTiposAceptados),
                    idsOf(boxTamanosAceptados),
                    txtNotasCasaCuna.getText()
            );
            lblEstadoCasaCuna.setText("Casa cuna activa.");
            lblMensajeCasaCuna.setText("Condiciones guardadas correctamente.");
        } catch (IllegalArgumentException e) {
            lblMensajeCasaCuna.setText(e.getMessage());
        } catch (SQLException e) {
            lblMensajeCasaCuna.setText("No se pudo guardar casa cuna: " + e.getMessage());
        }
    }

    @FXML
    public void volverMenu(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/menu.fxml", "BDP1 - Bienestar Animal");
    }

    private void loadCatalogsAndProfile() {
        try {
            loadCheckOptions(boxTiposAceptados, catalogRepository.findPetTypes());
            loadCheckOptions(boxTamanosAceptados, catalogRepository.findPetSizes());
            cbDonacionAlimento.setItems(FXCollections.observableArrayList(catalogRepository.findFoodDonationOptions()));

            FosterHomeProfile profile = fosterHomeRepository.findProfile(SessionContext.getCurrentPersonId());
            lblEstadoCasaCuna.setText(profile.isActive()
                    ? "Casa cuna activa."
                    : "Completa estas condiciones para activar casa cuna.");
            txtNotasCasaCuna.setText(profile.getNotes() == null ? "" : profile.getNotes());
            selectByIds(boxTiposAceptados, profile.getAcceptedTypeIds());
            selectByIds(boxTamanosAceptados, profile.getAcceptedSizeIds());
            selectComboById(cbDonacionAlimento, profile.getFoodDonationId());
            lblMensajeCasaCuna.setText("");
        } catch (SQLException e) {
            lblMensajeCasaCuna.setText("No se pudieron cargar las condiciones: " + e.getMessage());
        }
    }

    private void loadCheckOptions(VBox container, List<CatalogOption> options) {
        container.getChildren().clear();
        for (CatalogOption option : options) {
            CheckBox checkBox = new CheckBox(option.getLabel());
            checkBox.setUserData(option);
            checkBox.getStyleClass().add("check-option");
            container.getChildren().add(checkBox);
        }
    }

    private Long idOf(ComboBox<CatalogOption> comboBox) {
        CatalogOption selected = comboBox.getValue();
        return selected == null ? null : selected.getId();
    }

    private List<Long> idsOf(VBox container) {
        return container.getChildren()
                .stream()
                .filter(node -> node instanceof CheckBox checkBox && checkBox.isSelected())
                .map(node -> ((CatalogOption) node.getUserData()).getId())
                .toList();
    }

    private void selectByIds(VBox container, List<Long> ids) {
        for (Node node : container.getChildren()) {
            if (node instanceof CheckBox checkBox) {
                CatalogOption option = (CatalogOption) checkBox.getUserData();
                checkBox.setSelected(ids.contains(option.getId()));
            }
        }
    }

    private void selectComboById(ComboBox<CatalogOption> comboBox, Long id) {
        if (id == null) {
            comboBox.getSelectionModel().clearSelection();
            return;
        }

        comboBox.getItems()
                .stream()
                .filter(option -> option.getId() == id)
                .findFirst()
                .ifPresent(comboBox::setValue);
    }
}
