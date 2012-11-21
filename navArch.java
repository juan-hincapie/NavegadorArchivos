
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

public class navArch extends JFrame {
    JTree ArbolArchivos;
    DefaultMutableTreeNode Root;
    Vector<String> ListaDirSup=new Vector<String>();
    Vector<String> ListaArchSup=new Vector<String>();
    String localDir;
    
   public navArch()
    {
        super("NavCommander");
        setTitle("NavCommander");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(getContentPane());
        
        Container c1 = getContentPane();
        c1.setLayout(new BorderLayout());
        
        JPanel Botones = new JPanel();
        JPanel DirOrig = new JPanel();
        JPanel DirFin = new JPanel();
        //this.setLayout(new BorderLayout(7,15));
        
        Botones.setLayout(new GridLayout(4, 1));
        DirOrig.setLayout(new BorderLayout(50,50));
        Botones.add(new JButton("Copy"));
        Botones.add(new JButton("Paste"));
        Botones.add(new JButton("Delete"));
        Botones.add(new JButton("Move"));
        c1.add(Botones,BorderLayout.CENTER);
        
        JLabel DO = new JLabel("Directorio de Origen");
        DirOrig.add(DO,BorderLayout.NORTH);
        c1.add(DirOrig, BorderLayout.CENTER);
        
        JLabel DF = new JLabel("Directorio Final");
        DirFin.add(DF);
        c1.add(DirFin, BorderLayout.CENTER);
        
        JPanel cont = new JPanel();
        cont.setLayout(new GridLayout(1,3));
    
        cont.add(DO);
        cont.add(Botones);
        cont.add(DF);
        
        c1.add(cont,BorderLayout.CENTER);
   
        this.setVisible(true);
        this.setBounds(100,30,400,300);
        createTree();
        JPanel arbol=new JPanel(new GridLayout(1,1,30,30));
        arbol.setVisible(true);
        //createTree();
        JTree jtree=new JTree();
        ArbolArchivos.setBounds(0, 0, 300, 700);
        arbol.add(ArbolArchivos,1,1);
        DirOrig.add(arbol,BorderLayout.CENTER);
    }
    public void createTree(){
        Root = new DefaultMutableTreeNode("Raíz");
        ArbolArchivos = new JTree(Root);
        ArbolArchivos.setVisible(true);
        ArbolArchivos.setBounds(100,30,100,200);
        addHijos(Root);
        try{
            Process dir=Runtime.getRuntime().exec("cmd /c cd");        
            BufferedReader br=new BufferedReader(new InputStreamReader(dir.getInputStream()));
            localDir=br.readLine();
        }catch(Exception e){}
        
        ArbolArchivos.addTreeSelectionListener(
                new TreeSelectionListener() {
                    @Override
                    public void valueChanged(TreeSelectionEvent e) {
                        DefaultMutableTreeNode seleccionado=(DefaultMutableTreeNode)ArbolArchivos.getLastSelectedPathComponent();
                     if(ListaDirSup.contains(seleccionado.getUserObject().toString())){
                         addHijos(seleccionado);
                     }
                    }
                }
        );


    }

    public void addHijos(DefaultMutableTreeNode nodo){
        if(nodo.getChildCount()!=0){
            nodo.removeAllChildren();
        }
        Vector<String> ListaDir=new Vector<String>();
        Vector<String> ListaArch=new Vector<String>();
        DefaultMutableTreeNode upNode=(nodo.isRoot())?nodo:(DefaultMutableTreeNode)nodo.getParent();
        String Ruta=nodo.getUserObject().toString();
        while(!upNode.isRoot())
        {
            Ruta=upNode.getUserObject().toString()+"\\"+Ruta;
            upNode=(DefaultMutableTreeNode)upNode.getParent();
        }
 
        try{
            Process sysCall;
            BufferedReader br;
            String s="";
            String  strCmd="cmd /c cd "+localDir+"\\"+Ruta+ " & dir";
            System.out.println(strCmd);
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
            ListaArchSup.addAll(ListaArch);
            ListaDirSup.addAll(ListaDir);
        }catch(Exception e){}
    }


    public static void main(String[] args){
        navArch navArch = new navArch();
        navArch.setSize(500, 300);
   }


}