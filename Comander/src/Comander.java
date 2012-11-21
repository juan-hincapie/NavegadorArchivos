import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Vector;

public class Comander {
    private JTextField txtDest;
    private JTextPane textPane1;
    private JPanel panel1;
    private JTree trOrig;
    private JTree trDest;
    private JButton copiarButton;
    private JButton moverButton;
    private JButton borrarButton;
    private JButton renombrarButton;
    private JTextField txtOrig;
    DefaultMutableTreeNode RootOr;
    DefaultMutableTreeNode RootDe;
    Vector<String> ListaDirSupOr = new Vector<String>();
    Vector<String> ListaArchSupOr = new Vector<String>();
    Vector<String> ListaDirSupDe = new Vector<String>();
    Vector<String> ListaArchSupDe = new Vector<String>();
    String localDir;
    String Ruta;


    public Comander() {
        RootOr = new DefaultMutableTreeNode("Raíz");
        RootDe = new DefaultMutableTreeNode("Raíz");
        addHijos(RootOr, "O");
        addHijos(RootDe, "D");
        try {
            Process dir = Runtime.getRuntime().exec("cmd /c cd");
            BufferedReader br = new BufferedReader(new InputStreamReader(dir.getInputStream()));
            localDir = br.readLine();
        } catch (Exception e) {
        }
        $$$setupUI$$$();
        trOrig.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode seleccionado = (DefaultMutableTreeNode) trOrig.getLastSelectedPathComponent();
                if (ListaDirSupOr.contains(seleccionado.getUserObject().toString())) {
                    addHijos(seleccionado, "O");
                } else if (ListaArchSupOr.contains(seleccionado.getUserObject().toString())) {
                    Ruta += "\\" + seleccionado.getUserObject().toString();
                }
                txtOrig.setText(localDir + "\\" + Ruta);
            }
        });
        trDest.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode seleccionado = (DefaultMutableTreeNode) trDest.getLastSelectedPathComponent();
                if (ListaDirSupDe.contains(seleccionado.getUserObject().toString())) {
                    addHijos(seleccionado, "D");
                } else if (ListaArchSupDe.contains(seleccionado.getUserObject().toString())) {
                    Ruta += "\\" + seleccionado.getUserObject().toString();
                }
                txtDest.setText(localDir + "\\" + Ruta);
            }
        });
    }

    public void addHijos(DefaultMutableTreeNode nodo, String origen) {
        if (nodo.getChildCount() != 0) {
            nodo.removeAllChildren();
        }
        Vector<String> ListaDir = new Vector<String>();
        Vector<String> ListaArch = new Vector<String>();
        DefaultMutableTreeNode upNode = (nodo.isRoot()) ? nodo : (DefaultMutableTreeNode) nodo.getParent();
        Ruta = nodo.getUserObject().toString();
        while (!upNode.isRoot()) {
            Ruta = upNode.getUserObject().toString() + "\\" + Ruta;
            upNode = (DefaultMutableTreeNode) upNode.getParent();
        }

        try {
            Process sysCall;
            BufferedReader br;
            String s = "";
            String strCmd = "cmd /c cd " + localDir + "\\" + Ruta + " & dir";
            sysCall = Runtime.getRuntime().exec(strCmd);
            br = new BufferedReader(new InputStreamReader(sysCall.getInputStream()));
            while ((s = br.readLine()) != null) {
                int indexArch = "                                         ".length();
                if (s.contains("<DIR>")) {
                    int indexDir = s.indexOf("<DIR>") + "<DIR>".length() + 1;
                    String nomDir = s.substring(indexDir);
                    ListaDir.add(nomDir.trim());
                } else {
                    String nomArch = (s.length() > indexArch) ? s.substring(indexArch) : s;
                    ListaArch.add(nomArch);
                }
            }
            ListaDir = new Vector<String>(ListaDir.subList(2, ListaDir.size()));
            ListaArch = new Vector<String>(ListaArch.subList(5, ListaArch.size() - 2));
            int i;
            for (i = 0; i < ListaDir.size(); i++) {
                DefaultMutableTreeNode hijo = new DefaultMutableTreeNode(ListaDir.get(i).toString());
                hijo.setAllowsChildren(true);

                nodo.add(hijo);
            }
            for (i = 0; i < ListaArch.size(); i++) {
                DefaultMutableTreeNode hijo = new DefaultMutableTreeNode(ListaArch.get(i).toString());
                nodo.add(hijo);
            }
            if (origen.equals("O")) {
                ListaArchSupOr.addAll(ListaArch);
                ListaDirSupOr.addAll(ListaDir);
            } else {
                ListaArchSupDe.addAll(ListaArch);
                ListaDirSupDe.addAll(ListaDir);
            }
        } catch (Exception e) {
        }
    }

    private void createUIComponents() {
        trOrig = new JTree(RootOr);
        trDest = new JTree(RootDe);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Comander");
        frame.setContentPane(new Comander().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(trOrig, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        panel1.add(trDest, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JToolBar toolBar1 = new JToolBar();
        panel1.add(toolBar1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 20), null, 0, false));
        copiarButton = new JButton();
        copiarButton.setText("Copiar");
        toolBar1.add(copiarButton);
        moverButton = new JButton();
        moverButton.setText("Mover");
        toolBar1.add(moverButton);
        borrarButton = new JButton();
        borrarButton.setText("Borrar");
        toolBar1.add(borrarButton);
        renombrarButton = new JButton();
        renombrarButton.setText("Renombrar");
        toolBar1.add(renombrarButton);
        final JLabel label1 = new JLabel();
        label1.setText("Directorio Origen");
        panel1.add(label1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Directorio Destino");
        panel1.add(label2, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText(">>");
        panel1.add(label3, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtDest = new JTextField();
        panel1.add(txtDest, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtOrig = new JTextField();
        panel1.add(txtOrig, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
