package com.company;

import com.google.gson.Gson;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Main
{
    static DefaultListModel<String> Hypotises;
    static DefaultListModel<String> AllQuestions;
    static DefaultListModel<String> AllQuestions2;
    static DefaultListModel<String> Answers;
    static BayesModule bm;
    static MkbToJson mtj;
    static int counter = 0;

    public static void main(String[] args)
    {
        MyFrame f = new MyFrame();
        MyDialog dialog = new MyDialog(f);
        dialog.setVisible(true);
        DataJSON dataJSON = null;
        MkbToJson jJson = null;
        try {
            jJson = new MkbToJson("Тельчики.mkb");
            dataJSON = new Gson().fromJson(new FileReader("KnowBase.json"), DataJSON.class);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        bm = new BayesModule();
        bm.setDataJSON(dataJSON);
        bm.setMin(dialog.getMin());
        bm.setMax(dialog.getMax());
        bm.calcAvg();
        f.setBoundsForInput(dialog.min, dialog.max);
        bm.initHypithes();
        refreshHypotises();
        JList list = new JList(Hypotises);
        list.setVisibleRowCount(0);
        f.h_scroll.setViewportView(list);
        f.h_scroll.repaint();
        String[] data2 = new String[bm.getHypotezP().size()];
        int i = 0;
        AllQuestions = new DefaultListModel<String>();
        for(String item : dataJSON.questins)
        {
            AllQuestions.add(i, item);
            i++;
        }
        JList list2 = new JList(AllQuestions);
        list2.setVisibleRowCount(0);
        f.q_scroll.setViewportView(list2);
        f.q_scroll.repaint();
        Answers = new DefaultListModel<String>();
        f.answersLabel3.setText(AllQuestions.getElementAt(0));
        f.btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!f.textArea.getText().equals("") && AllQuestions.size()>0)
                {
                    try {
                        if (Double.parseDouble(f.textArea.getText()) >= bm.getMin() && Double.parseDouble(f.textArea.getText()) <= bm.getMax()) {
                            bm.nexStep(Double.parseDouble(f.textArea.getText()));
                            Answers.add(counter, AllQuestions.getElementAt(0) + " - " + f.textArea.getText());
                            refreshScrollPane(Answers, f.a_scroll);
                            refreshHypotises();
                            refreshScrollPane(Hypotises, f.h_scroll);
                            f.repaint();
                            counter++;
                            AllQuestions.remove(0);
                            if (0 < AllQuestions.size())
                                f.answersLabel3.setText(AllQuestions.getElementAt(0));
                            else f.answersLabel3.setText("");
                        }
                    }
                    catch (NumberFormatException er)
                    {
                        System.out.print(er.getMessage());
                    }
                }
            }
        });
    }

    public static void refreshHypotises()
    {
        Hypotises = new DefaultListModel<String>();
        int i = 0;
        for(HypotezItem item : bm.getHypotezP())
        {
            Hypotises.add(i, "("+ String.format("%.4f", item.pi)+") - "+ item.name);
            i++;
        }
        List<String> list = new ArrayList<>();
        for (i = 0; i < Hypotises.size(); i++) {
            list.add(Hypotises.get(i));
        }
        Collections.sort(list, Collections.reverseOrder());
        Hypotises.removeAllElements();
        for (String s : list) {
            Hypotises.addElement(s);
        }
    }


    public static void refreshScrollPane(DefaultListModel<String> dlm, JScrollPane scroll)
    {
        JList list = new JList(dlm);
        list.setVisibleRowCount(0);
        scroll.setViewportView(list);
    }
}
class MyDialog extends JDialog
{
    public double min;
    public double max;
    public MyDialog(JFrame owner)
    {

        super(owner, "MyDialog", true);
        owner.setDefaultCloseOperation(3);
        setLayout(null);
        JLabel l1 = new JLabel("Введите мин и макс");
        l1.setBounds(65,5, 150, 20);
        add(l1);
        JTextArea t1 =new JTextArea();
        t1.setBounds(65,30, 50, 20);
        add(t1);
        JTextArea t2 =new JTextArea();
        t2.setBounds(120,30, 50, 20);
        add(t2);

        JButton ok = new JButton("ok");
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event)
            {
                String str1 = t1.getText();
                String str2 = t2.getText();
                if(!str1.equals("") && !str2.equals(""))
                {
                    try
                    {
                        min = Double.parseDouble(str1);
                        max = Double.parseDouble(str2);
                        setVisible(false);
                    }
                    catch (NumberFormatException e)
                    {
                        System.out.print(e.getMessage());
                    }
                }
            }
        });
        JPanel panel = new JPanel();
        panel.add(ok);
        panel.setBounds(70,50, 50, 40);
        add(panel);
        setBounds(300, 300,260, 160);
    }
    public double getMin()
    {
        return min;
    }
    public double getMax()
    {
        return max;
    }
}
