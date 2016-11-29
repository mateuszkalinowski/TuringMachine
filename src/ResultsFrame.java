/**
 * Created by Mateusz on 09.03.2016.
 */
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;


class ResultsFrame extends JDialog
{
    public ResultsFrame(JFrame owner)
    {
        super(owner,"Machine - Results",true);
        setSize(350,200);
        setResizable(true);

        bMain = new JPanel(new BorderLayout());
        gNorth = new JPanel(new GridLayout(2,1));

        inputStringField = new JTextField();
        output = new JTextArea();
        output.setEditable(false);
        output.setLineWrap(true);
        output.setText("");
        outputScroll = new JScrollPane();
        inputStringField.setText("");
        scrollOutput = new JScrollPane(output);
        JButton executeButton = new JButton("Execute");

        executeButton.addActionListener(new ActionListener()
        {

            public void actionPerformed(ActionEvent event)
            {
                accepted = false;
                error = false;
                check = false;
                exit = false;
                output.setText("Turing Machine Steps:\n");
                partsAlphabet = Frame.sAlphabet.split(" ");
                partsStates = Frame.sStates.split(" ");
                partsFinish = Frame.sFinish.split(" ");
                mainTape = new ArrayList<String>();
                inputString = inputStringField.getText();
                for(int i = 0; i < inputString.length() ; i++)
                {
                    mainTape.add(inputString.charAt(i) + "");
                }
                mainTape.add(Frame.blankSymbolString);
                //Check for input errors

                if(compare(Frame.sAlphabet,inputString)==false)
                    error=true;

                for(int i = 0; i < Frame.sTransitionsCondition.size();i++)
                {
                    if(compare(Frame.sAlphabet,Frame.sTransitionsCondition.get(i))==false)
                    {
                        error=true;
                        break;
                    }
                }

                state = Frame.firstStateString;
                pointerPosition = 0;
                //machineStatus+= state;
                if(error==false)
                {

                    while(true)
                    {

                        inputSymbol = mainTape.get(pointerPosition);
                        if(Frame.sTransitionsState.size()==0)
                            break;
                        for(int j=0;j<Frame.sTransitionsState.size();j++)
                        {
                            //check = false;
                            for(int k = 0; k < partsFinish.length;k++)
                            {
                                if(state.equals(partsFinish[k]))
                                {
                                    exit = true;
                                }
                            }
                            if(Frame.sTransitionsState.get(j).equals(state))
                            {
                                if(Frame.sTransitionsCondition.get(j).equals(inputSymbol))
                                {
                                    //	check=true;
                                    if(Frame.sTransitionsNextState.get(j).equals("-"))
                                    {
                                        exit = true;
                                        break;
                                    }
                                    state = Frame.sTransitionsNextState.get(j);
                                    mainTape.set(pointerPosition, Frame.sTransitionsNewTapeSymbol.get(j));
                                    if(Frame.sTransitionsPointerDirection.get(j).equals("P"))
                                    {
                                        pointerPosition++;
                                    }
                                    else
                                        pointerPosition--;
                                    break;
                                }
                            }
                        }
                        for(int l = 0; l < mainTape.size();l++)
                        {
                            output.append(mainTape.get(l));
                        }
                        if(pointerPosition == mainTape.size())
                        {
                            mainTape.add(Frame.blankSymbolString);
                        }
                        output.append("\n");
                        if(exit == true)
                        {
                            break;
                        }
                    }
                }
                repaint();
            }
        });

        gNorth.add(new JLabel("Input Tape:",SwingConstants.CENTER));
        gNorth.add(inputStringField);
        bMain.add(scrollOutput,BorderLayout.CENTER);
        bMain.add(executeButton,BorderLayout.SOUTH);
        bMain.add(gNorth,BorderLayout.NORTH);
        add(bMain);
    }

    private boolean compare(String source, String second)
    {
        boolean good = false;

        for(int i = 0; i < second.length(); i++)
        {
            good = false;
            for( int j = 0; j < source.length(); j++)
            {
                if(second.charAt(i)==source.charAt(j))
                {
                    good = true;
                    break;
                }
            }

        }

        return good;
    }

    private int pointerPosition;

    private String[] partsAlphabet;
    private String[] partsStates;
    private String[] partsFinish;

    private String inputString;
    private ArrayList<String> mainTape;
    private String inputSymbol;

    private String state;

    private String machineStatus;

    private JPanel bMain;
    private JPanel gNorth;

    private boolean exit;
    private boolean accepted;
    private boolean check;
    private boolean error;


    private JScrollPane scrollOutput;

    private JTextField inputStringField;
    private JTextArea output;

    private JScrollPane outputScroll;
}
