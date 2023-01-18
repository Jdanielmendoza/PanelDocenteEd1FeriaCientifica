/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Presentacion;

import Datos.Conexion;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.lang.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicMenuBarUI;
import javax.swing.plaf.basic.BasicMenuItemUI;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.table.*;
import negocio.ListaDeEstudiante;
import negocio.TDA_Alumno;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 *
 * @author jdani
 */
public class NewPanelDocente extends javax.swing.JFrame {

    /**
     * Creates new form NewPanelDocente
     */
    //---------------veriaficar conexion a internet ---------------
    public void verificarConexion() {
        Socket sock = new Socket();
        InetSocketAddress addr = new InetSocketAddress("www.google.com", 80);
        try {
            sock.connect(addr, 3000);
            //JOptionPane.showMessageDialog(null, "Conexion Establecida..");
            jTextField2.setBackground(new Color(18, 176, 30));
            jTextField2.setToolTipText("Connected whith Data Base");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Procura Conectarte a internet al guardar cambios en la base de datos !..");
            jTextField2.setBackground(Color.RED);
            jTextField2.setToolTipText("Disconnected of Data Base");
        } finally {
            try {
                sock.close();
            } catch (IOException ex) {
                //Logger.getLogger(NewPanelDocente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static ListaDeEstudiante Students = new ListaDeEstudiante();
    //public static LinkedList<TDA_Alumno> Students;   // añadir la palabra static   porque ?  aun nose.. :v
    int contador;

    public NewPanelDocente() {
        initComponents();
        contador = 0;   //  para importar el archivo excel....
        this.tabla = (DefaultTableModel) jTable1.getModel();
        //this.Students = new LinkedList<>();
        NewPanelDocente.Students = new ListaDeEstudiante();

        //--------------verficar conexion a internet ----------------------------------------------
        Socket sock = new Socket();
        InetSocketAddress addr = new InetSocketAddress("www.google.com", 80);
        try {
            sock.connect(addr, 3000);
            //JOptionPane.showMessageDialog(null, "Conexion Establecida..");
            jTextField2.setBackground(new Color(18, 176, 30));
            jTextField2.setToolTipText("Connected whith Data Base");

            //-------------------------cargar la lista con los datos de la base de datos----------------------
            cargarListaDesdeDataBase();
            CargarTabla();
            //-----------------------------------------------------------------------------------------------

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "!Sin Conexion a internet....!!  No se puede guardar cambios a la Base De Datos");
            jTextField2.setBackground(Color.RED);
            jTextField2.setToolTipText("Sin conexion a internet..! No se puede guardar cambios en la base de datos");
        } finally {
            try {
                sock.close();
            } catch (IOException ex) {
                //Logger.getLogger(NewPanelDocente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // this.setExtendedState(NewPanelDocente.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);

        //--------------------imagen del perfil del docente ---------------------
        Image img = new ImageIcon(getClass().getResource("/images/perfil.png")).getImage();
        Image newimg = img.getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH);
        ImageIcon imageIcon = new ImageIcon(newimg);
        jLabel2.setIcon(imageIcon);

        //----color intercalado de las celdas 
        jTable1.setDefaultRenderer(Object.class, new TableCellRenderer() {
            private DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (row % 2 == 0) {
                    c.setBackground(new Color(255, 240, 220));

                } else {
                    c.setBackground(new Color(200, 200, 200));
                }
                return c;
            }

        });

        //-----color de fondo del encabezado
        /* try {
            if(UIManager.getLookAndFeel().toString().contains("Nimbus"))
                UIManager.put("nimbusBlueGrey",new Color(255,159,0));
                jTable1.getTableHeader().setBackground(new Color(0,0,0));
                
        } catch(Exception e) {
            e.printStackTrace();
        }*/
        for (int i = 0; i < 10; i++) {
            jTable1.getColumnModel().getColumn(i).setHeaderRenderer(new MyRenderer(new Color(255, 159, 0), Color.BLACK, new Font("Verdana", Font.BOLD, 14)));
        }
        jTable1.setGridColor(new Color(255, 119, 0));

        //-----personalizacion de la barra del menu -----------
        jMenuBar1.setUI(new BasicMenuBarUI() {
        });
        jMenuBar1.setBackground(new Color(42, 37, 80));
        //  jMenuBar1.setForeground(new Color(20,255,255));

        jMenu5.setUI(new BasicMenuItemUI() {
        });
        //  jMenu5.setBackground(new Color(255,119,0));
        // jMenu5.setForeground();

    }
//------------------------------------------------------FIN DEL CONSTRUCTOR----------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------

//----------------cargar lista desde la base de datos 
    public void cargarListaDesdeDataBase() {
        Conexion conn = new Conexion();
        Statement ps = null;   // para poder generar consultas sql
        ResultSet res = null;    // almacena los registros de la consulta

        try {
            ps = conn.conectar().createStatement();
            res = ps.executeQuery("SELECT * FROM Estudiante");
            while (res.next()) {
                Students.add(new TDA_Alumno(res.getInt("Registro"), res.getString("nombre"), res.getString("materia"), res.getString("gestion"), res.getInt("nota1"), res.getInt("nota2"), res.getInt("nota3"), res.getInt("notaPc"), res.getInt("notaR"), res.getInt("notaPr")));
            }
            conn.desconectar();
        } catch (Exception e) {
        }
    }

//--clase-jtable----------------------color de fondo del encabezado-/// (color del fondo, color de la letra)  y font---------------------------
    public class MyRenderer extends DefaultTableCellRenderer {

        Color background;
        Color foreground;
        Font font;

        public MyRenderer(Color background, Color foreground, Font font) {
            super();
            this.background = background;
            this.foreground = foreground;
            this.font = font;
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            comp.setBackground(background);
            comp.setForeground(foreground);
            comp.setFont(font);
            return comp;
        }
    }
    //-------------------------------------------------------------------
    public DefaultTableModel tabla;

    private void CargarTabla() {
        tabla = (DefaultTableModel) jTable1.getModel();
        tabla.setRowCount(0);
        for (int i = 0; i < Students.size(); i++) {
            tabla.addRow(Students.get(i).toArray());
        }
    }

    private void CargarTabla(String Materia) {
        tabla = (DefaultTableModel) jTable1.getModel();
        tabla.setRowCount(0);
        for (int i = 0; i < Students.size(); i++) {
            if (Students.get(i).getMateria().equals(Materia)) {
                tabla.addRow(Students.get(i).toArray());
            }
        }
    }

    //--------------------------------------------------------------------
    public void ActualizarStudent() {
        CargarTabla();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenu7 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(205, 205, 220));

        jTable1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Registro", "Nombre Completo", "Materia", "Gestion", "1er Ex.", "2do Ex.", "ExFinal", "Practico", "Repechaje", "Prom. Final"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable1.setCellSelectionEnabled(true);
        jTable1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTable1.setEnabled(false);
        jTable1.setRowHeight(25);
        jTable1.setSelectionBackground(new java.awt.Color(255, 255, 255));
        jTable1.setSelectionForeground(new java.awt.Color(0, 0, 0));
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable1.setShowGrid(true);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);
        jTable1.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setMinWidth(100);
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(100);
            jTable1.getColumnModel().getColumn(0).setMaxWidth(100);
            jTable1.getColumnModel().getColumn(1).setMinWidth(220);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(220);
            jTable1.getColumnModel().getColumn(1).setMaxWidth(220);
            jTable1.getColumnModel().getColumn(2).setMinWidth(150);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(150);
            jTable1.getColumnModel().getColumn(2).setMaxWidth(150);
            jTable1.getColumnModel().getColumn(3).setMinWidth(80);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(80);
            jTable1.getColumnModel().getColumn(3).setMaxWidth(80);
            jTable1.getColumnModel().getColumn(4).setMinWidth(70);
            jTable1.getColumnModel().getColumn(4).setPreferredWidth(70);
            jTable1.getColumnModel().getColumn(4).setMaxWidth(70);
            jTable1.getColumnModel().getColumn(5).setMinWidth(70);
            jTable1.getColumnModel().getColumn(5).setPreferredWidth(70);
            jTable1.getColumnModel().getColumn(5).setMaxWidth(70);
            jTable1.getColumnModel().getColumn(6).setMinWidth(80);
            jTable1.getColumnModel().getColumn(6).setPreferredWidth(80);
            jTable1.getColumnModel().getColumn(6).setMaxWidth(80);
            jTable1.getColumnModel().getColumn(7).setMinWidth(80);
            jTable1.getColumnModel().getColumn(7).setPreferredWidth(80);
            jTable1.getColumnModel().getColumn(7).setMaxWidth(80);
            jTable1.getColumnModel().getColumn(8).setMinWidth(95);
            jTable1.getColumnModel().getColumn(8).setPreferredWidth(95);
            jTable1.getColumnModel().getColumn(8).setMaxWidth(95);
            jTable1.getColumnModel().getColumn(9).setPreferredWidth(70);
        }

        jButton2.setBackground(new java.awt.Color(255, 119, 0));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(37, 29, 38));
        jButton2.setText("Habilitar Edicion");
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(255, 119, 0));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(37, 29, 38));
        jButton3.setText("Guardar Cambios");
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(255, 119, 0));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(37, 29, 38));
        jButton4.setText("Agregar Estudiante");
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jSeparator1.setBackground(new java.awt.Color(255, 159, 0));

        jSeparator2.setBackground(new java.awt.Color(255, 159, 0));

        jPanel3.setBackground(new java.awt.Color(42, 37, 80));
        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(224, 77, 1), 1, true));
        jPanel3.setForeground(new java.awt.Color(255, 255, 255));

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Segoe UI Emoji", 0, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 51, 51));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/iLupa.png"))); // NOI18N
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextField1.setBackground(new java.awt.Color(238, 238, 238));
        jTextField1.setFont(new java.awt.Font("Perpetua Titling MT", 1, 18)); // NOI18N
        jTextField1.setForeground(new java.awt.Color(51, 51, 51));
        jTextField1.setText("221045076");
        jTextField1.setToolTipText("Nro Registro");
        jTextField1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 255, 255)), "Nro Registro", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI Historic", 0, 10), new java.awt.Color(102, 102, 102))); // NOI18N
        jTextField1.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jTextField1.setMargin(new java.awt.Insets(5, 6, 5, 6));

        jLabel1.setFont(new java.awt.Font("Segoe UI Historic", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 119, 0));
        jLabel1.setText("BUSCAR");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jLabel3.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(42, 37, 58));
        jLabel3.setText("Codigo");

        jLabel4.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(42, 37, 58));
        jLabel4.setText("Nombre de Usuario");

        jLabel6.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        jLabel6.setText("8081");

        jLabel7.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel7.setText(" CAMPOS BARRERA MARIO ");

        jTextField2.setBackground(new java.awt.Color(0, 204, 0));
        jTextField2.setBorder(null);
        jTextField2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTextField2.setFocusable(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 10, Short.MAX_VALUE)))
                        .addGap(73, 73, 73)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)))
                .addGap(14, 14, 14)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jButton5.setBackground(new java.awt.Color(255, 255, 255));
        jButton5.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jButton5.setForeground(new java.awt.Color(0, 0, 0));
        jButton5.setText("Actualizar Tabla");
        jButton5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(255, 102, 102));
        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton6.setForeground(new java.awt.Color(37, 29, 38));
        jButton6.setText("Eliminar Estudiante");
        jButton6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 469, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(228, 228, 228)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jButton5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                        .addComponent(jButton4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21))))
        );

        jMenuBar1.setBackground(new java.awt.Color(0, 0, 0));
        jMenuBar1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 119, 0)));
        jMenuBar1.setForeground(new java.awt.Color(255, 255, 255));
        jMenuBar1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuBar1.setMinimumSize(new java.awt.Dimension(130, 35));
        jMenuBar1.setPreferredSize(new java.awt.Dimension(130, 35));

        jMenu1.setBackground(new java.awt.Color(0, 0, 0));
        jMenu1.setForeground(new java.awt.Color(255, 255, 255));
        jMenu1.setText("Menu");
        jMenu1.setBorderPainted(false);

        jMenuItem1.setText("seleccion 2");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setBackground(new java.awt.Color(0, 0, 0));
        jMenu2.setForeground(new java.awt.Color(255, 255, 255));
        jMenu2.setText("Agregar/Crear");
        jMenu2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jMenuItem4.setText("Importar Grupo desde un Excel");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuBar1.add(jMenu2);

        jMenu4.setForeground(new java.awt.Color(255, 255, 255));
        jMenu4.setText("Ver");
        jMenu4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenu4.setMargin(new java.awt.Insets(3, 10, 3, 10));

        jMenuItem3.setText("Grupo de una Materia");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem3);

        jMenuItem5.setText("Todos Los Inscritos");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem5);

        jMenuBar1.add(jMenu4);

        jMenu6.setForeground(new java.awt.Color(255, 255, 255));
        jMenu6.setText("Conexion");
        jMenu6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jMenuItem6.setText("Actualizar estado de Conexion");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem6);

        jMenuBar1.add(jMenu6);

        jMenu8.setForeground(new java.awt.Color(255, 255, 255));
        jMenu8.setText("Base De Datos");
        jMenu8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenu8.setMargin(new java.awt.Insets(3, 12, 3, 12));

        jMenuItem7.setText("Guardar Cambios en la Base De Datos ");
        jMenuItem7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuItem7MouseClicked(evt);
            }
        });
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem7);

        jMenuBar1.add(jMenu8);

        jMenu3.setEnabled(false);
        jMenu3.setFocusable(false);
        jMenu3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jMenu3.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jMenu3.setMargin(new java.awt.Insets(3, 920, 3, 6));
        jMenuBar1.add(jMenu3);

        jMenu7.setBackground(new java.awt.Color(255, 119, 0));
        jMenu7.setForeground(new java.awt.Color(255, 255, 255));
        jMenu7.setText("-");
        jMenu7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenu7.setFocusTraversalPolicyProvider(true);
        jMenu7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jMenu7.setHideActionText(true);
        jMenu7.setMargin(new java.awt.Insets(3, 15, 3, 15));
        jMenu7.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jMenu7MouseMoved(evt);
            }
        });
        jMenu7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu7MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jMenu7MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jMenu7MouseExited(evt);
            }
        });
        jMenu7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu7ActionPerformed(evt);
            }
        });
        jMenuBar1.add(jMenu7);

        jMenu5.setBackground(new java.awt.Color(255, 119, 0));
        jMenu5.setForeground(new java.awt.Color(224, 77, 1));
        jMenu5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/1487086345-cross_81577.png"))); // NOI18N
        jMenu5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenu5.setFocusTraversalPolicyProvider(true);
        jMenu5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jMenu5.setHideActionText(true);
        jMenu5.setMargin(new java.awt.Insets(3, 15, 3, 15));
        jMenu5.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jMenu5MouseMoved(evt);
            }
        });
        jMenu5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu5MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jMenu5MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jMenu5MouseExited(evt);
            }
        });
        jMenu5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu5ActionPerformed(evt);
            }
        });
        jMenuBar1.add(jMenu5);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here://-------------------------------------guardar cambios de la tabla

        jTable1.enable(false);                              // desabilita la edicion de la tabla
        jTable1.clearSelection();                          // limpia las selecciones si hay
        jTable1.editingStopped(new ChangeEvent(jTable1));  // deselecciona las celdas en modo edicion

        JOptionPane.showConfirmDialog(null, "Desea Guardar los Cambios?", "Guardado de manera local", JOptionPane.DEFAULT_OPTION);

        int i = 0;
        int Registro;
        String N;
        String M;
        String G;
        int N1;
        int N2;
        int N3;
        int NPc;
        int NR;
        int NPr;
        while (i < tabla.getRowCount()) {
            Registro = Integer.parseInt(tabla.getValueAt(i, 0).toString());
            if (tabla.getValueAt(i, 1) != null) {
                N = String.valueOf(tabla.getValueAt(i, 1));
            } else {
                N = "";
            }

            if (tabla.getValueAt(i, 2) != null) {
                M = String.valueOf(tabla.getValueAt(i, 2));
            } else {
                M = "";
            }

            if (tabla.getValueAt(i, 3) != null) {
                G = String.valueOf(tabla.getValueAt(i, 3));
            } else {
                G = "";
            }

            if (tabla.getValueAt(i, 4) != null) {
                N1 = Integer.parseInt(tabla.getValueAt(i, 4).toString());
            } else {
                N1 = 0;
            }

            if (tabla.getValueAt(i, 5) != null) {
                N2 = Integer.parseInt(tabla.getValueAt(i, 5).toString());
            } else {
                N2 = 0;
            }

            if (tabla.getValueAt(i, 6) != null) {
                N3 = Integer.parseInt(tabla.getValueAt(i, 6).toString());
            } else {
                N3 = 0;
            }

            if (tabla.getValueAt(i, 7) != null) {
                NPc = Integer.parseInt(tabla.getValueAt(i, 7).toString());
            } else {
                NPc = 0;
            }

            if (tabla.getValueAt(i, 8) != null) {
                NR = Integer.parseInt(tabla.getValueAt(i, 8).toString());
            } else {
                NR = 0;
            }

            if (tabla.getValueAt(i, 9) != null) {
                NPr = Integer.parseInt(tabla.getValueAt(i, 9).toString());
            } else {
                NPr = 0;
            }

            Students.get(getIndiceDe(Registro)).setNuevosDatos(N, M, G, N1, N2, N3, NPc, NR, NPr);

            i++;
        }


    }//GEN-LAST:event_jButton3ActionPerformed

    public int getIndiceDe(int Registro) {
        int index = 0;
        boolean find = false;
        while (index < Students.size() && !find) {
            if (Students.get(index).getRegistro() == Registro) {
                find = true;
                index--;
            }
            index++;
        }
        if (find) {
            return index;
        } else {
            return -1;
        }
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jTable1.enable(true);               // habilitar edicion de la tabla
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenu5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu5ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jMenu5ActionPerformed

    private void jMenu5MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu5MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu5MouseExited

    private void jMenu5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu5MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu5MouseEntered

    private void jMenu5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu5MouseClicked
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jMenu5MouseClicked

    private void jMenu5MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu5MouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu5MouseMoved

    private void jMenu7MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu7MouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu7MouseMoved

    private void jMenu7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu7MouseClicked
        // TODO add your handling code here:
        this.setExtendedState(ICONIFIED);
    }//GEN-LAST:event_jMenu7MouseClicked

    private void jMenu7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu7MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu7MouseEntered

    private void jMenu7MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu7MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu7MouseExited

    private void jMenu7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu7ActionPerformed


    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:            // agregar un solo estudiante a la base de datos
        FormInscripcionEstudiante estudiante = new FormInscripcionEstudiante();
        estudiante.setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:           crear grupo un grupo de estudiantes de dicha materia y gestion desde un excel
        JOptionPane.showMessageDialog(null, "Procure que el archivo excel tenga no mas a 10 columnas ");
        //  importExcelToJtableJava();
        contador++;
        File archivo;
        if (contador == 1) {
            AgregarFiltro();
        }

        // if(evt.getSource()==VistaEX.btnImportar){
        if (SelectArchivo.showDialog(null, "Seleccionar Archivo") == JFileChooser.APPROVE_OPTION) {
            archivo = SelectArchivo.getSelectedFile();
            //ALT + 124 ||
            if (archivo.getName().endsWith("xls") || archivo.getName().endsWith("xlsx")) {
                JOptionPane.showMessageDialog(null, Importar(archivo, tabla));
            } else {
                JOptionPane.showMessageDialog(null, "Seleccionar formato Valido");
            }
        }
        //  }
        CargarListaDesdeTabla();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    public void CargarListaDesdeTabla() {
        // Vector<Vector>  v = tabla.getDataVector(); 
        int R;
        String N;
        String M;
        String G;
        int N1;
        int N2;
        int N3;
        int NPc;
        int NR;
        int NPr;
        TDA_Alumno a;

        for (int i = 0; i < tabla.getRowCount(); i++) {
            if (tabla.getValueAt(i, 0) != null) {
                R = Integer.parseInt(tabla.getValueAt(i, 0).toString());
            } else {
                R = 0;
            }

            if (tabla.getValueAt(i, 1) != null) {
                N = String.valueOf(tabla.getValueAt(i, 1));
            } else {
                N = "";
            }

            if (tabla.getValueAt(i, 2) != null) {
                M = String.valueOf(tabla.getValueAt(i, 2));
            } else {
                M = "";
            }

            if (tabla.getValueAt(i, 3) != null) {
                G = String.valueOf(tabla.getValueAt(i, 3));
            } else {
                G = "";
            }

            if (tabla.getValueAt(i, 4) != null) {
                N1 = Integer.parseInt(tabla.getValueAt(i, 4).toString());
            } else {
                N1 = 0;
            }

            if (tabla.getValueAt(i, 5) != null) {
                N2 = Integer.parseInt(tabla.getValueAt(i, 5).toString());
            } else {
                N2 = 0;
            }

            if (tabla.getValueAt(i, 6) != null) {
                N3 = Integer.parseInt(tabla.getValueAt(i, 6).toString());
            } else {
                N3 = 0;
            }

            if (tabla.getValueAt(i, 7) != null) {
                NPc = Integer.parseInt(tabla.getValueAt(i, 7).toString());
            } else {
                NPc = 0;
            }

            if (tabla.getValueAt(i, 8) != null) {
                NR = Integer.parseInt(tabla.getValueAt(i, 8).toString());
            } else {
                NR = 0;
            }

            if (tabla.getValueAt(i, 9) != null) {
                NPr = Integer.parseInt(tabla.getValueAt(i, 9).toString());
            } else {
                NPr = 0;
            }

            a = new TDA_Alumno(R, N, M, G, N1, N2, N3, NPc, NR, NPr);

            if (!Contiene(R)) {
                Students.add(a);
            }

        }

    }

    public boolean Contiene(int reg) {           // returna true si el registro existe en mi lista  y false si no existe
        int i = 0;
        boolean contiene = false;
        while (i < NewPanelDocente.Students.size() && !contiene) {
            if (NewPanelDocente.Students.get(i).getRegistro() == reg) {
                contiene = true;
            }
            i++;
        }
        return contiene;
    }


    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        // TODO add your handling code here:     actuliza el estado de conexion. si esta conectado a internet o no 
        Socket sock = new Socket();
        InetSocketAddress addr = new InetSocketAddress("www.google.com", 80);
        try {
            sock.connect(addr, 3000);
            //JOptionPane.showMessageDialog(null, "Conexion Establecida..");
            jTextField2.setBackground(new Color(18, 176, 30));
            jTextField2.setToolTipText("Connected whith Data Base");
        } catch (IOException e) {
            jTextField2.setBackground(Color.RED);
            jTextField2.setToolTipText("Disconnected of Data Base");
        } finally {
            try {
                sock.close();
            } catch (IOException ex) {
                //Logger.getLogger(NewPanelDocente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        CargarTabla();

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:                    boton de buscar estudiante ---------
        int num = Integer.parseInt(jTextField1.getText());
        int i = 0;
        boolean encontrado = false;
        if (!Students.isEmpty()) {  //si la lista no esta vacia
            while (!encontrado && i < Students.size()) {
                if (Students.get(i).getRegistro() == num) {
                    tabla.setRowCount(0);
                    tabla.addRow(Students.get(i).toArray());
                    encontrado = true;
                }
                i++;
            }
            if (!encontrado) {
                JOptionPane.showMessageDialog(null, "estudiante no encontrado!!");
            }

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem7MouseClicked

    }//GEN-LAST:event_jMenuItem7MouseClicked
    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        // TODO add your handling code here:             //-----------------------------------GUARDAR CAMBIOS EN LA BASE DE DATOS-------
        JOptionPane.showConfirmDialog(null, "Desea Guardar los cambios en la Base de datos ? ");
        CargaSaveBD c;
        c = new CargaSaveBD();
        new Thread() {   //  dentro de un new Thread para ejecutar en paralelo los 2 procesos 
            @Override
            public void run() {
                c.setVisible(true);
                vaciarTablaOfDataBase();
                cargarDataBaseDesdeLista();
            }
        }.start();
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        //                                         //---------------Eliminar un estudiante de mi lista
        Integer registro = Integer.parseInt(JOptionPane.showInputDialog("Registro del Estudiante :", "221045076"));
        int i = 0;
        boolean encontrado = false;
        if (!Students.isEmpty()) {  //si la lista no esta vacia
            while (!encontrado && i < Students.size()) {
                if (Students.get(i).getRegistro() == registro) {
                    Students.remove(Students.get(i));
                    CargarTabla();
                    encontrado = true;
                }
                i++;
            }
            if (!encontrado) {
                JOptionPane.showMessageDialog(null, "estudiante no encontrado!!");
            }

        }

    }//GEN-LAST:event_jButton6ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // TODO add your handling code here:                          //---ver todos los inscritos 
        CargarTabla();
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        //                                                          //--ver estudiantes de una materia dada 
        //String materia = (String) JOptionPane.showInputDialog(null, "Seleccione una Materia a mostrar: ", "Materia", JOptionPane.DEFAULT_OPTION, null, carreras, carreras[0]);
        String materia = JOptionPane.showInputDialog(null, "Materia : ","Calculo I");
        CargarTabla(materia);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    public void vaciarTablaOfDataBase() {
        Conexion conn = new Conexion();
        Statement ps = null;   // para poder generar consultas sql
        ResultSet res = null;    // almacena los registros de la consulta

        try {
            ps = conn.conectar().createStatement();
            System.out.println(ps.execute("truncate Estudiante"));

            conn.desconectar();
        } catch (Exception e) {
            System.out.println("error");
        }
    }

    public void cargarDataBaseDesdeLista() {
        Conexion conn = new Conexion();
        PreparedStatement ps = null;   // para poder generar consultas sql
        // ResultSet res=null;    // almacena los registros de la consulta
        Connection c;
        try {
            c = conn.conectar();
            for (int i = 0; i < Students.size(); i++) {
                ps = c.prepareStatement("INSERT INTO Estudiante (Registro,nombre,materia,gestion,nota1,nota2,nota3,notaPc,notaR,notaPr) values( ? , ?, ? , ? ,?, ?,?,?,?,?)");
                ps.setInt(1, (int) Students.get(i).getRegistro());
                ps.setString(2, Students.get(i).getNombrecompleto());
                ps.setString(3, Students.get(i).getMateria());
                ps.setString(4, Students.get(i).getGestion());
                ps.setInt(5, (int) Students.get(i).getNota1());
                ps.setInt(6, (int) Students.get(i).getNota2());
                ps.setInt(7, (int) Students.get(i).getNotaExFinal());
                ps.setInt(8, (int) Students.get(i).getNotaPractico());
                ps.setInt(9, (int) Students.get(i).getNotaRepechaje());
                ps.setInt(10, (int) Students.get(i).getNotaPromedio());
                ps.execute();
            }
            conn.desconectar();
        } catch (Exception e) {
            System.out.println("error al cargar base de datos desde la tabla en locas!");
        }
    }

    //-----------------importar excel------------
    public void importExcelToJtableJava() {
        //DefaultTableModel model= (DefaultTableModel) jTable1.getModel();
        File excelFile;
        FileInputStream excelFIS = null;
        BufferedInputStream excelBIS = null;
        XSSFWorkbook excelImportToJTable = null;
        String defaultCurrentDirectoryPath = "C:\\Users\\Authentic\\Desktop";
        JFileChooser excelFileChooser = new JFileChooser(defaultCurrentDirectoryPath);
        excelFileChooser.setDialogTitle("Select Excel File");
        FileNameExtensionFilter fnef = new FileNameExtensionFilter("EXCEL FILES", "xls", "xlsx", "xlsm");
        excelFileChooser.setFileFilter(fnef);
        int excelChooser = excelFileChooser.showOpenDialog(null);
        if (excelChooser == JFileChooser.APPROVE_OPTION) {
            try {
                excelFile = excelFileChooser.getSelectedFile();
                //   jExcelFilePath.setText(excelFile.toString());
                excelFIS = new FileInputStream(excelFile);
                excelBIS = new BufferedInputStream(excelFIS);
                excelImportToJTable = new XSSFWorkbook(excelBIS);
                XSSFSheet excelSheet = excelImportToJTable.getSheetAt(0);
                for (int row = 1; row < excelSheet.getLastRowNum(); row++) {
                    XSSFRow excelRow = excelSheet.getRow(row);
                    /* XSSFCell excelLineNum = excelRow.getCell(0);
                    XSSFCell excelItemName = excelRow.getCell(1);
                    XSSFCell excelDescription = excelRow.getCell(2);
                    XSSFCell excelServiceDuration = excelRow.getCell(3);
                    XSSFCell excelQuantity = excelRow.getCell(4);
                   model.addRow(new Object[]{excelLineNum, excelItemName, excelDescription, excelServiceDuration,excelQuantity});
                     */

                    // aqui va el nombre de mis columas que importa de excel a mi jtable 
                    XSSFCell Codigo = excelRow.getCell(0);
                    XSSFCell NombreCompleto = excelRow.getCell(1);
                    XSSFCell Materia = excelRow.getCell(2);
                    XSSFCell Gestion = excelRow.getCell(3);
                    XSSFCell Examen1 = excelRow.getCell(4);
                    XSSFCell Examen2 = excelRow.getCell(5);
                    XSSFCell ExamenFin = excelRow.getCell(6);
                    XSSFCell Practico = excelRow.getCell(7);
                    XSSFCell Repechaje = excelRow.getCell(8);
                    XSSFCell PromedioFinal = excelRow.getCell(9);
                    //  model.addRow(new Object[]{Codigo, NombreCompleto, Materia, Gestion,Examen1,Examen2,ExamenFin,Practico,Repechaje,PromedioFinal});
                    tabla.addRow(new Object[]{Codigo, NombreCompleto, Materia, Gestion, Examen1, Examen2, ExamenFin, Practico, Repechaje, PromedioFinal});

                }
                JOptionPane.showMessageDialog(null, "Importacion Exitosa!!.....");
            } catch (IOException iOException) {
                JOptionPane.showMessageDialog(null, iOException.getMessage());
            } finally {
                try {
                    if (excelFIS != null) {
                        excelFIS.close();
                    }
                    if (excelBIS != null) {
                        excelBIS.close();
                    }
                    if (excelImportToJTable != null) {
                        excelImportToJTable.close();
                    }
                } catch (IOException iOException) {
                    JOptionPane.showMessageDialog(null, iOException.getMessage());
                }
            }
        }
    }

    Workbook book;

    public String Importar(File archivo, DefaultTableModel tabla) {
        String mensaje = "Error en la Importacion";
        //  DefaultTableModel modelo=tabla;
        // tabla.setModel(modelo);

        try {
            //CREA ARCHIVO CON EXTENSION XLS Y XLSX
            book = WorkbookFactory.create(new FileInputStream(archivo));
            Sheet hoja = book.getSheetAt(0);
            Iterator FilaIterator = hoja.rowIterator();
            int IndiceFila = -1;

            //VA SER VERDADERO SI EXISTEN FILAS POR RECORRER
            while (FilaIterator.hasNext()) {
                //INDICE FILA AUMENTA 1 POR CADA RECORRIDO
                IndiceFila++;
                Row fila = (Row) FilaIterator.next();
                //RECORRE LAS COLUMNAS O CELDAS DE UNA FILA YA CREADA
                Iterator ColumnaIterator = fila.cellIterator();
                //ASIGNAMOS EL MAXIMO DE COLUMNA PERMITIDO
                Object[] ListaColumna = new Object[15];
                int IndiceColumna = -1;
                //VA SER VERDADERO SI EXISTEN COLUMNAS POR RECORRER
                while (ColumnaIterator.hasNext()) {
                    //INDICE COLUMNA AUMENTA 1 POR CADA RECORRIDO
                    IndiceColumna++;
                    Cell celda = (Cell) ColumnaIterator.next();
                    //SI INDICE FILA ES IGUAL A "0" ENTONCES SE AGREGA UNA COLUMNA
                    if (IndiceFila == 0) {
                        // modelo.addColumn(celda.getStringCellValue());
                    } else {
                        if (celda != null) {
                            switch (celda.getCellType()) {
                                case NUMERIC:
                                    ListaColumna[IndiceColumna] = (int) Math.round(celda.getNumericCellValue());
                                    break;
                                case STRING:
                                    ListaColumna[IndiceColumna] = celda.getStringCellValue();
                                    break;
                                case BOOLEAN:
                                    ListaColumna[IndiceColumna] = celda.getBooleanCellValue();
                                    break;
                                case BLANK: {
                                    ListaColumna[IndiceColumna] = null;
                                }
                                default: {
                                    // ListaColumna[IndiceColumna]=celda.getDateCellValue();
                                    ListaColumna[IndiceColumna] = null;
                                    break;
                                }
                            }
                        }
                    }
                }

                if (IndiceFila != 0) {
                    tabla.addRow(ListaColumna);
                }
            }
            mensaje = "Importacion Exitosa";

        } catch (Exception e) {
        }
        return mensaje;
    }

    JFileChooser SelectArchivo = new JFileChooser();

    public void AgregarFiltro() {
        SelectArchivo.setFileFilter(new FileNameExtensionFilter("Excel (*.xls)", "xls"));
        SelectArchivo.setFileFilter(new FileNameExtensionFilter("Excel (*.xlsx)", "xlsx"));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewPanelDocente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new NewPanelDocente().setVisible(true);
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem3;
    public javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    public javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
