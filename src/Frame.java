/**
 * Created by Mateusz on 09.03.2016.
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

class Frame extends JFrame
{

    private ArrayList<String> sTransitionsNextTapeSymbol;
    public Frame()
    {
        setTitle("Turing Machine Emulator");
        setSize(230,500);
        setResizable(true);

        String plaf = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
        try
        {
            UIManager.setLookAndFeel(plaf);
            SwingUtilities.updateComponentTreeUI(Frame.this);
        }
        catch(Exception e)
        {
        }

        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        JMenu helpMenu = new JMenu("Help");
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        bMain = new JPanel(new BorderLayout());
        bTransitionFunction = new JPanel(new BorderLayout());
        bTransitionFunctionSouth = new JPanel(new BorderLayout());
        gTransitionFunctionSouth = new JPanel(new GridLayout(1,2));
        gNorth = new JPanel(new GridLayout(6,1));
        gSouth = new JPanel(new GridLayout(5,1));
        gNewTransitionOptions = new JPanel(new GridLayout(1,10));

        sTransitionsState = new ArrayList<String>();
        sTransitionsCondition = new ArrayList<String>();
        sTransitionsResult = new ArrayList<String>();

        sTransitionsNextState = new ArrayList<String>();
        sTransitionsNewTapeSymbol = new ArrayList<String>();
        sTransitionsPointerDirection = new ArrayList<String>();

        final DefaultListModel<String> transitionFunctionListModel = new DefaultListModel<String>();
        lsTransitionFunction = new JList<String>();
        lsTransitionFunction.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        lsTransitionFunction.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        lsTransitionFunction.setVisibleRowCount(-1);
        lsTransitionFunction = new JList<String>(transitionFunctionListModel);

        slTransitionFunction = new JScrollPane(lsTransitionFunction);

        newTransitionFieldState = new JTextField();
        newTransitionFieldCondition = new JTextField();
        //newTransitionFieldResult = new JTextField();

        newTransitionFieldNextState = new JTextField();
        newTransitionFieldNewTapeSymbol = new JTextField();
        newTransitionPointerDirection = new JTextField();

        cbBlankSymbol = new JComboBox<String>();
        popupMenuListener = new cbBlankSymbolListener();
        cbBlankSymbol.addPopupMenuListener(popupMenuListener);

        cbFirstState = new JComboBox<String>();
        popupMenuListenerSymbol = new cbFirstStateListener();
        cbFirstState.addPopupMenuListener(popupMenuListenerSymbol);

        addTransitionButton = new JButton("Add");
        addTransitionButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                String newForm = "( " + newTransitionFieldState.getText()+" ; "+ newTransitionFieldCondition.getText()+" ) -> (" +newTransitionFieldNextState.getText()+" , "+newTransitionFieldNewTapeSymbol.getText()+" , "+newTransitionPointerDirection.getText()+")";
                if(!newTransitionFieldState.getText().equals("") && !newTransitionFieldCondition.getText().equals("") && !newTransitionFieldNextState.getText().equals("") && !newTransitionFieldNewTapeSymbol.getText().equals("") && !newTransitionPointerDirection.getText().equals(""))
                {
                    boolean check1 = false;
                    boolean check2 = false;
                    boolean check3 = false;
                    sAlphabet = alphabetTextField.getText();
                    sStates = statesTextField.getText();
                    partsGpString = sAlphabet.split(" ");
                    for(int j = 0; j < partsGpString.length;j++)
                    {
                        if(newTransitionFieldCondition.getText().equals(partsGpString[j]))
                        {
                            check1 = true;
                        }
                    }
                    partsGpString = sStates.split(" ");
                    for(int j = 0; j < partsGpString.length;j++)
                    {
                        if(newTransitionFieldState.getText().equals(partsGpString[j]))
                        {
                            check2 = true;
                        }
                    }
                    for(int j = 0; j < partsGpString.length;j++)
                    {
                        if(newTransitionFieldNextState.getText().equals("-"))
                        {
                            check3 = true;
                        }
                        if(newTransitionFieldNextState.getText().equals(partsGpString[j]))
                        {
                            check3 = true;
                        }
                    }
                    if(check1 == true && check2 == true && check3 == true)
                    {
                        sTransitionsState.add(newTransitionFieldState.getText());
                        sTransitionsCondition.add(newTransitionFieldCondition.getText());
                        sTransitionsNextState.add(newTransitionFieldNextState.getText());
                        sTransitionsNewTapeSymbol.add(newTransitionFieldNewTapeSymbol.getText());
                        sTransitionsPointerDirection.add(newTransitionPointerDirection.getText());


                        transitionFunctionListModel.addElement(newForm);
                        newTransitionFieldState.setText("");
                        newTransitionFieldCondition.setText("");
                        newTransitionFieldNextState.setText("");
                        newTransitionFieldNewTapeSymbol.setText("");
                        newTransitionPointerDirection.setText("");
                    }
                    else
                        JOptionPane.showConfirmDialog(null, "It seems that your transition doesn't mach the alphabet or states list.", "Export Error",JOptionPane.PLAIN_MESSAGE,JOptionPane.ERROR_MESSAGE);
                }

                repaint();
            }
        });

        removeTransitionButton = new JButton("Remove");
        removeTransitionButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                int index;
                index = lsTransitionFunction.getSelectedIndex();
                if(index>=0)
                {
                    sTransitionsState.remove(index);
                    sTransitionsCondition.remove(index);
                    sTransitionsNextState.remove(index);
                    sTransitionsPointerDirection.remove(index);
                    sTransitionsNextState.remove(index);
                    transitionFunctionListModel.removeElementAt(index);
                }
                repaint();
            }
        });

        runButton = new JButton("Run");
        runButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
            /*    getTextFields();
                if(sDialog==null)
                {
                    sDialog = new ResultsFrame(Frame.this);
                }
                sDialog.setVisible(true);
                sDialog.setResizable(false);*/

                transitionFunctionListModel.addElement("a");
            }
        });
        chooseFile = new JFileChooser();
        Action exportAction = new AbstractAction("Export")
        {
            public void actionPerformed(ActionEvent event)
            {
                getTextFields();
                if(!sStates.equals("") && !sAlphabet.equals("") && !cbFirstState.getSelectedItem().equals("") && sTransitionsState.size()!=0)
                {
                    int i = chooseFile.showSaveDialog(null);
                    if( i == JFileChooser.APPROVE_OPTION)
                    {
                        filename = chooseFile.getSelectedFile().getPath();
                        PrintWriter writer;
                        try
                        {

                            writer = new PrintWriter(filename, "UTF-8");
                            writer.println(sStates);
                            writer.println(sAlphabet);
                            writer.println(cbBlankSymbol.getSelectedItem());
                            for(int j=0 ; j < sTransitionsState.size();j++)
                            {
                                gpString = ("( " + sTransitionsState.get(j) + " ; " + sTransitionsCondition.get(j) + " ) -> ( "+sTransitionsNextState.get(j) + " , " + sTransitionsNewTapeSymbol.get(j) + " , " + sTransitionsPointerDirection.get(j) + " )" );
                                writer.println(gpString);
                            }
                            writer.println(cbFirstState.getSelectedItem());
                            writer.println(sFinish);
                            writer.close();

                        }
                        catch (FileNotFoundException e)
                        {


                        }
                        catch (UnsupportedEncodingException e)
                        {

                        }

                    }
                }
                else
                    JOptionPane.showConfirmDialog(null, "Machine is not completed yet", "Export Error",JOptionPane.PLAIN_MESSAGE,JOptionPane.ERROR_MESSAGE);
            }
        };
        Action importAction = new AbstractAction("Import")
        {
            public void actionPerformed(ActionEvent event)
            {
                getTextFields();
                int selection;
                if(!sStates.equals("") || !sAlphabet.equals("")  || !sFinish.equals("") || sTransitionsState.size()!=0)
                    selection = JOptionPane.showConfirmDialog(null, "Are you sure? It will remove current machine.", "Confirmation",JOptionPane.OK_OPTION,JOptionPane.QUESTION_MESSAGE);
                else
                    selection = JOptionPane.OK_OPTION;
                if( selection == JOptionPane.OK_OPTION)
                {
                    int i = chooseFile.showOpenDialog(null);
                    if( i == JFileChooser.APPROVE_OPTION)
                    {
                        filename = chooseFile.getSelectedFile().getPath();
                        Scanner in;
                        try
                        {
                            in = new Scanner(new File(filename));
                            alphabetTextField.setText("");
                            statesTextField.setText("");
                            finishTextField.setText("");

                            sStates = "";
                            sAlphabet = "";
                            sFinish = "";

                            for(int j = sTransitionsState.size()-1; j >=0;j-- )
                            {
                                sTransitionsState.remove(j);
                                sTransitionsCondition.remove(j);
                                sTransitionsNextState.remove(j);
                                sTransitionsNewTapeSymbol.remove(j);
                                sTransitionsPointerDirection.remove(j);
                                transitionFunctionListModel.removeElementAt(j);
                            }
                            while(in.hasNextLine())
                            {
                                gpString = in.nextLine();
                                statesTextField.setText(gpString);
                                gpString = in.nextLine();
                                alphabetTextField.setText(gpString);
                                //
                                partsGpString = gpString.split(" ");
                                cbBlankSymbol.removeAllItems();
                                for(int z = 0; z < partsGpString.length;z++)
                                {
                                    cbBlankSymbol.addItem(partsGpString[z]);
                                }
                                //
                                gpString = in.nextLine();
                                cbBlankSymbol.setSelectedItem(gpString);
                                gpString = in.nextLine();
                                while(gpString.charAt(0)=='(')
                                {
                                    partsGpString = gpString.split(" ");

                                    String newForm = "( " + partsGpString[1]+" ; "+ partsGpString[3]+" ) -> ("+partsGpString[7]+" , "+partsGpString[9]+" , "+partsGpString[11]+")";
                                    sTransitionsState.add(partsGpString[1]);
                                    sTransitionsCondition.add(partsGpString[3]);
                                    sTransitionsNextState.add(partsGpString[7]);
                                    sTransitionsNewTapeSymbol.add(partsGpString[9]);
                                    sTransitionsPointerDirection.add(partsGpString[11]);
                                    transitionFunctionListModel.addElement(newForm);
                                    gpString = in.nextLine();
                                }
                                partsGpString = gpString.split(" ");
                                cbFirstState.removeAllItems();
                                for(int z = 0; z < partsGpString.length;z++)
                                {
                                    cbFirstState.addItem(partsGpString[z]);
                                }
                                cbFirstState.setSelectedItem(gpString);
                                gpString = in.nextLine();
                                finishTextField.setText(gpString);
                            }
                        }
                        catch (FileNotFoundException e)
                        {

                        }
                    }
                }
            }
        };
        Action exitAction = new AbstractAction("Exit")
        {
            public void actionPerformed(ActionEvent event)
            {
                int exit;
                getTextFields();
                if(!sStates.equals("") || !sAlphabet.equals("") || !cbFirstState.getSelectedItem().equals("") || !sFinish.equals("") || sTransitionsState.size()!=0)
                    exit = JOptionPane.showConfirmDialog(null, "Are you sure? The unsaved progress will be lost.", "Confirmation",JOptionPane.OK_OPTION,JOptionPane.QUESTION_MESSAGE);
                else
                    exit = JOptionPane.OK_OPTION;
                if(exit == JOptionPane.OK_OPTION)
                    System.exit(0);
            }
        };
        Action aboutAction = new AbstractAction("About")
        {
            public void actionPerformed(ActionEvent event)
            {
                if(aDialog==null)
                {
                    aDialog = new AboutDialog(Frame.this);
                }
                aDialog.setVisible(true);
                aDialog.setResizable(false);
            }
        };
        Action newAction = new AbstractAction("New")
        {
            public void actionPerformed(ActionEvent event)
            {
                int selection;
                getTextFields();
                if(!sStates.equals("") || !sAlphabet.equals("") || !cbFirstState.getSelectedItem().equals("") || !sFinish.equals("") || sTransitionsState.size()!=0)
                    selection = JOptionPane.showConfirmDialog(null, "Are you sure? It will remove current machine.", "Confirmation",JOptionPane.OK_OPTION,JOptionPane.QUESTION_MESSAGE);
                else
                    selection = JOptionPane.OK_OPTION;
                if( selection == JOptionPane.OK_OPTION)
                {
                    alphabetTextField.setText("");
                    statesTextField.setText("");
                    finishTextField.setText("");
                    cbBlankSymbol.removeAllItems();
                    for(int z = 0; z < partsGpString.length;z++)
                    {
                        cbBlankSymbol.addItem(partsGpString[z]);
                    }
                    cbFirstState.removeAllItems();
                    for(int z = 0; z < partsGpString.length;z++)
                    {
                        cbFirstState.addItem(partsGpString[z]);
                    }
                    sStates = "";
                    sAlphabet = "";
                    sFinish = "";

                    for(int i = sTransitionsState.size()-1; i >=0;i-- )
                    {
                        sTransitionsState.remove(i);
                        sTransitionsCondition.remove(i);
                        sTransitionsNextState.remove(i);
                        sTransitionsNewTapeSymbol.remove(i);
                        sTransitionsPointerDirection.remove(i);
                        transitionFunctionListModel.removeElementAt(i);
                    }
                }
            }
        };
        fileMenu.add(newAction);
        fileMenu.addSeparator();
        fileMenu.add(importAction);
        fileMenu.add(exportAction);
        fileMenu.addSeparator();
        fileMenu.add(exitAction);
        helpMenu.add(aboutAction);

        alphabetTextField = new JTextField();
        statesTextField = new JTextField();
        finishTextField = new JTextField();

        gNorth.add(new JLabel("States:",SwingConstants.CENTER));
        gNorth.add(statesTextField);
        gNorth.add(new JLabel("Allowed Symbols:",SwingConstants.CENTER));
        gNorth.add(alphabetTextField);
        gNorth.add(new JLabel("Blank Symbol:",SwingConstants.CENTER));
        gNorth.add(cbBlankSymbol);

        gSouth.add(new JLabel("Beginning State:",SwingConstants.CENTER));
        gSouth.add(cbFirstState);
        gSouth.add(new JLabel("Finish State: (optional)",SwingConstants.CENTER));
        gSouth.add(finishTextField);
        gSouth.add(runButton);

        gNewTransitionOptions.add(new JLabel("(",SwingConstants.CENTER));
        gNewTransitionOptions.add(newTransitionFieldState);
        gNewTransitionOptions.add(new JLabel(";",SwingConstants.CENTER));
        gNewTransitionOptions.add(newTransitionFieldCondition);
        gNewTransitionOptions.add(new JLabel(")",SwingConstants.CENTER));
        gNewTransitionOptions.add(newTransitionFieldNextState);
        gNewTransitionOptions.add(new JLabel(",",SwingConstants.CENTER));
        gNewTransitionOptions.add(newTransitionFieldNewTapeSymbol);
        gNewTransitionOptions.add(new JLabel(",",SwingConstants.CENTER));
        gNewTransitionOptions.add(newTransitionPointerDirection);

        gTransitionFunctionSouth.add(addTransitionButton);
        gTransitionFunctionSouth.add(removeTransitionButton);
        bTransitionFunctionSouth.add(gNewTransitionOptions,BorderLayout.NORTH);
        bTransitionFunctionSouth.add(gTransitionFunctionSouth,BorderLayout.SOUTH);
        bTransitionFunction.add(new JLabel("Transition Function",SwingConstants.CENTER),BorderLayout.NORTH);
        bTransitionFunction.add(slTransitionFunction,BorderLayout.CENTER);
        bTransitionFunction.add(bTransitionFunctionSouth,BorderLayout.SOUTH);
        bMain.add(gSouth,BorderLayout.SOUTH);
        bMain.add(gNorth,BorderLayout.NORTH);
        bMain.add(bTransitionFunction, BorderLayout.CENTER);
        add(bMain);
        repaint();
    }
    private void getTextFields()
    {
        sStates = statesTextField.getText();
        sAlphabet = alphabetTextField.getText();
        sFinish = finishTextField.getText();
        blankSymbolString = (String) cbBlankSymbol.getSelectedItem();
        firstStateString = (String) cbFirstState.getSelectedItem();
        return;
    }
    class cbBlankSymbolListener implements PopupMenuListener
    {
        @Override
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            partsGpString = alphabetTextField.getText().split(" ");
            cbBlankSymbol.removeAllItems();
            for(int i = 0; i < partsGpString.length;i++)
            {
                cbBlankSymbol.addItem(partsGpString[i]);
            }
        }

        @Override
        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void popupMenuCanceled(PopupMenuEvent e) {
            // TODO Auto-generated method stub

        }
    }
    class cbFirstStateListener implements PopupMenuListener
    {

        @Override
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            partsGpString = statesTextField.getText().split(" ");
            cbFirstState.removeAllItems();
            for(int i = 0; i < partsGpString.length;i++)
            {
                cbFirstState.addItem(partsGpString[i]);
            }

        }

        @Override
        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void popupMenuCanceled(PopupMenuEvent e) {
            // TODO Auto-generated method stub

        }

    }
    private JMenuBar menuBar;

    public JComboBox<String> cbBlankSymbol;
    public JComboBox<String> cbFirstState;

    private JPanel bMain;
    private JPanel bTransitionFunction;
    private JPanel bTransitionFunctionSouth;
    private JPanel gTransitionFunctionSouth;
    private JPanel gNorth;
    private JPanel gSouth;
    private JPanel gNewTransitionOptions;

    public static String sStates;
    public static String sAlphabet;
    public static ArrayList<String> sTransitionsState;
    public static ArrayList<String> sTransitionsCondition;
    public static ArrayList<String> sTransitionsResult;
    public static ArrayList<String> sTransitionsNextState;
    public static ArrayList<String> sTransitionsNewTapeSymbol;
    public static ArrayList<String> sTransitionsPointerDirection;
    public static String sFinish;

    private String gpString;

    private String[] partsGpString;

    private JScrollPane slTransitionFunction;

    public static JList<String> lsTransitionFunction;

    private JFileChooser chooseFile;
    private String filename;

    private JButton addTransitionButton;
    private JButton removeTransitionButton;
    private JButton runButton;

    private PopupMenuListener popupMenuListener;
    private PopupMenuListener popupMenuListenerSymbol;

    private JTextField newTransitionFieldState;
    private JTextField newTransitionFieldCondition;
    private JTextField newTransitionFieldNextState;
    private JTextField newTransitionFieldNewTapeSymbol;
    private JTextField newTransitionPointerDirection;

    private JTextField alphabetTextField;
    private JTextField statesTextField;
    private JTextField finishTextField;

    private ResultsFrame sDialog;
    private AboutDialog aDialog;

    public static String blankSymbolString;
    public static String firstStateString;
}