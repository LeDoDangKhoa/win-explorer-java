package sict.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class FileExplorer implements Runnable {

    private DefaultMutableTreeNode root;

    private DefaultTreeModel treeModel;

    private JTree tree;
    
    private JMenuItem menuDelete;
    private JPopupMenu popup;
    
    private File currentFile;

    @Override
    public void run() {
        JFrame frame = new JFrame("File Explorer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        File fileRoot = new File("C:/");
        root = new DefaultMutableTreeNode(new FileNode(fileRoot));
        treeModel = new DefaultTreeModel(root);

        tree = new JTree(treeModel);
        tree.setShowsRootHandles(true);
        JScrollPane scrollPane = new JScrollPane(tree);

        frame.add(scrollPane);
        frame.setLocationByPlatform(true);
        frame.setSize(640, 480);
        frame.setVisible(true);

        CreateChildNodes ccn = 
                new CreateChildNodes(fileRoot, root);
        new Thread(ccn).start();
        menuDelete = new JMenuItem("Xóa");
        popup = new JPopupMenu();
        popup.add(menuDelete);
        popup.addSeparator();
        
        tree.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				if(e.isPopupTrigger())
				{
					popup.show(e.getComponent(), e.getX(), e.getY());
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
        menuDelete.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				TreePath[] paths = tree.getSelectionPaths();
				currentFile = new File(paths+"");
				int result = JOptionPane.showConfirmDialog(null, "Bạn muốn xóa file này","Xóa File",JOptionPane.ERROR_MESSAGE);
				if(result==JOptionPane.OK_OPTION)
				{
					currentFile.delete();
				}
			}
		});
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new FileExplorer());
    }

    public class CreateChildNodes implements Runnable {

        private DefaultMutableTreeNode root;

        private File fileRoot;

        public CreateChildNodes(File fileRoot, 
                DefaultMutableTreeNode root) {
            this.fileRoot = fileRoot;
            this.root = root;
        }

        @Override
        public void run() {
            createChildren(fileRoot, root);
        }

        private void createChildren(File fileRoot, 
                DefaultMutableTreeNode node) {
            File[] files = fileRoot.listFiles();
            if (files == null) return;

            for (File file : files) {
                DefaultMutableTreeNode childNode = 
                        new DefaultMutableTreeNode(new FileNode(file));
                node.add(childNode);
                if (file.isDirectory()) {
                    createChildren(file, childNode);
                }
            }
        }

    }

    public class FileNode {

        private File file;

        public FileNode(File file) {
            this.file = file;
        }

        @Override
        public String toString() {
            String name = file.getName();
            if (name.equals("")) {
                return file.getAbsolutePath();
            } else {
                return name;
            }
        }
    }

}