import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Vector;

public class navArch extends JFrame {
    JTree ArbolArchivos;
    DefaultMutableTreeNode Root;
    Vector<String> ListaDir=new Vector<String>();
    Vector<String> ListaArch=new Vector<String>();
    public navArch()
    {
        super("NavCommander");
        setTitle("NavCommander");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(getContentPane());
        this.setLayout(new GridLayout(1,1));
        this.setVisible(true);
        this.setBounds(100,30,400,300);
        createTree();
        this.add(ArbolArchivos);
    }
    public void createTree(){
        Root = new DefaultMutableTreeNode("Raíz");
        ArbolArchivos = new JTree(Root);
        ArbolArchivos.setVisible(true);
        ArbolArchivos.setBounds(100,30,100,200);
        addHijos(Root);
        ArbolArchivos.addTreeSelectionListener(
                new TreeSelectionListener() {
                    @Override
                    public void valueChanged(TreeSelectionEvent e) {
                        DefaultMutableTreeNode seleccionado=(DefaultMutableTreeNode)ArbolArchivos.getLastSelectedPathComponent();
                     if(ListaDir.contains(seleccionado.getUserObject().toString())){

                         addHijos(seleccionado);
                     }
                    }
                }
        );


    }

    public void addHijos(DefaultMutableTreeNode nodo){
        ListaDir=new Vector<String>();
       ListaArch=new Vector<String>();

        try{
            Process sysCall;
            BufferedReader br;
            String s="";
            String  strCmd="";
            sysCall=Runtime.getRuntime().exec(strCmd);
            br=new BufferedReader(new InputStreamReader(sysCall.getInputStream()));
            while((s=br.readLine())!=null)
            {
                System.out.println(s);

                int indexArch="                                         ".length();


                if(s.contains("<DIR>")){

                    int indexDir=s.indexOf("<DIR>")+"<DIR>".length()+1;
                    String nomDir=s.substring(indexDir);
                    ListaDir.add(nomDir.trim());
                }

                else{
                    String nomArch=(s.length()>indexArch)?s.substring(indexArch):s;
                    ListaArch.add(nomArch);
                }
            }
            ListaDir=new Vector<String>(ListaDir.subList(2,ListaDir.size()));
            ListaArch=new Vector<String>(ListaArch.subList(5,ListaArch.size()-2));
            int i;
            for(i = 0; i<ListaDir.size();i++){
                DefaultMutableTreeNode hijo=new DefaultMutableTreeNode(ListaDir.get(i).toString());
                nodo.add(hijo);
            }
            for(i = 0; i<ListaArch.size();i++){
                DefaultMutableTreeNode hijo=new DefaultMutableTreeNode(ListaArch.get(i).toString());
                nodo.add(hijo);
            }

        }catch(Exception e){}
    }

    public void
    public static void main(String[] args){
       new navArch();
   }


}