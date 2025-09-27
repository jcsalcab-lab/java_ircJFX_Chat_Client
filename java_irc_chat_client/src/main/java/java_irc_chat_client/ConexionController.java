package java_irc_chat_client;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TreeItem;
import javafx.scene.control.Alert.AlertType;
import javafx.event.ActionEvent;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

public class ConexionController {

    @FXML private TextField servidorField, nickField, identdField;
    @FXML private CheckBox chkMultiserver, chkSecuenciaInicio, chkAutoConectar;
    @FXML private ComboBox<String> tipoConexionCombo;
    @FXML private TreeView<String> serverTreeView;
    @FXML private TreeView<String> arbolDerecha;
    @FXML private TableView<AutoConectorItem> autoConectarTable;
    @FXML private TableColumn<AutoConectorItem, Integer> colNumero;
    @FXML private TableColumn<AutoConectorItem, Boolean> colOn;
    @FXML private TableColumn<AutoConectorItem, String> colServidor;
    @FXML private TableColumn<AutoConectorItem, String> colNick;

    private final File configDir = new File(System.getProperty("user.home"), ".ircclient");
    private  File fileFormulario = new File(configDir, "FormularioConexion.xml");
    private final File fileZonas = new File(configDir, "Zonas_Servidores_IRC.xml");

    private Formulario formulario;
    
    

    @FXML
    public void initialize() throws Exception {
        if (!configDir.exists()) configDir.mkdirs();
        copiarSiNoExiste("/java_irc_chat_client/FormularioConexion.xml", fileFormulario);
        copiarSiNoExiste("/java_irc_chat_client/Zonas_Servidores_IRC.xml", fileZonas);

        // Inicializar tabla AutoConector
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colOn.setCellValueFactory(new PropertyValueFactory<>("on"));
        colNick.setCellValueFactory(new PropertyValueFactory<>("nick"));
        colServidor.setCellValueFactory(new PropertyValueFactory<>("servidor"));

        // Cargar formulario y TreeViews
        cargarFormulario();
        cargarTreeViewDerechoYEIzquierdo();

        // Listener TreeView izquierdo
        serverTreeView.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null && newSel.isLeaf()) {
                servidorField.setText(newSel.getValue());
            }
        });

        // Listener TreeView derecho (AutoConector)
        arbolDerecha.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                TreeItem<String> selectedTreeItem = arbolDerecha.getSelectionModel().getSelectedItem();
                if (selectedTreeItem != null && selectedTreeItem.isLeaf()) {
                    String servidorSeleccionado = selectedTreeItem.getValue();
                    AutoConectorItem filaSeleccionada = autoConectarTable.getSelectionModel().getSelectedItem();
                    if (filaSeleccionada != null) {
                        filaSeleccionada.setServidor(servidorSeleccionado);
                        autoConectarTable.refresh();
                    }
                }
            }
        });
    }
    
    public void setFileFormulario(File file) {
        this.fileFormulario = file;
    }

    private void copiarSiNoExiste(String recurso, File destino) throws Exception {
        if (!destino.exists()) {
            try (InputStream is = getClass().getResourceAsStream(recurso)) {
                if (is == null) throw new Exception("Recurso no encontrado: " + recurso);
                Files.copy(is, destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    // ===================== TREEVIEW =====================
    @XmlRootElement(name="ConexionIRC")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ConexionIRC {
        @XmlElement(name="ArbolServidores")
        public ArbolServidores arbolServidores = new ArbolServidores();
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ArbolServidores {
        @XmlElement(name="ChatZona") public Zona chatZona;
        @XmlElement(name="DALnet") public Zona dalnet;
        @XmlElement(name="EFnet") public Zona efnet;
        @XmlElement(name="Feenode") public Zona feenode;
        @XmlElement(name="Gamesurge") public Zona gamesurge;
        @XmlElement(name="GlobalIChat") public Zona globalIChat;
        @XmlElement(name="ircHispano") public Zona ircHispano;
        @XmlElement(name="IRCnet") public Zona ircNet;
        @XmlElement(name="Nuevos") public Zona nuevos;

        public List<Zona> zonas() {
            List<Zona> lista = new ArrayList<>();
            if(chatZona != null) lista.add(chatZona);
            if(dalnet != null) lista.add(dalnet);
            if(efnet != null) lista.add(efnet);
            if(feenode != null) lista.add(feenode);
            if(gamesurge != null) lista.add(gamesurge);
            if(globalIChat != null) lista.add(globalIChat);
            if(ircHispano != null) lista.add(ircHispano);
            if(ircNet != null) lista.add(ircNet);
            if(nuevos != null) lista.add(nuevos);
            return lista;
        }
    }
    
    
    

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Zona {
        @XmlTransient
        public String nombre;
        @XmlElement(name="Servidor")
        public List<String> servidores = new ArrayList<>();
        public Zona() {}
    }

    private void cargarTreeViewDerechoYEIzquierdo() throws Exception {
        JAXBContext context = JAXBContext.newInstance(ConexionIRC.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ConexionIRC conexionIRC = (ConexionIRC) unmarshaller.unmarshal(fileZonas);

        // TREEVIEW IZQUIERDO
        TreeItem<String> rootIzq = new TreeItem<>("Servidores IRC");
        rootIzq.setExpanded(true);
        for (Zona zona : conexionIRC.arbolServidores.zonas()) {
            zona.nombre = getNombreTag(zona, conexionIRC.arbolServidores);
            TreeItem<String> zonaItem = new TreeItem<>(zona.nombre);
            zonaItem.setExpanded(true);
            for (String servidor : zona.servidores) {
                zonaItem.getChildren().add(new TreeItem<>(servidor));
            }
            rootIzq.getChildren().add(zonaItem);
        }
        serverTreeView.setRoot(rootIzq);

        // TREEVIEW DERECHO
        TreeItem<String> rootDer = new TreeItem<>("Auto-Conector");
        rootDer.setExpanded(true);
        for (Zona zona : conexionIRC.arbolServidores.zonas()) {
            TreeItem<String> zonaItem = new TreeItem<>(zona.nombre);
            zonaItem.setExpanded(true);
            for (String servidor : zona.servidores) {
                zonaItem.getChildren().add(new TreeItem<>(servidor));
            }
            rootDer.getChildren().add(zonaItem);
        }
        arbolDerecha.setRoot(rootDer);
    }

    private String getNombreTag(Zona zona, ArbolServidores arbol) {
        if (arbol.chatZona == zona) return "ChatZona";
        if (arbol.dalnet == zona) return "DALnet";
        if (arbol.efnet == zona) return "EFnet";
        if (arbol.feenode == zona) return "Feenode";
        if (arbol.gamesurge == zona) return "Gamesurge";
        if (arbol.globalIChat == zona) return "GlobalIChat";
        if (arbol.ircHispano == zona) return "ircHispano";
        if (arbol.ircNet == zona) return "IRCnet";
        if (arbol.nuevos == zona) return "Nuevos";
        return "Zona";
    }


 // ===================== FORMULARIO =====================
    public void cargarFormulario() throws Exception {
        System.out.println("===== DEBUG: cargarFormulario() =====");
        System.out.println("Ruta del fichero físico: " + fileFormulario.getAbsolutePath());
        System.out.println("¿El fichero existe? " + fileFormulario.exists());
        System.out.println("¿Se puede leer? " + fileFormulario.canRead());

        // Leer y mostrar contenido crudo del XML para depuración
        try (java.util.Scanner scanner = new java.util.Scanner(fileFormulario)) {
            scanner.useDelimiter("\\Z"); // Leer todo
            String contenido = scanner.hasNext() ? scanner.next() : "";
            System.out.println("Contenido del fichero (primeros 500 chars): " + 
                (contenido.length() > 500 ? contenido.substring(0, 500) + "..." : contenido));
        } catch (Exception e) {
            System.out.println("Error leyendo fichero para debug: " + e.getMessage());
        }

        // Leer desde XML con JAXB
        JAXBContext context = JAXBContext.newInstance(Formulario.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        formulario = (Formulario) unmarshaller.unmarshal(fileFormulario);

        // Mostrar los valores cargados
        System.out.println("Servidor: " + formulario.conectar.servidorField);
        System.out.println("Nick: " + formulario.conectar.nickField);
        System.out.println("Tipo de conexión guardado: " + formulario.opciones.tipoConexionCombo);
        System.out.println("Opciones del combo: " + formulario.opciones.opcionesCombo);
        System.out.println("AutoConector activado: " + formulario.autoConector.chkAutoConectar);
        System.out.println("Número de filas en AutoConector: " + formulario.autoConector.tablaAutoConector.size());

        // Inicializar listas si son null
        if (formulario.opciones.opcionesCombo == null) formulario.opciones.opcionesCombo = new ArrayList<>();
        if (formulario.autoConector.tablaAutoConector == null) formulario.autoConector.tablaAutoConector = new ArrayList<>();

        // Conectar
        servidorField.setText(formulario.conectar.servidorField);
        nickField.setText(formulario.conectar.nickField);
        chkMultiserver.setSelected(formulario.conectar.chkMultiserver);

        // Opciones
        identdField.setText(formulario.opciones.identdField);
        chkSecuenciaInicio.setSelected(formulario.opciones.chkSecuenciaInicio);

        // ComboBox
        tipoConexionCombo.getItems().clear();
        if (!formulario.opciones.opcionesCombo.isEmpty()) {
            tipoConexionCombo.getItems().addAll(formulario.opciones.opcionesCombo);
        } else {
            tipoConexionCombo.getItems().addAll("IRC", "SSL", "WebSocket");
        }

        if (formulario.opciones.tipoConexionCombo != null && !formulario.opciones.tipoConexionCombo.isEmpty()) {
            tipoConexionCombo.setValue(formulario.opciones.tipoConexionCombo);
        } else if (!tipoConexionCombo.getItems().isEmpty()) {
            tipoConexionCombo.setValue(tipoConexionCombo.getItems().get(0));
        }

        // AutoConector
        chkAutoConectar.setSelected(formulario.autoConector.chkAutoConectar);
        autoConectarTable.getItems().clear();
        autoConectarTable.getItems().addAll(formulario.autoConector.tablaAutoConector);
    }


    public void guardarFormulario() throws Exception {
        // Sincronizar controles con objeto JAXB
        formulario.conectar.servidorField = servidorField.getText();
        formulario.conectar.nickField = nickField.getText();
        formulario.conectar.chkMultiserver = chkMultiserver.isSelected();

        formulario.opciones.identdField = identdField.getText();
        formulario.opciones.chkSecuenciaInicio = chkSecuenciaInicio.isSelected();
        formulario.opciones.tipoConexionCombo = tipoConexionCombo.getValue();
        formulario.opciones.opcionesCombo.clear();
        formulario.opciones.opcionesCombo.addAll(tipoConexionCombo.getItems());

        formulario.autoConector.chkAutoConectar = chkAutoConectar.isSelected();
        formulario.autoConector.tablaAutoConector.clear();
        formulario.autoConector.tablaAutoConector.addAll(autoConectarTable.getItems());

        // Guardar XML
        JAXBContext context = JAXBContext.newInstance(Formulario.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(formulario, fileFormulario);
    }


    @FXML
    private void onConectar(ActionEvent event) {
        if (servidorField.getText().isEmpty() || nickField.getText().isEmpty()) {
            Alert alert = new Alert(AlertType.WARNING, "Servidor o nick no pueden estar vacíos", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        try {
            guardarFormulario();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR, "Error guardando configuración: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    // ===================== JAXB CLASES =====================
    @XmlRootElement(name="Formulario")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Formulario {
        public Conectar conectar = new Conectar();
        public Opciones opciones = new Opciones();
        public AutoConector autoConector = new AutoConector();
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Conectar {
        public String servidorField = "";
        public String nickField = "";
        public boolean chkMultiserver = false;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Opciones {
        public String identdField = "";
        public boolean chkSecuenciaInicio = false;
        public String tipoConexionCombo = "";

        @XmlElementWrapper(name="Opciones_Combo_Tipo_de_Conexiones")
        @XmlElement(name="Opcion")
        public List<String> opcionesCombo = new ArrayList<>();
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class AutoConector {
        public boolean chkAutoConectar = false;

        @XmlElementWrapper(name="TablaAutoConector")
        @XmlElement(name="Fila")
        public List<AutoConectorItem> tablaAutoConector = new ArrayList<>();
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class AutoConectorItem {
        private int numero;
        private boolean on;
        private String nick;
        private String servidor;

        public AutoConectorItem() {}
        public AutoConectorItem(int numero, boolean on, String nick, String servidor) {
            this.numero = numero;
            this.on = on;
            this.nick = nick;
            this.servidor = servidor;
        }

        public int getNumero() { return numero; }
        public boolean isOn() { return on; }
        public String getNick() { return nick; }
        public String getServidor() { return servidor; }

        public void setNumero(int numero) { this.numero = numero; }
        public void setOn(boolean on) { this.on = on; }
        public void setNick(String nick) { this.nick = nick; }
        public void setServidor(String servidor) { this.servidor = servidor; }
    }
}
